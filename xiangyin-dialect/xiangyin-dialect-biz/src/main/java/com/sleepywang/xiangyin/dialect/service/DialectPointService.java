package com.sleepywang.xiangyin.dialect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.dialect.dto.DialectPointQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.DialectPointSaveDTO;
import com.sleepywang.xiangyin.dialect.entity.DialectPointEntity;
import com.sleepywang.xiangyin.dialect.vo.DialectPointVO;

import java.util.List;

public interface DialectPointService extends IService<DialectPointEntity> {
    List<DialectPointVO> selectDialectPoints(DialectPointQueryDTO query);
    IPage<DialectPointVO> selectDialectPointsPage(Page page, DialectPointQueryDTO query);
    R saveDialectPoint(DialectPointSaveDTO dialectPointSaveDTO);
    R deleteDialectPoint(Integer[] ids);
    List<DialectPointVO> fillDialectPointVO(List<DialectPointEntity> dialectPointEntities);
}
