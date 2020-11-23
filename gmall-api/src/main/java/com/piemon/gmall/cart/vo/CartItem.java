package com.piemon.gmall.cart.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: piemon
 * @date: 2020/11/2 22:49
 * @description: 购物项
 */
@Setter
public class CartItem implements Serializable {
    //当前购物项的基本信息
    @Getter
    private Long skuId;//skuId标识是哪个商品
    @Getter
    private String name;
    @Getter
    private String skuCode;
    @Getter
    private Integer stock;
    //三个销售属性
    @Getter
    private String sp1;
    @Getter
    private String sp2;
    @Getter
    private String sp3;
    @Getter
    private String pic;//图片
    @Getter
    private BigDecimal price;
    @Getter
    private BigDecimal promotionPrice;//促销价格

    //以上是购物项的基本信息
    @Getter
    private Integer count;//有多少个
    @Getter
    private Boolean check = true;//购物项的选中状态

    private BigDecimal totalPrice;//当前购物项总价

    public BigDecimal getTotalPrice(){
        BigDecimal bigDecimal = price.multiply(new BigDecimal(count.toString()));
        return bigDecimal;
    }

}
