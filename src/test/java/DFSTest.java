import com.opengms.wukai.pojo.Granger.GraphPojo;
import com.opengms.wukai.pojo.Result.Indicator.LinkForPercent;
import com.opengms.wukai.pojo.Result.Indicator.LinkForPercentGoals;
import com.opengms.wukai.pojo.Result.Spatial.ArcmapForSpace;
import com.opengms.wukai.tool.DFS;
import com.opengms.wukai.tool.OperateExcel;
import com.opengms.wukai.tool.SDGIndicatorTool;
import org.apache.poi.ss.formula.functions.Count;
import org.junit.Test;
import org.python.antlr.ast.Str;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DFSTest {
    //测试函数
    @org.junit.Test
    public void DFSTest() {
        //初始化图
        List<GraphPojo> Graph = new ArrayList<>();
        GraphPojo Node1 = new GraphPojo();
        Node1.setCodeX("1");
        Node1.getCodeY().add("2");
        Node1.getCodeY().add("3");
        Graph.add(Node1);
        GraphPojo Node2 = new GraphPojo();
        Node2.setCodeX("2");
        Node2.getCodeY().add("4");
        Graph.add(Node2);
        GraphPojo Node3 = new GraphPojo();
        Node3.setCodeX("4");
        Node3.getCodeY().add("7");
        Node3.getCodeY().add("8");
        Graph.add(Node3);
        GraphPojo Node4 = new GraphPojo();
        Node4.setCodeX("5");
        Node4.getCodeY().add("3");
        Graph.add(Node4);
        GraphPojo Node5 = new GraphPojo();
        Node5.setCodeX("7");
        //Node5.getCodeY().add("2");
        Graph.add(Node5);
        GraphPojo Node6 = new GraphPojo();
        Node6.setCodeX("3");
        Node6.getCodeY().add("5");
        Graph.add(Node6);
        GraphPojo Node7 = new GraphPojo();
        Node7.setCodeX("8");
        Node7.getCodeY().add("9");
        Graph.add(Node7);
        GraphPojo Node8 = new GraphPojo();
        Node8.setCodeX("9");
        Node8.getCodeY().add("2");
        Graph.add(Node8);
        //深度遍历算法
        DFS dfs=new DFS(Graph);
//        for (int i=0;i<Graph.size();i++){
        dfs.DFS(0);
//    }
        dfs.ConvertGranger();
        System.out.println("success");
    }

    @Test
    public void convert() throws IOException {
        OperateExcel operateExcel = new OperateExcel();
        List<List<String>> CountryList=operateExcel.readExcel(new File("G:/WukaiBag/Year2023/sustainableofpaper2023/result1/result1031/countryList.xlsx"));
        //从ArcMap类转化为
        List<ArcmapForSpace> arcmapForSpaceList=new ArrayList<>();
        for(int i=1;i<CountryList.size();i++){
            ArcmapForSpace oneMap=new ArcmapForSpace();
            oneMap.setAreaName(CountryList.get(i).get(0));
            oneMap.setAreaAb(CountryList.get(i).get(1));
            oneMap.setAreaID(CountryList.get(i).get(2));
            oneMap.setScale(Integer.parseInt(CountryList.get(i).get(3)));
            oneMap.setGoalX(CountryList.get(i).get(4));
            oneMap.setGoalY(CountryList.get(i).get(5));
            oneMap.setTargetX(CountryList.get(i).get(6));
            oneMap.setTargetY(CountryList.get(i).get(7));
            oneMap.setStaticsData(null);
            oneMap.setInteraction(CountryList.get(i).get(9));
            oneMap.setStrengthAll(Double.parseDouble(CountryList.get(i).get(10)));
            oneMap.setStrengthAllRSquared(Double.parseDouble(CountryList.get(i).get(11)));
            oneMap.setStrengthWithDirection(Double.parseDouble(CountryList.get(i).get(12)));
            oneMap.setPercent(Double.parseDouble(CountryList.get(i).get(13)));
            oneMap.setPValue(Double.parseDouble(CountryList.get(i).get(14)));
            if(oneMap.getPValue()<0.1){
                arcmapForSpaceList.add(oneMap);
            }
        }


        List<LinkForPercentGoals> LinkGoalCountryList=new ArrayList<>();
        List<LinkForPercentGoals> MainLinkGoalCountryList=new ArrayList<>();
        for(int i=1;i<18;i++){
            for(int j=1;j<18;j++){
                LinkForPercentGoals linkForPercentGoalsMF=new LinkForPercentGoals(String.valueOf(i),String.valueOf(j),"MF",3,arcmapForSpaceList);
                if(linkForPercentGoalsMF.getSingleInteractionNumber()>0){
                    LinkGoalCountryList.add(linkForPercentGoalsMF);
                }
                LinkForPercentGoals linkForPercentGoalsMI=new LinkForPercentGoals(String.valueOf(i),String.valueOf(j),"MI",3,arcmapForSpaceList);
                if(linkForPercentGoalsMI.getSingleInteractionNumber()>0){
                    LinkGoalCountryList.add(linkForPercentGoalsMI);
                }
                LinkForPercentGoals linkForPercentGoalsSF=new LinkForPercentGoals(String.valueOf(i),String.valueOf(j),"SF",3,arcmapForSpaceList);
                if(linkForPercentGoalsSF.getSingleInteractionNumber()>0){
                    LinkGoalCountryList.add(linkForPercentGoalsSF);
                }
                LinkForPercentGoals linkForPercentGoalsSI=new LinkForPercentGoals(String.valueOf(i),String.valueOf(j),"SI",3,arcmapForSpaceList);
                if(linkForPercentGoalsSI.getSingleInteractionNumber()>0){
                    LinkGoalCountryList.add(linkForPercentGoalsSI);
                }
                LinkForPercentGoals linkForPercentGoalsFI=new LinkForPercentGoals(String.valueOf(i),String.valueOf(j),"FI",3,arcmapForSpaceList);
                if(linkForPercentGoalsFI.getSingleInteractionNumber()>0){
                    LinkGoalCountryList.add(linkForPercentGoalsFI);
                }
                LinkForPercentGoals linkForPercentGoalsIF=new LinkForPercentGoals(String.valueOf(i),String.valueOf(j),"IF",3,arcmapForSpaceList);
                if(linkForPercentGoalsIF.getSingleInteractionNumber()>0){
                    LinkGoalCountryList.add(linkForPercentGoalsIF);
                }
                //得到主要的交互作用关系
                double[] StrengthMax= new double[]{
                                                   Math.abs(linkForPercentGoalsMF.getAllStrength()),
                                                   Math.abs(linkForPercentGoalsMI.getAllStrength()),
                                                   Math.abs(linkForPercentGoalsSF.getAllStrength()),
                                                   Math.abs(linkForPercentGoalsSI.getAllStrength()),
                                                   Math.abs(linkForPercentGoalsFI.getAllStrength()),
                                                   Math.abs(linkForPercentGoalsIF.getAllStrength())
                };
                double TempData=0;
                int TempIndex=-1;
                for(int Max=0;Max<StrengthMax.length;Max++){
                    if(StrengthMax[Max]>TempData){
                        TempData=StrengthMax[Max];
                        TempIndex=Max;
                    }
                }
                if(TempIndex!=-1){
                    switch (TempIndex){
                        case 0:
                            double Percent1=(linkForPercentGoalsMF.getPercent()*linkForPercentGoalsMF.getPercent())/
                                    (linkForPercentGoalsMF.getPercent()+linkForPercentGoalsMI.getPercent()+linkForPercentGoalsSF.getPercent()+
                                            linkForPercentGoalsSI.getPercent()+linkForPercentGoalsFI.getPercent()+linkForPercentGoalsIF.getPercent());
                            linkForPercentGoalsMF.setPercent(Percent1);
                            MainLinkGoalCountryList.add(linkForPercentGoalsMF);
                            break;
                        case 1:
                            double Percent2=(linkForPercentGoalsMI.getPercent()*linkForPercentGoalsMI.getPercent())/
                                    (linkForPercentGoalsMF.getPercent()+linkForPercentGoalsMI.getPercent()+linkForPercentGoalsSF.getPercent()+
                                            linkForPercentGoalsSI.getPercent()+linkForPercentGoalsFI.getPercent()+linkForPercentGoalsIF.getPercent());
                            linkForPercentGoalsMI.setPercent(Percent2);
                            MainLinkGoalCountryList.add(linkForPercentGoalsMI);
                            break;
                        case 2:
                            double Percent3=(linkForPercentGoalsSF.getPercent()*linkForPercentGoalsSF.getPercent())/
                                    (linkForPercentGoalsMF.getPercent()+linkForPercentGoalsMI.getPercent()+linkForPercentGoalsSF.getPercent()+
                                            linkForPercentGoalsSI.getPercent()+linkForPercentGoalsFI.getPercent()+linkForPercentGoalsIF.getPercent());
                            linkForPercentGoalsSF.setPercent(Percent3);
                            MainLinkGoalCountryList.add(linkForPercentGoalsSF);
                            break;
                        case 3:
                            double Percent4=(linkForPercentGoalsSI.getPercent()*linkForPercentGoalsSI.getPercent())/
                                    (linkForPercentGoalsMF.getPercent()+linkForPercentGoalsMI.getPercent()+linkForPercentGoalsSF.getPercent()+
                                            linkForPercentGoalsSI.getPercent()+linkForPercentGoalsFI.getPercent()+linkForPercentGoalsIF.getPercent());
                            linkForPercentGoalsSI.setPercent(Percent4);
                            MainLinkGoalCountryList.add(linkForPercentGoalsSI);
                            break;
                        case 4:
                            double Percent5=(linkForPercentGoalsFI.getPercent()*linkForPercentGoalsFI.getPercent())/
                                    (linkForPercentGoalsMF.getPercent()+linkForPercentGoalsMI.getPercent()+linkForPercentGoalsSF.getPercent()+
                                            linkForPercentGoalsSI.getPercent()+linkForPercentGoalsFI.getPercent()+linkForPercentGoalsIF.getPercent());
                            linkForPercentGoalsFI.setPercent(Percent5);
                            MainLinkGoalCountryList.add(linkForPercentGoalsFI);
                            break;
                        case 5:
                            double Percent6=(linkForPercentGoalsIF.getPercent()*linkForPercentGoalsIF.getPercent())/
                                    (linkForPercentGoalsMF.getPercent()+linkForPercentGoalsMI.getPercent()+linkForPercentGoalsSF.getPercent()+
                                            linkForPercentGoalsSI.getPercent()+linkForPercentGoalsFI.getPercent()+linkForPercentGoalsIF.getPercent());
                            linkForPercentGoalsIF.setPercent(Percent6);
                            MainLinkGoalCountryList.add(linkForPercentGoalsIF);
                            break;
                    }
                }

            }
        }
        operateExcel.writeExcel("G:/WukaiBag/Year2023/sustainableofpaper2023/result1/result1031/", "CountryGoalList",LinkForPercentGoals.class, LinkGoalCountryList);
        operateExcel.writeExcel("G:/WukaiBag/Year2023/sustainableofpaper2023/result1/result1031/", "MainCountryGoalList",LinkForPercentGoals.class, MainLinkGoalCountryList);
    }

    @Test
    public void ComparedNEInteraction() throws IOException{

    }

}
