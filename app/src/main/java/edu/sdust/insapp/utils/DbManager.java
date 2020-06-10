package edu.sdust.insapp.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/15.
 */

public class DbManager {
    /**
     * helper 全局OrderDBHelper对象
     */
    private static OrderDBHelper helper;

    /**
     * 单例模式获取helper对象
     * @param context  上下文对象
     * @return helper对象
     */
    public static OrderDBHelper getInstance(Context context){
        if(helper == null){
            helper = new OrderDBHelper(context.getApplicationContext());
        }
        return helper;
    }
}
