package com.piemon.gmall.portal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: piemon
 * @date: 2020/10/29 23:28
 * @description: 配置当前系统的线程池信息
 */
@Configuration
public class ThreadPoolConfig {
    @Value("${gmall.pool.coreSize}")
    private Integer coreSize;
    @Value("${gmall.pool.maximumPoolSize}")
    private Integer maximumPoolSize;
    @Value("${gmall.pool.queueSize}")
    private Integer queueSize;

    @Bean("mainThreadPoolExecutor")
    public ThreadPoolExecutor mainThreadPoolExecutor(PoolProperties poolProperties){

        LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<>(poolProperties.getQueueSize());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolProperties.getCoreSize(), poolProperties.getMaximumPoolSize(),
                10, TimeUnit.MINUTES, deque);

        return threadPoolExecutor;
    }
    @Bean("otherThreadPoolExecutor")
    public ThreadPoolExecutor otherThreadPoolExecutor(PoolProperties poolProperties){


        LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<>(poolProperties.getQueueSize());
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolProperties.getCoreSize(), poolProperties.getMaximumPoolSize(),
                10, TimeUnit.MINUTES, deque);

        return threadPoolExecutor;
    }
}
