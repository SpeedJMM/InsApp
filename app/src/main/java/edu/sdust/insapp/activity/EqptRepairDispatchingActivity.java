package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.DispaOrderBean;
import edu.sdust.insapp.bean.ViOveParGroupStaffBean;
import edu.sdust.insapp.bean.WorkJson;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class EqptRepairDispatchingActivity extends AppCompatActivity {
    private TextView title, dispatchtime, orderstatus, analysis, measure, staff;
    private EditText remark;
    private RecyclerView list_analysis, list_measure, list_staff;
    private Button submit, cancel;
    private List<String> analysisList, measureList, staffList, selectedStaffs;
    private List<Integer> staffIdList, selectedSatffList;
    private AnalysisAdapter analysisAdapter;
    private MeasureAdapter measureAdapter;
    private StaffAdapter staffAdapter;
    private static final int REQUEST_CODE = 0;
    private static final int SUCCESS_CHOOSE_ANALYSIS = 2;
    private static final int SUCCESS_CHOOSE_MEASURE = 3;
    public final static String DISPATCHED = "PGDZT03";
    private SimpleDateFormat uploadStandard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String eqptrepairdispaorderid;
    private String ovepargroupid;
    private String EqptTepairTaskDes;
    private String dispaordercontroldispatime;
    private String dispaorderid;
    private boolean[] selectedArray;
    private static final int REFRESH_CODE = 6;

    private void initControllers() {
        //标题
        title = findViewById(R.id.tv_erd_title);
        //调度派工时间
        dispatchtime = findViewById(R.id.tv_erd_dispatchtime);
        //派工单状态
        orderstatus = findViewById(R.id.tv_erd_orderstatus);
        //派工单备注
        remark = findViewById(R.id.et_erd_remark);
        //选择人员
        staff = findViewById(R.id.tv_erd_staff);
        //人员
        list_staff = findViewById(R.id.list_erd_staff);
        //选择安全危害分析
        analysis = findViewById(R.id.tv_erd_analysis);
        //安全危害分析
        list_analysis = findViewById(R.id.list_erd_analysis);
        //选择安全措施
        measure = findViewById(R.id.tv_erd_measure);
        //安全措施
        list_measure = findViewById(R.id.list_erd_measure);
        //派工
        submit = findViewById(R.id.btn_erd_submit);
        //取消
        cancel = findViewById(R.id.btn_erd_cancel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eqpt_repair_dispatching);

        Bundle bundle = getIntent().getExtras();
        eqptrepairdispaorderid = bundle.getString("eqptrepairdispaorderid");
        ovepargroupid = bundle.getString("ovepargroupid");
        EqptTepairTaskDes = bundle.getString("EqptTepairTaskDes");
        dispaordercontroldispatime = bundle.getString("dispaordercontroldispatime");
        dispaorderid = bundle.getString("dispaorderid");

        initControllers();
        initData();
        initView();
        initEvents();
    }

    private void initEvents() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = UrlConstant.disSecDisOrderAppURL;
                if (selectedStaffs.size() == 0) {
                    Toast.makeText(getApplicationContext(), "未选择人员", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (analysisList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "未选择安全危害分析", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (measureList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "未选择安全措施", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<WorkJson> analysisWorkJsons = new ArrayList<>();
                for (String analysis : analysisList) {
                    WorkJson workJson = new WorkJson();
                    String[] t = analysis.split(": ");
                    workJson.setId(t[0]);
                    analysisWorkJsons.add(workJson);
                }
                List<WorkJson> measureWorkJsons = new ArrayList<>();
                for (String measure : measureList) {
                    WorkJson workJson = new WorkJson();
                    String[] t = measure.split(": ");
                    workJson.setId(t[0]);
                    measureWorkJsons.add(workJson);
                }
                SecDisOrderJson secDisOrderJson = new SecDisOrderJson();
                secDisOrderJson.setDispaorderid(Integer.valueOf(dispaorderid));
                String dispaordersecdispatime = uploadStandard.format(new Date());
                secDisOrderJson.setDispaordersecdispatime(dispaordersecdispatime);
                secDisOrderJson.setDispaorderstatecode(DISPATCHED);
                secDisOrderJson.setDispaorderremark(remark.getText().toString());
                secDisOrderJson.setOveparstaffidList(selectedSatffList);
                secDisOrderJson.setMultiSelectHazardAnalysis(analysisWorkJsons);
                secDisOrderJson.setMultiSelectSafetyMeasures(measureWorkJsons);
                secDisOrderJson.setEqptrepairdispaorderid(Integer.valueOf(eqptrepairdispaorderid));
                secDisOrderJson.setChangeTask(1);
                Gson gson = new Gson();
                final Map<String, String> params = new HashMap<>();
                params.put("params", gson.toJson(secDisOrderJson));
                AlertDialog.Builder builder = new AlertDialog.Builder(EqptRepairDispatchingActivity.this);
                builder.setMessage("确认派工");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VolleyRequest.RequestPost(url, "disSecDisOrderApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                            @Override
                            public void onSuccess(String result) {
                                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                                int resultCode = jsonObject.get("result").getAsInt();
                                if (resultCode == 0) {
                                    Toast.makeText(getApplicationContext(), "派工成功", Toast.LENGTH_SHORT).show();
                                    setResult(REFRESH_CODE);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(VolleyError result) {
                                Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void jsonOnSuccess(JSONObject result) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });

        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] staffArray = staffList.toArray(new String[staffList.size()]);
                if (selectedArray == null || selectedArray.length == 0)
                    return;
                AlertDialog.Builder builder = new AlertDialog.Builder(EqptRepairDispatchingActivity.this)
                        .setTitle("选择人员")
                        .setMultiChoiceItems(staffArray, selectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                selectedArray[i] = b;
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedSatffList.clear();
                                selectedStaffs.clear();
                                for (int t = 0; t < staffIdList.size(); t++) {
                                    if (selectedArray[t]) {
                                        selectedSatffList.add(staffIdList.get(t));
                                        selectedStaffs.add(staffList.get(t));
                                        staffAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> analysisDatas = new ArrayList<>();
                analysisDatas.addAll(analysisList);
                Intent intent = new Intent(getApplicationContext(), SafetyHazardAnalysisActivity.class);
                intent.putStringArrayListExtra("analysisDatas", analysisDatas);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> measureDatas = new ArrayList<>();
                measureDatas.addAll(measureList);
                Intent intent = new Intent(getApplicationContext(), SafetyMeasureActivity.class);
                intent.putStringArrayListExtra("measureDatas", measureDatas);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list_analysis.setLayoutManager(layoutManager);
        list_analysis.setAdapter(analysisAdapter);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        list_measure.setLayoutManager(layoutManager1);
        list_measure.setAdapter(measureAdapter);
        list_staff.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list_staff.setAdapter(staffAdapter);
    }

    private void initData() {
        analysisList = new ArrayList<>();
        measureList = new ArrayList<>();
        staffList = new ArrayList<>();
        staffIdList = new ArrayList<>();
        selectedSatffList = new ArrayList<>();
        selectedStaffs = new ArrayList<>();
        analysisAdapter = new AnalysisAdapter(analysisList);
        measureAdapter = new MeasureAdapter(measureList);
        staffAdapter = new StaffAdapter(selectedStaffs);

        dispatchtime.setText(dispaordercontroldispatime);
        remark.setText(EqptTepairTaskDes);
        getStaffList();
    }

    private void getStaffList() {
        String url = UrlConstant.getAllGroStaffAppURL+"?ovepargroupid="+ovepargroupid;
        VolleyRequest.RequestGet(url, "getAllGroStaffApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<ViOveParGroupStaffBean> viOveParGroupStaffBeans = new Gson().fromJson(result, new TypeToken<List<ViOveParGroupStaffBean>>(){}.getType());
                for (ViOveParGroupStaffBean v : viOveParGroupStaffBeans) {
                    staffList.add(v.getOveparstaffname());
                    staffIdList.add(v.getOveparstaffid());
                }
                selectedArray = new boolean[staffList.size()];
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    //changeTask:1-问题，2-隐患，3-月度
    private class SecDisOrderJson extends DispaOrderBean {
        private List<Integer> oveparstaffidList;
        private List <WorkJson> multiSelectHazardAnalysis;
        private List <WorkJson> multiSelectSafetyMeasures;
        private Integer changeTask;
        private Integer eqptrepairdispaorderid;
        private Integer noneqptrepairdispaorderid;

        public List<Integer> getOveparstaffidList() {
            return oveparstaffidList;
        }

        public void setOveparstaffidList(List<Integer> oveparstaffidList) {
            this.oveparstaffidList = oveparstaffidList;
        }

        public List<WorkJson> getMultiSelectHazardAnalysis() {
            return multiSelectHazardAnalysis;
        }

        public void setMultiSelectHazardAnalysis(List<WorkJson> multiSelectHazardAnalysis) {
            this.multiSelectHazardAnalysis = multiSelectHazardAnalysis;
        }

        public List<WorkJson> getMultiSelectSafetyMeasures() {
            return multiSelectSafetyMeasures;
        }

        public void setMultiSelectSafetyMeasures(List<WorkJson> multiSelectSafetyMeasures) {
            this.multiSelectSafetyMeasures = multiSelectSafetyMeasures;
        }

        public Integer getChangeTask() {
            return changeTask;
        }

        public void setChangeTask(Integer changeTask) {
            this.changeTask = changeTask;
        }

        public Integer getEqptrepairdispaorderid() {
            return eqptrepairdispaorderid;
        }

        public void setEqptrepairdispaorderid(Integer eqptrepairdispaorderid) {
            this.eqptrepairdispaorderid = eqptrepairdispaorderid;
        }

        public Integer getNoneqptrepairdispaorderid() {
            return noneqptrepairdispaorderid;
        }

        public void setNoneqptrepairdispaorderid(Integer noneqptrepairdispaorderid) {
            this.noneqptrepairdispaorderid = noneqptrepairdispaorderid;
        }
    }

//    private class WorkJson{
//        private String id;
//        private String describe;
//
//        public WorkJson() {
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getDescribe() {
//            return describe;
//        }
//
//        public void setDescribe(String describe) {
//            this.describe = describe;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if(requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case SUCCESS_CHOOSE_ANALYSIS:
                    ArrayList<String> analysisDatas = data.getStringArrayListExtra("analysis");
                    analysisList.clear();
                    analysisList.addAll(analysisDatas);
                    analysisAdapter.notifyDataSetChanged();
                    break;
                case SUCCESS_CHOOSE_MEASURE:
                    ArrayList<String> measureDatas = data.getStringArrayListExtra("measure");
                    measureList.clear();
                    measureList.addAll(measureDatas);
                    measureAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    private class StaffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> dataList;

        public StaffAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            View taskView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                taskView = itemView;
                tv = itemView.findViewById(R.id.tv_item_content);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tv.setText(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private class AnalysisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> dataList;

        public AnalysisAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            View taskView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                taskView = itemView;
                tv = itemView.findViewById(R.id.tv_item_content);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tv.setText(dataList.get(position));
            viewHolder.taskView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EqptRepairDispatchingActivity.this)
                            .setMessage("是否删除？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    analysisList.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private class MeasureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> dataList;

        public MeasureAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            View taskView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                taskView = itemView;
                tv = itemView.findViewById(R.id.tv_item_content);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tv.setText(dataList.get(position));
            viewHolder.taskView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EqptRepairDispatchingActivity.this)
                            .setMessage("是否删除？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    measureList.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

}
