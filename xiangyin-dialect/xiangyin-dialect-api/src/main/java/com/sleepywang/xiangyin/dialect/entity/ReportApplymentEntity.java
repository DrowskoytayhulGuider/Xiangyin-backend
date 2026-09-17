package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 举报申请
 *
 * @author sleepywang
 * @date 2026-09-18 01:16:12
 */
@Data
@TableName("report_applyment")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "举报申请")
public class ReportApplymentEntity extends Model<ReportApplymentEntity> {


    /**
     * 举报申请编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="举报申请编号")
    private Integer id;

    /**
     * 举报对象类型（语料-CORPUS、字表-LIST、评论-COMMENT、用户-USER）
     */
    @Schema(description="举报对象类型（语料-CORPUS、字表-LIST、评论-COMMENT、用户-USER）")
    private Integer objectType;

    /**
     * 举报对象编号
     */
    @Schema(description="举报对象编号")
    private Integer objectId;

    /**
     * 举报原因
     */
    @Schema(description="举报原因")
    private String reason;

    /**
     * 举报人
     */
    @Schema(description="举报人")
    private Integer repoterId;

    /**
     * 举报状态（草稿-DRAFT、已提交-SUBMITTED、已处理-PROCESSED、已删除-DELETED）
     */
    @Schema(description="举报状态（草稿-DRAFT、已提交-SUBMITTED、已处理-PROCESSED、已删除-DELETED）")
    private String status;

    /**
     * 处理结果回复
     */
    @Schema(description="处理结果回复")
    private String resultReply;

    /**
     * 申请时间
     */
    @Schema(description="申请时间")
    private LocalDateTime applyTime;

    /**
     * 处理时间
     */
    @Schema(description="处理时间")
    private LocalDateTime processTime;
}
