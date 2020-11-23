package com.piemon.gmall.cart.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: piemon
 * @date: 2020/11/3 0:01
 * @description:
 */
@Configuration
public class RedisConfig {
    @Bean
    RedissonClient redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.112.30:6379");
        return Redisson.create(config);
    }
}
