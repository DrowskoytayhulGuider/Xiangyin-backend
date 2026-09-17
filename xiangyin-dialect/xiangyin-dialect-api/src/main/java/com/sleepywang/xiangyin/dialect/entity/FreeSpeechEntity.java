package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自由会话
 *
 * @author sleepywang
 * @date 2026-09-18 01:26:44
 */
@Data
@TableName("free_speech")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "自由会话")
public class FreeSpeechEntity extends Model<FreeSpeechEntity> {


    /**
     * 会话编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="会话编号")
    private Integer id;

    /**
     * 所属语料
     */
    @Schema(description="所属语料")
    private Integer corpusId;

    /**
     * 会话主题
     */
    @Schema(description="会话主题")
    private String speechTheme;

    /**
     * 会话大意（可以每一句都标注）
     */
    @Schema(description="会话大意（可以每一句都标注）")
    private String speechMeaning;

    /**
     * 音频路径
     */
    @Schema(description="音频路径")
    private String audioPath;

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
