package com.sleepywang.xiangyin.dialect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.dialect.entity.ConversationEntity;
import com.sleepywang.xiangyin.dialect.mapper.ConversationMapper;
import com.sleepywang.xiangyin.dialect.service.ConversationService;
import org.springframework.stereotype.Service;

/**
 * 私信
 *
 * @author sleepywang
 * @date 2026-09-19 19:50:02
 */
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, ConversationEntity> implements ConversationService {

}
