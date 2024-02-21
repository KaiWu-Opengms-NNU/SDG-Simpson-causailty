package com.opengms.wukai.pojo.Result.Indicator;


import lombok.Data;

import java.util.List;

@Data
public class LinkForPercentTarget {

    private String GoalX;
    private String GoalY;
    private String TargetX;
    private String TargetY;

    //尺度,数量
    private int Scale;
    private double SingleInteractionNumber;
    private double MaxSingleInteractionNumber;
    private double SingleMaxInteractionPercent;
    private double AllInteractionNumber;
    private double SingleAllInteractionPercent;
    private String Interaction;
    private double AveStrength;//求平均强度
    private double AbsAveStrength;//求平均强度的绝对值
    private double AllStrength;//求总强度
    private double RSquare;//求平均R方
    private double PValue;//求平均显著度
    private double Percent;//求平均占比



    public LinkForPercentTarget(){};

    public LinkForPercentTarget(String goalX,String goalY,String targetX, String targetY,String interaction,int scale, List<LinkForPercentCode> PercentList,double maxSingleInteractionNumber){
        this.GoalX=goalX;
        this.GoalY=goalY;
        this.TargetX=targetX;
        this.TargetY=targetY;
        this.Scale=scale;
        this.Interaction=interaction;
        //this.MaxSingleInteractionNumber = maxSingleInteractionNumber;

        double TempTime=0;
        double TempNumber=0;
        double TempStrength=0;
        double TempRSquare=0;
        double TempPercent=0;
        double TempPValue=0;
        double AllNumber=0;
        double AllCountryNumber=0;
        for(int i=0;i<PercentList.size();i++){
            String IndicatorX=PercentList.get(i).getIndicatorX();
            String IndicatorY=PercentList.get(i).getIndicatorY();
            AllCountryNumber=AllCountryNumber+PercentList.get(i).getNumber();
            if(JudgeTarget(IndicatorX,targetX)&&JudgeTarget(IndicatorY,targetY)){
                AllNumber=AllNumber+PercentList.get(i).getNumber();
                if(this.Interaction.equals(PercentList.get(i).getInteraction())){
                    TempNumber=TempNumber+PercentList.get(i).getNumber();
                    TempStrength=TempStrength+PercentList.get(i).getStrength();
                    TempRSquare=TempRSquare+PercentList.get(i).getRsquare();
                    TempPercent=TempPercent+PercentList.get(i).getPercent();
                    TempPValue = TempPValue+PercentList.get(i).getPvalue();
                    TempTime++;
                }
            }
        }
        this.SingleInteractionNumber =TempNumber;
       // this.SingleMaxInteractionPercent =this.SingleInteractionNumber/this.MaxSingleInteractionNumber *100;
        this.AllInteractionNumber=AllNumber;
        if(AllNumber!=0){
            this.SingleAllInteractionPercent =100*this.SingleInteractionNumber /this.AllInteractionNumber;
            this.AveStrength=TempStrength/TempTime;
            this.AllStrength=TempStrength;
            this.AbsAveStrength=Math.abs(this.AveStrength);
            this.RSquare=TempRSquare/TempTime;
            this.Percent=TempPercent/TempTime;
            this.PValue=TempPValue/TempTime;
        }else{
            this.SingleAllInteractionPercent =0;
            this.AveStrength=0;
            this.AbsAveStrength=0;
            this.AllStrength=0;
            this.RSquare=0;
            this.Percent=0;
            this.PValue=-1;
        }


    }

    private Boolean JudgeTarget(String Target,String Code){

        String[] TargetArray=Target.split("\\.");
        String TargetObtained=TargetArray[0]+"."+TargetArray[1];
        if(TargetObtained.equals(Code)){
            return true;
        }else {
            return false;
        }
    }
}
