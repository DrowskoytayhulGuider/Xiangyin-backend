package com.sleepywang.xiangyin.dialect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.dialect.dto.DialectSortQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectSortSaveDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectSortUpdateDTO;
import com.sleepywang.xiangyin.dialect.entity.DialectClassEntity;
import com.sleepywang.xiangyin.dialect.entity.DialectSortEntity;
import com.sleepywang.xiangyin.dialect.vo.DialectSortVO;

import java.util.List;
import java.util.Map;

public interface DialectSortService extends IService<DialectSortEntity> {
    IPage<DialectSortVO> getDialectSortPage(Page page, DialectSortQueryDTO query);
    R<Boolean> saveDialectSort(DialectSortSaveDTO dialectSortSaveDTO);
    R<Boolean> updateDialectSort(DialectSortUpdateDTO dialectSortUpdateDTO);
    R<Boolean> deleteDialectSortLogically(Integer[] ids);
    List<DialectSortVO>selectDialectSortsByDialectClasses(List<Integer> dialectClassIds);
    List<DialectSortVO> dialectSortEntities2DialectSortVOs(List<DialectSortEntity>dialectSortEntities, Map<Integer,String> idName, Map<Integer,DialectSortEntity>idDialectSort);

}
