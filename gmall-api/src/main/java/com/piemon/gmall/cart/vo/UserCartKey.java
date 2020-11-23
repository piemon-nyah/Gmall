package com.piemon.gmall.cart.vo;

import lombok.Data;

/**
 * @author: piemon
 * @date: 2020/11/6 0:13
 * @description:
 */
@Data
public class UserCartKey {
    private boolean login;//用户是否登录
    private Long userId;//用户如果登录，登录id
    private String tempCartKey;//用户没有登录而且没有购物车的临时购物车key

    private String finalCartKey;//用户最终用哪个购物车
}
