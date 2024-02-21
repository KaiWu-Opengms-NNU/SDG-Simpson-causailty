package com.opengms.wukai.pojo.SingleSeries;

import lombok.Data;

import java.util.List;

/**对应返回值中的属性值
 * @Description
 * @Author  Kai
 * @Date 2022/8/22
 */

@Data
public class Attribute {
        private String id;
        private List<Object> codes;
}
