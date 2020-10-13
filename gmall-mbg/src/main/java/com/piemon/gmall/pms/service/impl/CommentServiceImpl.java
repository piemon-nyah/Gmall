package com.piemon.gmall.pms.service.impl;

import com.piemon.gmall.pms.entity.Comment;
import com.piemon.gmall.pms.mapper.CommentMapper;
import com.piemon.gmall.pms.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
