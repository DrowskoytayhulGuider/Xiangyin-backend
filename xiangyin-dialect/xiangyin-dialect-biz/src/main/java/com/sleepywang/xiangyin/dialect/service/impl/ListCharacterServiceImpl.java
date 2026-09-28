package com.sleepywang.xiangyin.dialect.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.dialect.dto.ListCharacterQueryDTO;
import com.sleepywang.xiangyin.dialect.entity.CharacterListEntity;
import com.sleepywang.xiangyin.dialect.entity.GuangyunInitialEntity;
import com.sleepywang.xiangyin.dialect.entity.GuangyunRhymeEntity;
import com.sleepywang.xiangyin.dialect.entity.ListCharacterEntity;
import com.sleepywang.xiangyin.dialect.mapper.CharacterListMapper;
import com.sleepywang.xiangyin.dialect.mapper.GuangyunInitialMapper;
import com.sleepywang.xiangyin.dialect.mapper.GuangyunRhymeMapper;
import com.sleepywang.xiangyin.dialect.mapper.ListCharacterMapper;
import com.sleepywang.xiangyin.dialect.service.ListCharacterService;
import com.sleepywang.xiangyin.dialect.vo.ListCharacterVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字表字
 *
 * @author sleepywang
 * @date 2026-09-18 02:07:49
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ListCharacterServiceImpl extends ServiceImpl<ListCharacterMapper, ListCharacterEntity> implements ListCharacterService {
    /*
        private Integer id;
        private GuangyunInitialEntity guangyunInitial;
        private GuangyunRhymeEntity guangyunRhyme;
        private String guangyunTone;
        private Integer characterListId;
     */
    private final GuangyunRhymeMapper guangyunRhymeMapper;
    private final GuangyunInitialMapper guangyunInitialMapper;
    private final CharacterListMapper characterListMapper;
    public IPage<ListCharacterVO> getListCharacterPage(Page page, ListCharacterQueryDTO listCharacterQuery)
    {
        GuangyunInitialEntity queryInitial=listCharacterQuery.getGuangyunInitial();
        GuangyunRhymeEntity queryRhyme=listCharacterQuery.getGuangyunRhyme();
        List<Integer> matchInitialIds=guangyunInitialMapper.selectList(Wrappers.query(queryInitial))//queryInitial=null时是全量返回
                .stream().map(GuangyunInitialEntity::getId).toList();
        List<Integer> matchRhymeIds=guangyunRhymeMapper.selectList(Wrappers.query(queryRhyme))
                .stream().map(GuangyunRhymeEntity::getId).toList();
        List<ListCharacterEntity> matchListCharacter=list(Wrappers.<ListCharacterEntity>lambdaQuery()
                .in(!matchInitialIds.isEmpty(),ListCharacterEntity::getGuangyunInitialId,matchInitialIds)
                .in(!matchRhymeIds.isEmpty(),ListCharacterEntity::getGuangyunRhymeId,matchRhymeIds)
                .eq(listCharacterQuery.getId()!=null,ListCharacterEntity::getId,listCharacterQuery.getId())
                .eq(listCharacterQuery.getGuangyunTone()!=null&& !listCharacterQuery.getGuangyunTone().isEmpty(),ListCharacterEntity::getGuangyunTone,listCharacterQuery.getGuangyunTone())
                .eq(listCharacterQuery.getCharacterListId()!=null,ListCharacterEntity::getCharacterListId,listCharacterQuery.getCharacterListId())
                .eq(ListCharacterEntity::getIsDeleted,false));
        Map<Integer,GuangyunInitialEntity>idInitial=guangyunInitialMapper.selectList(Wrappers.query(queryInitial))
                .stream().collect(Collectors.toMap(GuangyunInitialEntity::getId, Function.identity()));
        Map<Integer,GuangyunRhymeEntity>idRhyme=guangyunRhymeMapper.selectList(Wrappers.query(queryRhyme))
                .stream().collect(Collectors.toMap(GuangyunRhymeEntity::getId, Function.identity()));
        Map<Integer,CharacterListEntity> idCharacterList=new HashMap<>();
//        if(listCharacterQuery.getCharacterListId()!=null)//查找单个字表
//        {
//            List<CharacterListEntity>matchCharacterList=characterListMapper.selectList(Wrappers.<CharacterListEntity>lambdaQuery()
//                            .eq(CharacterListEntity::getId,listCharacterQuery.getCharacterListId())
//                            .ne(CharacterListEntity::getStatus,"DELETED"));
//            idCharacterList.put(listCharacterQuery.getCharacterListId(),matchCharacterList.isEmpty()?null:matchCharacterList.get(0));//ById(listCharacterQuery.getCharacterListId()));//注意CharacterList要排删除
//        }
//        else//查找对应字表
//        {
            Set<Integer> matchCharacterListIds = matchListCharacter.stream()
                    .map(ListCharacterEntity::getCharacterListId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            idCharacterList=matchCharacterListIds.isEmpty()?Map.of():characterListMapper.selectList(Wrappers.<CharacterListEntity>lambdaQuery()
                    .in(CharacterListEntity::getId,matchCharacterListIds)
                    .ne(CharacterListEntity::getStatus,"DELETED"))
                    .stream().collect(Collectors.toMap(CharacterListEntity::getId,Function.identity()));
//        }
        CharacterListEntity baseCharacterList=new CharacterListEntity();
        baseCharacterList.setId(1);
        baseCharacterList.setName("基础字表");
        baseCharacterList.setStatus("RELEASED");
        baseCharacterList.setMakerId(1L);
        List<ListCharacterVO> matchListCharacterVOs=new ArrayList<>();
        for(ListCharacterEntity listCharacter:matchListCharacter)
        {
            /*
            private String character;/
            private GuangyunInitialEntity guangyunInitial;/
            private String guangyunTone;/
            private String meaning;/
            private GuangyunRhymeEntity guangyunRhyme;/
            private CharacterListEntity characterList;
            private String remark;/
             */
            ListCharacterVO listCharacterVO=new ListCharacterVO();
            listCharacterVO.setId(listCharacter.getId());
            listCharacterVO.setMeaning(listCharacter.getMeaning());
            listCharacterVO.setRemark(listCharacter.getRemark());
            listCharacterVO.setGuangyunTone(listCharacter.getGuangyunTone());
            listCharacterVO.setCharacter(listCharacter.getHanzi());
            listCharacterVO.setGuangyunInitial(idInitial.get(listCharacter.getGuangyunInitialId()));
            listCharacterVO.setGuangyunRhyme(idRhyme.get(listCharacter.getGuangyunRhymeId()));
            if(listCharacterQuery.getCharacterListId()==1)
                listCharacterVO.setCharacterList(baseCharacterList);
            else
                listCharacterVO.setCharacterList(idCharacterList.get(listCharacter.getCharacterListId()));
            matchListCharacterVOs.add(listCharacterVO);
        }
        long current=Math.max(1L,page.getCurrent());
        long size= Math.min(page.getSize()>0? page.getSize() : 10L, 100L);
        int total=matchListCharacter.size();
        int from = (int) Math.min((current - 1) * size, total);
        int to = (int) Math.min(from + size, total);
        Page<ListCharacterVO>listCharacterVOPage=new Page<>(current,size,total);
        listCharacterVOPage.setRecords(new ArrayList<>(matchListCharacterVOs.subList(from,to)));
        return listCharacterVOPage;
    }
}
