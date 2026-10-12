package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 点赞记录
 *
 * @author pig
 * @date 2026-10-09 22:57:24
 */
@Data
@TableName("object_like")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "点赞记录")
public class ObjectLikeEntity extends Model<ObjectLikeEntity> {


    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description="id")
    private Integer id;

    /**
     * 点赞对象的类型（语料、表）
     */
    @Schema(description="点赞对象的类型（语料、表）")
    private String objectType;

    /**
     * 点赞对象id
     */
    @Schema(description="点赞对象id")
    private Integer objectId;

    /**
     * 用户id
     */
    @Schema(description="用户id")
    private Long userId;

    /**
     * 点赞时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="点赞时间")
    private LocalDateTime createTime;
}
