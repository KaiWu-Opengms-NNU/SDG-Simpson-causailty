package com.opengms.wukai.pojo.Granger;

import lombok.Data;

@Data
public class GrangerResultGoal {
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
    private double GrangerPValue;
    private int Interaction;

    private double StrengthXY;
    private double R2XY;
    private double Pvalue;
    private String IndicatorInteractionType;
    private double IndicatorInteractionPercent;
    private String GoalInteractionType;
    private double GoalInteractionPercent;

    public GrangerResultGoal(GrangerResult grangerResult){
        this.GoalX=grangerResult.getGoalX();
        this.IndicatorX=grangerResult.getIndicatorX();
        this.StringCodeX=grangerResult.getStringCodeX();
        this.ADFFTestX=grangerResult.getADFFTestX();
        this.ADFPValueX=grangerResult.getADFPValueX();
        this.orderX=grangerResult.getOrderX();
        this.GoalY=grangerResult.getGoalY();
        this.IndicatorY=grangerResult.getIndicatorY();
        this.StringCodeY=grangerResult.getStringCodeY();
        this.ADFFTestY=grangerResult.getADFFTestY();
        this.ADFPValueY=grangerResult.getADFPValueY();
        this.orderY=grangerResult.getOrderY();
        this.GrangerPValue=grangerResult.getGrangerPValue();
        this.Interaction=grangerResult.getInteraction();
        this.StrengthXY=grangerResult.getStrengthXY();
        this.R2XY=grangerResult.getR2XY();
        this.Pvalue=grangerResult.getPvalue();
    }

    public GrangerResultGoal(String goalX,String goalY){
        this.GoalX=goalX;
        this.GoalY=goalY;
        this.IndicatorX="";
        this.StringCodeX="";
        this.ADFFTestX=0;
        this.orderX=0;
        this.IndicatorY="";
        this.StringCodeY="";
        this.ADFFTestY=0;
        this.ADFPValueY=0;
        this.GrangerPValue=0;
        this.Interaction=0;
        this.StrengthXY=0;
        this.R2XY=0;
        this.Pvalue=-100;
    }



}
