package com.piemon.gmall.pms.mapper;

import com.piemon.gmall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piemon.gmall.vo.product.PmsProductCategoryWithChildrenItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    List<PmsProductCategoryWithChildrenItem> listCatelogWithChilder(Integer i);

}
