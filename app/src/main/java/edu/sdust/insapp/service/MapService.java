package edu.sdust.insapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import edu.sdust.insapp.utils.InsOrderInfo;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;


public class MapService extends Service {

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    private String longi;
    private String lati;

    private Looper looper;
    private StartedServiceHandler startedServiceHandler;
    public String s;
    public final String TAG = "MapService";

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStop = false;
    public int intTimer = 2*60;//设置间隔时间
    private  int count=0;


    public MapService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocation();
        HandlerThread handlerThread = new HandlerThread("StartedService");
        handlerThread.start();
        //获得HandlerThread的Looper队列并用于Handler
        looper = handlerThread.getLooper();
        startedServiceHandler = new StartedServiceHandler(looper);
        Log.e(TAG, "onCreate");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        //s = intent.getStringExtra("StartedServiceTest");
        //对于每一个启动请求，都发送一个消息来启动一个处理
        //同时传入启动ID，以便任务完成后我们知道该终止哪一个请求。
        Message message = startedServiceHandler.obtainMessage();
        message.arg1 = 1;
        startedServiceHandler.sendMessage(message);
        //如果我们被杀死了，那从这里返回之后被重启
        return START_STICKY;

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
                            count++;
                            Thread.sleep(1000 * intTimer);//5分钟后再次执行
                            uploadLocation(longi,lati);
                        } catch (InterruptedException e) {
                            //Auto-generated catch block
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
    }

    //初始化定位器
    private void initLocation(){
        mLocationOption=null;
        mlocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if(aMapLocation.getErrorCode()==0){
                        lati = String.valueOf(aMapLocation.getLatitude());
                        longi = String.valueOf(aMapLocation.getLongitude());
                        if(count==0){
                            uploadLocation(longi,lati);
                        }
                        Log.i(TAG + "location", "onLocationChanged: "+count+"----"+longi+"----"+lati);
                    }
                    else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }else {
                    System.out.println("定位失败，loc is null");
                }
            }
        };
        //设置定位监听
        mlocationClient.setLocationListener(mLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }

    private void uploadLocation(String longi,String lati){
        Log.i(TAG + "location", count+"----"+longi+"----"+lati);
            if(longi!=null && lati!=null){
                int dispaOrderID = InsOrderInfo.getInstance().getOrderInfo().getOrderId();
                //int eqptAccntID = deviceId;
                //String url = UrlConstant.locationURL+"?dispaOrderID="+dispaOrderID+"&eqptAccntID="+eqptAccntID+"&longi="+longi+"&lati="+lati;
                String url = UrlConstant.elecLocationURL+"?dispaOrderID="+dispaOrderID+"&longi="+longi+"&lati="+lati;
                VolleyRequest.RequestGet(url, "location", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.i(TAG + "上传位置", result);
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

    public class StartedServiceHandler extends Handler {
        public StartedServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            //通常我们在这里执行一些工作，比如下载文件。
            try {
                Log.e(TAG, "开始在服务中处理信息");
                Thread.sleep(3000);
                // 触发定时器
                if (!isStop) {
                    startTimer();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //根据startId终止服务，这样我们就不会在处理其它工作的过程中再来终止服务
            //如果组件通过调用startService()（这会导致onStartCommand()的调用）启动了服务，那么服务将一直保持运行，直至自行用stopSelf()终止或由其它组件调用stopService()来终止它。
            //如果组件调用bindService()来创建服务（那onStartCommand()就不会被调用），则服务的生存期就与被绑定的组件一致。一旦所有客户端都对服务解除了绑定，系统就会销毁该服务。
            //stopSelf(msg.arg1);
            //Log.e(TAG, "startedService处理数据完成后自动停止");
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        //Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mlocationClient!=null) {
            mlocationClient.stopLocation();
        }
        // 停止定时器
        if (isStop) {
            stopTimer();
        }
        Log.e(TAG, "onDestroy");
    }
}
