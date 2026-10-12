package com.sleepywang.xiangyin.dialect.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DialectPointQueryDTO {
    /**
     * 这个接口用来分页返回所有的方言点，可以支持按方言点名称、行政区、
     * 分区方案下的方言分区、创建者、管理者、状态、创建时间、发布时间筛选。默认剔除已删除项
     */
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
     * 居民点 like
     */
    private String address;

    /**
     * 方言分区方案编号（必填）
     */
    private Integer plan;

    /**
     * 方言分类涉及的方言片
     */
    private List<Integer> dialectClassIds;
    /**
     * 是否筛选本管理员管理的方言点
     */
    private Boolean isAdmin;

    /**
     * 状态（未认领-UNCLAIMED、已发布-RELEASED、已删除-DELETED）
     */
    private String status;

    /**
     * 发布时间
     */
    private LocalDateTime releaseStartTime;
    private LocalDateTime releaseEndTime;
    /**
     * 创建时间
     */
    private LocalDateTime createStartTime;
    private LocalDateTime createEndTime;
}
