package com.zhao.usercenter.service;

import com.zhao.usercenter.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

/**
 * 算法工具 测试 类
 *<a href="https://blog.csdn.net/DBC_121/article/details/104198838">编辑距离算法</a>
 */
class AlgorithmUtilsTest {
    @Test
    void test(){
        List<String> str = Arrays.asList("csgo","java","大三");
        List<String> str1 = Arrays.asList("java","男","大三");
        List<String> str2 = Arrays.asList("python","女","大三");
        List<String> str3 = Arrays.asList("python","男","大三");
        List<String> str4 = Arrays.asList("java","男","大三");
        int i1 = AlgorithmUtils.minDistance(str, str1);
        int i2 = AlgorithmUtils.minDistance(str, str2);
        int i3 = AlgorithmUtils.minDistance(str, str3);
        int i4 = AlgorithmUtils.minDistance(str, str4);

        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);
        System.out.println(i4);
    }
}
