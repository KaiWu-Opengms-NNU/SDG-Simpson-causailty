package com.opengms.wukai.dto;

import lombok.Data;

@Data
public class GraphDto {
    private String StringCodeX;
    private String StringCodeY;
    private double GrangerPValue;

    public GraphDto(){
        this.GrangerPValue=0;
    }
}
