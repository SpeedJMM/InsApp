package edu.sdust.insapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;
import com.netease.imageSelector.ImageSelector;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import edu.sdust.insapp.bluetooth.ObservationsPoint;
import edu.sdust.insapp.utils.BleManager;
import edu.sdust.insapp.utils.Complex;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.DeviceIndexUtil;
import edu.sdust.insapp.utils.ImageUtils;
import edu.sdust.insapp.utils.InsOrderInfo;
import edu.sdust.insapp.utils.MyAdapter;
import edu.sdust.insapp.utils.MyVisualView;
import edu.sdust.insapp.utils.NetworkDetector;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

import static com.netease.imageSelector.ImageSelectorConstant.OUTPUT_LIST;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_IMAGE;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_PREVIEW;

public class EleDeviceInfoActivity extends AppCompatActivity implements MyAdapter.MyAdapterListener {
    //整机数据
    @BindView(R.id.lv_device_info_data)
    RecyclerView recyclerView;
    //测点信息
    //@BindView(R.id.lv_device_info_observation_points_info)
    //ListView pointsInfo;
    //历史数据按钮
    //@BindView(R.id.btn_device_info_history)
    //Button historyData;
    //测量按钮
    //@BindView(R.id.btn_device_info_survey)
    //Button surveyButton;
    //连接按钮
    //@BindView(R.id.btn_device_info_conn_bluet)
    //Button connBluetButton;
    //连接状态
    //@BindView(R.id.tv_device_info_bluet_conn_status)
    //TextView bluetConnStatus;
    //标题
    @BindView(R.id.tv_device_info_title)
    TextView title;
    //保存按钮
    @BindView(R.id.btn_device_info_save)
    Button saveButton;
    //清除按钮
    @BindView(R.id.btn_device_info_clean)
    Button cleanButton;
    //跳过按钮
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
    private GridView mImagesWall, mProblemImagesWall;   //照片墙，显示勾选的照片
    private MyAdapter mAdapter, mProblemAdapter;
    private ArrayList<String> paths, problemPaths;
    private EditText other, problemOther;
    private List<Integer> attachmentIds, prolAttachmentIds;//保存对应PICTURE_TABLE Id的list
    private int imageWallId = 1;
    public static String CAMERA_PATH = Environment.getExternalStorageDirectory().getPath() + "/InsPhotoCache";//拍照路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ele_device_info);
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
        //initLocation();
        mImagesWall = findViewById(R.id.ngiv_ele_device_info_choose_picture);
        mProblemImagesWall = findViewById(R.id.ngiv_ele_device_problem_info_choose_picture);
        other = findViewById(R.id.et_ele_device_info_other);
        problemOther = findViewById(R.id.et_ele_device_problem_info_other);
        paths = new ArrayList<>();
        problemPaths = new ArrayList<>();
        attachmentIds = new ArrayList<>();
        prolAttachmentIds = new ArrayList<>();
        mAdapter = new MyAdapter(getApplicationContext(), paths, this);
        mProblemAdapter = new MyAdapter(getApplicationContext(), problemPaths, this);
        mImagesWall.setAdapter(mAdapter);
        mImagesWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position) != null){
                    // 跳转到预览界面
                    ImageSelector.getInstance().launchDeletePreview(EleDeviceInfoActivity.this, paths, position);
                }
            }
        });
        mProblemImagesWall.setAdapter(mProblemAdapter);
        mProblemImagesWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageSelector.getInstance().launchDeletePreview(EleDeviceInfoActivity.this, problemPaths, i);
            }
        });
        //surveyButtonClick();
        skipButtonClick();
        saveButtonClick();
        cleanButtonClick();
        //historyButtonClick();
        //connBluetButtonClick();
        //bleManager = BleManager.getBleManager(EleDeviceInfoActivity.this, EleDeviceInfoActivity.this);
        //this.onConnProcessChange(bleManager.mConnectionState);

        initForDevice(getIntent().getExtras());

//        this.onReadSuccess("0200c80bc20d667eea00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mlocationClient.onDestroy();
    }

    //读取电气巡检设备问题备注和图片
    private void setElecProblemRemarkPicture() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int deviceId = deviceIndex.get(id);
            String attachmentIds = null;
            Cursor problemRemarkcursor = db.rawQuery("select * from " + DbConstant.DEVICE_FAULT_TABLE + " where deviceId=" + deviceId, null);
            if (problemRemarkcursor.moveToFirst()) {
                problemOther.setText(problemRemarkcursor.getString(problemRemarkcursor.getColumnIndex("describe")));
                attachmentIds = problemRemarkcursor.getString(problemRemarkcursor.getColumnIndex("attachmentIds"));
            } else {
                problemOther.setText("");
            }
            problemRemarkcursor.close();
            ArrayList<String> picpaths = new ArrayList<>();
            List<Integer> problemAttachmentIds = new ArrayList<>();
            if (attachmentIds != null && !attachmentIds.equals(""))
            if (!attachmentIds.equals("[]")) {
                attachmentIds = attachmentIds.substring(1, attachmentIds.length() - 1);
                String[] t;
                t = attachmentIds.split(",");
                for (String s : t) {
                    s = s.trim();
                    problemAttachmentIds.add(Integer.valueOf(s));
                }
            }
            Cursor piccursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE+" where deviceId="+deviceId, null);
            while (piccursor.moveToNext()) {
                int id = piccursor.getInt(piccursor.getColumnIndex("_id"));
                for (int attachmentId : problemAttachmentIds) {
                    if (attachmentId == id) {
                        String path = piccursor.getString(piccursor.getColumnIndex("path"));
                        picpaths.add(path);
                    }
                }
            }
            piccursor.close();
            updateProblemImages(picpaths);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    //读取本地电气巡检备注和图片
    private void setElecRemarkPicture() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int deviceId = deviceIndex.get(id);
            String attachmentIds = null;
            Cursor elecRemarkcursor = db.rawQuery("select * from "+DbConstant.ELECINSPENCLO_TABLE+" where inspEqptId="+deviceId, null);
            if (elecRemarkcursor.moveToFirst()) {
                other.setText(elecRemarkcursor.getString(elecRemarkcursor.getColumnIndex("elecInspRemark")));
                attachmentIds = elecRemarkcursor.getString(elecRemarkcursor.getColumnIndex("elecInspEncloRoute"));
            } else {
                other.setText("");
            }
            elecRemarkcursor.close();
            ArrayList<String> picpaths = new ArrayList<>();
            List<Integer> elecInspEncloRoutes = new ArrayList<>();
            if (attachmentIds != null && !attachmentIds.equals(""))
                if (!attachmentIds.equals("[]")) {
                    attachmentIds = attachmentIds.substring(1, attachmentIds.length() - 1);
                    String[] t;
                    t = attachmentIds.split(",");
                    for (String s : t) {
                        s = s.trim();
                        elecInspEncloRoutes.add(Integer.valueOf(s));
                    }
                }
            Cursor piccursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE+" where deviceId="+deviceId, null);
            while (piccursor.moveToNext()) {
                int id = piccursor.getInt(piccursor.getColumnIndex("_id"));
                for (int attachmentId : elecInspEncloRoutes) {
                    if (attachmentId == id) {
                        String path = piccursor.getString(piccursor.getColumnIndex("path"));
                        picpaths.add(path);
                    }
                }
            }
            piccursor.close();
            updateImages(picpaths);
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        // 接收图片选择器返回结果，更新所选图片集合
        if (requestCode == REQUEST_PREVIEW || requestCode == REQUEST_IMAGE) {
            ArrayList<String> newFiles = data.getStringArrayListExtra(OUTPUT_LIST);
            if (newFiles != null) {
                if (imageWallId == 1) {
                    List<String> filesPath;
                    try {
                        filesPath = saveBitmap(newFiles);
                    } catch (FileNotFoundException e) {
                        Log.e("EleDeviceInfoActivity", "onActivityResult: ");
                        filesPath = newFiles;
                    }
                    updateImages(filesPath);
                } else if (imageWallId == 2) {
                    updateProblemImages(newFiles);
                }
            }
        }
    }

    /**
     * 更新所选图片集合
     *
     * @param images
     */
    private void updateImages(List<String> images) {
        if (paths == null) {
            paths = new ArrayList<>();
        }
        paths.clear();
        for (String s : images) {
            paths.add(s);
        }
        if (mAdapter == null) {
            mAdapter = new MyAdapter(EleDeviceInfoActivity.this, paths,(MyAdapter.MyAdapterListener) this);
        }
        mImagesWall.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    //更新设备问题图片集合
    private void updateProblemImages(List<String> images) {
        if (problemPaths == null) {
            problemPaths = new ArrayList<>();
        }
        problemPaths.clear();
        for (String s : images) {
            problemPaths.add(s);
        }
        if (mProblemAdapter == null) {
            mProblemAdapter = new MyAdapter(EleDeviceInfoActivity.this, problemPaths,(MyAdapter.MyAdapterListener) this);
        }
        mProblemImagesWall.setAdapter(mProblemAdapter);
        mProblemAdapter.notifyDataSetChanged();
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
    //判断保存按钮是否可用
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
//        info.clear();
//        db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select deviceId from " + DbConstant.DEVICE_TABLE + " where deviceTag=?", new String[]{
//                deviceTag
//        });
//        if (cursor.moveToFirst()) {
//            int deviceId = cursor.getInt(cursor.getColumnIndex("deviceId"));
//            cursor = db.rawQuery("select * from " + DbConstant.POINT_TABLE + " where deviceId=" + deviceId, null);
//            while (cursor.moveToNext()) {
//                String tempe = cursor.getString(cursor.getColumnIndex("temperature"));
//                String vibra = cursor.getString(cursor.getColumnIndex("vibration"));
//                String speed = cursor.getString(cursor.getColumnIndex("speed"));
//                String accele = cursor.getString(cursor.getColumnIndex("acceleration"));
////                String data = speed+"     "+tempe+"   "+vibra+"  "+accele+"   ";
//                ObservationsPointsBean point = new ObservationsPointsBean(speed, tempe, vibra, accele);
//                info.add(point);
//
//            }
//            pointsInfo.setAdapter(new EleDeviceInfoActivity.ObservationsPointsAdapter(this, info));
//        }
//        db.close();
//        if (info.isEmpty()) {
//            for (int i = 0; i < pointSum; i++) {
//                info.add(new ObservationsPointsBean(null, null, "无数据", null));
//            }
//        }
//        pointsInfo.setAdapter(new EleDeviceInfoActivity.ObservationsPointsAdapter(this, info));
//        cursor.close();

        getComMacData();
        setElecRemarkPicture();
        setElecProblemRemarkPicture();
        if (compMacList.size() > 0){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new TrinityAdapter(compMacList));
            //setListViewHeightByItem(listView);
        }

    }

    //计算listview高度，设置为固定高度
    private void setListViewHeightByItem(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View item = listAdapter.getView(i, null, listView);
            //item的布局要求是linearLayout，否则measure(0,0)会报错。
            item.measure(0, 0);
            //计算出所有item高度的总和
            totalHeight += item.getMeasuredHeight();
        }
        //获取ListView的LayoutParams,只需要修改高度就可以。
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //修改ListView高度为item总高度和所有分割线的高度总和。
        //这里的分隔线是指ListView自带的divider
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //将修改过的参数，重新设置给ListView
        listView.setLayoutParams(params);
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
//    public void connBluetButtonClick() {
//        connBluetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String connBluetButtonTitle = connBluetButton.getText().toString();
//                if ("连接".equals(connBluetButtonTitle)) {
//                    Log.i(TAG, "连接");
//                    bleManager.startLeScan();
//                } else if ("停止".equals(connBluetButtonTitle)) {
//                    Log.i(TAG, "停止");
//                    bleManager.stopLeScan();
//                    bleManager.disConnect();
//                } else if ("断开".equals(connBluetButtonTitle)) {
//                    Log.i(TAG, "断开");
//                    bleManager.disConnect();
//                }
//            }
//        });
//    }

    //测量按钮点击事件
//    public void surveyButtonClick(){
//        surveyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bleManager.readCharacteristic();
//                pointInsTime = new Date();
//                saveButton.setEnabled(true);
//            }
//        });
//        //长按弹窗
//        surveyButton.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                bleManager.CharacteristicNotification(true);
//                showPopupWindow();
//                return true;
//            }
//        });
//    }

    //历史数据按钮点击事件
//    public void historyButtonClick() {
//        historyData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                db = helper.getReadableDatabase();
//                Cursor cursor = db.rawQuery("select deviceId from "+DbConstant.DEVICE_TABLE+" where deviceTag=?", new String[]{
//                        deviceTag
//                });
//                if(cursor.moveToFirst()) {
//                    int deviceId1 = cursor.getInt(cursor.getColumnIndex("deviceId"));
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", String.valueOf(deviceId1));
//                    Intent intent = new Intent(EleDeviceInfoActivity.this, HistoryDataChartActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//                cursor.close();
//            }
//        });
//    }

    /**
     * 照片处理
     */
    public List<String> saveBitmap(ArrayList<String> files) throws FileNotFoundException {
        File destdir = new File(CAMERA_PATH+"/temp");
        if (!destdir.exists()){
            destdir.mkdirs();
        }
        OutputStream out;
        List<String> loadfiles  = new ArrayList<>();
        for (int i=0; i<files.size(); i++){
            File file = new File(files.get(i));
            if (file.length() - 1200000 > 0){
                out = new FileOutputStream(CAMERA_PATH+"/temp/"+file.getName());
                Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(files.get(i),480,800);//按比例缩放
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);//压缩比
                loadfiles.add(CAMERA_PATH+"/temp/"+file.getName());
            } else {
                loadfiles.add(files.get(i));
            }
        }
        return loadfiles;
    }

    //判断是否完全填写
    private boolean Completely() {
        boolean complete = true;
        for (int i = 0; i < compMacList.size(); i++){
            String data = compMacData.get(i);
            if (data == null || data.equals("")){
                complete = false;
                break;
            }
        }
        return complete;
    }

    //判断是否完全填写II
    private boolean CompletelyII() {
        boolean complete = true;
        for (int i = 0; i < compMacList.size(); i++){
            String data = compMacData.get(i);
            if (i == compMacList.size()-2) {
                if (data.equals("正常")) {
                    break;
                } else {
                    continue;
                }
            }
            if (data == null || data.equals("")){
                complete = false;
                break;
            }
        }
        return complete;
    }

    //保存按钮点击事件
    private void saveButtonClick() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CompletelyII()) {
                    Toast.makeText(EleDeviceInfoActivity.this, "数据填写不完整，请检查", Toast.LENGTH_SHORT).show();
                } else {

                    boolean last = false;
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.beginTransaction();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Cursor cursor = db.rawQuery("select deviceId from " + DbConstant.DEVICE_TABLE + " where deviceTag=?", new String[]{
                                deviceTag
                        });
                        compMacInsTime = new Date();
                        attachmentIds.clear();
                        prolAttachmentIds.clear();
                        if (cursor.moveToFirst()) {
                            int deviceId = deviceIndex.get(id);
                            Cursor pointCursor = db.rawQuery("select * from " + DbConstant.POINT_TABLE + " where deviceId=" + deviceId, null);
                            //pointSum测点个数
                            if (pointSum == 0) {
                                Cursor comCursor = db.rawQuery("select * from " + DbConstant.COMPMAC_TABLE + " where deviceId=" + deviceId, null);
                                if (comCursor.moveToFirst()) {
                                    //改
                                    for (int i = 0; i < compMacList.size(); i++) {
                                        String name = compMacList.get(i).getName();
                                        String value = compMacData.get(i);
                                        db.execSQL("update " + DbConstant.COMPMAC_TABLE + " set value='" + value + "' where deviceId=" + deviceId + " and name='" + name + "'");
                                    }
                                    Toast.makeText(EleDeviceInfoActivity.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    //增
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
                                    Toast.makeText(EleDeviceInfoActivity.this, "数据保存成功", Toast.LENGTH_SHORT).show();
                                }
                                comCursor.close();
                            } else {//测点个数不为零
                                //测点数据为空，
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
                                    } else if (pointSum > pointData.size()) {
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
                                            } else {
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
                                        //pointInsTime?测点巡检时间 compMacInsTime？整机巡检时间
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
                                    Toast.makeText(EleDeviceInfoActivity.this, "数据保存成功", Toast.LENGTH_SHORT).show();
                                } else {//测点数据已存在
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
                                    Toast.makeText(EleDeviceInfoActivity.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                                }

                            }

                            //电气巡检图片附件及备注
                            //InspEqptID DispaOrderID ElecInspEnclo
                            int dispaOrderId = InsOrderInfo.getInstance().getOrderInfo().getOrderId();
                            String otherString = other.getText().toString();
                            //String pathString = null;

                            //电气照片路径存储到本地数据库
                            for(String p : paths){
                                db.execSQL("insert into "+DbConstant.PICTURE_TABLE+" (path,deviceId) values (?,?)", new Object[]{
                                        p,
                                        deviceId
                                });
                                Cursor piccursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE+" where path=? and deviceId="+deviceId, new String[]{
                                        p
                                });
                                if(piccursor.moveToFirst()){
                                    attachmentIds.add(piccursor.getInt(piccursor.getColumnIndex("_id")));
                                }
                                piccursor.close();
                            }
//                            Cursor piccursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE+" where deviceId="+deviceId, null);
//                            while (piccursor.moveToNext()) {
//                                attachmentIds.add(piccursor.getInt(piccursor.getColumnIndex("_id")));
//                            }
//                            piccursor.close();

                            Cursor encloCursor = db.rawQuery("select * from " + DbConstant.ELECINSPENCLO_TABLE + " where dispaOrderId=" + dispaOrderId + " and inspEqptId=" + deviceId, null);
                            if (encloCursor.moveToFirst()) {
                                //改
                                db.execSQL("update " + DbConstant.ELECINSPENCLO_TABLE + " set elecInspRemark='" + otherString + "' where dispaOrderId=" + dispaOrderId + " and inspEqptId=" + deviceId);
                                Toast.makeText(EleDeviceInfoActivity.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                            } else {
                                //增
                                db.execSQL("insert into " + DbConstant.ELECINSPENCLO_TABLE + " (dispaOrderId, inspEqptId, elecInspRemark) values (?, ?, ?)", new Object[]{
                                        dispaOrderId,
                                        deviceId,
                                        otherString
                                });

                                Toast.makeText(EleDeviceInfoActivity.this, "数据保存成功", Toast.LENGTH_SHORT).show();
                            }
                            encloCursor.close();

                            db.execSQL("update "+DbConstant.ELECINSPENCLO_TABLE + " set elecInspEncloRoute=? where dispaOrderId=" + dispaOrderId + " and inspEqptId=" + deviceId, new Object[]{
                                    attachmentIds.toString()
                            });

                            //设备问题
                            String eqptProblemRemark = problemOther.getText().toString();
                            if (eqptProblemRemark != null && !eqptProblemRemark.equals("") || problemPaths != null && !problemPaths.isEmpty()) {
                                //存储问题信息
                                Cursor deviceFault = db.rawQuery("select * from " + DbConstant.DEVICE_FAULT_TABLE + " where deviceId=" + deviceId, null);
                                if (deviceFault.moveToFirst()) {
                                    //改 //String update_sql = "update "+DbConstant.DEVICE_FAULT_TABLE+" set deviceId=? and faultPhenoId=? and treatMethodId=? and describe=? and discoveryTime=? where _id="+problemId;
                                    db.execSQL("update " + DbConstant.DEVICE_FAULT_TABLE + " set describe=?, discoveryTime=? where deviceId=?", new Object[]{
                                            eqptProblemRemark,
                                            simpleDateFormat.format(new Date()),
                                            deviceId
                                    });
                                    Toast.makeText(EleDeviceInfoActivity.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    //增
                                    String sql = "insert into "+ DbConstant.DEVICE_FAULT_TABLE+"(deviceId, describe, discoveryTime) values (?,?,?);";
                                    db.execSQL(sql, new Object[]{
                                            deviceId,
                                            eqptProblemRemark,
                                            simpleDateFormat.format(new Date()),
                                    });
                                }
                                deviceFault.close();

                                //照片路径存储到本地数据库
                                for(String p:problemPaths){
                                    db.execSQL("insert into "+DbConstant.PICTURE_TABLE+" (path,deviceId) values (?,?)", new Object[]{
                                            p,
                                            deviceId
                                    });
                                    Cursor problemcursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE+" where path=? and deviceId="+deviceId, new String[]{
                                            p
                                    });
                                    if(problemcursor.moveToFirst()){
                                        prolAttachmentIds.add(problemcursor.getInt(problemcursor.getColumnIndex("_id")));
                                    }
                                    problemcursor.close();
                                }
                                db.execSQL("update "+DbConstant.DEVICE_FAULT_TABLE+" set attachmentIds=? where deviceId="+deviceId, new Object[]{
                                        prolAttachmentIds.toString()
                                });
                                Toast.makeText(EleDeviceInfoActivity.this, "数据保存成功", Toast.LENGTH_SHORT).show();
                            }

                            //更新巡检状态
                            db.execSQL("update " + DbConstant.DEVICE_TABLE + " set status=? where deviceId=?", new Object[]{
                                    1,
                                    deviceId
                            });

                            last = searchAndTO(db);
                            db.setTransactionSuccessful();
                            //上传地理位置
                            //判断是否有网络连接
                            boolean chen = NetworkDetector.detect(EleDeviceInfoActivity.this);
                            if (!chen) {

                            } else {
                                uploadLocation(longi, lati, deviceId);
                            }

                            pointCursor.close();
                        } else {
                            Toast.makeText(EleDeviceInfoActivity.this, "未找到设备", Toast.LENGTH_LONG).show();

                        }

                        cursor.close();

                    } finally {
                        db.endTransaction();
                        db.close();

                    }
                    if (!last) initForDevice(nextDeviceBundle);

                }
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
                        db.execSQL("delete from " + DbConstant.ELECINSPENCLO_TABLE + " where inspEqptId=?", new Object[]{
                                deviceId
                        });
                        Toast.makeText(EleDeviceInfoActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(EleDeviceInfoActivity.this, EleDeviceInfoActivity.class);
                intent.putExtras(bundle);
                nextDeviceBundle = bundle;
//                initForDevice(bundle);
//                startActivity(intent);
//                finish();
            } else {
                Toast.makeText(EleDeviceInfoActivity.this, "已经是最后一台设备", Toast.LENGTH_SHORT).show();
                last = true;
            }
        } else {
            Toast.makeText(EleDeviceInfoActivity.this, "数据获取错误", Toast.LENGTH_SHORT).show();
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
                            /**
                             *
                             */
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

                    boolean chen = NetworkDetector.detect(EleDeviceInfoActivity.this);
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

    @Override
    public void onAddButtonClick() {
        switch (MyAdapter.getItemId()) {
            case R.id.ngiv_ele_device_info_choose_picture:
                imageWallId = 1;
                ImageSelector.getInstance().launchSelector(EleDeviceInfoActivity.this,paths);
                break;
            case R.id.ngiv_ele_device_problem_info_choose_picture:
                imageWallId = 2;
                ImageSelector.getInstance().launchSelector(EleDeviceInfoActivity.this,problemPaths);
                break;
        }
        //ImageSelector.getInstance().launchSelector(EleDeviceInfoActivity.this,paths);
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
//    private class ObservationsPointsAdapter extends BaseAdapter {
//        private List<ObservationsPointsBean> dataList;
//        private LayoutInflater layoutInflater;
//
//        public ObservationsPointsAdapter(Context context, List<ObservationsPointsBean> dataList) {
//            this.dataList = dataList;
//            layoutInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//
//        public int getCount() {
//            return dataList.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return dataList.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View convertView, ViewGroup viewGroup) {
//
//            if(convertView == null){
//                convertView = layoutInflater.inflate(R.layout.observation_point, null);
//
//            }
//            TextView infoSpeed  = convertView.findViewById(R.id.tv_point_speed);
//            TextView infoTitle = convertView.findViewById(R.id.tv_point_title);
//            infoTitle.setText(String.valueOf(i + 1));
//            TextView infoTempe = convertView.findViewById(R.id.tv_point_tempe);
//            TextView infoVibra = convertView.findViewById(R.id.tv_point_vibra);
//            TextView infoAccele = convertView.findViewById(R.id.tv_point_accele);
//            infoSpeed.setText(dataList.get(i).getSpeed());
//            infoTempe.setText(dataList.get(i).getTempe());
//            infoVibra.setText(dataList.get(i).getVibra());
//            infoAccele.setText(dataList.get(i).getAccele());
//            return convertView;
//
//
//        }
//    }

    private class TrinityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<CompMacBean> dataList;
        private Map<Integer, Integer> rblist = new HashMap<>();

        public TrinityAdapter(List<CompMacBean> dataList) {
            this.dataList = dataList;
        }

        private class RadioButtonViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            RadioGroup rg;
            RadioButton trb;
            RadioButton frb;

            public RadioButtonViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.radiobutton_data_key);
                rg = itemView.findViewById(R.id.radiobutton_data_rg);
                trb = itemView.findViewById(R.id.radiobutton_data_tvalue);
                frb = itemView.findViewById(R.id.radiobutton_data_fvalue);
            }
        }

        private class TextViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            EditText et;

            public TextViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_data_key);
                et = itemView.findViewById(R.id.tv_data_value);
            }
        }

        private class RemarkViewHolder extends RecyclerView.ViewHolder {
            EditText remark;

            public RemarkViewHolder(View itemView) {
                super(itemView);
                remark = itemView.findViewById(R.id.et_data_value);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_radiobutton, parent, false);
                    return new RadioButtonViewHolder(view);
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
                    return new TextViewHolder(view);
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_remark, parent, false);
                    return new RemarkViewHolder(view);
            }

            return null;
        }

        @Override
        public int getItemViewType(int position) {
            int type;

            if (dataList.get(position).isSelectable()){
                type = 0;
            } else {
                if (position == dataList.size()-1) {
                    type = 2;
                } else {
                    type = 1;
                }
            }
            return type;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (getItemViewType(position)) {
                case 0:
                    final RadioButtonViewHolder radioButtonViewHolder = (RadioButtonViewHolder) holder;
                    radioButtonViewHolder.tv.setText(dataList.get(position).getName());
                    List<String> options = dataList.get(position).getOptions();
                    if (rblist.get(position) != null){
                        radioButtonViewHolder.rg.check(rblist.get(position));
                    }
                    radioButtonViewHolder.trb.setText(options.get(0));
                    radioButtonViewHolder.frb.setText(options.get(1));
                    if (options.get(0).equals(compMacData.get(position))) {
                        radioButtonViewHolder.trb.setChecked(true);
                    } else if (options.get(1).equals(compMacData.get(position))) {
                        radioButtonViewHolder.frb.setChecked(true);
                    }
                    radioButtonViewHolder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            rblist.put(position, checkedId);
                            switch (checkedId) {
                                case R.id.radiobutton_data_tvalue:
                                    compMacData.put(position, (String) radioButtonViewHolder.trb.getText());
                                    break;
                                case R.id.radiobutton_data_fvalue:
                                    compMacData.put(position, (String) radioButtonViewHolder.frb.getText());
                                    break;
                            }
                        }
                    });
                    break;
                case 1:
                    TextViewHolder textViewHolder = (TextViewHolder) holder;
                    textViewHolder.textView.setText(dataList.get(position).getName());
                    textViewHolder.et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            compMacData.put(position, editable.toString());
                        }
                    });
                    textViewHolder.et.setText(compMacData.get(position));
                    break;
                case 2:
                    RemarkViewHolder remarkViewHolder = (RemarkViewHolder) holder;
                    remarkViewHolder.remark.setHint(dataList.get(position).getName());
                    remarkViewHolder.remark.setText(compMacData.get(position));
                    remarkViewHolder.remark.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            compMacData.put(position, editable.toString());
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

    }

    private class ElecDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<CompMacBean> dataList;
        private Map<Integer, Integer> rblist = new HashMap<>();

        public ElecDataAdapter(List<CompMacBean> dataList) {
            this.dataList = dataList;
        }

        private class RadioButtonViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            RadioGroup rg;
            RadioButton trb;
            RadioButton frb;

            public RadioButtonViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.radiobutton_data_key);
                rg = itemView.findViewById(R.id.radiobutton_data_rg);
                trb = itemView.findViewById(R.id.radiobutton_data_tvalue);
                frb = itemView.findViewById(R.id.radiobutton_data_fvalue);
            }
        }

        private class TextViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            EditText et;

            public TextViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_data_key);
                et = itemView.findViewById(R.id.tv_data_value);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_radiobutton, parent, false);
                return new RadioButtonViewHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
                return new TextViewHolder(view);
            }

        }

        @Override
        public int getItemViewType(int position) {
            int type;

            if (dataList.get(position).isSelectable()){
                type = 0;
            } else {
                type = 1;
            }
            return type;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (getItemViewType(position)) {
                case 0:
                    final RadioButtonViewHolder radioButtonViewHolder = (RadioButtonViewHolder) holder;
                    radioButtonViewHolder.tv.setText(dataList.get(position).getName());
                    List<String> options = dataList.get(position).getOptions();
                    if (rblist.get(position) != null){
                        radioButtonViewHolder.rg.check(rblist.get(position));
                    }
                    radioButtonViewHolder.trb.setText(options.get(0));
                    radioButtonViewHolder.frb.setText(options.get(1));
                    if (options.get(0).equals(compMacData.get(position))) {
                        radioButtonViewHolder.trb.setChecked(true);
                    } else if (options.get(1).equals(compMacData.get(position))) {
                        radioButtonViewHolder.frb.setChecked(true);
                    }
                    radioButtonViewHolder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            rblist.put(position, checkedId);
                            switch (checkedId) {
                                case R.id.radiobutton_data_tvalue:
                                    compMacData.put(position, (String) radioButtonViewHolder.trb.getText());
                                    break;
                                case R.id.radiobutton_data_fvalue:
                                    compMacData.put(position, (String) radioButtonViewHolder.frb.getText());
                                    break;
                            }
                        }
                    });
                    break;
                case 1:
                    TextViewHolder textViewHolder = (TextViewHolder) holder;
                    textViewHolder.textView.setText(dataList.get(position).getName());
                    textViewHolder.et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            compMacData.put(position, editable.toString());
                        }
                    });
                    textViewHolder.et.setText(compMacData.get(position));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

    }

    private class DataAdapter extends BaseAdapter {
        private List<CompMacBean> dataList;
        private LayoutInflater layoutInflater;
        private Context context;
        private Map<Integer, Integer> rblist = new HashMap<>();

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
            }else{
                convertView = layoutInflater.inflate(R.layout.item_radiobutton, null);
                TextView tv = convertView.findViewById(R.id.radiobutton_data_key);
                tv.setText(dataList.get(i).getName());
                RadioGroup rg = convertView.findViewById(R.id.radiobutton_data_rg);
                final RadioButton trb = convertView.findViewById(R.id.radiobutton_data_tvalue);
                final RadioButton frb = convertView.findViewById(R.id.radiobutton_data_fvalue);
                List<String> options = dataList.get(i).getOptions();
                if (rblist.get(i) != null){
                    rg.check(rblist.get(i));
                }
                trb.setText(options.get(0));
                frb.setText(options.get(1));
                if (options.get(0).equals(compMacData.get(i))) {
                    trb.setChecked(true);
                } else if (options.get(1).equals(compMacData.get(i))) {
                    frb.setChecked(true);
                }
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        rblist.put(i, checkedId);
                        switch (checkedId) {
                            case R.id.radiobutton_data_tvalue:
                                compMacData.put(i, (String) trb.getText());
                                break;
                            case R.id.radiobutton_data_fvalue:
                                compMacData.put(i, (String) frb.getText());
                                break;
                        }
                    }
                });
//                final Spinner spinner = convertView.findViewById(R.id.spinner_data_value);
//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i1, long l) {
//                        compMacData.put(i, (String) spinner.getSelectedItem());
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner, dataList.get(i).getOptions());
//                spinner.setAdapter(adapter);
//                int position = adapter.getPosition(compMacData.get(i));
//                spinner.setSelection(position);
            }

            return convertView;
        }
    }


    //--------------------------蓝牙方面代码
    private final static String TAG = "DeviceInfoActivicy";

//    @Override
//    public void onReadSuccess(String value) {
////        Log.i(TAG,value);
//        pointData = DataProcessing.convertBleDataToList(value);
//        //判断测点匹配情况
//        if(pointData.size() == pointSum){
//            info.clear();
//            for(ObservationsPoint point:pointData) {
////                ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
////                        point.getSpeed(),point.getTemperature(),point.getVibration(),point.getAcceleration());
//                ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
//                        new DecimalFormat("######0.0").format(point.getSpeed()),
//                        new DecimalFormat("########0.0").format(point.getTemperature()),
//                        new DecimalFormat("######0.000").format(point.getVibration()),
//                        new DecimalFormat("######0.0").format(point.getAcceleration()));
//                info.add(observationsPointsBean);
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    pointsInfo.setAdapter(new EleDeviceInfoActivity.ObservationsPointsAdapter(EleDeviceInfoActivity.this, info));
//                }
//            });
//
//            byte[] writeBytes = new byte[1];
//            writeBytes[0] = 0x01;
//            bleManager.write(writeBytes);
//        }
//        else if(pointData.size() > pointSum){
//            final AlertDialog.Builder dialog = new AlertDialog.Builder(EleDeviceInfoActivity.this);
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    AlertDialog alertDialog = dialog.setTitle("警告")
//                            .setMessage("测点数量不匹配")
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//                                }
//                            })
//                            .setPositiveButton("强制加载", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    info.clear();
//                                    for (int k = 0; k < pointSum; k++) {
////                                        ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
////                                                pointData.get(k).getSpeed()+"     "+pointData.get(k).getTemperature()+"     "+pointData.get(k).getVibration()+"     "+pointData.get(k).getAcceleration()+" ");
//                                        ObservationsPointsBean observationsPointsBean = new ObservationsPointsBean(
//                                                new DecimalFormat("######0.0").format(pointData.get(k).getSpeed()),
//                                                new DecimalFormat("########0.0").format(pointData.get(k).getTemperature()),
//                                                new DecimalFormat("######0.000").format(pointData.get(k).getVibration()),
//                                                new DecimalFormat("######0.0").format(pointData.get(k).getAcceleration()));
//                                        info.add(observationsPointsBean);
//                                    }
////                                    Log.i(TAG,new Gson().toJson(info));
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            pointsInfo.setAdapter(new EleDeviceInfoActivity.ObservationsPointsAdapter(EleDeviceInfoActivity.this, info));
//                                        }
//                                    });
//
//                                    byte[] writeBytes = new byte[1];
//                                    writeBytes[0] = 0x01;
//                                    bleManager.write(writeBytes);
//                                }
//                            }).create();
//                    if(!alertDialog.isShowing())
//                        alertDialog.show();
//                }
//            });
//
//        }
//        else if(pointData.size() < pointSum){
//            final AlertDialog.Builder dialog = new AlertDialog.Builder(EleDeviceInfoActivity.this);
////            byte[] writeBytes = new byte[1];
////            writeBytes[0] = 0x01;
////            bleManager.write(writeBytes);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    AlertDialog alertDialog = dialog.setTitle("警告")
//                            .setMessage("测点数据太少").create();
//                    if(!alertDialog.isShowing())
//                        alertDialog.show();
//                }
//            });
//            System.out.println("dialog = " + dialog);
//        }
//
//    }

//    @Override
//    public void onConnProcessChange(final int newStatus) {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (newStatus){
//                    case BleManager.STATE_CONNECTED:
//                        surveyButton.setEnabled(true);
//                        connBluetButton.setEnabled(true);
//                        connBluetButton.setText("断开");
//                        bluetConnStatus.setText("已连接");
//                        break;
//                    case BleManager.STATE_FIND_DEVICE:
//                        surveyButton.setEnabled(false);
//                        connBluetButton.setEnabled(true);
//                        connBluetButton.setText("停止");
//                        bluetConnStatus.setText("寻找设备...");
//                        break;
//                    case BleManager.STATE_DISCOVER_SERVER:
//                        surveyButton.setEnabled(false);
//                        connBluetButton.setEnabled(true);
//                        connBluetButton.setText("停止");
//                        bluetConnStatus.setText("发现服务...");
//                        break;
//                    case BleManager.STATE_DISCONNECTED:
//                        surveyButton.setEnabled(false);
//                        connBluetButton.setEnabled(true);
//                        connBluetButton.setText("连接");
//                        bluetConnStatus.setText("未连接");
//                        break;
//                }
//            }
//        });
//    }

//    @Override
//    public void onNotification(short[] sampleIntData) {
//        //mData就是傅里叶变换之后的数据。
//
//        mData =  FFT.getFrequency(sampleIntData);
//        Log.d("FFT","mData[]-------->"+mData[2].toString()+"/n"+mData[3].toString()+
//                "/n"+mData[4].toString()+"/n"+mData[5].toString()+"/n"+mData[6].toString()
//                +"/n"+mData[7].toString());
//        mHandler.sendEmptyMessageDelayed(EleDeviceInfoActivity.handler_key.UPDATE_UI.ordinal(),500);
//    }

//    @Override
//    public void onReadLength(byte[] sampleAndLength) {
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  request success
                }
                break;
        }
    }
    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(EleDeviceInfoActivity.this).inflate(R.layout.activity_fft, null);
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
        View rootview = LayoutInflater.from(EleDeviceInfoActivity.this).inflate(R.layout.activity_device_info, null);
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
            EleDeviceInfoActivity.handler_key key = EleDeviceInfoActivity.handler_key.values()[msg.what];
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
