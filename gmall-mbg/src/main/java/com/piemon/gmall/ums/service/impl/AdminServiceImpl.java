package com.piemon.gmall.ums.service.impl;

import com.piemon.gmall.ums.entity.Admin;
import com.piemon.gmall.ums.mapper.AdminMapper;
import com.piemon.gmall.ums.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
