package com.sleepywang.xiangyin.dialect.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.dialect.entity.DialectClassEntity;
import com.sleepywang.xiangyin.dialect.entity.DialectSortEntity;
import com.sleepywang.xiangyin.dialect.mapper.DialectClassMapper;
import com.sleepywang.xiangyin.dialect.mapper.DialectSortMapper;
import com.sleepywang.xiangyin.dialect.service.DialectClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 方言片
 *
 * @author sleepywang
 * @date 2026-09-18 01:07:26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DialectClassServiceImpl extends ServiceImpl<DialectClassMapper, DialectClassEntity> implements DialectClassService {
    private final DialectSortMapper dialectSortMapper;
    private final DialectClassMapper dialectClassMapper;
    @Override
    public R<Boolean> deleteDialectClass(Integer[] ids) {
        if(ids==null||ids.length==0)
            return R.failed(false).setMsg("请选择要删除的方言");
        if(dialectSortMapper.selectList(Wrappers.<DialectSortEntity>lambdaQuery()
                        .eq(DialectSortEntity::getIsDeleted,false)
                        .and(w->w.in(DialectSortEntity::getTopId, CollUtil.toList(ids))
                                .or().in(DialectSortEntity::getSecondId, CollUtil.toList(ids))
                                .or().in(DialectSortEntity::getThirdId, CollUtil.toList(ids))
                                .or().in(DialectSortEntity::getFourthId, CollUtil.toList(ids))
                                .or().in(DialectSortEntity::getFifthId, CollUtil.toList(ids))))
                .isEmpty())
        {
            // 正常删除
            if(removeBatchByIds(CollUtil.toList(ids)))
                return R.ok(true);
            return R.failed(false).setMsg("删除失败！");
        }

        return R.failed(false).setMsg("有与要删除的方言片关联的对象，请先解绑再删除！");
    }
    private Integer getNextDialectClassId(DialectSortEntity dialectSort,Integer id)
    {
        if(Objects.equals(dialectSort.getTopId(), id))
            return dialectSort.getSecondId();
        if(Objects.equals(dialectSort.getSecondId(),id))
            return dialectSort.getThirdId();
        if(Objects.equals(dialectSort.getThirdId(),id))
            return dialectSort.getFourthId();
        if(Objects.equals(dialectSort.getFourthId(),id))
            return dialectSort.getFifthId();
        return null;
    }
    public List<DialectClassEntity> selectNextDialectClass(Integer id)
    {
        //先找出未被删除的这个DialectClassId对应的实体
        List<DialectClassEntity> dialectClassParents=dialectClassMapper.selectList(Wrappers.<DialectClassEntity>lambdaQuery()
                .eq(DialectClassEntity::getId,id)
                .ne(DialectClassEntity::getIsDeleted,true));
        DialectClassEntity dialectClassParent=dialectClassParents.isEmpty()?null:dialectClassParents.get(0);
        if(dialectClassParent==null||dialectClassParent.getLevel()==5)
            return null;
        //我们保证DialectClass和引用其的DialectSort的分区字段严格对应，不会出现明明是level是4，引用它的DialectSort却是top_id的情况
        //也保证所有DialectSort的方言分区的plan一致性
        //再找到做所有引用了这个实体的DialectSort的下一级分区的id对应的DialectClass实体
        List<DialectSortEntity> dialectSorts=dialectSortMapper.selectList(Wrappers.<DialectSortEntity>lambdaQuery()
                .ne(DialectSortEntity::getIsDeleted,true)
                .and(w->w.eq(DialectSortEntity::getTopId,dialectClassParent.getId())
                        .or().eq(DialectSortEntity::getSecondId,dialectClassParent.getId())
                        .or().eq(DialectSortEntity::getThirdId,dialectClassParent.getId())
                        .or().eq(DialectSortEntity::getFourthId,dialectClassParent.getId())
                        .or().eq(DialectSortEntity::getFifthId,dialectClassParent.getId())));
        if(dialectSorts.isEmpty())
            return null;
        Set<Integer> nextLevelIds=dialectSorts.stream().map(e->getNextDialectClassId(e,id)).filter(Objects::nonNull).collect(Collectors.toSet());
        if(nextLevelIds.isEmpty())
            return null;
        Set<DialectClassEntity>nextDialectClass=dialectClassMapper.selectList(Wrappers.<DialectClassEntity>lambdaQuery()
                .in(DialectClassEntity::getId,nextLevelIds)
                .ne(DialectClassEntity::getIsDeleted,true)).stream().collect(Collectors.toSet());
        return nextDialectClass.stream().toList();
    }
}
