package com.sleepywang.xiangyin.dialect.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.security.annotation.HasPermission;
import com.sleepywang.xiangyin.dialect.entity.GuangyunInitialEntity;
import com.sleepywang.xiangyin.dialect.entity.GuangyunRhymeEntity;
import com.sleepywang.xiangyin.dialect.entity.ListCharacterEntity;
import com.sleepywang.xiangyin.dialect.service.GuangyunInitialService;
import com.sleepywang.xiangyin.dialect.service.GuangyunRhymeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 广韵声韵母管理接口
 *
 * @author sleepywang
 * @date 2026-09-18 02:07:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/guangyun" )
@Tag(description = "guangyun" , name = "广韵声韵体系管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GuangyunController {
    private final GuangyunRhymeService guangyunRhymeService;
    private final GuangyunInitialService guangyunInitialService;

    /*
    * 声母的增删改查
    */
    @Operation(summary = "分页查询声母" , description = "分页查询声母" )
    @GetMapping("/initial/page")
//    @HasPermission({"dialect_guangyun","dialect_onomatopoeia"})
    public R getInitialPage(@ParameterObject Page page, @ParameterObject GuangyunInitialEntity guangyunInitial)
    {
        LambdaQueryWrapper<GuangyunInitialEntity> wrapper= Wrappers.lambdaQuery(guangyunInitial);
        return R.ok(guangyunInitialService.page(page,wrapper));
    }
    @Operation(summary = "通过条件查询声母" , description = "通过条件查询声母" )
    @GetMapping("/initial/details" )
//    @HasPermission({"dialect_guangyun","dialect_onomatopoeia"})
    public R getInitialDetails(@ParameterObject GuangyunInitialEntity guangyunInitial) {
        return R.ok(guangyunInitialService.list(Wrappers.query(guangyunInitial)));
    }
    @Operation(summary = "新增广韵声母",description = "新增广韵声母")
//    @HasPermission("dialect_guangyun")
    @PostMapping("/initial")
    public R saveInitial(@RequestBody GuangyunInitialEntity guangyunInitial)
    {
        return R.ok(guangyunInitialService.save(guangyunInitial));
    }
    @Operation(summary = "修改广韵声母",description = "修改广韵声母")
//    @HasPermission("dialect_guangyun")
    @PutMapping ("/initial")
    public R updateInitial(@RequestBody GuangyunInitialEntity guangyunInitial)
    {
        return R.ok(guangyunInitialService.updateById(guangyunInitial));
    }
    @Operation(summary = "删除广韵声母",description = "删除广韵声母")
//    @HasPermission("dialect_guangyun")
    @DeleteMapping ("/initial")
    public R deleteInitial(@RequestBody Integer[] guangyunInitialIds)
    {
        return R.ok(guangyunInitialService.removeBatchByIds(CollUtil.toList(guangyunInitialIds)));
    }

    /*
     * 韵母的增删改查
     */
    @Operation(summary = "分页查询韵母" , description = "分页查询韵母" )
    @GetMapping("/rhyme/page")
//    @HasPermission("dialect_guangyun")
    public R getRhymePage(@ParameterObject Page page, @ParameterObject GuangyunRhymeEntity guangyunRhyme)
    {
        LambdaQueryWrapper<GuangyunRhymeEntity> wrapper= Wrappers.lambdaQuery(guangyunRhyme);
        return R.ok(guangyunRhymeService.page(page,wrapper));
    }
    @Operation(summary = "通过条件查询韵母" , description = "通过条件查询韵母" )
    @GetMapping("/rhyme/details" )
//    @HasPermission("dialect_guangyun")
    public R getRhymeDetails(@ParameterObject GuangyunRhymeEntity guangyunRhyme) {
        return R.ok(guangyunRhymeService.list(Wrappers.query(guangyunRhyme)));
    }
    @Operation(summary = "新增广韵韵母",description = "新增广韵韵母")
//    @HasPermission("dialect_guangyun")
    @PostMapping("/rhyme")
    public R saveRhyme(@RequestBody GuangyunRhymeEntity guangyunRhyme)
    {
        return R.ok(guangyunRhymeService.save(guangyunRhyme));
    }
    @Operation(summary = "修改广韵韵母",description = "修改广韵韵母")
//    @HasPermission("dialect_guangyun")
    @PutMapping ("/rhyme")
    public R updateRhyme(@RequestBody GuangyunRhymeEntity guangyunRhyme)
    {
        return R.ok(guangyunRhymeService.updateById(guangyunRhyme));
    }
    @Operation(summary = "删除广韵韵母",description = "删除广韵韵母")
//    @HasPermission("dialect_guangyun")
    @DeleteMapping ("/rhyme")
    public R deleteRhyme(@RequestBody Integer[] guangyunRhymeIds)
    {
        return R.ok(guangyunRhymeService.removeBatchByIds(CollUtil.toList(guangyunRhymeIds)));
    }

    /*
     * 广韵声母的拟音方案修改
     */
    @Operation(summary = "更新声母拟音方案",description = "更新声母拟音方案")
//    @HasPermission("dialect_onomatopoeia")
    @PutMapping("/initial/onomatopoeia")
    public R updateInitialOnomatopoeia(@RequestBody GuangyunInitialEntity guangyunInitial)
    {
        GuangyunInitialEntity guangyunInitialOnomatopoeia=new GuangyunInitialEntity();
        guangyunInitialOnomatopoeia.setId(guangyunInitial.getId());
        guangyunInitialOnomatopoeia.setOnomatopoeia1(guangyunInitial.getOnomatopoeia1());
        guangyunInitialOnomatopoeia.setOnomatopoeia2(guangyunInitial.getOnomatopoeia2());
        guangyunInitialOnomatopoeia.setOnomatopoeia3(guangyunInitial.getOnomatopoeia3());
        return R.ok(guangyunInitialService.updateById(guangyunInitial));
    }

    /*
     * 广韵韵母的拟音方案修改
     */
    @Operation(summary = "更新韵母拟音方案",description = "更新韵母拟音方案")
//    @HasPermission("dialect_onomatopoeia")
    @PutMapping("/rhyme/onomatopoeia")
    public R updateRhymeOnomatopoeia(@RequestBody GuangyunRhymeEntity guangyunRhyme)
    {
        GuangyunRhymeEntity guangyunRhymeOnomatopoeia=new GuangyunRhymeEntity();
        guangyunRhymeOnomatopoeia.setId(guangyunRhyme.getId());
        guangyunRhymeOnomatopoeia.setOnomatopoeia1(guangyunRhyme.getOnomatopoeia1());
        guangyunRhymeOnomatopoeia.setOnomatopoeia2(guangyunRhyme.getOnomatopoeia2());
        guangyunRhymeOnomatopoeia.setOnomatopoeia3(guangyunRhyme.getOnomatopoeia3());
        return R.ok(guangyunRhymeService.updateById(guangyunRhyme));
    }
}
