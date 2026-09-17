package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 私信
 *
 * @author sleepywang
 * @date 2026-09-18 01:12:04
 */
@Data
@TableName("conversation")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "私信")
public class ConversationEntity extends Model<ConversationEntity> {


    /**
     * 私信id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="私信id")
    private Integer id;

    /**
     * 发起对象
     */
    @Schema(description="发起对象")
    private Integer initiatorId;

    /**
     * 接收对象
     */
    @Schema(description="接收对象")
    private Integer recipientId;
    //recipientId

    /**
     * 最后一条消息时间
     */
    @Schema(description="最后一条消息时间")
    private LocalDateTime lastMessageTime;

    /**
     * 状态（已关闭-CLOSED、单向开启-SINGLE_ENABLE、双向开启-BIDIRECTION_ENABLE、已删除-DELETED）
     */
    @Schema(description="状态（已关闭-CLOSED、单向开启-SINGLE_ENABLE、双向开启-BIDIRECTION_ENABLE、已删除-DELETED）")
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
