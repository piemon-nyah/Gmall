package com.piemon.gmall.ums.service;

import com.piemon.gmall.ums.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
public interface MemberService extends IService<Member> {

    Member login(String username, String password);
}
