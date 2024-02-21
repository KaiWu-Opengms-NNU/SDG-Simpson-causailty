package com.opengms.wukai.dto;

import cointegration.MultipleLinearRegression;
import cointegration.StatisticsUtil;
import lombok.Data;

import javax.naming.ldap.PagedResultsControl;
import java.util.List;

/**
 * @Description 这个类主要用于多元线性回归的展示
 * @Author  Kai
 * @Date 2023/2/4
 */

@Data
public class MulLinearDto {
    private String GoalY;
    private List<String> GoalX;
    private String IndicatorY;
    private List<String> IndicatorX;
    private String StringCodeY;
    private List<String> StringCodeX;
    private String Function;
    private double AdjustRSquared;
    private double PValue;
//    private Boolean ADFJudge;
//    private double ADFPValue;

    public MulLinearDto(String goalY, List<String> goalX,String indicatorY,List<String> indicatorX, String stringCodeY, List<String> stringCodeX, MultipleLinearRegression mul){
        this.GoalY=goalY;
        this.GoalX=goalX;
        this.IndicatorY=indicatorY;
        this.IndicatorX=indicatorX;
        this.StringCodeY=stringCodeY;
        this.StringCodeX=stringCodeX;
        this.Function=mul.getFunction();
        this.AdjustRSquared=mul.getAdjRSquared();
        this.PValue= StatisticsUtil.getPValue(mul.getFValue(), mul.getDfIndependent(), mul.getDfDependent());
//        this.ADFJudge=ADFJudge;
//        this.ADFPValue=ADFPValue;
    }

}
