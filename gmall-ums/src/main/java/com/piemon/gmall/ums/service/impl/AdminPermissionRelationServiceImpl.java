package com.piemon.gmall.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piemon.gmall.ums.entity.AdminPermissionRelation;
import com.piemon.gmall.ums.mapper.AdminPermissionRelationMapper;
import com.piemon.gmall.ums.service.AdminPermissionRelationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户和权限关系表(除角色中定义的权限以外的加减权限) 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
public class AdminPermissionRelationServiceImpl extends ServiceImpl<AdminPermissionRelationMapper, AdminPermissionRelation> implements AdminPermissionRelationService {

}
