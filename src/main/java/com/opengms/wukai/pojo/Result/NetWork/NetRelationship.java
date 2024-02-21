package com.opengms.wukai.pojo.Result.NetWork;

import lombok.Data;

@Data
public class NetRelationship {
    private String GoalX;
    private String GoalY;
    private double Strength;
    private String Type;
    private String AreaName;
    private int Scale;
}
