package com.opengms.wukai.pojo.Result.Spatial;

import lombok.Data;

@Data
public class ArcmapForResult {
    private String GoalX;
    private String GoalY;
    private int NumberForMultiScale_All;
    private int NumberForDifferent_All;
    private double RatioForDifferent_All;
    private int NumberForMultiScale_Global;
    private int NumberForDifferent_Global;
    private double RatioForDifferent_Global;
    private int NumberForMultiScale_Region;
    private int NumberForDifferent_Region;
    private double RatioForDifferent_Region;
    private int NumberForMultiScale_Country;
    private int NumberForDifferent_Country;
    private double RatioForDifferent_Country;

    private int OriginalType;
    private double OriginalStrength;
    private int ChangeType;
    private double ChangeStrength;

    public ArcmapForResult(String goalX,String goalY){
        this.GoalX=goalX;
        this.GoalY=goalY;
    }



}
