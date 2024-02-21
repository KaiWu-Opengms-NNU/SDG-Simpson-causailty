package com.opengms.wukai.pojo.Result;

import lombok.Data;

import java.util.List;


/**汇总列表
 * @Description Analysis the  difference between the scale of world
 * @Author  Kai
 * @Date 2023/3/6
 */

@Data
public class ScaleTree {
    //初始化数据
    private int ID;//本身的序号ID
    private String Short;
    private int[] ChildrenID;//子区域的序号ID组
    private int ParentID;//父区域的序号ID,如果没有父层级则设为0
    private int level;//树的层级
    private AggregationForStatics aggregation;//记录国家的数据

    //无参构造
    public ScaleTree(){}
    //有参构造

    public ScaleTree(int ID, String aShort, int[] children, int parentID, int level, List<AggregationForStatics> aggregationList){
        this.ID=ID;
        this.Short=aShort;
        this.ChildrenID=children;
        this.ParentID=parentID;
        this.level=level;
        for(int i=0;i<aggregationList.size();i++){
            if(Integer.parseInt(aggregationList.get(i).getCountryCode())==this.ID){
                this.aggregation=aggregationList.get(i);
            }
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
}
