package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 语料字
 *
 * @author sleepywang
 * @date 2026-09-18 01:03:58
 */
@Data
@TableName("corpus_character")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "语料字")
public class CorpusCharacterEntity extends Model<CorpusCharacterEntity> {


    /**
     * 单字编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="单字编号")
    private Integer id;

    /**
     * 所属语料
     */
    @Schema(description="所属语料")
    private Integer corpusId;

    /**
     * 字形
     */
    @Schema(description="字形")
    private String character;

    /**
     * 中古声母
     */
    @Schema(description="中古声母")
    private Integer guangyunInitialId;

    /**
     * 中古韵母
     */
    @Schema(description="中古韵母")
    private Integer guangyunRhymeId;

    /**
     * 中古声调（阴平、阳平、阴上、阳上、阴去、阳去、阴入、阳入）
     */
    @Schema(description="中古声调（阴平、阳平、阴上、阳上、阴去、阳去、阴入、阳入）")
    private String guangyunTone;

    /**
     * 含义
     */
    @Schema(description="含义")
    private String meaning;

    /**
     * IPA标注
     */
    @Schema(description="IPA标注")
    private String ipa;

    /**
     * 方言声母
     */
    @Schema(description="方言声母")
    private String dialectInitial;

    /**
     * 方言韵母
     */
    @Schema(description="方言韵母")
    private String dialectRhyme;

    /**
     * 方言声调
     */
    @Schema(description="方言声调")
    private String dialectTone;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;

    /**
     * 音频路径
     */
    @Schema(description="音频路径")
    private String audioPath;

    /**
     * 是否删除
     */
    @Schema(description="是否删除")
    private Integer isDeleted;
}
