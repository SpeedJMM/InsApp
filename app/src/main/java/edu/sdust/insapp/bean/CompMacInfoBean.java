package edu.sdust.insapp.bean;

/**
 * Created by Administrator on 2017/11/17.
 */

public class CompMacInfoBean {

    //字段名
    private String name;
    //值
    private String value;

    public CompMacInfoBean() {
    }

    public CompMacInfoBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
