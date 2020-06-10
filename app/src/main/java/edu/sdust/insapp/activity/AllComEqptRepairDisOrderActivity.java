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
import edu.sdust.insapp.bean.EqptComDataBean;
import edu.sdust.insapp.bean.EqptComOrderBean;
import edu.sdust.insapp.utils.BaseViewHolder;
import edu.sdust.insapp.utils.CommonAdapter;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.EqptListAdapter;
import edu.sdust.insapp.utils.ListAdapter;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

/**
 * Created by Administrator on 2018/6/1.
 */

public class AllComEqptRepairDisOrderActivity extends Fragment{
    private RecyclerView problemlist;
    private ImageButton ivSearch;
    private TextView startTime;
    private DoubleDateSelectDialog mDoubleTimeSelectDialog;
    private String allowedSmallestTime, allowedBiggestTime;
    private TextView endTime;
    //private AutoCompleteTextView editSearch;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    List<EqptComOrderBean> data_list1;
    //private String deviceId = null;
    private EqptComOrderBean eqptComOrderBean1;
    private RecyclerViewHelper recyclerViewHelper;
    private ListAdapter listAdapter;
    private EqptListAdapter eqptAdapter;
    private int loadCount = 1;
    public final static String WAITING_DISPATCH = "PGDZT01";
    public final static String WAITING_SECONDARY_DISPATCH = "PGDZT02";
    public final static String DISPATCHED = "PGDZT03";
    public final static String FINISHED = "PGDZT04";
    private static final int REQUEST_CODE = 1;
    private static final int REFRESH_CODE = 6;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data_list1 = new ArrayList<EqptComOrderBean>();
        eqptAdapter = new EqptListAdapter(data_list1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_com_eqpt_repair_dis_order, container, false);
        problemlist = (RecyclerView)view.findViewById(R.id.all_com_eqpt_repair_dis_order_list);
        startTime = (TextView) view.findViewById(R.id.start_time);
        endTime = (TextView) view.findViewById(R.id.end_time);
        //editSearch = (AutoCompleteTextView) view.findViewById(R.id.edit_search);
        ivSearch =(ImageButton)view.findViewById(R.id.iv_search);
        loadCount = 1;
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        helper = DbManager.getInstance(getActivity());
        //data_list1 = new ArrayList<EqptComOrderBean>();
        //listAdapter.notifyDataSetChanged();
        listAdapter=new ListAdapter(data_list1);
        //eqptAdapter = new EqptListAdapter(data_list1);
        allowedBiggestTime = getTimeChange(new Date());
        allowedSmallestTime = getMonthAgo(new Date());
        startTime.setText(allowedSmallestTime);
        endTime.setText(allowedBiggestTime);
        recyclerViewHelper = new RecyclerViewHelper(problemlist, eqptAdapter);
        listAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
               Integer ss = data_list1.get(position).getEqptrepairdispaorderid();
               Bundle bundle = new Bundle();
               bundle.putString("eqptrepairdispaorderid", String.valueOf(ss));
               Intent intent = new Intent(getActivity(),EqptRepairProblemDetailActivity.class);
               intent.putExtras(bundle);
               startActivity(intent);
            }
        });
        eqptAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                Integer ss = data_list1.get(position).getEqptrepairdispaorderid();
                Bundle bundle = new Bundle();
                bundle.putString("eqptrepairdispaorderid", String.valueOf(ss));
                Integer dispaorderid = data_list1.get(position).getDispaorderid();
                Integer completorderid = data_list1.get(position).getCompletorderid();
                String stateCode = data_list1.get(position).getDispaorderstatecode() == null ? " " : data_list1.get(position).getDispaorderstatecode();

                Integer ovepargroupid = data_list1.get(position).getOvepargroupid();
                String EqptTepairTaskDes = data_list1.get(position).getEqptTepairTaskDes();
                String dispaordercontroldispatime = data_list1.get(position).getDispaordercontroldispatime();
                String dispaordersecdispatime = data_list1.get(position).getDispaordersecdispatime();

                String whetherworkticket = data_list1.get(position).getWhetherworkticket();
                String completordersubmittime = data_list1.get(position).getCompletordersubmittime();
                if (stateCode.equals(DISPATCHED)) {
                    bundle.putString("whetherworkticket", whetherworkticket);
                    bundle.putString("ovepargroupid", String.valueOf(ovepargroupid));
                    bundle.putString("dispaorderid", String.valueOf(dispaorderid));
                    bundle.putString("dispaorderstatecode", stateCode);
                    bundle.putString("completorderid", String.valueOf(completorderid));
                    bundle.putString("dispaordersecdispatime", dispaordersecdispatime);
                    Intent intent = new Intent(getActivity(), EqptRepairDisOrderActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE);
                } else if (stateCode.equals(FINISHED)) {
                    bundle.putString("dispaordersecdispatime", dispaordersecdispatime);
                    bundle.putString("completordersubmittime", completordersubmittime);
                    Intent intent = new Intent(getActivity(),EqptRepairProblemDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (stateCode.equals(WAITING_SECONDARY_DISPATCH)) {
                    bundle.putString("ovepargroupid", String.valueOf(ovepargroupid));
                    bundle.putString("EqptTepairTaskDes", EqptTepairTaskDes);
                    bundle.putString("dispaordercontroldispatime", dispaordercontroldispatime);
                    bundle.putString("dispaorderid", String.valueOf(dispaorderid));
                    Intent intent = new Intent(getActivity(),EqptRepairDispatchingActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {

                }
            }
        });
        startTime.setClickable(true);
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
                    //  2017/7/13 header view bind
                } else if (ViewType.TYPE_FOOTER == viewType) {
                    //  2017/7/13 footer view bind
                }
            }
        });
        recyclerViewHelper.setFooterChangeListener(new OnFooterChangeListener() {
            @Override
            public void onChange(RecyclerView.ViewHolder holder, int state) {
                if (FooterState.LOADING == state) {
                    //  2017/7/13 加载中
                } else if (FooterState.ERROR == state) {
                    //  2017/7/13 加载失败
                } else if (FooterState.NO_MORE == state) {
                    //  2017/7/13 加载完成
                }
            }
        });
        initData();
//搜索按钮点击事件
        ivSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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

    //第二页
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
                            String Listeqptorder = UrlConstant.listeqptorder;
                            String username, password;
                            if (cursor.moveToFirst()) {
                                username = cursor.getString(cursor.getColumnIndex("username"));
                                password = cursor.getString(cursor.getColumnIndex("password"));
                                Map<String, String> stringMap = new HashMap<>();
                                stringMap.put("username", username);
                                stringMap.put("password", password);
                                stringMap.put("starttime", allowedSmallestTime + " 00:00:00");
                                stringMap.put("size", String.valueOf(loadCount));
                                stringMap.put("endtime", allowedBiggestTime + " 23:59:59");
                                //Listeqptorder = UrlConstant.listeqptorder + "?username=" + username + "&password=" + password+"&starttime="+allowedSmallestTime+" 00:00:00"+"&endtime="+allowedBiggestTime+" 23:59:59"+"&size="+loadCount;
                                VolleyRequest.RequestPost(Listeqptorder, "eqptcomorder", stringMap, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                                    @Override
                                    public void onSuccess(String result) {
                                        EqptComDataBean eqptComOrderBean = new Gson().fromJson(result, EqptComDataBean.class);
                                        List<EqptComOrderBean> eqptcomorder = eqptComOrderBean.getData();
                                        if (eqptcomorder.size() > 0) {
                                            for (EqptComOrderBean eqptcom : eqptcomorder) {
                                                eqptComOrderBean1 =  new EqptComOrderBean();
                                                String[] eqpt = eqptcom.getEqptTepairTask().split("/");
                                                eqptComOrderBean1.setOvepargroupname(eqptcom.getOvepargroupname());
                                                eqptComOrderBean1.setEqptTepairTask(eqpt[0]+eqpt[1]);
                                                eqptComOrderBean1.setEqptrepairdispaorderid(eqptcom.getEqptrepairdispaorderid());

                                                eqptComOrderBean1.setEqptTepairImport(eqptcom.getEqptTepairImport());
                                                eqptComOrderBean1.setEqptTepairTaskDes(eqptcom.getEqptTepairTaskDes());
                                                eqptComOrderBean1.setViDispaStaff(eqptcom.getViDispaStaff());
                                                eqptComOrderBean1.setDispaordercontroldispatime(eqptcom.getDispaordercontroldispatime());
                                                eqptComOrderBean1.setDispaorderstatecode(eqptcom.getDispaorderstatecode());
                                                eqptComOrderBean1.setDispaorderstatename(eqptcom.getDispaorderstatename());
                                                //用于完工
                                                eqptComOrderBean1.setDispaorderid(eqptcom.getDispaorderid());
                                                eqptComOrderBean1.setCompletorderid(eqptcom.getCompletorderid());
                                                eqptComOrderBean1.setDispaordersecdispatime(eqptcom.getDispaordersecdispatime());
                                                //用于二次派工
                                                eqptComOrderBean1.setOvepargroupid(eqptcom.getOvepargroupid());
                                                eqptComOrderBean1.setEqptTepairTaskDes(eqptcom.getEqptTepairTaskDes());
                                                //用于判断作业票
                                                eqptComOrderBean1.setWhetherworkticket(eqptcom.getWhetherworkticket() == null ? "否" : eqptcom.getWhetherworkticket());
                                                //用于完工单详情
                                                eqptComOrderBean1.setCompletordersubmittime(eqptcom.getCompletordersubmittime());

                                                data_list1.add(eqptComOrderBean1);
                                            }
                                            //listAdapter.notifyDataSetChanged();
                                            eqptAdapter.notifyDataSetChanged();
                                            recyclerViewHelper.loadComplete(true);
                                        } else {
                                            //分页数据加载成功，没有更多。即全部加载完成
                                            recyclerViewHelper.loadComplete(false);
                                        }

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
        data_list1.clear();
        recyclerViewHelper.loadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
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
                            String Listeqptorder = UrlConstant.listeqptorder;
                            String username, password;
                            if (cursor.moveToFirst()) {
                                username = cursor.getString(cursor.getColumnIndex("username"));
                                password = cursor.getString(cursor.getColumnIndex("password"));
                                Map<String, String> stringMap = new HashMap<>();
                                stringMap.put("username", username);
                                stringMap.put("password", password);
                                stringMap.put("starttime", allowedSmallestTime + " 00:00:00");
                                stringMap.put("size", String.valueOf(loadCount));
                                stringMap.put("endtime", allowedBiggestTime + " 23:59:59");
                                //Listeqptorder = UrlConstant.listeqptorder + "?username=" + username + "&password=" + password+"&starttime="+allowedSmallestTime+" 00:00:00"+"&endtime="+allowedBiggestTime+" 23:59:59"+"&size="+loadCount;
                                VolleyRequest.RequestPost(Listeqptorder, "eqptcomorder", stringMap, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                                    @Override
                                    public void onSuccess(String result) {
                                        EqptComDataBean eqptComOrderBean = new Gson().fromJson(result, EqptComDataBean.class);
                                        List<EqptComOrderBean>eqptcomorder = eqptComOrderBean.getData();
                                        for (EqptComOrderBean eqptcom : eqptcomorder) {
                                            eqptComOrderBean1 =  new EqptComOrderBean();
                                            String[] eqpt = eqptcom.getEqptTepairTask().split("/");
                                            eqptComOrderBean1.setOvepargroupname(eqptcom.getOvepargroupname());
                                            eqptComOrderBean1.setEqptTepairTask(eqpt[0]+eqpt[1]);
                                            eqptComOrderBean1.setEqptrepairdispaorderid(eqptcom.getEqptrepairdispaorderid());

                                            eqptComOrderBean1.setEqptTepairImport(eqptcom.getEqptTepairImport());
                                            eqptComOrderBean1.setEqptTepairTaskDes(eqptcom.getEqptTepairTaskDes());
                                            eqptComOrderBean1.setViDispaStaff(eqptcom.getViDispaStaff());
                                            eqptComOrderBean1.setDispaordercontroldispatime(eqptcom.getDispaordercontroldispatime());
                                            eqptComOrderBean1.setDispaorderstatecode(eqptcom.getDispaorderstatecode());
                                            eqptComOrderBean1.setDispaorderstatename(eqptcom.getDispaorderstatename());
                                            //用于完工
                                            eqptComOrderBean1.setDispaorderid(eqptcom.getDispaorderid());
                                            eqptComOrderBean1.setCompletorderid(eqptcom.getCompletorderid());
                                            eqptComOrderBean1.setDispaordersecdispatime(eqptcom.getDispaordersecdispatime());
                                            //用于二次派工
                                            eqptComOrderBean1.setOvepargroupid(eqptcom.getOvepargroupid());
                                            eqptComOrderBean1.setEqptTepairTaskDes(eqptcom.getEqptTepairTaskDes());
                                            //用于判断作业票
                                            eqptComOrderBean1.setWhetherworkticket(eqptcom.getWhetherworkticket() == null ? "否" : eqptcom.getWhetherworkticket());
                                            //用于完工单详情
                                            eqptComOrderBean1.setCompletordersubmittime(eqptcom.getCompletordersubmittime());

                                            data_list1.add(eqptComOrderBean1);
                                        }
                                        //listAdapter.notifyDataSetChanged();
                                        eqptAdapter.notifyDataSetChanged();
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
    /*public List<EqptTag> getAllEqpt() {
        String sql = "select deviceId ,deviceName ,eqptCatgName ,deviceAbbre from dldevice";
        SQLiteDatabase db = helper.getWritableDatabase();
        List<EqptTag> pointList = new ArrayList<EqptTag>();
        EqptTag point = null;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            point = new EqptTag();
            point.setEqptaccntid(Integer.parseInt(cursor.getString(cursor
                    .getColumnIndex("deviceId"))));
            point.setEqpttag(cursor.getString(cursor
                    .getColumnIndex("deviceName")));
            point.setEqptcatgname(cursor.getString(cursor
                    .getColumnIndex("eqptCatgName")));
            point.setDeviceabbre(cursor.getString(cursor
                    .getColumnIndex("deviceAbbre")));
            pointList.add(point);
        }
        return pointList;
    }

    public void initACTV() {
        List<String> eqptTags = new LinkedList<>();
        List<EqptTag> jsonarray = this.getAllEqpt();
        for (EqptTag e : jsonarray) {
            eqptTags.add(e.toString());
        }
        if (editSearch != null&&this!= null) {
            editSearch.setEnabled(true);
            editSearch.setHint("请输入设备位号");
            editSearch.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, eqptTags));
        }

       editSearch.setTransformationMethod(new ReplacementTransformationMethod() {
            @Override
            protected char[] getOriginal() {
                char[] original = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
                return original;
            }

            @Override
            protected char[] getReplacement() {
                char[] replacement = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return replacement;
            }
        });

    }*/
}