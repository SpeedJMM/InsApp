package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class UploadBean {
    //文件的集合
    private List<String[]> files;
    //参数集合所对应的json串
    private String params;

    public UploadBean(List<String[]> files, String params) {
        this.files = files;
        this.params = params;
    }

    public List<String[]> getFiles() {
        return files;
    }

    public void setFiles(List<String[]> files) {
        this.files = files;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
