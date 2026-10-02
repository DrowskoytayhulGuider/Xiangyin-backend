package com.sleepywang.xiangyin.dialect.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.dialect.entity.ListSentenceEntity;
import com.sleepywang.xiangyin.dialect.entity.ListSentenceEntity;
import com.sleepywang.xiangyin.dialect.entity.SentenceListEntity;
import com.sleepywang.xiangyin.dialect.mapper.ListSentenceMapper;
import com.sleepywang.xiangyin.dialect.mapper.SentenceListMapper;
import com.sleepywang.xiangyin.dialect.service.ListSentenceService;
import com.sleepywang.xiangyin.dialect.vo.ListSentenceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 句表短句
 *
 * @author sleepywang
 * @date 2026-09-19 20:24:36
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ListSentenceServiceImpl extends ServiceImpl<ListSentenceMapper, ListSentenceEntity> implements ListSentenceService {
    private final SentenceListMapper sentenceListMapper;

    public IPage<ListSentenceVO> getListSentencePage(Page page, ListSentenceEntity listSentenceQuery)
    {
        listSentenceQuery.setIsDeleted(false);
//        listSentenceQuery.setMeaning(null);
        listSentenceQuery.setRemark(null);
        List<ListSentenceEntity> matchListSentence=list(Wrappers.<ListSentenceEntity>lambdaQuery()
                .like(!StrUtil.isBlank(listSentenceQuery.getStandardForm()),ListSentenceEntity::getStandardForm,listSentenceQuery.getStandardForm())
                .eq(listSentenceQuery.getSentenceListId()!=null,ListSentenceEntity::getSentenceListId,listSentenceQuery.getSentenceListId())
                .eq(ListSentenceEntity::getIsDeleted,false));
        SentenceListEntity baseSentenceList=new SentenceListEntity();
        baseSentenceList.setId(1);
        baseSentenceList.setName("基础句表");
        baseSentenceList.setStatus("RELEASED");
        baseSentenceList.setMakerId(1L);
        Map<Integer,SentenceListEntity> idSentenceList=new HashMap<>();
//        if(listSentenceQuery.getSentenceListId()!=null)
//        {
//            //单查一个
//            List<SentenceListEntity>matchSentenceListEntity=sentenceListMapper.selectList(Wrappers.<SentenceListEntity>lambdaQuery()
//                    .eq(SentenceListEntity::getId,listSentenceQuery.getSentenceListId())
//                    .ne(SentenceListEntity::getStatus,"DELETED"));
//            idSentenceList.put(listSentenceQuery.getSentenceListId(),matchSentenceListEntity.isEmpty()?null:matchSentenceListEntity.get(0));
//        }
//        else
//        {
        Set<Integer> matchSentenceListIds = matchListSentence.stream()
                .map(ListSentenceEntity::getSentenceListId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        idSentenceList=matchSentenceListIds.isEmpty()?Map.of():sentenceListMapper.selectList(Wrappers.<SentenceListEntity>lambdaQuery()
                        .in(SentenceListEntity::getId,matchSentenceListIds)
                        .ne(SentenceListEntity::getStatus,"DELETED"))
                .stream().collect(Collectors.toMap(SentenceListEntity::getId, Function.identity()));
//        }
        List<ListSentenceVO> matchListSentenceVOs=new ArrayList<>();
        for(ListSentenceEntity listSentence:matchListSentence)
        {

            ListSentenceVO listSentenceVO=new ListSentenceVO();
            listSentenceVO.setId(listSentence.getId());
//            listSentenceVO.setMeaning(listSentence.getMeaning());
            listSentenceVO.setRemark(listSentence.getRemark());
            listSentenceVO.setStandardForm(listSentence.getStandardForm());
//            listSentenceVO.setSpeechPart(listSentence.getSpeechPart());
//            listWordVO.setGuangyunTone(listWord.getGuangyunTone());
//            listWordVO.setWord(listWord.getWord());
//            listWordVO.setGuangyunInitial(idInitial.get(listWord.getGuangyunInitialId()));
//            listWordVO.setGuangyunRhyme(idRhyme.get(listWord.getGuangyunRhymeId()));
            if(listSentenceQuery.getSentenceListId()==1)
                listSentenceVO.setSentenceList(baseSentenceList);
            else
                listSentenceVO.setSentenceList(idSentenceList.get(listSentence.getSentenceListId()));
            matchListSentenceVOs.add(listSentenceVO);
        }
        long current=Math.max(1L,page.getCurrent());
        long size= Math.min(page.getSize()>0? page.getSize() : 10L, 100L);
        int total=matchListSentence.size();
        int from = (int) Math.min((current - 1) * size, total);
        int to = (int) Math.min(from + size, total);
        Page<ListSentenceVO>listSentenceVOPage=new Page<>(current,size,total);
        listSentenceVOPage.setRecords(new ArrayList<>(matchListSentenceVOs.subList(from,to)));
        return listSentenceVOPage;
    }
}
