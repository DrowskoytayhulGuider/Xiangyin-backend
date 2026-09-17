package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 收藏夹
 *
 * @author sleepywang
 * @date 2026-09-18 01:18:59
 */
@Data
@TableName("favourites")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "收藏夹")
public class FavouritesEntity extends Model<FavouritesEntity> {


    /**
     * 收藏夹编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="收藏夹编号")
    private Integer id;

    /**
     * 用户
     */
    @Schema(description="用户")
    private Integer userId;

    /**
     * 收藏夹名称
     */
    @Schema(description="收藏夹名称")
    private String name;

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

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
