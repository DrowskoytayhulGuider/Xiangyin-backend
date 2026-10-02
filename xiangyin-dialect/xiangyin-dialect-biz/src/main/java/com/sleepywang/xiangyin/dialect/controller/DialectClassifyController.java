package com.sleepywang.xiangyin.dialect.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.security.annotation.HasPermission;
import com.sleepywang.xiangyin.dialect.dto.DialectSortQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectSortSaveDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectSortUpdateDTO;
import com.sleepywang.xiangyin.dialect.entity.DialectClassEntity;
import com.sleepywang.xiangyin.dialect.entity.DialectSortEntity;
import com.sleepywang.xiangyin.dialect.service.DialectClassService;
import com.sleepywang.xiangyin.dialect.service.DialectSortService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dialect-classify")
@RequiredArgsConstructor
@Tag(description = "dialect-classify" , name = "方言分类管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DialectClassifyController {

    private final DialectClassService dialectClassService;
    private final DialectSortService dialectSortService;
    /**
     * 分页查询方言片
     */
    @GetMapping("/dialect-class")
    @Operation(summary = "分页查询方言片",description = "分页查询方言片")
    public R getDialectClassPage(@ParameterObject Page page, @ParameterObject DialectClassEntity dialectClass)
    {
        LambdaQueryWrapper<DialectClassEntity> wrapper= Wrappers.<DialectClassEntity>lambdaQuery()
                .eq(DialectClassEntity::getIsDeleted,false)
                .eq(dialectClass.getPlan()!=null,DialectClassEntity::getPlan,dialectClass.getPlan())
                .eq(dialectClass.getLevel()!=null,DialectClassEntity::getLevel,dialectClass.getLevel())
                .like(!StrUtil.isBlank(dialectClass.getName()),DialectClassEntity::getName,dialectClass.getName());
        return R.ok(dialectClassService.page(page,wrapper));
    }

    /**
     * 条件查询方言片
     */
    @GetMapping("/dialect-class/details")
    @Operation(summary = "条件查询方言片",description = "条件查询方言片")
    public R getDialectClassDetails(@ParameterObject DialectClassEntity dialectClass)
    {
        dialectClass.setIsDeleted(false);
        return R.ok(dialectClassService.list(Wrappers.query(dialectClass)));
    }

    /**
     * 新增方言片
     */
    @PostMapping("/dialect-class")
    @Operation(summary = "新增方言片",description = "新增方言片")
//    @HasPermission({"dialect-dialect-classify"})
    public R saveDialectClass(@RequestBody DialectClassEntity dialectClass)
    {
        return R.ok(dialectClassService.save(dialectClass));
    }

    /**
     * 修改方言片
     */
    @PutMapping("/dialect-class")
    @Operation(summary = "修改方言片",description = "修改方言片")
//    @HasPermission({"dialect-dialect-classify"})
    public R updateDialectClass(@RequestBody DialectClassEntity dialectClass)
    {
        return R.ok(dialectClassService.updateById(dialectClass));
    }

    /**
     * 物理删除方言片
     */
    @DeleteMapping("/dialect-class")
    @Operation(summary = "物理删除方言片",description = "物理删除方言片")
//    @HasPermission({"dialect-dialect-classify"})
    public R deleteDialectClass(@RequestBody Integer[] dialectClassIds)
    {
//        return R.ok(dialectClassService.removeBatchByIds(CollUtil.toList(dialectClassIds)));
        return dialectClassService.deleteDialectClass(dialectClassIds);
    }

    /**
     * 分页查询方言分类
     */
    @GetMapping("/dialect-sort")
    @Operation(summary = "分页查询方言分类",description = "分页查询方言分类")

    public R getDialectSortPage(@ParameterObject Page page, @ParameterObject DialectSortQueryDTO dialectSortQueryDTO)
    {
        return R.ok(dialectSortService.getDialectSortPage(page,dialectSortQueryDTO));
    }
    /**
     * 条件查询方言分类
     */
    @GetMapping("/dialect-sort/details")
    @Operation(summary = "条件查询方言分类",description = "条件查询方言分类")
    public R getDialectSortDetails(@ParameterObject DialectSortEntity dialectSortEntity)
    {
        dialectSortEntity.setIsDeleted(false);
        return R.ok(dialectSortService.list(Wrappers.query(dialectSortEntity)));
    }

    /**
     * 新增方言分类
     */
    @PostMapping("/dialect-sort")
    @Operation(summary = "新增方言分类",description = "新增方言分类")
//    @HasPermission({"dialect-dialect-classify"})
    public R saveDialectSort(@RequestBody DialectSortSaveDTO dialectSortSaveDTO)
    {
        return dialectSortService.saveDialectSort(dialectSortSaveDTO);
    }

    /**
     * 修改方言分类
     */
    @PutMapping("/dialect-sort")
    @Operation(summary = "修改方言分类",description = "修改方言分类")
//    @HasPermission({"dialect-dialect-classify"})
    public R updateDialectSort(@RequestBody DialectSortUpdateDTO dialectSortUpdateDTO)
    {
        return dialectSortService.updateDialectSort(dialectSortUpdateDTO);
    }

    /**
     * 逻辑删除方言分类
     */
    @DeleteMapping("/dialect-sort")
    @Operation(summary = "逻辑删除方言分类",description = "逻辑删除方言分类")
//    @HasPermission({"dialect-dialect-classify"})
    public R deleteDialectSort(@RequestBody Integer[] ids)
    {
        return dialectSortService.deleteDialectSortLogically(ids);
    }

    //TODO: 筛选上级方言片的所有下级方言片（id+名称+颜色）、筛选所有与已选择方言片(s)关联的方言分类（id+基和过渡方言名称+颜色）。根据用户选出的基方言片列表和过渡方言列表列举方言分类（id+基和过渡方言名称+颜色）？这是啥功能，搞忘了
    /**
     * 筛选上级方言片的所有下级方言片
     */
    @GetMapping("/dialect-class/{id}")
    @Operation(summary = "筛选上级方言片的所有下级方言片",description = "筛选上级方言片的所有下级方言片")
    public R selectNextDialectClass(@PathVariable("id") Integer id)
    {
        return R.ok(dialectClassService.selectNextDialectClass(id));
    }
    /**
     * 筛选所有与已选择方言片(s)关联的方言分类（id+基和过渡方言名称+颜色）-基和其有关、过渡也要和其有关
     */
    @GetMapping("/dialect-sort/by-dialect-class")
    @Operation(summary = "筛选所有与已选择方言片(s)关联的方言分类（id+基和过渡方言名称+颜色）-基和其有关、过渡也要和其有关",description = "筛选所有与已选择方言片(s)关联的方言分类（id+基和过渡方言名称+颜色）-基和其有关、过渡也要和其有关")
    public R selectDialectSortsByDialectClasses(@RequestBody DialectClassEntity[] dialectClassEntities)//到了服务层，先根据id去重
    {
        return R.ok(dialectSortService.selectDialectSortsByDialectClasses(dialectClassEntities));
    }
}
