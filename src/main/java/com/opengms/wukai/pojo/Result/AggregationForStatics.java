package com.opengms.wukai.pojo.Result;

import com.opengms.wukai.pojo.Granger.GrangerResult;
import com.opengms.wukai.pojo.Result.Indicator.LinkForNumber;

import lombok.Data;
import org.apache.poi.hssf.util.HSSFColor;
import org.python.antlr.ast.Str;


import java.util.List;

/**汇总列表
 * @Description 专门用来分析每一个国家的结果
 * @Author  Kai
 * @Date 2023/3/2
 */

@Data
public class AggregationForStatics {

    //地点
    private String countryName;
    private String countryCode;
    private String countryAb;

    //每个国家的强度和类型
    private List<List<String>> Indivisible;
    private List<List<String>> Cancelling;
    private List<List<String>> ReinforcingOnly;
    private List<List<String>> CounteractingOnly;
    private List<List<String>> ReinforcingThenCounteracting;
    private List<List<String>> CounteractingThenReinforcing;
    private List<List<String>> NeuralInteraction;


}
