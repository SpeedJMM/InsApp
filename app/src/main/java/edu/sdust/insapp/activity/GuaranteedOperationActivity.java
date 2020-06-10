package edu.sdust.insapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.ViKeepWorkWorkRecordBean;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class GuaranteedOperationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout srl;
    private RecyclerView rv;
    private List<ViKeepWorkWorkRecordBean> recordList;
    private OrderAdapter orderAdapter;


    private void initControllers() {
        //
        rv = findViewById(R.id.rv_go);
        //刷新
        srl = findViewById(R.id.srl_go);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guaranteed_operation);
        toolbar = findViewById(R.id.toolbar_go);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }

        initControllers();
        initData();
        initView();
        initEvents();

        getWorkRecord();
    }

    private void initEvents() {
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
        recordList = new ArrayList<>();
        orderAdapter = new OrderAdapter(recordList);
    }

    private void getWorkRecord() {
        srl.setRefreshing(true);
        String url = UrlConstant.listAllViKeepWorkWorkRecordAppURL;
        VolleyRequest.RequestGet(url, "listAllViKeepWorkWorkRecordApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<ViKeepWorkWorkRecordBean> viKeepWorkWorkRecordBeans = new Gson().fromJson(result, new TypeToken<List<ViKeepWorkWorkRecordBean>>(){}.getType());
                recordList.addAll(viKeepWorkWorkRecordBeans);
                orderAdapter.notifyDataSetChanged();
                srl.setRefreshing(false);
                srl.setEnabled(false);
            }

            @Override
            public void onError(VolleyError result) {
                //Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                Snackbar.make(rv, "网络请求错误", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    private class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ViKeepWorkWorkRecordBean> dataList;

        public OrderAdapter(List<ViKeepWorkWorkRecordBean> dataList) {
            this.dataList = dataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView date, week;
            View orderView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                orderView = itemView;
                date = itemView.findViewById(R.id.tv_go_item_date);
                week = itemView.findViewById(R.id.tv_go_item_week);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guaranteed_operation, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.date.setText(dataList.get(position).getKeepworkworkrecorddate().split(" ")[0]);
            viewHolder.week.setText(dataList.get(position).getKeepworkworkrecordweeknum());
            viewHolder.orderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), GuaranteedOperationDetailActivity.class);
                    intent.putExtra("keepworkworkrecordid", dataList.get(position).getKeepworkworkrecordid().toString());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

}
