package com.sleepywang.xiangyin.dialect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleepywang.xiangyin.dialect.entity.ListSentenceEntity;
import com.sleepywang.xiangyin.dialect.vo.ListSentenceVO;

public interface ListSentenceService extends IService<ListSentenceEntity> {
    IPage<ListSentenceVO> getListSentencePage(Page page, ListSentenceEntity listSentenceQuery);
}
