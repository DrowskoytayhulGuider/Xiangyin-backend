package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 对象-收藏夹关联
 *
 * @author sleepywang
 * @date 2026-09-18 01:17:32
 */
@Data
@TableName("object_favourites")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "对象-收藏夹关联")
public class ObjectFavouritesEntity extends Model<ObjectFavouritesEntity> {


    /**
     * 对象类型
     */
    @Schema(description="对象类型")
    private String objectType;

    /**
     * 对象编号
     */
    @Schema(description="对象编号")
    private Integer objectId;

    /**
     * 收藏夹编号
     */
    @Schema(description="收藏夹编号")
    private Integer favouritesId;

    /**
     * 添加时间
     */
    @Schema(description="添加时间")
    private LocalDateTime addinTime;

    /**
     * 是否删除
     */
    @Schema(description="是否删除")
    private Integer isDeleted;
}
