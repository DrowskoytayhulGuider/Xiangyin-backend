package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 评论
 *
 * @author sleepywang
 * @date 2026-09-18 01:19:46
 */
@Data
@TableName("comment")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "评论")
public class CommentEntity extends Model<CommentEntity> {


    /**
     * 评论编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="评论编号")
    private Integer id;

    /**
     * 评论内容
     */
    @Schema(description="评论内容")
    private String content;

    /**
     * 父评论ID
     */
    @Schema(description="父评论ID")
    private Integer parentId;

    /**
     * 对象类型（语料-CORPUS、字表-LIST）
     */
    @Schema(description="对象类型（语料-CORPUS、字表-LIST）")
    private String objectType;

    /**
     * 对象ID
     */
    @Schema(description="对象ID")
    private Integer objectId;

    /**
     * 状态（草稿-DRAFT、已发布-RELEASED、已隐藏-INVISIBLE、已删除-DELETED）
     */
    @Schema(description="状态（草稿-DRAFT、已发布-RELEASED、已隐藏-INVISIBLE、已删除-DELETED）")
    private String status;

    /**
     * 评论者
     */
    @Schema(description="评论者")
    private Integer posterId;

    /**
     * 回复对象ID
     */
    @Schema(description="回复对象ID")
    private Integer replyId;

    /**
     * 提到者ID列表
     */
    @Schema(description="提到者ID列表")
    private String mentionedIds;

    /**
     * 发布时间
     */
    @Schema(description="发布时间")
    private LocalDateTime releaseTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
