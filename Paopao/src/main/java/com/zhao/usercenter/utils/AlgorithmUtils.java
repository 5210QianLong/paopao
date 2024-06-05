package com.zhao.usercenter.utils;

import java.util.List;
import java.util.Objects;

/**
 * 算法工具类
 *<a href="https://blog.csdn.net/DBC_121/article/details/104198838">编辑距离算法</a>
 *这个算法局限性：：两个比较对象是内容乱序的，比较结果不如有序的高，即使乱序的两者内容一摸一样的
 */
public class AlgorithmUtils {

    public static int minDistance(List<String> tagList1,List<String> tagList2){
        int n =  tagList1.size();
        int m =  tagList2.size();

        if(n * m == 0)
            return n + m;

        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++){
            d[i][0] = i;
        }

        for (int j = 0; j < m + 1; j++){
            d[0][j] = j;
        }

        for (int i = 1; i < n + 1; i++){
            for (int j = 1; j < m + 1; j++){
                int left = d[i - 1][j] + 1;
                int down = d[i][j - 1] + 1;
                int left_down = d[i - 1][j - 1];
                if (!Objects.equals(tagList1.get(i - 1), tagList2.get(j - 1)))
                    left_down += 1;
                d[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return d[n][m];
    }
}
