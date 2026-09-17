package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 语料短句
 *
 * @author sleepywang
 * @date 2026-09-18 01:27:25
 */
@Data
@TableName("corpus_sentence")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "语料短句")
public class CorpusSentenceEntity extends Model<CorpusSentenceEntity> {


    /**
     * 短句编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="短句编号")
    private Integer id;

    /**
     * 所属语料
     */
    @Schema(description="所属语料")
    private Integer corpusId;

    /**
     * 普通话句意
     */
    @Schema(description="普通话句意")
    private String standardForm;

    /**
     * 方言正字
     */
    @Schema(description="方言正字")
    private String dialectForm;

    /**
     * 音频路径
     */
    @Schema(description="音频路径")
    private String audioPath;

    /**
     * IPA标注
     */
    @Schema(description="IPA标注")
    private String ipa;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;

    /**
     * 是否删除
     */
    @Schema(description="是否删除")
    private Integer isDelected;
}
