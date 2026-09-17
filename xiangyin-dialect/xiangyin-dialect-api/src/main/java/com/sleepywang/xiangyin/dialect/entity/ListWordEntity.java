package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 词表词
 *
 * @author sleepywang
 * @date 2026-09-18 01:24:09
 */
@Data
@TableName("list_word")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "词表词")
public class ListWordEntity extends Model<ListWordEntity> {


    /**
     * 词汇编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="词汇编号")
    private Integer id;

    /**
     * 所属词表
     */
    @Schema(description="所属词表")
    private Integer wordListId;

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
     * 备注
     */
    @Schema(description="备注")
    private String remark;

    /**
     * 是否删除
     */
    @Schema(description="是否删除")
    private Integer isDeleted;
}
