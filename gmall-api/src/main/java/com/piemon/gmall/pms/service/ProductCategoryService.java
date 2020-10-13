package com.piemon.gmall.pms.service;

import com.piemon.gmall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piemon.gmall.vo.product.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    /**
     * 查询这个菜单以及他的子菜单
     * @param i
     * @return
     */
    List<PmsProductCategoryWithChildrenItem> listCatelogWithChilder(Integer i);
}
