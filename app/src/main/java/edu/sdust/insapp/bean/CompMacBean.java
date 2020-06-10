package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */

public class CompMacBean {
    //字段名
    private String name;
    //单位
    private String unit;
    //是否是可选择的
    private boolean selectable;
    //下拉框列表
    private List<String> options;

    public CompMacBean(String name, String unit, boolean selectable, List<String> options) {
        this.name = name;
        this.unit = unit;
        this.selectable = selectable;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
