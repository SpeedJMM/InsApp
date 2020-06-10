package edu.sdust.insapp.activity;

import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.EverdeptovertimestaffenregisterBean;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class OvertimeRecordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private EditText dsb, jsb, dq, yb, qz, jjg, dydl, qtbs, remark;
    private String keepworkworkrecordid;
    private EverdeptovertimestaffenregisterBean everdeptovertimestaffenregisterBean;

    private void initControllers() {
        //done
        fab = findViewById(R.id.fab_or);
        //动设备加班人员
        dsb = findViewById(R.id.et_cor_dsb);
        //静设备加班人员
        jsb = findViewById(R.id.et_cor_jsb);
        //电气加班人员
        dq = findViewById(R.id.et_cor_dq);
        //仪表加班人员
        yb = findViewById(R.id.et_cor_yb);
        //起重加班人员
        qz = findViewById(R.id.et_cor_qz);
        //机加工加班人员
        jjg = findViewById(R.id.et_cor_jjg);
        //带压堵漏加班人员
        dydl = findViewById(R.id.et_cor_dydl);
        //前台部室加班人员
        qtbs = findViewById(R.id.et_cor_qdbs);
        //备注
        remark = findViewById(R.id.et_cor_remark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_record);
        toolbar = findViewById(R.id.toolbar_or);
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
    }

    private void initEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EverdeptovertimestaffenregisterBean bean = new EverdeptovertimestaffenregisterBean();
                bean.setKeepworkworkrecordid(Integer.valueOf(keepworkworkrecordid));
                bean.setEverdeptovertimestaffenregisterid(everdeptovertimestaffenregisterBean.getEverdeptovertimestaffenregisterid());
                bean.setMoveqptovertimestaff(dsb.getText().toString());
                bean.setStatequipovertimestaff(jsb.getText().toString());
                bean.setElecovertimestaff(dq.getText().toString());
                bean.setMeterovertimestaff(yb.getText().toString());
                bean.setLiftweiovertimestaff(qz.getText().toString());
                bean.setMachiningovertimestaff(jjg.getText().toString());
                bean.setPlwpovertimestaff(dydl.getText().toString());
                bean.setOtherdeptovertimestaff(qtbs.getText().toString());
                bean.setEverdeptovertimestaffremark(remark.getText().toString());
                Map<String, String> params = new HashMap<>();
                params.put("params", new Gson().toJson(bean));
                String url = UrlConstant.saveOvertimeStaffEnregisterAppURL;
                VolleyRequest.RequestPost(url, "saveOvertimeStaffEnregisterApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                        int resultCode = jsonObject.get("result").getAsInt();
                        if (resultCode == 0) {
                            Snackbar.make(view, "保存成功", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError result) {
                        Snackbar.make(view, "网络请求错误", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void jsonOnSuccess(JSONObject result) {

                    }
                });
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

    }

    private void initData() {
        getOvertimeRecord();
    }

    private void getOvertimeRecord() {
        String url = UrlConstant.getOvertimeStaffEnregisterAppURL + "?keepworkworkrecordid=" + keepworkworkrecordid;
        VolleyRequest.RequestGet(url, "getOvertimeStaffEnregisterApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    everdeptovertimestaffenregisterBean = new Gson().fromJson(result, EverdeptovertimestaffenregisterBean.class);
                    dsb.setText(everdeptovertimestaffenregisterBean.getMoveqptovertimestaff() != null ? everdeptovertimestaffenregisterBean.getMoveqptovertimestaff() : "");
                    jsb.setText(everdeptovertimestaffenregisterBean.getStatequipovertimestaff() != null ? everdeptovertimestaffenregisterBean.getStatequipovertimestaff() : "");
                    dq.setText(everdeptovertimestaffenregisterBean.getElecovertimestaff() != null ? everdeptovertimestaffenregisterBean.getElecovertimestaff() : "");
                    yb.setText(everdeptovertimestaffenregisterBean.getMeterovertimestaff() != null ? everdeptovertimestaffenregisterBean.getMeterovertimestaff() : "");
                    qz.setText(everdeptovertimestaffenregisterBean.getLiftweiovertimestaff() != null ? everdeptovertimestaffenregisterBean.getLiftweiovertimestaff() : "");
                    jjg.setText(everdeptovertimestaffenregisterBean.getMachiningovertimestaff() != null ? everdeptovertimestaffenregisterBean.getMachiningovertimestaff() : "");
                    dydl.setText(everdeptovertimestaffenregisterBean.getPlwpovertimestaff() != null ? everdeptovertimestaffenregisterBean.getPlwpovertimestaff() : "");
                    qtbs.setText(everdeptovertimestaffenregisterBean.getOtherdeptovertimestaff() != null ? everdeptovertimestaffenregisterBean.getOtherdeptovertimestaff() : "");
                    remark.setText(everdeptovertimestaffenregisterBean.getEverdeptovertimestaffremark() != null ? everdeptovertimestaffenregisterBean.getEverdeptovertimestaffremark() : "");
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

}
