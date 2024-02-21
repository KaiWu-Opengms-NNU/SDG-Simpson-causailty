package com.opengms.wukai.pojo.Granger;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data
public class ADFPojo {
    private String Goal;
    private String Series;
    private String indicator;
    private double[] Time;
    private double[] Value;
    private double ADFFTest;
    private double ADFPValue;
    private String Direction;
    private int Order=0;

    private String ADFResult;

    public ADFPojo(){}

    public ADFPojo(int StartYear,int EndYear,String goal,String indicator,String stringSeries,double[] inputTime,double[] inputValue,String direction){
        this.Goal=goal;
        this.indicator =indicator;
        this.Series =stringSeries;
        this.Time=new double[EndYear-StartYear+1];
        this.Value=new double[EndYear-StartYear+1];
        for(int i=0;i<Time.length;i++){
            this.Time[i]=StartYear+i;
        }
        for(int i=0;i<Value.length;i++){
            Value[i]=LinearInterpolation(StartYear+i,inputTime.length,inputTime,inputValue);
        }
        this.Direction=direction;
    }

    //选择不同的时间尺度
    public void SelectTimeScale(int TempeScale){
        int length=this.Time.length/TempeScale;
        double[] newTime=new double[length];
        double[] newValue=new double[length];
        for(int i=0;i<length;i++){
            newTime[i]=Time[TempeScale*i];
            newValue[i]=Value[TempeScale*i];
        }
        this.Time=newTime;
        this.Value=newValue;

    }


    public boolean ADFJudge(double[] valueX){
        double PValueOfADFX=1;
        int PhaseDown=0;
        //循环差分X数组
        while(PValueOfADFX>0.05&&PhaseDown<2){
            valueX=diffArray(valueX,PhaseDown);
            String[] ADFResultX=ADFTest(valueX.length,valueX);
            try{
                this.ADFFTest=Double.parseDouble(ADFResultX[0]);
                this.ADFPValue=Double.parseDouble(ADFResultX[1]);
            }catch (Exception e){
                return false;
            }
            if(Double.parseDouble(ADFResultX[1])<PValueOfADFX){
                PValueOfADFX=Double.parseDouble(ADFResultX[1]);
            }
            this.Order=PhaseDown;
            PhaseDown++;
        }
        if(PValueOfADFX>0.05||PValueOfADFX==0){
            return false;
        }else{
            //需要截取年份数据
            double[] tempYear=new double[valueX.length];
            for(int i=0;i<tempYear.length;i++){
                tempYear[i]=this.Time[i];
            }
            this.Time=tempYear;
            this.Value=valueX;
            return true;
        }
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
            args[1]="G:\\WukaiBag\\Year2023\\sustainableofpaper2023\\coding\\JavaProgram\\src\\main\\resources\\ADF.py";
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
            this.ADFResult=line;
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //数组差分算法
    private double[] diffArray(double[] originalArray, int order){
        if(order==0){
            return originalArray;
        }
        else {
            double[] diffArray = new double[originalArray.length - 1];
            for (int i = 0; i < diffArray.length; i++) {
                diffArray[i] = originalArray[i + 1] - originalArray[i];
            }
            return diffArray;
        }
    }
}
