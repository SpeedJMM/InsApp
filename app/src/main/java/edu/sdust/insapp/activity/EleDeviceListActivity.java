package edu.sdust.insapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.HashMap;
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
import edu.sdust.insapp.utils.DeviceIndexUtil;
import edu.sdust.insapp.utils.InsOrderInfo;
import edu.sdust.insapp.utils.OrderDBHelper;

public class EleDeviceListActivity extends AppCompatActivity {
    @BindView(R.id.vp_device_list)
    ViewPager viewPager;
    @BindView(R.id.tablayout_device_list)
    TabLayout tabLayout;
    private ListView deviceList, problemList;
    private LayoutInflater mInflater;
    private View deviceListView, problemListView;
    private TextView totalText;
    private TextView dTotalText;
    private FloatingActionButton scan;
    private List<View> viewList = new ArrayList<View>();
    private List<String> titleList = new ArrayList<String>();
    private static List<Map<String, String>> devices;
    private InsOrderBean insOrder;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private Map<Integer, Integer> problemIndex;
    private Map<Integer, Integer> deviceIndex;
    private List<Integer> deviceIdList = new ArrayList<>();
    //模拟问题列表
    private String[] problems = {
            "问题一",
            "问题二"
    };
    //模拟问题列表类型
    private String[] types = {
            "设备",
            "非设备"
    };
    //设备类型单选框结果
    String selectText = new String("设备");
    List<Map<String, String>> data_list;
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        verifyStoragePermissionsCAMERA(this);
        ButterKnife.bind(this);
        helper = DbManager.getInstance(this);
        insOrder = InsOrderInfo.getInstance();
        problemIndex = new HashMap<>();
        deviceIndex = DeviceIndexUtil.getInstance();
        ZXingLibrary.initDisplayOpinion(this);
        initView();
        initEvents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        totalText.setText(getTotalText());
        //dTotalText.setText(getDeviceTotalText());
        //设备列表
        getDevices();
        String[] deviceFrom = {
                "tag","status"
        };
        int[] deviceTo = {
                R.id.tv_device_list_item_tag,
                R.id.tv_device_list_item_status
        };
        deviceList.setAdapter(new SimpleAdapter(this, devices, R.layout.device_list_child, deviceFrom, deviceTo));
        //问题列表
        //getData();
        //String[] from = {"problem", "type"};
        //int[] to = {R.id.tv_problem_list_item_key, R.id.tv_problem_list_item_value};
        //problemList.setAdapter(new SimpleAdapter(this, data_list, R.layout.problem_list_child, from, to));
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

    //初始化事件
    private void initEvents() {

        deviceList = deviceListView.findViewById(R.id.lv_device_list_devices);
        //problemList = problemListView.findViewById(R.id.lv_problem_list_problems);
        //dTotalText = problemListView.findViewById(R.id.tv_problem_list_counts);
        //dTotalText.setText(getDeviceTotalText());
        totalText = deviceListView.findViewById(R.id.tv_device_list_total);
        scan = deviceListView.findViewById(R.id.fab_device_list_scan);
        totalText.setText(getTotalText());
        //设备列表
        getDevices();
        String[] deviceFrom = {
                "tag","status"
        };
        int[] deviceTo = {
                R.id.tv_device_list_item_tag,
                R.id.tv_device_list_item_status
        };
        deviceList.setAdapter(new SimpleAdapter(this, devices, R.layout.device_list_child, deviceFrom, deviceTo));

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderInfoBean orderInfoBean = insOrder.getOrderInfo();
                List<DeviceBean> insRoute = orderInfoBean.getInsRoute();
                String deviceTag = devices.get(i).get("tag");
                int pointSum = 0;
                //寻找当前点击设备位号的测点数量并传给下一个avtivity
                for(DeviceBean d:insRoute){
                    if(deviceTag.equals(d.getDeviceTag())){
                        pointSum = d.getPointSum();
                        break;
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString("deviceTag", deviceTag);
                bundle.putInt("pointSum", pointSum);
                bundle.putInt("id", i);


                Intent intent = new Intent(EleDeviceListActivity.this, EleDeviceInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        //问题列表
        //getData();
        //String[] from = {"problem", "type"};
        //int[] to = {R.id.tv_problem_list_item_key, R.id.tv_problem_list_item_value};
        //problemList.setAdapter(new SimpleAdapter(this, data_list, R.layout.problem_list_child, from, to));
//        problemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                db = helper.getReadableDatabase();
////                int count = 0;
//                int id = problemIndex.get(i);
////                Cursor cursor;
////                cursor = db.rawQuery("select count(*) from "+DbConstant.DEVICE_FAULT_TABLE, null);
////                if(cursor.moveToFirst()){
////                    count = cursor.getInt(0);
////                }
//                TextView textView = view.findViewById(R.id.tv_problem_list_item_value);
//                String text = (String) textView.getText();
//                Bundle bundle = new Bundle();
//                Intent intent = new Intent();
//                if("设备".equals(text)){
////                    id = i+1;
//                    intent.setClass(EleDeviceListActivity.this, ProblemDetailActivity.class);
//                }
//                else if("非设备".equals(text)){
////                    id = i+1 - count;
//                    intent.setClass(EleDeviceListActivity.this, NonProblemDetailActivity.class);
//                }
////                db.close();
//                Log.i("test", ""+id);
//                bundle.putInt("id", id);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 0);
//            }
//        });
//        Button register = problemListView.findViewById(R.id.btn_problem_list_register);
//        RadioGroup radioGroup = problemListView.findViewById(R.id.rg_problem_list_choose);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//                RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
//                selectText = radioButton.getText().toString();
//
//            }
//        });
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                if("设备".equals(selectText)){
//                    intent.setClass(EleDeviceListActivity.this, ProblemRegisterActivity.class);
//
//                }
//                else if("非设备".equals(selectText)){
//                    intent.setClass(EleDeviceListActivity.this, NonProblemRegisterActivity.class);
//                }
//                startActivityForResult(intent, 0);
////                startActivityForResult(intent);
//            }
//        });

    }
    //初始化tab页
    private void initView() {
        mInflater = LayoutInflater.from(this);
        deviceListView = mInflater.inflate(R.layout.elec_device_list, null);
        //problemListView = mInflater.inflate(R.layout.problem_list, null);
        viewList.add(deviceListView);
        //viewList.add(problemListView);
        //titleList.add("设备列表");
        //titleList.add("问题列表");
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        //tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));

        EleDeviceListActivity.MyPagerAdapter myPagerAdapter = new EleDeviceListActivity.MyPagerAdapter(viewList);
        viewPager.setAdapter(myPagerAdapter);
        //tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                OrderInfoBean orderInfoBean = insOrder.getOrderInfo();
                List<DeviceBean> insRoute = orderInfoBean.getInsRoute();
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String deviceTag = "";
                    int pointSum = 0;
                    //寻找当前点击设备位号的测点数量并传给下一个avtivity
                    int i = 1;
                    for(DeviceBean d:insRoute){
                        if(result.equals(d.getDeviceId() + "")){
                            pointSum = d.getPointSum();
                            deviceTag = d.getDeviceTag();
                            break;
                        }
                        i++;
                    }

                    if (deviceTag .equals("")) {
                        Toast.makeText(getApplicationContext(), "不在巡检范围内", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Bundle infoBundle = new Bundle();
                    infoBundle.putString("deviceTag", deviceTag);
                    infoBundle.putInt("pointSum", pointSum);
                    infoBundle.putInt("id", i);

                    Intent intent = new Intent(getApplicationContext(), EleDeviceInfoActivity.class);
                    intent.putExtras(infoBundle);
                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getApplicationContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //获取问题列表
    public List<Map<String, String>> getData(){
        data_list = new ArrayList<Map<String, String>>();
        db = helper.getReadableDatabase();
        String device_sql = "select _id, deviceId from "+DbConstant.DEVICE_FAULT_TABLE;
        Cursor cursor = db.rawQuery(device_sql, null);
        int count = 0;

        while(cursor.moveToNext()){
            Map<String, String> map = new HashMap<>();
            String deviceId = cursor.getString(cursor.getColumnIndex("deviceId"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            problemIndex.put(count, id);
            map.put("problem", deviceId);
            map.put("type", "设备");
            data_list.add(map);
            count++;
        }
        String nonDevice_sql = "select _id, faultposition from "+DbConstant.NON_DEVICE_FAULT_TABLE;
        cursor = db.rawQuery(nonDevice_sql, null);
        while(cursor.moveToNext()){
            Map<String, String> map = new HashMap<>();
            String position = cursor.getString(cursor.getColumnIndex("faultposition"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            problemIndex.put(count, id);
            map.put("problem", position);
            map.put("type", "非设备");
            data_list.add(map);
            count++;
        }
        db.close();
        return data_list;
    }

    //获取设备列表
    private List<Map<String, String>> getDevices(){
        devices = new ArrayList<Map<String, String>>();
        db = helper.getReadableDatabase();
        String sel_sql = "select deviceTag, status, deviceId from "+ DbConstant.DEVICE_TABLE;
        Cursor cursor = db.rawQuery(sel_sql, null);
        int i=0;
        while(cursor.moveToNext()){
            Map<String,String> deviceItem = new HashMap<>();
            String tag = cursor.getString(cursor.getColumnIndex("deviceTag"));
            deviceItem.put("tag", tag);

            int status = cursor.getInt(cursor.getColumnIndex("status"));
            switch (status){
                case 0:
                    deviceItem.put("status", "未巡检");
                    break;
                case 1:
                    deviceItem.put("status", "已巡检");
                    break;
                case -1:
                    deviceItem.put("status", "跳过");
                    break;
                default:
                    deviceItem.put("status", "无");
                    break;
            }
            devices.add(deviceItem);
            deviceIndex.put(i, cursor.getInt(cursor.getColumnIndex("deviceId")));
            i++;
        }
        db.close();

        return devices;

    }
    //设备列表下面统计框
    private String getTotalText(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select deviceTag from "+DbConstant.DEVICE_TABLE+" where status=0", null);
        int noFinish = cursor.getCount();
        cursor = db.rawQuery("select deviceTag from "+DbConstant.DEVICE_TABLE+" where status=1", null);
        int finish = cursor.getCount();
        cursor = db.rawQuery("select deviceTag from "+DbConstant.DEVICE_TABLE+" where status=-1", null);
        int strip = cursor.getCount();
        cursor = db.rawQuery("select deviceTag from "+DbConstant.DEVICE_TABLE, null);
        int total = cursor.getCount();
        db.close();

        return "共"+total+"台设备，"+noFinish+"台未测，"+strip+"台跳过，"+finish+"台已测";
    }
    //问题列表统计框
    private String getDeviceTotalText(){
        int dcount = 0;
        int ncount = 0;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from "+DbConstant.DEVICE_FAULT_TABLE, null);
        if(cursor.moveToFirst()){
            dcount = cursor.getInt(0);
        }
        cursor = db.rawQuery("select count(*) from "+DbConstant.NON_DEVICE_FAULT_TABLE, null);
        if(cursor.moveToFirst()){
            ncount = cursor.getInt(0);
        }
        db.close();
        return "共有"+dcount+"个设备问题，"+ncount+"个非设备问题";
    }

    class MyPagerAdapter extends PagerAdapter {

        private List<View> viewList;
        public MyPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }


        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));//删除页卡
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);//页卡标题
        }

    }
}
