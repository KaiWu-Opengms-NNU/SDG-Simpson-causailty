package com.opengms.wukai.pojo.Result.Indicator;


import lombok.Data;

import java.util.List;

@Data
public class GoalListAnalyse {
    private String GoalX;
    private String GoalY;
    private String Interaction;
    private double WorldValue=0;
    private double RegionValue=0;
    private double CountryValue=0;
    private double SumValue;
    private double VarValue;

    public void GetScaleValue(String interaction, List<LinkForPercentGoals> WorldList, List<LinkForPercentGoals> RegionList, List<LinkForPercentGoals> CountryList,double []SDGNumber)
    {
        this.Interaction=interaction;
        for(int i=0;i<WorldList.size();i++){
            switch (interaction){
                case "SF":
                    WorldValue=JudgeSameInteraction(this.GoalX,this.GoalY,"SF",WorldList.get(i));
                    break;
                case "SI":
                    WorldValue=JudgeSameInteraction(this.GoalX,this.GoalY,"SI",WorldList.get(i));
                    break;
                case "MF":
                    WorldValue=JudgeSameInteraction(this.GoalX,this.GoalY,"MF",WorldList.get(i));
                    break;
                case "MI":
                    WorldValue=JudgeSameInteraction(this.GoalX,this.GoalY,"MI",WorldList.get(i));
                    break;
                case "FI":
                    WorldValue=JudgeSameInteraction(this.GoalX,this.GoalY,"FI",WorldList.get(i));
                    break;
                case "IF":
                    WorldValue=JudgeSameInteraction(this.GoalX,this.GoalY,"IF",WorldList.get(i));
                    break;
                case "NO":
                    WorldValue=JudgeSameInteraction(this.GoalX,this.GoalY,"NO",WorldList.get(i));
                    break;
            }
            if(this.WorldValue!=0){
                break;
            }
        }
        for(int i=0;i<RegionList.size();i++){
            switch (interaction){
                case "SF":
                    RegionValue=JudgeSameInteraction(this.GoalX,this.GoalY,"SF",RegionList.get(i));
                    break;
                case "SI":
                    RegionValue=JudgeSameInteraction(this.GoalX,this.GoalY,"SI",RegionList.get(i));
                    break;
                case "MF":
                    RegionValue=JudgeSameInteraction(this.GoalX,this.GoalY,"MF",RegionList.get(i));
                    break;
                case "MI":
                    RegionValue=JudgeSameInteraction(this.GoalX,this.GoalY,"MI",RegionList.get(i));
                    break;
                case "FI":
                    RegionValue =JudgeSameInteraction(this.GoalX,this.GoalY,"FI",RegionList.get(i));
                    break;
                case "IF":
                    RegionValue =JudgeSameInteraction(this.GoalX,this.GoalY,"IF",RegionList.get(i));
                    break;
                case "NO":
                    RegionValue=JudgeSameInteraction(this.GoalX,this.GoalY,"NO",RegionList.get(i));
                    break;
            }
            if(RegionValue!=0){
                break;
            }

        }
        for(int i=0;i<CountryList.size();i++){
            switch (interaction){
                case "SF":
                    CountryValue=JudgeSameInteraction(this.GoalX,this.GoalY,"SF",CountryList.get(i));
                    break;
                case "SI":
                    CountryValue=JudgeSameInteraction(this.GoalX,this.GoalY,"SI",CountryList.get(i));
                    break;
                case "MF":
                    CountryValue=JudgeSameInteraction(this.GoalX,this.GoalY,"MF",CountryList.get(i));
                    break;
                case "MI":
                    CountryValue=JudgeSameInteraction(this.GoalX,this.GoalY,"MI",CountryList.get(i));
                    break;
                case "FI":
                    CountryValue=JudgeSameInteraction(this.GoalX,this.GoalY,"FI",CountryList.get(i));
                    break;
                case "IF":
                    CountryValue=JudgeSameInteraction(this.GoalX,this.GoalY,"IF",CountryList.get(i));
                    break;
                case "NO":
                    CountryValue=JudgeSameInteraction(this.GoalX,this.GoalY,"NO",CountryList.get(i));
                    break;
            }
            if(CountryValue!=0){
                break;
            }
        }
        //需要改变
        WorldValue=WorldValue/(SDGNumber[Integer.parseInt(GoalX)-1]*SDGNumber[Integer.parseInt(GoalY)-1]);
        RegionValue=RegionValue/(SDGNumber[Integer.parseInt(GoalX)-1]*SDGNumber[Integer.parseInt(GoalY)-1]);
        CountryValue=CountryValue/(SDGNumber[Integer.parseInt(GoalX)-1]*SDGNumber[Integer.parseInt(GoalY)-1]);
        this.SumValue=WorldValue+RegionValue+CountryValue;
        this.VarValue=standardDiviation(new double[]{WorldValue,RegionValue,CountryValue});


    }

    private double JudgeSameInteraction(String X,String Y,String interaction,LinkForPercentGoals Target){
        double result=0;
        if(Target.getInteraction().equals(interaction)){
            if(Target.getGoalX().equals(X)&&Target.getGoalY().equals(Y)){
                result=Target.getSingleMaxInteractionPercent();
            }
        }
        return result;
    }

    public static double average(double[] x) {
        int n = x.length;            //数列元素个数
        double sum = 0;
        for (double i : x) {        //求和
            sum+=i;
        }
        return sum/n;
    }

    /**
     * 传入一个数列x计算方差
     * 方差s^2=[（x1-x）^2+（x2-x）^2+......（xn-x）^2]/（n）（x为平均数）
     * @param x 要计算的数列
     * @return 方差
     */
    public static double variance(double[] x) {
        int n = x.length;            //数列元素个数
        double avg = average(x);    //求平均值
        double var = 0;
        for (double i : x) {
            var += (i-avg)*(i-avg);    //（x1-x）^2+（x2-x）^2+......（xn-x）^2
        }
        return var/n;
    }

    /**
     * 传入一个数列x计算标准差
     * 标准差σ=sqrt(s^2)，即标准差=方差的平方根
     * @param x 要计算的数列
     * @return 标准差
     */
    public static double standardDiviation(double[] x) {
        return  Math.sqrt(variance(x));
    }
}
