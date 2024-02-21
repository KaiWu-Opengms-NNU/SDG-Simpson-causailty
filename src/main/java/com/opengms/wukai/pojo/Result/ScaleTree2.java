package com.opengms.wukai.pojo.Result;

import com.opengms.wukai.pojo.Result.AggregationForStatics;
import lombok.Data;
import org.python.antlr.ast.Num;

import java.util.ArrayList;
import java.util.List;


/**汇总列表
 * @Description Analysis the  difference between the scale of world
 * @Author  Kai
 * @Date 2023/3/6
 */

@Data
public class ScaleTree2 {
    //初始化数据
    private int ID;//本身的序号ID
    private String Name;
    private String Short;
    private int[] ChildrenID;//子区域的序号ID组
    private int ParentID;//父区域的序号ID,如果没有父层级则设为0
    private int level;//树的层级
    private List<List<String>> RawGranger=new ArrayList<>();
    private double[] SDGDegreeList=new double[17];


    private double[] SDGInteractionList=new double[17];
    private double[] SDGPrioritizeList=new double[17];

    //无参构造
    public ScaleTree2(){}
    //有参构造
    public ScaleTree2(int ID, String aShort, int[] children, int parentID, int level, List<AggregationForStatics> aggregationList){
        this.ID=ID;
        this.Short=aShort;
        this.ChildrenID=children;
        this.ParentID=parentID;
        this.level=level;
        for (int i=0;i<SDGDegreeList.length;i++){
            SDGDegreeList[i]=0;
        }
    }
    //键值对ID代码映射，分区尺度
    public String getRegionCode(int ID){
        int[] IDArray=new int[]{15,202,419,21,143,30,35,34,145,151,154,39,155,53,54,57,61};
        String[] CodeArray=new String[]{"NFR","SFR","LAC","NAM","CAS","EAS","SEA","SAS","WAS","EEU","NEU","SEU","WEU","ANZ","MEL","MIC","POL"};
        int TempIndex=-1;
        for(int i=0;i<IDArray.length;i++){
            if(IDArray[i]==ID){
                TempIndex=i;
            }
        }
        return CodeArray[TempIndex];
    }
    //可持续发展指标对载入函数

    //可持续发展程度数据载入算法
    public void setSDGDegreeList(List<List<String>> DegreeData){
        if(this.level==0){
            for(int j=2;j<DegreeData.get(0).size();j++){
                int Number=0;
                for(int i=1;i<250;i++){
                    String[] TempArray=DegreeData.get(i).get(j).split("%");
                    double TempData=Double.parseDouble(TempArray[0]);
                    SDGDegreeList[j-2]=SDGDegreeList[j-2]+TempData;
                    Number++;
                }
                SDGDegreeList[j-2]=SDGDegreeList[j-2]/Number;
            }
        }
        if(this.level==2){
            for(int j=2;j<DegreeData.get(0).size();j++){
                int Number=0;
                for(int i=1;i<DegreeData.size();i++){
                    int DegreeDataID=Integer.parseInt(DegreeData.get(i).get(0));
                    int JudgeChildren=-1;
                    for(int childrenID=0;childrenID<this.ChildrenID.length;childrenID++){
                        if(DegreeDataID==this.ChildrenID[childrenID]){
                            JudgeChildren=1;
                        }
                    }
                    if(JudgeChildren==1){
                        String[] TempArray=DegreeData.get(i).get(j).split("%");
                        double TempData=Double.parseDouble(TempArray[0]);
                        SDGDegreeList[j-2]=SDGDegreeList[j-2]+TempData;
                        Number++;
                    }
                }
                SDGDegreeList[j-2]=SDGDegreeList[j-2]/Number;
            }
        }
        if (this.level==3){
            for(int j=2;j<DegreeData.get(0).size();j++){
                for(int i=1;i<DegreeData.size();i++){
                    int DegreeDataID=Integer.parseInt(DegreeData.get(i).get(0));
                    if(DegreeDataID==this.ID){
                        String[] TempArray=DegreeData.get(i).get(j).split("%");
                        double TempData=Double.parseDouble(TempArray[0]);
                        SDGDegreeList[j-2]=SDGDegreeList[j-2]+TempData;
                        break;
                    }
                }

            }
        }

    }
}
