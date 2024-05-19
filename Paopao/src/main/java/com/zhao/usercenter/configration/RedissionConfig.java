package com.zhao.usercenter.configration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedissionConfig {
    //动态加载address
    private String host;
    private int port;
    @Bean
    public RedissonClient redisson() {
        // 1. Create config object
        Config config = new Config();
        String address = String.format("redis://%s:%s",host,port);
        config.useSingleServer().setAddress(address).setDatabase(3);
        return Redisson.create(config);
    }
}
