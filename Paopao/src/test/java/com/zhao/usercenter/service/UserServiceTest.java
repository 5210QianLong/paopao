package com.zhao.usercenter.service;
import com.google.gson.Gson;
import com.zhao.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    void searchUserByTags() {
        List<String> list = Arrays.asList("java","python");
        List<User> users = userService.searchUserByTags(list);
        Assertions.assertNotNull(users);
        users.forEach(user -> System.out.println(user.getTags()));

    }
}