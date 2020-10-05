package com.piemon.gmall.pms.service.impl;

import com.piemon.gmall.pms.entity.Product;
import com.piemon.gmall.pms.mapper.ProductMapper;
import com.piemon.gmall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
