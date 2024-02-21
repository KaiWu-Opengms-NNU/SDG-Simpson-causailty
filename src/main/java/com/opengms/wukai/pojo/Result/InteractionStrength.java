package com.opengms.wukai.pojo.Result;


import lombok.Data;

import java.util.List;
import java.util.Random;


@Data
public class InteractionStrength {

    private int countryCode;
    private String countryName;
    private String countryAb;
    private int[] ChildrenID;//子区域的序号ID组
    private int ParentID;//父区域的序号ID,如果没有父层级则设为0
    private int level;//树的层级
    private int SdgX;//原因目标
    private int SdgY;//结果目标
    private int Length;//作用个数
    private double Angle;//平均作用强度
    private int Area;//总作用强度

    //无参构造
    public InteractionStrength(){

    }

    //有参构造
    public InteractionStrength(int countryCode,String countryName,String countryAb, int[] childrenID, int parentID,int level, int sdgX, int sdgY, List<List<String>> InteractionList,int offset){
        this.countryCode=countryCode;
        this.countryName=countryName;
        this.countryAb=countryAb;
        this.ChildrenID=childrenID;
        this.ParentID=parentID;
        this.level=level;
        this.SdgX=sdgX;
        this.SdgY=sdgY;
        int Number=0;
        int Sum=0;

        for(int i=1;i<InteractionList.size();i++){
            int tempSdgx=Integer.parseInt(InteractionList.get(i).get(0));
            int tempSdgy=Integer.parseInt(InteractionList.get(i).get(6));
            if(tempSdgx==this.SdgX&&tempSdgy==this.SdgY){
                Number=Number+1;
                Sum=Sum+Integer.parseInt(InteractionList.get(i).get(14));
            }
        }
        if(Number!=0){
            double num1=new Random().nextInt(5);
            this.Angle=(Sum/Number)*20+offset+num1;
        }else{
            this.Angle=0;
        }
        this.Length=Number;
        this.Area=Sum;
    }


}
