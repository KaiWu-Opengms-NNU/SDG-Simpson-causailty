package com.opengms.wukai.data;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opengms.wukai.dto.SingleSeriesDto;
import com.opengms.wukai.pojo.CountryCode;
import com.opengms.wukai.pojo.Indicators.Indicator;
import com.opengms.wukai.tool.WebService;

import java.util.ArrayList;
import java.util.List;


public class DataService {

    private String UriHead="https://unstats.un.org/SDGAPI";
    private WebService webService=new WebService();
    //得到国家列表
    public List<CountryCode> getGeaArea(){
        JSONArray WorldCountryJSON=JSONArray.parseArray(webService.getMethodNoParameter(UriHead+"/v1/sdg/GeoArea/List"));
        List<CountryCode> countryList=new ArrayList<CountryCode>();
        for (int i=0;i<WorldCountryJSON.size();i++) {
            CountryCode countryCode= JSON.toJavaObject((JSONObject)WorldCountryJSON.get(i),CountryCode.class);
            countryList.add(countryCode);
        }
        return countryList;

    }

    //得到指标列表
    public List<Indicator> getIndicator(){
        JSONArray IndicatorJson=JSONArray.parseArray(webService.getMethodNoParameter(UriHead+"/v1/sdg/Indicator/List"));
        List<Indicator> indicatorsList=new ArrayList<Indicator>();
        for(int i=0;i<IndicatorJson.size();i++){
            Indicator indicator=JSON.toJavaObject((JSONObject)IndicatorJson.get(i),Indicator.class);
            indicatorsList.add(indicator);
        }
        return indicatorsList;
    }


    //得到某个国家和某个指标的时间序列数据
    public JSONObject getSingleSeries(String indicators, String seriesCode, String areaCode){
        //创建参数
        SingleSeriesDto singleSeriesDto=new SingleSeriesDto();
        singleSeriesDto.setIndicators(indicators);
        singleSeriesDto.setSeriesCode(seriesCode);
        singleSeriesDto.setAreaCode(areaCode);
        singleSeriesDto.setYears("2020");
        return JSONObject.parseObject(webService.postHaveParameter(UriHead+"/v1/sdg/GlobalAndRegional/GetSingleSeries",singleSeriesDto,"com.opengms.wukai.dto.SingleSeriesDto"));

    }

}
