package com.opengms.wukai.pojo.Granger;

import lombok.Data;

@Data
public class LinearResult {
    //属性数据
    private String GoalX;
    private String IndicatorX;
    private String SeriesCodeX;
    private int OrderX=0;
    private String GoalY;
    private String IndicatorY;
    private String SeriesCodeY;
    private int OrderY=0;

    //协整结果
    private double Rsquare;
    private double AdjustRsquare;
    private double ADFPvalue;

    public LinearResult(
            String GoalX, String IndicatorX, String SeriesCodeX, int OrderX,
            String GoalY, String IndicatorY, String seriesCodeY, int OrderY,
            double rsquare,double adjustRsquare, double ADFPvalue
    ){
        this.GoalX=GoalX;
        this.IndicatorX=IndicatorX;
        this.SeriesCodeX=SeriesCodeX;
        this.OrderX=OrderX;
        this.GoalY=GoalY;
        this.IndicatorY=IndicatorY;
        this.SeriesCodeY=seriesCodeY;
        this.OrderY=OrderY;
        this.Rsquare=rsquare;
        this.AdjustRsquare=adjustRsquare;
        this.ADFPvalue=ADFPvalue;
    }
}
