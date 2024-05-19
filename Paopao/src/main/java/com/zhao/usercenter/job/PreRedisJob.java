package com.zhao.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhao.usercenter.constant.UserConstant.PROJECT_NAME;

@Component
@Slf4j
public class PreRedisJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String , Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    private final List<Long> mainUserList = List.of(12L);
    //要在主类上添加注解@enableScheduling 加载相应模块
    @Scheduled(cron = "0 20 10 * * *") // 秒 分 时 日 月 年
    public void doCacheRecommend() {
        //上分布式锁
        RLock rLock = redissonClient.getLock(PROJECT_NAME + ":prochchejob:docache:lock");
        try {
            if (rLock.tryLock(0,30000L, TimeUnit.MILLISECONDS)) {
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = String.format(PROJECT_NAME+":user:reccommend:%s",userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    //将查到的数据写入redis
                    try {
                        valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("redission set lock error", e);
        }finally {
            if (rLock.isHeldByCurrentThread()) {
                //是当前线程获得锁，就解锁
                rLock.unlock();
            }
        }

    }
}
