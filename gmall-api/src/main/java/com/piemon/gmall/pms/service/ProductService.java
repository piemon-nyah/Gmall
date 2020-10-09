package com.piemon.gmall.pms.service;

import com.piemon.gmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piemon.gmall.vo.PageInfoVo;
import com.piemon.gmall.vo.product.PmsProductQueryParam;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
public interface ProductService extends IService<Product> {
    /**
     * 根据复杂查询条件返回分页数据
     * @param productQueryParam
     * @return
     */

    PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam);
}
