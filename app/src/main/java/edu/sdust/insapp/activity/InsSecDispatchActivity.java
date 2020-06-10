package edu.sdust.insapp.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

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
import edu.sdust.insapp.bean.TreeNode;
import edu.sdust.insapp.bean.ViDispaStaffBean;
import edu.sdust.insapp.bean.ViInspDispaOrderForDispaBean;
import edu.sdust.insapp.bean.ViOveParGroupStaffBean;
import edu.sdust.insapp.bean.ViSystemUserDetail;
import edu.sdust.insapp.bean.WorkJson;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class InsSecDispatchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView dispatchtime, orderstatus, route, secdispatchtime, user, staff, analysis, measure;
    private RecyclerView rv_staff, rv_analysis, rv_measure;
    private EditText remark;
    private ViInspDispaOrderForDispaBean viInspDispaOrderForDispaBean;
    private String ovepargroupid, dispaorderid;
    private List<String> userNameList, staffList, selectedStaffList, analysisList, measureList;
    private List<Integer> userIdList, staffIdList, selectedStaffIdList;
    private Integer systemuserid;
    private boolean[] selectedArray;
    private StaffAdapter staffAdapter;
    private List<TreeNode> hazardAnalysisList, safetyMeasureList;
    private AnalysisAdapter analysisAdapter;
    private MeasureAdapter measureAdapter;
    private SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final int REFRESH_CODE = 10;
    private static final int REQUEST_CODE = 7;
    private static final int SUCCESS_CHOOSE_ANALYSIS = 2;
    private static final int SUCCESS_CHOOSE_MEASURE = 3;

    private void initControllers() {
        fab = findViewById(R.id.fab_isd);
        //调度派工时间
        dispatchtime = findViewById(R.id.tv_cisd_dispatchtime);
        //派工单状态
        orderstatus = findViewById(R.id.tv_cisd_orderstatus);
        //巡检路线
        route = findViewById(R.id.tv_cisd_route);
        //二次派工时间
        secdispatchtime = findViewById(R.id.tv_cisd_secdispatchtime);
        //系统用户
        user = findViewById(R.id.tv_cisd_user);
        //人员
        staff = findViewById(R.id.tv_cisd_staff);
        //人员列表
        rv_staff = findViewById(R.id.rv_cisd_staff);
        //安全危害分析
        analysis = findViewById(R.id.tv_cisd_analysis);
        //安全危害分析列表
        rv_analysis = findViewById(R.id.rv_cisd_analysis);
        //安全措施
        measure = findViewById(R.id.tv_cisd_measure);
        //安全措施列表
        rv_measure = findViewById(R.id.rv_cisd_measure);
        //派工单备注
        remark = findViewById(R.id.et_cisd_remark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_sec_dispatch);
        toolbar = findViewById(R.id.toolbar_isd);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }

        viInspDispaOrderForDispaBean = (ViInspDispaOrderForDispaBean) getIntent().getSerializableExtra("viInspDispaOrderForDispaBean");

        initControllers();
        initData();
        initView();
        initEvents();

        getGroStaffAndUser();
    }

    private void initEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (systemuserid == null) {
                    Snackbar.make(view, "请选择系统用户", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (selectedStaffList.size() == 0) {
                    Snackbar.make(view, "请选择人员", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (analysisList.size() == 0) {
                    Snackbar.make(view, "请选择安全危害分析", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (measureList.size() == 0) {
                    Snackbar.make(view, "请选择安全措施", Snackbar.LENGTH_SHORT).show();
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
                SecDispaorderJson json = new SecDispaorderJson();
                json.setDispaorderid(viInspDispaOrderForDispaBean.getDispaorderid());
                json.setDispaorderstatecode("PGDZT03");
                json.setDispaordersecdispatime(sdfTime.format(new Date()));
                json.setDispaorderremark(remark.getText().toString());
                json.setInspdispaorderid(viInspDispaOrderForDispaBean.getInspdispaorderid());
                json.setSystemuserid(systemuserid);
                json.setOveparstaffidList(selectedStaffIdList);
                json.setMultiSelectHazardAnalysis(analysisWorkJsons);
                json.setMultiSelectSafetyMeasures(measureWorkJsons);
                Map<String, String> params = new HashMap<>();
                params.put("params", new Gson().toJson(json));
                String url = UrlConstant.sendSecInsOrderAppURL;
                VolleyRequest.RequestPost(url, "sendSecInsOrderApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                        int resultCode = jsonObject.get("result").getAsInt();
                        if (resultCode == 0) {
                            setResult(REFRESH_CODE);
                            Snackbar.make(view, "派工成功", Snackbar.LENGTH_SHORT).show();
                        } else if (resultCode == -1) {
                            String message = jsonObject.get("message").getAsString();
                            if (message == null) {
                                message = "已经有派工单了";
                            }
                            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
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

        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(measureList);
                Intent intent = new Intent(getApplicationContext(), SafetyMeasureActivity.class);
                intent.putStringArrayListExtra("measureDatas", arrayList);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(analysisList);
                Intent intent = new Intent(getApplicationContext(), SafetyHazardAnalysisActivity.class);
                intent.putStringArrayListExtra("analysisDatas", arrayList);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] staffArray = staffList.toArray(new String[staffList.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
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
                                selectedStaffList.clear();
                                selectedStaffIdList.clear();
                                for (int t = 0; t < staffIdList.size(); t++) {
                                    if (selectedArray[t]) {
                                        selectedStaffList.add(staffList.get(t));
                                        selectedStaffIdList.add(staffIdList.get(t));
                                    }
                                }
                                staffAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] userNameArray = userNameList.toArray(new String[userNameList.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("选择系统用户")
                        .setItems(userNameArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                user.setText(userNameList.get(i));
                                systemuserid = userIdList.get(i);
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
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
        rv_staff.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_staff.setAdapter(staffAdapter);
        rv_analysis.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_analysis.setAdapter(analysisAdapter);
        rv_measure.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_measure.setAdapter(measureAdapter);
    }

    @SuppressLint("RestrictedApi")
    private void initData() {
        userNameList = new ArrayList<>();
        userIdList = new ArrayList<>();
        staffList = new ArrayList<>();
        staffIdList = new ArrayList<>();
        selectedStaffList = new ArrayList<>();
        selectedStaffIdList = new ArrayList<>();
        hazardAnalysisList = new ArrayList<>();
        safetyMeasureList = new ArrayList<>();
        analysisList = new ArrayList<>();
        measureList = new ArrayList<>();
        staffAdapter = new StaffAdapter(selectedStaffList);
        analysisAdapter = new AnalysisAdapter(analysisList);
        measureAdapter = new MeasureAdapter(measureList);

        ovepargroupid = viInspDispaOrderForDispaBean.getOvepargroupid().toString();
        dispaorderid = viInspDispaOrderForDispaBean.getDispaorderid().toString();
        if (viInspDispaOrderForDispaBean.getSystemuserid() != null)
            systemuserid = viInspDispaOrderForDispaBean.getSystemuserid();

        if (viInspDispaOrderForDispaBean.getDispaorderstatecode().equals("PGDZT03")) {
            fab.setVisibility(View.GONE);
        }

        dispatchtime.setText(viInspDispaOrderForDispaBean.getDispaordercontroldispatime().split(" ")[0]);
        orderstatus.setText(viInspDispaOrderForDispaBean.getDispaorderstatename());
        route.setText(viInspDispaOrderForDispaBean.getInsproutename());
        remark.setText(viInspDispaOrderForDispaBean.getDispaorderremark());
    }

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

    private void getSelAnalysisAndMeas() {
        String url = UrlConstant.getSelAnalysisAndMeasURL + "?dispaorderid=" + dispaorderid;
        VolleyRequest.RequestGet(url, "getSecHarmAnalysisApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                AnalysisAndMeas analysisAndMeas = new Gson().fromJson(result, AnalysisAndMeas.class);
                List<String> analysisCodeList = analysisAndMeas.getAnalysisCodeList();
                List<String> measCodeList = analysisAndMeas.getMeasCodeList();
                analysisList.clear();
                for (TreeNode analysisTreeNode : hazardAnalysisList) {
                    List<TreeNode> itemAnalysisList = analysisTreeNode.getChildren();
                    for (TreeNode analysisNode : itemAnalysisList) {
                        for (String analysisCode : analysisCodeList) {
                            if (analysisNode.getId().equals(analysisCode)) {
                                analysisList.add(analysisNode.toString());
                                break;
                            }
                        }
                    }
                }
                measureList.clear();
                for (TreeNode measureTreeNode : safetyMeasureList) {
                    List<TreeNode> itemMeasureList = measureTreeNode.getChildren();
                    for (TreeNode measureNode : itemMeasureList) {
                        for (String measureCode : measCodeList) {
                            if (measureNode.getId().equals(measureCode)) {
                                measureList.add(measureNode.toString());
                                break;
                            }
                        }
                    }
                }
                analysisAdapter.notifyDataSetChanged();
                measureAdapter.notifyDataSetChanged();
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

    private class AnalysisAndMeas {
        private List<String> AnalysisCodeList;
        private List<String> MeasCodeList;

        public List<String> getAnalysisCodeList() {
            return AnalysisCodeList;
        }

        public void setAnalysisCodeList(List<String> analysisCodeList) {
            AnalysisCodeList = analysisCodeList;
        }

        public List<String> getMeasCodeList() {
            return MeasCodeList;
        }

        public void setMeasCodeList(List<String> measCodeList) {
            MeasCodeList = measCodeList;
        }
    }

    private void getSecMeas() {
        String url = UrlConstant.getSecMeasAppURL;
        VolleyRequest.RequestGet(url, "getSecHarmAnalysisApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<TreeNode> treeNodes = new Gson().fromJson(result, new TypeToken<List<TreeNode>>(){}.getType());
                safetyMeasureList.clear();
                safetyMeasureList.addAll(treeNodes);
                getSelAnalysisAndMeas();
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

    private void getSecHarmAnalysis() {
        String url = UrlConstant.getSecHarmAnalysisAppURL;
        VolleyRequest.RequestGet(url, "getSecHarmAnalysisApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<TreeNode> treeNodes = new Gson().fromJson(result, new TypeToken<List<TreeNode>>(){}.getType());
                hazardAnalysisList.clear();
                hazardAnalysisList.addAll(treeNodes);
                getSecMeas();
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

    private void getDisStaff() {
        String url = UrlConstant.getDisStaffURL + "?dispaorderid=" + dispaorderid;
        VolleyRequest.RequestGet(url, "getGroStaffAndUser", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<ViDispaStaffBean> viDispaStaffBeans = new Gson().fromJson(result, new TypeToken<List<ViDispaStaffBean>>(){}.getType());
                for (ViDispaStaffBean viDispaStaffBean : viDispaStaffBeans) {
                    int i = 0;
                    for (Integer staffId : staffIdList) {
                        if (viDispaStaffBean.getOveparstaffid().equals(staffId)) {
                            selectedStaffList.add(viDispaStaffBean.getOveparstaffname());
                            selectedStaffIdList.add(viDispaStaffBean.getOveparstaffid());
                            selectedArray[i] = true;
                        }
                        i++;
                    }
                }
                staffAdapter.notifyDataSetChanged();
                getSecHarmAnalysis();
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

    private void getGroStaffAndUser() {
        String url = UrlConstant.getGroStaffAndUserURL + "?ovepargroupid=" + ovepargroupid;
        VolleyRequest.RequestGet(url, "getGroStaffAndUser", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                GroStaffAndUser groStaffAndUser = new Gson().fromJson(result, GroStaffAndUser.class);
                List<ViOveParGroupStaffBean> viOveParGroupStaffBeans = groStaffAndUser.getViOveParGroupStaffList();
                List<ViSystemUserDetail> viSystemUserDetails = groStaffAndUser.getViSystemUserDetailList();
                for (ViSystemUserDetail viSystemUserDetail : viSystemUserDetails) {
                    userNameList.add(viSystemUserDetail.getOveparstaffname());
                    userIdList.add(viSystemUserDetail.getSystemuserid());
                }
                for (ViOveParGroupStaffBean viOveParGroupStaffBean : viOveParGroupStaffBeans) {
                    staffList.add(viOveParGroupStaffBean.getOveparstaffname());
                    staffIdList.add(viOveParGroupStaffBean.getOveparstaffid());
                }
                selectedArray = new boolean[staffList.size()];
                if (systemuserid != null) {
                    int i = 0;
                    for (Integer userId : userIdList) {
                        if (userId.equals(systemuserid)) {
                            user.setText(userNameList.get(i));
                            user.setEnabled(false);
                            break;
                        }
                        i++;
                    }
                }
                getDisStaff();
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

    private class GroStaffAndUser {
        private List<ViOveParGroupStaffBean> viOveParGroupStaffList;
        private List<ViSystemUserDetail> viSystemUserDetailList;

        public List<ViOveParGroupStaffBean> getViOveParGroupStaffList() {
            return viOveParGroupStaffList;
        }

        public void setViOveParGroupStaffList(List<ViOveParGroupStaffBean> viOveParGroupStaffList) {
            this.viOveParGroupStaffList = viOveParGroupStaffList;
        }

        public List<ViSystemUserDetail> getViSystemUserDetailList() {
            return viSystemUserDetailList;
        }

        public void setViSystemUserDetailList(List<ViSystemUserDetail> viSystemUserDetailList) {
            this.viSystemUserDetailList = viSystemUserDetailList;
        }
    }

    private class SecDispaorderJson {
        private Integer dispaorderid;
        private Integer ovepargroupid;
        private String dispaorderstatecode;
        private String dispaordercontroldispatime;
        private String dispaordersecdispatime;
        private String dispaorderremark;
        private Integer inspdispaorderid;
        private Integer systemuserid;
        private List <Integer> oveparstaffidList;
        private List <WorkJson> multiSelectHazardAnalysis;
        private List <WorkJson> multiSelectSafetyMeasures;

        public Integer getDispaorderid() {
            return dispaorderid;
        }

        public void setDispaorderid(Integer dispaorderid) {
            this.dispaorderid = dispaorderid;
        }

        public Integer getOvepargroupid() {
            return ovepargroupid;
        }

        public void setOvepargroupid(Integer ovepargroupid) {
            this.ovepargroupid = ovepargroupid;
        }

        public String getDispaorderstatecode() {
            return dispaorderstatecode;
        }

        public void setDispaorderstatecode(String dispaorderstatecode) {
            this.dispaorderstatecode = dispaorderstatecode;
        }

        public String getDispaordercontroldispatime() {
            return dispaordercontroldispatime;
        }

        public void setDispaordercontroldispatime(String dispaordercontroldispatime) {
            this.dispaordercontroldispatime = dispaordercontroldispatime;
        }

        public String getDispaordersecdispatime() {
            return dispaordersecdispatime;
        }

        public void setDispaordersecdispatime(String dispaordersecdispatime) {
            this.dispaordersecdispatime = dispaordersecdispatime;
        }

        public String getDispaorderremark() {
            return dispaorderremark;
        }

        public void setDispaorderremark(String dispaorderremark) {
            this.dispaorderremark = dispaorderremark;
        }

        public Integer getInspdispaorderid() {
            return inspdispaorderid;
        }

        public void setInspdispaorderid(Integer inspdispaorderid) {
            this.inspdispaorderid = inspdispaorderid;
        }

        public Integer getSystemuserid() {
            return systemuserid;
        }

        public void setSystemuserid(Integer systemuserid) {
            this.systemuserid = systemuserid;
        }

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_edit, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tv.setText(dataList.get(position));
            viewHolder.taskView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_edit, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tv.setText(dataList.get(position));
            viewHolder.taskView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_edit, parent, false);
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
}
