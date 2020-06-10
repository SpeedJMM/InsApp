package edu.sdust.insapp.bean;


public class NonDeviceFaultRequest {
    String username;
    String password;
    NonDeviceFaultBean nonDeviceFault;

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

    public NonDeviceFaultBean getNonDeviceFault() {
        return nonDeviceFault;
    }

    public void setNonDeviceFault(NonDeviceFaultBean nonDeviceFault) {
        this.nonDeviceFault = nonDeviceFault;
    }
}
