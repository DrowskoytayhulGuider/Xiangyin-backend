package com.sleepywang.xiangyin.dialect.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.security.util.SecurityUtils;
import com.sleepywang.xiangyin.dialect.dto.DialectPointQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectPointSaveDTO;
import com.sleepywang.xiangyin.dialect.entity.DialectPointEntity;
import com.sleepywang.xiangyin.dialect.service.DialectPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dialect-point")
@Tag(description = "dialect-point" , name = "方言点管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DialectPointController {
    private final DialectPointService dialectPointService;
    /**
     * 这个接口用来分页返回所有的方言点，可以支持按方言点名称、行政区、
     * 分区方案下的方言分区、创建者、管理者、状态、创建时间、发布时间筛选。默认剔除已删除项
     */
    @GetMapping
    @Operation(summary = "分页返回所有的方言点" , description = "这个接口用来分页返回所有的方言点，可以支持按方言点名称、行政区、分区方案下的方言分区、创建者、管理者、状态、创建时间、发布时间筛选。默认剔除已删除项" )
    //TODO 添加权限
    public R getDialectPointPage(@ParameterObject Page page, @ParameterObject DialectPointQueryDTO dialectPointQuery)
    {
        return R.ok(dialectPointService.selectDialectPointsPage(page,dialectPointQuery));
    }
    /**
     * 这个接口用来返回所有的方言点，可以支持按方言点名称、行政区、
     * 分区方案下的方言分区、创建者、管理者、状态、创建时间、发布时间筛选。默认剔除已删除项
     */
    @GetMapping("/details")
    @Operation(summary = "返回所有的方言点列表" , description = "这个接口用来返回所有的方言点，可以支持按方言点名称、行政区、分区方案下的方言分区、创建者、管理者、状态、创建时间、发布时间筛选。默认剔除已删除项" )
    //TODO 添加权限
    public R getDialectPointDetails( @ParameterObject DialectPointQueryDTO dialectPointQuery)
    {
        return R.ok(dialectPointService.selectDialectPoints(dialectPointQuery));
    }

    /**
     * 方言点的添加
     */
    @PostMapping("")
    @Operation(summary = "方言点的添加" , description = "方言点的添加")
    //TODO 添加权限
    public R addDialectPoint(@RequestBody DialectPointSaveDTO dialectPointSave)
    {
        return dialectPointService.saveDialectPoint(dialectPointSave);
    }
    /**
     * 方言点的修改：管理员才可以修改
     */
    @PutMapping("")
    @Operation(summary = "方言点的修改" , description = "方言点的修改")
    //TODO 添加权限
    public R updateDialectPoint(@RequestBody DialectPointEntity dialectPoint)
    {
//        dialectPoint.setId(null);
//        dialectPoint.setProvince(null);
//        dialectPoint.setCity(null);
//        dialectPoint.setCounty(null);
//        dialectPoint.setTown(null);
//        dialectPoint.setAddress(null);
//        dialectPoint.setLng(null);
//        dialectPoint.setLat(null);
        dialectPoint.setCreateTime(null);
        dialectPoint.setReleaseTime(null);
        if("DELETED".equals(dialectPoint.getStatus()))
            dialectPoint.setStatus(null);
        //允许更改管理员id，因为允许方言点转让
        //当方言点的状态要设置为已发布，其实就是管理员认领这个方言点，当然要更新发布时间和管理员id了
        if("RELEASED".equals(dialectPoint.getStatus())) {
            dialectPoint.setAdminId(SecurityUtils.getUser().getId());
            dialectPoint.setReleaseTime(LocalDateTime.now());
        }
        return R.ok(dialectPointService.updateById(dialectPoint));
    }
    /**
     * 方言点的删除：用户不可删除，管理员才可以。
     * 先校验有没有语料关联方言点id in ids且status不为DELETED，
     * 没有才能将选中的ids对应的status字段置为DELETED；有则阻断操作并提示。
     * 换言之，管理员只能删除空的方言点
     */
    @DeleteMapping("")
    @Operation(summary = "方言点的删除" , description = "用户不可删除，管理员才可以。先校验有没有语料关联方言点id in ids且status不为DELETED，没有才能将选中的ids对应的status字段置为DELETED；有则阻断操作并提示。换言之，管理员只能删除空的方言点")
    //TODO 添加权限
    public R deleteDialectPoint(@RequestBody Integer[] ids)
    {
        return dialectPointService.deleteDialectPoint(ids);
    }
}
