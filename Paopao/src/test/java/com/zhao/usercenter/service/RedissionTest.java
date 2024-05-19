package com.zhao.usercenter.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissionTest {

    @Resource
    private RedissonClient redissonClient;
//    @Test
    void Test(){
        //操作list
        RList<String> rlist = redissonClient.getList("r-list");
//        rlist.add("1");
        Assertions.assertTrue(rlist.contains("1"));
        rlist.remove(0);
        //操作map
        RMap<Object, Object> rMap = redissonClient.getMap("r-map");
//        rMap.put("1", "2");
        Assertions.assertTrue(rMap.containsKey("1"));
        Assertions.assertEquals("2", rMap.get("1"));
        rMap.remove("1");
    }
}
