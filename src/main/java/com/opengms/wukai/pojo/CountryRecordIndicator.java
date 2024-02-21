package com.opengms.wukai.pojo;

import com.opengms.wukai.pojo.Result.ScaleTree;
import com.opengms.wukai.pojo.SingleSeries.YearRecord;
import javafx.scene.transform.Scale;
import lombok.Data;
import org.apache.commons.math3.stat.descriptive.summary.Sum;

import java.util.ArrayList;
import java.util.List;

/**主要程序
 * @Description Analysis data at Indicator level
 * @Author  Kai
 * @Date 2022/11/2
 */

@Data
public class CountryRecordIndicator {
    private String GeoAreaID;
    private String GeoAreaName;
    private String GeaAreaParentID;
    private String GeaAreaScale;
    private List<String> Goals=new ArrayList<>();
    private List<String> SeriesCode=new ArrayList<>();
    private List<String> Indicator=new ArrayList<>();
    private List<String> Direction=new ArrayList<>();
    private List<List<YearRecord>> data=new ArrayList<>();
    private List<String> Units=new ArrayList<>();

    //输出类
    private List<CountryRecordResult> recordResults=new ArrayList<>();


    //数据载入——国家尺度
    public void LoadData(List<List<String>> SDG1,List<List<String>> SDG2,List<List<String>> SDG3,List<List<String>> SDG4,
                         List<List<String>> SDG5,List<List<String>> SDG6,List<List<String>> SDG7,List<List<String>> SDG8,
                         List<List<String>> SDG9,List<List<String>> SDG10,List<List<String>> SDG11,List<List<String>> SDG12,
                         List<List<String>> SDG13,List<List<String>> SDG14,List<List<String>> SDG15,List<List<String>> SDG16,
                         List<List<String>> SDG17,int StartYear,int EndYear){
        for(int i=0;i<this.SeriesCode.size();i++){
            switch (this.getGoals().get(i)){
                case "1":
                    SearchData(i,SDG1,StartYear,EndYear);
                    break;
                case "2":
                    SearchData(i,SDG2,StartYear,EndYear);
                    break;
                case "3":
                    SearchData(i,SDG3,StartYear,EndYear);
                    break;
                case "4":
                    SearchData(i,SDG4,StartYear,EndYear);
                    break;
                case "5":
                    SearchData(i,SDG5,StartYear,EndYear);
                    break;
                case "6":
                    SearchData(i,SDG6,StartYear,EndYear);
                    break;
                case "7":
                    SearchData(i,SDG7,StartYear,EndYear);
                    break;
                case "8":
                    SearchData(i,SDG8,StartYear,EndYear);
                    break;
                case "9":
                    SearchData(i,SDG9,StartYear,EndYear);
                    break;
                case "10":
                    SearchData(i,SDG10,StartYear,EndYear);
                    break;
                case "11":
                    SearchData(i,SDG11,StartYear,EndYear);
                    break;
                case "12":
                    SearchData(i,SDG12,StartYear,EndYear);
                    break;
                case "13":
                    SearchData(i,SDG13,StartYear,EndYear);
                    break;
                case "14":
                    SearchData(i,SDG14,StartYear,EndYear);
                    break;
                case "15":
                    SearchData(i,SDG15,StartYear,EndYear);
                    break;
                case "16":
                    SearchData(i,SDG16,StartYear,EndYear);
                    break;
                case "17":
                    SearchData(i,SDG17,StartYear,EndYear);
                    break;
            }
        }
    }

    //数据载入——分区尺度
//    public void LoadData_Region(List<CountryRecordIndicator> countryRecordList, List<ScaleTree> scaleTreeList){
//        //找到子节点
//        int[] childrenList = new int[0];
//        for(int i=0; i<scaleTreeList.size();i++){
//            if(this.GeoAreaID.equals(scaleTreeList.get(i).getID())&&this.GeaAreaScale.equals("2")){
//                childrenList=scaleTreeList.get(i).getChildrenID();
//                break;
//            }
//        }
//        List<CountryRecordIndicator> ChildrenObjList=new ArrayList<>();
//        //找到分区下的国家数据
//        for(int i=0;i<childrenList.length;i++){
//            for(int j=0;j<countryRecordList.size();j++){
//                if(countryRecordList.get(j).GeoAreaID.equals(String.valueOf(i))){
//                    ChildrenObjList.add(countryRecordList.get(j));
//                    break;
//                }
//            }
//        }
//        for(int IndicatorI=0;IndicatorI<SeriesCode.size();IndicatorI++){
//
//            //得到了所有子节点的数据
//            for(int i=0;i<ChildrenObjList.size();i++){
//               CountryRecordIndicator  childrenCountry=ChildrenObjList.get(i);
//
//            }
//        }
//    }


    //数据载入——全球尺度
    //public void LoadData_World(){}




    private void SearchData(int k,List<List<String>> SDGX, int StartYear, int EndYear){
        List<YearRecord> oneYear=new ArrayList<>();
        String tempUnits="NULL";
        for (int i=1;i<SDGX.size();i++){
            if(this.GeoAreaID.equals(SDGX.get(i).get(5))&&this.getSeriesCode().get(k).equals(SDGX.get(i).get(3))){
                YearRecord yearRecord=new YearRecord();
                yearRecord.setYear(SDGX.get(i).get(7));
                yearRecord.setValue(SDGX.get(i).get(8));
                oneYear.add(yearRecord);
                tempUnits=SDGX.get(i).get(9);
            }
        }
        //做数据归一化以及求平均
        List<YearRecord> ProcessingData=averageData(StartYear,EndYear,oneYear);
        if(oneYear.size()>0){
            //ProcessingData=DataUniform(ProcessingData);
            ProcessingData=DataUniform2(ProcessingData);

        }
        data.add(ProcessingData);
        try{
            Units.add(tempUnits);
        }catch(Exception e){
            Units.add("NULL");
        }
    }
    //数据归一化（Z值归一化）
    private List<YearRecord> DataUniform(List<YearRecord> originalData){
        //还是得判断一下数据能否转化成double
        for (int i=originalData.size()-1;i>=0;i--){
            try{
                Double.parseDouble(originalData.get(i).getValue());
            }catch (Exception e){
                originalData.remove(i);
            }
        }
        double Sum=0;
        List<YearRecord> result=new ArrayList<>();
        //求平均数
        for(int i=0;i<originalData.size();i++){
            Sum=Sum+Double.parseDouble(originalData.get(i).getValue());
        }
        double Mean=Sum/originalData.size();
        //求标准差
        double Variance=0;
        for(int i=0;i<originalData.size();i++){
            Variance=Variance+(Double.parseDouble(originalData.get(i).getValue())-Mean)*(Double.parseDouble(originalData.get(i).getValue())-Mean);
        }
        double MeanVari=Variance/originalData.size();
        double StandardDeviation=Math.sqrt(MeanVari);
        for(int i=0;i<originalData.size();i++){
            YearRecord oneYear=new YearRecord();
            oneYear.setValue(String.valueOf((Double.parseDouble(originalData.get(i).getValue())-Mean)/StandardDeviation));
            oneYear.setYear(originalData.get(i).getYear());
            result.add(oneYear);
        }
        return result;
    }

    //数据归一化（Min-Max归一化）
    private List<YearRecord> DataUniform2(List<YearRecord> originalData){
        for (int i=originalData.size()-1;i>=0;i--){
            try{
                Double.parseDouble(originalData.get(i).getValue());
            }catch (Exception e){
                originalData.remove(i);
            }
        }
        List<YearRecord> result=new ArrayList<>();
        //求最大值和最小值
        try{
            double Max=Double.parseDouble(originalData.get(0).getValue());
            double Min=Double.parseDouble(originalData.get(0).getValue());
            for(int i=0;i<originalData.size();i++){
                if(Double.parseDouble(originalData.get(i).getValue())>Max){
                    Max=Double.parseDouble(originalData.get(i).getValue());
                }
                if(Double.parseDouble(originalData.get(i).getValue())<Min){
                    Min=Double.parseDouble(originalData.get(i).getValue());
                }
            }
            if(Max>Min){
                for(int i=0;i<originalData.size();i++){
                    YearRecord oneYear=new YearRecord();
                    oneYear.setValue(String.valueOf(100*(Double.parseDouble(originalData.get(i).getValue())-Min)/(Max-Min)));
                    oneYear.setYear(originalData.get(i).getYear());
                    result.add(oneYear);
                }
            }
        }catch (Exception e){

        }
        return result;
    }

    private List<YearRecord> averageData(int StartYear, int EndYear, List<YearRecord> Record){
        List<YearRecord> NewRecord=new ArrayList<>();
            for(int i=StartYear;i<=EndYear;i++){
                YearRecord oneData=new YearRecord();
                oneData.setYear(String.valueOf(i));
                try{
                    double TempAverage=0;
                    int Num=0;
                    for(int j=0;j<Record.size();j++){
                        if(Record.get(j).getYear().equals(String.valueOf(i))){
                            TempAverage=TempAverage+Double.parseDouble(Record.get(j).getValue());
                            Num++;
                        }
                    }
                    if(Num!=0){
                        TempAverage=TempAverage/Num;
                        oneData.setValue(String.valueOf(TempAverage));
                        NewRecord.add(oneData);
                    }
                }catch(Exception e){

                }
            }
        return NewRecord;
    }

    //输出类转化
    public void ConvertOutput(){
        for(int i=0;i<Goals.size();i++){
            CountryRecordResult countryRecordResult=new CountryRecordResult(Goals.get(i),Indicator.get(i),SeriesCode.get(i),data.get(i),Direction.get(i),Units.get(i));
            recordResults.add(countryRecordResult);
        }
    }

    public void removeData(int i){
        if(i>=0&&i<Goals.size()){
            this.SeriesCode.remove(i);
            this.Indicator.remove(i);
            this.Goals.remove(i);
            this.data.remove(i);
        }else {
            System.out.println("数据删除失败");
        }
    }

    public void Clear(){
        this.SeriesCode.clear();
        this.Indicator.clear();
        this.Goals.clear();
        this.data.clear();
    }



}
