package com.zhao.usercenter;

import com.zhao.usercenter.mapper.UserMapper;
import com.zhao.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * RunWith(SpringRunner.class)
 * RunWith(SpringJUnit4ClassRunner.class)
 * Spring框架提供了一个测试运行器（SpringRunner或SpringJUnit4ClassRunner ）
 * , 它继承自 JUnit 的 BlockJUnit4ClassRunner 类。它可以加载 Spring 应用程序上下文并将其注入到测试类中
 * ，从而提供对依赖注入功能的支持。当您想要在测试期间使用 Spring 框架中的 beans 时，
 * 就需要在测试类上添加@RunWith(SpringRunner.class)注解，并使其运行基于 Spring 的单元测试。
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(4,userList.size());//断言，我觉得
        userList.forEach(System.out::println);
    }
}