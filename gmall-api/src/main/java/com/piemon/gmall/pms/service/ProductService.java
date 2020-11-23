package com.piemon.gmall.pms.service;

import com.piemon.gmall.pms.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piemon.gmall.to.es.EsProduct;
import com.piemon.gmall.vo.PageInfoVo;
import com.piemon.gmall.vo.product.PmsProductParam;
import com.piemon.gmall.vo.product.PmsProductQueryParam;

import java.util.List;

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

    /**
     * 保存商品数据
     * @param productParam
     */
    void saveProduct(PmsProductParam productParam);


    /**
     * 批量上下架
     * @param ids
     * @param publishStatus
     */
    void updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 查询商品详情
     * @param id
     * @return
     */
    Product productInfo(Long id);

    EsProduct productAllInfo(Long id);

    EsProduct productSkuInfo(Long id);
}
