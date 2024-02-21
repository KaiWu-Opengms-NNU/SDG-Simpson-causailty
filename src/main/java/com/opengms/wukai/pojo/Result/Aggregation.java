package com.opengms.wukai.pojo.Result;



import lombok.Data;


/**汇总列表
 * @Description 汇总每一个国家的结果
 * @Author  Kai
 * @Date 2023/3/2
 */
@Data
public class Aggregation{
    //地点
    private String countryName;
    private String countryCode;


    //属性
    private int IndivisibleN;
    private int CancellingN;
    private int ReinforcingOnlyN;
    private int CounteractingOnlyN;
    private int FacilitationToInhibitionN;
    private int InhibitionToFacilitationN;
    private int NeutralInteractionN;

}
