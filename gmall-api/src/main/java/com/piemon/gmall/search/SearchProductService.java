package com.piemon.gmall.search;

import com.piemon.gmall.vo.search.SearchParam;
import com.piemon.gmall.vo.search.SearchResponse;

/**
 * @author: piemon
 * @date: 2020/10/23 22:22
 * @description:商品检索
 */
public interface SearchProductService {


    SearchResponse searchProduct(SearchParam searchParam);
}
