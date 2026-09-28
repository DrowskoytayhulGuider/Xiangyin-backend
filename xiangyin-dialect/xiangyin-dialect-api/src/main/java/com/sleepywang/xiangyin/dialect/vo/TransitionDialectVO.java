package com.sleepywang.xiangyin.dialect.vo;

import lombok.Data;

@Data
public class TransitionDialectVO {
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
}
