package cn.ccccltd.beans;

import lombok.Data;

import java.util.List;

/**
 *  柱状图数据类
 */
@Data
public class LineBean {
    private List<String> columns;
    private List<Entry> rows;
}
