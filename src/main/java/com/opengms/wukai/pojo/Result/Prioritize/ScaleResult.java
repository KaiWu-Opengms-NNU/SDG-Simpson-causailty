package com.opengms.wukai.pojo.Result.Prioritize;

import com.opengms.wukai.pojo.Result.ScaleTree2;
import lombok.Data;

@Data
public class ScaleResult {
    private int ID;//本身的序号ID
    private String Name;
    private String Short;
    private int ParentID;//父区域的序号ID,如果没有父层级则设为0
    private int level;//树的层级
    private double SDG1;
    private double SDG2;
    private double SDG3;
    private double SDG4;
    private double SDG5;
    private double SDG6;
    private double SDG7;
    private double SDG8;
    private double SDG9;
    private double SDG10;
    private double SDG11;
    private double SDG12;
    private double SDG13;
    private double SDG14;
    private double SDG15;
    private double SDG16;
    private double SDG17;

    private String MaxLabel;
    private double MaxStrength;

    public ScaleResult(ScaleTree2 data){
        this.ID=data.getID();
        this.Name=data.getName();
        this.Short=data.getShort();
        this.ParentID=data.getParentID();
        this.level=data.getLevel();
        this.SDG1=data.getSDGPrioritizeList()[0];
        this.SDG2=data.getSDGPrioritizeList()[1];
        this.SDG3=data.getSDGPrioritizeList()[2];
        this.SDG4=data.getSDGPrioritizeList()[3];
        this.SDG5=data.getSDGPrioritizeList()[4];
        this.SDG6=data.getSDGPrioritizeList()[5];
        this.SDG7=data.getSDGPrioritizeList()[6];
        this.SDG8=data.getSDGPrioritizeList()[7];
        this.SDG9=data.getSDGPrioritizeList()[8];
        this.SDG10=data.getSDGPrioritizeList()[9];
        this.SDG11=data.getSDGPrioritizeList()[10];
        this.SDG12=data.getSDGPrioritizeList()[11];
        this.SDG13=data.getSDGPrioritizeList()[12];
        this.SDG14=data.getSDGPrioritizeList()[13];
        this.SDG15=data.getSDGPrioritizeList()[14];
        this.SDG16=data.getSDGPrioritizeList()[15];
        this.SDG17=data.getSDGPrioritizeList()[16];
        int Index=-1;
        double TempleData=-9999;
        for(int i=0;i<data.getSDGPrioritizeList().length;i++){
            if(data.getSDGPrioritizeList()[i]>TempleData){
                Index=i+1;
                TempleData=data.getSDGPrioritizeList()[i];
            }
        }
        this.MaxLabel="SDG "+Index;
        this.MaxStrength=TempleData;

    }
}
