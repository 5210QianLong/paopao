package com.zhao.usercenter.service;

import com.zhao.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
//    @Test
    public void test() {
        //value是字符串
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set("name"  , "zhao");
        operations.set("age", 18);
        operations.set("yupi",2.0);
        System.out.println(operations.get("name"));
        User user = new User();
        user.setUsername("dog");
        operations.set("user", user);
        Assertions.assertTrue(18==(Integer) operations.get("age"));
        Assertions.assertTrue("zhao".equals(operations.get("name")));
        System.out.println(operations.get("user"));


    }
}
