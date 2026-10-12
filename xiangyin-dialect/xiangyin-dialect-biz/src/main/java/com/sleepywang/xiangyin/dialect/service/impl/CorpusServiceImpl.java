package com.sleepywang.xiangyin.dialect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.common.data.cache.RedisUtils;
import com.sleepywang.xiangyin.common.security.util.SecurityUtils;
import com.sleepywang.xiangyin.dialect.dto.CorpusQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.CorpusSaveDTO;
import com.sleepywang.xiangyin.dialect.entity.*;
import com.sleepywang.xiangyin.dialect.mapper.*;
import com.sleepywang.xiangyin.dialect.service.CorpusService;
import com.sleepywang.xiangyin.dialect.service.DialectPointService;
import com.sleepywang.xiangyin.dialect.util.CommUtils;
import com.sleepywang.xiangyin.dialect.vo.CorpusVO;
import com.sleepywang.xiangyin.dialect.vo.DialectPointVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 语料
 *
 * @author sleepywang
 * @date 2026-09-19 20:37:15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CorpusServiceImpl extends ServiceImpl<CorpusMapper, CorpusEntity> implements CorpusService {
//     * 语料编号
//    private Integer id;
//     * 所属方言点
//    private Integer dialectPointId;
//     * 语料名称
//    private String name;
//     * 所属派别（新派-NEW、中派-MEDIUM、老派-OLD）
//    private String faction;
//     * 发音人年龄
//    private Integer ageStart;
//    private Integer ageEnd;
//     * 发音人性别
//    private String sex;
//     * 发音人文化程度
//    private List<String> educationLevels;
//     * 发音人职业
//    private String vocation;
//     * 提交人
//    private Long submitterId;
//     * 状态（草稿-DRAFT、待审核-PENDING、已发布-RELEASED、已打回-REJECTED、已删除-DELETED）。
//    private String status;
//     * 创建时间
//    private LocalDateTime createTimeStart;
//    private LocalDateTime createTimeEnd;
//     * 是否按点赞数排序，默认false，并按照正宗数排序
//    private Boolean isSortedByLike=false;
//    private static final String CORPUS_RELEASED_KEY="corpus:released";
    private final DialectPointService dialectPointService;
    private final CorpusCharacterMapper corpusCharacterMapper;
    private final CorpusWordMapper corpusWordMapper;
    private final CorpusSentenceMapper corpusSentenceMapper;
    private final FreeSpeechMapper freeSpeechMapper;
    private final ObjectLikeMapper objectLikeMapper;
    private final CorpusAuthenticMapper corpusAuthenticMapper;
    private final CorpusPublishedMapper corpusPublishedMapper;

//    private final DialectPointMapper dialectPointMapper;
    public List<CorpusVO> getCorpusVOs(CorpusQueryDTO query) {
        //DONE 这里还必须考虑，如果用户将语料修改为了草稿，并且发布给管理员审核，这个时候数据库中的语料状态已变为待审核，此时要保证之前发布的版本
        //DONE 仍能被其他用户可见，故不能更新缓存，但此时管理员也是从缓存获取已发布的语料，那这个语料已变为待审核就对管理员不可见。
        //DONE 解决方案：管理员先从数据库获取未删除且未发布且不为草稿的语料，再从缓存中获取已发布的语料，并且剔除已在数据库中筛选出来的id
        //DONE 因此用以审核和用以浏览的逻辑不一样，需区分

        //DONE 最好重构这一部分，已发布的语料我们再数据库中多加一个表，published_corpus，靠这个表来判断是否有已发布的上一个版本。不再使用redis。
        //DONE 已发布的语料用户浏览的得是这张表的内容。管理员审核依然是从工作表中获取未删除且未发布且不为草稿的语料，
        // 再从发布表中获取已发布的语料，并且剔除已在工作表中筛选出来的id
        // 并不要再全量重建发布表了。将缓存全部撤掉
        //DONE 还有一个问题，既然用户撤回已发布的语料为草稿之后，浏览模式只认发布表中的已发布的（状态为RELEASE）；审核模式就根本不筛选DRAFT，
        // 而且也是优先从缓存读已发布的。这就导致语料提交的用户使用这个接口，无论如何都找不到自己撤回成草稿的语料，更遑论修改和重新发布。

        //已发布的语料必须要从从缓存中读出来的才算
        if(query.getMode()==null)//此为必填项
            return null;
        List<CorpusEntity> matchCorpusOthers;
        List<CorpusPublishedEntity> matchCorpusReleased = null;
        List<String> educationLevels = CollUtil.isEmpty(query.getEducationLevels()) ? null : query.getEducationLevels()
                .stream().filter(e -> !StrUtil.isBlank(e)).toList();
//        List<CorpusEntity> corpusReleased = RedisUtils.get(CORPUS_RELEASED_KEY);
//        System.out.println("corpus_raw:\n"+corpusReleased);
//        if (corpusReleased == null)
//            corpusReleased = new ArrayList<>();

        if (query.getMode()==1) {
//            matchCorpusReleased = StrUtil.isBlank(query.getStatus()) || query.getStatus().equals("RELEASED") ? corpusReleased.stream()
//                    .filter(e -> query.getId() == null || query.getId().equals(e.getId()))/
//                    .filter(e -> CommUtils.like(e.getName(), query.getName()))/
//                    .filter(e -> StrUtil.isBlank(query.getFaction()) || query.getFaction().equals(e.getFaction()))/
//                    .filter(e -> query.getAgeStart() == null || e.getAge() >= query.getAgeStart())/
//                    .filter(e -> query.getAgeEnd() == null || e.getAge() <= query.getAgeEnd())/
//                    .filter(e -> StrUtil.isBlank(query.getSex()) || e.getSex().equals(query.getSex()))/
//                    .filter(e -> CollUtil.isEmpty(educationLevels) || educationLevels.contains(e.getEducationLevel()))/
//                    .filter(e -> CommUtils.like(e.getVocation(), query.getVocation()))/
//                    .filter(e -> query.getSubmitterId() == null || query.getSubmitterId().equals(e.getSubmitterId()))/
//                    .filter(e -> "RELEASED".equals(e.getStatus()))/
//                    .filter(e -> query.getCreateTimeStart() == null || !e.getCreateTime().isBefore(query.getCreateTimeStart()))
//                    .filter(e -> query.getCreateTimeEnd() == null || !e.getCreateTime().isAfter(query.getCreateTimeEnd())).toList() : null;
//            System.out.println("fromRedisFiltered: \n"+matchCorpusReleased);
            matchCorpusReleased=StrUtil.isBlank(query.getStatus()) || query.getStatus().equals("RELEASED") ?corpusPublishedMapper.selectList(Wrappers.<CorpusPublishedEntity>lambdaQuery()
                    .eq(query.getId()!=null,CorpusPublishedEntity::getCorpusId,query.getId())
                    .like(!StrUtil.isBlank(query.getName()),CorpusPublishedEntity::getName,query.getName())
                    .eq(!StrUtil.isBlank(query.getFaction()),CorpusPublishedEntity::getFaction,query.getFaction())
                    .ge(query.getAgeStart() != null,CorpusPublishedEntity::getAge,query.getAgeStart())
                    .le(query.getAgeEnd() != null,CorpusPublishedEntity::getAge,query.getAgeEnd())
                    .eq(!StrUtil.isBlank(query.getSex()),CorpusPublishedEntity::getSex,query.getSex())
                    .in(!CollUtil.isEmpty(educationLevels),CorpusPublishedEntity::getEducationLevel,query.getEducationLevels())
                    .like(!StrUtil.isBlank(query.getVocation()),CorpusPublishedEntity::getVocation,query.getVocation())
                    .eq(query.getSubmitterId() != null,CorpusPublishedEntity::getSubmitterId,query.getSubmitterId())
                    .eq(CorpusPublishedEntity::getStatus,"RELEASED")
                    .ge(query.getCreateTimeStart() != null,CorpusPublishedEntity::getCreateTime,query.getCreateTimeStart())
                    .le(query.getCreateTimeEnd() != null,CorpusPublishedEntity::getCreateTime,query.getCreateTimeEnd())

            ):null;
            //DONE 是否按点赞数排序，默认false，并按照正宗数排序
            if(CollUtil.isNotEmpty(matchCorpusReleased)){
                if (query.getIsSortedByLike() != null && query.getIsSortedByLike())
                    matchCorpusReleased.sort(Comparator.comparing(CorpusPublishedEntity::getLikeNumber).thenComparing(CorpusPublishedEntity::getAuthenticNumber));
                else
                    matchCorpusReleased.sort(Comparator.comparing(CorpusPublishedEntity::getAuthenticNumber).thenComparing(CorpusPublishedEntity::getLikeNumber));
            }
            Set<Integer> corpusIdsReleased = CollUtil.isEmpty(matchCorpusReleased) ? Set.of() : matchCorpusReleased.stream().map(CorpusPublishedEntity::getCorpusId).collect(Collectors.toSet());
//            System.out.println("idsFromRedis: \n"+corpusIdsReleased);
            //从数据库中读取出除已发布和已删除的其他状态的实体
            matchCorpusOthers = list(Wrappers.<CorpusEntity>lambdaQuery()
                    .ne(CorpusEntity::getStatus, "DELETED")
                    .ne(CorpusEntity::getStatus, "RELEASED")
                    .notIn(CollUtil.isNotEmpty(corpusIdsReleased), CorpusEntity::getId, corpusIdsReleased)
                    .eq(query.getId() != null, CorpusEntity::getId, query.getId())
                    .like(!StrUtil.isBlank(query.getName()), CorpusEntity::getName, query.getName())
                    .eq(!StrUtil.isBlank(query.getFaction()), CorpusEntity::getFaction, query.getFaction())
                    .ge(query.getAgeStart() != null, CorpusEntity::getAge, query.getAgeStart())
                    .le(query.getAgeEnd() != null, CorpusEntity::getAge, query.getAgeEnd())
                    .eq(!StrUtil.isBlank(query.getSex()), CorpusEntity::getSex, query.getSex())
                    .in(!CollUtil.isEmpty(educationLevels), CorpusEntity::getEducationLevel, educationLevels)
                    .like(!StrUtil.isBlank(query.getVocation()), CorpusEntity::getVocation, query.getVocation())
                    .eq(query.getSubmitterId() != null, CorpusEntity::getSubmitterId, query.getSubmitterId())
                    .eq(!StrUtil.isBlank(query.getStatus()), CorpusEntity::getStatus, query.getStatus())
                    .ge(query.getCreateTimeStart() != null, CorpusEntity::getCreateTime, query.getCreateTimeStart())
                    .le(query.getCreateTimeEnd() != null, CorpusEntity::getCreateTime, query.getCreateTimeEnd()));
            System.out.println("fromDB: \n"+matchCorpusOthers);
        }
        else if(query.getMode()==2)
        {
            matchCorpusOthers = list(Wrappers.<CorpusEntity>lambdaQuery()
                    .ne(CorpusEntity::getStatus, "DELETED")
                    .ne(CorpusEntity::getStatus, "RELEASED")
                    .ne(CorpusEntity::getStatus,"DRAFT")
                    .eq(query.getId() != null, CorpusEntity::getId, query.getId())
                    .like(!StrUtil.isBlank(query.getName()), CorpusEntity::getName, query.getName())
                    .eq(!StrUtil.isBlank(query.getFaction()), CorpusEntity::getFaction, query.getFaction())
                    .ge(query.getAgeStart() != null, CorpusEntity::getAge, query.getAgeStart())
                    .le(query.getAgeEnd() != null, CorpusEntity::getAge, query.getAgeEnd())
                    .eq(!StrUtil.isBlank(query.getSex()), CorpusEntity::getSex, query.getSex())
                    .in(!CollUtil.isEmpty(educationLevels), CorpusEntity::getEducationLevel, educationLevels)
                    .like(!StrUtil.isBlank(query.getVocation()), CorpusEntity::getVocation, query.getVocation())
                    .eq(query.getSubmitterId() != null, CorpusEntity::getSubmitterId, query.getSubmitterId())
                    .eq(!StrUtil.isBlank(query.getStatus()), CorpusEntity::getStatus, query.getStatus())
                    .ge(query.getCreateTimeStart() != null, CorpusEntity::getCreateTime, query.getCreateTimeStart())
                    .le(query.getCreateTimeEnd() != null, CorpusEntity::getCreateTime, query.getCreateTimeEnd()));
            Set<Integer> corpusIdsReleased = CollUtil.isEmpty(matchCorpusOthers) ? Set.of() : matchCorpusOthers.stream().map(CorpusEntity::getId).collect(Collectors.toSet());
//            matchCorpusReleased = StrUtil.isBlank(query.getStatus()) || query.getStatus().equals("RELEASED") ? corpusReleased.stream()
//                    .filter(e -> query.getId() == null || query.getId().equals(e.getId()))
//                    .filter(e->CollUtil.isEmpty(corpusIdsReleased)||!corpusIdsReleased.contains(e.getId()))
//                    .filter(e -> CommUtils.like(e.getName(), query.getName()))
//                    .filter(e -> StrUtil.isBlank(query.getFaction()) || query.getFaction().equals(e.getFaction()))
//                    .filter(e -> query.getAgeStart() == null || e.getAge() >= query.getAgeStart())
//                    .filter(e -> query.getAgeEnd() == null || e.getAge() <= query.getAgeEnd())
//                    .filter(e -> StrUtil.isBlank(query.getSex()) || e.getSex().equals(query.getSex()))
//                    .filter(e -> CollUtil.isEmpty(educationLevels) || educationLevels.contains(e.getEducationLevel()))
//                    .filter(e -> CommUtils.like(e.getVocation(), query.getVocation()))
//                    .filter(e -> query.getSubmitterId() == null || query.getSubmitterId().equals(e.getSubmitterId()))
//                    .filter(e -> "RELEASED".equals(e.getStatus()))
//                    .filter(e -> query.getCreateTimeStart() == null || !e.getCreateTime().isBefore(query.getCreateTimeStart()))
//                    .filter(e -> query.getCreateTimeEnd() == null || !e.getCreateTime().isAfter(query.getCreateTimeEnd())).toList() : null;
            matchCorpusReleased=StrUtil.isBlank(query.getStatus()) || query.getStatus().equals("RELEASED") ?corpusPublishedMapper.selectList(Wrappers.<CorpusPublishedEntity>lambdaQuery()
                    .eq(query.getId()!=null,CorpusPublishedEntity::getCorpusId,query.getId())
                    .notIn(!CollUtil.isEmpty(corpusIdsReleased),CorpusPublishedEntity::getCorpusId,corpusIdsReleased)
                    .like(!StrUtil.isBlank(query.getName()),CorpusPublishedEntity::getName,query.getName())
                    .eq(!StrUtil.isBlank(query.getFaction()),CorpusPublishedEntity::getFaction,query.getFaction())
                    .ge(query.getAgeStart() != null,CorpusPublishedEntity::getAge,query.getAgeStart())
                    .le(query.getAgeEnd() != null,CorpusPublishedEntity::getAge,query.getAgeEnd())
                    .eq(!StrUtil.isBlank(query.getSex()),CorpusPublishedEntity::getSex,query.getSex())
                    .in(!CollUtil.isEmpty(educationLevels),CorpusPublishedEntity::getEducationLevel,query.getEducationLevels())
                    .like(!StrUtil.isBlank(query.getVocation()),CorpusPublishedEntity::getVocation,query.getVocation())
                    .eq(query.getSubmitterId() != null,CorpusPublishedEntity::getSubmitterId,query.getSubmitterId())
                    .eq(CorpusPublishedEntity::getStatus,"RELEASED")
                    .ge(query.getCreateTimeStart() != null,CorpusPublishedEntity::getCreateTime,query.getCreateTimeStart())
                    .le(query.getCreateTimeEnd() != null,CorpusPublishedEntity::getCreateTime,query.getCreateTimeEnd())

            ):null;
            if(CollUtil.isNotEmpty(matchCorpusReleased)){
                if (query.getIsSortedByLike() != null && query.getIsSortedByLike())
                    matchCorpusReleased.sort(Comparator.comparing(CorpusPublishedEntity::getLikeNumber).thenComparing(CorpusPublishedEntity::getAuthenticNumber));
                else
                    matchCorpusReleased.sort(Comparator.comparing(CorpusPublishedEntity::getAuthenticNumber).thenComparing(CorpusPublishedEntity::getLikeNumber));
            }
        }
        else
        {
            matchCorpusOthers = list(Wrappers.<CorpusEntity>lambdaQuery()
                    .ne(CorpusEntity::getStatus, "DELETED")
                    .eq(query.getId() != null, CorpusEntity::getId, query.getId())
                    .like(!StrUtil.isBlank(query.getName()), CorpusEntity::getName, query.getName())
                    .eq(!StrUtil.isBlank(query.getFaction()), CorpusEntity::getFaction, query.getFaction())
                    .ge(query.getAgeStart() != null, CorpusEntity::getAge, query.getAgeStart())
                    .le(query.getAgeEnd() != null, CorpusEntity::getAge, query.getAgeEnd())
                    .eq(!StrUtil.isBlank(query.getSex()), CorpusEntity::getSex, query.getSex())
                    .in(!CollUtil.isEmpty(educationLevels), CorpusEntity::getEducationLevel, educationLevels)
                    .like(!StrUtil.isBlank(query.getVocation()), CorpusEntity::getVocation, query.getVocation())
                    .eq(query.getSubmitterId() != null, CorpusEntity::getSubmitterId, query.getSubmitterId())
                    .eq(!StrUtil.isBlank(query.getStatus()), CorpusEntity::getStatus, query.getStatus())
                    .ge(query.getCreateTimeStart() != null, CorpusEntity::getCreateTime, query.getCreateTimeStart())
                    .le(query.getCreateTimeEnd() != null, CorpusEntity::getCreateTime, query.getCreateTimeEnd()));
        }

        //合并两者，并开始装配VO
        List<CorpusEntity> matchCorpusAll = new ArrayList<>(matchCorpusOthers);
        if (matchCorpusReleased != null) {
            List<CorpusEntity>matchCorpusReleasedNormal=new ArrayList<>();
            for(CorpusPublishedEntity corpusPublished:matchCorpusReleased)
            {
                CorpusEntity corpus=new CorpusEntity();
                corpus.setId(corpusPublished.getCorpusId());
                corpus.setName(corpusPublished.getName());
                corpus.setRemark(corpusPublished.getRemark());
                corpus.setAge(corpusPublished.getAge());
                corpus.setSex(corpusPublished.getSex());
                corpus.setVocation(corpusPublished.getVocation());
                corpus.setFaction(corpusPublished.getFaction());
                corpus.setIntroduction(corpusPublished.getIntroduction());
                corpus.setStatus(corpusPublished.getStatus());
                corpus.setSubmitterId(corpusPublished.getSubmitterId());
                corpus.setLikeNumber(corpusPublished.getLikeNumber());
                corpus.setCreateTime(corpusPublished.getCreateTime());
                corpus.setPhonologyExplanation(corpusPublished.getPhonologyExplanation());
                corpus.setOriginalPlace(corpusPublished.getOriginalPlace());
                corpus.setPermanentPlace(corpusPublished.getPermanentPlace());
                corpus.setLiveOutside(corpusPublished.getLiveOutside());
                corpus.setAffection(corpusPublished.getAffection());
                corpus.setDialectPointId(corpusPublished.getDialectPointId());
                corpus.setEducationLevel(corpusPublished.getEducationLevel());
                corpus.setAuthenticNumber(corpusPublished.getAuthenticNumber());
                matchCorpusReleasedNormal.add(corpus);
            }
            matchCorpusAll.addAll(matchCorpusReleasedNormal);
        }
        System.out.println("all: \n"+matchCorpusAll);
//         * 语料编号
//        private Integer id;
//         * 所属方言点
//        private DialectPointVO dialectPoint;
//         * 语料名称
//        private String name;
//         * 所属派别（新派-NEW、中派-MEDIUM、老派-OLD）
//        private String faction;
//         * 所受影响普化(POPULARIZATION)、其他方言影响(AFFECTED_BY_OTHER_DIALECT)、NONE）
//        private String affection;
//         * 发音人年龄
//        private Integer age;
//         * 发音人性别
//        private String sex;
//         * 发音人文化程度
//        private String educationLevel;
//         * 发音人是否长期居于外地
//        private Boolean liveOutside;
//         * 发音人常住地
//        private String permanentPlace;
//         * 发音人职业
//        private String vocation;
//         * 发音人籍贯
//        private String originalPlace;
//         * 提交人
//        private Long submitterId;
//         * 语料说明
//        private String introduction;
//         * 音系说明
//        private String phonologyExplanation;
//         * 备注
//        private String remark;
//         * 点赞数
//        private Integer likeNumber;
//         * 正宗数
//        private Integer authenticNumber;
//         * 状态（草稿-DRAFT、待审核-PENDING、已发布-RELEASED、已打回-REJECTED、已删除-DELETED）。
//        private String status;
//         * 创建时间
//        private LocalDateTime createTime;
        List<CorpusVO> matchCorpusVOs = new ArrayList<>();
        for (CorpusEntity corpusEntity : matchCorpusAll) {
            CorpusVO corpusVO = new CorpusVO();
            corpusVO.setId(corpusEntity.getId());
            //装填 private DialectPointVO dialectPoint;
            DialectPointEntity dialectPoint = dialectPointService.getById(corpusEntity.getDialectPointId());
            System.out.println("corpusEntity.getId(): "+corpusEntity.getDialectPointId());
            if ("DELETED".equals(dialectPoint.getStatus()))
                corpusVO.setDialectPoint(null);
            else {
                List<DialectPointEntity> dialectPointEntityList = new ArrayList<>();
                dialectPointEntityList.add(dialectPoint);
                DialectPointVO dialectPointVO = dialectPointService.fillDialectPointVO(dialectPointEntityList).get(0);
                corpusVO.setDialectPoint(dialectPointVO);
            }
            corpusVO.setName(corpusEntity.getName());
            corpusVO.setFaction(corpusEntity.getFaction());
            corpusVO.setAffection(corpusEntity.getAffection());
            corpusVO.setAge(corpusEntity.getAge());
            corpusVO.setSex(corpusEntity.getSex());
            corpusVO.setEducationLevel(corpusEntity.getEducationLevel());
            corpusVO.setLiveOutside(corpusEntity.getLiveOutside());
            corpusVO.setPermanentPlace(corpusEntity.getPermanentPlace());
            corpusVO.setVocation(corpusEntity.getVocation());
            corpusVO.setOriginalPlace(corpusEntity.getOriginalPlace());
            corpusVO.setSubmitterId(corpusEntity.getSubmitterId());
            corpusVO.setIntroduction(corpusEntity.getIntroduction());
            corpusVO.setPhonologyExplanation(corpusEntity.getPhonologyExplanation());
            corpusVO.setRemark(corpusEntity.getRemark());
            corpusVO.setLikeNumber(corpusEntity.getLikeNumber());
            corpusVO.setAuthenticNumber(corpusEntity.getAuthenticNumber());
            corpusVO.setStatus(corpusEntity.getStatus());
            corpusVO.setCreateTime(corpusEntity.getCreateTime());
            matchCorpusVOs.add(corpusVO);
        }
        System.out.println("VOs: \n"+matchCorpusVOs);
        return matchCorpusVOs;
    }
    public IPage<CorpusVO>getCorpusVOsPage(Page page,CorpusQueryDTO query)
    {
        List<CorpusVO>matchCorpusVOs=getCorpusVOs(query);
        return CommUtils.List2IPage(page,matchCorpusVOs);
    }
//     * 所属方言点
//    private Integer dialectPointId;
//     * 语料名称
//    private String name;
//     * 所属派别（新派-NEW、中派-MEDIUM、老派-OLD）
//    private String faction;
//     * 所受影响普化(POPULARIZATION)、其他方言影响(AFFECTED_BY_OTHER_DIALECT)、NONE）
//    private String affection;
//     * 发音人年龄
//    private Integer age;
//     * 发音人性别
//    private String sex;
//     * 发音人文化程度
//    private String educationLevel;
//     * 发音人是否长期居于外地
//    private Boolean liveOutside;
//     * 发音人常住地
//    private String permanentPlace;
//     * 发音人职业
//    private String vocation;
//     * 发音人籍贯
//    private String originalPlace;
//     * 语料说明
//    private String introduction;
//     * 音系说明
//    private String phonologyExplanation;
//     * 备注
//    private String remark;
    public R<Boolean> addCorpus(CorpusSaveDTO corpusSave)
    {
        CorpusEntity corpusEntity=new CorpusEntity();
        corpusEntity.setDialectPointId(corpusSave.getDialectPointId());
        corpusEntity.setName(corpusSave.getName());
        corpusEntity.setFaction(corpusSave.getFaction());
        corpusEntity.setAffection(corpusSave.getAffection());
        corpusEntity.setAge(corpusSave.getAge());
        corpusEntity.setSex(corpusSave.getSex());
        corpusEntity.setEducationLevel(corpusSave.getEducationLevel());
        corpusEntity.setLiveOutside(corpusSave.getLiveOutside());
        corpusEntity.setPermanentPlace(corpusSave.getPermanentPlace());
        corpusEntity.setVocation(corpusSave.getVocation());
        corpusEntity.setOriginalPlace(corpusSave.getOriginalPlace());
        corpusEntity.setIntroduction(corpusSave.getIntroduction());
        corpusEntity.setPhonologyExplanation(corpusSave.getPhonologyExplanation());
        corpusEntity.setRemark(corpusSave.getRemark());
//        corpusEntity.setLikeNumber(0);
//        corpusEntity.setAuthenticNumber(0.0);
        corpusEntity.setSubmitterId(SecurityUtils.getUser().getId());
        corpusEntity.setStatus("DRAFT");
        if(save(corpusEntity))
            return R.ok(true);
        return R.ok(false).setMsg("添加失败！");
    }
    /**
     * 逻辑删除语料。不用校验是否被引用，只询问是否删除（前端询问），语料下的字、词、句、会话一并直接标记为isDelete=true
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteCorpus(Integer[]ids)
    {
        if(ids==null||ids.length==0)
            return R.failed(false).setMsg("请选择要删除的语料！");
        //先找到引用这些语料的字、词、句、会话
        try {
            corpusCharacterMapper.update(Wrappers.<CorpusCharacterEntity>lambdaUpdate()
                    .set(CorpusCharacterEntity::getIsDeleted, true)
                    .in(CorpusCharacterEntity::getCorpusId, CollUtil.toList(ids))
                    .eq(CorpusCharacterEntity::getIsDeleted, false));
            corpusWordMapper.update(Wrappers.<CorpusWordEntity>lambdaUpdate()
                    .set(CorpusWordEntity::getIsDeleted, true)
                    .in(CorpusWordEntity::getCorpusId, CollUtil.toList(ids))
                    .eq(CorpusWordEntity::getIsDeleted, false));
            corpusSentenceMapper.update(Wrappers.<CorpusSentenceEntity>lambdaUpdate()
                    .set(CorpusSentenceEntity::getIsDeleted, true)
                    .in(CorpusSentenceEntity::getCorpusId, CollUtil.toList(ids))
                    .eq(CorpusSentenceEntity::getIsDeleted, false));
            freeSpeechMapper.update(Wrappers.<FreeSpeechEntity>lambdaUpdate()
                    .set(FreeSpeechEntity::getIsDeleted, false)
                    .in(FreeSpeechEntity::getCorpusId, CollUtil.toList(ids))
                    .eq(FreeSpeechEntity::getIsDeleted, false));
            update(Wrappers.<CorpusEntity>lambdaUpdate()
                    .set(CorpusEntity::getStatus, "DELETED")
                    .in(CorpusEntity::getId, CollUtil.toList(ids))
                    .ne(CorpusEntity::getStatus, "DELETED"));
            //DONE 同步删除发布表
            corpusPublishedMapper.delete(Wrappers.<CorpusPublishedEntity>lambdaQuery()
                    .in(CorpusPublishedEntity::getCorpusId,CollUtil.toList(ids)));
        }
        catch (Exception e)
        {
            throw new RuntimeException("删除失败！");
        }
        return R.ok(true);
    }
    /**
     * 用户发布：不是真的发布，是用户将状态设置为待审核，并提交到管理员处待审
     */
    public R<Boolean> releaseCorpus(Integer id)
    {
        if(update(Wrappers.<CorpusEntity>lambdaUpdate()
                .set(CorpusEntity::getStatus,"PENDING")
                .eq(CorpusEntity::getId,id)
                .in(CorpusEntity::getStatus,"DRAFT","REJECTED")))
            return R.ok(true).setMsg("发布成功，已提交至管理员审核！");
        return R.failed(false).setMsg("发布失败！");
    }
    /**
     * 管理员审核，注意这是事务，一旦出错，全部回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> reviewCorpus(Integer id,Boolean isPassed)
    {
        //如果审核通过，一定要更新已发布表
        if(isPassed)
        {
            boolean updateDB=update(Wrappers.<CorpusEntity>lambdaUpdate()
                    .set(CorpusEntity::getStatus,"RELEASED")
                    .eq(CorpusEntity::getStatus,"PENDING")
                    .eq(CorpusEntity::getId,id));
            if(!updateDB)
                return R.failed(false).setMsg("这篇语料已被审核或还未被用户提交！");
//            RedisUtils.delete(CORPUS_RELEASED_KEY);
//            List<CorpusEntity>releasedCorpusEntities=list(Wrappers.<CorpusEntity>lambdaQuery()
//                    .eq(CorpusEntity::getStatus,"RELEASED"));
//            boolean updateRedis=RedisUtils.set(CORPUS_RELEASED_KEY,releasedCorpusEntities);
            boolean isReleased=corpusPublishedMapper.exists(Wrappers.<CorpusPublishedEntity>lambdaQuery()
                    .eq(CorpusPublishedEntity::getCorpusId,id)
                    .eq(CorpusPublishedEntity::getStatus,"RELEASED"));
            if(isReleased)
            {
                //DONE 将工作表中的数据更新发布表，先要找到CorpusPublished中的对应id，再找到工作表CorpusEntity，装填CorpusEntity.id
                // 为CorpusPublishedEntity.corpusId，CorpusPublishedEntity.id装填为id，再装填其余字段，updateById即可
                CorpusPublishedEntity corpusPublished=corpusPublishedMapper.selectOne(Wrappers.<CorpusPublishedEntity>lambdaQuery()
                        .eq(CorpusPublishedEntity::getCorpusId,id)
                        .eq(CorpusPublishedEntity::getStatus,"RELEASED"));
                CorpusEntity corpus=getOne(Wrappers.<CorpusEntity>lambdaQuery()
                        .eq(CorpusEntity::getId,id)
                        .eq(CorpusEntity::getStatus,"RELEASED"));
                fillCorpusPublished(corpus, corpusPublished);
                //已发布用户编辑后重新发布LikeNumber、AuthenticNumber不可从工作表中读取，要保留原有数据
                corpusPublished.setLikeNumber(null);
                corpusPublished.setAuthenticNumber(null);
                try {
                    if(corpusPublishedMapper.updateById(corpusPublished)==0)
                        throw new RuntimeException("发布失败！");
                }catch (Exception e) {
                    throw new RuntimeException("发布失败！");
                }
            }
            else
            {
                //DONE 向发布表中插入一条工作表的拷贝
                CorpusEntity corpus=getOne(Wrappers.<CorpusEntity>lambdaQuery()
                        .eq(CorpusEntity::getId,id)
                        .eq(CorpusEntity::getStatus,"RELEASED"));
                CorpusPublishedEntity corpusPublished=new CorpusPublishedEntity();
                corpusPublished.setCorpusId(id);
                fillCorpusPublished(corpus, corpusPublished);
                corpusPublished.setLikeNumber(0);
                corpusPublished.setAuthenticNumber(0.0);
                corpusPublished.setScoreNum(0);
                corpusPublished.setScoreTotal(0.0);
                try{
                    if(corpusPublishedMapper.insert(corpusPublished)==0)
                        throw new RuntimeException("发布失败！");
                }catch (Exception e)
                {
                    throw new RuntimeException("发布失败！");
                }
            }
            return R.ok(true);
        }
        else //如果审核不通过（即打回），就要区别是已发布打回还是待审核打回
        {
            CorpusEntity corpus = getOne(Wrappers.<CorpusEntity>lambdaQuery()
                    .eq(CorpusEntity::getId, id)
                    .ne(CorpusEntity::getStatus, "DELETED"));
            if(corpus==null)
                return R.failed(false).setMsg("要删除的语料不存在或已被删除");
            boolean updateDB = update(Wrappers.<CorpusEntity>lambdaUpdate()
                    .set(CorpusEntity::getStatus, "REJECTED")
                    .in(CorpusEntity::getStatus, "PENDING", "RELEASED")
                    .eq(CorpusEntity::getId, id));
            //TODO 如果用户将发布的语料撤回为草稿再提交审核，那么发布表里是RELEASED,但工作表里是PENDING,于是会走到else，不删发布表
            // 正确做法应该是改为查看发布表里存不存在此语料，存在的话就得删掉（因为管理员打回就意味着下架，一定不能再可以被展示了）
            if (corpusPublishedMapper.exists(Wrappers.<CorpusPublishedEntity>lambdaQuery()
                    .eq(CorpusPublishedEntity::getCorpusId,id)
                    .eq(CorpusPublishedEntity::getStatus,"RELEASED")))//如果是已发布的语料要打回，按一定要更新缓存
            {
//                RedisUtils.delete(CORPUS_RELEASED_KEY);
//                List<CorpusEntity> releasedCorpusEntities = list(Wrappers.<CorpusEntity>lambdaQuery()
//                        .eq(CorpusEntity::getStatus, "RELEASED"));
//                boolean updateRedis = RedisUtils.set(CORPUS_RELEASED_KEY, releasedCorpusEntities);
//                if (updateRedis && updateDB)
//                    return R.ok(true);
                try {
                    if (corpusPublishedMapper.delete(Wrappers.<CorpusPublishedEntity>lambdaQuery()
                            .eq(CorpusPublishedEntity::getCorpusId, id)
                            .eq(CorpusPublishedEntity::getStatus, "RELEASED")) == 0) {
                        throw new RuntimeException("打回失败！");
                    }
                }catch (Exception e)
                {
                    throw new RuntimeException("打回失败！");
                }
                return R.ok(true);
            } else {
                if (updateDB)
                    return R.ok(true);
            }
            return R.failed(false).setMsg("打回失败！");
        }
    }

    private void fillCorpusPublished(CorpusEntity corpus, CorpusPublishedEntity corpusPublished) {
        corpusPublished.setName(corpus.getName());
        corpusPublished.setRemark(corpus.getRemark());
        corpusPublished.setAge(corpus.getAge());
        corpusPublished.setSex(corpus.getSex());
        corpusPublished.setVocation(corpus.getVocation());
        corpusPublished.setFaction(corpus.getFaction());
        corpusPublished.setIntroduction(corpus.getIntroduction());
        corpusPublished.setStatus(corpus.getStatus());
        corpusPublished.setSubmitterId(corpus.getSubmitterId());
//        corpusPublished.setLikeNumber(corpus.getLikeNumber());
        corpusPublished.setCreateTime(corpus.getCreateTime());
        corpusPublished.setPhonologyExplanation(corpus.getPhonologyExplanation());
        corpusPublished.setOriginalPlace(corpus.getOriginalPlace());
        corpusPublished.setPermanentPlace(corpus.getPermanentPlace());
        corpusPublished.setLiveOutside(corpus.getLiveOutside());
        corpusPublished.setAffection(corpus.getAffection());
        corpusPublished.setDialectPointId(corpus.getDialectPointId());
        corpusPublished.setEducationLevel(corpus.getEducationLevel());
//        corpusPublished.setAuthenticNumber(corpus.getAuthenticNumber());
    }

    /**
     * 用户将数据库语料状态设置为草稿
     */
    public R<Boolean>withdrawCorpus(Integer id)
    {
        //撤回不用管缓存，原有的版本应该要继续发布。新版本重新发布被管理员审核之后覆盖旧缓存
        boolean updateDB=update(Wrappers.<CorpusEntity>lambdaUpdate()
                .set(CorpusEntity::getStatus,"DRAFT")
                .eq(CorpusEntity::getId,id)
                .eq(CorpusEntity::getStatus,"RELEASED"));
        if(updateDB)
            return R.ok(true);
        return R.failed(false).setMsg("修改失败！");
    }
    /**
     * 修改语料基本信息
     */
    public R<Boolean>updateCorpus(CorpusEntity corpus)
    {
        //我们修改语料的时候只能修改语料的基本信息，绝不能修改：语料编号、提交人、创建时间、点赞数、正宗数、状态
        //状态的修改应该提供专门的接口，并严格按状态自动机变更状态
        //不用管缓存，因为已发布的语料不可编辑，必须将其打回或者撤回之后才可编辑。
        CorpusEntity matchCorpus=getOne(Wrappers.<CorpusEntity>lambdaQuery()
                .eq(CorpusEntity::getId,corpus.getId())
                .ne(CorpusEntity::getStatus,"DELETED")
                .ne(CorpusEntity::getStatus,"RELEASED"));
        if (matchCorpus==null)
            return R.failed(false).setMsg("更新失败！语料不存在、已删除或者已发布");
        corpus.setSubmitterId(null);
        corpus.setCreateTime(null);
//        corpus.setLikeNumber(null);
//        corpus.setAuthenticNumber(null);
        corpus.setStatus(null);
        if(updateById(corpus))
            return R.ok(true);
        return R.failed(false).setMsg("修改失败！");
    }
    @Transactional(rollbackFor = Exception.class)
    public R likeCorpus(Integer id)
    {
        CorpusEntity corpus = getOne(Wrappers.<CorpusEntity>lambdaQuery()
                .eq(CorpusEntity::getId, id).eq(CorpusEntity::getStatus, "RELEASED"));
        if (corpus == null) return R.failed().setMsg("该语料不可点赞");
        ObjectLikeEntity objectLike=new ObjectLikeEntity();
        objectLike.setObjectType("CORPUS");
        objectLike.setObjectId(id);
        objectLike.setUserId(SecurityUtils.getUser().getId());
        try{
            objectLikeMapper.insert(objectLike);
        }
        catch (DuplicateKeyException e)
        {
            return R.failed().setMsg("您已经点过赞了！");
        }
        if(corpusPublishedMapper.update(Wrappers.<CorpusPublishedEntity>lambdaUpdate()
                .setIncrBy(CorpusPublishedEntity::getLikeNumber,1)
                .eq(CorpusPublishedEntity::getStatus,"RELEASED")
                .eq(CorpusPublishedEntity::getCorpusId,id))!=0//DONE 其实这里应该只更新发布表的，工作表不管点赞数正宗数，这俩数据根据发布表里的已发布数据为准
        )
            return R.ok(true);
        throw new RuntimeException("点赞失败！");
    }

    @Transactional(rollbackFor = Exception.class)
    public R unlikeCorpus(Integer id)
    {
        CorpusEntity corpus = getOne(Wrappers.<CorpusEntity>lambdaQuery()
                .eq(CorpusEntity::getId, id).eq(CorpusEntity::getStatus, "RELEASED"));
        if (corpus == null) return R.failed().setMsg("该语料不可取消点赞");

        if(objectLikeMapper.delete(Wrappers.<ObjectLikeEntity>lambdaQuery()
                    .eq(ObjectLikeEntity::getObjectType,"CORPUS")
                    .eq(ObjectLikeEntity::getObjectId,id)
                    .eq(ObjectLikeEntity::getUserId,SecurityUtils.getUser().getId()))!=0)
        {
            if(corpusPublishedMapper.update(Wrappers.<CorpusPublishedEntity>lambdaUpdate()
                    .setDecrBy(CorpusPublishedEntity::getLikeNumber, 1)
                    .eq(CorpusPublishedEntity::getCorpusId, id)
                    .gt(CorpusPublishedEntity::getLikeNumber, 0)
                    .eq(CorpusPublishedEntity::getStatus,"RELEASED"))!=0)//DONE 其实这里应该只更新发布表的，工作表不管点赞数正宗数，这俩数据根据发布表里的已发布数据为准
                return R.ok(true);
            throw new RuntimeException("取消失败！");
        }
        //else是delete什么都没有改，那就不需要回滚，故直接return
        return R.failed().setMsg("您还没有点过赞！");
    }
    @Transactional(rollbackFor = Exception.class)
    public R scoreCorpus(Integer id,Double score)
    {
        if(id==null)
            return R.failed(false).setMsg("请选择你要打分的语料");
        if(score==null||score<0||score>5)
           return R.failed(false).setMsg("正宗程度只能在0~5之间！");
        CorpusEntity corpus = getOne(Wrappers.<CorpusEntity>lambdaQuery()
                .eq(CorpusEntity::getId, id).eq(CorpusEntity::getStatus, "RELEASED"));
        if(corpus==null)
            return R.failed(false).setMsg("该语料不可打分！");
        CorpusAuthenticEntity corpusAuthenticEntity=new CorpusAuthenticEntity();
        corpusAuthenticEntity.setCorpusId(id);
        corpusAuthenticEntity.setScore(score);
        corpusAuthenticEntity.setUserId(SecurityUtils.getUser().getId());
        try{
            corpusAuthenticMapper.insert(corpusAuthenticEntity);
        }
        catch (DuplicateKeyException e)
        {
            return R.failed().setMsg("您已经打过分了！");
        }
        if(corpusPublishedMapper.update(null, Wrappers.<CorpusPublishedEntity>lambdaUpdate()
            .setSql("score_total = score_total + {0}, score_num = score_num + 1, "
                    + "authentic_number = ROUND(score_total / score_num, 2)", score)
            .eq(CorpusPublishedEntity::getCorpusId, id))!=0//DONE 其实这里应该只更新发布表的，工作表不管点赞数正宗数，这俩数据根据发布表里的已发布数据为准
        )
            return R.ok(true);
        throw new RuntimeException("打分失败！");
    }
}
