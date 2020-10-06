package com.piemon.gmall.ums.config;

import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.util.ResourceUtils.getFile;

@Configuration
public class UmsDataSourceConfig {
    @Bean
    public DataSource dataSource() throws IOException, SQLException {
        File file = getFile("classpath:sharding-jdbc.yml");
        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(file);

        return dataSource;
    }
}
