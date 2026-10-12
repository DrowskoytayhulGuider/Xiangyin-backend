package com.sleepywang.xiangyin.dialect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.dialect.entity.CorpusAuthenticEntity;
import com.sleepywang.xiangyin.dialect.mapper.CorpusAuthenticMapper;
import com.sleepywang.xiangyin.dialect.service.CorpusAuthenticService;
import org.springframework.stereotype.Service;

/**
 * 语料正宗记录（和点赞不同，正宗侧重的是收集的原始语料质量（即发音人的方言水平）；点赞侧重语料的标注处理质量和完善程度）
 *
 * @author pig
 * @date 2026-10-09 22:58:24
 */
@Service
public class CorpusAuthenticServiceImpl extends ServiceImpl<CorpusAuthenticMapper, CorpusAuthenticEntity> implements CorpusAuthenticService {

}
