package com.piemon.gmall.pms.service;

import com.piemon.gmall.pms.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piemon.gmall.vo.PageInfoVo;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
public interface BrandService extends IService<Brand> {

    PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize);
}
