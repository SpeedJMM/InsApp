package edu.sdust.insapp.activity;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.netease.imageSelector.ImageSelector;
import com.netease.imageSelector.ImageSelectorConfiguration;
import com.netease.imageSelector.ImageSelectorConstant;

import edu.sdust.insapp.R;

/**
 * Created by Administrator on 2017/10/15.
 */

public class MyApplication extends Application {

    public static RequestQueue queues;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreat","aaaaa" );
        queues = Volley.newRequestQueue(getApplicationContext());

        ImageSelectorConfiguration configuration = new ImageSelectorConfiguration.Builder(this)
                .setImageSaveDirectory(R.string.app_name)
                .setMaxSelectNum(5)
                .setSpanCount(4)
                .setSelectMode(ImageSelectorConstant.MODE_MULTIPLE)
                .setTitleHeight(48)
                .build();
        ImageSelector.getInstance().init(configuration);
    }

    public static RequestQueue getHttpQueue(){
        return queues;
    }
}
