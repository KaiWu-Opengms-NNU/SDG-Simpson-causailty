package com.opengms.wukai.pojo.Granger;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description Storage of adjacency chain tables of graphs
 * @Author  Kai
 * @Date 2023/2/4
 */
@Data
public class GraphPojo {
    private String CodeX;
    private int VisitSta;
    private List<String> CodeY ;

    //无参构造的时候初始化链表
    public GraphPojo(){
        this.VisitSta=0;
        this.CodeY=new ArrayList<>();
    }


}
