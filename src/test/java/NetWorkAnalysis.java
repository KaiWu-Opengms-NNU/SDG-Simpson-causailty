import com.opengms.wukai.pojo.Result.Prioritize.NetCompareResult;
import com.opengms.wukai.tool.OperateExcel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Comparable;

public class NetWorkAnalysis {
    public static String NetworkBasePath="G:/WukaiBag/Year2023/sustainableofpaper2023/result5/OutFile/xlsx";
    public static String NetworkOutPath="G:/WukaiBag/Year2023/sustainableofpaper2023/result5/NetWorkAnalysis/";
    public static void main(String[] args) throws IOException {
        OperateExcel operateExcel = new OperateExcel();
        File dir = new File(NetworkBasePath);
        List<File> AllFileList=operateExcel.getAllFile(dir);
        List<List<NetCompareResult>> DifferentScaleList=new ArrayList<>();
        List<String> FileNameList=new ArrayList<>();
        for(int i=0;i<AllFileList.size();i++){
            FileNameList.add(AllFileList.get(i).getName());
            int Scale=0;
            if(AllFileList.get(i).getName().contains("Country")){
                Scale=3;
            }
            if(AllFileList.get(i).getName().contains("Region")){
                Scale=2;
            }
            if(AllFileList.get(i).getName().contains("World")){
                Scale=0;
            }
            int Interaction=0;
            if(AllFileList.get(i).getName().contains("MF")){
                Interaction=1;
            }
            if(AllFileList.get(i).getName().contains("SF")){
                Interaction=2;
            }
            if(AllFileList.get(i).getName().contains("FI")){
                Interaction=3;
            }
            if(AllFileList.get(i).getName().contains("IF")){
                Interaction=4;
            }
            if(AllFileList.get(i).getName().contains("SI")){
                Interaction=5;
            }
            if(AllFileList.get(i).getName().contains("MI")){
                Interaction=6;
            }
            List<List<String>> TempData=operateExcel.readExcel(AllFileList.get(i));
            List<NetCompareResult> TempNetCompareList=new ArrayList<>();
            for(int Ti=1;Ti<TempData.size();Ti++){
                NetCompareResult OneData=new NetCompareResult();
                OneData.setGoal(TempData.get(Ti).get(0));
                OneData.setScale(Scale);
                OneData.setInteraction(Interaction);
                OneData.setWeightInDegree(Double.parseDouble(TempData.get(Ti).get(2)));
                OneData.setWeightOutDegree(Double.parseDouble(TempData.get(Ti).get(3)));
                OneData.setBetweenessCentrality(Double.parseDouble(TempData.get(Ti).get(8)));
                OneData.setEigenCentrality(Double.parseDouble(TempData.get(Ti).get(9)));
                TempNetCompareList.add(OneData);
            }


            DifferentScaleList.add(TempNetCompareList);
        }

        List<String[]> IndegreeResultList=new ArrayList<>();
        //得到数据，开始分析，首先分析每个尺度上各个尺度最大的前三个目标,首先进行排序
        for(int i=0;i<DifferentScaleList.size();i++){
            List<NetCompareResult> OneDataList=DifferentScaleList.get(i);
            //按照入度排序
            Collections.sort(OneDataList, new Comparator<NetCompareResult>() {
                @Override
                public int compare(NetCompareResult o1, NetCompareResult o2) {
                    return new Double(o2.getWeightInDegree()).compareTo(new Double(o1.getWeightInDegree()));
                }
            });
            if(OneDataList.size()>=3){
                String[] AnalysisResult=new String[6];
                AnalysisResult[0]=String.valueOf(OneDataList.get(0).getScale());
                AnalysisResult[1]=String.valueOf(OneDataList.get(0).getInteraction());
                AnalysisResult[2]="WeightInDegree";
                AnalysisResult[3]=String.valueOf(OneDataList.get(0).getGoal());
                AnalysisResult[4]=String.valueOf(OneDataList.get(1).getGoal());
                AnalysisResult[5]=String.valueOf(OneDataList.get(2).getGoal());
                System.out.println("success");
                IndegreeResultList.add(AnalysisResult);
            }else{
                String[] AnalysisResult=new String[3+OneDataList.size()];
                AnalysisResult[0]=String.valueOf(OneDataList.get(0).getScale());
                AnalysisResult[1]=String.valueOf(OneDataList.get(0).getInteraction());
                AnalysisResult[2]="WeightInDegree";
                for(int OneDataI=0;OneDataI<OneDataList.size();OneDataI++){
                    AnalysisResult[3+OneDataI]=String.valueOf(OneDataList.get(OneDataI).getGoal());
                }
                IndegreeResultList.add(AnalysisResult);
            }

        }

        List<String[]> OutdegreeResultList=new ArrayList<>();
        //得到数据，开始分析，首先分析每个尺度上各个尺度最大的前三个目标,首先进行排序
        for(int i=0;i<DifferentScaleList.size();i++){
            List<NetCompareResult> OneDataList=DifferentScaleList.get(i);
            //按照入度排序
            Collections.sort(OneDataList, new Comparator<NetCompareResult>() {
                @Override
                public int compare(NetCompareResult o1, NetCompareResult o2) {
                    return new Double(o2.getWeightOutDegree()).compareTo(new Double(o1.getWeightOutDegree()));
                }
            });
            if(OneDataList.size()>=3){
                String[] AnalysisResult=new String[3+2*OneDataList.size()];
                AnalysisResult[0]=String.valueOf(OneDataList.get(0).getScale());
                AnalysisResult[1]=String.valueOf(OneDataList.get(0).getInteraction());
                AnalysisResult[2]="WeightOutDegree";
                for(int Gi=0;Gi<OneDataList.size();Gi++){
                    AnalysisResult[3+2*Gi]=String.valueOf(OneDataList.get(Gi).getGoal());
                    AnalysisResult[4+2*Gi]=String.valueOf(OneDataList.get(Gi).getWeightOutDegree());
                }
                System.out.println("success");
                OutdegreeResultList.add(AnalysisResult);
            }else{
                String[] AnalysisResult=new String[3+2*OneDataList.size()];
                AnalysisResult[0]=String.valueOf(OneDataList.get(0).getScale());
                AnalysisResult[1]=String.valueOf(OneDataList.get(0).getInteraction());
                AnalysisResult[2]="WeightOutDegree";
                for(int Gi=0;Gi<OneDataList.size();Gi++){
                    AnalysisResult[3+2*Gi]=String.valueOf(OneDataList.get(Gi).getGoal());
                    AnalysisResult[4+2*Gi]=String.valueOf(OneDataList.get(Gi).getWeightOutDegree());
                }
                OutdegreeResultList.add(AnalysisResult);
            }

        }

        operateExcel.writeExcelString(NetworkOutPath,"Outdegree",OutdegreeResultList);
        System.out.println("success");
    }

}
