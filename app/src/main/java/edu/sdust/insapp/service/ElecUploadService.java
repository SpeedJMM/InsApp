package edu.sdust.insapp.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.sdust.insapp.activity.MyApplication;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.ElecUploadUtils;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.PostUploadRequest;
import edu.sdust.insapp.utils.UrlConstant;

public class ElecUploadService extends Service {

    private Looper looper;
    private StartedServiceHandler startedServiceHandler;
    public String s;
    public final String TAG = "ElecUploadService";

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStop = false;
    public int intTimer = 2*60;//设置间隔时间
    private List<Integer> elecInspEncloIds;
    private List<Integer> elecInspEncloRoutes;

    public ElecUploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("StartedService");
        handlerThread.start();
        //获得HandlerThread的Looper队列并用于Handler
        looper = handlerThread.getLooper();
        startedServiceHandler = new StartedServiceHandler(looper);
        elecInspEncloIds = new ArrayList<>();
        elecInspEncloRoutes = new ArrayList<>();
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 停止定时器
        if (isStop) {
            stopTimer();
        }
        Log.e(TAG, "onDestroy");
    }

    private void getElecInspEncloId() {
        elecInspEncloIds.clear();
        elecInspEncloRoutes.clear();
        OrderDBHelper helper = DbManager.getInstance(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("select * from " + DbConstant.ELECINSPENCLO_TABLE + " where whetherUpload <> 1 or whetherUpload is null", null);
            while (cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
                elecInspEncloIds.add(id);

                String elecInspEncloRoute = cursor.getString(cursor.getColumnIndex("elecInspEncloRoute"));
                if (!elecInspEncloRoute.equals("[]")) {
                    elecInspEncloRoute = elecInspEncloRoute.substring(1, elecInspEncloRoute.length() - 1);
                    String[] t;
                    t = elecInspEncloRoute.split(",");
                    for (String s : t) {
                        s = s.trim();
                        elecInspEncloRoutes.add(Integer.valueOf(s));
                    }
                }
            }

//            db.execSQL("update " + DbConstant.PICTURE_TABLE + " set whetherUpload=? where _id=2", new Object[] {
//                    Integer.valueOf(1)
//            });

            for (Integer i : elecInspEncloRoutes ) {
                db.execSQL("update " + DbConstant.PICTURE_TABLE + " set whetherUpload=1 where _id=" + i);
                Log.i(TAG, "getElecInspEncloId: " + i);
            }

            for (Integer i : elecInspEncloIds) {
                db.execSQL("update " + DbConstant.ELECINSPENCLO_TABLE + " set whetherUpload=1 where _id=" + i);
                Log.i(TAG, "getElecInspEncloId: " + i);
            }

            db.setTransactionSuccessful();
            cursor.close();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void setWhetherUploadFailed() {
        OrderDBHelper helper = DbManager.getInstance(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Integer i : elecInspEncloRoutes ) {
                db.execSQL("update " + DbConstant.PICTURE_TABLE + " set whetherUpload = 0 where _id = " + i);
            }

            for (Integer i : elecInspEncloIds) {
                db.execSQL("update " + DbConstant.ELECINSPENCLO_TABLE + " set whetherUpload=0 where _id=" + i);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void uploadElecInspEnclo() {
        String params = ElecUploadUtils.getElecInspEncloParams(getApplicationContext());
        HashMap<String, String> stringMap = new HashMap<>();
        stringMap.put("params", params);
        LinkedList<String[]> files = ElecUploadUtils.getElecInspEncloFiles(getApplicationContext());

        if (files.size() == 0){
            return;
        }

        getElecInspEncloId();

        MyApplication.getHttpQueue().add(new PostUploadRequest(UrlConstant.uploadElecEncloURL, files, stringMap
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int resultCode = response.getInt("result");
                    if (resultCode == 0) {

                    } else {
                        setWhetherUploadFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    setWhetherUploadFailed();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                setWhetherUploadFailed();
                Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
            }
        }));
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
                            uploadElecInspEnclo();
                        } catch (InterruptedException e) {
                            //  Auto-generated catch block
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
        // Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
