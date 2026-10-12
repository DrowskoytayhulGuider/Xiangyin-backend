package com.sleepywang.xiangyin.dialect.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DialectPointSaveDTO {
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
    private Integer dialectSort1Id;
    /**
     * 分区方案二方言类型
     */
    private Integer dialectSort2Id;
    /**
     * 分区方案三方言类型
     */
    private Integer dialectSort3Id;
    /**
     * 分区方案四方言类型
     */
    private Integer dialectSort4Id;
    /**
     * 备注
     */
    private String remark;

}
