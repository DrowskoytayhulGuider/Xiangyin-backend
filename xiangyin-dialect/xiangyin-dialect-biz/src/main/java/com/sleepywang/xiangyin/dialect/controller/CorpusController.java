package com.sleepywang.xiangyin.dialect.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.dialect.dto.CorpusQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.CorpusSaveDTO;
import com.sleepywang.xiangyin.dialect.entity.CorpusEntity;
import com.sleepywang.xiangyin.dialect.service.CorpusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/corpus")
@Tag(description = "corpus" , name = "语料管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class CorpusController {
    //我们修改语料的时候只能修改语料的基本信息，绝不能修改：语料编号、提交人、创建时间、点赞数、正宗数、状态
    //状态的修改应该提供专门的接口，并严格按状态自动机变更状态
    //涉及状态修改的操作包括：用户提交、用户删除、管理员审核（发布、打回）等
    //状态（草稿-DRAFT、待审核-PENDING、已发布-RELEASED、已打回-REJECTED、已删除-DELETED）。
    // 状态机说明：用户创建了语料，则设置为草稿，此时只有用户处可见，可编辑。
    // 用户点击发布，变为待审核，此时用户和管理员可见，用户不可编辑，管理员可编辑。
    // 管理员选择审核通过，变为已发布，此时所有用户和管理员可见，都不可编辑。
    // 管理员选择打回，变为已打回，此时用户和管理员都可见，用户可编辑，管理员不可编辑。
    // 用户再次发布，又转换为待审核。
    // 管理员对已发布的语料选择打回，又变为已打回（删除缓存）。
    // 管理员对已打回的语料选择审核通过，则变为已发布。
    // 用户自己删除语料，变为已删除，所有人不可见不可编辑。（删除缓存）
    // 已发布的语料用户可以修改为草稿用以修改，然后重新提交变为待审核。
    // 删除语料下辖字表字、词表词、句表句和会话也会设置为删除。这些下属表项的可见性跟随语料。
    // 语料可按照：语料编号、所属方言点、语料名称、所属派别、发音人年龄（区间）、发音人性别、
    // 发音人文化程度（多选）、发音人职业（like）、提交人、
    // 创建时间（区间）、状态，分页及列表筛选，并按正宗数排序，可以选择按点赞排序。（后期加入置顶功能，置顶内部单独排序，始终在最上面）
    // 筛选同样先剔除已删除的

    //为实现用户将已发布的语料变为草稿并修改后重新审核时其他用户访问语料不间断，需要使用redis缓存系统。
    //审核通过发布的语料应同步到redis中，面向用户的查看、筛选页面的应从redis中读取已发布的版本。面向管理员的查看、筛选页面应反映语料的真实状态，
    //故仍从数据库读取
    private final CorpusService corpusService;
    /**
     * 分页查询语料
     */
    @GetMapping
    @Operation(summary = "分页查询语料",description = "分页查询语料")
    //TODO 加权限
    public R getCorpusPage(@ParameterObject Page page, @ParameterObject CorpusQueryDTO query)
    {
        return R.ok(corpusService.getCorpusVOsPage(page,query));
    }
    /**
     * 列表查询语料
     */
    @GetMapping("/details")
    @Operation(summary = "列表查询语料",description = "列表查询语料")
    //TODO 加权限
    public R getCorpusDetails(@ParameterObject CorpusQueryDTO query)
    {
        return R.ok(corpusService.getCorpusVOs(query));
    }
    /**
     * 新增语料
     */
    @PostMapping
    @Operation(summary = "新增语料",description = "新增语料")
    //TODO 加权限
    public R addCorpus(@RequestBody CorpusSaveDTO corpusSave)
    {
        return corpusService.addCorpus(corpusSave);
    }
    /**
     * 修改语料基本信息
     */
    @PutMapping
    @Operation(summary = "修改语料基本信息",description = "修改语料基本信息，记得要改数据库，并将数据库中语料状态改为草稿")
    //TODO 加权限
    public R updateCorpus(@RequestBody CorpusEntity corpus)
    {
        //我们修改语料的时候只能修改语料的基本信息，绝不能修改：语料编号、提交人、创建时间、点赞数、正宗数、状态
        //状态的修改应该提供专门的接口，并严格按状态自动机变更状态
        //不用管缓存，因为已发布的语料不可编辑，必须将其打回或者撤回之后才可编辑。
        return corpusService.updateCorpus(corpus);
    }
    /**
     * 逻辑删除语料。不用校验是否被引用，只询问是否删除（前端询问），语料下的字、词、句、会话一并直接标记为isDelete=true
     */
    @DeleteMapping
    @Operation(summary = "逻辑删除语料",description = "逻辑删除语料")
    //TODO 加权限
    public R deleteCorpus(@RequestBody Integer[] ids)
    {
        return corpusService.deleteCorpus(ids);
    }
    /**
     * 用户发布
     */
    @PutMapping("/release/{id}")
    @Operation(summary = "用户发布",description = "用户发布：不是真的发布，是用户将状态设置为待审核，并提交到管理员处待审")
    //TODO 加权限
    public R releaseCorpus(@PathVariable Integer id)
    {
        return corpusService.releaseCorpus(id);
    }
    /**
     * 管理员审核，支持发布和从发布状态和从PENDING状态打回
     */
    @PutMapping("/review/{id}/{isPassed}")
    //TODO 加权限
    @Operation(summary = "管理员审核",description = "管理员审核，支持发布和从发布状态和从PENDING状态打回")
    public R reviewCorpus(@PathVariable Integer id,@PathVariable Boolean isPassed)
    {
        return corpusService.reviewCorpus(id,isPassed);
    }
    /**
     * 用户将数据库语料状态设置为草稿
     */
    @PutMapping("/withdraw/{id}")
    //TODO 加权限
    @Operation(summary = "用户将数据库语料状态设置为草稿",description = "用户将已发布的数据库语料状态设置为草稿")
    public R withdrawCorpus(@PathVariable Integer id)
    {
        return corpusService.withdrawCorpus(id);
    }
    /**
     * 点赞
     */
    @PutMapping("/like/{id}")
    @Operation(summary = "点赞",description = "点赞")
    public R likeCorpus(@PathVariable Integer id)
    {
        return corpusService.likeCorpus(id);
    }
    /**
     * 取消点赞
     */
    @PutMapping("/unlike/{id}")
    @Operation(summary = "取消点赞",description = "取消点赞")
    public R unlikeCorpus(@PathVariable Integer id)
    {
        return corpusService.unlikeCorpus(id);
    }
    /**
     * 打正宗分数
     */
    @PutMapping("/authentic/{id}")
    @Operation(summary = "打正宗分数",description = "打正宗分数")
    public R unlikeCorpus(@PathVariable Integer id,Double score)
    {
        return corpusService.scoreCorpus(id,score);
    }
}
