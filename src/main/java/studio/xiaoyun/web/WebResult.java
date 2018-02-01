package studio.xiaoyun.web;

import java.util.List;

/**
 * 控制器的方法要包含一个总数和一个列表时可以用这个类封装
 */
public class WebResult {
    /**
     * 总数
     */
    private long total;
    /**
     * 行
     */
    private List<?> rows;


    /**
     * 构造函数
     * @param total 数量
     * @param rows  多行数据
     */
    public WebResult(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public List<?> getRows() {
        return rows;
    }
}
