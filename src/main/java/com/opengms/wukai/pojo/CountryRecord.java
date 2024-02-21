package com.opengms.wukai.pojo;


import com.opengms.wukai.pojo.SingleSeries.YearRecord;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CountryRecord {
    private String GeaArea;
    private String GeoAreaName;
    private List<String> SeriesCode=new ArrayList<>();
    private List<String> Indicator=new ArrayList<>();
    private List<String> Dimensions=new ArrayList<>();
    private List<String> Goals=new ArrayList<>();
    private List<List<YearRecord>> data=new ArrayList<>();
    private String Units;

    public void addSeriesCode(String SeriesCode){
        this.SeriesCode.add(SeriesCode);
    }

    public void addIndicator(String Indicator){
        this.Indicator.add(Indicator);
    }

    public void addDimensions(String Dimensions){
        this.Dimensions.add(Dimensions);
    }

    public void addGoals(String Goal){this.Goals.add(Goal);}

    public void addDataList(List<YearRecord> data){
        this.data.add(data);
    }

    public void removeData(int i){
        if(i>=0&&i<Goals.size()){
            this.SeriesCode.remove(i);
            this.Indicator.remove(i);
            this.Dimensions.remove(i);
            this.Goals.remove(i);
            this.data.remove(i);
        }else {
            System.out.println("数据删除失败");
        }
    }

    public boolean JudgeData(List<YearRecord> data,int Duration){
        List<YearRecord> TempData=new ArrayList<>();
        for (int i=data.size()-1;i>=0;i--){
            try{
                Double.parseDouble(data.get(i).getValue());
                TempData.add(data.get(i));
            }catch (Exception e){

            }
        }
        if(TempData.size()>=Duration){
            return true;
        }else{
            return false;
        }
    }

    public void Clear(){
        this.SeriesCode.clear();
        this.Indicator.clear();
        this.Dimensions.clear();
        this.Goals.clear();
        this.data.clear();
    }



}
