package edu.sdust.insapp.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.DeviceBean;
import edu.sdust.insapp.bean.InsOrderBean;
import edu.sdust.insapp.bean.OrderInfoBean;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.InsOrderInfo;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.PostUploadRequest;
import edu.sdust.insapp.utils.UploadUtils;
import edu.sdust.insapp.utils.UrlConstant;

public class InspectionActivity extends AppCompatActivity {
    @BindView(R.id.lv_inspection_order_info)
    ListView OrderInfo;
    @BindView(R.id.btn_inspection_start)
    Button startIns;
    @BindView(R.id.btn_inspection_upload)
    Button uploadButton;
    private List<Map<String, Object>> data_list;
    private OrderDBHelper helper;
    private InsOrderBean insOrder;
    private SQLiteDatabase db;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;//偏好设置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        ButterKnife.bind(this);
        insOrder = InsOrderInfo.getInstance();
        helper = DbManager.getInstance(this);
        initList();
        initBluetooth();
        uploadButtonClick();
        verifyStoragePermissionsCAMERA(this);
    }

    //获取拍照权限
    public static void verifyStoragePermissionsCAMERA(Activity activity) {
        try {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                        1);}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //上传巡检数据
    private void uploadButtonClick() {

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(InspectionActivity.this);
                dialog.setTitle("巡检结果")
                        .setMessage("是否上传？")
                        .setPositiveButton("上传", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                uploadData();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                dialog.show();
            }
        });
    }
    //上传
    private void uploadData(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+DbConstant.DEVICE_TABLE+" where status=0", null);
        if(!cursor.moveToFirst()){
            try {
                String params = UploadUtils.getUploadParams(InspectionActivity.this);
                HashMap<String, String> stringMap = new HashMap<>();
                stringMap.put("params", params);
                LinkedList<String[]> files = UploadUtils.getFiles(InspectionActivity.this);
                MyApplication.getHttpQueue().add(new PostUploadRequest(UrlConstant.uploadURL, files, stringMap
                        , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int resultCode = response.getInt("result");
                            if (resultCode == 0) {
                                Toast.makeText(InspectionActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                editor.clear();
                                editor.commit();
                                db = helper.getReadableDatabase();
                                db.beginTransaction();
                                try {
                                    db.execSQL("delete from " + DbConstant.DEVICE_TABLE);
                                    db.execSQL("delete from " + DbConstant.POINT_TABLE);
                                    db.execSQL("delete from " + DbConstant.DEVICE_FAULT_TABLE);
                                    db.execSQL("delete from " + DbConstant.NON_DEVICE_FAULT_TABLE);
                                    db.execSQL("delete from " + DbConstant.PICTURE_TABLE);
                                    db.execSQL("delete from " + DbConstant.COMPMAC_TABLE);
                                    db.execSQL("delete from "+DbConstant.POSITION_TABLE);
                                    db.setTransactionSuccessful();
                                    Intent intent = new Intent(InspectionActivity.this, mainView.class);
                                    startActivity(intent);
                                } finally {
                                    db.endTransaction();
                                    db.close();
                                }
                            } else if (resultCode == -1) {
                                Toast.makeText(InspectionActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                            } else if (resultCode == -2) {
                                Toast.makeText(InspectionActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            } else if (resultCode == -3) {
                                Toast.makeText(InspectionActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                            } else if (resultCode == -20) {
                                Toast.makeText(InspectionActivity.this, "工单不存在", Toast.LENGTH_SHORT).show();
                            } else if (resultCode == -21) {
                                Toast.makeText(InspectionActivity.this, "工单状态异常", Toast.LENGTH_SHORT).show();
                            } else if (resultCode == -22) {
                                Toast.makeText(InspectionActivity.this, "内部错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(InspectionActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();

                    }
                }));
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {

            }
            db.close();
        }else{
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("存在未巡检设备，无法上传数据。")
                    .create();
            dialog.show();
        }


    }
    //启动蓝牙
    private void initBluetooth() {
        BluetoothManager mBluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager != null) {
            BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();  //打开蓝牙
                }
            }
        }
    }
    //初始化派工单
    private void initList() {
        OrderInfoBean orderInfoBean = insOrder.getOrderInfo();
        String[] keys = {
                "派工单编号",
                "派工单描述",
                "调度派工时间",
                "班组派工时间",
                "巡检路线名",
                "巡检路线",
                "巡检人员"
        };
        List<String> devices = new ArrayList<>();
        sp=getSharedPreferences("TestCache",MODE_PRIVATE);
        editor=sp.edit();
        List<DeviceBean> deviceBeen = orderInfoBean.getInsRoute();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        for(DeviceBean d:deviceBeen){
            devices.add(d.getDeviceTag());
        }
        String d = devices.toString();
        String e = orderInfoBean.getInsPeople().toString();
        if(orderInfoBean.getInsRouteName() == null || d == null || e == null){
            if(orderInfoBean.getInsRouteName() == null){
                Toast.makeText(InspectionActivity.this, "派工单巡检路线号为空", Toast.LENGTH_SHORT).show();
            }else if(d == null){
                Toast.makeText(InspectionActivity.this, "派工单无巡检设备", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(InspectionActivity.this, "派工单无巡检人员", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        Object[] values = {
                orderInfoBean.getOrderId(),
                orderInfoBean.getDescription()!=null?orderInfoBean.getDescription():"无描述信息",
                orderInfoBean.getDispatchingTime()!=null?simpleDateFormat.format(orderInfoBean.getDispatchingTime()):"无",
                orderInfoBean.getSecondaryDispatchingTime()!=null?simpleDateFormat.format(orderInfoBean.getSecondaryDispatchingTime()):"无",
                orderInfoBean.getInsRouteName(),
                d.substring(1, d.length()-1),
                e.substring(1, e.length()-1)
        };
        //String de =TimeConvert.parseHour(String.valueOf(orderInfoBean.getDispatchingTime()));
        //db = helper.getWritableDatabase();
        //db.beginTransaction();
//        try {
//            String infor_sql = "insert into " + DbConstant.ORDERINFO_TABLE + " (orderId,insRouteName, dispatchingTime, secondaryDispatchingTime, description,insPeople) VALUES (?,?,?,?,?,?);";
//            Object[] params = {
//                    orderInfoBean.getOrderId(),
//                    orderInfoBean.getInsRouteName(),
//                    simpleDateFormat.format(orderInfoBean.getSecondaryDispatchingTime()),
//                    //orderInfoBean.getDispatchingTime(),
//                    //de,
//                    //shsi,
//                    orderInfoBean.getSecondaryDispatchingTime(),
//                    orderInfoBean.getDescription(),
//                    e.substring(1, e.length()-1)
//            };
//            //Log.i("时间", String.valueOf(de));
//            //Log.i("语句",infor_sql);
//            db.execSQL(infor_sql, params);
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//            db.close();
//        }

        data_list = new ArrayList<Map<String, Object>>();
        getData(keys, values);

        String[] from = {"key", "value"};
        int[] to = {R.id.tv_inspection_order_item_key, R.id.tv_inspection_order_item_value};
        OrderInfo.setAdapter(new SimpleAdapter(this, data_list, R.layout.inspection_order_child, from, to));
        startIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsOrderBean ins = InsOrderInfo.getInstance();
                final OrderInfoBean orderInfoBean = ins.getOrderInfo();
                final int orderId = orderInfoBean.getOrderId();
                db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from " + DbConstant.DEVICE_TABLE + " where orderId="+orderId+" and status=1 or status=-1", null);
                Cursor cursor1 = db.rawQuery("select * from "+DbConstant.DEVICE_FAULT_TABLE, null);
                Cursor cursor2 = db.rawQuery("select * from "+DbConstant.NON_DEVICE_FAULT_TABLE, null);
                if (cursor.moveToFirst() || cursor1.moveToFirst() || cursor2.moveToFirst()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(InspectionActivity.this);
                    dialog.setTitle("警告")
                            .setMessage("已存在设备测点数据，是否恢复?")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(InspectionActivity.this, DeviceListActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    editor.clear();
                                    editor.commit();
                                    db.beginTransaction();
                                    try {
                                        db.execSQL("delete from " + DbConstant.POINT_TABLE);
                                        db.execSQL("delete from " + DbConstant.DEVICE_TABLE);
                                        db.execSQL("delete from "+DbConstant.DEVICE_FAULT_TABLE);
                                        db.execSQL("delete from "+DbConstant.NON_DEVICE_FAULT_TABLE);
                                        db.execSQL("delete from "+DbConstant.PICTURE_TABLE);
                                        db.execSQL("delete from "+DbConstant.COMPMAC_TABLE);
                                        db.execSQL("delete from "+DbConstant.POSITION_TABLE);
                                        //db.execSQL("delete from "+DbConstant.DOWNLOADDEVICE_TABLE);
                                        db.setTransactionSuccessful();
                                    } finally {
                                        db.endTransaction();
                                        db.close();
                                    }
                                    List<DeviceBean> insRoute = orderInfoBean.getInsRoute();
                                    int count = 1;
                                    for (DeviceBean d : insRoute) {

                                        db = helper.getWritableDatabase();
                                        db.beginTransaction();
                                        try {
                                            String ins_sql = "insert into " + DbConstant.DEVICE_TABLE + " (deviceId, countId, deviceTag, pointSum, orderId) "
                                                    + "select ?,?,?,?,? where not exists (select * from " + DbConstant.DEVICE_TABLE + " where deviceId=?)";
                                            Object[] params = {
                                                    d.getDeviceId(),
                                                    count,
                                                    d.getDeviceTag(),
                                                    d.getPointSum(),
                                                    orderId,
                                                    d.getDeviceId()
                                            };
                                            db.execSQL(ins_sql, params);
                                            db.setTransactionSuccessful();
                                        } finally {
                                            db.endTransaction();
                                            db.close();
                                        }
                                        count ++;
                                    }
                                    Intent intent = new Intent(InspectionActivity.this, DeviceListActivity.class);
                                    startActivity(intent);
                                }
                            }).create();
                    dialog.show();
                }else{
                    db.beginTransaction();
                    try {
                        db.execSQL("delete from " + DbConstant.POINT_TABLE);
                        db.execSQL("delete from " + DbConstant.DEVICE_TABLE);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                        db.close();
                    }
                    List<DeviceBean> insRoute = orderInfoBean.getInsRoute();
                    int count = 1;
                    for (DeviceBean d : insRoute) {
                        db = helper.getWritableDatabase();
                        db.beginTransaction();
                        try {
                            String ins_sql = "insert into " + DbConstant.DEVICE_TABLE + " (deviceId, countId, deviceTag, pointSum, orderId) "
                                    + "select ?,?,?,?,? where not exists (select * from " + DbConstant.DEVICE_TABLE + " where deviceId=?)";
                            Object[] params = {
                                    d.getDeviceId(),
                                    count,
                                    d.getDeviceTag(),
                                    d.getPointSum(),
                                    orderId,
                                    d.getDeviceId()
                            };
                            db.execSQL(ins_sql, params);
                            db.setTransactionSuccessful();
                        } finally {
                            db.endTransaction();
                            db.close();
                        }
                        count ++;
                    }
                    Intent intent = new Intent(InspectionActivity.this, DeviceListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private List<Map<String, Object>> getData(String[] keys, Object[] values){
        for(int i=0;i<keys.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", keys[i]);
            map.put("value", values[i]);
            data_list.add(map);
        }

        return data_list;
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this,"1",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, mainView.class);
        startActivity(intent);
        this.finish();
    }
}
