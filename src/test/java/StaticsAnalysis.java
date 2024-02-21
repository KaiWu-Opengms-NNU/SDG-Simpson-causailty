import com.opengms.wukai.pojo.Result.*;

import com.opengms.wukai.pojo.Result.Indicator.*;
import com.opengms.wukai.pojo.Result.NetWork.NetNode;
import com.opengms.wukai.pojo.Result.NetWork.NetRelationship;
import com.opengms.wukai.pojo.Result.Prioritize.GoalCompareResult;
import com.opengms.wukai.pojo.Result.Spatial.*;
import com.opengms.wukai.pojo.Result.anaylse.circular;
import com.opengms.wukai.tool.OperateExcel;
import com.opengms.wukai.tool.SDGIndicatorTool;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StaticsAnalysis {
    public static String Path="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal/src/test/result_spatial/";
    public static String OutPath="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal/src/test/result_spatial_per/";
    public static String OutPath2="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal/src/test/result_spatial_perlocation/";
    public static String OutPath3="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal/src/test/result_spatial_prioritize/";
    public static String OutPath4="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal/src/test/result_spatial_SP/";



    // 这是一年间隔的参数
    public static int Country_Target_Max=8;
    public static int Region_Target_Max=2;
    public static int Continent_Target_Max=1;
    public static int World_Target_Max=1;
    public static double Country_Goals_Max=42;
    public static double Region_Goals_Max=8;
    public static double Continent_Goals_MAX=1;
    public static double World_Goals_Max=4;



    public static void main(String[] args) throws IOException {
        //首先无论怎么样，先给SDG的X目标和Y目标排序
        OperateExcel operateExcel = new OperateExcel();
        List<AggregationForStatics> AggregationList = new ArrayList<>();
        List<List<String>> indicatorResultByUsing = operateExcel.readExcel2(new File("G:/WukaiBag/Year2023/sustainableofpaper2023/Indicator/IndicatorOnlyOne0818.xls"));
        //载入以世界、大洲、分区、国家四个尺度的分类序号
        int WorldNo = 1;
        int[] Continent = new int[]{2, 9, 19, 142, 150};
        int[] AfricaNo = new int[]{15, 202};
        int[] AmericaNo = new int[]{419, 21};
        int[] AsiaNo = new int[]{143, 30, 35, 34, 145};
        int[] OceaniaNo = new int[]{53, 54, 57, 61};
        int[] EuropeNo = new int[]{151, 154, 39, 155};
        //初始化树结果来分析
        List<ScaleTree> WorldDifferenceLevel = new ArrayList<>();
        //载入洲尺度
        ScaleTree World = new ScaleTree(WorldNo, "WOR", Continent, 0, 0, AggregationList);
        WorldDifferenceLevel.add(World);
        ScaleTree Africa = new ScaleTree(2, "AFR", AfricaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Africa);
        ScaleTree Oceania = new ScaleTree(9, "OCE", OceaniaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Oceania);
        ScaleTree Americas = new ScaleTree(19, "AME", AmericaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Americas);
        ScaleTree Asia = new ScaleTree(142, "ASI", AsiaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Asia);
        ScaleTree Europe = new ScaleTree(150, "EUR", EuropeNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Europe);
        //载入标准分区文档
        List<List<String>> CorrespondenceRegionAndCountry = new OperateExcel().readExcel(new File("G:/WukaiBag/Year2023/sustainableofpaper2023/reclass/标准分区文档分析程序载入.xlsx"));
        //载入分区及国家尺度
        for (int i = 1; i < WorldDifferenceLevel.size(); i++) {
            if (WorldDifferenceLevel.get(i).getLevel() == 1) {
                for (int j = 0; j < WorldDifferenceLevel.get(i).getChildrenID().length; j++) {
                    //分区尺度载入ID
                    int RegionID = WorldDifferenceLevel.get(i).getChildrenID()[j];
                    //进一步找儿子ID
                    List<String> ChildrenList = new ArrayList<>();
                    for (int Ci = 1; Ci < CorrespondenceRegionAndCountry.size(); Ci++) {
                        if (RegionID == Integer.parseInt(CorrespondenceRegionAndCountry.get(Ci).get(2))) {
                            String Code = CorrespondenceRegionAndCountry.get(Ci).get(3);
                            String ChildrenID = CorrespondenceRegionAndCountry.get(Ci).get(5);
                            ChildrenList.add(ChildrenID);
                            ScaleTree Country = new ScaleTree(Integer.parseInt(ChildrenID), Code, new int[0], RegionID, 3, AggregationList);
                            WorldDifferenceLevel.add(Country);
                        }
                    }
                    int[] ChildrenArray = new int[ChildrenList.size()];
                    for (int Ci = 0; Ci < ChildrenList.size(); Ci++) {
                        ChildrenArray[Ci] = Integer.parseInt(ChildrenList.get(Ci));
                    }
                    ScaleTree ToolTree = new ScaleTree();
                    ScaleTree Region = new ScaleTree(RegionID, ToolTree.getRegionCode(RegionID), ChildrenArray, WorldDifferenceLevel.get(i).getID(), 2, AggregationList);
                    WorldDifferenceLevel.add(Region);
                }
            }
        }


        //所有数据已分层完毕，第一步形成不同原因目标在各个尺度上显现的百分比


        //载入总体文件
        List<List<String>> AnalysisResult = operateExcel.readExcel2(new File(Path + "WorldRecord.xls"));
        //转化成类列表
        for (int i = 1; i < AnalysisResult.size()-1; i++) {

            //载入分析数据
            AggregationForStatics aggregation = new AggregationForStatics();
            //载入国家名
            aggregation.setCountryName(AnalysisResult.get(i).get(0));
            //载入国家代码
            aggregation.setCountryCode(AnalysisResult.get(i).get(1));
            int TempAbIndex = -1;
            for (int j = 0; j < WorldDifferenceLevel.size(); j++) {
                if (Integer.parseInt(aggregation.getCountryCode()) == WorldDifferenceLevel.get(j).getID()) {
                    TempAbIndex = j;
                }
            }
            if (TempAbIndex == -1) {
                continue;
            } else {
                aggregation.setCountryAb(WorldDifferenceLevel.get(TempAbIndex).getShort());
            }


            //读入不同类型的作用指标对数量
            List<List<String>> Indivisible;
            if (AnalysisResult.get(i).get(2).equals("0")) {
                Indivisible = new ArrayList<>();
            } else {
                Indivisible = operateExcel.readExcel2(new File(Path + AnalysisResult.get(i).get(0) + "_Indivisible.xls"));
            }
            aggregation.setIndivisible(Indivisible);
            List<List<String>> Cancelling;
            if (AnalysisResult.get(i).get(3).equals("0")) {
                Cancelling = new ArrayList<>();
            } else {
                Cancelling = operateExcel.readExcel2(new File(Path + AnalysisResult.get(i).get(0) + "_Cancelling.xls"));
            }
            aggregation.setCancelling(Cancelling);
            List<List<String>> ReinforcingOnly;
            if (AnalysisResult.get(i).get(4).equals("0")) {
                ReinforcingOnly = new ArrayList<>();
            } else {
                ReinforcingOnly = operateExcel.readExcel2(new File(Path + AnalysisResult.get(i).get(0) + "_ReinforingOnly.xls"));
            }
            aggregation.setReinforcingOnly(ReinforcingOnly);
            List<List<String>> CounteractingOnly;
            if (AnalysisResult.get(i).get(5).equals("0")) {
                CounteractingOnly = new ArrayList<>();
            } else {
                CounteractingOnly = operateExcel.readExcel2(new File(Path + AnalysisResult.get(i).get(0) + "_CounteractingOnly.xls"));
            }
            aggregation.setCounteractingOnly(CounteractingOnly);
            List<List<String>> ReinforcingThenCounteracting;
            if (AnalysisResult.get(i).get(6).equals("0")) {
                ReinforcingThenCounteracting = new ArrayList<>();
            } else {
                ReinforcingThenCounteracting = operateExcel.readExcel2(new File(Path + AnalysisResult.get(i).get(0) + "_FacilitationToInhibition.xls"));
            }
            aggregation.setReinforcingThenCounteracting(ReinforcingThenCounteracting);
            List<List<String>> CounteractingThenReinforcing;
            if (AnalysisResult.get(i).get(7).equals("0")) {
                CounteractingThenReinforcing = new ArrayList<>();
            } else {
                CounteractingThenReinforcing = operateExcel.readExcel2(new File(Path + AnalysisResult.get(i).get(0) + "_InhibitionToFacilitation.xls"));
            }
            aggregation.setCounteractingThenReinforcing(CounteractingThenReinforcing);
            List<List<String>> NeuralInteraction;
            if (AnalysisResult.get(i).get(8).equals("0")) {
                NeuralInteraction = new ArrayList<>();
            } else {
                NeuralInteraction = operateExcel.readExcel2(new File(Path + AnalysisResult.get(i).get(0) + "_NeutralInteraction.xls"));
            }
            aggregation.setNeuralInteraction(NeuralInteraction);
            AggregationList.add(aggregation);
            WorldDifferenceLevel.get(TempAbIndex).setAggregation(aggregation);

        }



        List<List<String>> TargetList = new ArrayList<>();
        String TempTarget = "";
        for (int i = 1; i < indicatorResultByUsing.size(); i++) {
            List<String> GoalsAndTarget = new ArrayList<>();
            GoalsAndTarget.add(indicatorResultByUsing.get(i).get(0));
            String Target = indicatorResultByUsing.get(i).get(1);
            GoalsAndTarget.add(Target);

            if (TempTarget.equals(Target)) {
                continue;
            } else {
                TargetList.add(GoalsAndTarget);
                TempTarget = Target;
            }
        }


        /** Fig2的结果
         *
         * */
        List<List<ArcmapForSpace>> AllCountryAndRegionList = new ArrayList<>();

        for (int i = 0; i < TargetList.size(); i++) {
            for (int j = 0; j < TargetList.size(); j++) {
                List<ArcmapForSpace> CountryAndRegionList = new ArrayList<>();
                for (int CountryI = 0; CountryI < WorldDifferenceLevel.size(); CountryI++) {
                    if (WorldDifferenceLevel.get(CountryI).getAggregation() != null) {
                        AggregationForStatics CountryData = WorldDifferenceLevel.get(CountryI).getAggregation();
                        int scaleI = WorldDifferenceLevel.get(CountryI).getLevel();
                        System.out.println(CountryI);
                        ArcmapForSpace arcmapForSpace = new ArcmapForSpace(CountryData.getCountryName(), CountryData.getCountryAb(), CountryData.getCountryCode(), scaleI,
                                TargetList.get(i).get(0), TargetList.get(j).get(0), TargetList.get(i).get(1), TargetList.get(j).get(1), CountryData);
                        arcmapForSpace.GetInteractionAndStrength();
                        if (arcmapForSpace.getInteraction().equals("null")==false) {
                            arcmapForSpace.setStaticsData(null);
                            CountryAndRegionList.add(arcmapForSpace);
                        }
                    }
                }
                if (CountryAndRegionList.size() > 0) {
                    AllCountryAndRegionList.add(CountryAndRegionList);
                }
            }
        }

        List<List<ArcmapForSpace>> DifferentScaleList = new ArrayList<>();
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                System.out.println(i);
                List<ArcmapForSpace> CountryAndRegionList_Goals = new ArrayList<>();
                for (int countryI = 0; countryI < AllCountryAndRegionList.size(); countryI++) {
                    List<ArcmapForSpace> CountryAndRegion_Targets = AllCountryAndRegionList.get(countryI);
                    for (int Index = 0; Index < CountryAndRegion_Targets.size(); Index++) {
                        ArcmapForSpace TargetPair = CountryAndRegion_Targets.get(Index);
                        //判断是否和目标相等
                        if (i == Integer.parseInt(TargetPair.getGoalX()) && j == Integer.parseInt(TargetPair.getGoalY())) {
                            //如果相同，就判断有没有相同区域
                            String Id = TargetPair.getAreaID();
                            int AllListIndex = -1;
                            for (int ListI = 0; ListI < CountryAndRegionList_Goals.size(); ListI++) {
                                if (CountryAndRegionList_Goals.get(ListI).getAreaID().equals(String.valueOf(Id))) {
                                    //重复指标游标载入
                                    AllListIndex = ListI;
                                }
                            }
                            if (AllListIndex != -1) {
                                //说明存在这个区域
                                if (TargetPair.getStrengthAll() > CountryAndRegionList_Goals.get(AllListIndex).getStrengthAll()) {
                                    CountryAndRegionList_Goals.get(AllListIndex).SetValue(TargetPair);
                                }
                            } else {
                                CountryAndRegionList_Goals.add(TargetPair);
                            }
                        }
                    }
                }

                //开始计算不同层级的方差，需要在不同层级
                List<ArcmapForSpace> newCountryAndRegionList_Goals = new ArrayList<>();
                //用一个类链表记录变化的类型和方差
                for (int countryI = 0; countryI < CountryAndRegionList_Goals.size(); countryI++) {
                    ArcmapForSpace oneRegion = CountryAndRegionList_Goals.get(countryI);
                    int ID = Integer.parseInt(oneRegion.getAreaID());
                    int Scale = oneRegion.getScale();
                    SDGIndicatorTool tool = new SDGIndicatorTool();
                    //如果是第三层级，计算方程为其值—父集区域值——父集区域值
                    if (Scale == 3) {
                        int ParentRegionID = tool.FindParentID(WorldDifferenceLevel, ID);
                        int ParentWorldID = 1;
                        int[] CountryAndRegion = {ID, ParentRegionID, ParentWorldID};
                        List<ArcmapForSpace> TempArcmapForSpace = new ArrayList<>();
                        //找到存在父集区域
                        String CountryInteraction = oneRegion.getInteraction();
                        int CountryNumber = 1;
                        String RegionInteraction = null;
                        int RegionNumber = 0;
                        String WorldInteraction = null;
                        int WorldNumber = 0;
                        for (int scaleI = 0; scaleI < CountryAndRegion.length; scaleI++) {
                            ArcmapForSpace arcmapForSpace = tool.FindArcMapID(CountryAndRegionList_Goals, CountryAndRegion[scaleI]);
                            if (arcmapForSpace != null) {
                                TempArcmapForSpace.add(arcmapForSpace);
                                if (arcmapForSpace.getScale() == 2) {
                                    RegionInteraction = arcmapForSpace.getInteraction();
                                    RegionNumber = 1;
                                }
                                if (arcmapForSpace.getScale() == 0) {
                                    WorldInteraction = arcmapForSpace.getInteraction();
                                    WorldNumber = 1;
                                }
                            }
                        }
                        double[] TempCalculate = new double[TempArcmapForSpace.size()];
                        for (int countryJ = 0; countryJ < TempArcmapForSpace.size(); countryJ++) {
                            TempCalculate[countryJ] = TempArcmapForSpace.get(countryJ).getStrengthWithDirection();
                        }
                        double InteractionVar = tool.CalculateVarianceByInteraction(TempArcmapForSpace);
                        double StrengthVar = tool.CalculateVariance(TempCalculate);
                        oneRegion.SetValueByVariance(TempArcmapForSpace.size(), InteractionVar, StrengthVar, CountryInteraction,
                                1, CountryNumber, RegionInteraction, RegionNumber, WorldInteraction, WorldNumber);
                    }
                    //如果是第二层级，计算方程为子集平均值——区域值——父集区域值
                    if (Scale == 2) {
                        int[] childrenID = tool.FindChildrenIDArray(WorldDifferenceLevel, ID);
                        //首先根据childrenID找到子区域的所有对象
                        List<ArcmapForSpace> TempArcmapForChildrenList = new ArrayList<>();
                        int[] TempInteractionList = new int[]{0, 0, 0, 0, 0, 0, 0};

                        for (int cI = 0; cI < childrenID.length; cI++) {
                            //找到该区域下面的所有对象
                            ArcmapForSpace arcmapForSpace = tool.FindArcMapID(CountryAndRegionList_Goals, childrenID[cI]);
                            if (arcmapForSpace != null) {
                                TempArcmapForChildrenList.add(arcmapForSpace);
                                String TempInteraction = arcmapForSpace.getInteraction();
                                switch (TempInteraction) {
                                    case "MF":
                                        TempInteractionList[0]++;
                                        break;
                                    case "SF":
                                        TempInteractionList[1]++;
                                        break;
                                    case "FI":
                                        TempInteractionList[2]++;
                                        break;
                                    case "IF":
                                        TempInteractionList[3]++;
                                        break;
                                    case "SI":
                                        TempInteractionList[4]++;
                                        break;
                                    case "MI":
                                        TempInteractionList[5]++;
                                        break;
                                    case "NE":
                                        TempInteractionList[6]++;
                                        break;
                                    default:
                                }

                            }
                        }
                        //
                        String CountryInteraction = tool.GetMaxCountryNumber(TempInteractionList);
                        int CountryNumber = TempArcmapForChildrenList.size();
                        double StrengthofCountryInteraction = 0;
                        //需要求国家层面的某种作用类型的强度
                        int CountryNumberThisInteraction=0;
                        if (TempArcmapForChildrenList.size() != 0) {
                            for (int Ti = 0; Ti < TempArcmapForChildrenList.size(); Ti++) {
                                if (TempArcmapForChildrenList.get(Ti).getInteraction().equals(CountryInteraction)) {
                                    StrengthofCountryInteraction = StrengthofCountryInteraction + TempArcmapForChildrenList.get(Ti).getStrengthWithDirection();
                                    CountryNumberThisInteraction++;
                                }
                            }
                            StrengthofCountryInteraction = StrengthofCountryInteraction / CountryNumberThisInteraction;
                        }
                        String RegionInteraction = CountryAndRegionList_Goals.get(countryI).getInteraction();
                        int RegionNumber = 1;
                        double StrengthofRegionInteraction = CountryAndRegionList_Goals.get(countryI).getStrengthWithDirection();
                        String WorldInteraction = null;
                        int WorldNumber = 0;
                        double StrengthofWorldInteraction = 0;
                        for (int countryJ = 0; countryJ < CountryAndRegionList_Goals.size(); countryJ++) {
                            if (CountryAndRegionList_Goals.get(countryJ).getScale() == 0) {
                                WorldInteraction = CountryAndRegionList_Goals.get(countryJ).getInteraction();
                                WorldNumber = 1;
                                StrengthofWorldInteraction = CountryAndRegionList_Goals.get(countryJ).getStrengthWithDirection();
                                break;
                            }
                        }
                        //开始计算
                        String[] TempInteractionListForDifferentScale = null;
                        double[] TempInteractionStrengthForDifferentScale = null;
                        if (CountryNumber > 0) {
                            //这里进行了修改，把上级world给删去了,注意
                            TempInteractionListForDifferentScale = new String[]{RegionInteraction, CountryInteraction};
                            TempInteractionStrengthForDifferentScale = new double[]{StrengthofRegionInteraction, StrengthofCountryInteraction};
                        }else {
                            TempInteractionListForDifferentScale = new String[]{RegionInteraction};
                            TempInteractionStrengthForDifferentScale = new double[]{StrengthofRegionInteraction};
                        }
                        //计算作用类型方差和强度方程
                        if (TempInteractionListForDifferentScale.length > 1) {
                            double InteractionVar = tool.CalculateVarianceByInteraction(TempInteractionListForDifferentScale);
                            double StrengthVar = tool.CalculateVariance(TempInteractionStrengthForDifferentScale);
                            oneRegion.SetValueByVariance(TempInteractionListForDifferentScale.length, InteractionVar, StrengthVar,
                                    CountryInteraction, CountryNumber,CountryNumberThisInteraction, RegionInteraction, RegionNumber, WorldInteraction, WorldNumber);
                            CountryAndRegionList_Goals.get(countryI).setTypeAndStrength(TempInteractionListForDifferentScale,TempInteractionStrengthForDifferentScale);
                        } else {
                            oneRegion.SetValueByVariance(1, 0, 0, null, 0,0, RegionInteraction, RegionNumber, WorldInteraction , WorldNumber);
                        }
                    }
                    //如果是第一层级，计算方程为子集平均值——区域平均值——父集值
                    if (Scale == 0) {
                        String WorldInteraction = CountryAndRegionList_Goals.get(countryI).getInteraction();
                        int WorldNumber = 1;
                        double StrengthofWorldInteraction = CountryAndRegionList_Goals.get(countryI).getStrengthWithDirection();
                        String RegionInteraction = null;
                        int RegionNumber = 0;
                        double StrengthofRegionInteraction = 0;
                        String CountryInteraction = null;
                        int CountryNumber = 0;
                        double StrengthofCountryInteraction = 0;
                        int[] TempRegionInteractionList = new int[]{0, 0, 0, 0, 0, 0,0};
                        int[] TempCountryInteractionList = new int[]{0, 0, 0, 0, 0, 0,0};
                        //先找到每个层级上最多的交互作用类型
                        for (int countryJ = 0; countryJ < CountryAndRegionList_Goals.size(); countryJ++) {
                            ArcmapForSpace arcmapForSpace = CountryAndRegionList_Goals.get(countryJ);
                            if (arcmapForSpace.getScale() == 2) {
                                String TempInteraction = arcmapForSpace.getInteraction();
                                switch (TempInteraction) {
                                    case "MF":
                                        TempRegionInteractionList[0]++;
                                        break;
                                    case "SF":
                                        TempRegionInteractionList[1]++;
                                        break;
                                    case "FI":
                                        TempRegionInteractionList[2]++;
                                        break;
                                    case "IF":
                                        TempRegionInteractionList[3]++;
                                        break;
                                    case "SI":
                                        TempRegionInteractionList[4]++;
                                        break;
                                    case "MI":
                                        TempRegionInteractionList[5]++;
                                        break;
                                    case "NE":
                                        TempRegionInteractionList[6]++;
                                        break;
                                    default:
                                }
                            }
                            if (arcmapForSpace.getScale() == 3) {
                                String TempInteraction = arcmapForSpace.getInteraction();
                                switch (TempInteraction) {
                                    case "MF":
                                        TempCountryInteractionList[0]++;
                                        break;
                                    case "SF":
                                        TempCountryInteractionList[1]++;
                                        break;
                                    case "FI":
                                        TempCountryInteractionList[2]++;
                                        break;
                                    case "IF":
                                        TempCountryInteractionList[3]++;
                                        break;
                                    case "SI":
                                        TempCountryInteractionList[4]++;
                                        break;
                                    case "MI":
                                        TempCountryInteractionList[5]++;
                                        break;
                                    case "NE":
                                        TempCountryInteractionList[6]++;
                                        break;
                                    default:
                                }
                            }
                        }
                        RegionInteraction = tool.GetMaxCountryNumber(TempRegionInteractionList);
                        CountryInteraction = tool.GetMaxCountryNumber(TempCountryInteractionList);
                        for (int countryJ = 0; countryJ < CountryAndRegionList_Goals.size(); countryJ++) {
                            ArcmapForSpace arcmapForSpace = CountryAndRegionList_Goals.get(countryJ);
                            if (arcmapForSpace.getScale() == 2 && arcmapForSpace.getInteraction().equals(RegionInteraction)) {
                                StrengthofRegionInteraction = StrengthofRegionInteraction + arcmapForSpace.getStrengthWithDirection();
                                RegionNumber++;
                            }
                            if (arcmapForSpace.getScale() == 3 && arcmapForSpace.getInteraction().equals(CountryInteraction)) {
                                StrengthofCountryInteraction = StrengthofCountryInteraction + arcmapForSpace.getStrengthWithDirection();
                                CountryNumber++;
                            }
                        }
                        StrengthofRegionInteraction = StrengthofRegionInteraction / RegionNumber;
                        StrengthofCountryInteraction = StrengthofCountryInteraction / CountryNumber;
                        String[] TempInteractionListForDifferentScale = null;
                        double[] TempInteractionStrengthForDifferentScale = null;
                        //这里也是国家尺度给去除了
                        if (RegionNumber > 0) {
                            TempInteractionListForDifferentScale = new String[]{WorldInteraction, RegionInteraction};
                            TempInteractionStrengthForDifferentScale = new double[]{StrengthofWorldInteraction, StrengthofRegionInteraction};
                        }  else {
                            TempInteractionListForDifferentScale = new String[]{WorldInteraction};
                            TempInteractionStrengthForDifferentScale = new double[]{StrengthofWorldInteraction};
                        }
                        if (TempInteractionListForDifferentScale.length > 1) {
                            double InteractionVar = tool.CalculateVarianceByInteraction(TempInteractionListForDifferentScale);
                            double StrengthVar = tool.CalculateVariance(TempInteractionStrengthForDifferentScale);
                            CountryAndRegionList_Goals.get(countryI).SetValueByVariance(TempInteractionListForDifferentScale.length, InteractionVar, StrengthVar,
                                    CountryInteraction, 193,CountryNumber, RegionInteraction, RegionNumber, WorldInteraction, WorldNumber);
                            CountryAndRegionList_Goals.get(countryI).setTypeAndStrength(TempInteractionListForDifferentScale,TempInteractionStrengthForDifferentScale);
                        } else {
                            oneRegion.SetValueByVariance(1, 0, 0, null, 0,0 ,null, 0, WorldInteraction, 1);
                        }
                    }
                    if(oneRegion.getScale()!=1){
                        newCountryAndRegionList_Goals.add(oneRegion);
                    }
                }
                if (CountryAndRegionList_Goals.size() > 0) {
                    String FileName = "FromSDG" + i + "toSDG" + j;
                    operateExcel.writeExcel(OutPath2, FileName, ArcmapForSpace.class, newCountryAndRegionList_Goals);
                }
                DifferentScaleList.add(newCountryAndRegionList_Goals);
            }
        }
        new SDGIndicatorTool().ListLength(DifferentScaleList);
        //用一个类链表记录统计结果
        List<ArcmapForResult> arcmapForResultList = new ArrayList<>();

        //三个尺度需要有任一一个尺度大于百分之30%才可以用于进一步的分析
        for (int i = 0; i < DifferentScaleList.size(); i++) {
            List<ArcmapForSpace> arcmapForSpaceList = DifferentScaleList.get(i);
            //首先是需要判断要不要进一步分析
            if (new SDGIndicatorTool().JudgeFurtherScaleEffect(arcmapForSpaceList).equals("NO")) {
                continue;
            }
            //以指标对为单位形成统计图
            if(arcmapForSpaceList.size()>0){
                ArcmapForResult arcmapForResult = new ArcmapForResult(arcmapForSpaceList.get(0).getGoalX(), arcmapForSpaceList.get(0).getGoalY());

                int TempScaleNumber = 0;
                int TempDifferentScaleNumber = 0;
                int TempScaleNumber_G = 0;
                int TempDifferentScaleNumber_G = 0;
                int TempScaleNumber_R = 0;
                int TempDifferentScaleNumber_R = 0;
                int TempScaleNumber_C = 0;
                int TempDifferentScaleNumber_C = 0;
                for (int j = 0; j < arcmapForSpaceList.size(); j++) {
                    //先每个指标发生变化的比例 然后从小到大排序
                    if (arcmapForSpaceList.get(j).getPairLevel() > 1) {
                        TempScaleNumber++;
                        double JudgeValue=0;
                        if (arcmapForSpaceList.get(j).getInteractionVar() != JudgeValue) {
                            TempDifferentScaleNumber++;
                        }
                        //不同层级
                        if(arcmapForSpaceList.get(j).getScale()==0){
                            TempScaleNumber_G++;
                            if (arcmapForSpaceList.get(j).getInteractionVar() != JudgeValue) {
                                TempDifferentScaleNumber_G++;
                            }
                        }
                        if(arcmapForSpaceList.get(j).getScale()==2){
                            TempScaleNumber_R++;
                            if (arcmapForSpaceList.get(j).getInteractionVar() != JudgeValue) {
                                TempDifferentScaleNumber_R++;
                            }
                        }
                        if(arcmapForSpaceList.get(j).getScale()==3){
                            TempScaleNumber_C ++;
                            if (arcmapForSpaceList.get(j).getInteractionVar() != JudgeValue) {
                                TempDifferentScaleNumber_C++;
                            }
                        }

                    }else{
                        TempScaleNumber++;
                        if(arcmapForSpaceList.get(j).getScale()==0){
                            TempScaleNumber_G++;
                        }
                        if(arcmapForSpaceList.get(j).getScale()==2){
                            TempScaleNumber_R++;
                        }
                        if(arcmapForSpaceList.get(j).getScale()==3){
                            TempScaleNumber_C ++;
                        }
                    }
                    arcmapForResult.setNumberForMultiScale_All(TempScaleNumber);
                    arcmapForResult.setNumberForDifferent_All(TempDifferentScaleNumber);
                    if(TempScaleNumber!=0){
                        double RatioAll=(float)TempDifferentScaleNumber/TempScaleNumber;
                        arcmapForResult.setRatioForDifferent_All(RatioAll);
                    }else{
                        arcmapForResult.setRatioForDifferent_All(0);
                    }
                    //设置全球
                    arcmapForResult.setNumberForMultiScale_Global(TempScaleNumber_G);
                    arcmapForResult.setNumberForDifferent_Global(TempDifferentScaleNumber_G);
                    if(TempScaleNumber_G!=0){
                        double RatioAll_G=(float)TempDifferentScaleNumber_G/TempScaleNumber_G;
                        arcmapForResult.setRatioForDifferent_Global(RatioAll_G);
                    }else{
                        arcmapForResult.setRatioForDifferent_Global(0);
                    }
                    //设置区域
                    arcmapForResult.setNumberForMultiScale_Region(TempScaleNumber_R);
                    arcmapForResult.setNumberForDifferent_Region(TempDifferentScaleNumber_R);
                    if(TempScaleNumber_R!=0){
                        double RatioAll_R=(float)TempDifferentScaleNumber_R/TempScaleNumber_R;
                        arcmapForResult.setRatioForDifferent_Region(RatioAll_R);
                    }else{
                        arcmapForResult.setRatioForDifferent_Region(0);
                    }
                    //设置国家
                    arcmapForResult.setNumberForMultiScale_Country(TempScaleNumber_C);
                    arcmapForResult.setNumberForDifferent_Country(TempDifferentScaleNumber_C);
                    if(TempScaleNumber_C!=0){
                        double RatioAll_C=(float)TempDifferentScaleNumber_C/TempScaleNumber_C;
                        arcmapForResult.setRatioForDifferent_Country(RatioAll_C);
                    }else{
                        arcmapForResult.setRatioForDifferent_Country(0);
                    }

                }
                int CountryNumber=0;
                //再遍历一遍载入作用变化和强度变化
                for (int j = 0; j < arcmapForSpaceList.size(); j++){
                    //说明在全球尺度上存在辛普森效应
                    if(arcmapForSpaceList.get(j).getScale()==0&&arcmapForSpaceList.get(j).getInteractionVar()==1){
                        arcmapForResult.setOriginalType(arcmapForSpaceList.get(j).getOriginalType());
                        arcmapForResult.setChangeType(arcmapForSpaceList.get(j).getChangeType());
                        arcmapForResult.setOriginalStrength(arcmapForSpaceList.get(j).getOriginalStrength());
                        arcmapForResult.setChangeStrength(arcmapForSpaceList.get(j).getChangeStrength());
                        break;
                    }

                    if(arcmapForSpaceList.get(j).getScale()==2&&arcmapForSpaceList.get(j).getInteractionVar()==1){
                        if(arcmapForSpaceList.get(j).getCountryNumber()>CountryNumber){
                            CountryNumber=arcmapForSpaceList.get(j).getCountryNumber();
                            arcmapForResult.setOriginalType(arcmapForSpaceList.get(j).getOriginalType());
                            arcmapForResult.setChangeType(arcmapForSpaceList.get(j).getChangeType());
                            arcmapForResult.setOriginalStrength(arcmapForSpaceList.get(j).getOriginalStrength());
                            arcmapForResult.setChangeStrength(arcmapForSpaceList.get(j).getChangeStrength());
                        }
                    }
                }
                arcmapForResultList.add(arcmapForResult);
            }

        }
        //以国家为单位形成方差空间分布图
        for(int i=0;i<WorldDifferenceLevel.size();i++){
            int ZoneID=WorldDifferenceLevel.get(i).getID();
            ArcmapForSpace TempArcmapForSpace=new ArcmapForSpace();
            for(int j=0;j<DifferentScaleList.size();j++){
                List<ArcmapForSpace> arcmapForSpaceList = DifferentScaleList.get(j);
                if (new SDGIndicatorTool().JudgeFurtherScaleEffect(arcmapForSpaceList).equals("NO")) {
                    continue;
                }
                if(arcmapForSpaceList.size()>0){
                    for(int h=0;h<arcmapForSpaceList.size();h++){
                        if(TempArcmapForSpace==null&&ZoneID==Integer.parseInt(arcmapForSpaceList.get(h).getAreaID())){
                            if(arcmapForSpaceList.get(h).getInteractionVar()>0){
                                TempArcmapForSpace=arcmapForSpaceList.get(h);
                            }
                        }else{
                            if(TempArcmapForSpace.getStrengthVar()<arcmapForSpaceList.get(h).getStrengthVar()&&ZoneID==Integer.parseInt(arcmapForSpaceList.get(h).getAreaID())){
                                if(arcmapForSpaceList.get(h).getInteractionVar()>0){
                                    TempArcmapForSpace=arcmapForSpaceList.get(h);
                                }
                            }
                        }
                    }

                }
            }
        }
        if (arcmapForResultList.size() > 0) {
            operateExcel.writeExcel(OutPath2, "DifferentScaleList", ArcmapForResult.class, arcmapForResultList);
        }

        List<ArcmapForSpace> AllSDGList=new ArrayList<>();
        for(int i=0;i<DifferentScaleList.size();i++){
            List<ArcmapForSpace> arcmapForSpaceList = DifferentScaleList.get(i);
            //首先是需要判断要不要进一步分析
            if (new SDGIndicatorTool().JudgeFurtherScaleEffect(arcmapForSpaceList).equals("NO")) {
                continue;
            }
            for(int j=0;j<DifferentScaleList.get(i).size();j++){
                AllSDGList.add(DifferentScaleList.get(i).get(j));
            }
        }
        if(AllSDGList.size()>0){
            operateExcel.writeExcel(OutPath2,"AllSDGList",ArcmapForSpace.class, AllSDGList);
        }

        /*补充评价*/
        List<circular> CircularList= new ArrayList<>();
        for(int SDGi=1;SDGi<18;SDGi++){
            circular NN=new circular();
            NN.setGoalX(String.valueOf("SDG"+SDGi));
            NN.setInteraction("NE");
            circular SF=new circular();
            SF.setGoalX(String.valueOf("SDG"+SDGi));
            SF.setInteraction("SF");
            circular SI=new circular();
            SI.setGoalX(String.valueOf("SDG"+SDGi));
            SI.setInteraction("SI");
            circular MF=new circular();
            MF.setGoalX(String.valueOf("SDG"+SDGi));
            MF.setInteraction("MF");
            circular MI=new circular();
            MI.setGoalX(String.valueOf("SDG"+SDGi));
            MI.setInteraction("MI");
            circular FI=new circular();
            FI.setGoalX(String.valueOf("SDG"+SDGi));
            FI.setInteraction("FI");
            circular IF=new circular();
            IF.setGoalX(String.valueOf("SDG"+SDGi));
            IF.setInteraction("IF");
            double AllNumberWorld=0;
            double AllNumberRegion=0;
            double AllNumberCountry=0;
            for(int i=0;i<AllSDGList.size();i++){
                if(AllSDGList.get(i).getGoalX().equals(String.valueOf(SDGi))){
                    if(AllSDGList.get(i).getScale()==0){
                        AllNumberWorld++;
                        switch (AllSDGList.get(i).getInteraction()){
                            case "NE":
                                NN.setWorldN(NN.getWorldN()+1);
                                break;
                            case "SF":
                                SF.setWorldN(SF.getWorldN()+1);
                                break;
                            case "SI":
                                SI.setWorldN(SI.getWorldN()+1);
                                break;
                            case "MF":
                                MF.setWorldN(MF.getWorldN()+1);
                                break;
                            case "MI":
                                MI.setWorldN(MI.getWorldN()+1);
                                break;
                            case "FI":
                                FI.setWorldN(FI.getWorldN()+1);
                                break;
                            case "IF":
                                IF.setWorldN(IF.getWorldN()+1);
                                break;
                            default:
                        }

                    }
                    if(AllSDGList.get(i).getScale()==2){
                        AllNumberRegion++;
                        switch (AllSDGList.get(i).getInteraction()){
                            case "NE":
                                NN.setRegionN(NN.getRegionN()+1);
                                break;
                            case "SF":
                                SF.setRegionN(SF.getRegionN()+1);
                                break;
                            case "SI":
                                SI.setRegionN(SI.getRegionN()+1);
                                break;
                            case "MF":
                                MF.setRegionN(MF.getRegionN()+1);
                                break;
                            case "MI":
                                MI.setRegionN(MI.getRegionN()+1);
                                break;
                            case "FI":
                                FI.setRegionN(FI.getRegionN()+1);
                                break;
                            case "IF":
                                IF.setRegionN(IF.getRegionN()+1);
                                break;
                            default:
                        }
                    }
                    if(AllSDGList.get(i).getScale()==3){
                        AllNumberCountry++;
                        switch (AllSDGList.get(i).getInteraction()){
                            case "NE":
                                NN.setCountryN(NN.getCountryN()+1);
                                break;
                            case "SF":
                                SF.setCountryN(SF.getCountryN()+1);
                                break;
                            case "SI":
                                SI.setCountryN(SI.getCountryN()+1);
                                break;
                            case "MF":
                                MF.setCountryN(MF.getCountryN()+1);
                                break;
                            case "MI":
                                MI.setCountryN(MI.getCountryN()+1);
                                break;
                            case "FI":
                                FI.setCountryN(FI.getCountryN()+1);
                                break;
                            case "IF":
                                IF.setCountryN(IF.getCountryN()+1);
                                break;
                            default:
                        }

                    }
                }
                if (AllNumberWorld!=0){
                    NN.setWorldP(NN.getWorldN()/AllNumberWorld);
                    SF.setWorldP(SF.getWorldN()/AllNumberWorld);
                    SI.setWorldP(SI.getWorldN()/AllNumberWorld);
                    MF.setWorldP(MF.getWorldN()/AllNumberWorld);
                    MI.setWorldP(MI.getWorldN()/AllNumberWorld);
                    FI.setWorldP(FI.getWorldN()/AllNumberWorld);
                    IF.setWorldP(IF.getWorldN()/AllNumberWorld);
                }
                if(AllNumberRegion!=0){
                    NN.setRegionP(NN.getRegionN()/AllNumberRegion);
                    SF.setRegionP(SF.getRegionN()/AllNumberRegion);
                    SI.setRegionP(SI.getRegionN()/AllNumberRegion);
                    MF.setRegionP(MF.getRegionN()/AllNumberRegion);
                    MI.setRegionP(MI.getRegionN()/AllNumberRegion);
                    FI.setRegionP(FI.getRegionN()/AllNumberRegion);
                    IF.setRegionP(IF.getRegionN()/AllNumberRegion);
                }
                if(AllNumberCountry!=0){
                    NN.setCountryP(NN.getCountryN()/AllNumberCountry);
                    SF.setCountryP(SF.getCountryN()/AllNumberCountry);
                    SI.setCountryP(SI.getCountryN()/AllNumberCountry);
                    MF.setCountryP(MF.getCountryN()/AllNumberCountry);
                    MI.setCountryP(MI.getCountryN()/AllNumberCountry);
                    FI.setCountryP(FI.getCountryN()/AllNumberCountry);
                    IF.setCountryP(IF.getCountryN()/AllNumberCountry);
                }
            }
            CircularList.add(NN);
            CircularList.add(SF);
            CircularList.add(SI);
            CircularList.add(MF);
            CircularList.add(MI);
            CircularList.add(FI);
            CircularList.add(IF);
        }
        if(CircularList.size()>0){
            operateExcel.writeExcel(OutPath2,"circularList",circular.class, CircularList);
        }
        System.out.println("success");

        /*Fig1绘制网络图*/
        List<LinkForNumber> CountryNumberHeatMap=new ArrayList<>();
        List<LinkForNumber> RegionNumberHeatMap=new ArrayList<>();
        List<LinkForNumber> WorldNumberHeatMap=new ArrayList<>();
        for(int x=1;x<18;x++){
            for(int y=1;y<18;y++){
                LinkForNumber CountryN=new LinkForNumber();
                LinkForNumber RegionN=new LinkForNumber();
                LinkForNumber WorldN=new LinkForNumber();
                CountryN.setGoalX(x);
                RegionN.setGoalX(x);
                WorldN.setGoalX(x);
                CountryN.setGoalY(y);
                RegionN.setGoalY(y);
                WorldN.setGoalY(y);
                for (int i=0;i<AllSDGList.size();i++){
                    if(AllSDGList.get(i).getScale()==3){
                        if(AllSDGList.get(i).getGoalX().equals(String.valueOf(x))&&AllSDGList.get(i).getGoalY().equals(String.valueOf(y))){
                            CountryN.SetNumber(AllSDGList.get(i).getInteraction(),AllSDGList.get(i).getStrengthAll());
                        }
                    }
                    if(AllSDGList.get(i).getScale()==2){
                        if(AllSDGList.get(i).getGoalX().equals(String.valueOf(x))&&AllSDGList.get(i).getGoalY().equals(String.valueOf(y))){
                            RegionN.SetNumber(AllSDGList.get(i).getInteraction(),AllSDGList.get(i).getStrengthAll());
                        }
                    }
                    if(AllSDGList.get(i).getScale()==0){
                        if(AllSDGList.get(i).getGoalX().equals(String.valueOf(x))&&AllSDGList.get(i).getGoalY().equals(String.valueOf(y))){
                            WorldN.SetNumber(AllSDGList.get(i).getInteraction(),AllSDGList.get(i).getStrengthAll());
                        }
                    }
                }
                CountryN.Calculate();
                RegionN.Calculate();
                WorldN.Calculate();
                CountryNumberHeatMap.add(CountryN);
                RegionNumberHeatMap.add(RegionN);
                WorldNumberHeatMap.add(WorldN);
            }
        }
        if(CountryNumberHeatMap.size()>0){
            operateExcel.writeExcel(OutPath,"CountryHeatMap",LinkForNumber.class, CountryNumberHeatMap);
        }
        if (RegionNumberHeatMap.size()>0){
            operateExcel.writeExcel(OutPath,"RegionHeatMap",LinkForNumber.class, RegionNumberHeatMap);
        }
        if (WorldNumberHeatMap.size()>0){
            operateExcel.writeExcel(OutPath,"WorldHeatMap",LinkForNumber.class, WorldNumberHeatMap);
        }
        List<List<GoalCompareResult>> ReClassGoalForDifferentSpatial=new ArrayList<>();
        for(int i=1;i<18;i++){
            List<GoalCompareResult> CompareResultList=new ArrayList<>();
            for(int j=1;j<18;j++){
                if(i==j){
                    continue;
                }
                GoalCompareResult OnePair=new GoalCompareResult();
                OnePair.setGoalPair(i,j);
                //全球尺度
                OnePair.setScale(1);
                double AllStrengthOfPair=0;
                double AverageRSquare=0;
                int TempNum=0;
                for(int WorldI=0;WorldI< AllSDGList.size();WorldI++){
                    if(Integer.parseInt( AllSDGList.get(WorldI).getGoalX()) ==i&&Integer.parseInt( AllSDGList.get(WorldI).getGoalY())==j){
                        if(AllSDGList.get(WorldI).getScale()==0){
                            AllStrengthOfPair=AllStrengthOfPair+ AllSDGList.get(WorldI).getStrengthWithDirection();
                            AverageRSquare=AverageRSquare+ AllSDGList.get(WorldI).getStrengthAllRSquared();
                            TempNum=TempNum+1;
                        }
                    }
                }

                if(TempNum==0){
                    OnePair.setAllStrengthOfPair(0);
                    OnePair.setAverageRSquare(0);
                }else{
                    OnePair.setAllStrengthOfPair(AllStrengthOfPair/TempNum);
                    OnePair.setAverageRSquare(AverageRSquare/TempNum);
                }
                CompareResultList.add(OnePair);
                //分区尺度
                GoalCompareResult OnePair2=new GoalCompareResult();
                OnePair2.setGoalPair(i,j);
                OnePair2.setScale(2);
                double AllStrengthOfPair2=0;
                double AverageRSquare2=0;
                int TempNum2=0;
                for(int RegionI=0;RegionI<AllSDGList.size();RegionI++){
                    if(Integer.parseInt(AllSDGList.get(RegionI).getGoalX()) ==i&&Integer.parseInt(AllSDGList.get(RegionI).getGoalY())==j){
                        if(AllSDGList.get(RegionI).getScale()==2){
                            AllStrengthOfPair2=AllStrengthOfPair2+AllSDGList.get(RegionI).getStrengthWithDirection();
                            AverageRSquare2=AverageRSquare2+AllSDGList.get(RegionI).getStrengthAllRSquared();
                            TempNum2=TempNum2+1;
                        }
                    }
                }

                if(TempNum2==0){
                    OnePair2.setAllStrengthOfPair(0);
                    OnePair2.setAverageRSquare(0);
                }else{
                    OnePair2.setAllStrengthOfPair(AllStrengthOfPair2/TempNum2);
                    OnePair2.setAverageRSquare(AverageRSquare2/TempNum2);
                }
                CompareResultList.add(OnePair2);
                //国家尺度
                GoalCompareResult OnePair3=new GoalCompareResult();
                OnePair3.setGoalPair(i,j);
                OnePair3.setScale(3);
                double AllStrengthOfPair3=0;
                double AverageRSquare3=0;
                int TempNum3=0;
                for(int CountryI=0;CountryI<AllSDGList.size();CountryI++){
                    if(Integer.parseInt(AllSDGList.get(CountryI).getGoalX()) ==i&&Integer.parseInt(AllSDGList.get(CountryI).getGoalY())==j){
                        if(AllSDGList.get(CountryI).getScale()==3){
                            AllStrengthOfPair3=AllStrengthOfPair3+AllSDGList.get(CountryI).getStrengthWithDirection();
                            AverageRSquare3=AverageRSquare3+AllSDGList.get(CountryI).getStrengthAllRSquared();
                            TempNum3=TempNum3+1;
                        }
                    }
                }

                if(TempNum3==0){
                    OnePair3.setAllStrengthOfPair(0);
                    OnePair3.setAverageRSquare(0);
                }else{
                    OnePair3.setAllStrengthOfPair(AllStrengthOfPair3/TempNum3);
                    OnePair3.setAverageRSquare(AverageRSquare3/TempNum3);
                }
                CompareResultList.add(OnePair3);
            }
            ReClassGoalForDifferentSpatial.add(CompareResultList);
            //operateExcel.writeExcel(OutPath3,"SDG"+i,GoalCompareResult.class,CompareResultList);
        }

        //Fig5
        //构造生成网络的数据
        double [][] WMFNet=new double[17][17];
        double [][] WMINet=new double[17][17];
        double [][] WSFNet=new double[17][17];
        double [][] WSINet=new double[17][17];
        double [][] WFINet=new double[17][17];
        double [][] WIFNet=new double[17][17];
        double [][] RMFNet=new double[17][17];
        double [][] RMINet=new double[17][17];
        double [][] RSFNet=new double[17][17];
        double [][] RSINet=new double[17][17];
        double [][] RFINet=new double[17][17];
        double [][] RIFNet=new double[17][17];
        double [][] CMFNet=new double[17][17];
        double [][] CMINet=new double[17][17];
        double [][] CSFNet=new double[17][17];
        double [][] CSINet=new double[17][17];
        double [][] CFINet=new double[17][17];
        double [][] CIFNet=new double[17][17];


        for (int i = 1; i <= 17; i++) {
            for (int j = 1; j <= 17; j++) {
                String GoalX = String.valueOf(i);
                String GoalY = String.valueOf(j);
                LinkForPercentGoals WMF = new LinkForPercentGoals(GoalX, GoalY, "MF", 0, AllSDGList);
                if (WMF.getSingleInteractionNumber() > 0) {
                    WMFNet[i-1][j-1]=WMF.getAveStrength();
                }else{
                    WMFNet[i-1][j-1]=0;
                }
                LinkForPercentGoals WMI = new LinkForPercentGoals(GoalX, GoalY, "MI", 0, AllSDGList);
                if (WMI.getSingleInteractionNumber() > 0) {
                    WMINet[i-1][j-1]=WMI.getAveStrength();
                }else{
                    WMINet[i-1][j-1]=0;
                }
                LinkForPercentGoals WSF = new LinkForPercentGoals(GoalX, GoalY, "SF", 0, AllSDGList);
                if (WSF.getSingleInteractionNumber() > 0) {
                    WSFNet[i-1][j-1]=WSF.getAveStrength();
                }else{
                    WSFNet[i-1][j-1]=0;
                }
                LinkForPercentGoals WSI = new LinkForPercentGoals(GoalX, GoalY, "SI", 0, AllSDGList);
                if (WSI.getSingleInteractionNumber() > 0) {
                    WSINet[i-1][j-1]=WSI.getAveStrength();
                }else{
                    WSINet[i-1][j-1]=0;
                }
                LinkForPercentGoals WFI = new LinkForPercentGoals(GoalX, GoalY, "FI", 0, AllSDGList);
                if (WFI.getSingleInteractionNumber() > 0) {
                    WFINet[i-1][j-1]=WFI.getAveStrength();
                }else {
                    WFINet[i-1][j-1]=0;
                }
                LinkForPercentGoals WIF = new LinkForPercentGoals(GoalX, GoalY, "IF", 0, AllSDGList);
                if (WIF.getSingleInteractionNumber() > 0) {
                    WIFNet[i-1][j-1]=WIF.getAveStrength();
                }else{
                    WIFNet[i-1][j-1]=0;
                }

                LinkForPercentGoals RMF = new LinkForPercentGoals(GoalX, GoalY, "MF", 2, AllSDGList);
                if (RMF.getSingleInteractionNumber() > 0) {
                    RMFNet[i-1][j-1]=RMF.getAveStrength();
                }else{
                    RMFNet[i-1][j-1]=0;
                }
                LinkForPercentGoals RMI = new LinkForPercentGoals(GoalX, GoalY, "MI", 2, AllSDGList);
                if (RMI.getSingleInteractionNumber() > 0) {
                    RMINet[i-1][j-1]=RMI.getAveStrength();
                }else{
                    RMINet[i-1][j-1]=0;
                }
                LinkForPercentGoals RSF = new LinkForPercentGoals(GoalX, GoalY, "SF", 2, AllSDGList);
                if (RSF.getSingleInteractionNumber() > 0) {
                    RSFNet[i-1][j-1]=RSF.getAveStrength();
                }else{
                    RSFNet[i-1][j-1]=0;
                }
                LinkForPercentGoals RSI = new LinkForPercentGoals(GoalX, GoalY, "SI", 2, AllSDGList);
                if (RSI.getSingleInteractionNumber() > 0) {
                    RSINet[i-1][j-1]=RSI.getAveStrength();
                }else{
                    RSINet[i-1][j-1]=0;
                }
                LinkForPercentGoals RFI = new LinkForPercentGoals(GoalX, GoalY, "FI", 2, AllSDGList);
                if (RFI.getSingleInteractionNumber() > 0) {
                    RFINet[i-1][j-1]=RFI.getAveStrength();
                }else{
                    RFINet[i-1][j-1]=0;
                }
                LinkForPercentGoals RIF = new LinkForPercentGoals(GoalX, GoalY, "IF", 2, AllSDGList);
                if (RIF.getSingleInteractionNumber() > 0) {
                    RIFNet[i-1][j-1]=RIF.getAveStrength();
                }else{
                    RIFNet[i-1][j-1]=0;
                }


                LinkForPercentGoals CoMF = new LinkForPercentGoals(GoalX, GoalY, "MF", 3, AllSDGList);
                if (CoMF.getSingleInteractionNumber() > 0) {
                    CMFNet[i-1][j-1]=CoMF.getAveStrength();
                }else{
                    CMFNet[i-1][j-1]=0;
                }
                LinkForPercentGoals CoMI = new LinkForPercentGoals(GoalX, GoalY, "MI", 3, AllSDGList);
                if (CoMI.getSingleInteractionNumber() > 0) {
                    CMINet[i-1][j-1]=CoMI.getAveStrength();
                }else {
                    CMINet[i-1][j-1]=0;
                }
                LinkForPercentGoals CoSF = new LinkForPercentGoals(GoalX, GoalY, "SF", 3, AllSDGList);
                if (CoSF.getSingleInteractionNumber() > 0) {
                    CSFNet[i-1][j-1]=CoSF.getAveStrength();
                }else{
                    CSFNet[i-1][j-1]=0;
                }
                LinkForPercentGoals CoSI = new LinkForPercentGoals(GoalX, GoalY, "SI", 3, AllSDGList);
                if (CoSI.getSingleInteractionNumber() > 0) {
                    CSINet[i-1][j-1]=CoSI.getAveStrength();
                }else{
                    CSINet[i-1][j-1]=0;
                }
                LinkForPercentGoals CoFI = new LinkForPercentGoals(GoalX, GoalY, "FI", 3, AllSDGList);
                if (CoFI.getSingleInteractionNumber() > 0) {
                    CFINet[i-1][j-1]=CoFI.getAveStrength();
                }else{
                    CFINet[i-1][j-1]=0;
                }
                LinkForPercentGoals CoIF = new LinkForPercentGoals(GoalX, GoalY, "IF", 3, AllSDGList);
                if (CoIF.getSingleInteractionNumber() > 0) {
                    CIFNet[i-1][j-1]=CoIF.getAveStrength();
                }else{
                    CIFNet[i-1][j-1]=0;
                }

            }
        }
        //输出网络数据
        operateExcel.writeExcel(OutPath, "WorldNetMF", WMFNet);
        operateExcel.writeExcel(OutPath, "WorldNetMI", WMINet);
        operateExcel.writeExcel(OutPath, "WorldNetSF", WSFNet);
        operateExcel.writeExcel(OutPath, "WorldNetSI", WSINet);
        operateExcel.writeExcel(OutPath, "WorldNetFI", WFINet);
        operateExcel.writeExcel(OutPath, "WorldNetIF", WIFNet);

        operateExcel.writeExcel(OutPath, "RegionNetMF", RMFNet);
        operateExcel.writeExcel(OutPath, "RegionNetMI", RMINet);
        operateExcel.writeExcel(OutPath, "RegionNetSF", RSFNet);
        operateExcel.writeExcel(OutPath, "RegionNetSI", RSINet);
        operateExcel.writeExcel(OutPath, "RegionNetFI", RFINet);
        operateExcel.writeExcel(OutPath, "RegionNetIF", RIFNet);

        operateExcel.writeExcel(OutPath, "CountryNetMF", CMFNet);
        operateExcel.writeExcel(OutPath, "CountryNetMI", CMINet);
        operateExcel.writeExcel(OutPath, "CountryNetSF", CSFNet);
        operateExcel.writeExcel(OutPath, "CountryNetSI", CSINet);
        operateExcel.writeExcel(OutPath, "CountryNetFI", CFINet);
        operateExcel.writeExcel(OutPath, "CountryNetIF", CIFNet);

        double [][] WorldAll=new double[17][17];
        double [][] RegionAll=new double[17][17];
        double [][] CountryAll=new double[17][17];
        //输出一下三个尺度总网络的数据
        for(int i=0;i<17;i++){
            for(int j=0;j<17;j++){
                WorldAll[i][j]=WMFNet[i][j]+WSFNet[i][j]+WFINet[i][j]+WMINet[i][j]+WIFNet[i][j]+WSINet[i][j];
                RegionAll[i][j]=RMFNet[i][j]+RSFNet[i][j]+RFINet[i][j]+RMINet[i][j]+RIFNet[i][j]+RSINet[i][j];
                CountryAll[i][j]=CMFNet[i][j]+CSFNet[i][j]+CFINet[i][j]+CMINet[i][j]+CIFNet[i][j]+CSINet[i][j];
            }
        }
        operateExcel.writeExcelNoAbs(OutPath, "WorldNetAll", WorldAll);
        operateExcel.writeExcelNoAbs(OutPath, "RegionNetAll",RegionAll);
        operateExcel.writeExcelNoAbs(OutPath, "CountryNetAll", CountryAll);
        //补充材料的图

        double [][] MFAll=new double[17][17];
        double [][] SFAll=new double[17][17];
        double [][] FIAll=new double[17][17];
        double [][] IFAll=new double[17][17];
        double [][] SIAll=new double[17][17];
        double [][] MIAll=new double[17][17];
        for(int i=0;i<17;i++){
            for(int j=0;j<17;j++){
                if(WMFNet[i][j]>1||RMFNet[i][j]>1||CMFNet[i][j]>1){
                    MFAll[i][j]=(WMFNet[i][j]+RMFNet[i][j]+CMFNet[i][j])/3;
                }else{
                    MFAll[i][j]=0;
                }
                if(WSFNet[i][j]>1||RSFNet[i][j]>1||CSFNet[i][j]>1){
                    SFAll[i][j]=(WSFNet[i][j]+RSFNet[i][j]+CSFNet[i][j])/3;
                }else{
                    SFAll[i][j]=0;
                }
                if(WFINet[i][j]>1||RFINet[i][j]>1||CFINet[i][j]>1){
                    FIAll[i][j]=(WFINet[i][j]+RFINet[i][j]+RFINet[i][j])/3;
                }else{
                    FIAll[i][j]=0;
                }
                if(WIFNet[i][j]<-1||RIFNet[i][j]<-1||CIFNet[i][j]<-1){
                    IFAll[i][j]=(WIFNet[i][j]+RIFNet[i][j]+RIFNet[i][j])/3;
                }else{
                    IFAll[i][j]=0;
                }
                if(WSINet[i][j]<-1||RSINet[i][j]<-1||CSINet[i][j]<-1){
                    SIAll[i][j]=(WSINet[i][j]+RSINet[i][j]+RSINet[i][j])/3;
                }else{
                    SIAll[i][j]=0;
                }
                if(WMINet[i][j]<-1||RMINet[i][j]<-1||CMINet[i][j]<-1){
                    MIAll[i][j]=(WMINet[i][j]+RMINet[i][j]+RMINet[i][j])/3;
                }else{
                    MIAll[i][j]=0;
                }
            }
        }
        operateExcel.writeExcelNoAbs(OutPath, "MFAll",MFAll);
        operateExcel.writeExcelNoAbs(OutPath, "SFAll",SFAll);
        operateExcel.writeExcelNoAbs(OutPath, "FIAll",FIAll);
        operateExcel.writeExcelNoAbs(OutPath, "IFAll",IFAll);
        operateExcel.writeExcelNoAbs(OutPath, "SIAll",SIAll);
        operateExcel.writeExcelNoAbs(OutPath, "MIAll",MIAll);
        //输出每个作用类型的主要目标对

        for(int SDG=1;SDG<18;SDG++){
            List<ArcmapForSpace> singleGoal=new ArrayList<>();
            for(int i=0;i<AllSDGList.size();i++){
                if((Integer.parseInt(AllSDGList.get(i).getGoalX())==SDG)&&(AllSDGList.get(i).getScale()!=1)){
                    singleGoal.add(AllSDGList.get(i));
                }
            }
            if(singleGoal.size()>0){
                String Name="SDG"+SDG;
                operateExcel.writeExcel(OutPath4,Name,ArcmapForSpace.class, singleGoal);
            }
        }


//        List<ArcmapForSpace2> CountryChangeList=new ArrayList<>();
//        for(int i=0;i<AllSDGList.size();i++){
//            //因为是尺度由小尺度向大尺度变化，因此只分析小尺度上的
//            if(AllSDGList.get(i).getScale()==3){
//                ArcmapForSpace2 OneCountry=new ArcmapForSpace2(AllSDGList.get(i));
//                //找到分区尺度上的相同位置的
//                int FatherID=new SDGIndicatorTool().FindParentID(WorldDifferenceLevel,Integer.parseInt(OneCountry.getAreaID()));
//                for(int j=0;j<AllSDGList.size();j++){
//                    if(FatherID==Integer.parseInt(AllSDGList.get(j).getAreaID())&&OneCountry.getGoalX()==AllSDGList.get(j).getGoalX()&&OneCountry.getGoalY()==AllSDGList.get(j).getGoalY()){
//                        OneCountry.setRegionInteraction(AllSDGList.get(j).getInteraction());
//                        OneCountry.setRegionStrength(AllSDGList.get(j).getStrengthWithDirection());
//                    }
//                }
//                //找到全球尺度上的相同指标
//                for(int j=0;j<AllSDGList.size();j++){
//                    if(Integer.parseInt(AllSDGList.get(j).getAreaID())==1&&OneCountry.getGoalX()==AllSDGList.get(j).getGoalX()&&OneCountry.getGoalY()==AllSDGList.get(j).getGoalY()){
//                        OneCountry.setWorldInteraction(AllSDGList.get(j).getWorldInteraction());
//                        OneCountry.setWorldStrength(AllSDGList.get(j).getStrengthWithDirection());
//                    }
//                }
//                OneCountry.CalculateType();
//                CountryChangeList.add(OneCountry);
//            }
//        }
//        if(CountryChangeList.size()>0){
//            operateExcel.writeExcel(OutPath2,"ThreeChangeSDGList",ArcmapForSpace2.class,CountryChangeList);
//        }
        List<ArcmapForSpace3> WorldRegionCountryList=new ArrayList<>();
        for(int i=1;i<18;i++){
            for(int j=1;j<18;j++){
                ArcmapForSpace3 oneData=new ArcmapForSpace3(String.valueOf(i),String.valueOf(j),AllSDGList);
                oneData.CalculateStrengthAndType();
                if(oneData.getWorldExist()==false&&oneData.getRegionExist()==false)
                {

                }else{
                    WorldRegionCountryList.add(oneData);
                }
            }
        }
        if(WorldRegionCountryList.size()>0){
            operateExcel.writeExcel(OutPath2,"ScaleChangeSDGList",ArcmapForSpace3.class,WorldRegionCountryList);
        }

        List<ArcmapForSpace4> ArcmapCountryList=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapRegionList=new ArrayList<>();
        for(int i=0;i<WorldDifferenceLevel.size();i++){
            if(WorldDifferenceLevel.get(i).getLevel()==3){
                ArcmapForSpace4 oneCountry=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),WorldDifferenceLevel.get(i).getAggregation().getCountryName(),
                        WorldDifferenceLevel.get(i).getParentID(),3,"SimpsonChange",WorldRegionCountryList);
                ArcmapForSpace4 oneCountry1=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),WorldDifferenceLevel.get(i).getAggregation().getCountryName(),
                        WorldDifferenceLevel.get(i).getParentID(),3,"NoChange",WorldRegionCountryList);
                ArcmapForSpace4 oneCountry2=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()), WorldDifferenceLevel.get(i).getAggregation().getCountryName(),
                        WorldDifferenceLevel.get(i).getParentID(),3,"OtherChange",WorldRegionCountryList);
                ArcmapCountryList.add(oneCountry);
                ArcmapCountryList.add(oneCountry1);
                ArcmapCountryList.add(oneCountry2);
            }
            if(WorldDifferenceLevel.get(i).getLevel()==2){
                ArcmapForSpace4 oneRegion=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),WorldDifferenceLevel.get(i).getAggregation().getCountryName(),
                        WorldDifferenceLevel.get(i).getParentID(),2,"SimpsonChange",WorldRegionCountryList);
                ArcmapForSpace4 oneRegion1=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),WorldDifferenceLevel.get(i).getAggregation().getCountryName(),
                        WorldDifferenceLevel.get(i).getParentID(),2,"NoChange",WorldRegionCountryList);
                ArcmapForSpace4 oneRegion2=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),WorldDifferenceLevel.get(i).getAggregation().getCountryName(),
                        WorldDifferenceLevel.get(i).getParentID(),2,"OtherChange",WorldRegionCountryList);
                ArcmapRegionList.add(oneRegion);
                ArcmapRegionList.add(oneRegion1);
                ArcmapRegionList.add(oneRegion2);
            }
        }
        if(WorldRegionCountryList.size()>0){
            operateExcel.writeExcel(OutPath2,"ScaleChangeSDGListCountry",ArcmapForSpace4.class,ArcmapCountryList);
        }
        if(ArcmapRegionList.size()>0){
            operateExcel.writeExcel(OutPath2,"ScaleChangeSDGListRegion",ArcmapForSpace4.class,ArcmapRegionList);
        }

        //Result3
        //不同尺度上的优先度目标计算及识别
        //需要计算节点列表和关系列表，节点列表在ArcmapResult2中，关系列表在GrangerList中
        //首先得到目标对


        List<NetNode> NodeAll=new ArrayList<>();
        List<NetRelationship> RelationshipAll=new ArrayList<>();
        for(int i=0;i<WorldDifferenceLevel.size();i++){
            if(WorldDifferenceLevel.get(i).getAggregation().getCountryName().equals("Liechtenstein")!=true){
                List<List<String>> OneCountryGrangerList = operateExcel.readExcel2(new File(Path + WorldDifferenceLevel.get(i).getAggregation().getCountryName() + "_Granger.xls"));
                List<NetRelationship> relationshipList=new ArrayList<>();
                for(int Index=1;Index<OneCountryGrangerList.size();Index++){
                    if(new SDGIndicatorTool().JudgeIfSelectedGoalPair(OneCountryGrangerList.get(Index).get(0),OneCountryGrangerList.get(Index).get(6),arcmapForResultList)){
                        NetRelationship netRelationship=new NetRelationship();
                        netRelationship.setGoalX(OneCountryGrangerList.get(Index).get(0));
                        netRelationship.setGoalY(OneCountryGrangerList.get(Index).get(6));
                        netRelationship.setStrength(Double.parseDouble(OneCountryGrangerList.get(Index).get(14)));
                        netRelationship.setType(OneCountryGrangerList.get(Index).get(17));
                        netRelationship.setAreaName(WorldDifferenceLevel.get(i).getAggregation().getCountryName());
                        netRelationship.setScale(WorldDifferenceLevel.get(i).getLevel());
                        relationshipList.add(netRelationship);

                    }
                }
                relationshipList=new SDGIndicatorTool().NoRepeatToSum(relationshipList);
                if(relationshipList.size()>0){
                    for(int NRi=0;NRi<relationshipList.size();NRi++){
                        RelationshipAll.add(relationshipList.get(NRi));
                    }
                }

                List<NetNode> nodeList=new ArrayList<>();
                for(int SDGI=1;SDGI<18;SDGI++){
                    NetNode oneNode=new NetNode();
                    oneNode.RelationshipConvertNode(String.valueOf(SDGI),relationshipList);
                    if(oneNode.getGoal()!=null){
                        oneNode.setGeoId(WorldDifferenceLevel.get(i).getID());
                        nodeList.add(oneNode);
                        NodeAll.add(oneNode);
                    }
                }
                if(nodeList.size()>0){
                    operateExcel.writeExcel(OutPath3,WorldDifferenceLevel.get(i).getAggregation().getCountryName()+"Relationship",NetRelationship.class, relationshipList);
                    //判断Node列表的最大值和最小值
                    if(nodeList.size()>=6){
                        nodeList.get(new SDGIndicatorTool().findPositionOfThirdMaxWeightOutDegree(nodeList,1)).setMaxOrMin("Max1");
                        nodeList.get(new SDGIndicatorTool().findPositionOfThirdMaxWeightOutDegree(nodeList,2)).setMaxOrMin("Max2");
                        nodeList.get(new SDGIndicatorTool().findPositionOfThirdMaxWeightOutDegree(nodeList,3)).setMaxOrMin("Max3");
                        nodeList.get(new SDGIndicatorTool().findPositionOfThirdMaxWeightOutDegree(nodeList,nodeList.size()-2)).setMaxOrMin("Min3");
                        nodeList.get(new SDGIndicatorTool().findPositionOfThirdMaxWeightOutDegree(nodeList,nodeList.size()-1)).setMaxOrMin("Min2");
                        nodeList.get(new SDGIndicatorTool().findPositionOfThirdMaxWeightOutDegree(nodeList,nodeList.size())).setMaxOrMin("Min1");
                    }
                    operateExcel.writeExcel(OutPath3,WorldDifferenceLevel.get(i).getAggregation().getCountryName()+"Node",NetNode.class, nodeList);
                }
            }
        }

        operateExcel.writeExcel(OutPath3,"RelationshipAll",NetRelationship.class, RelationshipAll);
        operateExcel.writeExcel(OutPath3,"NodeAll",NetNode.class, NodeAll);



        //补充材料
        List<ArcmapForSpace4> ArcmapCountryList2=new ArrayList<>();List<ArcmapForSpace4> ArcmapRegionList6=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList3=new ArrayList<>();List<ArcmapForSpace4> ArcmapRegionList7=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList6=new ArrayList<>();List<ArcmapForSpace4> ArcmapRegionList8=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList7=new ArrayList<>();List<ArcmapForSpace4> ArcmapRegionList9=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList8=new ArrayList<>();List<ArcmapForSpace4> ArcmapRegionList10=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList9=new ArrayList<>();List<ArcmapForSpace4> ArcmapRegionList17=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList10=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList11=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList12=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList14=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList15=new ArrayList<>();
        List<ArcmapForSpace4> ArcmapCountryList17=new ArrayList<>();
        for(int i=0;i<WorldDifferenceLevel.size();i++){
            if(WorldDifferenceLevel.get(i).getLevel()==3)
            {
                ArcmapForSpace4 oneCountry2=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"2");
                ArcmapForSpace4 oneCountry3=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"3");
                ArcmapForSpace4 oneCountry6=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"6");
                ArcmapForSpace4 oneCountry7=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"7");
                ArcmapForSpace4 oneCountry8=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"8");
                ArcmapForSpace4 oneCountry9=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"9");
                ArcmapForSpace4 oneCountry10=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"10");
                ArcmapForSpace4 oneCountry11=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"11");
                ArcmapForSpace4 oneCountry12=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"12");
                ArcmapForSpace4 oneCountry14=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"14");
                ArcmapForSpace4 oneCountry15=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"15");
                ArcmapForSpace4 oneCountry17=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),3,"SimpsonChange",WorldRegionCountryList,"17");
                ArcmapCountryList2.add(oneCountry2);
                ArcmapCountryList3.add(oneCountry3);
                ArcmapCountryList6.add(oneCountry6);
                ArcmapCountryList7.add(oneCountry7);
                ArcmapCountryList8.add(oneCountry8);
                ArcmapCountryList9.add(oneCountry9);
                ArcmapCountryList10.add(oneCountry10);
                ArcmapCountryList11.add(oneCountry11);
                ArcmapCountryList12.add(oneCountry12);
                ArcmapCountryList14.add(oneCountry14);
                ArcmapCountryList15.add(oneCountry15);
                ArcmapCountryList17.add(oneCountry17);
            }
            if(WorldDifferenceLevel.get(i).getLevel()==2){
                ArcmapForSpace4 oneRegion6=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),2,"SimpsonChange",WorldRegionCountryList,"6");
                ArcmapForSpace4 oneRegion7=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),2,"SimpsonChange",WorldRegionCountryList,"7");
                ArcmapForSpace4 oneRegion8=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),2,"SimpsonChange",WorldRegionCountryList,"8");
                ArcmapForSpace4 oneRegion9=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),2,"SimpsonChange",WorldRegionCountryList,"9");
                ArcmapForSpace4 oneRegion10=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),2,"SimpsonChange",WorldRegionCountryList,"10");
                ArcmapForSpace4 oneRegion17=new ArcmapForSpace4(String.valueOf(WorldDifferenceLevel.get(i).getID()),
                        WorldDifferenceLevel.get(i).getAggregation().getCountryName(),2,"SimpsonChange",WorldRegionCountryList,"17");
                ArcmapRegionList6.add(oneRegion6);
                ArcmapRegionList7.add(oneRegion7);
                ArcmapRegionList8.add(oneRegion8);
                ArcmapRegionList9.add(oneRegion9);
                ArcmapRegionList10.add(oneRegion10);
                ArcmapRegionList17.add(oneRegion17);
            }
        }
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_2",ArcmapForSpace4.class,ArcmapCountryList2);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_3",ArcmapForSpace4.class,ArcmapCountryList3);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_6",ArcmapForSpace4.class,ArcmapCountryList6);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_7",ArcmapForSpace4.class,ArcmapCountryList7);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_8",ArcmapForSpace4.class,ArcmapCountryList8);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_9",ArcmapForSpace4.class,ArcmapCountryList9);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_10",ArcmapForSpace4.class,ArcmapCountryList10);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_11",ArcmapForSpace4.class,ArcmapCountryList11);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_12",ArcmapForSpace4.class,ArcmapCountryList12);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_14",ArcmapForSpace4.class,ArcmapCountryList14);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_15",ArcmapForSpace4.class,ArcmapCountryList15);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListCountry_Simpson_17",ArcmapForSpace4.class,ArcmapCountryList17);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListRegion_Simpson_6",ArcmapForSpace4.class,ArcmapRegionList6);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListRegion_Simpson_7",ArcmapForSpace4.class,ArcmapRegionList7);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListRegion_Simpson_8",ArcmapForSpace4.class,ArcmapRegionList8);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListRegion_Simpson_9",ArcmapForSpace4.class,ArcmapRegionList9);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListRegion_Simpson_10",ArcmapForSpace4.class,ArcmapRegionList10);
        operateExcel.writeExcel(OutPath4,"ScaleChangeSDGListRegion_Simpson_17",ArcmapForSpace4.class,ArcmapRegionList17);


        //Data S1
        //所有的原数数据存在WorldDifferenceLevel变量中
        HSSFWorkbook DataS1 = new HSSFWorkbook();
        for(int Ai=0;Ai<WorldDifferenceLevel.size();Ai++){
            //记录sheet为国家名
            Sheet sheet=DataS1.createSheet(WorldDifferenceLevel.get(Ai).getAggregation().getCountryName());
            //记录表头
            HSSFRow row0 = (HSSFRow) sheet.createRow(0);
            HSSFCell cell = null;
            int Length=0;
            for(int i=0;i<WorldDifferenceLevel.get(0).getAggregation().getIndivisible().get(0).size();i++){
                row0.createCell(i).setCellValue(WorldDifferenceLevel.get(0).getAggregation().getIndivisible().get(0).get(i));
            }
            Length=1;
            //双向促进
            if(WorldDifferenceLevel.get(Ai).getAggregation().getIndivisible().size()>0){
                for(int i=0;i<WorldDifferenceLevel.get(Ai).getAggregation().getIndivisible().size();i++){
                    HSSFRow row =(HSSFRow)sheet.createRow(Length);
                    if(new SDGIndicatorTool().JudgeRawData(WorldDifferenceLevel.get(Ai).getAggregation().getCountryAb(),
                            "MF",WorldDifferenceLevel.get(Ai).getAggregation().getIndivisible().get(i),AllSDGList)){
                        for(int j=0;j<WorldDifferenceLevel.get(Ai).getAggregation().getIndivisible().get(0).size();j++){
                            row.createCell(j).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getIndivisible().get(i).get(j));
                        }
                        Length++;
                    }
                }

            }
            //单向促进
            if(WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingOnly().size()>0){
                for(int i=1;i<WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingOnly().size();i++){
                    HSSFRow row =(HSSFRow)sheet.createRow(Length);
                    if(new SDGIndicatorTool().JudgeRawData(WorldDifferenceLevel.get(Ai).getAggregation().getCountryAb(),
                            "SF",WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingOnly().get(i),AllSDGList)){
                        for(int j=0;j<WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingOnly().get(0).size();j++){
                            row.createCell(j).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingOnly().get(i).get(j));
                        }
                        Length++;
                    }

                }
            }
            //先促进后抑制
            if(WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingThenCounteracting().size()>0){
                for(int i=1;i<WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingThenCounteracting().size();i++){
                    HSSFRow row =(HSSFRow)sheet.createRow(Length);
                    if(new SDGIndicatorTool().JudgeRawData(WorldDifferenceLevel.get(Ai).getAggregation().getCountryAb(),
                            "FI",WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingThenCounteracting().get(i),AllSDGList)){
                        for(int j=0;j<WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingThenCounteracting().get(0).size();j++){
                            row.createCell(j).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getReinforcingThenCounteracting().get(i).get(j));
                        }
                        Length++;
                    }

                }
            }
            //先抑制后促进
            if(WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingThenReinforcing().size()>0){
                for(int i=1;i<WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingThenReinforcing().size();i++){
                    HSSFRow row =(HSSFRow)sheet.createRow(Length);
                    if(new SDGIndicatorTool().JudgeRawData(WorldDifferenceLevel.get(Ai).getAggregation().getCountryAb(),
                            "IF",WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingThenReinforcing().get(i),AllSDGList)){
                        for(int j=0;j<WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingThenReinforcing().get(0).size();j++){
                            row.createCell(j).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingThenReinforcing().get(i).get(j));
                        }
                        Length++;
                    }

                }
            }
            //单向抑制
            if(WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingOnly().size()>0){
                for(int i=0;i<WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingOnly().size();i++){
                    HSSFRow row =(HSSFRow)sheet.createRow(Length);
                    if(new SDGIndicatorTool().JudgeRawData(WorldDifferenceLevel.get(Ai).getAggregation().getCountryAb(),
                            "SI",WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingOnly().get(i),AllSDGList)){
                        for(int j=0;j<WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingOnly().get(0).size();j++){
                            row.createCell(j).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getCounteractingOnly().get(i).get(j));
                        }
                        Length++;
                    }

                }

            }
            //双向抑制
            if(WorldDifferenceLevel.get(Ai).getAggregation().getCancelling().size()>0){
                for(int i=1;i<WorldDifferenceLevel.get(Ai).getAggregation().getCancelling().size();i++){
                    HSSFRow row =(HSSFRow)sheet.createRow(Length);
                    if(new SDGIndicatorTool().JudgeRawData(WorldDifferenceLevel.get(Ai).getAggregation().getCountryAb(),
                            "MI",WorldDifferenceLevel.get(Ai).getAggregation().getCancelling().get(i),AllSDGList)){
                        for(int j=0;j<WorldDifferenceLevel.get(Ai).getAggregation().getCancelling().get(0).size();j++){
                            row.createCell(j).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getCancelling().get(i).get(j));
                        }
                        Length++;
                    }

                }
            }
            //中性作用
            if(WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().size()>0){
                for(int i=1;i<WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().size();i++){
                    HSSFRow row =(HSSFRow)sheet.createRow(Length);
                    if(new SDGIndicatorTool().JudgeRawData(WorldDifferenceLevel.get(Ai).getAggregation().getCountryAb(),
                            "NE",WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().get(i),AllSDGList)){
                        for(int j=0;j<13;j++){
                            row.createCell(j).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().get(i).get(j));
                        }
                        row.createCell(13).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().get(i).get(15));
                        row.createCell(14).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().get(i).get(16));
                        row.createCell(15).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().get(i).get(17));
                        row.createCell(16).setCellValue(WorldDifferenceLevel.get(Ai).getAggregation().getNeuralInteraction().get(i).get(18));
                        row.createCell(17).setCellValue("neutralization");
                        row.createCell(18).setCellValue("1.0");
                        row.createCell(19).setCellValue("no-directional neutralization");
                        row.createCell(20).setCellValue("1.0");
                        Length++;
                    }

                }
            }
        }
        File excelFile = new File(OutPath4+"DataS1.xls");
        FileOutputStream out=new FileOutputStream(excelFile);
        // 清理
        out.flush();
        // 将 Workbook 中的数据通过流写入
        DataS1.write(out);
        // 关闭流
        out.close();



     }

}
