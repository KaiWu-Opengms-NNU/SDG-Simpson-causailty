package com.opengms.wukai.tool;



import cointegration.RawData;
import com.opengms.wukai.pojo.Granger.GrangerResult;
import com.opengms.wukai.pojo.Result.NetWork.NetNode;
import com.opengms.wukai.pojo.Result.NetWork.NetRelationship;
import com.opengms.wukai.pojo.Result.Spatial.ArcmapForResult;
import com.opengms.wukai.pojo.Result.Spatial.ArcmapForSpace;
import com.opengms.wukai.pojo.Result.ScaleTree;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SDGIndicatorTool {

    //判断制定的指标在不在百分比指标中
    public String JudgeSDGIndicator(String SeriesCode, List<List<String>> IndicatorByUsing){
        String JudgeResult=null;
        for(int i=1;i<IndicatorByUsing.size();i++){
            if(SeriesCode.equals(IndicatorByUsing.get(i).get(3))){
                JudgeResult=IndicatorByUsing.get(i).get(5);
                break;
            }
        }
        return JudgeResult;
    }

    //提取每个指标对在全球的百分比
    public int GetGoalNumber(int x,int y,List<List<String>> InteractionList){
        int Number=0;
        for(int i=1;i<InteractionList.size();i++){
            int FileX=Integer.parseInt(InteractionList.get(i).get(0));
            int FileY=Integer.parseInt(InteractionList.get(i).get(6));
            if(x==FileX&&y==FileY){
                Number++;
            }
        }
        return Number;
    }

    //判断到底是权衡还是协同作用
    public double JudgeInteraction(GrangerResult grangerResult,int StartYear,int EndYear){
        double JudgeResult=0;
        int Duration=EndYear-StartYear;
        int SumT=0;
        for(int t=1;t<Duration;t++){
            SumT=SumT+(int)Math.pow(t,grangerResult.getOrderY()-grangerResult.getOrderX());
        }
        //JudgeResult=(-1)*(grangerResult.getGrangerParameters()[1]/grangerResult.getGrangerParameters()[2])*SumT*grangerResult.getInteraction();//-b/a判断
        JudgeResult=grangerResult.getGrangerParameters()[1]*SumT*grangerResult.getInteraction();//以b判断
        return JudgeResult;
    }

    //在原始序列中找到原始数据
    public double[] GetRawData (List<RawData> rawDataList, String StringCode){
        double [] result=null;
        for(int i=0;i<rawDataList.size();i++){
            if(StringCode.equals(rawDataList.get(i).getIndicator())){
               result=rawDataList.get(i).getValue();
             }
        }
        return result;
    }

    //提取每个指标对在不同作用类型下在不同尺度的数量
    public int GetGoalNumberInDiffScale(String CodeX,String CodeY,List<List<String>> InteractionList){
        int Number=0;
        try{
            for (int i=1;i<InteractionList.size();i++){
                String X=InteractionList.get(i).get(1);
                String Y=InteractionList.get(i).get(7);
                if (X.equals(CodeX)&&Y.equals(CodeY)){
                    Number++;
                }
            }
        }catch (Exception e){
            System.out.println("无数据");
        }
        return Number;
    }

    //提取每个指标在不同作用类型下的强度
    public double GetGoalStrengthInDiffScale(String CodeX,String CodeY, List<List<String>> InteractionList){
        double AllStrength=0;
        try{
            for (int i=1;i<InteractionList.size();i++){
                String X=InteractionList.get(i).get(1);
                String Y=InteractionList.get(i).get(7);
                if (X.equals(CodeX)&&Y.equals(CodeY)){
                    AllStrength=AllStrength+ Double.parseDouble(InteractionList.get(i).get(14));
                }
            }
        }catch(Exception e) {
            System.out.println("无数据");
        }
        return AllStrength;
    }

    //提取每个指标在不同作用类型下的R方
    public double GetGoalRSquareInDiffScale(String CodeX,String CodeY, List<List<String>> InteractionList){
        double AllRsquare=0;
        try{
            for (int i=1;i<InteractionList.size();i++){
                String X=InteractionList.get(i).get(1);
                String Y=InteractionList.get(i).get(7);
                if (X.equals(CodeX)&&Y.equals(CodeY)){
                    AllRsquare=AllRsquare+ Double.parseDouble(InteractionList.get(i).get(15));
                }
            }
        }catch(Exception e) {
            System.out.println("无数据");
        }
        return AllRsquare;
    }

    public double GetGoalPvalueInDiffScale(String CodeX,String CodeY,List<List<String>> InteractionList){
        double Pvalue=0;
        try{
            for (int i=1;i<InteractionList.size();i++){
                String X=InteractionList.get(i).get(1);
                String Y=InteractionList.get(i).get(7);
                if (X.equals(CodeX)&&Y.equals(CodeY)){
                    Pvalue=Pvalue+ Double.parseDouble(InteractionList.get(i).get(16));
                }
            }
        }catch(Exception e) {
            System.out.println("无数据");
        }
        return Pvalue;
    }

    //提取每个指标在主要作用的所占比例
    public double GetGoalPercentInDiffScale(String CodeX,String CodeY, List<List<String>> InteractionList){
        double AllPercent=0;
        try{
            for (int i=1;i<InteractionList.size();i++){
                String X=InteractionList.get(i).get(1);
                String Y=InteractionList.get(i).get(7);
                if (X.equals(CodeX)&&Y.equals(CodeY)){
                    AllPercent=AllPercent+ Double.parseDouble(InteractionList.get(i).get(20));
                }
            }
        }catch(Exception e) {
            System.out.println("无数据");
        }
        return AllPercent;
    }


    public int GetAllNumberInDiffScale(List<List<String>> InteractionList){
        int Number=0;
        try{
            for (int i=1;i<InteractionList.size();i++){
                Number++;
            }
        }catch (Exception e){
            System.out.println("无数据");
        }
        return Number;
    }


    //得到父集指标
    public int FindParentID(List<ScaleTree> worldLevel, int ID){
        int ParentID=-1;
        for(int i=0;i<worldLevel.size();i++){
            if(ID==worldLevel.get(i).getID()){
                ParentID=worldLevel.get(i).getParentID();
            }
        }
        return ParentID;
    }

    //得到子集指标
    public int[] FindChildrenIDArray(List<ScaleTree> worldLevel, int ID){
        int[] childrenArray=new int[0];
        for(int i=0;i<worldLevel.size();i++){
            if(ID==worldLevel.get(i).getID()){
                childrenArray=worldLevel.get(i).getChildrenID();
            }
        }
        return childrenArray;
    }


    //提取在每个目标对列表根据ID找值
    public ArcmapForSpace FindArcMapID(List<ArcmapForSpace> ArcmapForSpaceList, int ID){
        ArcmapForSpace arcmapForSpace=null;
        for(int i=0; i<ArcmapForSpaceList.size();i++){
            if(Integer.parseInt(ArcmapForSpaceList.get(i).getAreaID())==ID){
                arcmapForSpace=ArcmapForSpaceList.get(i);
                break;
            }
        }
        return arcmapForSpace;
    }

    //根据数组计算强度方差
    public double CalculateVariance(double [] data){
        double Variance=0;
        double Average=0;
        for(int i=0;i<data.length;i++){
            Average=data[i]+Average;
        }
        Average=Average/data.length;
        for(int i=0;i<data.length;i++){
            Variance=Variance+(data[i]-Average)*(data[i]-Average);
        }
        double standardDeviation=Math.sqrt(Variance/data.length);
        return standardDeviation;
    }
    //计算数组交互作用类型的方程
    public double CalculateVarianceByInteraction(List<ArcmapForSpace> arcmapForSpaceList){
        int[] data=new int[arcmapForSpaceList.size()];
        for(int i=0;i<data.length;i++){
            switch(arcmapForSpaceList.get(i).getInteraction()){
                case "MF":
                    data[i]=1;
                    break;
                case "SF":
                    data[i]=1;
                    break;
                case "FI":
                    data[i]=1;
                    break;
                case "IF":
                    data[i]=-1;
                    break;
                case "SI":
                    data[i]=-1;
                    break;
                case "MI":
                    data[i]=-1;
                    break;
                default:
            }

        }
        double Variance=0;
        double Average=0;
        for(int i=0;i<data.length;i++){
            Average=data[i]+Average;
        }
        Average=Average/data.length;
        for(int i=0;i<data.length;i++){
            Variance=Variance+(data[i]-Average)*(data[i]-Average);
        }
        double standardDeviation=Math.sqrt(Variance/data.length);
        return standardDeviation;

    }
    public double CalculateVarianceByInteraction(String [] data2){
        int[] data=new int[data2.length];
        for(int i=0;i<data.length;i++){
            switch(data2[i]){
                case "MF":
                    data[i]=1;
                    break;
                case "SF":
                    data[i]=1;
                    break;
                case "FI":
                    data[i]=1;
                    break;
                case "IF":
                    data[i]=-1;
                    break;
                case "SI":
                    data[i]=-1;
                    break;
                case "MI":
                    data[i]=-1;
                    break;
                case "NE":
                    data[i]=0;
                    break;
                default:
            }

        }
        double Variance=0;
        double Average=0;
        for(int i=0;i<data.length;i++){
            Average=data[i]+Average;
        }
        Average=Average/data.length;
        for(int i=0;i<data.length;i++){
            Variance=Variance+(data[i]-Average)*(data[i]-Average);
        }
        double standardDeviation=Math.sqrt(Variance/data.length);
        return standardDeviation;
    }

    //得到数组最大值
    public String GetMaxCountryNumber(int [] data){
        int TempIndex=-1;
        int TempValue=0;
        for(int i=0;i<data.length-1;i++){
            if(data[i]>TempValue){
                TempValue=data[i];
                TempIndex=i;
            }
        }
        if(TempIndex!=-1){
            String Interaction= null;
            switch (TempIndex){
                case 0:
                    Interaction="MF";
                    break;
                case 1:
                    Interaction="SF";
                    break;
                case 2:
                    Interaction="FI";
                    break;
                case 3:
                    Interaction="IF";
                    break;
                case 4:
                    Interaction="SI";
                    break;
                case 5:
                    Interaction="MI";
                    break;
                default:
            }
            return Interaction;
        }else{
            if(data[data.length-1]!=0){
                return "NE";
            }
            return null;
        }
    }


    //判断需不需要进一步分析
    public String JudgeFurtherScaleEffect(List<ArcmapForSpace> arcmapForSpaceList){
        int NumberCountry=0;
        int NumberRegion=0;
        int NumberGlobal=0;
        for(int i=0;i<arcmapForSpaceList.size();i++){
            if(arcmapForSpaceList.get(i).getScale()==0){
                NumberGlobal++;
            }
            if(arcmapForSpaceList.get(i).getScale()==2){
                NumberRegion++;
            }
            if(arcmapForSpaceList.get(i).getScale()==3){
                NumberCountry++;
            }
        }
        double NumberRegion2=NumberRegion;
        NumberRegion2=NumberRegion2/17;
        double NumberCountry2=NumberCountry;
        NumberCountry2=NumberCountry2/193;
        String JudgeResult="NO";
        if(NumberRegion2>0.5){
            JudgeResult = "YES";
        }
        if(NumberCountry2>0.5) {
            JudgeResult = "YES";
        }
        if(NumberGlobal>0.5){
            JudgeResult = "YES";
        }
        return JudgeResult;
    }
    //网络图列表里面去重求和
    public List<NetRelationship> NoRepeatToSum(List<NetRelationship> relationshipList){
        List<NetRelationship> NewNetRelationship=new ArrayList<>();
        for (int i=1;i<18;i++){
            for(int j=1;j<18;j++){
                double TempStrength=0;
                String Type=null;
                String GeoName=null;
                int Scale=0;
                for(int Ri=0;Ri<relationshipList.size();Ri++){
                    if(relationshipList.get(Ri).getGoalX().equals(String.valueOf(i))&&relationshipList.get(Ri).getGoalY().equals(String.valueOf(j))) {
                        TempStrength=TempStrength+relationshipList.get(Ri).getStrength();
                        Type=relationshipList.get(Ri).getType();
                        GeoName=relationshipList.get(Ri).getAreaName();
                        Scale=relationshipList.get(Ri).getScale();
                    }
                }
                if(Type!=null){
                    NetRelationship Relationship=new NetRelationship();
                    Relationship.setGoalX(String.valueOf(i));
                    Relationship.setGoalY(String.valueOf(j));
                    Relationship.setStrength(TempStrength);
                    Relationship.setType(Type);
                    Relationship.setAreaName(GeoName);
                    Relationship.setScale(Scale);
                    NewNetRelationship.add(Relationship);
                }

            }
        }
        return NewNetRelationship;
    }

    //判断目标对在不在筛选目标内
    public boolean JudgeIfSelectedGoalPair(String GoalX, String GoalY, List<ArcmapForResult> JudgeList)
    {
        boolean state=false;
        for(int i = 0; i<JudgeList.size();i++){
            if(JudgeList.get(i).getGoalX().equals(GoalX)&&JudgeList.get(i).getGoalY().equals(GoalY)){
                state=true;
                break;
            }
        }
        return state;
    }
    public void ListLength(List<List<ArcmapForSpace>> arcmapForSpaceList){
        int Length=0;
        for(int i=0;i<arcmapForSpaceList.size();i++){
            int Temp=0;
            for(int j=0;j<arcmapForSpaceList.get(i).size();j++){
                Temp++;
            }
            Length=Length+Temp;
        }
        System.out.println(Length);
    }

    public  int findPositionOfThirdMaxWeightOutDegree(List<NetNode> list,int j) {
        if (list == null || list.size() < 3) {
            return -1; // 返回-1表示列表为空或不足三个元素
        }
        // 对列表进行排序（降序）
        Collections.sort(list, (obj1, obj2) -> Double.compare(obj2.getWeightOutDegree(), obj1.getWeightOutDegree()));
        double thirdMaxValue = list.get(j-1).getWeightOutDegree();
        // 找到第三大值在原始列表中的位置
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getWeightOutDegree() == thirdMaxValue) {
                return i;
            }
        }
        return -1; // 如果未找到，返回-1
    }

    //最后判断形成DataS1的判断函数
    public boolean JudgeRawData(String AreaAb, String Interaction,List<String> oneRecord,List<ArcmapForSpace> AllSDGData){
        boolean JudgeState=false;
        for(int i=0;i<AllSDGData.size();i++){
            if(AllSDGData.get(i).getAreaAb().equals(AreaAb)&&AllSDGData.get(i).getInteraction().equals(Interaction))
            {
                if(AllSDGData.get(i).getGoalX().equals(oneRecord.get(0))&&AllSDGData.get(i).getGoalY().equals(oneRecord.get(6))){
                    JudgeState=true;
                }
            }
        }
        return JudgeState;

    }

}
