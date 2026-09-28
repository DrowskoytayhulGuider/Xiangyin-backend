package com.sleepywang.xiangyin.dialect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleepywang.xiangyin.dialect.entity.ListWordEntity;
import com.sleepywang.xiangyin.dialect.vo.ListWordVO;

public interface ListWordService extends IService<ListWordEntity> {
    IPage<ListWordVO> getListWordPage(Page page, ListWordEntity listWordQuery);
}
