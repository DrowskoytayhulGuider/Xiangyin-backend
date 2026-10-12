package com.sleepywang.xiangyin.dialect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.common.core.context.UserContext;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.security.util.SecurityUtils;
import com.sleepywang.xiangyin.dialect.dto.DialectPointQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectPointSaveDTO;
import com.sleepywang.xiangyin.dialect.entity.CorpusEntity;
import com.sleepywang.xiangyin.dialect.entity.DialectClassEntity;
import com.sleepywang.xiangyin.dialect.entity.DialectPointEntity;
import com.sleepywang.xiangyin.dialect.entity.DialectSortEntity;
import com.sleepywang.xiangyin.dialect.mapper.CorpusMapper;
import com.sleepywang.xiangyin.dialect.mapper.DialectClassMapper;
import com.sleepywang.xiangyin.dialect.mapper.DialectPointMapper;
import com.sleepywang.xiangyin.dialect.service.DialectPointService;
import com.sleepywang.xiangyin.dialect.service.DialectSortService;
import com.sleepywang.xiangyin.dialect.util.CommUtils;
import com.sleepywang.xiangyin.dialect.vo.DialectPointVO;
import com.sleepywang.xiangyin.dialect.vo.DialectSortVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.util.OtherInfoGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 方言点
 *
 * @author sleepywang
 * @date 2026-09-19 20:37:55
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DialectPointServiceImpl extends ServiceImpl<DialectPointMapper, DialectPointEntity> implements DialectPointService {

    //按条件筛选方言点
//     * 这个接口用来分页返回所有的方言点，可以支持按方言点名称、行政区、
//     * 分区方案下的方言分区、创建者、管理者、状态、创建时间、发布时间筛选。默认剔除已删除项
//    private Integer id;
//     * 方言点名称
//    private String name;
//     * 省（数据由GIS服务提供，本地不管理）
//    private String province;
//     * 市（数据由GIS服务提供，本地不管理）
//    private String city;
//     * 县（数据由GIS服务提供，本地不管理）
//    private String county;
//     * 乡（数据由GIS服务提供，本地不管理）
//    private String town;
//     * 居民点 like
//    private String address;
//     * 方言分区方案编号
//    private Integer plan;
//     * 方言分类涉及的方言片
//    private Integer dialectSort1Id;
//     * 是否筛选本管理员管理的方言点
//    private Boolean isAdmin;
//     * 状态（未认领-UNCLAIMED、已发布-RELEASED、已删除-DELETED）
//    private String status;
//     * 发布时间
//    private LocalDateTime releaseTime;
//     * 创建时间
//    private LocalDateTime createTime;
    private final DialectSortService dialectSortService;
    private final DialectClassMapper dialectClassMapper;
    private final CorpusMapper corpusMapper;
    public List<DialectPointVO> selectDialectPoints(DialectPointQueryDTO query)
    {
        if(query.getPlan()!=null&&(query.getPlan()>=5||query.getPlan()<=0))
            return null;
        Set<Integer>matchDialectSortIds;
        if(query.getDialectClassIds()!=null&&!query.getDialectClassIds().isEmpty())
        {
            List<DialectSortVO>dialectSortVOS=dialectSortService.selectDialectSortsByDialectClasses(query.getDialectClassIds());
            System.out.println("dialectSortVOS");
            System.out.println(dialectSortVOS);
            matchDialectSortIds=dialectSortVOS.stream().map(DialectSortVO::getId).collect(Collectors.toSet());
        }
        else
            matchDialectSortIds=Set.of();
        //先将满足查询条件的Entities查出来，然后再装配VO
        List<DialectPointEntity>matchDialectPoints=list(Wrappers.<DialectPointEntity>lambdaQuery()
                .ne(DialectPointEntity::getStatus,"DELETED")
                .eq(query.getId()!=null,DialectPointEntity::getId,query.getId())
                .like(!StrUtil.isBlank(query.getName()),DialectPointEntity::getName,query.getName())
                .eq(!StrUtil.isBlank(query.getProvince()),DialectPointEntity::getProvince,query.getProvince())
                .eq(!StrUtil.isBlank(query.getCity()),DialectPointEntity::getCity,query.getCity())
                .eq(!StrUtil.isBlank(query.getCounty()),DialectPointEntity::getCounty,query.getCounty())
                .eq(!StrUtil.isBlank(query.getTown()),DialectPointEntity::getTown,query.getTown())
                .eq(!StrUtil.isBlank(query.getAddress()),DialectPointEntity::getAddress,query.getAddress())
                .in(query.getPlan()!=null&&!matchDialectSortIds.isEmpty()&&query.getPlan()==1,DialectPointEntity::getDialectSort1Id,matchDialectSortIds)
                .in(query.getPlan()!=null&&!matchDialectSortIds.isEmpty()&&query.getPlan()==2,DialectPointEntity::getDialectSort2Id,matchDialectSortIds)
                .in(query.getPlan()!=null&&!matchDialectSortIds.isEmpty()&&query.getPlan()==3,DialectPointEntity::getDialectSort3Id,matchDialectSortIds)
                .in(query.getPlan()!=null&&!matchDialectSortIds.isEmpty()&&query.getPlan()==4,DialectPointEntity::getDialectSort4Id,matchDialectSortIds)
                .eq(query.getIsAdmin()!=null&&query.getIsAdmin(),DialectPointEntity::getAdminId, SecurityUtils.getUser().getId())
                .eq(!StrUtil.isBlank(query.getStatus()),DialectPointEntity::getStatus,query.getStatus())
                .ge(query.getCreateStartTime()!=null,DialectPointEntity::getCreateTime,query.getCreateStartTime())
                .le(query.getCreateEndTime()!=null,DialectPointEntity::getCreateTime,query.getCreateEndTime())
                .ge(query.getReleaseStartTime()!=null,DialectPointEntity::getReleaseTime,query.getReleaseStartTime())
                .le(query.getReleaseEndTime()!=null,DialectPointEntity::getReleaseTime,query.getReleaseEndTime())
        );
        System.out.println(matchDialectPoints);
        //DialectPointVO
//        private Integer id;
//        private String name;
//        private String province;
//        private String city;
//        private String county;
//        private String town;
//        private String address;
//        private Double lng;
//        private Double lat;
//        private String introduction;
//        private String phonologyExplanation;
//        private DialectSortVO dialectSort1;
//        private DialectSortVO dialectSort2;
//        private DialectSortVO dialectSort3;
//        private DialectSortVO dialectSort4;
//        private Long adminId;
//        private String status;
//        private String remark;
//        private LocalDateTime releaseTime;
//        private LocalDateTime createTime;
        if(matchDialectPoints.isEmpty())
            return null;
        return fillDialectPointVO(matchDialectPoints);
    }
    public List<DialectPointVO> fillDialectPointVO(List<DialectPointEntity> dialectPointEntities)
    {
        List<DialectPointVO>matchDialectPointVOs=new ArrayList<>();
        Map<Integer,DialectSortEntity> idDialectSort=dialectSortService.list(Wrappers.<DialectSortEntity>lambdaQuery()
                        .eq(DialectSortEntity::getIsDeleted,false))
                .stream().collect(Collectors.toMap(DialectSortEntity::getId, Function.identity()));
        Map<Integer,String>idName=dialectClassMapper.selectList(Wrappers.<DialectClassEntity>lambdaQuery()
                        .eq(DialectClassEntity::getIsDeleted,false))
                .stream().collect(Collectors.toMap(DialectClassEntity::getId,DialectClassEntity::getName));
        List<DialectSortEntity>dialectSortEntities=new ArrayList<>();
        for(DialectPointEntity dialectPoint:dialectPointEntities)
        {
            DialectPointVO dialectPointVO=new DialectPointVO();
            dialectPointVO.setId(dialectPoint.getId());
            dialectPointVO.setName(dialectPoint.getName());
            dialectPointVO.setProvince(dialectPoint.getProvince());
            dialectPointVO.setCity(dialectPoint.getCity());
            dialectPointVO.setCounty(dialectPoint.getCounty());
            dialectPointVO.setTown(dialectPoint.getTown());
            dialectPointVO.setAddress(dialectPoint.getAddress());
            dialectPointVO.setLat(dialectPoint.getLat());
            dialectPointVO.setLng(dialectPoint.getLng());
            dialectPointVO.setIntroduction(dialectPoint.getIntroduction());
            dialectPointVO.setPhonologyExplanation(dialectPoint.getPhonologyExplanation());
            // dialectSort1~4的装配，四个分区方案下的dialectSort
            // id-DialectSortEntity-(DialectSortEntity.topId)-DialectClassEntity-
            // (DialectClassEntity.name)-topClassification(String)-
            // (DialectSortEntity.transitionIds,String)-List<DialectSortEntity>-
            // (DialectSortEntity.topId)-DialectClassEntity-(DialectClassEntity.name)-
            // TransitionDialectVO.topClassification(String)-List<TransitionDialectVO>-
            // DialectSortEntity.colours,String-(split)-DialectSortVO.colours,[]-DialectSortVO
            // TODO 到DialectSortService中将这一块封装，然后复用
            dialectSortEntities.add(dialectPoint.getDialectSort1Id()==null?null:idDialectSort.get(dialectPoint.getDialectSort1Id()));
            dialectSortEntities.add(dialectPoint.getDialectSort2Id()==null?null:idDialectSort.get(dialectPoint.getDialectSort2Id()));
            dialectSortEntities.add(dialectPoint.getDialectSort3Id()==null?null:idDialectSort.get(dialectPoint.getDialectSort3Id()));
            dialectSortEntities.add(dialectPoint.getDialectSort4Id()==null?null:idDialectSort.get(dialectPoint.getDialectSort4Id()));

            dialectPointVO.setAdminId(dialectPoint.getAdminId());
            dialectPointVO.setStatus(dialectPoint.getStatus());
            dialectPointVO.setRemark(dialectPoint.getRemark());
            dialectPointVO.setReleaseTime(dialectPoint.getReleaseTime());
            dialectPointVO.setCreateTime(dialectPoint.getCreateTime());
            matchDialectPointVOs.add(dialectPointVO);
        }
        System.out.println(dialectSortEntities);
        //不要用ArrayQueue实现队列，只要队列里面有null（用null占位）就报错
        //用LinkedList实现
        Queue<DialectSortVO>dialectSortVOs=new LinkedList<>(dialectSortService.dialectSortEntities2DialectSortVOs(dialectSortEntities,idName,idDialectSort));
        for(DialectPointVO dialectPointVO:matchDialectPointVOs)
        {
            dialectPointVO.setDialectSort1(dialectSortVOs.poll());
            dialectPointVO.setDialectSort2(dialectSortVOs.poll());
            dialectPointVO.setDialectSort3(dialectSortVOs.poll());
            dialectPointVO.setDialectSort4(dialectSortVOs.poll());
        }
        return matchDialectPointVOs;
    }
    public IPage<DialectPointVO> selectDialectPointsPage(Page page, DialectPointQueryDTO query)
    {
        List<DialectPointVO>matchDialectPointVOs=selectDialectPoints(query);
        return CommUtils.List2IPage(page,matchDialectPointVOs);
    }

    public R saveDialectPoint(DialectPointSaveDTO dialectPointSaveDTO)
    {
//         * 方言点名称
//        private String name;
//         * 省（数据由GIS服务提供，本地不管理）
//        private String province;
//         * 市（数据由GIS服务提供，本地不管理）
//        private String city;
//         * 县（数据由GIS服务提供，本地不管理）
//        private String county;
//         * 乡（数据由GIS服务提供，本地不管理）
//        private String town;
//         * 居民点
//        private String address;
//         * 经度
//        private Double lng;
//         * 纬度
//        private Double lat;
//         * 方言点介绍
//        private String introduction;
//         * 方言点音韵特征
//        private String phonologyExplanation;
//         * 分区方案一方言类型
//        private Integer dialectSort1Id;
//         * 分区方案二方言类型
//        private Integer dialectSort2Id;
//         * 分区方案三方言类型
//        private Integer dialectSort3Id;
//         * 分区方案四方言类型
//        private Integer dialectSort4Id;
//         * 备注
//        private String remark;
        DialectPointEntity dialectPointEntity=new DialectPointEntity();
        dialectPointEntity.setName(dialectPointSaveDTO.getName());
        dialectPointEntity.setProvince(dialectPointSaveDTO.getProvince());
        dialectPointEntity.setCity(dialectPointSaveDTO.getCity());
        dialectPointEntity.setCounty(dialectPointSaveDTO.getCounty());
        dialectPointEntity.setTown(dialectPointSaveDTO.getTown());
        dialectPointEntity.setAddress(dialectPointSaveDTO.getAddress());
        dialectPointEntity.setLat(dialectPointSaveDTO.getLat());
        dialectPointEntity.setLng(dialectPointSaveDTO.getLng());
        dialectPointEntity.setIntroduction(dialectPointSaveDTO.getIntroduction());
        dialectPointEntity.setPhonologyExplanation(dialectPointSaveDTO.getPhonologyExplanation());
        dialectPointEntity.setDialectSort1Id(dialectPointSaveDTO.getDialectSort1Id());
        dialectPointEntity.setDialectSort2Id(dialectPointSaveDTO.getDialectSort2Id());
        dialectPointEntity.setDialectSort3Id(dialectPointSaveDTO.getDialectSort3Id());
        dialectPointEntity.setDialectSort4Id(dialectPointSaveDTO.getDialectSort4Id());
        dialectPointEntity.setRemark(dialectPointSaveDTO.getRemark());
        dialectPointEntity.setStatus("UNCLAIMED");
        if(save(dialectPointEntity))
            return R.ok(true);
        return R.failed(false).setMsg("新增方言点失败");
    }
    // 用户不可删除，管理员才可以。
    // 先校验有没有语料关联方言点id in ids且status不为DELETED，没有才能将选中的ids对应的status字段置为DELETED；
    // 有则阻断操作并提示。换言之，管理员只能删除空的方言点
    public R deleteDialectPoint(Integer[] ids)
    {
        if(ids==null||ids.length==0)
            return R.failed(false).setMsg("请选择要删除的方言点");
        //先校验有没有语料关联方言点id in ids且status不为DELETED
        boolean hasLinkedCorpus=!corpusMapper.selectList(Wrappers.<CorpusEntity>lambdaQuery()
                .ne(CorpusEntity::getStatus,"DELETED")
                .in(CorpusEntity::getDialectPointId, CollUtil.toList(ids))).isEmpty();
        if(hasLinkedCorpus)
            return R.failed(false).setMsg("有引用该方言点的语料，只有为空的方言点才可被删除！");
        if(update(Wrappers.<DialectPointEntity>lambdaUpdate()
                .set(DialectPointEntity::getStatus, "DELETED")
                .in(DialectPointEntity::getId, CollUtil.toList(ids)))){
            return R.ok(true);
        }
        return R.failed(false).setMsg("删除出错");
    }
}
