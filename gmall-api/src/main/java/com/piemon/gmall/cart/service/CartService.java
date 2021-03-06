package com.piemon.gmall.cart.service;

import com.piemon.gmall.cart.vo.CartResponse;

import java.util.concurrent.ExecutionException;

/**
 * @author: piemon
 * @date: 2020/11/3 21:21
 * @description: 购物车服务
 */
public interface CartService {

    /**
     * 添加商品去购物车
     * @param skuId
     * @param num
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse addToCart(Long skuId, Integer num, String cartKey, String accessToken) throws ExecutionException, InterruptedException;

    /**
     * 修改购物项数量
     * @param skuId
     * @param num
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse updateCartItem(Long skuId, Integer num, String cartKey, String accessToken);

    /**
     * 获取购物车的所有数据
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse listCart(String cartKey, String accessToken);

    /**
     * 删除购物项
     * @param skuId
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse deleteCart(Long skuId, String cartKey, String accessToken);

    /**
     * 清空购物车
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse clearCart(String cartKey, String accessToken);

    /**
     * 选中购物项
     * @param skuIds
     * @param ops
     * @param cartKey
     * @param accessToken
     * @return
     */
    CartResponse checkCartItems(String skuIds, Integer ops, String cartKey, String accessToken);
}
