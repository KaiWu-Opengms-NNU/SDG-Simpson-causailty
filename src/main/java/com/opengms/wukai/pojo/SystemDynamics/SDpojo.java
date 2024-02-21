package com.opengms.wukai.pojo.SystemDynamics;

import SD.hsbo.fbg.systemdynamics.exceptions.ModelException;
import SD.hsbo.fbg.systemdynamics.functions.EulerCauchyIntegration;
import SD.hsbo.fbg.systemdynamics.functions.IFunction;
import SD.hsbo.fbg.systemdynamics.model.*;
import SD.hsbo.fbg.systemdynamics.output.CSVExporter;
import SD.hsbo.fbg.systemdynamics.output.ChartViewer;
import com.opengms.wukai.pojo.CountryRecordIndicator;
import com.opengms.wukai.pojo.Granger.MultipleLinearPojo;
import com.opengms.wukai.pojo.SingleSeries.YearRecord;
import javafx.scene.chart.ValueAxis;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Data
public class SDpojo {

    private final int SDEndYear=2020;
    private final int SDStartYear=2005;

    //整个SD模型的建模数据
    List<MultipleLinearPojo> multipleLinearPojoList;
    Model model;

    //表函数列表
    List<String> SDXList;
    List<Variable> lookupVariableList;


    //回归列表
    List<String> SDYList;
    List<Variable> NormalVarableList;


    //初始化SD建模模型
    public SDpojo(List<MultipleLinearPojo> multipleLinearPojoList){
        this.multipleLinearPojoList=multipleLinearPojoList;
        //记录模拟年份
        this.model=new Model(SDStartYear,SDEndYear,1,new EulerCauchyIntegration());
    }

    //入度为零的指标，作为表函数,由于作为表函数的数据得从
    public void GetXVariableForScenario(CountryRecordIndicator countryI) throws ModelException {
        SDXList=new ArrayList<>();
        for(int i=0;i<multipleLinearPojoList.size();i++){
            List<String> codeXList=multipleLinearPojoList.get(i).getStringCodeX();
            for(int j=0;j<codeXList.size();j++){
                String codex=codeXList.get(j);
                    //判断是否与X重复
                    int JudgeRepeatX=1;
                    for(int z=0;z<SDXList.size();z++){
                        if(codex.equals(SDXList.get(z))){
                            JudgeRepeatX=0;
                            break;
                        }
                    }
                    if(JudgeRepeatX==1){
                        //判断是否与Y重复
                        int JudgeRepeatY=1;
                        for(int z=0;z<multipleLinearPojoList.size();z++){
                            if(codex.equals(multipleLinearPojoList.get(z).getStringCodeY())){
                                JudgeRepeatY=0;
                                break;
                            }
                        }
                        if(JudgeRepeatY==1){
                            SDXList.add(codex);
                        }
                    }
            }
        }
        lookupVariableList=new ArrayList<>();
        //循环创建模型变量
        for(int i=0;i<SDXList.size();i++){
            //创建变量
            Variable variable=(Variable)model.createModelEntity(ModelEntityType.VARIABLE,SDXList.get(i));
            //寻找原始数据
            for(int j=0;j<countryI.getSeriesCode().size();j++){
                if (SDXList.get(i).equals(countryI.getSeriesCode().get(j))){
                    //创建表函数的连接线
                    Converter lookupConverter=model.createConverter(variable);
                    //载入表函数的数据
                    List<YearRecord> InputData=countryI.getData().get(j);
                    double[] TempTimeX=new double[InputData.size()];
                    double[] TempValueX=new double[InputData.size()];
                    for(int tempI=0;tempI<InputData.size();tempI++){
                        String TempYear=InputData.get(tempI).getYear();
                        TempYear=TempYear.substring(1,5);
                        TempTimeX[tempI]=Double.parseDouble(TempYear);
                        TempValueX[tempI]=Double.parseDouble(InputData.get(tempI).getValue());
                    }
                    TreeMap<Integer, Double> lookupTable=new TreeMap<>();
                    //找到了数据，载入表函数数据
                    for(int Yi=0;Yi<(SDEndYear-SDStartYear+1);Yi++){
                        lookupTable.put(SDStartYear+Yi,LinearInterpolation(SDStartYear+Yi,TempTimeX.length,TempTimeX,TempValueX));
                    }
                    System.out.println(lookupTable.toString());
                    //再将表函数的数据载入模型变量内
                    lookupConverter.setFunction(new IFunction() {
                        @Override
                        public double calculateEntityValue() {
                            double currentTime=model.getCurrentTime();
                            double result=lookupTable.get((int)currentTime);
                            return result;
                        }
                    });

                }
            }
            lookupVariableList.add(variable);
        }
    }

    //入度不为零的指标，作为因变量，由表函数回归而得
    public void GetYVariable() throws ModelException {
        SDYList=new ArrayList<>();
        NormalVarableList=new ArrayList<>();
        //先初始化Y变量
        for(int i=0;i<multipleLinearPojoList.size();i++){
            MultipleLinearPojo multipleLinearPojo=multipleLinearPojoList.get(i);
            String codey=multipleLinearPojo.getStringCodeY();
            //创建变量Y
            Variable variableY=(Variable)model.createModelEntity(ModelEntityType.VARIABLE,codey);
            //加入ListY列表
            SDYList.add(codey);
            //加入NormalYList列表
            NormalVarableList.add(variableY);
        }
        //然后初始化连接线
        for(int i=0;i<multipleLinearPojoList.size();i++){
            MultipleLinearPojo multipleLinearPojo=multipleLinearPojoList.get(i);
            String codey=multipleLinearPojo.getStringCodeY();
            //创建变量Y
            Variable variableY=NormalVarableList.get(i);
            //创建变量X
            ArrayList<Variable> input=new ArrayList<Variable>();
            for(int xi=0;xi<multipleLinearPojo.getStringCodeX().size();xi++){
                String oneX=multipleLinearPojo.getStringCodeX().get(xi);
                for(int xj=0;xj<SDXList.size();xj++){
                    //这里仅仅从x里面找了 没有从Y里面找 所以导致Y的个数变低
                    if(oneX.equals(SDXList.get(xj))){
                        input.add(this.lookupVariableList.get(xj));
                        break;
                    }
                }
                //如果x没找到的从Y列表里找
                for(int yj=0;yj<SDYList.size();yj++){
                    if(oneX.equals(SDYList.get(yj))){
                        input.add(NormalVarableList.get(yj));
                        break;
                    }
                }
            }
            //将List转化为参数
            Variable[] variableX=new Variable[input.size()];
            input.toArray(variableX);
            Converter variableConvert=model.createConverter(variableY,variableX);
            variableConvert.setFunction(new IFunction() {
                //回归计算函数
                @Override
                public double calculateEntityValue() {
                    double result=0;
                    for(int xi=0;xi<multipleLinearPojo.getStringCodeX().size();xi++){
                        result=result+(multipleLinearPojo.getMulRes().getParameters()[xi+1]*input.get(xi).getCurrentValue());
                    }
                    result=result+multipleLinearPojo.getMulRes().getParameters()[0];
                    return result;
                }
            });
        }
    }


    //所有变量全部设置完成之后，开始展开模拟
    public void StartSDSimulation(String excelName) throws ModelException{
        Simulation simulation = new Simulation(model);
        CSVExporter SDResult=new CSVExporter("G:/WukaiBag/Year2022/sustainableDevelopmentPaper/coding/JavaProgram/src/test/result/"+excelName+".csv", ",");
        simulation.addSimulationEventListener(SDResult);
        model.setFinalTime(2020);
        try{
            simulation.run();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    //分段线性插值(包含内插和外插）
    private double LinearInterpolation(double X0, int n,double x[],double y[]){
        double Y=0;
        double temp=100;
        int Index=0;
        //由于插值点2000数据异常，将2000（包括之前的数据删除

        //找出离插值点最近的点
        for(int i=0;i<n;i++){
            if(Math.abs(x[i]-X0) <temp){
                temp=Math.abs(x[i]-X0);
                Index=i;
            }
        }
        if(x[Index]==x[0]){
            Y=((y[Index+1]-y[Index])/(x[Index+1]-x[Index]))*(X0-x[Index])+y[Index];
        }else if(x[Index]==x[n-1]){
            Y=((y[Index]-y[Index-1])/(x[Index]-x[Index-1]))*(X0-x[Index])+y[Index];
        }else{
            if(X0>x[Index]){
                Y=((y[Index+1]-y[Index])/(x[Index+1]-x[Index]))*(X0-x[Index])+y[Index];
            }else {
                Y=((y[Index]-y[Index-1])/(x[Index]-x[Index-1]))*(X0-x[Index])+y[Index];
            }
        }
        return Y;
    }

}
