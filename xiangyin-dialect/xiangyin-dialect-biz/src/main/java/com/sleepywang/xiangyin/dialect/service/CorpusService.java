package com.sleepywang.xiangyin.dialect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleepywang.xiangyin.common.core.util.R;
import com.sleepywang.xiangyin.dialect.dto.CorpusQueryDTO;
import com.sleepywang.xiangyin.dialect.dto.CorpusSaveDTO;
import com.sleepywang.xiangyin.dialect.entity.CorpusEntity;
import com.sleepywang.xiangyin.dialect.vo.CorpusVO;

import java.util.List;

public interface CorpusService extends IService<CorpusEntity> {
    List<CorpusVO> getCorpusVOs(CorpusQueryDTO query);
    IPage<CorpusVO> getCorpusVOsPage(Page page, CorpusQueryDTO query);
    R<Boolean> addCorpus(CorpusSaveDTO corpusSave);
    R<Boolean> deleteCorpus(Integer[]ids);
    R<Boolean> releaseCorpus(Integer id);
    R<Boolean> reviewCorpus(Integer id,Boolean isPassed);
    R<Boolean>withdrawCorpus(Integer id);
    R<Boolean>updateCorpus(CorpusEntity corpus);
    R likeCorpus(Integer id);
    R unlikeCorpus(Integer id);
    R scoreCorpus(Integer id,Double score);
}
