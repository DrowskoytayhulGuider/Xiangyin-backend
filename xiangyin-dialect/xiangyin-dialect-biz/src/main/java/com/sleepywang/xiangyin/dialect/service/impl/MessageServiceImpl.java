package com.sleepywang.xiangyin.dialect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.dialect.entity.MessageEntity;
import com.sleepywang.xiangyin.dialect.mapper.MessageMapper;
import com.sleepywang.xiangyin.dialect.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * 私信消息
 *
 * @author sleepywang
 * @date 2026-09-19 19:48:40
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {

}
