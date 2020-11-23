package com.piemon.gmall.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: piemon
 * @date: 2020/10/30 23:51
 * @description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gmall.pool")
public class PoolProperties {
    private Integer coreSize;
    private Integer maximumPoolSize;
    private Integer queueSize;
}
