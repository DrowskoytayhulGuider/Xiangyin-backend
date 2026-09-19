package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字表字
 *
 * @author sleepywang
 * @date 2026-09-18 02:07:49
 */
@Data
@TableName("list_character")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字表字")
public class ListCharacterEntity extends Model<ListCharacterEntity> {


	/**
	* 单字编号
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="单字编号")
    private Integer id;

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
	* 中古声调
	*/
    @Schema(description="中古声调")
    private String guangyunTone;

	/**
	* 字义
	*/
    @Schema(description="字义")
    private String meaning;

	/**
	* 中古韵母
	*/
    @Schema(description="中古韵母")
    private Integer guangyunRhymeId;

	/**
	* 所属字表
	*/
    @Schema(description="所属字表")
    private Integer characterListId;

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
