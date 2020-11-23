package com.piemon.gmall.portal.controller;

import com.piemon.gmall.pms.service.ProductService;
import com.piemon.gmall.to.CommonResult;
import com.piemon.gmall.to.es.EsProduct;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: piemon
 * @date: 2020/10/28 21:38
 * @description:
 */
@RestController
public class ProductItemController {

    @Reference
    ProductService productService;

    @Qualifier("mainThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor mainThreadPoolExecutor;

    @Qualifier("otherThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor otherThreadPoolExecutor;

    /**
     * 数据库(商品的基本信息表、商品的属性表、商品的促销表)和es(info/attr/sale)
     * 高并发系统的优化：1.加缓存 2.开异步
     *
     * @return
     */
    public EsProduct productInfo2(Long id){
        //感知结果
        CompletableFuture.supplyAsync(()->{
            return "";
        },mainThreadPoolExecutor).whenComplete((r,e)->{
            System.out.println("处理结果"+r);
            System.out.println("处理异常"+e);
        });
        //1.商品基本数据(名字介绍等)
        //2.商品属性数据
        //3.商品营销数据
        //4.商品配送数据
        //5.商品增值服务数据

        return null;
    }

    /**
     * 商品的详情
     * @param id
     * @return
     */
    @GetMapping("/item/{id}")
    public CommonResult productInfo(@PathVariable("id") Long id){
        EsProduct esProduct = productService.productAllInfo(id);
        return new CommonResult().success(esProduct);
    }
    /**
     * sku的详情
     */
    @GetMapping("item/sku/{id}.html")
    public CommonResult productSkuInfo(@PathVariable("id") Long id){
        EsProduct esProduct = productService.productSkuInfo(id);
        return new CommonResult().success(esProduct);
    }
}
