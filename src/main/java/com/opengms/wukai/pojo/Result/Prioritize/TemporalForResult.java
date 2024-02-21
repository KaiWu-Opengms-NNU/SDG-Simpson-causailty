package com.opengms.wukai.pojo.Result.Prioritize;

import com.opengms.wukai.pojo.Result.Spatial.ArcmapForSpace;
import lombok.Data;

@Data
public class TemporalForResult {
    private String AreaName;
    private String AreaAb;
    private String AreaID;
    private int Scale;
    private int GoalX;
    private int GoalY;

    private String TargetX_one;
    private String TargetY_one;
    private String Interaction_one;
    private double StrengthAll_one;
    private double StrengthAllRSquared_one;
    private double StrengthWithDirection_one;

    private String TargetX_three;
    private String TargetY_three;
    private String Interaction_three;
    private double StrengthAll_three;
    private double StrengthAllRSquared_three;
    private double StrengthWithDirection_three;

    //Temporal Result
    private int IndicatorPairDifference=0;
    private int InteractionSimpsonDifference=0;
    private double StrengthDifference=0;

    public void SetOneYearScale(ArcmapForSpace arcmapForSpace){
        this.TargetX_one=arcmapForSpace.getTargetX();
        this.TargetY_one=arcmapForSpace.getTargetY();
        this.Interaction_one=arcmapForSpace.getInteraction();
        this.StrengthAll_one=arcmapForSpace.getStrengthAll();
        this.StrengthAllRSquared_one=arcmapForSpace.getStrengthAllRSquared();
        this.StrengthWithDirection_one=arcmapForSpace.getStrengthWithDirection();
    }

    public void SetThreeYearScale(ArcmapForSpace arcmapForSpace){
        this.TargetX_three=arcmapForSpace.getTargetX();
        this.TargetY_three=arcmapForSpace.getTargetY();
        this.Interaction_three=arcmapForSpace.getInteraction();
        this.StrengthAll_three=arcmapForSpace.getStrengthAll();
        this.StrengthAllRSquared_three=arcmapForSpace.getStrengthAllRSquared();
        this.StrengthWithDirection_three=arcmapForSpace.getStrengthWithDirection();
    }

    public void JudgeAndSetResult(){
        if(this.StrengthAll_one!=0&&this.StrengthAll_three!=0){
            if(TargetX_one.equals(TargetX_three)&&TargetY_one.equals(TargetX_three)){
                this.IndicatorPairDifference=2;
            }else{
                this.IndicatorPairDifference=1;
            }
            int OneInteraction=0;
            switch(Interaction_one){
                case "MF":
                    OneInteraction=1;
                    break;
                case "SF":
                    OneInteraction=1;
                    break;
                case "FI":
                    OneInteraction=1;
                    break;
                case "IF":
                    OneInteraction=-1;
                    break;
                case "SI":
                    OneInteraction=-1;
                    break;
                case "MI":
                    OneInteraction=-1;
                    break;
                default:
            }
            int ThreeInteraction=0;
            switch(Interaction_three){
                case "MF":
                    ThreeInteraction=1;
                    break;
                case "SF":
                    ThreeInteraction=1;
                    break;
                case "FI":
                    ThreeInteraction=1;
                    break;
                case "IF":
                    ThreeInteraction=-1;
                    break;
                case "SI":
                    ThreeInteraction=-1;
                    break;
                case "MI":
                    ThreeInteraction=-1;
                    break;
                default:
            }
            this.InteractionSimpsonDifference=ThreeInteraction-OneInteraction;
            this.StrengthDifference=StrengthWithDirection_three-StrengthWithDirection_one;
        }
    }
}
