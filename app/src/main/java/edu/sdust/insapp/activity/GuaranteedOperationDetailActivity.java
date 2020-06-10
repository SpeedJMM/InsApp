package edu.sdust.insapp.activity;

import android.content.Intent;
import android.os.Build;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.NightKeepWorkAndStaffBean;
import edu.sdust.insapp.bean.ViKeepWorkKeepWatchStaffBean;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class GuaranteedOperationDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView staffBtn, overtimeBtn;
    private SwipeRefreshLayout srl;
    private RecyclerView rv;
    private CardView overtimeCard;
    private OrderAdapter orderAdapter;
    private String keepworkworkrecordid;
    private List<NightKeepWorkAndStaffBean> orderList;
    private List<ViKeepWorkKeepWatchStaffBean> viKeepWorkKeepWatchStaffList;
    private static final int REQUEST_CODE = 4;
    private static final int ORDER_REFRESH_CODE = 9;

    private void initControllers() {
        //新建工单
        fab = findViewById(R.id.fab_god);
        //值班人员
        staffBtn = findViewById(R.id.tv_god_staff_btn);
        //加班登记
        overtimeBtn = findViewById(R.id.tv_god_overtime_btn);
        overtimeCard = findViewById(R.id.cv_god_overtime);
        //刷新
        srl = findViewById(R.id.srl_god);
        //工单列表
        rv = findViewById(R.id.rv_god);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guaranteed_operation_detail);
        toolbar = findViewById(R.id.toolbar_god);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }

        keepworkworkrecordid = getIntent().getStringExtra("keepworkworkrecordid");

        initControllers();
        initData();
        initView();
        initEvents();

        getNightWorkOrder();
    }

    private void initEvents() {
        overtimeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OvertimeRecordActivity.class);
                intent.putExtra("keepworkworkrecordid", keepworkworkrecordid);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), KeepWorkOrderNewActivity.class);
                intent.putExtra("keepworkworkrecordid", keepworkworkrecordid);
                intent.putExtra("viKeepWorkKeepWatchStaffList", (Serializable) viKeepWorkKeepWatchStaffList);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        srl.setColorSchemeResources(R.color.colorPrimary);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setAdapter(orderAdapter);
    }

    private void initData() {
        orderList = new ArrayList<>();
        viKeepWorkKeepWatchStaffList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case ORDER_REFRESH_CODE:
                    getNightWorkOrder();
                    break;
            }
        }
    }

    private void getKeepWorkStaff() {
        String url = UrlConstant.AllKeepWorkStaffAppURL + "?keepworkworkrecordid=" + keepworkworkrecordid;
        VolleyRequest.RequestGet(url, "listAllNightKeepWorkApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<ViKeepWorkKeepWatchStaffBean> viKeepWorkKeepWatchStaffBeans = new Gson().fromJson(result, new TypeToken<List<ViKeepWorkKeepWatchStaffBean>>(){}.getType());
                viKeepWorkKeepWatchStaffList.clear();
                viKeepWorkKeepWatchStaffList.addAll(viKeepWorkKeepWatchStaffBeans);
            }

            @Override
            public void onError(VolleyError result) {
                Snackbar.make(fab, "网络请求错误", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    private void getNightWorkOrder() {
        srl.setRefreshing(true);
        String url = UrlConstant.listAllNightKeepWorkAppURL + "?keepworkworkrecordid=" + keepworkworkrecordid;
        VolleyRequest.RequestGet(url, "listAllNightKeepWorkApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<NightKeepWorkAndStaffBean> nightKeepWorkAndStaffBeans = new Gson().fromJson(result, new TypeToken<List<NightKeepWorkAndStaffBean>>(){}.getType());
                orderList.clear();
                orderList.addAll(nightKeepWorkAndStaffBeans);
                orderAdapter.notifyDataSetChanged();
                srl.setRefreshing(false);
                srl.setEnabled(false);
                getKeepWorkStaff();
            }

            @Override
            public void onError(VolleyError result) {
                Snackbar.make(fab, "网络请求错误", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    private class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<NightKeepWorkAndStaffBean> dataList;

        public OrderAdapter(List<NightKeepWorkAndStaffBean> dataList) {
            this.dataList = dataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView workcontent, profession, completion;
            View orderView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                orderView = itemView;
                workcontent = itemView.findViewById(R.id.tv_god_item_workcontent);
                profession = itemView.findViewById(R.id.tv_god_item_profession);
                completion = itemView.findViewById(R.id.tv_god_item_completion);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keepwork_order, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.workcontent.setText(dataList.get(position).getNightkeepworkworkcontent());
            viewHolder.profession.setText(dataList.get(position).getMaintenancemajorclsname());
            if (dataList.get(position).getNightkeepworkworkfinishsituation()) {
                viewHolder.completion.setText("已完成");
            } else {
                viewHolder.completion.setText("未完成");
            }
            viewHolder.orderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), KeepWorkOrderEditActivity.class);
                    intent.putExtra("nightKeepWorkAndStaffBean", dataList.get(position));
                    intent.putExtra("viKeepWorkKeepWatchStaffList", (Serializable) viKeepWorkKeepWatchStaffList);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}
