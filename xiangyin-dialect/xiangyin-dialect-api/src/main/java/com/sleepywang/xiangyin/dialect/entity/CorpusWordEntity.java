package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 语料词
 *
 * @author sleepywang
 * @date 2026-09-18 01:27:59
 */
@Data
@TableName("corpus_word")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "语料词")
public class CorpusWordEntity extends Model<CorpusWordEntity> {

    //TODO 在数据库和实体中加一个关键字字段，并补齐按关键字筛选的功能
    // 这个字段用来给采集者标记一些发音人的特殊语言现象（比如儿化、子缀、连读变调标记（阳平+阴平）等）。选一两个标记做典型就行了
    // 前端以标签的形式显示
    /**
     * 词汇编号
     */
    @TableId(type = IdType.AUTO)
    @Schema(description="词汇编号")
    private Integer id;

    /**
     * 所属语料
     */
    @Schema(description="所属语料")
    private Integer corpusId;

    /**
     * 普通话标准词形
     */
    @Schema(description="普通话标准词形")
    private String standardForm;

    /**
     * 词性
     */
    @Schema(description="词性")
    private String speechPart;

    /**
     * 含义
     */
    @Schema(description="含义")
    private String meaning;

    /**
     * 方言正字
     */
    @Schema(description="方言正字")
    private String dialectForm;

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
