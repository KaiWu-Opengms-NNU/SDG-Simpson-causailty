package com.opengms.wukai.dto;

import lombok.Data;

import java.util.List;

/**单个国家单个指标时序数据DTO
 * @Description
 * @Author  Kai
 * @Date 2022/8/22
 */
@Data
public class SingleSeriesDto {
    private String indicators;
    private String seriesCode;
    private String areaCode;
    private String years;
}

