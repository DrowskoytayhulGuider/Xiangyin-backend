package com.sleepywang.xiangyin.dialect.dto;

import lombok.Data;

@Data
public class DialectSortSaveDTO {
    // 所属方案编号（前端对应）
    private Integer plan;
    // 一级分区
    private Integer topId;
    // 二级分区
    private Integer secondId;
    // 三级分区
    private Integer thirdId;
    // 四级分区
    private Integer fourthId;
    // 五级分区
    private Integer fifthId;
    // 过渡方言分类id
    private Integer[] transitionDialectSortIds;
    // 颜色
//    private String colour;
    // 分类说明
    private String sortExplanation;
    // 备注
    private String remark;
}
