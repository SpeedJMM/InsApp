package edu.sdust.insapp.bean;

import java.util.List;

public class ElecInspEncloParams {

    //用户名
    private String username;
    //密码
    private String password;
    //电气巡检附件列表，无照片
    private List<ElecInspEncloBean> elecInspEnclo;

    public ElecInspEncloParams() {

    }

    public ElecInspEncloParams(String username, String password, List<ElecInspEncloBean> elecInspEnclo) {
        this.username = username;
        this.password = password;
        this.elecInspEnclo = elecInspEnclo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ElecInspEncloBean> getElecInspEnclo() {
        return elecInspEnclo;
    }

    public void setElecInspEnclo(List<ElecInspEncloBean> elecInspEnclo) {
        this.elecInspEnclo = elecInspEnclo;
    }
}
