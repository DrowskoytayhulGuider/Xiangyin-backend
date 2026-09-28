package com.sleepywang.xiangyin.dialect.vo;

import cn.hutool.core.date.DateTime;
import com.sleepywang.xiangyin.admin.api.entity.SysUser;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DialectSortVO {
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
    // 过渡方言分类
    private List<TransitionDialectVO> transitionDialectSort;
    // 颜色 (RGB)
    private String[] colours;
    // 分类说明
    private String sortExplanation;
}
