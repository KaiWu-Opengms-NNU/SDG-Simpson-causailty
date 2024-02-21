package com.opengms.wukai.pojo.SingleSeries;


import lombok.Data;

import java.util.List;

/**单个国家单个指标时序数据返回值类
 * @Description
 * @Author  Kai
 * @Date 2022/8/22
 */
@Data
public class SingleSeries {
    private String totalElements;
    private String seriesDescription;
    private String series;
    private String source;
    //可转成Json数组
    private List<Attribute> attributes;
    //可转成Json数组
    private List<DataFirstLayer> data;




}
