package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 句表短句
 *
 * @author sleepywang
 * @date 2026-09-18 01:23:32
 */
@Data
@TableName("list_sentence")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "句表短句")
public class ListSentenceEntity extends Model<ListSentenceEntity> {


    /**
     * 短句编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="短句编号")
    private Integer id;

    /**
     * 所属句表
     */
    @Schema(description="所属句表")
    private Integer sentenceListId;

    /**
     * 普通话句意
     */
    @Schema(description="普通话句意")
    private String standardForm;

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
