package com.zhao.usercenter.configration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * /@CrossOrigin(allowCredentials = "true", origins = {"<a href="http://localhost:5173/">...</a>"})
 * 可通过上述注解 也可以实现如下配置类的功能，解决跨域问题
 * @author zql
 */
@Configuration
public class CustomCorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173/")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")//允许的请求方法
                        .maxAge(3600);
            }
        };
    }
}
