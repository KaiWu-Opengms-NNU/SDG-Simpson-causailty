package com.opengms.wukai.tool;

import com.opengms.wukai.dto.GraphDto;
import com.opengms.wukai.pojo.Granger.GraphPojo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 图的深度遍历算法
 * @Author  Kai
 * @Date 2023/1/30
 */

@Data
public class DFS {
    //载入图存储
    private List<GraphPojo> GraphList;
    //存储环的个数
    private int CycleNumber;
    //存储可能存在的环
    private List<String> DFSResultList;
    //存储成环的因果指标对
    private List<GraphDto> CycleIndicatorList;

    public DFS(List<GraphPojo> graphList){
        this.GraphList=graphList;
        this.DFSResultList=new ArrayList<>();
        this.CycleNumber=0;

    }

    //根据节点名称判断是否存在Codex序号
    public int JudgeNo(String CodeY){
        int result=-1;
        for(int i=0;i<GraphList.size();i++){
            if(CodeY==GraphList.get(i).getCodeX()){
                result=i;
                break;
            }
        }
        //如果存在和CodeX相同的CodeY，就把CodeX
        return result;
    }
    //深度遍历
    public void DFS(int i){
        System.out.println(GraphList.get(i).getCodeX());
        DFSResultList.add(GraphList.get(i).getCodeX());
        GraphList.get(i).setVisitSta(1);
        for(int j=0;j<GraphList.get(i).getCodeY().size();j++){
            //找到下一个链表
            int JNo=JudgeNo(GraphList.get(i).getCodeY().get(j));
            //如果JNo等于-1，说明这个节点没有出度，不可能存在环
            if(JNo!=-1){
                //并且已经被访问过
                if(GraphList.get(JNo).getVisitSta()==1){
                    DFSResultList.add(GraphList.get(JNo).getCodeX());
                    System.out.println(GraphList.get(JNo).getCodeX());
                    System.out.println("existCycle");
                    DFSResultList.add("existCycle");
                    break;
                //当前结点后边的结点都被访问过，直接跳至下一个结点
                }else if(GraphList.get(JNo).getVisitSta()==-1){
                    System.out.println("--->");
                    continue;
                }else {
                    System.out.println("|");
                    System.out.println("|");
                    System.out.println("v");
                    DFS(JNo);
                }
            }

        }
        //遍历过所有相连的结点后，把本节点标记为-1
        GraphList.get(i).setVisitSta(-1);
    }
    //将String的列表转化Granger的List
    public void ConvertGranger(){
        this.CycleIndicatorList=new ArrayList<>();
        for(int i=0;i<DFSResultList.size();i++){
            //如果遇到existCycle说明存在闭环
            if(DFSResultList.get(i).equals("existCycle")){
                this.CycleNumber++;
                //将上一个作为闭环的起点
                String StartNode=DFSResultList.get(i-1);
                //引入一个变量判断是否是完全环
                int JudgeClosed=0;
                for(int j=i-2;j>=0;j--){
                    //如果遇到了重复的节点，说明是个完全环
                    if(DFSResultList.get(j).equals(StartNode)){
                        JudgeClosed=1;
                        break;
                    }
                    //如果到下一个"existCycle"还没有遇到重复的结点，说明是个部分环。
                    if(DFSResultList.get(j).equals("existCycle")){
                        break;
                    }
                }
                //如果是个全环，才加入到结果里
                if(JudgeClosed==1){
                    for(int j=i-2;j>=0;j--){
                        //如果遇到重复节点说明这个环已经结束，如果没遇到重复的节点说明是部分环，不需要加入
                        if(DFSResultList.get(j).equals(StartNode)||DFSResultList.get(j).equals("existCycle")){
                            GraphDto BEndNode=new GraphDto();
                            //补上最后一条链路形成闭环
                            BEndNode.setStringCodeX(DFSResultList.get(i-2));
                            BEndNode.setStringCodeY(StartNode);
                            CycleIndicatorList.add(BEndNode);
                            GraphDto EndNode=new GraphDto();
                            EndNode.setStringCodeX("existCycle");
                            EndNode.setStringCodeY("existCycle");
                            CycleIndicatorList.add(EndNode);
                            break;
                        }else{
                            GraphDto MiddleNode=new GraphDto();
                            MiddleNode.setStringCodeY(DFSResultList.get(j));
                            MiddleNode.setStringCodeX(DFSResultList.get(j-1));
                            CycleIndicatorList.add(MiddleNode);
                        }
                    }
                }
            }
        }

    }

    //





}
