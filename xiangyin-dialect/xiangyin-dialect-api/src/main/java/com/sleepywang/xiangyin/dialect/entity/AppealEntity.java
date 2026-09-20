package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 上诉表
 *
 * @author sleepywang
 * @date 2026-09-20 21:31:45
 */
@Data
@TableName("appeal")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "上诉表")
public class AppealEntity extends Model<AppealEntity> {


    /**
     * 上诉编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="上诉编号")
    private Integer id;

    /**
     * 通知编号（违规通知的编号）
     */
    @Schema(description="通知编号（违规通知的编号）")
    private Integer noticeId;

    /**
     * 上诉原因
     */
    @Schema(description="上诉原因")
    private String reason;

    /**
     * 状态（草稿-DRAFT、已提交-SUBMITTED、通过-PASSED、驳回-REJECTED、已删除-DELETED）
     */
    @Schema(description="状态（草稿-DRAFT、已提交-SUBMITTED、通过-PASSED、驳回-REJECTED、已删除-DELETED）")
    private String status;

    /**
     * 处理结果回复
     */
    @Schema(description="处理结果回复")
    private String resultReply;

    /**
     * 上诉时间
     */
    @Schema(description="上诉时间")
    private LocalDateTime appealTime;

    /**
     * 处理时间
     */
    @Schema(description="处理时间")
    private LocalDateTime processTime;
}
