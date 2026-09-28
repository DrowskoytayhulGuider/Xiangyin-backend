package com.sleepywang.xiangyin.dialect.service.impl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.sleepywang.xiangyin.dialect.entity.CharacterListEntity;
//import com.sleepywang.xiangyin.dialect.entity.ListCharacterEntity;
import com.sleepywang.xiangyin.dialect.entity.ListWordEntity;
import com.sleepywang.xiangyin.dialect.entity.WordListEntity;
import com.sleepywang.xiangyin.dialect.mapper.ListWordMapper;
import com.sleepywang.xiangyin.dialect.mapper.WordListMapper;
import com.sleepywang.xiangyin.dialect.service.ListWordService;
//import com.sleepywang.xiangyin.dialect.vo.ListCharacterVO;
import com.sleepywang.xiangyin.dialect.vo.ListWordVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 词表词
 *
 * @author sleepywang
 * @date 2026-09-19 20:30:35
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ListWordServiceImpl extends ServiceImpl<ListWordMapper, ListWordEntity> implements ListWordService {
    private final WordListMapper wordListMapper;
    /*
     * 词汇编号
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="词汇编号")
    private Integer id;
     * 所属词表
    @Schema(description="所属词表")
    private Integer wordListId;
     * 普通话标准词形
    @Schema(description="普通话标准词形")
    private String standardForm;
     * 词性
    @Schema(description="词性")
    private String speechPart;
     */
    public IPage<ListWordVO> getListWordPage(Page page, ListWordEntity listWordQuery)
    {
        listWordQuery.setIsDeleted(false);
        listWordQuery.setMeaning(null);
        listWordQuery.setRemark(null);
        List<ListWordEntity>matchListWord=list(Wrappers.query(listWordQuery));
        WordListEntity baseWordList=new WordListEntity();
        baseWordList.setId(1);
        baseWordList.setName("基础词表");
        baseWordList.setStatus("RELEASED");
        baseWordList.setMakerId(1L);
        Map<Integer,WordListEntity> idWordList=new HashMap<>();
//        if(listWordQuery.getWordListId()!=null)
//        {
//            //单查一个
//            List<WordListEntity>matchWordListEntity=wordListMapper.selectList(Wrappers.<WordListEntity>lambdaQuery()
//                    .eq(WordListEntity::getId,listWordQuery.getWordListId())
//                    .ne(WordListEntity::getStatus,"DELETED"));
//            idWordList.put(listWordQuery.getWordListId(),matchWordListEntity.isEmpty()?null:matchWordListEntity.get(0));
//        }
//        else
//        {
        Set<Integer> matchWordListIds = matchListWord.stream()
                .map(ListWordEntity::getWordListId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        idWordList=matchWordListIds.isEmpty()?Map.of():wordListMapper.selectList(Wrappers.<WordListEntity>lambdaQuery()
                        .in(WordListEntity::getId,matchWordListIds)
                        .ne(WordListEntity::getStatus,"DELETED"))
                .stream().collect(Collectors.toMap(WordListEntity::getId,Function.identity()));
//        }
        List<ListWordVO> matchListWordVOs=new ArrayList<>();
        for(ListWordEntity listWord:matchListWord)
        {
            /*
             * 词汇编号
            private Integer id;/
             * 所属词表
            private WordListEntity wordList;/
             * 普通话标准词形
            private String standardForm;/
             * 词性
            private String speechPart;/
             * 含义
            private String meaning;/
             * 备注
            private String remark;/
             */
            ListWordVO listWordVO=new ListWordVO();
            listWordVO.setId(listWord.getId());
            listWordVO.setMeaning(listWord.getMeaning());
            listWordVO.setRemark(listWord.getRemark());
            listWordVO.setStandardForm(listWord.getStandardForm());
            listWordVO.setSpeechPart(listWord.getSpeechPart());
//            listWordVO.setGuangyunTone(listWord.getGuangyunTone());
//            listWordVO.setWord(listWord.getWord());
//            listWordVO.setGuangyunInitial(idInitial.get(listWord.getGuangyunInitialId()));
//            listWordVO.setGuangyunRhyme(idRhyme.get(listWord.getGuangyunRhymeId()));
            if(listWordQuery.getWordListId()==1)
                listWordVO.setWordList(baseWordList);
            else
                listWordVO.setWordList(idWordList.get(listWord.getWordListId()));
            matchListWordVOs.add(listWordVO);
        }
        long current=Math.max(1L,page.getCurrent());
        long size= Math.min(page.getSize()>0? page.getSize() : 10L, 100L);
        int total=matchListWord.size();
        int from = (int) Math.min((current - 1) * size, total);
        int to = (int) Math.min(from + size, total);
        Page<ListWordVO>listWordVOPage=new Page<>(current,size,total);
        listWordVOPage.setRecords(new ArrayList<>(matchListWordVOs.subList(from,to)));
        return listWordVOPage;
    }
}
