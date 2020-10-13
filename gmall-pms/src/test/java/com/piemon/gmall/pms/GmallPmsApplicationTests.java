package com.piemon.gmall.pms;

import com.piemon.gmall.pms.entity.Brand;
import com.piemon.gmall.pms.entity.Product;
import com.piemon.gmall.pms.service.BrandService;
import com.piemon.gmall.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class GmallPmsApplicationTests {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedisTemplate<Object,Object> redisTemplateObj;
//    @Test
//    void contextLoads() {
//        Product byId = productService.getById(1);
//        System.out.println(byId.getName());
//    }
    @Test
    public void redisTemplateObj(){
        Brand brand = new Brand();
        brand.setName("哈哈哈");
        redisTemplateObj.opsForValue().set("abc",brand);
        System.out.println("存入对象");
        Brand abc = (Brand) redisTemplateObj.opsForValue().get("abc");
        System.out.println("刚才保存的对象的值是"+abc.getName());
    }
}
