package com.sleepywang.xiangyin.dialect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.dialect.entity.WordListEntity;
import com.sleepywang.xiangyin.dialect.mapper.WordListMapper;
import com.sleepywang.xiangyin.dialect.service.WordListService;
import org.springframework.stereotype.Service;

/**
 * 词表
 *
 * @author sleepywang
 * @date 2026-09-19 20:32:48
 */
@Service
public class WordListServiceImpl extends ServiceImpl<WordListMapper, WordListEntity> implements WordListService {

}
