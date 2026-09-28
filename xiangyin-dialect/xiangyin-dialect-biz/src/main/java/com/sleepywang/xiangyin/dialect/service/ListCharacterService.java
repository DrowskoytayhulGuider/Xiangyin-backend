package com.sleepywang.xiangyin.dialect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleepywang.xiangyin.dialect.dto.ListCharacterQueryDTO;
import com.sleepywang.xiangyin.dialect.entity.ListCharacterEntity;
import com.sleepywang.xiangyin.dialect.vo.ListCharacterVO;

public interface ListCharacterService extends IService<ListCharacterEntity> {
    IPage<ListCharacterVO> getListCharacterPage(Page page, ListCharacterQueryDTO listCharacterQuery);
}
