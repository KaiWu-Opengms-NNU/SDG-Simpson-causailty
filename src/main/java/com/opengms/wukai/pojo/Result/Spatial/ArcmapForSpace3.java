package com.opengms.wukai.pojo.Result.Spatial;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArcmapForSpace3 {
    private String GoalX;
    private String GoalY;

    //全球尺度的属性
    private Boolean WorldExist=false;
    private String WorldType;
    private double WorldStrength;

    //分区尺度的属性
    private Boolean RegionExist=false;
    private String RegionMainType;
    private double RegionMainStrength;
    private List<String> RegionIDList=new ArrayList<>();

    //国家尺度的属性
    private Boolean CountryExist=false;
    private String CountryMainType;
    private double CountryMainStrength;
    private List<String> CountryIDList=new ArrayList<>();

    //结果
    private String WorldAndRegionChangeType=null;
    private double WorldAndRegionChangeStrength=0;
    private String RegionAndCountryChangeType=null;
    private double RegionAndCountryChangeStrength=0;

    private String WorldAndRegionAndCountryType=null;
    private int Direction=0;


    //初始化数组
    public ArcmapForSpace3(String goalX,String goalY,List<ArcmapForSpace> AllSDGList){
        this.GoalX=goalX;
        this.GoalY=goalY;
        //载入全球尺度的主要交互作用
        for(int i=0;i<AllSDGList.size();i++){
            if(AllSDGList.get(i).getGoalX().equals(goalX)&&AllSDGList.get(i).getGoalY().equals(goalY)&&AllSDGList.get(i).getScale()==0){
                this.WorldExist=true;
                this.WorldType=AllSDGList.get(i).getInteraction();
                this.WorldStrength=AllSDGList.get(i).getStrengthWithDirection();
                break;
            }
        }
        //载入区域尺度的主要交互作用
        int[] TempRegionInteractionList = new int[]{0, 0, 0, 0, 0, 0, 0};
        double[] TempRegionStrengthList = new double[]{0,0,0,0,0,0,0};
        for(int i=0;i<AllSDGList.size();i++){
            if (AllSDGList.get(i).getGoalX().equals(goalX)&&AllSDGList.get(i).getGoalY().equals(goalY)&&AllSDGList.get(i).getScale()==2){
                String TempInteraction = AllSDGList.get(i).getInteraction();
                double TempStrength=AllSDGList.get(i).getStrengthAll();
                switch (TempInteraction) {
                    case "MF":
                        TempRegionInteractionList[0]++;
                        TempRegionStrengthList[0]=TempRegionStrengthList[0]+TempStrength;
                        break;
                    case "SF":
                        TempRegionInteractionList[1]++;
                        TempRegionStrengthList[1]=TempRegionStrengthList[1]+TempStrength;
                        break;
                    case "FI":
                        TempRegionInteractionList[2]++;
                        TempRegionStrengthList[2]=TempRegionStrengthList[2]+TempStrength;
                        break;
                    case "IF":
                        TempRegionInteractionList[3]++;
                        TempRegionStrengthList[3]=TempRegionStrengthList[3]+TempStrength;
                        break;
                    case "SI":
                        TempRegionInteractionList[4]++;
                        TempRegionStrengthList[4]=TempRegionStrengthList[4]+TempStrength;
                        break;
                    case "MI":
                        TempRegionInteractionList[5]++;
                        TempRegionStrengthList[5]=TempRegionStrengthList[5]+TempStrength;
                        break;
                    case "NE":
                        TempRegionInteractionList[6]++;
                        break;
                    default:
                }
            }
        }
        String RegionInteraction =this.GetMaxNumber(TempRegionInteractionList,TempRegionStrengthList );
        if(RegionInteraction!=null){
            this.RegionExist=true;
            this.RegionMainType=RegionInteraction;
            double TempStrength=0;
            for(int i=0;i<AllSDGList.size();i++){
                if (AllSDGList.get(i).getGoalX().equals(goalX)&&AllSDGList.get(i).getGoalY().equals(goalY)&&AllSDGList.get(i).getScale()==2&&AllSDGList.get(i).getInteraction().equals(RegionInteraction)){

                    TempStrength=TempStrength+AllSDGList.get(i).getStrengthWithDirection();
                     RegionIDList.add(AllSDGList.get(i).getAreaID());
                }
            }
            this.RegionMainStrength=TempStrength/RegionIDList.size();
        }
        //载入国家尺度上的主要交互作用
        int [] TempCountryInteractionList=new int[]{0,0,0,0,0,0,0};
        double[] TempCountryStrengthList = new double[]{0,0,0,0,0,0,0};
        for(int i=0;i<AllSDGList.size();i++){
            if (AllSDGList.get(i).getGoalX().equals(goalX)&&AllSDGList.get(i).getGoalY().equals(goalY)&&AllSDGList.get(i).getScale()==3){
                String TempInteraction = AllSDGList.get(i).getInteraction();
                double TempStrength=AllSDGList.get(i).getStrengthAll();
                switch (TempInteraction) {
                    case "MF":
                        TempCountryInteractionList[0]++;
                        TempCountryStrengthList[0]= TempCountryStrengthList[0]+TempStrength;
                        break;
                    case "SF":
                        TempCountryInteractionList[1]++;
                        TempCountryStrengthList[1]= TempCountryStrengthList[1]+TempStrength;
                        break;
                    case "FI":
                        TempCountryInteractionList[2]++;
                        TempCountryStrengthList[2]= TempCountryStrengthList[2]+TempStrength;
                        break;
                    case "IF":
                        TempCountryInteractionList[3]++;
                        TempCountryStrengthList[3]= TempCountryStrengthList[3]+TempStrength;
                        break;
                    case "SI":
                        TempCountryInteractionList[4]++;
                        TempCountryStrengthList[4]= TempCountryStrengthList[4]+TempStrength;
                        break;
                    case "MI":
                        TempCountryInteractionList[5]++;
                        TempCountryStrengthList[5]= TempCountryStrengthList[5]+TempStrength;
                        break;
                    case "NE":
                        TempCountryInteractionList[6]++;
                        break;
                    default:
                }
            }
        }
        String CountryInteraction =this.GetMaxNumber(TempCountryInteractionList,TempCountryStrengthList);
        if(CountryInteraction!=null){
            this.CountryExist= true;
            this.CountryMainType=CountryInteraction;
            double TempStrength=0;
            for(int i=0;i<AllSDGList.size();i++){
                if (AllSDGList.get(i).getGoalX().equals(goalX)&&AllSDGList.get(i).getGoalY().equals(goalY)&&AllSDGList.get(i).getScale()==3&&AllSDGList.get(i).getInteraction().equals(CountryInteraction)){
                    TempStrength=TempStrength+AllSDGList.get(i).getStrengthWithDirection();
                    CountryIDList.add(AllSDGList.get(i).getAreaID());
                }
            }
            this.CountryMainStrength=TempStrength/CountryIDList.size();
        }


    }

    public void CalculateStrengthAndType(){
        if(WorldExist&&RegionExist){
             this.WorldAndRegionChangeStrength=WorldStrength-RegionMainStrength;
             if(WorldStrength<0&&RegionMainStrength>0){
                 this.WorldAndRegionChangeType="SimpsonChange";
             }else if(WorldStrength>0&&RegionMainStrength<0){
                 this.WorldAndRegionChangeType="SimpsonChange";
             }else{
                 if(WorldType.equals(RegionMainType)){
                     this.WorldAndRegionChangeType="NoChange";
                 }else{
                     this.WorldAndRegionChangeType="OtherChange";
                 }
             }
        }

        if(RegionExist&&CountryExist){
            this.RegionAndCountryChangeStrength=RegionMainStrength-CountryMainStrength;
            if(RegionMainStrength>0&&CountryMainStrength<0){
                this.RegionAndCountryChangeType="SimpsonChange";
            }else if(RegionMainStrength<0&&CountryMainStrength>0){
                this.RegionAndCountryChangeType="SimpsonChange";
            }else{
                if(RegionMainType.equals(CountryMainType)){
                    this.RegionAndCountryChangeType="NoChange";
                }else{
                    this.RegionAndCountryChangeType="OtherChange";
                }
            }
        }

        if(WorldAndRegionChangeType==null){
            this.WorldAndRegionAndCountryType=RegionAndCountryChangeType;
            this.Direction=this.ConvertNumber(RegionMainType)-this.ConvertNumber(CountryMainType);
        }
        if(WorldAndRegionChangeType!=null&&RegionAndCountryChangeType!=null){
            if (WorldAndRegionChangeType.equals("SimpsonChange")||RegionAndCountryChangeType.equals("SimpsonChange")){
                this.WorldAndRegionAndCountryType="SimpsonChange";
            }else if(WorldAndRegionChangeType.equals("NoChange")&&RegionAndCountryChangeType.equals("NoChange")){
                this.WorldAndRegionAndCountryType="NoChange";
            }else{
                this.WorldAndRegionAndCountryType="OtherChange";
            }
            this.Direction=this.ConvertNumber(WorldType)-this.ConvertNumber(CountryMainType);
        }

    }

    public String GetMaxNumber(int [] data, double[] Strength){
        int TempIndex=-1;
        int TempValue=0;
        for(int i=0;i<data.length-1;i++){
            if(data[i]>TempValue){
                TempValue=data[i];
                TempIndex=i;
            }
        }
        if(TempValue>0){
            int Time=0;
            int[] judgeindex=new int[]{0,0,0,0,0,0,0};
            for(int i=0;i<data.length-1;i++){
                if(data[i]==TempValue){
                    Time++;
                    judgeindex[i]=1;
                }
            }
            if(Time==1){

            }else {
                double TempStength=Strength[TempIndex];
                for(int i=0;i<Strength.length-1;i++){
                    if(judgeindex[i]==1&&Strength[i]>TempStength){
                        TempIndex=i;
                        TempStength=Strength[i];
                    }
                }
            }
        }
        if(TempIndex==-1&&data[data.length-1]!=0){
            return "NE";
        }else if(TempIndex!=-1){
            String TempInteraction = null;
            switch (TempIndex){
                case 0:
                    TempInteraction="MF";
                    break;
                case 1:
                    TempInteraction="SF";
                    break;
                case 2:
                    TempInteraction="FI";
                    break;
                case 3:
                    TempInteraction="IF";
                    break;
                case 4:
                    TempInteraction="SI";
                    break;
                case 5:
                    TempInteraction="MI";
                    break;
                default:
            }
            return TempInteraction;
        }else{
            return null;
        }
    }

    public int ConvertNumber(String interaction){
        int Number=0;
        try{
            switch (interaction) {
                case "MF":
                    Number=3;
                    break;
                case "SF":
                    Number=2;
                    break;
                case "FI":
                    Number=1;
                    break;
                case "IF":
                    Number=-1;
                    break;
                case "SI":
                    Number=-2;
                    break;
                case "MI":
                    Number=-3;
                    break;
                case "NE":
                    Number=0;
                    break;
                default:
            }
        }catch (Exception I){

        }
        return  Number;
    }
}
