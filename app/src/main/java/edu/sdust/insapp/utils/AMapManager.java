package edu.sdust.insapp.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by 35139 on 2017/12/17.
 */

public class AMapManager implements AMapLocationListener{
    private static AMapManager mAMapManager;
    private AMapLocation lastLocation;
    private Context mContext;
    public AMapLocationClient mLocationClient;
    public AMapLocationListener mLocationListener;
    public AMapLocationClientOption mLocationOption;


    public static void initAMapManager(Context context){
        mAMapManager = new AMapManager();
        mAMapManager.mContext = context;
        mAMapManager.mLocationClient = new AMapLocationClient(context);
        mAMapManager.mLocationListener = mAMapManager;
        mAMapManager.mLocationOption = new AMapLocationClientOption();
        mAMapManager.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mAMapManager.mLocationOption.setNeedAddress(false);
        mAMapManager.mLocationOption.setOnceLocationLatest(true);
        mAMapManager.mLocationClient.setLocationOption(mAMapManager.mLocationOption);
    }

    public static AMapManager getInstance(){
        if(mAMapManager == null)
            throw new RuntimeException("AMapManager Not Init");
        return mAMapManager;
    }

    public void start(){
        mLocationClient.startLocation();
    }

    public AMapLocation getLastLocation() {
        return lastLocation;
    }

    public void end(){
        mLocationClient.stopLocation();
    }

    public void destory(){
        mLocationClient.onDestroy();
    }


    private AMapManager(){

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.i("MapManager",aMapLocation.getDescription());
        lastLocation = aMapLocation;
    }


}
