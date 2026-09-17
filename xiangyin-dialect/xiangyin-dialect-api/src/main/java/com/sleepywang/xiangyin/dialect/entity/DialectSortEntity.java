package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 方言分类
 *
 * @author sleepywang
 * @date 2026-09-18 01:05:55
 */
@Data
@TableName("dialect_sort")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "方言分类")
public class DialectSortEntity extends Model<DialectSortEntity> {


    /**
     * 分类编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="分类编号")
    private Integer id;

    /**
     * 所属方案（1、2、3、4）
     */
    @Schema(description="所属方案（1、2、3、4）")
    private Integer plan;

    /**
     * 一级片区
     */
    @Schema(description="一级片区")
    private Integer topId;

    /**
     * 二级片区
     */
    @Schema(description="二级片区")
    private Integer secondId;

    /**
     * 三级片区
     */
    @Schema(description="三级片区")
    private Integer thirdId;

    /**
     * 四级片区
     */
    @Schema(description="四级片区")
    private Integer fourthId;

    /**
     * 五级片区
     */
    @Schema(description="五级片区")
    private Integer fifthId;

    /**
     * 过渡编号（多个的话用逗号分隔）
     */
    @Schema(description="过渡编号（多个的话用逗号分隔）")
    private String transitionIds;

    /**
     * 颜色（有过渡的话也是逗号分隔）
     */
    @Schema(description="颜色（有过渡的话也是逗号分隔）")
    private String colours;

    /**
     * 是否删除
     */
    @Schema(description="是否删除")
    private String isDeleted;

    /**
     * 分类说明
     */
    @Schema(description="分类说明")
    private String sortExplanation;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;

    /**
     * 创建者
     */
    @Schema(description="创建者")
    private Integer creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
