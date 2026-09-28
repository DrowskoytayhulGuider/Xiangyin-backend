package com.sleepywang.xiangyin.dialect.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.security.annotation.HasPermission;
import com.sleepywang.xiangyin.dialect.dto.ListCharacterQueryDTO;
import com.sleepywang.xiangyin.dialect.entity.ListCharacterEntity;
import com.sleepywang.xiangyin.dialect.entity.ListSentenceEntity;
import com.sleepywang.xiangyin.dialect.entity.ListWordEntity;
import com.sleepywang.xiangyin.dialect.service.ListCharacterService;
import com.sleepywang.xiangyin.dialect.service.ListSentenceService;
import com.sleepywang.xiangyin.dialect.service.ListWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/base-list")
@Tag(description = "base_list" , name = "基础字、词、句表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
/**
 * 基础字/词/句表管理接口
 *
 * @author sleepywang
 * @date 2026-09-21 00:51:00
 */
public class BaseListController {

    private final ListCharacterService listCharacterService;
    private final ListWordService listWordService;
    private final ListSentenceService listSentenceService;

    /**
     * 分页查出基础字表中的所有字
     */
    //TODO 字表最后向前端应该返回VO，包括声母信息和韵母信息，而不是只有一个id
    @GetMapping("/character")
    @Operation(summary = "分页查出基础字表中的所有字",description = "分页查出基础字表中的所有字")
    public R getBaseCharacterPage(@ParameterObject Page page, @ParameterObject ListCharacterQueryDTO listCharacterQueryDTO)
    {
//        LambdaQueryWrapper<ListCharacterEntity> wrapper= new LambdaQueryWrapper<>();
//        wrapper.eq(ListCharacterEntity::getCharacterListId,1)
//                .eq(ListCharacterEntity::getIsDeleted,false);
//        return R.ok(listCharacterService.page(page,wrapper));
        listCharacterQueryDTO.setCharacterListId(1);
        return R.ok(listCharacterService.getListCharacterPage(page,listCharacterQueryDTO));
    }

//    /**
//     * 条件查询基础字表中的字
//     */
//    @GetMapping("/character/detials")
//    @Operation(summary = "条件查询基础字表中的字",description = "条件查询基础字表中的字")
//    public R getCharacterDetails(@ParameterObject ListCharacterEntity listCharacter)
//    {
//        listCharacter.setCharacterListId(1);
//        listCharacter.setIsDeleted(false);
//        return R.ok(listCharacterService.list(Wrappers.query(listCharacter)));
//    }

    /**
     * 增加基础字表中的字
     */
    @PostMapping("/character")
    @Operation(summary = "增加基础字表中的字",description = "增加基础字表中的字")
//    @HasPermission({"dialect_base_list"})
    public R saveBaseListCharacter(@RequestBody ListCharacterEntity listCharacter)
    {
        listCharacter.setCharacterListId(1);
        listCharacter.setIsDeleted(false);
        return R.ok(listCharacterService.save(listCharacter));
    }

    /**
     * 修改基础字表中的字
     */
    @PutMapping("/character")
    @Operation(summary = "修改基础字表中的字",description = "修改基础字表中的字")
//    @HasPermission({"dialect_base_list"})
    public R updateBaseListCharacter(@RequestBody ListCharacterEntity listCharacter)
    {
        listCharacter.setCharacterListId(1);
        listCharacter.setIsDeleted(false);
        return R.ok(listCharacterService.updateById(listCharacter));
    }

    /**
     * 物理删除基础字表中的字
     */
    @DeleteMapping("/character")
    @Operation(summary = "物理删除基础字表中的字",description = "物理删除基础字表中的字")
//    @HasPermission({"dialect_base_list"})
    public R deleteBaseListCharacter(@RequestBody Integer[]listCharacterIds)
    {
        return R.ok(listCharacterService.removeBatchByIds(CollUtil.toList(listCharacterIds)));
    }

    //TODO: 补充基础词表管理接口

    /**
     * 分页查出基础词表中的所有词
     */
    @GetMapping("/word")
    @Operation(summary = "分页查出基础词表中的所有词",description = "分页查出基础词表中的所有词")
    public R getBaseWordPage(@ParameterObject Page page, @ParameterObject ListWordEntity listWord)
    {
//        LambdaQueryWrapper<ListWordEntity> wrapper= new LambdaQueryWrapper<>();
//        wrapper.eq(ListWordEntity::getWordListId,1)
//                .eq(ListWordEntity::getIsDeleted,false);
        listWord.setWordListId(1);
        return R.ok(listWordService.getListWordPage(page,listWord));
    }

//    /**
//     * 条件查询基础词表中的词
//     */
//    @GetMapping("/word/detials")
//    @Operation(summary = "条件查询基础词表中的词",description = "条件查询基础词表中的词")
//    public R getWordDetails(@ParameterObject ListWordEntity listWord)
//    {
//        listWord.setWordListId(1);
//        listWord.setIsDeleted(false);
//        return R.ok(listWordService.list(Wrappers.query(listWord)));
//    }

    /**
     * 增加基础词表中的词
     */
    @PostMapping("/word")
    @Operation(summary = "增加基础词表中的词",description = "增加基础词表中的词")
//    @HasPermission({"dialect_base_list"})
    public R saveBaseListWord(@RequestBody ListWordEntity listWord)
    {
        listWord.setWordListId(1);
        listWord.setIsDeleted(false);
        return R.ok(listWordService.save(listWord));
    }

    /**
     * 修改基础词表中的词
     */
    @PutMapping("/word")
    @Operation(summary = "修改基础词表中的词",description = "修改基础词表中的词")
//    @HasPermission({"dialect_base_list"})
    public R updateBaseListWord(@RequestBody ListWordEntity listWord)
    {
        listWord.setWordListId(1);
        listWord.setIsDeleted(false);
        return R.ok(listWordService.updateById(listWord));
    }

    /**
     * 物理删除基础词表中的词
     */
    @DeleteMapping("/word")
    @Operation(summary = "物理删除基础词表中的词",description = "物理删除基础词表中的词")
//    @HasPermission({"dialect_base_list"})
    public R deleteBaseListWord(@RequestBody Integer[]listWordIds)
    {
        return R.ok(listWordService.removeBatchByIds(CollUtil.toList(listWordIds)));
    }

    //TODO: 补充基础句表的管理接口

    /**
     * 分页查出基础句表中的所有句
     */
    @GetMapping("/sentence")
    @Operation(summary = "分页查出基础句表中的所有句",description = "分页查出基础句表中的所有句")
    public R getBaseSentencePage(@ParameterObject Page page, @ParameterObject ListSentenceEntity listSentence)
    {
//        LambdaQueryWrapper<ListSentenceEntity> wrapper= new LambdaQueryWrapper<>();
//        wrapper.eq(ListSentenceEntity::getSentenceListId,1);
        listSentence.setSentenceListId(1);
        return R.ok(listSentenceService.getListSentencePage(page,listSentence));
    }

//    /**
//     * 条件查询基础句表中的句
//     */
//    @GetMapping("/sentence/detials")
//    @Operation(summary = "条件查询基础句表中的句",description = "条件查询基础句表中的句")
//    public R getSentenceDetails(@ParameterObject ListSentenceEntity listSentence)
//    {
//        listSentence.setSentenceListId(1);
//        return R.ok(listSentenceService.list(Wrappers.query(listSentence)));
//    }

    /**
     * 增加基础句表中的句
     */
    @PostMapping("/sentence")
    @Operation(summary = "增加基础句表中的句",description = "增加基础句表中的句")
//    @HasPermission({"dialect_base_list"})
    public R saveBaseSentenceWord(@RequestBody ListSentenceEntity listSentence)
    {
        listSentence.setSentenceListId(1);
        listSentence.setIsDeleted(false);
        return R.ok(listSentenceService.save(listSentence));
    }

    /**
     * 修改基础句表中的句
     */
    @PutMapping("/sentence")
    @Operation(summary = "修改基础句表中的句",description = "修改基础句表中的句")
//    @HasPermission({"dialect_base_list"})
    public R updateBaseSentenceWord(@RequestBody ListSentenceEntity listSentence)
    {
        listSentence.setSentenceListId(1);
        listSentence.setIsDeleted(false);
        return R.ok(listSentenceService.updateById(listSentence));
    }

    /**
     * 物理删除基础句表中的句
     */
    @DeleteMapping("/sentence")
    @Operation(summary = "物理删除基础句表中的句",description = "物理删除基础句表中的句")
//    @HasPermission({"dialect_base_list"})
    public R deleteBaseSentenceWord(@RequestBody Integer[]listSentenceIds)
    {
        return R.ok(listSentenceService.removeBatchByIds(CollUtil.toList(listSentenceIds)));
    }
}
