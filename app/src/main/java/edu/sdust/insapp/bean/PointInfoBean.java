package edu.sdust.insapp.bean;

import java.util.Date;

/**
 * Created by Administrator on 2017/10/31.
 */

public class PointInfoBean {
    //测点序号
    private int pointNum;
    //时间
    private Date insTime = new Date();
    //温度
    private String tempe;
    //震动
    private String vibra;
    //速度
    private String speed;
    //加速度
    private String accele;

    public PointInfoBean() {
    }

    public PointInfoBean(int pointNum, Date insTime, String tempe, String vibra, String speed, String accele) {
        this.pointNum = pointNum;
        this.insTime = insTime;
        this.tempe = tempe;
        this.vibra = vibra;
        this.speed = speed;
        this.accele = accele;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    public Date getInsTime() {
        return insTime;
    }

    public void setInsTime(Date insTime) {
        this.insTime = insTime;
    }

    public String getTempe() {
        return tempe;
    }

    public void setTempe(String tempe) {
        this.tempe = tempe;
    }

    public String getVibra() {
        return vibra;
    }

    public void setVibra(String vibra) {
        this.vibra = vibra;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAccele() {
        return accele;
    }

    public void setAccele(String accele) {
        this.accele = accele;
    }
}
