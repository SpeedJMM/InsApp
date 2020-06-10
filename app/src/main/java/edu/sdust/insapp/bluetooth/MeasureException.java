package edu.sdust.insapp.bluetooth;

/**
 * Created by 35139 on 2017/10/11.
 */

public class MeasureException extends Exception {
    //设备未连接
    public final static int DEVICE_OFFLINE = -1;
    //连接被中断
    public final static int LINK_INTERRUPT = -2;
    //超时
    public final static int TIME_OUT = -3;

    public MeasureException() {
    }

    public MeasureException(String message) {
        super(message);
    }

    public MeasureException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeasureException(Throwable cause) {
        super(cause);
    }

}
