package com.opengms.wukai.dto;

import lombok.Data;

/**单个国家单个指标时序数据DTO
 * @Description
 * @Author  Kai
 * @Date 2022/2/13
 */

@Data
public class NormalDto {
    private String Goal;
    private String Indicator;
    private String SeriesCode;
    private String Data;
}
