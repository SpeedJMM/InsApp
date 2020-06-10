package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.fantasy.doubledatepicker.DoubleDateSelectDialog;
import com.google.gson.Gson;
import com.jeanboy.recyclerviewhelper.RecyclerViewHelper;
import com.jeanboy.recyclerviewhelper.adapter.ViewType;
import com.jeanboy.recyclerviewhelper.footer.FooterState;
import com.jeanboy.recyclerviewhelper.listener.LoadMoreListener;
import com.jeanboy.recyclerviewhelper.listener.OnFooterChangeListener;
import com.jeanboy.recyclerviewhelper.listener.OnViewBindListener;
import com.jeanboy.recyclerviewhelper.listener.TipsListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.NonEqptComDataBean;
import edu.sdust.insapp.bean.NonEqptComOrderBean;
import edu.sdust.insapp.utils.BaseViewHolder;
import edu.sdust.insapp.utils.CommonAdapter;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.NonELAdapter;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

import static edu.sdust.insapp.activity.AllComEqptRepairDisOrderActivity.DISPATCHED;
import static edu.sdust.insapp.activity.AllComEqptRepairDisOrderActivity.FINISHED;
import static edu.sdust.insapp.activity.AllComEqptRepairDisOrderActivity.WAITING_SECONDARY_DISPATCH;

/**
 * Created by Administrator on 2018/6/10.
 */

public class AllComNonEqptRepairDisOrderActivity extends Fragment {
    private RecyclerView problemlist;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private RecyclerViewHelper recyclerViewHelper;
    private NonELAdapter listAdapter;
    private int loadCount = 1;
    List<NonEqptComOrderBean> data_list;
    private NonEqptComOrderBean nonEqptComOrderBeen1;
    private TextView startTime, endTime;
    private ImageButton search;
    private String allowedSmallestTime, allowedBiggestTime;
    private DoubleDateSelectDialog mDoubleTimeSelectDialog;
    private static final int REQUEST_CODE = 2;
    private static final int REFRESH_CODE = 7;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data_list = new ArrayList<NonEqptComOrderBean>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_com_non_eqpt_repair_dis_order, container, false);
        problemlist= (RecyclerView)view.findViewById(R.id.all_com_non_eqpt_repair_dis_order_list);
        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        search = view.findViewById(R.id.iv_search);
        loadCount = 1;
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        helper = DbManager.getInstance(getActivity());
        listAdapter  = new NonELAdapter(data_list);
        recyclerViewHelper = new RecyclerViewHelper(problemlist,listAdapter);
        allowedBiggestTime = getTimeChange(new Date());
        allowedSmallestTime = getMonthAgo(new Date());
        startTime.setText(allowedSmallestTime);
        endTime.setText(allowedBiggestTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showCustomTimePicker();

            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomTimePicker();
            }
        });
        listAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                Integer ss = data_list.get(position).getNoneqptrepairdispaorderid();
                Bundle bundle = new Bundle();
                bundle.putString("noneqptrepairdispaorderid", String.valueOf(ss));

                String disp  = data_list.get(position).getViDispaStaff();
                String ditime  = data_list.get(position).getCompletordersubmittime();
                String stateCode = data_list.get(position).getDispaorderstatecode();
                Integer ovepargroupid = data_list.get(position).getOvepargroupid();
                String dispaordercontroldispatime = data_list.get(position).getDispaordercontroldispatime();
                Integer dispaorderid = data_list.get(position).getDispaorderid();
                Integer completorderid = data_list.get(position).getCompletorderid();
                String dispaordersecdispatime = data_list.get(position).getDispaordersecdispatime();

                if (stateCode.equals(WAITING_SECONDARY_DISPATCH)) {
                    bundle.putString("ovepargroupid", String.valueOf(ovepargroupid));
                    bundle.putString("dispaordercontroldispatime", dispaordercontroldispatime);
                    bundle.putString("dispaorderid", String.valueOf(dispaorderid));
                    Intent intent = new Intent(getActivity(),NonEqptRepairDispatchingActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE);
                } else if (stateCode.equals(DISPATCHED)) {
                    bundle.putString("ovepargroupid", String.valueOf(ovepargroupid));
                    bundle.putString("dispaorderid", String.valueOf(dispaorderid));
                    bundle.putString("dispaorderstatecode", stateCode);
                    bundle.putString("completorderid", String.valueOf(completorderid));
                    bundle.putString("dispaordersecdispatime", dispaordersecdispatime);
                    Intent intent = new Intent(getActivity(), NonEqptRepairDisOrderActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE);
                } else if (stateCode.equals(FINISHED)) {
                    bundle.putString("dispaordersecdispatime", dispaordersecdispatime);
                    bundle.putString("disordername",disp);
                    bundle.putString("completordersubmittime",ditime);
                    Intent intent = new Intent(getActivity(),NonEqptRepairTaskProblemDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {

                }
            }
        });
        //设置没有数据的Tips
        recyclerViewHelper.setTipsEmptyView(R.layout.view_data_empty);
        //设置加载中的Tips
        recyclerViewHelper.setTipsLoadingView(R.layout.view_data_loading);
        //设置加载失败的Tips
        recyclerViewHelper.setTipsErrorView(R.layout.view_data_error);

        //默认加载更多 footer 也可自定义
        recyclerViewHelper.useDefaultFooter();

        //加载失败，没有数据时Tips的接口
        recyclerViewHelper.setTipsListener(new TipsListener() {
            @Override
            public void retry() {
                initData();
            }
        });
        //加载更多的接口
        recyclerViewHelper.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadNext();
            }
        });
        recyclerViewHelper.setOnViewBindListener(new OnViewBindListener() {
            @Override
            public void onBind(RecyclerView.ViewHolder holder, int viewType) {
                Log.d(AllComEqptRepairDisOrderActivity.class.getName(), "==============onBind============");
                if (ViewType.TYPE_HEADER == viewType) {
                    // TODO: 2017/7/13 header view bind
                } else if (ViewType.TYPE_FOOTER == viewType) {
                    // TODO: 2017/7/13 footer view bind
                }
            }
        });
        recyclerViewHelper.setFooterChangeListener(new OnFooterChangeListener() {
            @Override
            public void onChange(RecyclerView.ViewHolder holder, int state) {
                Log.d(AllComEqptRepairDisOrderActivity.class.getName(), "==============onChange============");
                if (FooterState.LOADING == state) {
                    // TODO: 2017/7/13 加载中
                } else if (FooterState.ERROR == state) {
                    // TODO: 2017/7/13 加载失败
                } else if (FooterState.NO_MORE == state) {
                    // TODO: 2017/7/13 加载完成
                }
            }
        });

        initData();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String StartTime = startTime.getText().toString().trim();
                String EndTime = endTime.getText().toString().trim();
                if (StartTime == null || "".equals(StartTime)) {
                    Toast.makeText(getActivity(), "开始时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (EndTime == null || "".equals(EndTime)) {
                    Toast.makeText(getActivity(), "结束时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                allowedSmallestTime=StartTime;
                allowedBiggestTime=EndTime;
                loadCount = 1;
                initData();
            }
        });
    }

    /**
     *日期转换
     * @param
     * @return
     */
    public static String getTimeChange(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }
    /**
     *获取一个月前的日期
     * @param
     * @return
     */
    public static String getMonthAgo(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    public void showCustomTimePicker() {
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleDateSelectDialog(getActivity(), allowedSmallestTime, allowedBiggestTime);
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleDateSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime1, String endTime1) {
                    startTime.setText(startTime1);
                    endTime.setText(endTime1);
                }
            });
            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case REFRESH_CODE:
                    loadCount = 1;
                    initData();
                    break;
            }
        }
    }

    private void loadNext() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (false && loadCount % 2 != 0) {
                            //分页数据加载失败
                            recyclerViewHelper.loadError();
                        } else if (loadCount < 50000) {
                            String find_sql = "select * from " + DbConstant.USER_TABLE;
                            db = helper.getReadableDatabase();
                            Cursor cursor = db.rawQuery(find_sql, null);
                            String Listeqptorder = UrlConstant.nonListeqptorder;
                            String username, password;
                            if (cursor.moveToFirst()) {
                                username = cursor.getString(cursor.getColumnIndex("username"));
                                password = cursor.getString(cursor.getColumnIndex("password"));
                                Map<String, String> stringMap = new HashMap<>();
                                stringMap.put("username", username);
                                stringMap.put("password", password);
                                stringMap.put("size", String.valueOf(loadCount));
                                stringMap.put("starttime", allowedSmallestTime + " 00:00:00");
                                stringMap.put("endtime", allowedBiggestTime + " 23:59:59");
                                //Listeqptorder = UrlConstant.nonListeqptorder + "?username=" + username + "&password=" + password+"&size="+loadCount;
                                VolleyRequest.RequestPost(Listeqptorder, "eqptcomorder", stringMap, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                                    @Override
                                    public void onSuccess(String result) {
                                        NonEqptComDataBean nonEqptComDataBean = new Gson().fromJson(result,NonEqptComDataBean.class);
                                        List<NonEqptComOrderBean> nonEqptComOrderBeenlist = nonEqptComDataBean.getData();
                                        for (NonEqptComOrderBean nonEqptComOrderBean:nonEqptComOrderBeenlist){
                                            nonEqptComOrderBeen1 =  new NonEqptComOrderBean();
                                            nonEqptComOrderBeen1.setOvepargroupname(nonEqptComOrderBean.getOvepargroupname());
                                            nonEqptComOrderBeen1.setNonEqptRepairTask(nonEqptComOrderBean.getNonEqptRepairTask());
                                            nonEqptComOrderBeen1.setCompletordersubmittime(nonEqptComOrderBean.getCompletordersubmittime());
                                            nonEqptComOrderBeen1.setViDispaStaff(nonEqptComOrderBean.getViDispaStaff());
                                            nonEqptComOrderBeen1.setNoneqptrepairdispaorderid(nonEqptComOrderBean.getNoneqptrepairdispaorderid());

                                            nonEqptComOrderBeen1.setDispaordercontroldispatime(nonEqptComOrderBean.getDispaordercontroldispatime());
                                            nonEqptComOrderBeen1.setDispaorderstatecode(nonEqptComOrderBean.getDispaorderstatecode());
                                            nonEqptComOrderBeen1.setDispaorderstatename(nonEqptComOrderBean.getDispaorderstatename());
                                            nonEqptComOrderBeen1.setDispaorderid(nonEqptComOrderBean.getDispaorderid());
                                            nonEqptComOrderBeen1.setCompletorderid(nonEqptComOrderBean.getCompletorderid());
                                            nonEqptComOrderBeen1.setOvepargroupid(nonEqptComOrderBean.getOvepargroupid());
                                            nonEqptComOrderBeen1.setDispaordersecdispatime(nonEqptComOrderBean.getDispaordersecdispatime());

                                            data_list.add(nonEqptComOrderBeen1);
                                        }
                                        listAdapter.notifyDataSetChanged();
                                        recyclerViewHelper.loadComplete(true);
                                    }

                                    @Override
                                    public void onError(VolleyError result) {
                                    }

                                    @Override
                                    public void jsonOnSuccess(JSONObject result) {
                                    }
                                });
                            }
                            db.close();
                        } else {
                            //分页数据加载成功，没有更多。即全部加载完成
                            recyclerViewHelper.loadComplete(false);
                        }

                        loadCount++;
                    }
                });
            }
        }).start();

    }

    private void initData() {
        data_list.clear();
        recyclerViewHelper.loadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadCount == 1) {
                            String find_sql = "select * from " + DbConstant.USER_TABLE;
                            db = helper.getReadableDatabase();
                            Cursor cursor = db.rawQuery(find_sql, null);
                            String Listeqptorder = UrlConstant.nonListeqptorder;
                            String username, password;
                            if (cursor.moveToFirst()) {
                                username = cursor.getString(cursor.getColumnIndex("username"));
                                password = cursor.getString(cursor.getColumnIndex("password"));
                                Map<String, String> stringMap = new HashMap<>();
                                stringMap.put("username", username);
                                stringMap.put("password", password);
                                stringMap.put("size", String.valueOf(loadCount));
                                stringMap.put("starttime", allowedSmallestTime + " 00:00:00");
                                stringMap.put("endtime", allowedBiggestTime + " 23:59:59");
                                //Listeqptorder = UrlConstant.nonListeqptorder + "?username=" + username + "&password=" + password+"&size="+loadCount;
                                VolleyRequest.RequestPost(Listeqptorder, "eqptcomorder", stringMap, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                                    @Override
                                    public void onSuccess(String result) {
                                        NonEqptComDataBean nonEqptComDataBean = new Gson().fromJson(result,NonEqptComDataBean.class);
                                        List<NonEqptComOrderBean> nonEqptComOrderBeenlist = nonEqptComDataBean.getData();
                                        for (NonEqptComOrderBean nonEqptComOrderBean:nonEqptComOrderBeenlist){
                                            nonEqptComOrderBeen1 =  new NonEqptComOrderBean();
                                            nonEqptComOrderBeen1.setOvepargroupname(nonEqptComOrderBean.getOvepargroupname());
                                            nonEqptComOrderBeen1.setNonEqptRepairTask(nonEqptComOrderBean.getNonEqptRepairTask());
                                            nonEqptComOrderBeen1.setCompletordersubmittime(nonEqptComOrderBean.getCompletordersubmittime());
                                            nonEqptComOrderBeen1.setViDispaStaff(nonEqptComOrderBean.getViDispaStaff());
                                            nonEqptComOrderBeen1.setNoneqptrepairdispaorderid(nonEqptComOrderBean.getNoneqptrepairdispaorderid());

                                            nonEqptComOrderBeen1.setDispaordercontroldispatime(nonEqptComOrderBean.getDispaordercontroldispatime());
                                            nonEqptComOrderBeen1.setDispaorderstatecode(nonEqptComOrderBean.getDispaorderstatecode());
                                            nonEqptComOrderBeen1.setDispaorderstatename(nonEqptComOrderBean.getDispaorderstatename());
                                            nonEqptComOrderBeen1.setDispaorderid(nonEqptComOrderBean.getDispaorderid());
                                            nonEqptComOrderBeen1.setCompletorderid(nonEqptComOrderBean.getCompletorderid());
                                            nonEqptComOrderBeen1.setOvepargroupid(nonEqptComOrderBean.getOvepargroupid());
                                            nonEqptComOrderBeen1.setDispaordersecdispatime(nonEqptComOrderBean.getDispaordersecdispatime());

                                            data_list.add(nonEqptComOrderBeen1);
                                        }
                                        listAdapter.notifyDataSetChanged();
                                        recyclerViewHelper.loadComplete(true);
                                    }

                                    @Override
                                    public void onError(VolleyError result) {
                                    }

                                    @Override
                                    public void jsonOnSuccess(JSONObject result) {
                                    }
                                });
                            }
                            db.close();
                            //首次加载数据成功
                        } else if (loadCount ==0) {
                            //首次数据记载失败
                            recyclerViewHelper.loadError();
                        }
                        loadCount++;
                    }
                });
            }
        }).start();
    }
}
