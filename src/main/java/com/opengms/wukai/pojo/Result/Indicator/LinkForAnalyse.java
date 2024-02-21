package com.opengms.wukai.pojo.Result.Indicator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**汇总列表
 * @Description Analysis the link(interaction) between some SDG indicators
 * @Author  Kai
 * @Date 2023/3/6
 */


@Data
public class LinkForAnalyse {

    //记录X指标
    private List<String> IndicatorsXList=new ArrayList<>();
    //记录Y指标
    private String IndicatorY;
    //记录联结强度
    private List<String> InteractionList=new ArrayList<>();


}
