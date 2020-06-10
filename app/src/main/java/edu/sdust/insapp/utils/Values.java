package edu.sdust.insapp.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 35139 on 2017/10/17.
 */

public class Values {
    public static String COUNT_FOUR 	   = "00002a3a-0000-1000-8000-00805f9b34fb";//读
    public static String COUNT_WRITE 	   = "00002a39-0000-1000-8000-00805f9b34fb";//写
    public static String COUNT_SERVICE	   = "0000180d-0000-1000-8000-00805f9b34fb";//服务
    public static String FFT               = "00002af2-0000-1000-8000-00805f9b34fb";//傅里叶分析
    public static String FFT_INFO          = "00002af1-0000-1000-8000-00805f9b34fb";//读属性、采样率和字节长度

    public static final int MAJOR_MOV = 1;      //动设备
    public static final int MAJOR_STAT = 2;     //静设备
    public static final int MAJOR_ELE = 3;      //电气
    public static final int MAJOR_METER = 4;    //仪表

    public static int getVersionCode(Context context){
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionCode = packInfo.versionCode;
        return versionCode;
    }
}
