package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 私信消息
 *
 * @author sleepywang
 * @date 2026-09-18 01:10:20
 */
@Data
@TableName("message")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "私信消息")
public class MessageEntity extends Model<MessageEntity> {


    /**
     * 消息id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="消息id")
    private Integer id;

    /**
     * 私信id
     */
    @Schema(description="私信id")
    private Integer conversationId;

    /**
     * 父消息id
     */
    @Schema(description="父消息id")
    private Integer parentId;

    /**
     * 消息内容
     */
    @Schema(description="消息内容")
    private String content;

    /**
     * 发送者
     */
    @Schema(description="发送者")
    private Integer initiatorId;

    /**
     * 接收者
     */
    @Schema(description="接收者")
    private Integer recipientId;

    /**
     * 状态（已撤回-WITHDRAWED、未读-UNREAD、发送中-SENDING、已读-READ）
     */
    @Schema(description="状态（已撤回-WITHDRAWED、未读-UNREAD、发送中-SENDING、已读-READ）")
    private String status;

    /**
     * 发送时间
     */
    @Schema(description="发送时间")
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
