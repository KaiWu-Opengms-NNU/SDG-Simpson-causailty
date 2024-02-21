package com.opengms.wukai.pojo.Result.Spatial;


import com.opengms.wukai.pojo.Result.AggregationForStatics;
import lombok.Data;
import org.apache.commons.math3.analysis.function.Sin;

import java.lang.ref.PhantomReference;
import java.util.List;

@Data
public class ArcmapForSpace {
    private String AreaName;
    private String AreaAb;
    private String AreaID;
    private int Scale;
    private String GoalX;
    private String GoalY;
    private String TargetX;
    private String TargetY;

    private AggregationForStatics StaticsData;
    //结果文件
    private String Interaction;
    private double StrengthAll;
    private double StrengthAllRSquared;
    private double StrengthWithDirection=0;
    private double Percent;
    private double PValue;

    //存贮类型和强度
    private int OriginalType;
    private double OriginalStrength;
    private int ChangeType;
    private double ChangeStrength;



    //计算方差
    private int PairLevel;
    private double InteractionVar;
    private double StrengthVar;
    private String CountryInteraction;
    private int CountryNumberAll;
    private int CountryNumber;
    private String RegionInteraction;
    private int RegionNumber;
    private String WorldInteraction;
    private int WorldNumber;



    public ArcmapForSpace(){

    }

    public ArcmapForSpace(String areaName,String areaAb, String areaID, int scale, String goalX,String goalY,String targetX,String targetY, AggregationForStatics staticsData){
        this.AreaName=areaName;
        this.AreaAb=areaAb;
        this.AreaID=areaID;
        this.Scale=scale;
        this.GoalX=goalX;
        this.GoalY=goalY;
        this.TargetX=targetX;
        this.TargetY=targetY;
        this.StaticsData=staticsData;
    }

    public void GetInteractionAndStrength(){
        //先读取六个列表
        List<List<String>> SingleFacilitation=StaticsData.getReinforcingOnly();
        List<List<String>> SingleInhibition=StaticsData.getCounteractingOnly();
        List<List<String>> MutualFacilitation=StaticsData.getIndivisible();
        List<List<String>> MutualInhibition=StaticsData.getCancelling();
        List<List<String>> Competition=StaticsData.getReinforcingThenCounteracting();
        List<List<String>> Competition2=StaticsData.getCounteractingThenReinforcing();
        List<List<String>> Netural=StaticsData.getNeuralInteraction();

        double StrengthSF=0;
        double StrengthSFR=0;
        double PercentSF=0;
        double PValueSF=0;
        if(SingleFacilitation.size()>0){
            StrengthSF=GetStrength(SingleFacilitation);
            StrengthSFR=GetR2(SingleFacilitation);
            PercentSF=GetPercent(SingleFacilitation);
            PValueSF=GetPValue(SingleFacilitation);
        }
        double StrengthSI=0;
        double StrengthSIR=0;
        double PercentSI=0;
        double PValueSI=0;
        if(SingleFacilitation.size()>0){
            StrengthSI=GetStrength(SingleInhibition);
            StrengthSIR=GetR2(SingleInhibition);
            PercentSI=GetPercent(SingleInhibition);
            PValueSI=GetPValue(SingleInhibition);
        }
        double StrengthMF=0;
        double StrengthMFR=0;
        double PercentMF=0;
        double PValueMF=0;
        if(MutualFacilitation.size()>0){
            StrengthMF=GetStrength(MutualFacilitation);
            StrengthMFR=GetR2(MutualFacilitation);
            PercentMF=GetPercent(MutualFacilitation);
            PValueMF=GetPValue(MutualFacilitation);
        }
        double StrengthMI=0;
        double StrengthMIR=0;
        double PercentMI=0;
        double PValueMI=0;
        if(MutualInhibition.size()>0){
            StrengthMI=GetStrength(MutualInhibition);
            StrengthMIR=GetR2(MutualInhibition);
            PercentMI=GetPercent(MutualInhibition);
            PValueMI=GetPValue(MutualInhibition);

        }
        double StrengthCO=0;
        double StrengthCOR=0;
        double PercentCO=0;
        double PValueCO=0;
        if(Competition.size()>0){
            StrengthCO=GetStrength(Competition);
            StrengthCOR=GetR2(Competition);
            PercentCO=GetPercent(Competition);
            PValueCO=GetPValue(Competition);
        }
        double StrengthCO2=0;
        double StrengthCO2R=0;
        double PercentCO2=0;
        double PValueCO2=0;
        if (Competition2.size()>0){
            StrengthCO2=GetStrength(Competition2);
            StrengthCO2R=GetR2(Competition2);
            PercentCO2=GetPercent(Competition2);
            PValueCO2=GetPValue(Competition2);
        }
        double[] StrengthInteraction=new double[]{Math.abs(StrengthSF),Math.abs(StrengthSI),Math.abs(StrengthMF),Math.abs(StrengthMI),Math.abs(StrengthCO),Math.abs(StrengthCO2)};
        double Max=StrengthInteraction[0];
        int Index=0;
        for(int i=0;i<StrengthInteraction.length;i++){
            if(StrengthInteraction[i]>Max){
                Max=StrengthInteraction[i];
                Index=i;
            }
        }

        this.StrengthAll=Max;
        if(Max>0){
            switch (Index){
                case 0:
                    this.Interaction="SF";
                    this.StrengthWithDirection =StrengthSF;
                    this.StrengthAllRSquared=StrengthSFR;
                    this.Percent=PercentSF*PercentSF/(PercentCO+PercentCO2+PercentMF+PercentMI+PercentSF+PercentSI);
                    this.PValue=PValueSF;
                    break;
                case 1:
                    this.Interaction="SI";
                    this.StrengthWithDirection =StrengthSI;
                    this.StrengthAllRSquared=StrengthSIR;
                    this.Percent=PercentSI*PercentSI/(PercentCO+PercentCO2+PercentMF+PercentMI+PercentSF+PercentSI);
                    this.PValue=PValueSI;
                    break;
                case 2:
                    this.Interaction="MF";
                    this.StrengthWithDirection =StrengthMF;
                    this.StrengthAllRSquared=StrengthMFR;
                    this.Percent=PercentMF*PercentMF/(PercentCO+PercentCO2+PercentMF+PercentMI+PercentSF+PercentSI);
                    this.PValue=PValueMF;
                    break;
                case 3:
                    this.Interaction="MI";
                    this.StrengthWithDirection =StrengthMI;
                    this.StrengthAllRSquared=StrengthMIR;
                    this.Percent=PercentMI*PercentMI/(PercentCO+PercentCO2+PercentMF+PercentMI+PercentSF+PercentSI);
                    this.PValue=PValueMI;
                    break;
                case 4:
                    this.Interaction="FI";
                    this.StrengthWithDirection =StrengthCO;
                    this.StrengthAllRSquared=StrengthCOR;
                    this.Percent=PercentCO*PercentCO/(PercentCO+PercentCO2+PercentMF+PercentMI+PercentSF+PercentSI);
                    this.PValue=PValueCO;
                    break;
                case 5:
                    this.Interaction="IF";
                    this.StrengthWithDirection =StrengthCO2;
                    this.StrengthAllRSquared=StrengthCO2R;
                    this.Percent=PercentCO2*PercentCO2/(PercentCO+PercentCO2+PercentMF+PercentMI+PercentSF+PercentSI);
                    this.PValue=PValueCO2;
            }
        }else{
            if(GetStrengthNe(Netural)==1){
                this.Interaction="NE";
                this.StrengthWithDirection =0;
                this.StrengthAllRSquared=0;
                this.Percent=100;
                this.PValue=-1;
            }else{
                this.Interaction="null";
                this.StrengthWithDirection =0;
                this.StrengthAllRSquared=0;
                this.Percent=100;
                this.PValue=-1;
            }
        }

    }

    private double GetStrength(List<List<String>> Interaction){
        double result=0;
        double TempIndicatorXY=0;
        System.out.println(this.AreaName);
        for(int i=1; i<Interaction.size();i++){
            String IndicatorX=Interaction.get(i).get(1);
            String IndicatorY=Interaction.get(i).get(7);
            String StrengthXY=Interaction.get(i).get(14);
            if(JudgeTarget(IndicatorX,TargetX)&&(JudgeTarget(IndicatorY,TargetY))){
                TempIndicatorXY=TempIndicatorXY+Double.parseDouble(StrengthXY);

            }
        }
        result=TempIndicatorXY;
        return result;
    }

    private double GetStrengthNe(List<List<String>> Interaction){
        double judge=-1;
        System.out.println(this.AreaName);
        for(int i=1; i<Interaction.size();i++){
            String IndicatorX=Interaction.get(i).get(1);
            String IndicatorY=Interaction.get(i).get(7);
            if(JudgeTarget(IndicatorX,TargetX)&&(JudgeTarget(IndicatorY,TargetY))){
              judge=1;
            }
        }
        return judge;
    }

    private double GetR2(List<List<String>> Interaction){
        double TempR2=0;
        for(int i=1; i<Interaction.size();i++){
            String IndicatorX=Interaction.get(i).get(1);
            String IndicatorY=Interaction.get(i).get(7);
            String R2=Interaction.get(i).get(15);
            if(JudgeTarget(IndicatorX,TargetX)&&(JudgeTarget(IndicatorY,TargetY))){
                TempR2=TempR2+Double.parseDouble(R2);
            }
        }
        return TempR2;
    }

    private double GetPValue(List<List<String>> Interaction){
        double TempPvalue=0;
        for(int i=1; i<Interaction.size();i++){
            String IndicatorX=Interaction.get(i).get(1);
            String IndicatorY=Interaction.get(i).get(7);
            String P=Interaction.get(i).get(16);
            if(JudgeTarget(IndicatorX,TargetX)&&(JudgeTarget(IndicatorY,TargetY))){
                TempPvalue=TempPvalue+Double.parseDouble(P);
            }
        }
        return TempPvalue;
    }

    private double GetPercent(List<List<String>> Interaction){
        double TempPercent=0;
        for(int i=1; i<Interaction.size();i++){
            String IndicatorX=Interaction.get(i).get(1);
            String IndicatorY=Interaction.get(i).get(7);
            String P=Interaction.get(i).get(20);
            if(JudgeTarget(IndicatorX,TargetX)&&(JudgeTarget(IndicatorY,TargetY))){
                TempPercent=TempPercent+Double.parseDouble(P);
            }
        }
        return TempPercent;
    }


    public void SetValue(ArcmapForSpace arcmapForSpace){
        this.TargetX=arcmapForSpace.getTargetX();
        this.TargetY=arcmapForSpace.getTargetY();
        this.Interaction=arcmapForSpace.getInteraction();
        this.StrengthAll=arcmapForSpace.getStrengthAll();
        this.StrengthAllRSquared=arcmapForSpace.getStrengthAllRSquared();
        this.StrengthWithDirection=arcmapForSpace.getStrengthWithDirection();
    }

    public void setTypeAndStrength(String[] TypeList, double[] StrengthList){
        int [] NewType=new int[2];
        for(int i=0;i<2;i++){
            switch (TypeList[i]){
                case "MF":
                    NewType[i]=3;
                    break;
                case "SF":
                    NewType[i]=2;
                    break;
                case "FI":
                    NewType[i]=1;
                    break;
                case "IF":
                    NewType[i]=-1;
                    break;
                case "SI":
                    NewType[i]=-2;
                    break;
                case "MI":
                    NewType[i]=-3;
                    break;
                case "NE":
                    NewType[i]=0;
            }
        }
        this.OriginalType=NewType[0];
        this.ChangeType=NewType[1]-NewType[0];
        this.OriginalStrength=StrengthList[0];
        this.ChangeStrength=StrengthList[1]-StrengthList[0];
    }

    public void SetValueByVariance(int pairLevel,double interactionVar,double strengthVar,String countryInteraction,int countryNumberAll,int countryNumber,String regionInteraction,int regionNumber,String worldInteraction,int worldNumber){
        this.PairLevel=pairLevel;
        this.InteractionVar=interactionVar;
        this.StrengthVar=strengthVar;
        this.CountryInteraction=countryInteraction;
        this.CountryNumberAll=countryNumberAll;
        this.CountryNumber=countryNumber;
        this.RegionInteraction=regionInteraction;
        this.RegionNumber=regionNumber;
        this.WorldInteraction=worldInteraction;
        this.WorldNumber=worldNumber;
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
