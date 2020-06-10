package edu.sdust.insapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import edu.sdust.insapp.utils.InsOrderInfo;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class ElecPositioningService extends Service {
    private static final String TAG = "ElecPositioningService";
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    private String longi;
    private String lati;
    private static final long ELEC_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStop = false;
    public int intTimer = 20;

    public static Intent newIntent(Context context) {
        return new Intent(context, ElecPositioningService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = ElecPositioningService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), ELEC_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

//    public ElecPositioningService() {
//        super(TAG);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("location", "EPServer onCreate");
        lati = null;
        longi = null;
        initLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("location", "EPServer onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("location", "onHandleIntent");
        if (!isNetworkAvailableAndConnected()) {
            return;
        }

        // 触发定时器
        if (!isStop) {
            startTimer();
        }

    }

    @Override
    public boolean stopService(Intent name) {
        stopTimer();
        return super.stopService(name);
    }

    private void startTimer() {
        isStop = true;//定时器启动后，修改标识，关闭定时器的开关
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    do {
                        try {
                            Thread.sleep(1000 * intTimer);//intTimer秒后再次执行
                            if (longi != null && !longi.equals("") && lati !=null && !lati.equals(""))
                            uploadLocation(longi,lati);
                        } catch (InterruptedException e) {
                            return;
                        }
                    } while (isStop);
                }
            };
        }
        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, 0);//执行定时器中的任务
        }
    }

    /**
     * 停止定时器，初始化定时器开关
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        isStop = false;//重新打开定时器开关
        mlocationClient.onDestroy();
        Log.i("location", "stopTimer: ");
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }

    //初始化定位器
    private void initLocation(){
        mLocationOption=null;
        mlocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        Log.i("location", "initLocation: 初始化");
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if(aMapLocation.getErrorCode()==0){
                        lati = String.valueOf(aMapLocation.getLatitude());
                        longi = String.valueOf(aMapLocation.getLongitude());
                        Log.i("location", "onLocationChanged: "+longi+"----"+lati);
                    }
                    else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //设置定位监听
        mlocationClient.setLocationListener(mLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //获取一次定位结果：
//        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);
//        //获取最近3s内精度最高的一次定位结果：
//        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
        //关闭缓存机制
        //mLocationOption.setLocationCacheEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000*5);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
        Log.i("location", "initLocation: startLocation()");
    }

    private void uploadLocation(String longi,String lati){
        Log.i("location", longi+"----"+lati);
        if (false)
        if(longi!=null && lati!=null){
            int dispaOrderID = InsOrderInfo.getInstance().getOrderInfo().getOrderId();
            //int eqptAccntID = deviceId;
            //String url = UrlConstant.locationURL+"?dispaOrderID="+dispaOrderID+"&eqptAccntID="+eqptAccntID+"&longi="+longi+"&lati="+lati;
            String url = UrlConstant.elecLocationURL+"?dispaOrderID="+dispaOrderID+"&longi="+longi+"&lati="+lati;
            VolleyRequest.RequestGet(url, "location", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    Log.i("上传位置", result);
                }

                @Override
                public void onError(VolleyError result) {
                    Log.e("上传位置", result.toString());
                }

                @Override
                public void jsonOnSuccess(JSONObject result) {

                }
            });
        }

    }
}
