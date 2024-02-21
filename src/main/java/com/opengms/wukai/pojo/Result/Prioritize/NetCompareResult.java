package com.opengms.wukai.pojo.Result.Prioritize;

import lombok.Data;

@Data
public class NetCompareResult {
    private String Goal;
    private int Scale;
    private int Interaction;
    private double WeightInDegree;
    private double WeightOutDegree;
    private double BetweenessCentrality;
    private double EigenCentrality;

}
