package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 方言点
 *
 * @author sleepywang
 * @date 2026-09-18 01:29:30
 */
@Data
@TableName("dialect_point")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "方言点")
public class DialectPointEntity extends Model<DialectPointEntity> {


    /**
     * 方言点编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="方言点编号")
    private Integer id;

    /**
     * 方言点名称
     */
    @Schema(description="方言点名称")
    private String name;

    /**
     * 省（数据由GIS服务提供，本地不管理）
     */
    @Schema(description="省（数据由GIS服务提供，本地不管理）")
    private String province;

    /**
     * 市（数据由GIS服务提供，本地不管理）
     */
    @Schema(description="市（数据由GIS服务提供，本地不管理）")
    private String city;

    /**
     * 县（数据由GIS服务提供，本地不管理）
     */
    @Schema(description="县（数据由GIS服务提供，本地不管理）")
    private String county;

    /**
     * 乡（数据由GIS服务提供，本地不管理）
     */
    @Schema(description="乡（数据由GIS服务提供，本地不管理）")
    private String town;

    /**
     * 居民点
     */
    @Schema(description="居民点")
    private String address;

    /**
     * 经度
     */
    @Schema(description="经度")
    private Double lng;

    /**
     * 纬度
     */
    @Schema(description="纬度")
    private Double lat;

    /**
     * 方言点介绍
     */
    @Schema(description="方言点介绍")
    private String introduction;

    /**
     * 方言点音韵特征
     */
    @Schema(description="方言点音韵特征")
    private String phonologyExplanation;

    /**
     * 分区方案一方言类型
     */
    @Schema(description="分区方案一方言类型")
    private Integer dialectSort1Id;

    /**
     * 分区方案二方言类型
     */
    @Schema(description="分区方案二方言类型")
    private Integer dialectSort2Id;

    /**
     * 分区方案三方言类型
     */
    @Schema(description="分区方案三方言类型")
    private Integer dialectSort3Id;

    /**
     * 分区方案四方言类型
     */
    @Schema(description="分区方案四方言类型")
    private Integer dialectSort4Id;

    /**
     * 方言点管理员
     */
    @Schema(description="方言点管理员")
    private Integer adminId;

    /**
     * 状态（未认领-UNCLAIMED、已发布-RELEASED、已删除-DELETED）
     */
    @Schema(description="状态（未认领-UNCLAIMED、已发布-RELEASED、已删除-DELETED）")
    private String status;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;

    /**
     * 发布时间
     */
    @Schema(description="发布时间")
    private LocalDateTime releaseTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;
}
