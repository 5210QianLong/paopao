package com.zhao.usercenter.common;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    protected int pageNum = 1;
    /**
     * 页面大小
     */
    protected int pageSize = 10;
}
