package com.opengms.wukai.pojo.Result.Indicator;

import com.opengms.wukai.pojo.Result.ScaleTree;
import com.opengms.wukai.tool.SDGIndicatorTool;
import lombok.Data;
import org.apache.commons.math3.stat.descriptive.summary.Sum;

import java.util.List;

/**汇总列表
 * @Description Analysis the  difference between the scale of world
 * @Author  Kai
 * @Date 2023/3/6
 */

@Data
public class LinkForPercentCode {
    //指标对属性
    private String GoalX;
    private String GoalY;
    private String indicatorX;
    private String indicatorY;
    private String seriesX;
    private String seriesY;
    private String descriptionX;
    private String descriptionY;
    private double strength;
    private double rsquare;
    private double pvalue;
    private double percent;

    //指标对信息
    //尺度,数量
    private int Scale;
    private double Number;
    private String Interaction;

    //无参构造
    public LinkForPercentCode(){}

    //有参构造
    public LinkForPercentCode(String goalX, String goalY,
                              String indicatorX, String indicatorY,
                              String seriesX, String seriesY,
                              String descriptionX, String descriptionY,
                              int scale, List<ScaleTree> WorldDifferenceLevel, String interaction){
        this.GoalX=goalX;
        this.GoalY=goalY;
        this.indicatorX=indicatorX;
        this.indicatorY=indicatorY;
        this.seriesX=seriesX;
        this.seriesY=seriesY;
        this.descriptionX=descriptionX;
        this.descriptionY=descriptionY;
        this.Scale=scale;
        this.Interaction=interaction;
        SDGIndicatorTool tool=new SDGIndicatorTool();
        double InteractionNumber=0;
        double StrengthValue=0;
        double RSquareValue=0;
        double Percent=0;
        double Pvalue=0;
        for(int i=0;i<WorldDifferenceLevel.size();i++){
            if(WorldDifferenceLevel.get(i).getLevel()==scale&&WorldDifferenceLevel.get(i).getAggregation()!=null){
                //还得判断不同的类型
                switch (interaction){
                    case "MF":
                        InteractionNumber=InteractionNumber+tool.GetGoalNumberInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getIndivisible());
                        StrengthValue=StrengthValue+tool.GetGoalStrengthInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getIndivisible());
                        RSquareValue=RSquareValue+tool.GetGoalRSquareInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getIndivisible());
                        Percent=Percent+tool.GetGoalPercentInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getIndivisible());
                        Pvalue=Pvalue+tool.GetGoalPvalueInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getIndivisible());
                        break;
                    case "MI":
                        InteractionNumber=InteractionNumber+tool.GetGoalNumberInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCancelling());
                        StrengthValue=StrengthValue+tool.GetGoalStrengthInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCancelling());
                        RSquareValue=RSquareValue+tool.GetGoalRSquareInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCancelling());
                        Percent=Percent+tool.GetGoalPercentInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCancelling());
                        Pvalue=Pvalue+tool.GetGoalPvalueInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCancelling());
                        break;
                    case "SF":
                        InteractionNumber=InteractionNumber+tool.GetGoalNumberInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingOnly());
                        StrengthValue=StrengthValue+tool.GetGoalStrengthInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingOnly());
                        RSquareValue=RSquareValue+tool.GetGoalRSquareInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingOnly());
                        Percent=Percent+tool.GetGoalPercentInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingOnly());
                        Pvalue=Pvalue+tool.GetGoalPvalueInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingOnly());
                        break;
                    case "SI":
                        InteractionNumber=InteractionNumber+tool.GetGoalNumberInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingOnly());
                        StrengthValue=StrengthValue+tool.GetGoalStrengthInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingOnly());
                        RSquareValue= RSquareValue+tool.GetGoalRSquareInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingOnly());
                        Percent=Percent+tool.GetGoalPercentInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingOnly());
                        Pvalue=Pvalue+tool.GetGoalPvalueInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingOnly());
                        break;
                    case "FI":
                        InteractionNumber=InteractionNumber+tool.GetGoalNumberInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingThenCounteracting());
                        StrengthValue=StrengthValue+tool.GetGoalStrengthInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingThenCounteracting());
                        RSquareValue= RSquareValue+tool.GetGoalRSquareInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingThenCounteracting());
                        Percent=Percent+tool.GetGoalPercentInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingThenCounteracting());
                        Pvalue=Pvalue+tool.GetGoalPvalueInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getReinforcingThenCounteracting());
                        break;
                    case "IF":
                        InteractionNumber=InteractionNumber+tool.GetGoalNumberInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingThenReinforcing());
                        StrengthValue=StrengthValue+tool.GetGoalStrengthInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingThenReinforcing());
                        RSquareValue= RSquareValue+tool.GetGoalRSquareInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingThenReinforcing());
                        Percent=Percent+tool.GetGoalPercentInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingThenReinforcing());
                        Pvalue=Pvalue+tool.GetGoalPvalueInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getCounteractingThenReinforcing());
                        break;
                    case "NO":
                        InteractionNumber=InteractionNumber+tool.GetGoalNumberInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getNeuralInteraction());
                        StrengthValue=StrengthValue+tool.GetGoalStrengthInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getNeuralInteraction());
                        RSquareValue= RSquareValue+tool.GetGoalRSquareInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getNeuralInteraction());
                        Percent=Percent+tool.GetGoalPercentInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getNeuralInteraction());
                        Pvalue=Pvalue+tool.GetGoalPvalueInDiffScale(indicatorX,indicatorY,WorldDifferenceLevel.get(i).getAggregation().getNeuralInteraction());
                        break;
                }

            }
        }
        this.Number=InteractionNumber;
        this.strength=StrengthValue/InteractionNumber;
        this.rsquare=RSquareValue/InteractionNumber;
        this.percent=Percent/InteractionNumber;
        this.pvalue=Pvalue/InteractionNumber;

    }


}