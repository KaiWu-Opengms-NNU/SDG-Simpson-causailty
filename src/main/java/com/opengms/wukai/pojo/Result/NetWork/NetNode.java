package com.opengms.wukai.pojo.Result.NetWork;

import lombok.Data;

import java.util.List;


@Data
public class NetNode {
    private String Goal=null;
    private double WeightInDegree=0;
    private double WeightOutDegree=0;
    private String GeaName;
    private int GeoId;
    private int scale;
    private String MaxOrMin=null;


    public void RelationshipConvertNode(String inputGoal, List<NetRelationship> netRelationshipList){
        int JudgeExist=0;
        for(int i=0;i<netRelationshipList.size();i++){
            if(inputGoal.equals(netRelationshipList.get(i).getGoalX())){
                this.WeightOutDegree=this.WeightOutDegree+netRelationshipList.get(i).getStrength();
                JudgeExist=1;
            }
        }
        for(int i=0;i<netRelationshipList.size();i++){
            if(inputGoal.equals(netRelationshipList.get(i).getGoalY())){
                this.WeightInDegree=this.WeightInDegree+netRelationshipList.get(i).getStrength();
                JudgeExist=1;
            }
        }
        if(JudgeExist==1){
            this.Goal=inputGoal;
            this.GeaName=netRelationshipList.get(0).getAreaName();
            this.scale=netRelationshipList.get(0).getScale();
        }
    }
}
