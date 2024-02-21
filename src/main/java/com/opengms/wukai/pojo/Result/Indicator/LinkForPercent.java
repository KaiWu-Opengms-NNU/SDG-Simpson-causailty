package com.opengms.wukai.pojo.Result.Indicator;

import lombok.Data;

@Data
public class LinkForPercent {
    private int ID;
    private int parentID;
    private int level;
    private String IndicatorX;
    private String IndicatorY;
    private double percent;

}
