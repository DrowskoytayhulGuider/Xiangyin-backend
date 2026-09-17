package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 通知消息
 *
 * @author sleepywang
 * @date 2026-09-18 01:09:23
 */
@Data
@TableName("notice")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "通知消息")
public class NoticeEntity extends Model<NoticeEntity> {


    /**
     * 通知编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="通知编号")
    private Integer id;

    /**
     * 接收者
     */
    @Schema(description="接收者")
    private Integer recipientId;

    /**
     * 类型系统通知-SYS_NOTICE、举报结果-REPORT_RESULT、违规通知-VIOLATION_NOTICE）
     */
    @Schema(description="类型系统通知-SYS_NOTICE、举报结果-REPORT_RESULT、违规通知-VIOLATION_NOTICE）")
    private String type;

    /**
     * 通知内容
     */
    @Schema(description="通知内容")
    private String content;

    /**
     * 通知时间
     */
    @Schema(description="通知时间")
    private LocalDateTime noticeTime;

    /**
     * 状态（草稿-DRAFT、已发送-SENT、已删除-DELETED）
     */
    @Schema(description="状态（草稿-DRAFT、已发送-SENT、已删除-DELETED）")
    private String status;
}
