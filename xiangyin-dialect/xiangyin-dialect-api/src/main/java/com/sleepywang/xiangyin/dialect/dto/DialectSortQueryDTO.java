package com.sleepywang.xiangyin.dialect.dto;

import com.sleepywang.xiangyin.admin.api.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DialectSortQueryDTO {
    private Integer id;
    // 所属方案编号（前端对应）
    private Integer plan;
    // 一级分区
    private String topClassification;
    // 二级分区
    private String secondClassification;
    // 三级分区
    private String thirdClassification;
    // 四级分区
    private String fourthClassification;
    // 五级分区
    private String fifthClassification;
    // 过渡方言分类id
    private Integer[] transitionDialectSortIds;

}
