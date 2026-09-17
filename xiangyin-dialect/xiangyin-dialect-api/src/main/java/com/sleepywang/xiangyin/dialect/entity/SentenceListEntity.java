package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 句表
 *
 * @author sleepywang
 * @date 2026-09-18 01:31:15
 */
@Data
@TableName("sentence_list")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "句表")
public class SentenceListEntity extends Model<SentenceListEntity> {


    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Integer id;

    /**
     * name
     */
    @Schema(description="name")
    private String name;

    /**
     * dialectSort1Id
     */
    @Schema(description="dialectSort1Id")
    private Integer dialectSort1Id;

    /**
     * dialectSort2Id
     */
    @Schema(description="dialectSort2Id")
    private Integer dialectSort2Id;

    /**
     * dialectSort3Id
     */
    @Schema(description="dialectSort3Id")
    private Integer dialectSort3Id;

    /**
     * dialectSort4Id
     */
    @Schema(description="dialectSort4Id")
    private Integer dialectSort4Id;

    /**
     * makerId
     */
    @Schema(description="makerId")
    private Integer makerId;

    /**
     * keywords
     */
    @Schema(description="keywords")
    private String keywords;

    /**
     * province
     */
    @Schema(description="province")
    private String province;

    /**
     * city
     */
    @Schema(description="city")
    private String city;

    /**
     * county
     */
    @Schema(description="county")
    private String county;

    /**
     * town
     */
    @Schema(description="town")
    private String town;

    /**
     * likeNumber
     */
    @Schema(description="likeNumber")
    private Integer likeNumber;

    /**
     * remark
     */
    @Schema(description="remark")
    private String remark;

    /**
     * releaseTime
     */
    @Schema(description="releaseTime")
    private LocalDateTime releaseTime;

    /**
     * status
     */
    @Schema(description="status")
    private String status;

    /**
     * createTime
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="createTime")
    private LocalDateTime createTime;
}
