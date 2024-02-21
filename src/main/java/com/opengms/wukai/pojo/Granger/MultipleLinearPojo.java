package com.opengms.wukai.pojo.Granger;

import cointegration.MultipleLinearRegression;
import cointegration.StatisticsUtil;
import com.opengms.wukai.pojo.SingleSeries.YearRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 存贮多元线性回归的结果
 * @Author  Kai
 * @Date 2023/2/4
 */

@Data
public class MultipleLinearPojo implements Comparable<MultipleLinearPojo> {
    private final int yearLength=16;
    //记录目标因果
    private String GoalY;
    private List<String> GoalX;
    //记录指标因果
    private String StringIndicatorY;
    private List<String> StringIndicatorX;
    //记录指标因果
    private String StringCodeY;
    private List<String> StringCodeX;
    //记录阶数
    private int OrderY;
    private int[] OrderX;
    //记录原始数据
    private double[] StringCodeYData;
    private double[][] StringCodeXData;
    private int XDataIndex;
    //记录做完差分之后的数据
    private double[] DiffYData;
    private double[][] DiffXData;
    //记录结果数据
    private MultipleLinearRegression MulRes;
    private double ADFPvalue;
    private Boolean ADFJudge;

    //初始化数据
    public MultipleLinearPojo(){
        this.GoalX=new ArrayList<>();
        this.StringIndicatorX=new ArrayList<>();
        this.StringCodeX=new ArrayList<>();
    }

    //初始化计算Y数据资源
    public void SetStringCodeYData(List<YearRecord> InputData){
        //先把读取的tempI转化成数组
        double[] TempTimeY=new double[InputData.size()];
        double[] TempValueY=new double[InputData.size()];
        for(int tempI=0;tempI<InputData.size();tempI++){
            String TempYear=InputData.get(tempI).getYear();
            TempYear=TempYear.substring(1,5);
            TempTimeY[tempI]=Double.parseDouble(TempYear);
            TempValueY[tempI]=Double.parseDouble(InputData.get(tempI).getValue());
        }
        //通过线性插值统一把数值范围插值到2000到2020年
        StringCodeYData=new double[yearLength];
        for(int Yi=0;Yi<StringCodeYData.length;Yi++){
            this.StringCodeYData[Yi]=LinearInterpolation(2005+Yi,TempTimeY.length,TempTimeY,TempValueY);
        }
    }

    //初始化X二维数组的长度
    public void SetStringCodeXDataNumber(){
        if (StringCodeX.size()!=0){
            this.StringCodeXData=new double[StringCodeX.size()][];
            this.OrderX=new int[StringCodeX.size()];
            this.XDataIndex=0;
        }else {
            System.out.println("X元素为空");
        }
    }

    //载入X二维数组的每一个列表长度
    public void SetStringCodeXData(List<YearRecord>InputData){
        //先把读取的tempI转化成数组
        double[] TempTimeX=new double[InputData.size()];
        double[] TempValueX=new double[InputData.size()];
        for(int tempI=0;tempI<InputData.size();tempI++){
            String TempYear=InputData.get(tempI).getYear();
            TempYear=TempYear.substring(1,5);
            TempTimeX[tempI]=Double.parseDouble(TempYear);
            TempValueX[tempI]=Double.parseDouble(InputData.get(tempI).getValue());
        }
        this.StringCodeXData[XDataIndex]=new double[yearLength];
        for (int Xi=0;Xi<StringCodeXData[XDataIndex].length;Xi++){
            this.StringCodeXData[XDataIndex][Xi]=LinearInterpolation(2005+Xi,TempTimeX.length,TempTimeX,TempValueX);
        }
        //不要忘了载入成功之后把游标后移一位；
        XDataIndex++;
    }

    //载入完数据之后需要进行计算
    public void MultipleLinearCalcu(){
        //需要计算所有X指标中是否有共线性
        List<String> JudgeXList=new ArrayList<>();
        for (int Xi=0;Xi<StringCodeX.size()-1;Xi++){
            int StateJudge=0;
            for(int Xj=Xi+1;Xj<StringCodeX.size();Xj++){
                double[] DataOfXI= StringCodeXData[Xi];
                double[] DataOfXJ= StringCodeXData[Xj];
                double[] Validation=new double[yearLength];
                for(int Vi=0;Vi<DataOfXI.length;Vi++){
                    if(DataOfXI[Vi]!=0){
                        Validation[Vi]=DataOfXJ[Vi]/DataOfXI[Vi];
                    }else{
                        Validation[Vi]=0;
                    }
                }

                for(int Vi=0;Vi<Validation.length-1;Vi++){
                    if (Math.abs(Validation[Vi]-Validation[Vi+1])<0.001){
                         StateJudge=1;
                    }
                }
            }
            if(StateJudge==1){
                JudgeXList.add("1");
            }else {
                JudgeXList.add("0");
            }
        }
        JudgeXList.add("0");
        //删除重复要素
        for(int Di=StringCodeX.size()-1;Di>=0;Di--){
            if(JudgeXList.get(Di).equals("1")){
                //删除x元素
                this.XDataIndex--;
                this.GoalX.remove(Di);
                this.StringIndicatorX.remove(Di);
                this.StringCodeX.remove(Di);
                //删除x数据
                double [][] TempXData=new double[XDataIndex][];
                for(int tempIndex=0;tempIndex<XDataIndex;tempIndex++){
                    if(tempIndex<Di){
                        TempXData[tempIndex]=StringCodeXData[tempIndex];
                    }else{
                        TempXData[tempIndex]=StringCodeXData[tempIndex+1];
                    }
                }
                this.StringCodeXData=TempXData;
            }
        }
        //需要进行矩阵转置
        double[][] StringCodeXDataForCal=new double[yearLength][];
        for (int i=0;i<yearLength;i++){
            StringCodeXDataForCal[i]=new double[StringCodeX.size()];
            for(int j=0;j<StringCodeX.size();j++){
                StringCodeXDataForCal[i][j]=this.StringCodeXData[j][i];
            }
        }
        this.MulRes=new MultipleLinearRegression();
        this.MulRes.addData(StringCodeXDataForCal,this.StringCodeYData,this.StringCodeX.size());
        System.out.println(this.MulRes.getFunction());
        System.out.println("标准误： "  + this.MulRes.getStdErrors()[0]);
        System.out.println("R方 : " + this.MulRes.getRSquared());
        System.out.println("调整后R方 :" + this.MulRes.getAdjRSquared());
        System.out.println("f值：" + this.MulRes.getFValue()) ;
        System.out.println("f检验P值:" + StatisticsUtil.getPValue(this.MulRes.getFValue(), this.MulRes.getDfIndependent(), this.MulRes.getDfDependent()));
    }
    //载入阶数数组
    public void SetOrderX(int number){
        this.OrderX[XDataIndex]=number;
    }


    //进行回归计算，但是这次带上了阶数，2.0版
    public void MultipleLinearCalcu2(){
        //首先差分Y数据
        this.DiffYData=diffArray(this.StringCodeYData,this.OrderY);
        //其次差分X数据
        this.DiffXData=new double[StringCodeX.size()][];
        for(int i=0;i<StringCodeX.size();i++){
            this.DiffXData[i]=diffArray(this.StringCodeXData[i],OrderX[i]);
        }
        double[] StringCodeYDataForCal=new double[19];
        for (int i=0;i<19;i++){
            StringCodeYDataForCal[i]=this.DiffYData[i];
        }
        //由于进行了最高为2阶的差分，在2000到2018年尺度上进行转置
        double[][] StringCodeXDataForCal=new double[19][];
        for (int i=0;i<19;i++){
            StringCodeXDataForCal[i]=new double[StringCodeX.size()];
            for(int j=0;j<StringCodeX.size();j++){
                StringCodeXDataForCal[i][j]=this.DiffXData[j][i];
            }
        }
        this.MulRes=new MultipleLinearRegression();
        this.MulRes.addData(StringCodeXDataForCal,StringCodeYDataForCal,this.StringCodeX.size());
        System.out.println(this.MulRes.getFunction());
        System.out.println("标准误： "  + this.MulRes.getStdErrors()[0]);
        System.out.println("R方 : " + this.MulRes.getRSquared());
        System.out.println("调整后R方 :" + this.MulRes.getAdjRSquared());
        System.out.println("f值：" + this.MulRes.getFValue()) ;
        System.out.println("f检验P值:" + StatisticsUtil.getPValue(this.MulRes.getFValue(), this.MulRes.getDfIndependent(), this.MulRes.getDfDependent()));


    }
    //协整检验
    public void CointegrationCal(){
        double[] params=new double[StringCodeX.size()+1];
        for(int i=0;i<params.length;i++){
            params[i]=this.MulRes.getParameters()[i];
        }
        //获取残差
        double[] ResidualError=new double[yearLength];
        for(int i=0;i<yearLength;i++){
            double SimulationValue=0;
            for(int j=0;j< StringCodeX.size();j++){
                SimulationValue=SimulationValue+params[j]*this.StringCodeXData[j][i];
            }
            //加上常数项
            SimulationValue=SimulationValue+params[StringCodeX.size()];
            ResidualError[i]=Math.abs(this.StringCodeYData[i]-SimulationValue);
            System.out.println(ResidualError[i]);
        }
        String[] ADFResult=new ADFPojo().ADFTest(ResidualError.length,ResidualError);
        double ADFPValue;
        try{
            ADFPValue=Double.parseDouble(ADFResult[1]);
            this.ADFPvalue=ADFPValue;
        }catch (Exception e){
            System.out.println("协整检验失败");
            this.ADFJudge=false;
        }
        if(this.ADFPvalue>0.05){
            System.out.println("协整检验失败");
            this.ADFJudge=false;
        }else{
            System.out.println("协整检验成功");
            this.ADFJudge=true;
        }
    }

    //协整检验2.0
    public void CointegrationCal2(){
        double[] params=new double[StringCodeX.size()+1];
        for(int i=0;i<params.length;i++){
            params[i]=this.MulRes.getParameters()[i];
        }
        //获取残差
        double[] ResidualError=new double[19];
        for(int i=0;i<19;i++){
            double SimulationValue=0;
            for(int j=0;j<StringCodeX.size();j++){
                SimulationValue=SimulationValue+params[j]*this.DiffXData[j][i];
            }
            //加上常数项
            SimulationValue=SimulationValue+params[StringCodeX.size()];
            ResidualError[i]=Math.abs(this.DiffYData[i]-SimulationValue);
            System.out.println(ResidualError[i]);
        }
        String[] ADFResult=new ADFPojo().ADFTest(ResidualError.length,ResidualError);
        double ADFPValue;
        try{
            ADFPValue=Double.parseDouble(ADFResult[1]);
            this.ADFPvalue=ADFPValue;
        }catch (Exception e){
            System.out.println("协整检验失败");
            this.ADFJudge=false;
        }
        if(this.ADFPvalue>0.05){
            System.out.println("协整检验失败");
            this.ADFJudge=false;
        }else{
            System.out.println("协整检验成功");
            this.ADFJudge=true;
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

    //排序算法
    @Override
    public int compareTo(MultipleLinearPojo O){
        int result=(int)(this.getMulRes().getAdjRSquared()-O.getMulRes().getAdjRSquared());
        return result;
    }
}
