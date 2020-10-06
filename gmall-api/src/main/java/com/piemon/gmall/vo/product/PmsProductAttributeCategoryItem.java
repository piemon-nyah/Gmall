package com.piemon.gmall.vo.product;

import com.piemon.gmall.pms.entity.ProductAttribute;
import com.piemon.gmall.pms.entity.ProductAttributeCategory;
import lombok.Data;

import java.util.List;

/**
 * 包含有分类下属性的vo
 */
@Data
public class PmsProductAttributeCategoryItem extends ProductAttributeCategory {
    private List<ProductAttribute> productAttributeList;


}
