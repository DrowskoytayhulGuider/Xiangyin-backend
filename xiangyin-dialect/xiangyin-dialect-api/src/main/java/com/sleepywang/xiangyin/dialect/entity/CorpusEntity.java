package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 语料
 *
 * @author sleepywang
 * @date 2026-09-18 01:28:41
 */
@Data
@TableName("corpus")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "语料")
public class CorpusEntity extends Model<CorpusEntity> {


    /**
     * 语料编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="语料编号")
    private Integer id;

    /**
     * 所属方言点
     */
    @Schema(description="所属方言点")
    private Integer dialectPointId;

    /**
     * 语料名称
     */
    @Schema(description="语料名称")
    private String name;

    /**
     * 所属派别（新派-NEW、中派-MEDIUM、老派-OLD）
     */
    @Schema(description="所属派别（新派-NEW、中派-MEDIUM、老派-OLD）")
    private String faction;

    /**
     * 所受影响普化(POPULARIZATION)、其他方言影响(AFFECTED_BY_OTHER_DIALECT)）
     */
    @Schema(description="所受影响普化(POPULARIZATION)、其他方言影响(AFFECTED_BY_OTHER_DIALECT)）")
    private String affection;

    /**
     * 发音人年龄
     */
    @Schema(description="发音人年龄")
    private Integer age;

    /**
     * 发音人性别
     */
    @Schema(description="发音人性别")
    private String sex;

    /**
     * 发音人文化程度
     */
    @Schema(description="发音人文化程度")
    private String educationLevel;

    /**
     * 发音人是否长期居于外地
     */
    @Schema(description="发音人是否长期居于外地")
    private Integer liveOutside;

    /**
     * 发音人常住地
     */
    @Schema(description="发音人常住地")
    private String permanentPlace;

    /**
     * 发音人职业
     */
    @Schema(description="发音人职业")
    private String vocation;

    /**
     * 发音人籍贯
     */
    @Schema(description="发音人籍贯")
    private String originalPlace;

    /**
     * 提交人
     */
    @Schema(description="提交人")
    private Integer submitterId;

    /**
     * 语料说明
     */
    @Schema(description="语料说明")
    private String introduction;

    /**
     * 音系说明
     */
    @Schema(description="音系说明")
    private String phonologyExplanation;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;

    /**
     * 点赞数
     */
    @Schema(description="点赞数")
    private Integer likeNumber;

    /**
     * 正宗数
     */
    @Schema(description="正宗数")
    private Integer authenticNumber;

    /**
     * 状态（草稿-DRAFT、已发布-RELEASED、已删除-DELETED）
     */
    @Schema(description="状态（草稿-DRAFT、已发布-RELEASED、已删除-DELETED）")
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
