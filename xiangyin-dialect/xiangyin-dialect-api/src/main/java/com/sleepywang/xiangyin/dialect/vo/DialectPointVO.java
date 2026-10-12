package com.sleepywang.xiangyin.dialect.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DialectPointVO {
    private Integer id;
    /**
     * 方言点名称
     */
    private String name;

    /**
     * 省（数据由GIS服务提供，本地不管理）
     */
    private String province;

    /**
     * 市（数据由GIS服务提供，本地不管理）
     */
    private String city;

    /**
     * 县（数据由GIS服务提供，本地不管理）
     */
    private String county;

    /**
     * 乡（数据由GIS服务提供，本地不管理）
     */
    private String town;

    /**
     * 居民点
     */
    private String address;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 方言点介绍
     */
    private String introduction;

    /**
     * 方言点音韵特征
     */
    private String phonologyExplanation;

    /**
     * 分区方案一方言类型
     */
    private DialectSortVO dialectSort1;

    /**
     * 分区方案二方言类型
     */
    private DialectSortVO dialectSort2;

    /**
     * 分区方案三方言类型
     */
    private DialectSortVO dialectSort3;

    /**
     * 分区方案四方言类型
     */
    private DialectSortVO dialectSort4;

    /**
     * 方言点管理员
     */
    private Long adminId;

    /**
     * 状态（未认领-UNCLAIMED、已发布-RELEASED、已删除-DELETED）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
