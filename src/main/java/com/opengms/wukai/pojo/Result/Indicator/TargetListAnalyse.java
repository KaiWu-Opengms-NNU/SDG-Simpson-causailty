package com.opengms.wukai.pojo.Result.Indicator;

import lombok.Data;

import java.util.List;

@Data
public class TargetListAnalyse {
    private String TargetX;
    private String TargetY;
    private String Interaction;
    private double WorldValue=0;
    private double RegionValue=0;
    private double CountryValue=0;
    private double SumValue;
    private double World_Region_Change;
    private double Region_Country_Change;

    public void GetScaleValue(String interaction, List<LinkForPercentTarget> WorldList, List<LinkForPercentTarget> RegionList, List<LinkForPercentTarget> CountryList)
    {
        this.Interaction=interaction;
        for(int i=0;i<WorldList.size();i++){
            switch (interaction){
                case "SF":
                    WorldValue=JudgeSameInteraction(this.TargetX,this.TargetY,"SF",WorldList.get(i));
                    break;
                case "SI":
                    WorldValue=JudgeSameInteraction(this.TargetX,this.TargetY,"SI",WorldList.get(i));
                    break;
                case "MF":
                    WorldValue=JudgeSameInteraction(this.TargetX,this.TargetY,"MF",WorldList.get(i));
                    break;
                case "MI":
                    WorldValue=JudgeSameInteraction(this.TargetX,this.TargetY,"MI",WorldList.get(i));
                    break;
                case "FI":
                    WorldValue=JudgeSameInteraction(this.TargetX,this.TargetY,"FI",WorldList.get(i));
                    break;
                case "IF":
                    WorldValue=JudgeSameInteraction(this.TargetX,this.TargetY,"IF",WorldList.get(i));
                    break;
                case "NO":
                    WorldValue=JudgeSameInteraction(this.TargetX,this.TargetY,"NO",WorldList.get(i));
                    break;
            }
            if(this.WorldValue!=0){
                break;
            }
        }
        for(int i=0;i<RegionList.size();i++){
            switch (interaction){
                case "SF":
                    RegionValue=JudgeSameInteraction(this.TargetX,this.TargetY,"SF",RegionList.get(i));
                    break;
                case "SI":
                    RegionValue=JudgeSameInteraction(this.TargetX,this.TargetY,"SI",RegionList.get(i));
                    break;
                case "MF":
                    RegionValue=JudgeSameInteraction(this.TargetX,this.TargetY,"MF",RegionList.get(i));
                    break;
                case "MI":
                    RegionValue=JudgeSameInteraction(this.TargetX,this.TargetY,"MI",RegionList.get(i));
                    break;
                case "FI":
                    RegionValue =JudgeSameInteraction(this.TargetX,this.TargetY,"FI",RegionList.get(i));
                    break;
                case "IF":
                    RegionValue =JudgeSameInteraction(this.TargetX,this.TargetY,"IF",RegionList.get(i));
                    break;
                case "NO":
                    RegionValue=JudgeSameInteraction(this.TargetX,this.TargetY,"NO",RegionList.get(i));
                    break;
            }
            if(RegionValue!=0){
                break;
            }
        }
        for(int i=0;i<CountryList.size();i++){
            switch (interaction){
                case "SF":
                    CountryValue=JudgeSameInteraction(this.TargetX,this.TargetY,"SF",CountryList.get(i));
                    break;
                case "SI":
                    CountryValue=JudgeSameInteraction(this.TargetX,this.TargetY,"SI",CountryList.get(i));
                    break;
                case "MF":
                    CountryValue=JudgeSameInteraction(this.TargetX,this.TargetY,"MF",CountryList.get(i));
                    break;
                case "MI":
                    CountryValue=JudgeSameInteraction(this.TargetX,this.TargetY,"MI",CountryList.get(i));
                    break;
                case "FI":
                    CountryValue=JudgeSameInteraction(this.TargetX,this.TargetY,"FI",CountryList.get(i));
                    break;
                case "IF":
                    CountryValue=JudgeSameInteraction(this.TargetX,this.TargetY,"IF",CountryList.get(i));
                    break;
                case "NO":
                    CountryValue=JudgeSameInteraction(this.TargetX,this.TargetY,"NO",CountryList.get(i));
                    break;
            }
            if(CountryValue!=0){
                break;
            }
        }
        this.SumValue=WorldValue+RegionValue+CountryValue;
        this.World_Region_Change=WorldValue-RegionValue;
        this.Region_Country_Change=RegionValue-CountryValue;
    }

    private double JudgeSameInteraction(String X,String Y,String interaction,LinkForPercentTarget Target){
        double result=0;
        if(Target.getInteraction().equals(interaction)){
            if(Target.getTargetX().equals(X)&&Target.getTargetY().equals(Y)){
                result=Target.getSingleMaxInteractionPercent();
            }
        }
        return result;
    }

}
