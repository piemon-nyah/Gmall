package com.piemon.gmall.pms.service;

import com.piemon.gmall.pms.entity.ProductAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piemon.gmall.vo.PageInfoVo;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
public interface ProductAttributeService extends IService<ProductAttribute> {
    /**
     * 查询某个属性分类下的所有属性和参数
      * @param cid
     * @param type
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfoVo getCategoryAttributes(Long cid, Integer type, Integer pageSize, Integer pageNum);
}
