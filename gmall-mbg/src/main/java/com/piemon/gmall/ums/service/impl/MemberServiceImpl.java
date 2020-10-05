package com.piemon.gmall.ums.service.impl;

import com.piemon.gmall.ums.entity.Member;
import com.piemon.gmall.ums.mapper.MemberMapper;
import com.piemon.gmall.ums.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

}
