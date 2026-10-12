package com.sleepywang.xiangyin.dialect.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CorpusSaveDTO {
    /**
     * 所属方言点
     */
    private Integer dialectPointId;
    /**
     * 语料名称
     */
    private String name;
    /**
     * 所属派别（新派-NEW、中派-MEDIUM、老派-OLD）
     */
    private String faction;
    /**
     * 所受影响普化(POPULARIZATION)、其他方言影响(AFFECTED_BY_OTHER_DIALECT)、NONE）
     */
    private String affection;
    /**
     * 发音人年龄
     */
    private Integer age;
    /**
     * 发音人性别
     */
    private String sex;
    /**
     * 发音人文化程度
     */
    private String educationLevel;
    /**
     * 发音人是否长期居于外地
     */
    private Boolean liveOutside;
    /**
     * 发音人常住地
     */
    private String permanentPlace;
    /**
     * 发音人职业
     */
    private String vocation;
    /**
     * 发音人籍贯
     */
    private String originalPlace;
    /**
     * 语料说明
     */
    private String introduction;
    /**
     * 音系说明
     */
    private String phonologyExplanation;
    /**
     * 备注
     */
    private String remark;
}
