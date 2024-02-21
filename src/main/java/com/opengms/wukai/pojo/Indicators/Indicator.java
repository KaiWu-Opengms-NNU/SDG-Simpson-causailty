package com.opengms.wukai.pojo.Indicators;

import lombok.Data;


@Data
public class Indicator {
    private String goal;
    private String target;
    private String code;
    private String series;
    private String description;
    private String direction;
    private double AreasNumber;
    private double TemporalNumber;
    private String unit;

}
