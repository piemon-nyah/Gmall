package com.piemon.gmall.pms.mapper;

import com.piemon.gmall.pms.entity.ProductAttribute;
import com.piemon.gmall.pms.entity.ProductAttributeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piemon.gmall.to.es.EsProductAttributeValue;

import java.util.List;

/**
 * <p>
 * 存储产品参数信息的表 Mapper 接口
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
public interface ProductAttributeValueMapper extends BaseMapper<ProductAttributeValue> {

    List<EsProductAttributeValue> selectProductBaseAttrAndValue(Long id);

    List<ProductAttribute> selectProductSaleAttrName(Long id);

}
