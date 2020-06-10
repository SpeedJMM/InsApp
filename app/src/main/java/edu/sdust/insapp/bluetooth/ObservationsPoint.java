package edu.sdust.insapp.bluetooth;

/**
 * Created by 35139 on 2017/10/11.
 */

public class ObservationsPoint {
    //温度
    private double temperature;
    //振动
    private double vibration;
    //速度
    private double speed;
    //加速度
    private double acceleration;

    public ObservationsPoint() {
    }

    public ObservationsPoint(double temperature, double vibration, double speed, double acceleration) {
        this.temperature = temperature;
        this.vibration = vibration;
        this.speed = speed;
        this.acceleration = acceleration;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getVibration() {
        return vibration;
    }

    public void setVibration(double vibration) {
        this.vibration = vibration;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }
}
