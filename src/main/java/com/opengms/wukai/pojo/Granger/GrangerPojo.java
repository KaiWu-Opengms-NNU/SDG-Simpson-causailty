package com.opengms.wukai.pojo.Granger;

import dro.stat.GrangerCausalIndicator;
import dro.stat.GrangerCausality;
import dro.stat.GrangerCausalityStrategy_Bivariate;
import lombok.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data
public class GrangerPojo {
    //输入的一些属性
    //地区代码
    private String GeaArea;
    //地区名称
    private String GeoAreaName;

    /*X指标的一些属性
    名称,时间,值,阶数*/

    private String GoalX;
    private String IndicatorX;
    private String StringCodeX;
    private double[] TimeX;
    private double[] ValueX;
    private double ADFFTestX;
    private double ADFPValueX;
    private int OrderX=0;
    private String DirectionX;

    /*y指标的一些属性
    名称,时间,值,阶数*/
    private String GoalY;
    private String IndicatorY;
    private String StringCodeY;
    private double[] TimeY;
    private double[] ValueY;
    private double ADFFTestY;
    private double ADFPValueY;
    private int OrderY=0;
    private String DirectionY;

    //输出的一些指标
    private GrangerCausalIndicator GrangerResult;

    //构造函数待定
    //无参
    public GrangerPojo(){}

    //有参（两个时间序列，ADF解耦）
    public GrangerPojo(
            String goalX,String goalY,
            String indicatorX,String indicatorY,
            String stringCodeX, String stringCodeY,
            double[] inputTimeX, double[] inputTimeY, double[] inputValueX, double[] inputValueY,
            double ADFFTestX,double ADFPValueX,double ADFFTestY,double ADFPValueY,
            int orderX,int orderY,String directionX,String directionY){
        this.GoalX=goalX;
        this.GoalY=goalY;
        this.IndicatorX=indicatorX;
        this.IndicatorY=indicatorY;
        this.StringCodeX=stringCodeX;
        this.StringCodeY=stringCodeY;
        this.TimeX=inputTimeX;
        this.TimeY=inputTimeY;
        this.ValueX=inputValueX;
        this.ValueY=inputValueY;
        this.ADFFTestX=ADFFTestX;
        this.ADFPValueX=ADFPValueX;
        this.ADFFTestY=ADFFTestY;
        this.ADFPValueY=ADFPValueY;
        this.OrderX=orderX;
        this.OrderY=orderY;
        this.DirectionX=directionX;
        this.DirectionY=directionY;
    }

    //考虑到X指标和Y指标时间不一致的情况需要进行插值,统一插值到00-20区间
    //拉格朗日插值法
    private double LagrangeInterpolation(double X, int n, double x[], double y[]) {
        double Y = 0;
        for (int k = 0; k <= n; k++) {
            double t = 1;
            for (int j = 0; j <=n; j++) {
                if (j == k) {
                    j = k + 1;
                    if (j > n) {
                        break;
                    }
                }
                t *= (X - x[j]) / (x[k] - x[j]);
            }
            Y = Y + t * y[k];
        }
        return Y;
    }

    //分段线性插值(包含内插和外插）
    private double LinearInterpolation(double X0, int n,double x[],double y[]){
        double Y=0;
        double temp=100;
        int Index=0;
        //找出离插值点最近的点
        for(int i=0;i<n;i++){
            if(Math.abs(x[i]-X0) <temp){
                temp=Math.abs(x[i]-X0);
                Index=i;
            }
        }
        if(x[Index]==x[0]){
            Y=((y[Index+1]-y[Index])/(x[Index+1]-x[Index]))*(X0-x[Index])+y[Index];
        }else if(x[Index]==x[n-1]){
            Y=((y[Index]-y[Index-1])/(x[Index]-x[Index-1]))*(X0-x[Index])+y[Index];
        }else{
            if(X0>x[Index]){
                Y=((y[Index+1]-y[Index])/(x[Index+1]-x[Index]))*(X0-x[Index])+y[Index];
            }else {
                Y=((y[Index]-y[Index-1])/(x[Index]-x[Index-1]))*(X0-x[Index])+y[Index];
            }
        }
        return Y;
    }

    //ADF检验,String[0]表示ADF F检验,String[1]表示ADF P值
    public String[] ADFTest(int n,double[] data){
        Process proc;
        try {
            String args[]=new String[n+2];
            args[0]="python";
            args[1]="G:\\WukaiBag\\Year2022\\sustainableDevelopmentPaper\\coding\\JavaProgram\\src\\main\\resources\\ADF.py";
            //组合参数
            for(int i=2;i<n+2;i++){
                args[i]=String.valueOf(data[i-2]);
            }

            proc = Runtime.getRuntime().exec(args);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                String[] SplitLine=line.split("[,;(;)]");
                return new String[]{SplitLine[1],SplitLine[2]};
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //格兰杰因果关系执行,未做ADF检验
    public GrangerCausalIndicator DoGrangerCasual(double[] valueY,double[] valueX, int lagSize){
        GrangerCausality Granger1=new GrangerCausalityStrategy_Bivariate(lagSize);
        //做完差分之后得再看看两个数组的长度是否一致
        GrangerCausalIndicator result;
        if(this.OrderX!=this.OrderY){
            if(this.OrderX>this.OrderY){
                double[] adjustValueY=new double[valueY.length+this.OrderY-this.OrderX];
                for (int i=0;i<valueY.length+this.OrderY-this.OrderX;i++){
                    adjustValueY[i]=valueY[i];
                }
                result=Granger1.apply(adjustValueY,valueX);
            }else{
                double[] adjustValueX=new double[valueX.length+this.OrderX-this.OrderY];
                for(int i=0;i<valueX.length+this.OrderX-this.OrderY;i++){
                    adjustValueX[i]=valueX[i];
                }
                result=Granger1.apply(valueY,adjustValueX);
            }

        }else{
            result=Granger1.apply(valueY,valueX);
        }
        return result;
    }

}
