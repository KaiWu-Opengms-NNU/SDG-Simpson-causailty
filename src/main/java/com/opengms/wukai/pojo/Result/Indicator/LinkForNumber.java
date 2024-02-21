package com.opengms.wukai.pojo.Result.Indicator;

import lombok.Data;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.python.antlr.ast.Str;

/**汇总列表
 * @Description Analysis the  difference between the scale of world
 * @Author  Kai
 * @Date 2023/3/6
 */

@Data
public class LinkForNumber {
    private int GoalX;
    private int GoalY;
    private double IndivisibleN=0;
    private double CancellingN=0;
    private double CounteractingN=0;
    private double CounteractingThenReinforcingN=0;
    private double ReinforcingN=0;
    private double ReinforcingThenCounteractingN=0;
    private double IndivisibleP;
    private double  CancellingP;
    private double  CounteractingP;
    private double  CounteractingThenReinforcingP;
    private double  ReinforcingP;
    private double  ReinforcingThenCounteractingP;


    public void SetNumber(String interaction,double Strength){
        if(interaction!="NE"){
            switch (interaction){
                case "MF":
                    this.IndivisibleN=this.IndivisibleN+Strength;
                    break;
                case "SF":
                    this.ReinforcingN=this.ReinforcingN+Strength;
                    break;
                case "FI":
                    this.ReinforcingThenCounteractingN=this.ReinforcingThenCounteractingN+ Strength;
                    break;
                case "IF":
                    this.CounteractingThenReinforcingN=this.CounteractingThenReinforcingP+ Strength;
                    break;
                case "SI":
                    this.CounteractingN=this.CounteractingN+ Strength;
                    break;
                case "MI":
                    this.CancellingN=this.CancellingN+Strength;
                    break;
                default:
            }
        }
    }


    public void Calculate(){
        double Sum=IndivisibleN+CancellingN+CounteractingThenReinforcingN+CounteractingN+ReinforcingN+ReinforcingThenCounteractingN;
        if (Sum==0){
            this.IndivisibleP=0;
            this.CancellingP=0;
            this.CounteractingP=0;
            this.CounteractingThenReinforcingP=0;
            this.ReinforcingP=0;
            this.ReinforcingThenCounteractingP=0;
        }else{
            this.IndivisibleP=this.IndivisibleN/Sum;
            this.CancellingP=this.CancellingN/Sum;
            this.CounteractingP=this.CounteractingN/Sum;
            this.CounteractingThenReinforcingP=this.CounteractingThenReinforcingN/Sum;
            this.ReinforcingP=this.ReinforcingN/Sum;
            this.ReinforcingThenCounteractingP=this.ReinforcingThenCounteractingN/Sum;
        }
    }




}
