package com.opengms.wukai.pojo.SingleSeries;

import lombok.Data;

/**对应返回值中的第一层data
 * @Description
 * @Author  Kai
 * @Date 2022/8/22
 */

@Data
public class DataSecondLayer {
    private String geoAreaCode;
    private String geoAreaName;
    private String parentId;
    private String children;
    private String years;
}
