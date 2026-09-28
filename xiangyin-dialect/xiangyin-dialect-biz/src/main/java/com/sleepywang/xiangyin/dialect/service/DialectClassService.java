package com.sleepywang.xiangyin.dialect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.dialect.entity.DialectClassEntity;

import java.util.List;

public interface DialectClassService extends IService<DialectClassEntity> {
     R<Boolean> deleteDialectClass(Integer[] ids);
     List<DialectClassEntity> selectNextDialectClass(Integer id);
}
