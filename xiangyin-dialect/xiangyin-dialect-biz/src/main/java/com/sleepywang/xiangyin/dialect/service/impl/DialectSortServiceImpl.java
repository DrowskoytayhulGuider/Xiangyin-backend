package com.sleepywang.xiangyin.dialect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
//import com.alibaba.nacos.api.naming.pojo.healthcheck.impl.Http;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.security.service.PigUser;
import com.sleepywang.xiangyin.common.security.util.SecurityUtils;
import com.sleepywang.xiangyin.dialect.dto.DialectSortQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectSortSaveDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectSortUpdateDTO;
import com.sleepywang.xiangyin.dialect.entity.*;
import com.sleepywang.xiangyin.dialect.mapper.*;
import com.sleepywang.xiangyin.dialect.service.*;
import com.sleepywang.xiangyin.dialect.vo.DialectSortVO;
import com.sleepywang.xiangyin.dialect.vo.TransitionDialectVO;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 方言分类
 *
 * @author sleepywang
 * @date 2026-09-19 19:39:54
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DialectSortServiceImpl extends ServiceImpl<DialectSortMapper, DialectSortEntity> implements DialectSortService {

    private final DialectClassMapper dialectClassMapper;
    private final CharacterListMapper characterListMapper;
    private final WordListMapper wordListMapper;
    private final SentenceListMapper sentenceListMapper;
    private final DialectPointMapper dialectPointMapper;
    public IPage<DialectSortVO> getDialectSortPage(Page page, DialectSortQueryDTO query)
    {
        //TODO 试试用like优化一下逻辑
        MPJQueryWrapper<DialectSortEntity> wrapper=new MPJQueryWrapper<>(DialectSortEntity.class);
        wrapper.select("t.id", "t.plan", "t.sort_explanation", "t.create_time",
                        "t.colours", "t.transition_ids", "t.creator_id","t.top_id",
                        "t.second_id","t.third_id","t.fourth_id","t.fifth_id")
                .leftJoin("dialect_class dc1 on dc1.id=t.top_id and dc1.is_deleted=0")
                .leftJoin("dialect_class dc2 on dc2.id=t.second_id and dc2.is_deleted=0")
                .leftJoin("dialect_class dc3 on dc3.id=t.third_id and dc3.is_deleted=0")
                .leftJoin("dialect_class dc4 on dc4.id=t.fourth_id and dc4.is_deleted=0")
                .leftJoin("dialect_class dc5 on dc5.id=t.fifth_id and dc5.is_deleted=0")
                .eq("t.is_deleted",0)
                .eq(query.getId()!=null,"t.id",query.getId())
                .eq(query.getPlan()!=null,"t.plan",query.getPlan())
                .like(StrUtil.isNotBlank(query.getTopClassification()),    "dc1.name", query.getTopClassification())
                .like(StrUtil.isNotBlank(query.getSecondClassification()), "dc2.name", query.getSecondClassification())
                .like(StrUtil.isNotBlank(query.getThirdClassification()),  "dc3.name", query.getThirdClassification())
                .like(StrUtil.isNotBlank(query.getFourthClassification()), "dc4.name", query.getFourthClassification())
                .like(StrUtil.isNotBlank(query.getFifthClassification()),  "dc5.name", query.getFifthClassification());
        List<DialectSortVO>dialectSortVOS=new ArrayList<>();
        List<DialectSortEntity>dialectSortEntities=wrapper.list();
        //筛选所有筛出实体用到的id组成集合
        Map<Integer,String> nameMap= dialectClassMapper.selectList(Wrappers.<DialectClassEntity>lambdaQuery()
                .eq(DialectClassEntity::getIsDeleted,false)).stream()
                .collect(Collectors.toMap(DialectClassEntity::getId,DialectClassEntity::getName));
        for(DialectSortEntity dialectSort: dialectSortEntities)
        {

            List<String> transitionIdStrs = (dialectSort.getTransitionIds()==null)?List.of():List.of(dialectSort.getTransitionIds().split(","));
            boolean isRejected=false;//是否舍弃
            Set<Integer>transitionDialectSortIds=new HashSet<>();
            //如果query.getTransitionDialectSortIds()为null则不筛，不筛就是全部都要
            for(String id:transitionIdStrs)
            {
                if(!id.trim().isEmpty())
                    transitionDialectSortIds.add(Integer.valueOf(id.trim()));
            }
            if(query.getTransitionDialectSortIds()!=null)
            {
                if(!transitionDialectSortIds.containsAll(CollUtil.toList(query.getTransitionDialectSortIds())))
                {
                    isRejected=true;
                }
            }
            if(!isRejected)
            {

                DialectSortVO dialectSortVO=new DialectSortVO();
                dialectSortVO.setId(dialectSort.getId());
                dialectSortVO.setTopClassification(nameMap.get(dialectSort.getTopId()));
                dialectSortVO.setSecondClassification(nameMap.get(dialectSort.getSecondId()));
                dialectSortVO.setThirdClassification(nameMap.get(dialectSort.getThirdId()));
                dialectSortVO.setFourthClassification(nameMap.get(dialectSort.getFourthId()));
                dialectSortVO.setFifthClassification(nameMap.get(dialectSort.getFifthId()));
                //找出所有与其过渡的DialectSort
                dialectSortVO.setTransitionDialectSort(transitionDialectSortIds.isEmpty()?List.of():
                        list(Wrappers.<DialectSortEntity>lambdaQuery().in(DialectSortEntity::getId,transitionDialectSortIds)
                                        .eq(DialectSortEntity::getIsDeleted,false)).stream()
                        .map(e->{
                            TransitionDialectVO transitionDialectVO=new TransitionDialectVO();
                            transitionDialectVO.setTopClassification(nameMap.get(e.getTopId()));
                            transitionDialectVO.setSecondClassification(nameMap.get(e.getSecondId()));
                            transitionDialectVO.setThirdClassification(nameMap.get(e.getThirdId()));
                            transitionDialectVO.setFourthClassification(nameMap.get(e.getFourthId()));
                            transitionDialectVO.setFifthClassification(nameMap.get(e.getFifthId()));
                            return transitionDialectVO;
                        }).collect(Collectors.toList()));
                dialectSortVO.setSortExplanation(dialectSort.getSortExplanation());
                dialectSortVO.setColours(StrUtil.isBlank(dialectSort.getColours())
                        ? new String[0]
                        : dialectSort.getColours().split(","));
                dialectSortVO.setPlan(dialectSort.getPlan());
                dialectSortVOS.add(dialectSortVO);
            }
        }
        //打印调试日志
        for(DialectSortVO dialectSortVO:dialectSortVOS)
        {
            log.debug(dialectSortVO.getId().toString());
        }
        long current=Math.max(1L,page.getCurrent());
        long size= Math.min(page.getSize()>0? page.getSize() : 10L, 100L);
        int total=dialectSortVOS.size();
        int from = (int) Math.min((current - 1) * size, total);
        int to = (int) Math.min(from + size, total);
        Page<DialectSortVO>dialectSortEntityPage=new Page<>(current,size,total);
        dialectSortEntityPage.setRecords(new ArrayList<>(dialectSortVOS.subList(from,to)));
        return dialectSortEntityPage;
    }
    private Integer getLastLNotNullLevelDialectClassId(DialectSortSaveDTO dialectSortSaveDTO)
    {
        if(dialectSortSaveDTO.getSecondId()==null)
            return dialectSortSaveDTO.getTopId();
        if(dialectSortSaveDTO.getThirdId()==null)
            return dialectSortSaveDTO.getSecondId();
        if(dialectSortSaveDTO.getFourthId()==null)
            return dialectSortSaveDTO.getThirdId();
        if(dialectSortSaveDTO.getFifthId()==null)
            return dialectSortSaveDTO.getFourthId();
        return dialectSortSaveDTO.getFifthId();
    }
    public R<Boolean> saveDialectSort(DialectSortSaveDTO dialectSortSaveDTO)
    {
//        boolean isBaseDialectSort=false;
//        if(dialectSortSaveDTO.getTransitionDialectSortIds()==null||dialectSortSaveDTO.getTransitionDialectSortIds().length==0)
//        {
//            if(StrUtil.isBlank(dialectSortSaveDTO.getColour()))
//                return R.failed(false).setMsg("添加的方言分类既不是过渡分类也不是基类");
//            else
//                isBaseDialectSort=true;
//
//        }
        //校验：必须保证全是同一个plan下的分区方案，且引用的DialectClass的level必须与分区等级对应
        //dialectSortSaveDTO的plan必填
        if(dialectSortSaveDTO.getPlan()==null)
            return R.failed(false).setMsg("方言分类的plan必填");
        if(dialectSortSaveDTO.getTopId()!=null) {
            DialectClassEntity top = dialectClassMapper.selectOne(Wrappers.<DialectClassEntity>lambdaQuery()
                    .eq(DialectClassEntity::getId, dialectSortSaveDTO.getTopId())
                    .eq(DialectClassEntity::getIsDeleted, false));
            if(top==null)
                return R.failed(false).setMsg("新增方言分类的一级分区不存在");
            if(!Objects.equals(top.getPlan(), dialectSortSaveDTO.getPlan()))
                return R.failed(false).setMsg("新增方言分类的一级分区所属方案与方言分类不一致");
        }

        if(dialectSortSaveDTO.getSecondId()!=null) {
            DialectClassEntity second = dialectClassMapper.selectOne(Wrappers.<DialectClassEntity>lambdaQuery()
                    .eq(DialectClassEntity::getId, dialectSortSaveDTO.getSecondId())
                    .eq(DialectClassEntity::getIsDeleted, false));
            if(second==null)
                return R.failed(false).setMsg("新增方言分类的一级分区不存在");
            if(!Objects.equals(second.getPlan(), dialectSortSaveDTO.getPlan()))
                return R.failed(false).setMsg("新增方言分类的一级分区所属方案与方言分类不一致");
        }
        if(dialectSortSaveDTO.getThirdId()!=null) {
            DialectClassEntity third = dialectClassMapper.selectOne(Wrappers.<DialectClassEntity>lambdaQuery()
                    .eq(DialectClassEntity::getId, dialectSortSaveDTO.getThirdId())
                    .eq(DialectClassEntity::getIsDeleted, false));
            if(third==null)
                return R.failed(false).setMsg("新增方言分类的一级分区不存在");
            if(!Objects.equals(third.getPlan(), dialectSortSaveDTO.getPlan()))
                return R.failed(false).setMsg("新增方言分类的一级分区所属方案与方言分类不一致");
        }
        if(dialectSortSaveDTO.getFourthId()!=null) {
            DialectClassEntity fourth = dialectClassMapper.selectOne(Wrappers.<DialectClassEntity>lambdaQuery()
                    .eq(DialectClassEntity::getId, dialectSortSaveDTO.getFourthId())
                    .eq(DialectClassEntity::getIsDeleted, false));
            if(fourth==null)
                return R.failed(false).setMsg("新增方言分类的一级分区不存在");
            if(!Objects.equals(fourth.getPlan(), dialectSortSaveDTO.getPlan()))
                return R.failed(false).setMsg("新增方言分类的一级分区所属方案与方言分类不一致");
        }
        if(dialectSortSaveDTO.getFifthId()!=null) {
            DialectClassEntity fifth = dialectClassMapper.selectOne(Wrappers.<DialectClassEntity>lambdaQuery()
                    .eq(DialectClassEntity::getId, dialectSortSaveDTO.getFifthId())
                    .eq(DialectClassEntity::getIsDeleted, false));
            if(fifth==null)
                return R.failed(false).setMsg("新增方言分类的一级分区不存在");
            if(!Objects.equals(fifth.getPlan(), dialectSortSaveDTO.getPlan()))
                return R.failed(false).setMsg("新增方言分类的一级分区所属方案与方言分类不一致");
        }
        //过渡方言分类的分区方言也要一致
        for(Integer id:dialectSortSaveDTO.getTransitionDialectSortIds())
        {
            DialectSortEntity dialectSortById=getOne(Wrappers.<DialectSortEntity>lambdaQuery()
                    .eq(DialectSortEntity::getId,id)
                    .eq(DialectSortEntity::getIsDeleted,false));
            if(dialectSortById==null)
                return R.failed(false).setMsg("过渡分类里面有方言分类不存在或已删除");
            if(!Objects.equals(dialectSortById.getPlan(), dialectSortSaveDTO.getPlan()))
                return R.failed(false).setMsg("过渡分类采用的分区方案必须和基分类一致");
        }

        //基础颜色都是五级分区中最后一个非null分区id对应的DialectClass的颜色
        StringBuilder colourStr= new StringBuilder();
        List<DialectClassEntity>dialectClassEntitiesByLastId= dialectClassMapper.selectList(Wrappers.<DialectClassEntity>lambdaQuery()
                .eq(DialectClassEntity::getId,getLastLNotNullLevelDialectClassId(dialectSortSaveDTO))
                .eq(DialectClassEntity::getIsDeleted,false)).stream().toList();
        if(CollUtil.isNotEmpty(dialectClassEntitiesByLastId))
        {
            colourStr.append(dialectClassEntitiesByLastId.get(0).getColour()).append(",");
        }
//        List<String> baseDialectSortColour;
//        if(!isBaseDialectSort) {
//            baseDialectSortColour = list(Wrappers.<DialectSortEntity>lambdaQuery()
//                    .eq(dialectSortSaveDTO.getTopId() != null, DialectSortEntity::getTopId, dialectSortSaveDTO.getTopId())
//                    .eq(dialectSortSaveDTO.getSecondId() != null, DialectSortEntity::getSecondId, dialectSortSaveDTO.getSecondId())
//                    .eq(dialectSortSaveDTO.getThirdId() != null, DialectSortEntity::getThirdId, dialectSortSaveDTO.getThirdId())
//                    .eq(dialectSortSaveDTO.getFourthId() != null, DialectSortEntity::getFourthId, dialectSortSaveDTO.getFourthId())
//                    .eq(dialectSortSaveDTO.getFifthId() != null, DialectSortEntity::getFifthId, dialectSortSaveDTO.getFifthId())
//                    .and(w -> w.isNull(DialectSortEntity::getTransitionIds)
//                            .or().eq(DialectSortEntity::getTransitionIds, ""))
//                    .eq(DialectSortEntity::getPlan,dialectSortSaveDTO.getPlan())
//                    .eq(DialectSortEntity::getIsDeleted, false)
//            ).stream().map(e -> e.getColours()).collect(Collectors.toList());
//            //不允许不存在同一分区方案下五级方言全部相同的没有过渡方言的方言分类，同一分区下五级方言全部相同的没有过渡方言的方言分类是这个方言分类的基色。
//            if(CollUtil.isEmpty(baseDialectSortColour)) {
//                return R.failed(false).setMsg("该过渡分类没有对应基类");
//            }
//            colourStr.append(baseDialectSortColour.get(0)).append(",");
//        }
//        else
//        {
//            colourStr.append(dialectSortSaveDTO.getColour()).append(",");
//        }
        DialectSortEntity dialectSort=new DialectSortEntity();
        StringBuilder transitionDialectIdStr= new StringBuilder();

        Integer[] ids=dialectSortSaveDTO.getTransitionDialectSortIds()==null?new Integer[0]:
                dialectSortSaveDTO.getTransitionDialectSortIds();
        // {id: colours}
        Map<Integer,String> sortIdColour=ids.length==0?Map.of():list(Wrappers.<DialectSortEntity>lambdaQuery()
            .in(DialectSortEntity::getId,CollUtil.toList(ids))
            .eq(DialectSortEntity::getIsDeleted,false))
                .stream()
                .collect(Collectors.toMap(DialectSortEntity::getId,e -> StrUtil.blankToDefault(e.getColours(), "")));
        for(Integer i:ids)
        {
            if(sortIdColour.containsKey(i))
            {
                transitionDialectIdStr.append(i).append(",");
                String colour= sortIdColour.get(i);
                colourStr.append(colour).append(",");
            }
        }
        // 防止退化为基类
        if (ids.length > 0 && transitionDialectIdStr.isEmpty()) {
            return R.failed(false, "所选的过渡分类不存在或已被删除");
        }
        transitionDialectIdStr.setLength(Math.max(0, transitionDialectIdStr.length() - 1));
        colourStr.setLength(Math.max(0, colourStr.length() - 1));
        dialectSort.setPlan(dialectSortSaveDTO.getPlan());
        dialectSort.setSortExplanation(dialectSortSaveDTO.getSortExplanation());
        dialectSort.setRemark(dialectSortSaveDTO.getRemark());
        dialectSort.setIsDeleted(false);
        dialectSort.setTopId(dialectSortSaveDTO.getTopId());
        dialectSort.setSecondId(dialectSortSaveDTO.getSecondId());
        dialectSort.setThirdId(dialectSortSaveDTO.getThirdId());
        dialectSort.setFourthId(dialectSortSaveDTO.getFourthId());
        dialectSort.setFifthId(dialectSortSaveDTO.getFifthId());
//        if(dialectSortSaveDTO.getTransitionDialectSortIds()==null||dialectSortSaveDTO.getTransitionDialectSortIds().length==0)
//            dialectSort.setColours(dialectSortSaveDTO.getColour());
//        else
        dialectSort.setColours(colourStr.toString());
        dialectSort.setTransitionIds(transitionDialectIdStr.toString());
        PigUser currentUser=SecurityUtils.getUser();
        if(currentUser==null)
            return R.failed(false).setMsg("未登录，无操作权限").setCode(401);
        dialectSort.setCreatorId(currentUser.getId());
//        return R.ok(save(dialectSort));
        if(save(dialectSort))
            return R.ok(true);
        return R.failed(false).setMsg("插入错误！");
    }

    public R<Boolean> updateDialectSort(DialectSortUpdateDTO dialectSortUpdateDTO)
    {
        // TODO 尝试让方言分区1-5和过渡分区不变也要带原值，不许为null，可不可以实现修改方言区
        // 前端保证未删除的方言分类才可编辑
        DialectSortEntity updateDialectSort=new DialectSortEntity();
        updateDialectSort.setId(dialectSortUpdateDTO.getId());
        if(dialectSortUpdateDTO.getRemark()!=null)
            updateDialectSort.setRemark(dialectSortUpdateDTO.getRemark());
        if(dialectSortUpdateDTO.getSortExplanation()!=null)
            updateDialectSort.setSortExplanation(dialectSortUpdateDTO.getSortExplanation());
//        if(dialectSortUpdateDTO.getColour()!=null)
//            updateDialectSort.setColours(dialectSortUpdateDTO.getColour());
        if(updateById(updateDialectSort))
            return R.ok(true);
        return R.failed(false).setMsg("更新错误！");
    }

    public R<Boolean> deleteDialectSortLogically(Integer[] ids)
    {
        if(ids==null||ids.length==0)
            return R.failed(false).setMsg("请选择要删除的方言");
        if(dialectPointMapper.selectList(Wrappers.<DialectPointEntity>lambdaQuery()
                .ne(DialectPointEntity::getStatus,"DELETED")
                .and(w->w.in(DialectPointEntity::getDialectSort1Id,CollUtil.toList(ids))
                        .or()
                        .in(DialectPointEntity::getDialectSort2Id,CollUtil.toList(ids))
                        .or()
                        .in(DialectPointEntity::getDialectSort3Id,CollUtil.toList(ids))
                        .or()
                        .in(DialectPointEntity::getDialectSort4Id,CollUtil.toList(ids)))).isEmpty() ||
                characterListMapper.selectList(Wrappers.<CharacterListEntity>lambdaQuery()
                        .ne(CharacterListEntity::getStatus,"DELETED")
                        .and(w->w.in(CharacterListEntity::getDialectSort1Id,CollUtil.toList(ids))
                                .or()
                                .in(CharacterListEntity::getDialectSort2Id,CollUtil.toList(ids))
                                .or()
                                .in(CharacterListEntity::getDialectSort3Id,CollUtil.toList(ids))
                                .or()
                                .in(CharacterListEntity::getDialectSort4Id,CollUtil.toList(ids)))).isEmpty()||
                wordListMapper.selectList(Wrappers.<WordListEntity>lambdaQuery()
                        .ne(WordListEntity::getStatus,"DELETED")
                        .and(w->w.in(WordListEntity::getDialectSort1Id,CollUtil.toList(ids))
                                .or()
                                .in(WordListEntity::getDialectSort2Id,CollUtil.toList(ids))
                                .or()
                                .in(WordListEntity::getDialectSort3Id,CollUtil.toList(ids))
                                .or()
                                .in(WordListEntity::getDialectSort4Id,CollUtil.toList(ids)))).isEmpty()||
                sentenceListMapper.selectList(Wrappers.<SentenceListEntity>lambdaQuery()
                        .ne(SentenceListEntity::getStatus,"DELETED")
                        .and(w->w.in(SentenceListEntity::getDialectSort1Id,CollUtil.toList(ids))
                                .or()
                                .in(SentenceListEntity::getDialectSort2Id,CollUtil.toList(ids))
                                .or()
                                .in(SentenceListEntity::getDialectSort3Id,CollUtil.toList(ids))
                                .or()
                                .in(SentenceListEntity::getDialectSort4Id,CollUtil.toList(ids)))).isEmpty())
        {
            //正常逻辑删除
//            dialectSort.setIsDeleted(true);
            List<DialectSortEntity> dialectSorts=list(Wrappers.<DialectSortEntity>lambdaQuery()
                    .in(DialectSortEntity::getId,CollUtil.toList(ids))
                    .eq(DialectSortEntity::getIsDeleted,false));
            for(DialectSortEntity dialectSort:dialectSorts)
            {
                dialectSort.setIsDeleted(true);
                if(!updateById(dialectSort))
                    return R.failed(false).setMsg("删除错误！");
            }
            return R.ok(true);
        }
        else
            return R.failed(false).setMsg("有与要删除的方言分类关联的对象，请先解绑再删除！");
    }
    /**
     * 筛选所有与已选择方言片(s)关联的方言分类（id+基和过渡方言名称+颜色）-基和其有关、过渡也要和其有关
     */
    private boolean isContainTransitionDialectSort(DialectSortEntity dialectSort,Set<Integer> transitionIds)
    {
        List<String> dialectSortTransitionIdStrs = (dialectSort.getTransitionIds()==null)?List.of():List.of(dialectSort.getTransitionIds().split(","));
        List<Integer>dialectSortTransitionIds=dialectSortTransitionIdStrs.stream().map(e->e.trim().isEmpty()?null:Integer.valueOf(e.trim())).filter(Objects::nonNull).toList();
//        List<Integer>transitionIdList= CollUtil.toList(transitionIds);
        return !Collections.disjoint(dialectSortTransitionIds,transitionIds);
    }
    //我们规定dialectClassEntities数值的第一个是topId、第二个是secondId、第三个是thirdId...
    public List<DialectSortVO>selectDialectSortsByDialectClasses(DialectClassEntity[]dialectClassEntities)
    {
        List<Integer>dialectClassIds=CollUtil.toList(dialectClassEntities).stream().map(DialectClassEntity::getId).toList();//.collect(Collectors.toSet());
        if(dialectClassIds.size()>5)
            return null;
        //把与其关联的基类全部筛出来
        List<DialectSortEntity>linkedBaseDialectSort=list(Wrappers.<DialectSortEntity>lambdaQuery()
                .eq(DialectSortEntity::getIsDeleted,false)
                .and(w-> {
                    if(dialectClassIds.size()>0)w.eq(DialectSortEntity::getTopId,dialectClassIds.get(0));
                    if(dialectClassIds.size()>1)w.eq(DialectSortEntity::getSecondId,dialectClassIds.get(1));
                    if(dialectClassIds.size()>2)w.eq(DialectSortEntity::getThirdId,dialectClassIds.get(2));
                    if(dialectClassIds.size()>3)w.eq(DialectSortEntity::getFourthId,dialectClassIds.get(3));
                    if(dialectClassIds.size()>4)w.eq(DialectSortEntity::getFifthId,dialectClassIds.get(4));
                })
//                    .and(w -> w.isNull(DialectSortEntity::getTransitionIds)
//                            .or().eq(DialectSortEntity::getTransitionIds, "")
//                            .or().eq(DialectSortEntity::getTransitionIds, " "))
        ).stream().filter(e->StrUtil.isBlank(e.getTransitionIds())).toList();//这也是结果之一
        if(linkedBaseDialectSort.isEmpty())//基类都没找到匹配的，那过渡类里更找不到
            return null;
        int planId=linkedBaseDialectSort.get(0).getPlan();
        for(DialectSortEntity dialectSort:linkedBaseDialectSort)
        {
            if(dialectSort.getPlan()!=planId)
                return null;//不是同一个分区方案的方言片列表
        }
        Set<Integer>linkedBaseDialectSortIds=linkedBaseDialectSort.stream().map(DialectSortEntity::getId).collect(Collectors.toSet());
        //匹配过度类，过渡类的过渡id里面有以上基类其中之一即算
        List<DialectSortEntity>linkedTransitionDialectSort=list(Wrappers.<DialectSortEntity>lambdaQuery()
                .eq(DialectSortEntity::getIsDeleted,false)
//                .eq(e->isContainTransitionDialectSort(e,linkedBaseDialectSortIds),true)  eq等生成sql子句的代码里不要用函数
                .eq(DialectSortEntity::getPlan,planId)).stream().filter(e->isContainTransitionDialectSort(e,linkedBaseDialectSortIds))
                .toList();
        //合并两个list
        List<DialectSortEntity>linkedDialectSort=new ArrayList<>(linkedBaseDialectSort);
        linkedDialectSort.addAll(linkedTransitionDialectSort);

        /**
         * private Integer id;/
         *     // 所属方案编号（前端对应）
         *     private Integer plan;/
         *     // 一级分区
         *     private String topClassification;
         *     // 二级分区
         *     private String secondClassification;
         *     // 三级分区
         *     private String thirdClassification;
         *     // 四级分区
         *     private String fourthClassification;
         *     // 五级分区
         *     private String fifthClassification;
         *     // 过渡方言分类
         *     private List<TransitionDialectVO> transitionDialectSort;/
         *     // 颜色 (RGB)
         *     private String[] colours;/
         *     // 分类说明
         *     private String sortExplanation;/
         */
//        Set<Integer>allDialectClassIds=list(Wrappers.<DialectSortEntity>lambdaQuery().eq(DialectSortEntity::getIsDeleted,false)).stream().flatMap(e-> Stream.of(e.getTopId(),e.getSecondId(),
//                e.getThirdId(),e.getFourthId(),e.getFifthId())).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Integer,String>idName=dialectClassMapper.selectList(Wrappers.<DialectClassEntity>lambdaQuery()
                .eq(DialectClassEntity::getIsDeleted,false)
                .eq(DialectClassEntity::getPlan,planId)).stream().collect(Collectors.toMap(DialectClassEntity::getId,DialectClassEntity::getName));
        Map<Integer,DialectSortEntity>idDialectSort=list(Wrappers.<DialectSortEntity>lambdaQuery()
                .eq(DialectSortEntity::getIsDeleted,false)
                .eq(DialectSortEntity::getPlan,planId)).stream().collect(Collectors.toMap(DialectSortEntity::getId, Function.identity()));
        List<DialectSortVO>allDialectSortVOs=new ArrayList<>();
        for(DialectSortEntity dialectSort:linkedDialectSort)
        {
            DialectSortVO dialectSortVO=new DialectSortVO();
            dialectSortVO.setId(dialectSort.getId());
            dialectSortVO.setPlan(dialectSort.getPlan());
            dialectSortVO.setColours(dialectSort.getColours().split(","));
            dialectSortVO.setSortExplanation(dialectSort.getSortExplanation());
            dialectSortVO.setTopClassification(idName.get(dialectSort.getTopId()));
            dialectSortVO.setSecondClassification(idName.get(dialectSort.getSecondId()));
            dialectSortVO.setThirdClassification(idName.get(dialectSort.getThirdId()));
            dialectSortVO.setFourthClassification(idName.get(dialectSort.getFourthId()));
            dialectSortVO.setFifthClassification(idName.get(dialectSort.getFifthId()));
            if(dialectSort.getTransitionIds()==null||StrUtil.isBlank(dialectSort.getTransitionIds()))
            {
                allDialectSortVOs.add(dialectSortVO);
                continue;
            }
            List<String>transitionIdStrs=CollUtil.toList(dialectSort.getTransitionIds().split(","));
            Set<Integer>transitionIds=transitionIdStrs.stream().map(e->e.trim().isEmpty()?null:Integer.valueOf(e.trim())).filter(Objects::nonNull).collect(Collectors.toSet());
            if(transitionIds.isEmpty())
            {
                allDialectSortVOs.add(dialectSortVO);
                continue;
            }
            //填充transitionDialectSortVOs
            List<TransitionDialectVO>transitionDialectVOS=new ArrayList<>();
            for(Integer id:transitionIds)
            {
                TransitionDialectVO transitionDialectVO=new TransitionDialectVO();
                DialectSortEntity transitionDialectSort=idDialectSort.get(id);
                transitionDialectVO.setTopClassification(idName.get(transitionDialectSort.getTopId()));
                transitionDialectVO.setSecondClassification(idName.get(transitionDialectSort.getSecondId()));
                transitionDialectVO.setThirdClassification(idName.get(transitionDialectSort.getThirdId()));
                transitionDialectVO.setFourthClassification(idName.get(transitionDialectSort.getFourthId()));
                transitionDialectVO.setFifthClassification(idName.get(transitionDialectSort.getFifthId()));
                transitionDialectVOS.add(transitionDialectVO);
            }
            dialectSortVO.setTransitionDialectSort(transitionDialectVOS);
            allDialectSortVOs.add(dialectSortVO);
//            List<DialectSortEntity>transitionDialectSorts=list(Wrappers.<DialectSortEntity>lambdaQuery()
//                    .in(DialectSortEntity::getId,transitionIds)
//                    .eq(DialectSortEntity::getIsDeleted,false));

        }
        return allDialectSortVOs;
    }
}
