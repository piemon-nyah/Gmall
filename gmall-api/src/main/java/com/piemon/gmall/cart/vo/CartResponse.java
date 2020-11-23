package com.piemon.gmall.cart.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: piemon
 * @date: 2020/11/3 22:32
 * @description:
 */
@Data
public class CartResponse implements Serializable {

    private Cart cart;//整个购物车
    private CartItem cartItem;//某项购物项
    private String cartKey;

}
