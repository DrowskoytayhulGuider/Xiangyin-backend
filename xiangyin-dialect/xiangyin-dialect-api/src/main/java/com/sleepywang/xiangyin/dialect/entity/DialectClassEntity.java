package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 方言片
 *
 * @author sleepywang
 * @date 2026-09-18 01:07:31
 */
@Data
@TableName("dialect_class")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "方言片")
public class DialectClassEntity extends Model<DialectClassEntity> {


    /**
     * 片区编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="片区编号")
    private Integer id;

    /**
     * 所属方案（1、2、3、4）
     */
    @Schema(description="所属方案（1、2、3、4）")
    private Integer plan;

    /**
     * 片区名称
     */
    @Schema(description="片区名称")
    private String name;

    /**
     * 片区层级
     */
    @Schema(description="片区层级")
    private String level;

    /**
     * 划片依据
     */
    @Schema(description="划片依据")
    private String classificationEvidence‌;

    /**
     * 颜色
     */
    @Schema(description="颜色")
    private String colour;

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
     * 是否删除
     */
    @Schema(description="是否删除")
    private String isDeleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
