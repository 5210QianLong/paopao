package com.zhao.usercenter;

import com.zhao.usercenter.model.domain.User;
import com.zhao.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class InsertUsersTest {
//    @Autowired
    private UserService userService;

//    @Test
//    public void testInsertUsers() {
//        StopWatch stopwatch = new StopWatch();
//        stopwatch.start();
//        final int INSERT_NUM = 10000;
//        int batchSize = 5000;
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(60, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(10000));
//        //分十组
//        int j = 0;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            List<User> userList = new ArrayList<>();
//            while(true){
//                j++;
//                User user = new User();
//                user.setUsername("路人甲"+300000+j);
//                user.setUserAccount("zhao");
//                user.setAvatarUrl("src/main/resources/static/HTB1hp8Mbv5G3KVjSZPx762I3XXax.webp");
//                user.setGender(0);
//                user.setUserPassword("Zql_32169");
//                user.setProfile("心爱的idea");
//                user.setUserRole(0);
//                user.setEmail("123@qq.com");
//                user.setUserStatus(0);
//                user.setPlanetCode("12345");
//                user.setPhone("3131");
//                user.setTags("[]");
//                userList.add(user);
//                if (j%batchSize ==0){
//                    break;
//                }
//            }
//            //异步执行
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                userService.saveBatch(userList, batchSize);
//            },threadPoolExecutor);
//            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//        stopwatch.stop();
//        System.out.println(stopwatch.getTotalTimeMillis());
//    }
}
