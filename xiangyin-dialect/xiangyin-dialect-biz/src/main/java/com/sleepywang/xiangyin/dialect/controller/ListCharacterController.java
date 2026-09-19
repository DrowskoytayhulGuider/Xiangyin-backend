package com.sleepywang.xiangyin.dialect.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.log.annotation.SysLog;
import com.sleepywang.xiangyin.common.excel.annotation.ResponseExcel;
import com.sleepywang.xiangyin.common.excel.annotation.RequestExcel;
import com.sleepywang.xiangyin.dialect.entity.ListCharacterEntity;
import com.sleepywang.xiangyin.dialect.service.ListCharacterService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.sleepywang.xiangyin.common.security.annotation.HasPermission;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 字表字
 *
 * @author sleepywang
 * @date 2026-09-18 02:07:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/listCharacter" )
@Tag(description = "listCharacter" , name = "字表字管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ListCharacterController {

    private final  ListCharacterService listCharacterService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param listCharacter 字表字
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @HasPermission("dialect_listcharacter_view")
    public R getListCharacterPage(@ParameterObject Page page, @ParameterObject ListCharacterEntity listCharacter) {
        LambdaQueryWrapper<ListCharacterEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(listCharacterService.page(page, wrapper));
    }


    /**
     * 通过条件查询字表字
     * @param listCharacter 查询条件
     * @return R  对象列表
     */
    @Operation(summary = "通过条件查询" , description = "通过条件查询对象" )
    @GetMapping("/details" )
    @HasPermission("dialect_listcharacter_view")
    public R getDetails(@ParameterObject ListCharacterEntity listCharacter) {
        return R.ok(listCharacterService.list(Wrappers.query(listCharacter)));
    }

    /**
     * 新增字表字
     * @param listCharacter 字表字
     * @return R
     */
    @Operation(summary = "新增字表字" , description = "新增字表字" )
    @SysLog("新增字表字" )
    @PostMapping
    @HasPermission("dialect_listcharacter_add")
    public R save(@RequestBody ListCharacterEntity listCharacter) {
        return R.ok(listCharacterService.save(listCharacter));
    }

    /**
     * 修改字表字
     * @param listCharacter 字表字
     * @return R
     */
    @Operation(summary = "修改字表字" , description = "修改字表字" )
    @SysLog("修改字表字" )
    @PutMapping
    @HasPermission("dialect_listcharacter_edit")
    public R updateById(@RequestBody ListCharacterEntity listCharacter) {
        return R.ok(listCharacterService.updateById(listCharacter));
    }

    /**
     * 通过id删除字表字
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除字表字" , description = "通过id删除字表字" )
    @SysLog("通过id删除字表字" )
    @DeleteMapping
    @HasPermission("dialect_listcharacter_del")
    public R removeById(@RequestBody Integer[] ids) {
        return R.ok(listCharacterService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param listCharacter 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("dialect_listcharacter_export")
    public List<ListCharacterEntity> exportExcel(ListCharacterEntity listCharacter,Integer[] ids) {
        return listCharacterService.list(Wrappers.lambdaQuery(listCharacter).in(ArrayUtil.isNotEmpty(ids), ListCharacterEntity::getId, ids));
    }

    /**
     * 导入excel 表
     * @param listCharacterList 对象实体列表
     * @param bindingResult 错误信息列表
     * @return ok fail
     */
    @PostMapping("/import")
    @HasPermission("dialect_listcharacter_export")
    public R importExcel(@RequestExcel List<ListCharacterEntity> listCharacterList, BindingResult bindingResult) {
        return R.ok(listCharacterService.saveBatch(listCharacterList));
    }
}
