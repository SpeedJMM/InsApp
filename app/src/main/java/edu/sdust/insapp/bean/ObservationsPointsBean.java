package edu.sdust.insapp.bean;

/**
 * Created by Administrator on 2017/9/5.
 */

public class ObservationsPointsBean {
    private String speed;
    private String tempe;
    private String vibra;
    private String accele;

    public ObservationsPointsBean(String speed, String tempe, String vibra, String accele) {
        this.speed = speed;
        this.tempe = tempe;
        this.vibra = vibra;
        this.accele = accele;

    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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

    public String getAccele() {
        return accele;
    }

    public void setAccele(String accele) {
        this.accele = accele;
    }
}
