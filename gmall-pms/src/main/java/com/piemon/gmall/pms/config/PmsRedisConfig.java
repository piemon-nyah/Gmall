package com.piemon.gmall.pms.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;


import java.net.UnknownHostException;

/**
 * @author: piemon
 * @date: 2020/10/13 21:01
 * @description: RedisTemplate默认采用jdk的方式序列化，不能跨语言
 *               我们自己创建满意的序列化容器（一般采用json格式）
 */
@Configuration
public class PmsRedisConfig {
    //GenericJackson2JsonRedisSerializer支持泛型
    @Bean("redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //修改默认的序列化方式
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
