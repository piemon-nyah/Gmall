package com.piemon.gmall.constant;

/**
 * @author: piemon
 * @date: 2020/11/3 22:54
 * @description:
 */
public class CartConstant {
    public final static String TEMP_CART_KEY_PREFIX = "cart:temp:";//临时购物车前缀，后面加cartKey
    public final static String USER_CART_KEY_PREFIX = "cart:user:";//用户购物车前缀，后面加用户Id
    public final static String CART_CHECKED_KEY = "checked";//购物车在redis中存储哪些被选中用的key
}
