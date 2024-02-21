package com.opengms.wukai.pojo.SystemDynamics;

import lombok.Data;

import java.util.List;

/**
 * @Description 这个类主要系统动力学的读取和验证
 * @Author  Kai
 * @Date 2023/2/15
 */
@Data
public class SDResultPojo {
    private String code;
    private double[] data=new double[16];
    private int i=0;

    //SD模型的历史检验
    private String Goal;
    private double[] error=new double[16];
    private double AverageError;


    public void setData(double data){
        this.data[this.i]=data;
        this.i++;
    }

    public void setZero(){
        this.i=0;
    }

    //计算SD模型的所有误差
    public void getErrorOfSDModel(double[] actualData)
    {
        double sum=0;
        double average=0;
        for (int Di=0;Di<data.length;Di++){
            sum=sum+Math.abs(actualData[Di]);
        }
        for (int Di=0;Di<data.length;Di++){
            this.error[Di]=Math.abs((actualData[Di]-data[Di])*actualData[Di]);
            average=average+this.error[Di];
        }
        this.AverageError=average/sum;
    }


}
