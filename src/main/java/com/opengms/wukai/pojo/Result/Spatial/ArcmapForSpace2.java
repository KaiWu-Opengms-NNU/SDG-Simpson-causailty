package com.opengms.wukai.pojo.Result.Spatial;

import com.opengms.wukai.tool.SDGIndicatorTool;
import lombok.Data;

@Data
public class ArcmapForSpace2 {
    //分区数据
    private String AreaName;
    private String AreaAb;
    private String AreaID;
    private int Scale;
    private String GoalX;
    private String GoalY;

    //国家本身的交互作用数据
    private String Interaction;
    private double StrengthWithDirection=0;
    private double Percent;
    private double PValue;

    //高尺度的数据
    private String RegionInteraction=null;
    private double RegionStrength=0;
    private String WorldInteraction=null;
    private double WorldStrength=0;

    private int levelNumber=0;
    private String ChangeType;
    private double StrengthVar;


    //空的构造函数
    public ArcmapForSpace2(){

    }

    //非空的构造函数
    public ArcmapForSpace2(ArcmapForSpace input){
        this.AreaName=input.getAreaName();
        this.AreaAb=input.getAreaAb();
        this.AreaID=input.getAreaID();
        this.Scale=input.getScale();
        this.GoalX=input.getGoalX();
        this.GoalY=input.getGoalY();
        this.Interaction=input.getInteraction();
        this.StrengthWithDirection= input.getStrengthWithDirection();
        this.Percent=input.getPercent();
        this.PValue=input.getPValue();
    }

    public void CalculateType(){
        String[] InteractionList=new String[]{this.Interaction,this.RegionInteraction,this.WorldInteraction};
        int[] InteractionList2=new int[]{-100,-100,-100};
        for(int i=0;i<InteractionList.length;i++){
            if(InteractionList[i]!=null){
                this.levelNumber++;
                switch (InteractionList[i]){
                    case "MF":
                        InteractionList2[i]=3;
                        break;
                    case "SF":
                        InteractionList2[i]=2;
                        break;
                    case "FI":
                        InteractionList2[i]=1;
                        break;
                    case "NE":
                        InteractionList2[i]=0;
                        break;
                    case "IF":
                        InteractionList2[i]=-1;
                        break;
                    case "SI":
                        InteractionList2[i]=-2;
                        break;
                    case "MI":
                        InteractionList2[i]=-3;
                        break;
                    default:
                }
            }
        }
        if(levelNumber==3){
            int CountryToRegion=InteractionList2[0]-InteractionList2[1];
            int RegionToWorld=InteractionList2[1]-InteractionList2[2];
            if(CountryToRegion==0&&RegionToWorld==0){
                this.ChangeType="NoChange";
            }else if(CountryToRegion>0&&RegionToWorld>0){
                this.ChangeType="IChange";
            }else if(CountryToRegion>0&&RegionToWorld==0){
                this.ChangeType="IChange";
            }else if(CountryToRegion==0&&RegionToWorld>0){
                this.ChangeType="IChange";
            }else if(CountryToRegion<0&&RegionToWorld==0){
                this.ChangeType="FChange";
            }else if(CountryToRegion<0&&RegionToWorld<0){
                this.ChangeType="FChange";
            }else if(CountryToRegion==0&&RegionToWorld<0){
                this.ChangeType="FChange";
            }else if(CountryToRegion>0&&RegionToWorld<0){
                this.ChangeType="IChangeFChange";
            }else{
                this.ChangeType="FChangeIChange";
            }
            this.StrengthVar=new SDGIndicatorTool().CalculateVariance(new double[]{StrengthWithDirection,RegionStrength, WorldStrength});
        }else if(levelNumber==2) {
            int[] InteractionList3 = new int[]{0, 0};
            int j = 0;
            for (int i = 0; i < InteractionList2.length; i++) {
                if (InteractionList2[i] != -100) {
                    InteractionList3[j] = InteractionList2[i];
                    j++;
                }
            }
            int CountryToRegionOrWorld = InteractionList3[0] - InteractionList3[1];
            if (CountryToRegionOrWorld == 0) {
                this.ChangeType = "NoChange";
            } else if (CountryToRegionOrWorld > 0) {
                this.ChangeType = "IChange";
            } else {
                this.ChangeType = "FChange";
            }
            double[] StrengthList=new double[2];
            if(InteractionList2[0]==-100){
                StrengthList[0]=RegionStrength;
                StrengthList[1]=WorldStrength;
            }else if(InteractionList2[1]==-100){
                StrengthList[0]=StrengthWithDirection;
                StrengthList[1]=WorldStrength;
            }else{
                StrengthList[0]=StrengthWithDirection;
                StrengthList[1]=RegionStrength;
            }
            this.StrengthVar=new SDGIndicatorTool().CalculateVariance(StrengthList);
        }
        else{
            this.ChangeType="null";
            this.StrengthVar=0;
        }


    }

}
