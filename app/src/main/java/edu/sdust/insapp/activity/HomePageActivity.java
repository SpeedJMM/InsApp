package edu.sdust.insapp.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.InsOrderBean;
import edu.sdust.insapp.bean.UpdateResult;
import edu.sdust.insapp.utils.AnalysisInsOrder;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.DownLoadBroadcastReceiver;
import edu.sdust.insapp.utils.InsOrderInfo;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.Values;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;
import edu.sdust.insapp.utils.netState;

public class HomePageActivity extends android.app.Fragment {
    @BindView(R.id.gv_home_page)
    GridView gridView;
    @BindView(R.id.rollPagerView_home_page)
    RollPagerView rollPagerView;
    //下载更新广播
    DownLoadBroadcastReceiver downLoadBroadcastReceiver;
    //功能区图片
    private int[] icons = {
            R.drawable.xunjian, R.drawable.wentizhongxin, R.drawable.xuexizhongxin, R.drawable.gongdanguanli, R.drawable.shujuzhongxin,R.drawable.yuangong
    };
    //功能区文字（与icons对应）
    private String[] titles = {
            "巡 检", "问题中心","学习中心","工单管理","保运执行","巡检二次派工"//"员 工"
    };
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private InsOrderBean insOrderInfo;
    private boolean mReceiverTag = false;

    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //注册广播*/
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_home_page_mk2, container, false);
       if(!mReceiverTag) {
           downLoadBroadcastReceiver = new DownLoadBroadcastReceiver();
           mReceiverTag = true;
           IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
           intentFilter.addAction("com.broadcast.test2");
           getActivity().getApplicationContext().registerReceiver(downLoadBroadcastReceiver, intentFilter);
           Intent intent=new Intent();
           intent.setAction("com.broadcast.test2");
           intent.putExtra("name", "动态注册广播");
           getActivity().getApplicationContext().sendBroadcast(intent);
       }
        //发送消息

       ButterKnife.bind(this,view);
       helper = DbManager.getInstance(getActivity());
        initRollPager();
        initMenu();
        checkUpdate();
        return  view;
    }
//九宫格功能区
    private void initMenu() {
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();

        String [] from ={"image","text"};
        int [] to = {R.id.iv_functional_area,R.id.tv_functional_area_title};
        sim_adapter = new SimpleAdapter(getActivity(), data_list, R.layout.activity_home_page_child_mk2, from, to);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(sim_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        insClick(getActivity());
                        break;
                    case 1:
                        insData();
                        break;
                    case 2:
                        break;
                    case 3:
                        Eqptcomorder();
                        break;
                    case 4:
                        Intent intent = new Intent(getActivity(), GuaranteedOperationActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        Intent intent1 = new Intent(getActivity(), InsDispatchActivity.class);
                        startActivity(intent1);
                        break;
                    default:
                        break;

                }
            }
        });
    }
//轮播图
    private void initRollPager() {
        //设置播放时间间隔
        rollPagerView.setPlayDelay(4000);
        //设置透明度
        rollPagerView.setAnimationDurtion(500);
        rollPagerView.setAdapter(new RollPageViewAdapter(getActivity()));
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icons.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icons[i]);
            map.put("text", titles[i]);
            data_list.add(map);
        }

        return data_list;
    }

    //轮播图
    private class RollPageViewAdapter extends StaticPagerAdapter

    {
        private Activity activity;
        private int[] imgs = {
            R.drawable.lun1, R.drawable.lun2, R.drawable.lun3,R.drawable.lun4,R.drawable.lun5
        };
        public RollPageViewAdapter(Activity activity){
            this.activity = activity;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView=new ImageView(container.getContext());
            imageView.setImageResource(imgs[position]);
            //图片按比例缩小
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return imageView;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }
    //巡检数据点击事件
    private void insData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请选择问题类型");
        builder.setPositiveButton("设备问题", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getActivity(),ProblemRegisterActivity.class);
                startActivity(intent);
            }

        });
        builder.setNegativeButton("非设备问题", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(getActivity(),NonProblemRegisterActivity.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    private void  Eqptcomorder(){
        Intent intent = new Intent(getActivity(),queryview.class);
        startActivity(intent);
    }
    //巡检功能点击事件
    private void insClick(Context context){
        if(netState.getAPNType(context)>0) {
            //本地sqlite获取用户名、密码
            String find_sql = "select * from " + DbConstant.USER_TABLE;
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery(find_sql, null);
            String username, password;
            String url = UrlConstant.dispatchOrderURL;
            if (cursor.moveToFirst()) {
                username = cursor.getString(cursor.getColumnIndex("username"));
                password = cursor.getString(cursor.getColumnIndex("password"));
                //db.endTransaction();
                db.close();
                url = url + "?username=" + username + "&password=" + password;
                //下载派工单
                VolleyRequest.RequestGet(url, "downloadInsOrder", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("data", result);
                        //解析派工单
                        AnalysisInsOrder analysisInsOrder = new AnalysisInsOrder(result);
                        InsOrderBean ins = analysisInsOrder.analysisAndstorage();
                        //派工单单例
                        insOrderInfo = InsOrderInfo.getInstance();
                        insOrderInfo.setOrderInfo(ins.getOrderInfo());
                        insOrderInfo.setResult(ins.getResult());
                        insOrderInfo.setHasOrder(ins.isHasOrder());

                        if (!ins.isHasOrder()) {
                            Toast.makeText(getActivity(), "没有派工单", Toast.LENGTH_LONG).show();
                        } else if (ins.getResult() == 0 && getActivity() != null) {
                            String maintenanceMajorClsCode = ins.getOrderInfo().getMaintenanceMajorClsCode();
                            if (maintenanceMajorClsCode.equals("01")){
                                Intent intent = new Intent(getActivity(), InspectionActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (maintenanceMajorClsCode.equals("03")){
                                Intent intent = new Intent(getActivity(), EleInspectionActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }


                        } else if (ins.getResult() == -1) {
                            Toast.makeText(getActivity(), "用户不存在", Toast.LENGTH_LONG).show();
                        } else if (ins.getResult() == -2) {
                            Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                        } else if (ins.getResult() == -3) {
                            Toast.makeText(getActivity(), "系统错误", Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onError(VolleyError result) {
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void jsonOnSuccess(JSONObject result) {

                    }
                });

            }
        }else{
           // db = helper.getReadableDatabase();
            //String sql = "select * from " + DbConstant.USER_TABLE;

        }
    }
    private void checkUpdate() {
        final int versionCode = Values.getVersionCode(getActivity());
        if (versionCode < 1)
            return;

        String updateURL = UrlConstant.updateURL + "?versionCode=" + versionCode;

        StringRequest stringRequest = new StringRequest(updateURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final UpdateResult updateResult = new Gson().fromJson(response, UpdateResult.class);

                if(!updateResult.isNewVersion())
                    return;

                getActivity().runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("更新 " + updateResult.getVersionName());
                                    builder.setMessage(updateResult.getMessage());

                                    builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            try{
                                                //删除设备台账
                                                db = helper.getReadableDatabase();
                                                db.beginTransaction();
                                                try{
                                                    db.execSQL("delete from "+DbConstant.DOWNLOADDEVICE_TABLE);
                                                    db.execSQL("delete from "+DbConstant.EQPTACCNTTREE_TABLE);
                                                    db.setTransactionSuccessful();
                                                }finally {
                                                    db.endTransaction();
                                                    db.close();
                                                }
                                                //创建下载任务,downloadUrl就是下载链接
                                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(UrlConstant.BASE_URL + updateResult.getDownloadURL()));
                                                request.setDescription("更新APP");
                                                request.allowScanningByMediaScanner();// 设置可以被扫描到
                                                request.setVisibleInDownloadsUi(true);// 设置下载可见
                                                request.setNotificationVisibility(
                                                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//下载完成后通知栏任然可见
                                                request.setDestinationInExternalPublicDir("/download/", "app.apk");
                                                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                                long refernece = downloadManager.enqueue(request);
                                                SharedPreferences sPreferences = getActivity().getSharedPreferences(
                                                        "downloadplato", 0);
                                                sPreferences.edit().putLong("plato", refernece).commit();//保存此次下载ID
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.create().show();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }
                );


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MyApplication.getHttpQueue().add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除广播
        if(mReceiverTag){
            try {
                mReceiverTag= false;
                getActivity().unregisterReceiver(downLoadBroadcastReceiver);
            }catch (IllegalArgumentException e){
                if (e.getMessage().contains("Receiver not registered")) {
                    // Ignore this exception. This is exactly what is desired
                } else {
                    // unexpected, re-throw
                    throw e;
                }
            }
        }

    }
}
