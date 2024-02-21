package com.opengms.wukai.pojo.Result.Spatial;



import lombok.Data;

import java.util.List;

@Data
public class ArcmapForSpace4
{
    private String AreaID;
    private String AreaName;
    private int ParentID;
    private int Scale;
    private String ChangeType;
    private double AllSumStr;
    private int SDG1=0;
    private int SDG2=0;
    private int SDG3=0;
    private int SDG4=0;
    private int SDG5=0;
    private int SDG6=0;
    private int SDG7=0;
    private int SDG8=0;
    private int SDG9=0;
    private int SDG10=0;
    private int SDG11=0;
    private int SDG12=0;
    private int SDG13=0;
    private int SDG14=0;
    private int SDG15=0;
    private int SDG16=0;
    private int SDG17=0;
    private int NChange=0;
    private int OChange=0;
    private int SChange=0;

    public ArcmapForSpace4(String areaID, String areaName, int parentID, int scale, String changeType, List<ArcmapForSpace3> inputList){
        this.AreaID=areaID;
        this.AreaName=areaName;
        this.Scale=scale;
        this.ParentID=parentID;
        this.ChangeType=changeType;
        double TempStrength=0;
        if(this.Scale==3){
            for(int i=0;i<inputList.size();i++){
                ArcmapForSpace3 oneData=inputList.get(i);
                int ExistID=-1;
                for(int j=0;j<oneData.getCountryIDList().size();j++){
                    if(oneData.getCountryIDList().get(j).equals(this.AreaID)){
                        ExistID=1;
                        break;
                    }
                }
                if(ExistID==1){
                    if(oneData.getRegionAndCountryChangeType()!=null){
                        if(oneData.getRegionAndCountryChangeType().equals(this.ChangeType)){
                            switch (oneData.getGoalX()){
                                case "1":
                                    SDG1++;
                                    break;
                                case "2":
                                    SDG2++;
                                    break;
                                case "3":
                                    SDG3++;
                                    break;
                                case "4":
                                    SDG4++;
                                    break;
                                case "5":
                                    SDG5++;
                                    break;
                                case "6":
                                    SDG6++;
                                    break;
                                case "7":
                                    SDG7++;
                                    break;
                                case "8":
                                    SDG8++;
                                    break;
                                case "9":
                                    SDG9++;
                                    break;
                                case "10":
                                    SDG10++;
                                    break;
                                case "11":
                                    SDG11++;
                                    break;
                                case "12":
                                    SDG12++;
                                    break;
                                case "13":
                                    SDG13++;
                                    break;
                                case "14":
                                    SDG14++;
                                    break;
                                case "15":
                                    SDG15++;
                                    break;
                                case "16":
                                    SDG16++;
                                    break;
                                case "17":
                                    SDG17++;
                                    break;
                            }
                        }
                        if(oneData.getRegionAndCountryChangeType().equals("SimpsonChange")){
                            SChange++;
                            TempStrength=TempStrength+oneData.getRegionAndCountryChangeStrength();
                        }
                        if(oneData.getRegionAndCountryChangeType().equals("NoChange")){
                            NChange++;
                        }
                        if(oneData.getRegionAndCountryChangeType().equals("OtherChange")){
                            OChange++;
                        }
                    }

                }
            }
        }
        if(this.Scale==2){
            for(int i=0;i<inputList.size();i++){
                ArcmapForSpace3 oneData=inputList.get(i);
                int ExistID=-1;
                for(int j=0;j<oneData.getRegionIDList().size();j++){
                    if(oneData.getRegionIDList().get(j).equals(this.AreaID)&&oneData.getWorldAndRegionChangeType()!=null){
                        ExistID=1;
                        break;
                    }
                }
                if(ExistID==1){
                    if(oneData.getWorldAndRegionChangeType()!=null){
                        if(oneData.getWorldAndRegionChangeType().equals(this.ChangeType)){
                            switch (oneData.getGoalX()){
                                case "1":
                                    SDG1++;
                                    break;
                                case "2":
                                    SDG2++;
                                    break;
                                case "3":
                                    SDG3++;
                                    break;
                                case "4":
                                    SDG4++;
                                    break;
                                case "5":
                                    SDG5++;
                                    break;
                                case "6":
                                    SDG6++;
                                    break;
                                case "7":
                                    SDG7++;
                                    break;
                                case "8":
                                    SDG8++;
                                    break;
                                case "9":
                                    SDG9++;
                                    break;
                                case "10":
                                    SDG10++;
                                    break;
                                case "11":
                                    SDG11++;
                                    break;
                                case "12":
                                    SDG12++;
                                    break;
                                case "13":
                                    SDG13++;
                                    break;
                                case "14":
                                    SDG14++;
                                    break;
                                case "15":
                                    SDG15++;
                                    break;
                                case "16":
                                    SDG16++;
                                    break;
                                case "17":
                                    SDG17++;
                                    break;
                            }
                        }
                        if(oneData.getWorldAndRegionChangeType().equals("SimpsonChange")){
                            SChange++;
                            TempStrength=TempStrength+oneData.getWorldAndRegionChangeStrength();
                        }
                        if(oneData.getWorldAndRegionChangeType().equals("NoChange")){
                            NChange++;
                        }
                        if(oneData.getWorldAndRegionChangeType().equals("OtherChange")){
                            OChange++;
                        }
                    }
                }
            }
        }

        this.AllSumStr=-TempStrength;
    }

    //只考虑辛普森变化的
    public ArcmapForSpace4(String areaID, String areaName, int scale, String changeType, List<ArcmapForSpace3> inputList,String GoalX){
        this.AreaID=areaID;
        this.AreaName=areaName;
        this.Scale=scale;
        this.ChangeType=changeType;
        double TempStrength=0;
        if(this.Scale==3){
            for(int i=0;i<inputList.size();i++){
                ArcmapForSpace3 oneData=inputList.get(i);
                int ExistID=-1;
                for(int j=0;j<oneData.getCountryIDList().size();j++){
                    if(oneData.getCountryIDList().get(j).equals(this.AreaID)&&oneData.getGoalX().equals(GoalX))
                    {
                        ExistID=1;
                        break;
                    }
                }
                if(ExistID==1){
                    if(oneData.getRegionAndCountryChangeType()!=null){
                        if(oneData.getRegionAndCountryChangeType().equals(this.ChangeType)&&oneData.getGoalX().equals(GoalX)){
                            switch (oneData.getGoalY()){
                                case "1":
                                    SDG1++;
                                    break;
                                case "2":
                                    SDG2++;
                                    break;
                                case "3":
                                    SDG3++;
                                    break;
                                case "4":
                                    SDG4++;
                                    break;
                                case "5":
                                    SDG5++;
                                    break;
                                case "6":
                                    SDG6++;
                                    break;
                                case "7":
                                    SDG7++;
                                    break;
                                case "8":
                                    SDG8++;
                                    break;
                                case "9":
                                    SDG9++;
                                    break;
                                case "10":
                                    SDG10++;
                                    break;
                                case "11":
                                    SDG11++;
                                    break;
                                case "12":
                                    SDG12++;
                                    break;
                                case "13":
                                    SDG13++;
                                    break;
                                case "14":
                                    SDG14++;
                                    break;
                                case "15":
                                    SDG15++;
                                    break;
                                case "16":
                                    SDG16++;
                                    break;
                                case "17":
                                    SDG17++;
                                    break;
                            }
                            TempStrength=TempStrength+oneData.getRegionAndCountryChangeStrength();
                            SChange++;
                        }
                    }

                }
            }
        }
        if(this.Scale==2){
            for(int i=0;i<inputList.size();i++){
                ArcmapForSpace3 oneData=inputList.get(i);
                int ExistID=-1;
                for(int j=0;j<oneData.getRegionIDList().size();j++){
                    if(oneData.getRegionIDList().get(j).equals(this.AreaID)&&oneData.getWorldAndRegionChangeType()!=null){
                       if(oneData.getGoalX().equals(GoalX)){
                           ExistID=1;
                           break;
                       }
                    }
                }
                if(ExistID==1){
                    if(oneData.getWorldAndRegionChangeType()!=null){
                        if(oneData.getWorldAndRegionChangeType().equals(this.ChangeType)&&oneData.getGoalX().equals(GoalX)){
                            switch (oneData.getGoalY()){
                                case "1":
                                    SDG1++;
                                    break;
                                case "2":
                                    SDG2++;
                                    break;
                                case "3":
                                    SDG3++;
                                    break;
                                case "4":
                                    SDG4++;
                                    break;
                                case "5":
                                    SDG5++;
                                    break;
                                case "6":
                                    SDG6++;
                                    break;
                                case "7":
                                    SDG7++;
                                    break;
                                case "8":
                                    SDG8++;
                                    break;
                                case "9":
                                    SDG9++;
                                    break;
                                case "10":
                                    SDG10++;
                                    break;
                                case "11":
                                    SDG11++;
                                    break;
                                case "12":
                                    SDG12++;
                                    break;
                                case "13":
                                    SDG13++;
                                    break;
                                case "14":
                                    SDG14++;
                                    break;
                                case "15":
                                    SDG15++;
                                    break;
                                case "16":
                                    SDG16++;
                                    break;
                                case "17":
                                    SDG17++;
                                    break;
                            }
                            TempStrength=TempStrength+oneData.getWorldAndRegionChangeStrength();
                            SChange++;
                        }
                    }
                }
            }
        }
        this.AllSumStr=-TempStrength;
    }

}
