package com.piemon.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piemon.gmall.constant.SysCacheConstant;
import com.piemon.gmall.pms.entity.ProductCategory;
import com.piemon.gmall.pms.mapper.ProductCategoryMapper;
import com.piemon.gmall.pms.service.ProductCategoryService;
import com.piemon.gmall.vo.product.PmsProductCategoryWithChildrenItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Slf4j
@Service
@Component
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    ProductCategoryMapper categoryMapper;

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;
    /**
     * 分布式缓存用redis
     * @param i
     * @return
     */
    @Override
    public List<PmsProductCategoryWithChildrenItem> listCatelogWithChilder(Integer i) {
        //加入缓存逻辑
        List<PmsProductCategoryWithChildrenItem> items;

        Object menuCache = redisTemplate.opsForValue().get(SysCacheConstant.CATEGORY_MENU_CACHE_KEY);
        if(menuCache!=null){
            //缓存中有
            log.debug("菜单数据命中缓存......");
            items = (List<PmsProductCategoryWithChildrenItem>) menuCache;
        }else {
            items = categoryMapper.listCatelogWithChilder(i);
            //放到缓存redis中
            redisTemplate.opsForValue().set(SysCacheConstant.CATEGORY_MENU_CACHE_KEY,items);
        }



        return items;
    }
}
