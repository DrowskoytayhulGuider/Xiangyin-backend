package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 词表
 *
 * @author sleepywang
 * @date 2026-09-18 01:25:25
 */
@Data
@TableName("word_list")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "词表")
public class WordListEntity extends Model<WordListEntity> {


    /**
     * 句表编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="句表编号")
    private Integer id;

    /**
     * 句表名称
     */
    @Schema(description="句表名称")
    private String name;

    /**
     * 所属方言片区一（分区方案一）
     */
    @Schema(description="所属方言片区一（分区方案一）")
    private Integer dialectSort1Id;

    /**
     * 所属方言片区二
     */
    @Schema(description="所属方言片区二")
    private Integer dialectSort2Id;

    /**
     * 所属方言片区三
     */
    @Schema(description="所属方言片区三")
    private Integer dialectSort3Id;

    /**
     * 所属方言片区四
     */
    @Schema(description="所属方言片区四")
    private Integer dialectSort4Id;

    /**
     * 制作人
     */
    @Schema(description="制作人")
    private Integer makerId;

    /**
     * 关键字
     */
    @Schema(description="关键字")
    private String keywords;

    /**
     * 省
     */
    @Schema(description="省")
    private String province;

    /**
     * 市
     */
    @Schema(description="市")
    private String city;

    /**
     * 县
     */
    @Schema(description="县")
    private String county;

    /**
     * 乡
     */
    @Schema(description="乡")
    private String town;

    /**
     * 点赞数
     */
    @Schema(description="点赞数")
    private Integer likeNumber;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;

    /**
     * 发布时间
     */
    @Schema(description="发布时间")
    private LocalDateTime releaseTime;

    /**
     * 状态（草稿DRAFT、已发布(RELEASED)、已删除(DELETED)）
     */
    @Schema(description="状态（草稿DRAFT、已发布(RELEASED)、已删除(DELETED)）")
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
