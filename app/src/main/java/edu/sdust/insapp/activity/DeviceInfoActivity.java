package edu.sdust.insapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.CompMacBean;
import edu.sdust.insapp.bean.DeviceBean;
import edu.sdust.insapp.bean.InsOrderBean;
import edu.sdust.insapp.bean.ObservationsPointsBean;
import edu.sdust.insapp.bean.OrderInfoBean;
import edu.sdust.insapp.bluetooth.BlueToothCallBack;
import edu.sdust.insapp.bluetooth.ObservationsPoint;
import edu.sdust.insapp.utils.BleManager;
import edu.sdust.insapp.utils.Complex;
import edu.sdust.insapp.utils.DataProcessing;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.DeviceIndexUtil;
import edu.sdust.insapp.utils.FFT;
import edu.sdust.insapp.utils.InsOrderInfo;
import edu.sdust.insapp.utils.MyVisualView;
import edu.sdust.insapp.utils.NetworkDetector;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class DeviceInfoActivity extends AppCompatActivity implements BlueToothCallBack {
    //整机数据
    @BindView(R.id.lv_device_info_data)
    ListView listView;
    //测点信息
    @BindView(R.id.lv_device_info_observation_points_info)
    ListView pointsInfo;
    //历史数据按钮
    @BindView(R.id.btn_device_info_history)
    Button historyData;
    //测量按钮
    @BindView(R.id.btn_device_info_survey)
    Button surveyButton;
    //连接按钮
    @BindView(R.id.btn_device_info_conn_bluet)
    Button connBluetButton;
    //连接状态
    @BindView(R.id.tv_device_info_bluet_conn_status)
    TextView bluetConnStatus;
    //标题
    @BindView(R.id.tv_device_info_title)
    TextView title;
    //保存按钮
    @BindView(R.id.btn_device_info_save)
    Button saveButton;
    //清除按钮
    @BindView(R.id.btn_device_info_clean)
    Button cleanButton;

    @BindView(R.id.btn_device_info_skip)
    Button skipButton;
    //点击测量后获得的数据
    List<ObservationsPoint> pointData;
    //显示数据
    List<ObservationsPointsBean> info;
    //蓝牙管理器
    BleManager bleManager;

    private PopupWindow mPopWindow;
    MyVisualView visualizerView;
    Complex[] mData = new Complex[128];

    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private String deviceTag;
    private  Integer deviceId;
    //private  Integer deviceId1;
    private int pointSum;
    private Bundle nextDeviceBundle;
    private InsOrderBean insOrder;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private List<CompMacBean> compMacList;
    private Map<Integer, String> compMacData;
    private Map<Integer, Integer> deviceIndex;
    private Date pointInsTime;
    private Date compMacInsTime;
    private int id;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    private String longi;
    private String lati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        //判断安卓版本 动态请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        ButterKnife.bind(this);
        lati = null;
        longi = null;
        deviceIndex = DeviceIndexUtil.getInstance();
        helper = DbManager.getInstance(this);
        insOrder = InsOrderInfo.getInstance();
        initLocation();
        surveyButtonClick();
        skipButtonClick();
        saveButtonClick();
        cleanButtonClick();
        historyButtonClick();
        connBluetButtonClick();
        bleManager = BleManager.getBleManager(DeviceInfoActivity.this, DeviceInfoActivity.this);
        this.onConnProcessChange(bleManager.mConnectionState);

        initForDevice(getIntent().getExtras());

//        this.onReadSuccess("0200c80bc20d667eea00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    }

    private void initForDevice(Bundle bundle){
        deviceTag = bundle.getString("deviceTag");
        deviceId = bundle.getInt("id");
        //deviceId1 = bundle.getInt("deviceId");
        pointSum = bundle.getInt("pointSum");
        id = bundle.getInt("id");
        title.setText(deviceTag);
        compMacData = new HashMap<>();
        pointData = new ArrayList<>();
        info = new ArrayList<>();
        compMacList = new ArrayList<>();
        initList();
        //判断保存按钮是否可用
        checkEnable();
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
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }
    private void checkEnable() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select deviceId from "+DbConstant.DEVICE_TABLE+" where deviceTag=?", new String[]{
                deviceTag
        });
        if(cursor.moveToFirst()){
            int deviceId = cursor.getInt(cursor.getColumnIndex("deviceId"));
            Cursor temp_cursor = db.rawQuery("select count(*) from "+DbConstant.POINT_TABLE+" where deviceId="+deviceId, null);
            if(temp_cursor.moveToFirst()){
                int count = temp_cursor.getInt(0);
                if(count == 0 && pointSum > 0){
                    saveButton.setEnabled(false);
                    cleanButton.setEnabled(false);
                }
            }
            temp_cursor.close();
        }
        cursor.close();
    }

    private void initList() {
        info.clear();
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select deviceId from " + DbConstant.DEVICE_TABLE + " where deviceTag=?", new String[]{
                deviceTag
        });
        if (cursor.moveToFirst()) {
            int deviceId = cursor.getInt(cursor.getColumnIndex("deviceId"));
            cursor = db.rawQuery("select * from " + DbConstant.POINT_TABLE + " where deviceId=" + deviceId, null);
            while (cursor.moveToNext()) {
                String tempe = cursor.getString(cursor.getColumnIndex("temperature"));
                String vibra = cursor.getString(cursor.getColumnIndex("vibration"));
                String speed = cursor.getString(cursor.getColumnIndex("speed"));
                String accele = cursor.getString(cursor.getColumnIndex("acceleration"));
//                String data = speed+"     "+tempe+"   "+vibra+"  "+accele+"   ";
                ObservationsPointsBean point = new ObservationsPointsBean(speed, tempe, vibra, accele);
                info.add(point);

            }
            pointsInfo.setAdapter(new ObservationsPointsAdapter(this, info));
        }
        db.close();
        if (info.isEmpty()) {
            for (int i = 0; i < pointSum; i++) {
                info.add(new ObservationsPointsBean(null, null, "无数据", null));
            }
        }
        pointsInfo.setAdapter(new ObservationsPointsAdapter(this, info));

        getComMacData();
        listView.setAdapter(new DataAdapter(this, compMacList));

        cursor.close();
    }

    private void getComMacData() {
        insOrder = InsOrderInfo.getInstance();
        OrderInfoBean order = insOrder.getOrderInfo();
        List<DeviceBean> list = order.getInsRoute();
        for (DeviceBean d : list) {
            if (d.getDeviceTag().equals(deviceTag)) {
                compMacList = d.getCompMacs();
            }
        }
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select deviceId from " + DbConstant.DEVICE_TABLE + " where deviceTag=?", new String[]{
                deviceTag
        });
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int i = 0;
            cursor = db.rawQuery("select * from " + DbConstant.COMPMAC_TABLE + " where deviceId=" + id, null);
            while (cursor.moveToNext()) {
                compMacData.put(i, cursor.getString(cursor.getColumnIndex("value")));
                i++;
            }
        }
        cursor.close();
//        Log.i("data", compMacData.toString());
    }

    //连接按钮点击事件
    public void connBluetButtonClick() {
        connBluetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String connBluetButtonTitle = connBluetButton.getText().toString();
                if ("连接".equals(connBluetButtonTitle)) {
                    Log.i(TAG, "连接");
                    bleManager.startLeScan();
                } else if ("停止".equals(connBluetButtonTitle)) {
                    Log.i(TAG, "停止");
                    bleManager.stopLeScan();
                    bleManager.disConnect();
                } else if ("断开".equals(connBluetButtonTitle)) {
                    Log.i(TAG, "断开");
                    bleManager.disConnect();
                }
            }
        });
    }

    //测量按钮点击事件
    public void surveyButtonClick(){
        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bleManager.readCharacteristic();
                pointInsTime = new Date();
                saveButton.setEnabled(true);
            }
        });
        //长按弹窗
        surveyButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bleManager.CharacteristicNotification(true);
                showPopupWindow();
                return true;
            }
        });
    }

    //历史数据按钮点击事件
    public void historyButtonClick() {
        historyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select deviceId from "+DbConstant.DEVICE_TABLE+" where deviceTag=?", new String[]{
                        deviceTag
                });
                if(cursor.moveToFirst()) {
                    int deviceId1 = cursor.getInt(cursor.getColumnIndex("deviceId"));
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(deviceId1));
                    Intent intent = new Intent(DeviceInfoActivity.this, HistoryDataChartActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                cursor.close();
            }
        });
    }

    //保存按钮点击事件
    private void saveButtonClick() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean last = false;
                SQLiteDatabase db = helper.getWritableDatabase();
                db.beginTransaction();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Cursor cursor = db.rawQuery("select deviceId from " + DbConstant.DEVICE_TABLE + " where deviceTag=?", new String[]{
                            deviceTag
                    });
                    compMacInsTime = new Date();
                    if (cursor.moveToFirst()) {
                        int deviceId = deviceIndex.get(id);
                        Cursor pointCursor = db.rawQuery("select * from " + DbConstant.POINT_TABLE + " where deviceId=" + deviceId, null);
                        if(pointSum == 0) {
                            Cursor comCursor = db.rawQuery("select * from "+DbConstant.COMPMAC_TABLE+" where deviceId="+deviceId,null);
                            if(comCursor.moveToFirst()){
                                for (int i = 0; i < compMacList.size(); i++) {
                                    String name = compMacList.get(i).getName();
                                    String value = compMacData.get(i);
                                    db.execSQL("update " + DbConstant.COMPMAC_TABLE + " set value='" + value + "' where deviceId=" + deviceId + " and name='" + name + "'");
                                }
                                Toast.makeText(DeviceInfoActivity.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                            }else{
                                for (int i = 0; i < compMacList.size(); i++) {
                                    String name = compMacList.get(i).getName();
                                    String value = compMacData.get(i);
                                    db.execSQL("insert into " + DbConstant.COMPMAC_TABLE + " (name, value, deviceId) values (?, ?, ?)", new Object[]{
                                            name,
                                            value,
                                            deviceId
                                    });
                                }
                                if (pointInsTime != null) {
                                    db.execSQL("update " + DbConstant.DEVICE_TABLE + " set pointInsTime='" + simpleDateFormat.format(pointInsTime) + "' where deviceId=" + deviceId);
                                }
                                String sql = "update " + DbConstant.DEVICE_TABLE + " set compMacInsTime='" + simpleDateFormat.format(compMacInsTime) + "' where deviceId=" + deviceId;
                                db.execSQL(sql);
                                Toast.makeText(DeviceInfoActivity.this, "数据保存成功", Toast.LENGTH_SHORT).show();
                            }
                            comCursor.close();
                        }else{
                            if (!pointCursor.moveToFirst()) {
                                if (pointSum == pointData.size()) {
                                    for (int i = 0; i < pointSum; i++) {
                                        db.execSQL("insert into " + DbConstant.POINT_TABLE + " (pointName, insTime, temperature,vibration,speed,acceleration,deviceId) values(?,?,?,?,?,?,?)", new Object[]{
                                                i + 1,
                                                simpleDateFormat.format(new Date()),
                                                pointData.get(i).getTemperature(),
                                                pointData.get(i).getVibration(),
                                                pointData.get(i).getSpeed(),
                                                pointData.get(i).getAcceleration(),
                                                deviceId

                                        });
                                    }
                                }else if (pointSum > pointData.size()) {
                                    for (int i = 0; i < pointSum; i++) {
                                        if (i < pointData.size()) {
                                            db.execSQL("insert into " + DbConstant.POINT_TABLE + " (pointName,insTime, temperature,vibration,speed,acceleration,deviceId) values(?,?,?,?,?,?,?)", new Object[]{
                                                    i + 1,
                                                    simpleDateFormat.format(new Date()),
                                                    pointData.get(i).getTemperature(),
                                                    pointData.get(i).getVibration(),
                                                    pointData.get(i).getSpeed(),
                                                    pointData.get(i).getAcceleration(),
                                                    deviceId
                                            });
                                        }else{
                                            db.execSQL("insert into " + DbConstant.POINT_TABLE + " (pointName,insTime,temperature,vibration,speed,acceleration,deviceId) values(?,?,?,?,?,?,?)", new Object[]{
                                                    i + 1,
                                                    simpleDateFormat.format(new Date()),
                                                    "0",
                                                    "0",
                                                    "0",
                                                    "0",
                                                    deviceId
                                            });
                                        }

                                    }
                                    if (pointInsTime != null) {
                                        db.execSQL("update " + DbConstant.DEVICE_TABLE + " set pointInsTime='" + simpleDateFormat.format(pointInsTime) + "' where deviceId=" + deviceId);
                                    }
                                    String sql = "update " + DbConstant.DEVICE_TABLE + " set compMacInsTime='" + simpleDateFormat.format(compMacInsTime) + "' where deviceId=" + deviceId;
                                    db.execSQL(sql);
                                } else {
                                    for (int i = 0; i < pointSum; i++) {
                                        db.execSQL("insert into " + DbConstant.POINT_TABLE + " (pointName,insTime,temperature,vibration,speed,acceleration,deviceId) values(?,?,?,?,?,?,?)", new Object[]{
                                                i + 1,
                                                simpleDateFormat.format(new Date()),
                                                pointData.get(i).getTemperature(),
                                                pointData.get(i).getVibration(),
                                                pointData.get(i).getSpeed(),
                                                pointData.get(i).getAcceleration(),
                                                deviceId
                                        });
                                    }
                                }
                                for (int i = 0; i < compMacList.size(); i++) {
                                    String name = compMacList.get(i).getName();
                                    String value = compMacData.get(i);
                                    db.execSQL("insert into " + DbConstant.COMPMAC_TABLE + " (name, value, deviceId) values (?, ?, ?)", new Object[]{
                                            name,
                                            value,
                                            deviceId
                                    });
                                }
                                if (pointInsTime != null) {
                                    db.execSQL("update " + DbConstant.DEVICE_TABLE + " set pointInsTime='" + simpleDateFormat.format(pointInsTime) + "' where deviceId=" + deviceId);
                                }
                                String sql = "update " + DbConstant.DEVICE_TABLE + " set compMacInsTime='" + simpleDateFormat.format(compMacInsTime) + "' where deviceId=" + deviceId;
                                db.execSQL(sql);
                                Toast.makeText(DeviceInfoActivity.this, "数据保存成功", Toast.LENGTH_SHORT).show();
                            } else{
                                for (int i = 0; i < pointData.size(); i++) {
                                    if (i < pointSum) {
                                        db.execSQL("update " + DbConstant.POINT_TABLE + " set temperature=?, vibration=?, speed=?, acceleration=? where deviceId=? and pointName=?", new Object[]{
                                                pointData.get(i).getTemperature(),
                                                pointData.get(i).getVibration(),
                                                pointData.get(i).getSpeed(),
                                                pointData.get(i).getAcceleration(),
                                                deviceId,
                                                i + 1
                                        });
                                    }


                                }
                                for (int i = 0; i < compMacList.size(); i++) {
                                    String name = compMacList.get(i).getName();
                                    String value = compMacData.get(i);

                                    db.execSQL("update " + DbConstant.COMPMAC_TABLE + " set value='" + value + "' where deviceId=" + deviceId + " and name='" + name + "'");
                                }
                                Toast.makeText(DeviceInfoActivity.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                            }

                        }
                        db.execSQL("update " + DbConstant.DEVICE_TABLE + " set status=? where deviceId=?", new Object[]{
                                1,
                                deviceId
                        });

                        last = searchAndTO(db);
                        db.setTransactionSuccessful();
                        //上传地理位置
                        boolean chen = NetworkDetector.detect(DeviceInfoActivity.this);
                        if(!chen){

                        }else {
                            uploadLocation(longi,lati, deviceId);
                        }

                        pointCursor.close();
                    } else {
                        Toast.makeText(DeviceInfoActivity.this, "未找到设备", Toast.LENGTH_LONG).show();

                    }

                    cursor.close();

                } finally {
                    db.endTransaction();
                    db.close();

                }
                if(!last)initForDevice(nextDeviceBundle);

            }
        });
    }
    //点击清除按钮
    private void cleanButtonClick() {
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean last = false;
                SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select deviceId from " + DbConstant.DEVICE_TABLE + " where deviceTag=?", new String[]{
                        deviceTag
                    });
                if (cursor.moveToFirst()) {
                    int deviceId = cursor.getInt(cursor.getColumnIndex("deviceId"));

                    db.beginTransaction();
                    try {
                        db.execSQL("delete from " + DbConstant.POINT_TABLE +" where deviceId=?" ,new Object[]{
                                deviceId
                        });
                        db.execSQL("update " + DbConstant.DEVICE_TABLE + " set status=? where deviceId=?", new Object[]{
                                0,
                                deviceId
                        });
                        db.execSQL("delete from "+DbConstant.COMPMAC_TABLE+" where deviceId=?" ,new Object[]{
                                deviceId
                        });
                        Toast.makeText(DeviceInfoActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                        last = searchAndTO(db);
                        db.setTransactionSuccessful();
                    }finally {
                        db.endTransaction();
                    }
                    cursor.close();
                    db.close();
                    if(!last)initForDevice(nextDeviceBundle);
                }
            }
        });
    }

    //跳过并进入下一个设备
    private boolean searchAndTO(SQLiteDatabase db1) {
        boolean last = false;
        SQLiteDatabase db = db1;
        Cursor cursor = db.rawQuery("select deviceId from devicesinfo where deviceTag=?", new String[]{
                deviceTag
        });
        if (cursor.moveToFirst()) {
            int d_id = deviceIndex.get(id + 1) == null ? 100000 : deviceIndex.get(id + 1);
//            int id = cursor.getInt(cursor.getColumnIndex("deviceId"))+1;
            cursor = db.rawQuery("select deviceTag,pointSum from devicesinfo where deviceId=" + d_id, null);
            if (cursor.moveToFirst()) {
                Bundle bundle = new Bundle();
                bundle.putString("deviceTag", cursor.getString(cursor.getColumnIndex("deviceTag")));
                bundle.putInt("pointSum", cursor.getInt(cursor.getColumnIndex("pointSum")));
                bundle.putInt("id", id + 1);
                Intent intent = new Intent(DeviceInfoActivity.this, DeviceInfoActivity.class);
                intent.putExtras(bundle);
                nextDeviceBundle = bundle;
//                initForDevice(bundle);
//                startActivity(intent);
//                finish();
            } else {
                Toast.makeText(DeviceInfoActivity.this, "已经是最后一台设备", Toast.LENGTH_SHORT).show();
                last = true;
            }
        } else {
            Toast.makeText(DeviceInfoActivity.this, "数据获取错误", Toast.LENGTH_SHORT).show();
            last = true;
        }
        cursor.close();
        return last;
    }

    //跳过按钮点击事件
    private void skipButtonClick() {
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean last = false;
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select deviceId, status from " + DbConstant.DEVICE_TABLE + " where deviceTag=?", new String[]{
                        deviceTag
                });
                if (cursor.moveToFirst()) {
                    int deviceId = cursor.getInt(cursor.getColumnIndex("deviceId"));
                    int status = cursor.getInt(cursor.getColumnIndex("status"));
                    if (status != 1) {
                        db.beginTransaction();
                        try {
                            db.execSQL("update " + DbConstant.DEVICE_TABLE + " set status=? where deviceId=?", new Object[]{
                                    -1,
                                    deviceId

                            });
                            last = searchAndTO(db);
                            db.setTransactionSuccessful();
                        } finally {
                            db.endTransaction();

                        }
                    } else {
                        last = searchAndTO(db);
                    }
                    db.close();
                    //上传地理位置

                    boolean chen = NetworkDetector.detect(DeviceInfoActivity.this);
                    if(!chen){

                    }else {
                        uploadLocation(longi,lati, deviceId);
                    }
                    if(!last)initForDevice(nextDeviceBundle);
                }
                cursor.close();
            }

        });

    }


    private void uploadLocation(String longi,String lati,int deviceId){
        Log.i("location", longi+"----"+lati);
        if(longi!=null && lati!=null){
            int dispaOrderID = InsOrderInfo.getInstance().getOrderInfo().getOrderId();
            int eqptAccntID = deviceId;
            String url = UrlConstant.locationURL+"?dispaOrderID="+dispaOrderID+"&eqptAccntID="+eqptAccntID+"&longi="+longi+"&lati="+lati;
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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    //
//    /**
//     * LocationListern监听器
//     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
//     */
//
//    LocationListener locationListener =  new LocationListener() {
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle arg2) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            //如果位置发生变化,重新显示
//            showLocation(location);
//
//        }
//    };
    private class ObservationsPointsAdapter extends BaseAdapter {
        private List<ObservationsPointsBean> dataList;
        private LayoutInflater layoutInflater;

        public ObservationsPointsAdapter(Context context, List<ObservationsPointsBean> dataList) {
            this.dataList = dataList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override

        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.observation_point, null);

            }
            TextView infoSpeed  = convertView.findViewById(R.id.tv_point_speed);
            TextView infoTitle = convertView.findViewById(R.id.tv_point_title);
            infoTitle.setText(String.valueOf(i + 1));
            TextView infoTempe = convertView.findViewById(R.id.tv_point_tempe);
            TextView infoVibra = convertView.findViewById(R.id.tv_point_vibra);
            TextView infoAccele = convertView.findViewById(R.id.tv_point_accele);
            infoSpeed.setText(dataList.get(i).getSpeed());
            infoTempe.setText(dataList.get(i).getTempe());
            infoVibra.setText(dataList.get(i).getVibra());
            infoAccele.setText(dataList.get(i).getAccele());
            return convertView;


        }
    }
    private class DataAdapter extends BaseAdapter {
        private List<CompMacBean> dataList;
        private LayoutInflater layoutInflater;
        private Context context;

        public DataAdapter(Context context, List<CompMacBean> dataList) {
            this.dataList = dataList;
            layoutInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            if(!dataList.get(i).isSelectable()){
                convertView = layoutInflater.inflate(R.layout.item_text, null);
                TextView textView = convertView.findViewById(R.id.tv_data_key);
                textView.setText(dataList.get(i).getName());
                EditText et = convertView.findViewById(R.id.tv_data_value);
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        compMacData.put(i, editable.toString());
                    }
                });
                et.setText(compMacData.get(i));
                et.setText(compMacData.get(i));
            }else{
                convertView = layoutInflater.inflate(R.layout.item_spinner, null);
                TextView tv = convertView.findViewById(R.id.spinner_data_key);
                tv.setText(dataList.get(i).getName());
                final Spinner spinner = convertView.findViewById(R.id.spinner_data_value);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i1, long l) {
                        compMacData.put(i, (String) spinner.getSelectedItem());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner, dataList.get(i).getOptions());
                spinner.setAdapter(adapter);
                int position = adapter.getPosition(compMacData.get(i));
                spinner.setSelection(position);
            }

            return convertView;
        }
    }


    //--------------------------蓝牙方面代码
    private final static String TAG = "DeviceInfoActivicy";

    @Override
    public void onReadSuccess(String value) {
//        Log.i(TAG,value);
        pointData = DataProcessing.convertBleDataToList(value);
        //判断测点匹配情况
        if(pointData.size() == pointSum){
            info.clear();
            for(ObservationsPoint point:pointData) {
//                ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
//                        point.getSpeed(),point.getTemperature(),point.getVibration(),point.getAcceleration());
                ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
                        new DecimalFormat("######0.0").format(point.getSpeed()),
                                new DecimalFormat("########0.0").format(point.getTemperature()),
                                new DecimalFormat("######0.000").format(point.getVibration()),
                                new DecimalFormat("######0.0").format(point.getAcceleration()));
                info.add(observationsPointsBean);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pointsInfo.setAdapter(new ObservationsPointsAdapter(DeviceInfoActivity.this, info));
                }
            });

            byte[] writeBytes = new byte[1];
            writeBytes[0] = 0x01;
            bleManager.write(writeBytes);
        }
        else if(pointData.size() > pointSum){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(DeviceInfoActivity.this);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = dialog.setTitle("警告")
                            .setMessage("测点数量不匹配")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("强制加载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    info.clear();
                                    for (int k = 0; k < pointSum; k++) {
//                                        ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
//                                                pointData.get(k).getSpeed()+"     "+pointData.get(k).getTemperature()+"     "+pointData.get(k).getVibration()+"     "+pointData.get(k).getAcceleration()+" ");
                                        ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
                                                new DecimalFormat("######0.0").format(pointData.get(k).getSpeed()),
                                                new DecimalFormat("########0.0").format(pointData.get(k).getTemperature()),
                                                new DecimalFormat("######0.000").format(pointData.get(k).getVibration()),
                                                new DecimalFormat("######0.0").format(pointData.get(k).getAcceleration()));
                                        info.add(observationsPointsBean);
                                    }
//                                    Log.i(TAG,new Gson().toJson(info));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pointsInfo.setAdapter(new ObservationsPointsAdapter(DeviceInfoActivity.this, info));
                                        }
                                    });

                                    byte[] writeBytes = new byte[1];
                                    writeBytes[0] = 0x01;
                                    bleManager.write(writeBytes);
                                }
                            }).create();
                    if(!alertDialog.isShowing())
                        alertDialog.show();
                }
            });

        }
        else if(pointData.size() < pointSum){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(DeviceInfoActivity.this);
//            byte[] writeBytes = new byte[1];
//            writeBytes[0] = 0x01;
//            bleManager.write(writeBytes);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = dialog.setTitle("警告")
                            .setMessage("测点数据太少").create();
                    if(!alertDialog.isShowing())
                        alertDialog.show();
                }
            });
            System.out.println("dialog = " + dialog);
        }

    }

    @Override
    public void onConnProcessChange(final int newStatus) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (newStatus){
                    case BleManager.STATE_CONNECTED:
                        surveyButton.setEnabled(true);
                        connBluetButton.setEnabled(true);
                        connBluetButton.setText("断开");
                        bluetConnStatus.setText("已连接");
                        break;
                    case BleManager.STATE_FIND_DEVICE:
                        surveyButton.setEnabled(false);
                        connBluetButton.setEnabled(true);
                        connBluetButton.setText("停止");
                        bluetConnStatus.setText("寻找设备...");
                        break;
                    case BleManager.STATE_DISCOVER_SERVER:
                        surveyButton.setEnabled(false);
                        connBluetButton.setEnabled(true);
                        connBluetButton.setText("停止");
                        bluetConnStatus.setText("发现服务...");
                        break;
                    case BleManager.STATE_DISCONNECTED:
                        surveyButton.setEnabled(false);
                        connBluetButton.setEnabled(true);
                        connBluetButton.setText("连接");
                        bluetConnStatus.setText("未连接");
                        break;
                }
            }
        });
    }

    @Override
    public void onNotification(short[] sampleIntData) {
        //mData就是傅里叶变换之后的数据。

        mData =  FFT.getFrequency(sampleIntData);
        Log.d("FFT","mData[]-------->"+mData[2].toString()+"/n"+mData[3].toString()+
                "/n"+mData[4].toString()+"/n"+mData[5].toString()+"/n"+mData[6].toString()
                +"/n"+mData[7].toString());
        mHandler.sendEmptyMessageDelayed(handler_key.UPDATE_UI.ordinal(),500);
    }

    @Override
    public void onReadLength(byte[] sampleAndLength) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }
    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(DeviceInfoActivity.this).inflate(R.layout.activity_fft, null);
        mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setOutsideTouchable(false);
        mPopWindow.setFocusable(true);
        mPopWindow.setContentView(contentView);

        visualizerView = contentView.findViewById(R.id.fft_view);
        visualizerView.SetInfo(
                new String[]{"0","5","10","15","20","25","30","35","40","45",
                        "50","55","60", "65"},   //X轴刻度
                new String[]{"","50","100","150","200","250","300","350"},   //Y轴刻度
                new String[]{"150","230","10","136","45","40","112","313"},
                "波形图");
        TextView close_pop = contentView.findViewById(R.id.close_characteristic_pop);
        close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleManager.CharacteristicNotification(false);
                mPopWindow.dismiss();
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(DeviceInfoActivity.this).inflate(R.layout.activity_device_info, null);
        //弹窗距离底边
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 330);

    }
    private enum handler_key {

        //更新界面
        UPDATE_UI,

        DISCONNECT,
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                case UPDATE_UI:
                    updataUI();
                    break;
                case DISCONNECT:
                    //蓝牙断开
                    break;
            }
        }
    };

    private void updataUI() {
        visualizerView.updateVisualizer(mData);
    }

}
