package com.opengms.wukai.pojo.Result.Prioritize;

import com.opengms.wukai.pojo.Result.Indicator.LinkForPercent;
import lombok.Data;

@Data
public class GoalCompareResult {
    private int GoalX;
    private int GoalY;
    private int Scale;
    private double AllStrengthOfPair;//不同尺度上的分目标对强度
    private double AverageRSquare;

    public GoalCompareResult(){}

    public void setGoalPair(int x,int y){
        this.GoalX=x;
        this.GoalY=y;
    }

}
