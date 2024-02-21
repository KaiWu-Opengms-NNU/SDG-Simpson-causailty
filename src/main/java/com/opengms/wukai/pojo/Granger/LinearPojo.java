package com.opengms.wukai.pojo.Granger;


import cointegration.SimpleLinearRegression;
import com.opengms.wukai.pojo.CountryRecordIndicator;
import com.opengms.wukai.pojo.SingleSeries.YearRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author  Kai
 * @Date 2022/11/10
 */
@Data
public class LinearPojo {
    private int StartYear;
    private int EndYear;
    private String GoalX;
    private String IndicatorX;
    private String SeriesCodeX;
    private double[] ValueX;
    private int OrderX;
    private String GoalY;
    private String IndicatorY;
    private String SeriesCodeY;
    private double[] ValueY;
    private int OrderY;


    //记录存贮结果
    private SimpleLinearRegression simpleLinearRegressionX_Y;
    private SimpleLinearRegression simpleLinearRegressionY_X;



    //载入属性数据
    public LinearPojo(int StartYear,int EndYear,
                      String GoalX, String IndicatorX, String SeriesCodeX, int OrderX,
                      String GoalY, String IndicatorY, String seriesCodeY, int OrderY ){
        //载入属性数据
        this.StartYear=StartYear;
        this.EndYear=EndYear;
        this.GoalX=GoalX;
        this.GoalY=GoalY;
        this.IndicatorX=IndicatorX;
        this.IndicatorY=IndicatorY;
        this.SeriesCodeX=SeriesCodeX;
        this.SeriesCodeY=seriesCodeY;
        this.OrderX=OrderX;
        this.OrderY=OrderY;
        //载入回归数据
        this.ValueX=new double[EndYear-StartYear+1];
        this.ValueY=new double[EndYear-StartYear+1];

    }

    public void InsertData(CountryRecordIndicator countryRecord){
        int XIndex=0;
        int YIndex=0;
        for(int i=0;i<countryRecord.getSeriesCode().size();i++){
            if(countryRecord.getSeriesCode().get(i).equals(this.SeriesCodeX)){
                 XIndex=i;
            }
            if(countryRecord.getSeriesCode().get(i).equals(this.SeriesCodeY)){
                 YIndex=i;
            }
        }
       List<YearRecord> InputDataX=countryRecord.getData().get(XIndex);
        double[] TempTimeX=new double[InputDataX.size()];
        double[] TempValueX=new double[InputDataX.size()];
        for(int tempI=0;tempI<InputDataX.size();tempI++){
            String TempYear=InputDataX.get(tempI).getYear();
            TempYear=TempYear.substring(1,5);
            TempTimeX[tempI]=Double.parseDouble(TempYear);
            TempValueX[tempI]=Double.parseDouble(InputDataX.get(tempI).getValue());
        }
        for(int Xi=0;Xi<this.ValueX.length;Xi++){
            this.ValueX[Xi]=LinearInterpolation(this.StartYear+Xi,TempTimeX.length,TempTimeX,TempValueX);
        }
       List<YearRecord> InputDataY=countryRecord.getData().get(YIndex);
        //先把读取的tempI转化成数组
       double[] TempTimeY= new double[InputDataY.size()];
       double[] TempValueY=new double[InputDataY.size()];
       for(int tempI=0;tempI<InputDataY.size();tempI++){
            String TempYear=InputDataY.get(tempI).getYear();
            TempYear=TempYear.substring(1,5);
            TempTimeY[tempI]=Double.parseDouble(TempYear);
            TempValueY[tempI]=Double.parseDouble(InputDataY.get(tempI).getValue());
       }
        for(int Yi=0;Yi<ValueY.length;Yi++){
           this.ValueY[Yi]=LinearInterpolation(StartYear+Yi,TempTimeY.length,TempTimeY,TempValueY);
        }
    }


    //进行线性回归X到Y
    public void DoSimpleLinearRegressionXtoY() {
        double[][]LinearData=new double[ValueX.length][];
        for(int i=0;i<ValueX.length;i++){
            LinearData[i]=new double[2];
            LinearData[i][0]=this.ValueX[i];
            LinearData[i][1]=this.ValueY[i];
        }
        SimpleLinearRegression simpleLinearRegression=new SimpleLinearRegression();
        simpleLinearRegression.addData(LinearData);
        this.simpleLinearRegressionX_Y=simpleLinearRegression;
    }

    //进行线性回归Y到X
    public void DosimpleLinearRegressionYtoX() {
        double[][]LinearData=new double[ValueX.length][];
        for(int i=0;i<ValueX.length;i++){
            LinearData[i]=new double[2];
            LinearData[i][0]=this.ValueY[i];
            LinearData[i][1]=this.ValueX[i];
        }
        SimpleLinearRegression simpleLinearRegression=new SimpleLinearRegression();
        simpleLinearRegression.addData(LinearData);
        this.simpleLinearRegressionY_X=simpleLinearRegression;
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
}
