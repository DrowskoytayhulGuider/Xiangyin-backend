package com.sleepywang.xiangyin.dialect.dto;

import lombok.Data;

@Data
public class DialectSortUpdateDTO {
    private Integer id;
    // 分类说明
    private String sortExplanation;
    // 备注
    private String remark;
}
