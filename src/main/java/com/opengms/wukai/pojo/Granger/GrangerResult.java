package com.opengms.wukai.pojo.Granger;


import com.opengms.wukai.pojo.Indicators.Indicator;
import dro.stat.GrangerCausalIndicator;
import lombok.Data;
import lombok.ToString;
import org.apache.xmlbeans.impl.store.Public2;

import javax.print.DocFlavor;


@Data
public class GrangerResult {
    //指标的一些计算结果
    private String GoalX;
    private String IndicatorX;
    private String StringCodeX;
    private double ADFFTestX;
    private double ADFPValueX;
    private int orderX;

    private String GoalY;
    private String IndicatorY;
    private String StringCodeY;
    private double ADFFTestY;
    private double ADFPValueY;
    private int orderY;

    //Granger的结果
    //private double GrangerFTest;
    private double GrangerPValue;
    private double[] GrangerParameters;
    private String GrangerParametersS="";
    private int Interaction;

    private double StrengthXY;
    private double R2XY;
    private double Pvalue;


    //构造函数

    public GrangerResult(String goalX, String indicatorX, String SeriesCodeX,double ADFFTestX,double ADFPValueX,
                         String goalY, String indicatorY, String SeriesCodeY,double ADFFTestY,double ADFPValueY,
                         double grangerPValue,double[] grangerParameters,
                         int orderX,int orderY,String directionx,String directiony){
        this.GoalX=goalX;
        this.IndicatorX=indicatorX;
        this.StringCodeX=SeriesCodeX;
        this.ADFFTestX=ADFFTestX;
        this.ADFPValueX=ADFPValueX;
        this.orderX=orderX;
        this.GoalY=goalY;
        this.IndicatorY=indicatorY;
        this.StringCodeY=SeriesCodeY;
        this.ADFFTestY=ADFFTestY;
        this.ADFPValueY=ADFPValueY;
        this.orderY=orderY;
        this.GrangerPValue=grangerPValue;
        this.GrangerParameters=grangerParameters;
        for(int i=0;i<grangerParameters.length;i++){
            GrangerParametersS=GrangerParametersS+"a"+i+":"+grangerParameters[i]+";";
        }
        this.orderY=orderY;
        if (directionx.equals("Middle")||directiony.equals("Middle")){
            this.GrangerPValue=1;
        }
        if(directionx.equals(directiony)){
            this.Interaction=1;
        }else{
            this.Interaction=-1;
        }


    }





}
