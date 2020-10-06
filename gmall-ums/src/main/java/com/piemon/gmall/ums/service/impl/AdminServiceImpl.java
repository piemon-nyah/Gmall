package com.piemon.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piemon.gmall.ums.entity.Admin;
import com.piemon.gmall.ums.mapper.AdminMapper;
import com.piemon.gmall.ums.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
@Component
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {
        //利用spring自带的加密工具类DigestUtils进行密码加密
        String digestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        //将账号与加密后的密码与数据库的账号密码进行比对（数据库中的密码是加密后的密码）
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>().eq("username", username).eq("password", digestAsHex);

        Admin admin = adminMapper.selectOne(queryWrapper);



        return admin;
    }

    /**
     * 获取用户详情
     * @param userName
     * @return
     */
    @Override
    public Admin getUserInfo(String userName) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",userName));
    }
}
