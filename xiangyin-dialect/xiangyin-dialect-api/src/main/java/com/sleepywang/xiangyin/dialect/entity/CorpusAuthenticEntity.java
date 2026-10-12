package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 语料正宗记录（和点赞不同，正宗侧重的是收集的原始语料质量（即发音人的方言水平）；点赞侧重语料的标注处理质量和完善程度）
 *
 * @author pig
 * @date 2026-10-09 22:58:24
 */
@Data
@TableName("corpus_authentic")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "语料正宗记录（和点赞不同，正宗侧重的是收集的原始语料质量（即发音人的方言水平）；点赞侧重语料的标注处理质量和完善程度）")
public class CorpusAuthenticEntity extends Model<CorpusAuthenticEntity> {


    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="id")
    private Integer id;

    /**
     * 语料id
     */
    @Schema(description="语料id")
    private Integer corpusId;

    /**
     * 用户id
     */
    @Schema(description="用户id")
    private Long userId;

    /**
     * 点正宗的时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="点正宗的时间")
    private LocalDateTime createTime;
    /**
     * 正宗分数
     */
    @Schema(description="正宗分数")
    private Double score;
}
