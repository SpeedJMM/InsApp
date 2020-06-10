package edu.sdust.insapp.bean;


public class DeviceFaultRequest {
    String username;
    String password;
    DeviceFaultBean deviceFault;

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

    public DeviceFaultBean getDeviceFault() {
        return deviceFault;
    }

    public void setDeviceFault(DeviceFaultBean deviceFault) {
        this.deviceFault = deviceFault;
    }
}
