package edu.sdust.insapp.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/22.
 */

public class DeviceIndexUtil {
    private static Map<Integer, Integer> deviceIndex;

    public static Map<Integer, Integer> getInstance(){
        if(deviceIndex == null){
            deviceIndex = new HashMap<>();
        }
        return deviceIndex;
    }
}
