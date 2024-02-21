package com.opengms.wukai.pojo;

import com.opengms.wukai.pojo.SingleSeries.YearRecord;
import lombok.Data;

import java.util.List;

@Data
public class CountryRecordResult {
    private String Goals;
    private String Indicator;
    private String SeriesCode;
    private String Data;
    private String Direction;
    private String Units;

    public CountryRecordResult(String goals, String indicator, String seriesCode, List<YearRecord> YearData,String direction, String units){
        this.Goals=goals;
        this.Indicator=indicator;
        this.SeriesCode=seriesCode;
        this.Data="";
        for(int i=0;i<YearData.size();i++){
            Data=Data+"["+YearData.get(i).getYear()+":"+YearData.get(i).getValue()+"],";
        }
        this.Direction=direction;
        this.Units=units;
    }
}
