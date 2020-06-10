package edu.sdust.insapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.ViInspDispaOrderForDispaBean;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class InsDispatchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private SwipeRefreshLayout srl;
    private RecyclerView rv;
    private OrderAdapter orderAdapter;
    private List<ViInspDispaOrderForDispaBean> inspRouteList;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private String username;

    private static final int REQUEST_CODE = 6;
    private static final int REFRESH_CODE = 10;

    private void initControllers() {
        fab = findViewById(R.id.fab_id);
        srl = findViewById(R.id.srl_cid);
        rv = findViewById(R.id.rv_cid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_dispatch);
        toolbar = findViewById(R.id.toolbar_id);
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

        getInspRouteList();
    }

    private void initEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        helper = DbManager.getInstance(getApplicationContext());
        inspRouteList = new ArrayList<>();
        orderAdapter = new OrderAdapter(inspRouteList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case REFRESH_CODE:
                    getInspRouteList();
                    break;
            }
        }
    }

    private void getInspRouteList() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select username from "+ DbConstant.USER_TABLE, null);
        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("username"));
            srl.setRefreshing(true);
            String url = UrlConstant.listAllInsDisRouteAppURL + "?username=" + username;
            VolleyRequest.RequestGet(url, "listAllInsDisRouteApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    List<ViInspDispaOrderForDispaBean> viInspDispaOrderForDispaBeans = new Gson().fromJson(result, new TypeToken<List<ViInspDispaOrderForDispaBean>>(){}.getType());
                    inspRouteList.clear();
                    inspRouteList.addAll(viInspDispaOrderForDispaBeans);
                    orderAdapter.notifyDataSetChanged();
                    srl.setRefreshing(false);
                    srl.setEnabled(false);
                    if (inspRouteList.size() == 0) {
                        Snackbar.make(fab, "无更多工单", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
                    }
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
        db.close();
    }

    private class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ViInspDispaOrderForDispaBean> dataList;

        public OrderAdapter(List<ViInspDispaOrderForDispaBean> dataList) {
            this.dataList = dataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView title, username, statename;
            View orderView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                orderView = itemView;
                title = itemView.findViewById(R.id.tv_id_item_title);
                statename = itemView.findViewById(R.id.tv_id_item_content3);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insporder, parent, false);
            return new OrderAdapter.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            OrderAdapter.ItemViewHolder viewHolder = (OrderAdapter.ItemViewHolder) holder;
            viewHolder.title.setText(dataList.get(position).getInsproutename());
            viewHolder.statename.setText(dataList.get(position).getDispaorderstatename());
            viewHolder.orderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), InsSecDispatchActivity.class);
                    intent.putExtra("viInspDispaOrderForDispaBean", dataList.get(position));
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
