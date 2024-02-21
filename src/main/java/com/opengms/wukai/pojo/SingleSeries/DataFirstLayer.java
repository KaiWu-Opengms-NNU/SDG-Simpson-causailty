package com.opengms.wukai.pojo.SingleSeries;

import lombok.Data;

import java.util.List;

/**对应返回值中的第二层data
 * @Description
 * @Author  Kai
 * @Date 2022/8/22
 */

@Data
public class DataFirstLayer {
    private String dimensions;
    private String sort;
    private List<DataSecondLayer> data;
}
