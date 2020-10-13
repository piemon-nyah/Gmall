package com.piemon.gmall.pms.service.impl;

import com.piemon.gmall.pms.entity.ProductLadder;
import com.piemon.gmall.pms.mapper.ProductLadderMapper;
import com.piemon.gmall.pms.service.ProductLadderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品阶梯价格表(只针对同商品) 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-12
 */
@Service
public class ProductLadderServiceImpl extends ServiceImpl<ProductLadderMapper, ProductLadder> implements ProductLadderService {

}
