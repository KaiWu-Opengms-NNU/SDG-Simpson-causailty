
import com.opengms.wukai.pojo.Result.AggregationForStatics;
import com.opengms.wukai.pojo.Result.Prioritize.ScaleResult;
import com.opengms.wukai.pojo.Result.ScaleTree;
import com.opengms.wukai.pojo.Result.ScaleTree2;
import com.opengms.wukai.pojo.Result.Spatial.ArcmapForResult;
import com.opengms.wukai.tool.OperateExcel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrioritizeAnalysis {
    public static String Path="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal/src/test/result_spatial/";
    public static String OutPath2="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal/src/test/result_spatial_prioritize/";

    public static void main(String[] args) throws IOException{
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
        List<ScaleTree2> WorldDifferenceLevel = new ArrayList<>();
        //载入洲尺度
        ScaleTree2 World = new ScaleTree2(WorldNo, "WOR", Continent, 0, 0, AggregationList);
        WorldDifferenceLevel.add(World);
        ScaleTree2 Africa = new ScaleTree2(2, "AFR", AfricaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Africa);
        ScaleTree2 Oceania = new ScaleTree2(9, "OCE", OceaniaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Oceania);
        ScaleTree2 Americas = new ScaleTree2(19, "AME", AmericaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Americas);
        ScaleTree2 Asia = new ScaleTree2(142, "ASI", AsiaNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Asia);
        ScaleTree2 Europe = new ScaleTree2(150, "EUR", EuropeNo, 1, 1, AggregationList);
        WorldDifferenceLevel.add(Europe);
        //载入标准分区文档
        List<List<String>> CorrespondenceRegionAndCountry = operateExcel.readExcel(new File("G:/WukaiBag/Year2023/sustainableofpaper2023/reclass/标准分区文档分析程序载入.xlsx"));
        //载入分区及国家尺度
        List<List<String>> SDGDegreeList=operateExcel.readExcel2(new File("G:/WukaiBag/Year2023/sustainableofpaper2023/Data/SDGofDegree/SDGDegreeData.xls"));
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
                            ScaleTree2 Country = new ScaleTree2(Integer.parseInt(ChildrenID), Code, new int[0], RegionID, 3, AggregationList);
                            WorldDifferenceLevel.add(Country);
                        }
                    }
                    int[] ChildrenArray = new int[ChildrenList.size()];
                    for (int Ci = 0; Ci < ChildrenList.size(); Ci++) {
                        ChildrenArray[Ci] = Integer.parseInt(ChildrenList.get(Ci));
                    }
                    ScaleTree2 ToolTree = new ScaleTree2();
                    ScaleTree2 Region = new ScaleTree2(RegionID, ToolTree.getRegionCode(RegionID), ChildrenArray, WorldDifferenceLevel.get(i).getID(), 2, AggregationList);
                    WorldDifferenceLevel.add(Region);
                }
            }
        }
        //载入可持续发展程度数据
        List<List<String>> AnalysisResult = operateExcel.readExcel2(new File(Path + "WorldRecord.xls"));
        for(int i=0;i<WorldDifferenceLevel.size();i++){
            for (int j= 1; j < AnalysisResult.size(); j++) {
                if(WorldDifferenceLevel.get(i).getID()==Integer.parseInt(AnalysisResult.get(j).get(1))){
                   WorldDifferenceLevel.get(i).setName(AnalysisResult.get(j).get(0));
                }
            }
            List<List<String>> AllInteraction = operateExcel.readExcel2(new File(Path +  WorldDifferenceLevel.get(i).getName() + "_Granger.xls"));
            WorldDifferenceLevel.get(i).setRawGranger(AllInteraction);
            //载入国家尺度的程度数据
            if(WorldDifferenceLevel.get(i).getLevel()!=2){
                WorldDifferenceLevel.get(i).setSDGDegreeList(SDGDegreeList);
            }
        }

        //获得数据，开始分析
        List<ScaleResult> scaleResultList=new ArrayList<>();
        for(int i=0;i<WorldDifferenceLevel.size();i++){
            //每个SDG指标计算
            List<List<String>> GrangerData=WorldDifferenceLevel.get(i).getRawGranger();
            for(int Goal=1;Goal<18;Goal++){
                double TempStrength=0;
                for(int Gi=0;Gi<GrangerData.size();Gi++){
                    if(GrangerData.get(Gi).get(0).equals(String.valueOf(Goal))){
                        TempStrength=TempStrength+Double.parseDouble(GrangerData.get(Gi).get(14));
                    }
                }
                //得到每个指标的具体强度
                WorldDifferenceLevel.get(i).getSDGInteractionList()[Goal-1]=TempStrength;
                //得到每个指标的优先度.计算公式=总作用强度*(1-程度）
                double TempDegree=  WorldDifferenceLevel.get(i).getSDGDegreeList()[Goal-1];
                WorldDifferenceLevel.get(i).getSDGPrioritizeList()[Goal-1]=TempStrength*(1-TempDegree);
            }
            scaleResultList.add(new ScaleResult(WorldDifferenceLevel.get(i)));
        }

        operateExcel.writeExcel(OutPath2, "prioritize", ScaleResult.class,scaleResultList);
        System.out.println("success");
    }
}
