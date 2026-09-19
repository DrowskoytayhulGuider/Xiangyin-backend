package com.sleepywang.xiangyin.dialect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleepywang.xiangyin.dialect.entity.CommentEntity;
import com.sleepywang.xiangyin.dialect.mapper.CommentMapper;
import com.sleepywang.xiangyin.dialect.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * 评论
 *
 * @author sleepywang
 * @date 2026-09-19 19:54:49
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {

}
