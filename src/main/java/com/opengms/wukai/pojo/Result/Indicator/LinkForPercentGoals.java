package com.opengms.wukai.pojo.Result.Indicator;


import com.opengms.wukai.pojo.Result.Spatial.ArcmapForSpace;
import lombok.Data;

import java.util.List;

@Data
public class LinkForPercentGoals {
    private String GoalX;
    private String GoalY;


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
    private double Pvalue;//求平均P值
    private double Percent;//求平均占比



    public LinkForPercentGoals(String goalX,String goalY,String interaction,int scale, List<LinkForPercentCode> PercentList,double maxSingleNumber,double []SDGNumber){
        this.GoalX=goalX;
        this.GoalY=goalY;
        this.Interaction=interaction;
        this.Scale=scale;

        double TempTime=0;
        double TempNumber=0;
        double TempStrength=0;
        double TempRSquare=0;
        double TempPercent=0;
        double TempPvalue=0;
        double AllNumber=0;
        double AllCountryNumber=0;
        for(int i=0;i<PercentList.size();i++){
            String GoalX1=PercentList.get(i).getGoalX();
            String GoalY1=PercentList.get(i).getGoalY();
            AllCountryNumber=AllCountryNumber+PercentList.get(i).getNumber();
            if(GoalX1.equals(goalX)&&GoalY1.equals(goalY)){
                AllNumber=AllNumber+PercentList.get(i).getNumber();
                if(this.Interaction.equals(PercentList.get(i).getInteraction())){
                    TempNumber=TempNumber+PercentList.get(i).getNumber();
                    TempStrength=TempStrength+PercentList.get(i).getStrength();
                    TempRSquare=TempRSquare+PercentList.get(i).getRsquare();
                    TempPercent=TempPercent+PercentList.get(i).getPercent();
                    TempPvalue=TempPvalue+PercentList.get(i).getPvalue();
                    TempTime++;
                }
            }
        }
        this.SingleInteractionNumber =TempNumber;
        this.AllInteractionNumber=AllNumber;
        if(AllNumber!=0){
            this.SingleAllInteractionPercent =100*TempNumber /AllNumber;
            this.AveStrength=TempStrength/TempTime;
            this.AbsAveStrength=Math.abs(this.AveStrength);
            this.AllStrength=TempStrength;
            this.RSquare=TempRSquare/TempTime;
            this.Pvalue=TempPvalue/TempTime;
            this.Percent=TempPercent/TempTime;
        }else{
            this.SingleAllInteractionPercent =0;
            this.AveStrength=0;
            this.AbsAveStrength=0;
            this.RSquare=0;
            this.Percent=0;
            this.Pvalue=-1;
        }

    }

    public LinkForPercentGoals(String goalX, String goalY, String interaction, int scale, List<ArcmapForSpace> PercentList){
        this.GoalX=goalX;
        this.GoalY=goalY;
        this.Interaction=interaction;
        this.Scale=scale;

        double TempTime=0;
        double TempNumber=0;
        double TempStrength=0;
        double TempRSquare=0;
        double TempPercent=0;
        double TempPvalue=0;
        double AllNumber=0;
        double AllCountryNumber=0;
        for(int i=0;i<PercentList.size();i++){
            String GoalX1=PercentList.get(i).getGoalX();
            String GoalY1=PercentList.get(i).getGoalY();
            AllCountryNumber=AllCountryNumber+1;
            if(GoalX1.equals(goalX)&&GoalY1.equals(goalY)){
                if(PercentList.get(i).getScale()==scale)
                {
                    AllNumber=AllNumber+1;
                    if(this.Interaction.equals(PercentList.get(i).getInteraction())){
                        TempNumber=TempNumber+1;
                        TempStrength=TempStrength+PercentList.get(i).getStrengthWithDirection();
                        TempRSquare=TempRSquare+PercentList.get(i).getStrengthAllRSquared();
                        TempPercent=TempPercent+PercentList.get(i).getPercent();
                        TempPvalue=TempPvalue+PercentList.get(i).getPValue();
                        TempTime++;
                    }
                }
            }
        }
        this.SingleInteractionNumber =TempNumber;
        this.AllInteractionNumber=AllNumber;
        if(TempNumber!=0){
            this.SingleAllInteractionPercent =100*TempNumber /AllNumber;
            this.AveStrength=TempStrength/TempTime;
            this.AbsAveStrength=Math.abs(this.AveStrength);
            this.AllStrength=TempStrength;
            this.Percent=TempPercent/TempTime;
            this.Pvalue=TempPvalue/TempTime;
            this.RSquare=TempRSquare/TempTime;
        }else{
            this.SingleAllInteractionPercent =0;
            this.AveStrength=0;
            this.AbsAveStrength=0;
            this.Percent=0;
            this.Pvalue=0;
            this.RSquare=0;
        }

    }
}
