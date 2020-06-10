package edu.sdust.insapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.AutoDetailsJson;
import edu.sdust.insapp.bean.EqptProblemFaultPositionBean;
import edu.sdust.insapp.bean.EqptRepairTaskCompletSituationBean;
import edu.sdust.insapp.bean.EqptRepairTaskProblemBean;
import edu.sdust.insapp.bean.FaultCauseBean;
import edu.sdust.insapp.bean.FaultTreatmentMeasBean;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class EqptProblemTaskActivity extends AppCompatActivity {
    /**
     * sbwh:设备位号
     * wtms:问题描述
     * gzjl:故障机理
     * gzyy:故障原因
     * gzclcs:故障处理措施
     * gzbw:故障部位
     * sTime:任务开始时间
     * eTime:任务结束时间
     * complete:是否完成
     */
    private TextView title, sbwh, wtms, gzjl, gzbw, sTime, eTime;
    private Spinner gzyy, gzclcs;
    private RecyclerView recyclerView, list_gzbw;
    private EditText remark;
    private Button save, cancel;
    private RadioGroup complete;
    private RadioButton positive, negative;

    private int start_year,start_month,start_day ,end_year, end_month, end_day = 0;
    private int start_hour,start_minute,start_second,end_hour,end_minute,end_second = 0;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat uploadStandard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private EqptRepairTaskProblemBean eqptRepairTaskProblemBean;
    private String ovepargroupid, dispaordersecdispatime, dispaorderid;
    //故障原因
    private List<String> causes;
    private String selectedCause;
    //故障处理措施
    private List<String> measures;
    private String selectedMeasure;
    //故障机理
    private TaskAdapter adapter;
    private List<String> mechanisms = new ArrayList<>();
    //故障部位
    private LocationAdapter locationAdapter;
    private List<String> locations = new ArrayList<>();
    private List<String> locationCodes = new ArrayList<>();
    //是否完成
    private int whetherfinish;
    private static final String faulttreatmentmeascatgcode = "GZCLCSFL01";
    private static final int REQUEST_CODE = 0;
    private static final int SUCCESS_CHOOSE = 2;
    private static final int SUCCESS_CHOOSE_LOCATION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eqpt_problem_task);

        eqptRepairTaskProblemBean = getIntent().getParcelableExtra("eqptRepairTaskProblemBean");
        ovepargroupid = getIntent().getStringExtra("ovepargroupid");
        dispaordersecdispatime = getIntent().getStringExtra("dispaordersecdispatime");
        dispaorderid = getIntent().getStringExtra("dispaorderid");

        initControllers();
        calendar = Calendar.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TaskAdapter(mechanisms);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        list_gzbw.setLayoutManager(layoutManager1);
        locationAdapter = new LocationAdapter(locations);
        list_gzbw.setAdapter(locationAdapter);

        causes = new ArrayList<>();
        measures = new ArrayList<>();
        sTime.setEnabled(false);
        eTime.setEnabled(false);
        initData();
        getCauseData();
        getMeasureData();
        getFaultLocationList();

        sTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        start_year = year;
                        start_month = month + 1;
                        start_day = day;
                        final String MM = String.format("%02d", start_month);
                        final String dd = String.format("%02d", start_day);
                        new TimePickerDialog(EqptProblemTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                start_hour = hour;
                                start_minute = minute;
                                Calendar c = Calendar.getInstance();
                                start_second = c.get(Calendar.SECOND);
                                String hh = String.format("%02d", start_hour);
                                String mm = String.format("%02d", start_minute);
                                String ss = String.format("%02d", start_second);
                                String tt = start_year + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss;
                                sTime.setText(tt);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), true).show();

                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        end_year = year;
                        end_month = month + 1;
                        end_day = day;
                        final String MM = String.format("%02d", end_month);
                        final String dd = String.format("%02d", end_day);
                        new TimePickerDialog(EqptProblemTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                end_hour = hour;
                                end_minute = minute;
                                Calendar c = Calendar.getInstance();
                                end_second = c.get(Calendar.SECOND);
                                String hh = String.format("%02d", end_hour);
                                String mm = String.format("%02d", end_minute);
                                String ss = String.format("%02d", end_second);
                                String tt = end_year + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss;
                                eTime.setText(tt);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), true).show();

                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        gzyy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCause = causes.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gzclcs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMeasure = measures.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gzjl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> mechanismDatas = new ArrayList<>();
                mechanismDatas.addAll(mechanisms);
                Intent intent = new Intent(getApplicationContext(), FaultMechanismActivity.class);
                intent.putStringArrayListExtra("mechanismDatas", mechanismDatas);
                intent.putExtra("ovepargroupid", ovepargroupid);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        gzbw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("eqptaccntid", eqptRepairTaskProblemBean.getEqptaccntid().toString());
                ArrayList<String> locationDatas = new ArrayList<>();
                locationDatas.addAll(locations);
                Intent intent = new Intent(getApplicationContext(), FaultLocationActivity.class);
                intent.putExtras(bundle);
                intent.putStringArrayListExtra("locationDatas", locationDatas);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        complete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_ept_positive:
                        whetherfinish = 0;
                        break;
                    case R.id.rb_ept_negative:
                        whetherfinish = 1;
                        break;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mechanisms.size() == 0) {
                    Toast.makeText(getApplicationContext(), "未选择故障机理", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (locations.size() == 0) {
                    Toast.makeText(getApplicationContext(), "未选择故障部位", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String url = UrlConstant.updateEqptRepairTaskComAppURL;
                EqptRepairTaskCompletSituationBean parameter = new EqptRepairTaskCompletSituationBean();
                parameter.setEqptrepairtaskcompletsituationid(eqptRepairTaskProblemBean.getEqptrepairtaskcompletsituationid());
                parameter.setEqptrepaircompletorderid(eqptRepairTaskProblemBean.getEqptrepaircompletorderid());
                parameter.setEqptrepairtaskid(eqptRepairTaskProblemBean.getEqptrepairtaskid());
                parameter.setFaultcausecode(selectedCause.split(":")[0]);
                parameter.setFaultmechanismcode(mechanisms.get(0).split(":")[0]);
                parameter.setFaulttreatmentmeascode(selectedMeasure.split(":")[0]);
                parameter.setEqptrepairtaskstarttime(sTime.getText().toString());
                final String endTime = uploadStandard.format(new Date());
                parameter.setEqptrepairtaskendtime(endTime);
                parameter.setEqptrepairtaskwhetherfinish(whetherfinish == 0 ? true : false);
                parameter.setEqptrepairtaskcompletsituationremark(remark.getText().toString());
                final Gson gson = new Gson();
                final Map<String, String> params = new HashMap<>();
                params.put("params", gson.toJson(parameter));

                String urlAdd = UrlConstant.addProFaultPositionAppURL;
                List<EqptProblemFaultPositionBean> epfps = new ArrayList<>();
                boolean existed;
                Collections.sort(locationCodes);
                for(String location : locations) {
                    existed = true;
                    String[] t = location.split(": ");
                    int low = 0;
                    int high = locationCodes.size() - 1;
                    while (low < high) {
                        int mid = (low + high) / 2;
                        if (t[0].equals(locationCodes.get(mid))) {
                            existed = false;
                            break;
                        }
                        if (t[0].compareTo(locationCodes.get(mid)) > 0) {
                            low = mid + 1;
                        } else if (t[0].compareTo(locationCodes.get(mid)) < 0) {
                            high = mid - 1;
                        }
                    }
//                    for (String locationCode : locationCodes) {
//                        if (t[0].equals(locationCode)) {
//                            existed = false;
//                            break;
//                        }
//                    }
                    if (existed) {
                        EqptProblemFaultPositionBean parameter1 = new EqptProblemFaultPositionBean();
                        parameter1.setEqptproblemid(eqptRepairTaskProblemBean.getEqptproblemid());
                        parameter1.setEqptfaultpositioncode(t[0]);
                        epfps.add(parameter1);
                    }
                }
                Map<String, String> params1 = new HashMap<>();
                params1.put("params", gson.toJson(epfps));

                final String autoDetails = UrlConstant.autoDetailsAppURL;
                AutoDetailsJson json = new AutoDetailsJson();
                json.setChangeTask(1);
                json.setEqptrepairdispaorderid(eqptRepairTaskProblemBean.getEqptrepairdispaorderid());
                json.setDispaorderid(Integer.valueOf(dispaorderid));
                json.setEqptaccntids(new Integer[]{eqptRepairTaskProblemBean.getEqptaccntid()});
                json.setAttribvalues(new String[]{eqptRepairTaskProblemBean.getAttribvalue()});
                json.setEqptproblemids(new Integer[]{eqptRepairTaskProblemBean.getEqptproblemid()});
                json.setEqptrepairtaskstarttime(sTime.getText().toString());
                json.setEqptrepairtaskendtime(endTime);
                json.setEqptrepairtaskcompletsituationremark(remark.getText().toString());
                final Map<String, String> params2 = new HashMap<>();
                params2.put("params", gson.toJson(json));

                //故障部位只能添加
                VolleyRequest.RequestPost(urlAdd, "addProFaultPositionApp", params1, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                        int resultCode = jsonObject.get("result").getAsInt();
                        if (resultCode == 0) {
                            VolleyRequest.RequestPost(url, "updateEqptRepairTaskComApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                                @Override
                                public void onSuccess(String result) {
                                    JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                                    int resultCode = jsonObject.get("result").getAsInt();
                                    if (resultCode == 0) {
                                        VolleyRequest.RequestPost(autoDetails, "autoDetailsApp", params2, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                                            @Override
                                            public void onSuccess(String result) {
                                                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                                                int resultCode = jsonObject.get("result").getAsInt();
                                                if (resultCode == 0) {
                                                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
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
    }

    //故障处理措施
    private void getMeasureData() {
        String url = UrlConstant.getFaulttreatmentmeasAppURL + "?faulttreatmentmeascatgcode=" + faulttreatmentmeascatgcode;
        VolleyRequest.RequestGet(url, "getFaulttreatmentmeasApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<FaultTreatmentMeasBean> faultTreatmentMeases = new Gson().fromJson(result, new TypeToken<List<FaultTreatmentMeasBean>>(){}.getType());
                for (FaultTreatmentMeasBean f : faultTreatmentMeases) {
                    measures.add(f.toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_child, measures);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gzclcs.setAdapter(adapter);

                String faulttreatmentmeascode = eqptRepairTaskProblemBean.getFaulttreatmentmeascode();
                if (faulttreatmentmeascode != null) {
                    int i = 0;
                    for (FaultTreatmentMeasBean f : faultTreatmentMeases) {
                        if (faulttreatmentmeascode.equals(f.getFaulttreatmentmeascode())) {
                            break;
                        }
                        i++;
                    }
                    gzclcs.setSelection(i, true);
                    selectedMeasure = measures.get(i);
                } else {
                    selectedMeasure = measures.get(0);
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

    //故障原因
    private void getCauseData() {
        String url = UrlConstant.getFaultcauseAppURL;
        VolleyRequest.RequestGet(url, "getFaultcauseApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<FaultCauseBean> faultcauses = new Gson().fromJson(result, new TypeToken<List<FaultCauseBean>>(){}.getType());
                for (FaultCauseBean f : faultcauses) {
                    causes.add(f.toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_child, causes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gzyy.setAdapter(adapter);

                String faultcausecode = eqptRepairTaskProblemBean.getFaultcausecode();
                if (faultcausecode != null) {
                    int i = 0;
                    for (FaultCauseBean f : faultcauses) {
                        if (faultcausecode.equals(f.getFaultcausecode())) {
                            break;
                        }
                        i++;
                    }
                    gzyy.setSelection(i, true);
                    selectedCause = causes.get(i);
                } else {
                    selectedCause = causes.get(0);
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

    //获取设备问题任务对应设备故障部位
    private void getFaultLocationList() {
        String url = UrlConstant.listAllViEqptProblemFaultPositionAppURL + "?eqptproblemid=" + eqptRepairTaskProblemBean.getEqptproblemid();
        VolleyRequest.RequestGet(url, "listAllViEqptProblemFaultPositionApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<ViEqptProblemFaultPosition> viEqptProblemFaultPositions = new Gson().fromJson(result, new TypeToken<List<ViEqptProblemFaultPosition>>(){}.getType());
                if (viEqptProblemFaultPositions.size() != 0) {
                    for (ViEqptProblemFaultPosition v : viEqptProblemFaultPositions) {
                        locations.add(v.eqptfaultpositioncode3+": "+v.eqptfaultpositionname3);
                        locationCodes.add(v.eqptfaultpositioncode3);
                    }
                    locationAdapter.notifyDataSetChanged();
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

    private class ViEqptProblemFaultPosition {
        private Integer eqptproblemid;
        private Integer eqptproblemfaultpositionid;
        private String eqptfaultpositioncode3;
        private String eqptfaultpositionname3;
        private String eqptfaultpositioncode2;
        private String eqptfaultpositionname2;
        private String eqptfaultpositioncode1;
        private String eqptfaultpositionname1;

        public ViEqptProblemFaultPosition() {
        }

        public Integer getEqptproblemid() {
            return eqptproblemid;
        }

        public void setEqptproblemid(Integer eqptproblemid) {
            this.eqptproblemid = eqptproblemid;
        }

        public Integer getEqptproblemfaultpositionid() {
            return eqptproblemfaultpositionid;
        }

        public void setEqptproblemfaultpositionid(Integer eqptproblemfaultpositionid) {
            this.eqptproblemfaultpositionid = eqptproblemfaultpositionid;
        }

        public String getEqptfaultpositioncode3() {
            return eqptfaultpositioncode3;
        }

        public void setEqptfaultpositioncode3(String eqptfaultpositioncode3) {
            this.eqptfaultpositioncode3 = eqptfaultpositioncode3;
        }

        public String getEqptfaultpositionname3() {
            return eqptfaultpositionname3;
        }

        public void setEqptfaultpositionname3(String eqptfaultpositionname3) {
            this.eqptfaultpositionname3 = eqptfaultpositionname3;
        }

        public String getEqptfaultpositioncode2() {
            return eqptfaultpositioncode2;
        }

        public void setEqptfaultpositioncode2(String eqptfaultpositioncode2) {
            this.eqptfaultpositioncode2 = eqptfaultpositioncode2;
        }

        public String getEqptfaultpositionname2() {
            return eqptfaultpositionname2;
        }

        public void setEqptfaultpositionname2(String eqptfaultpositionname2) {
            this.eqptfaultpositionname2 = eqptfaultpositionname2;
        }

        public String getEqptfaultpositioncode1() {
            return eqptfaultpositioncode1;
        }

        public void setEqptfaultpositioncode1(String eqptfaultpositioncode1) {
            this.eqptfaultpositioncode1 = eqptfaultpositioncode1;
        }

        public String getEqptfaultpositionname1() {
            return eqptfaultpositionname1;
        }

        public void setEqptfaultpositionname1(String eqptfaultpositionname1) {
            this.eqptfaultpositionname1 = eqptfaultpositionname1;
        }
    }

    private void initData() {
        sbwh.setText(eqptRepairTaskProblemBean.getAttribvalue());
        wtms.setText(eqptRepairTaskProblemBean.getEqptproblemdesc());
        if (eqptRepairTaskProblemBean.getEqptrepairtaskstarttime() != null) {
            sTime.setText(eqptRepairTaskProblemBean.getEqptrepairtaskstarttime());
        } else {
            sTime.setText(dispaordersecdispatime);
        }
        if (eqptRepairTaskProblemBean.getEqptrepairtaskendtime() != null) {
            eTime.setText(eqptRepairTaskProblemBean.getEqptrepairtaskendtime());
        } else {
            eTime.setText(uploadStandard.format(new Date()));
        }

        if (eqptRepairTaskProblemBean.getEqptrepairtaskwhetherfinish() != null) {
            if (eqptRepairTaskProblemBean.getEqptrepairtaskwhetherfinish()) {
                positive.setChecked(true);
                whetherfinish = 0;
            } else {
                negative.setChecked(true);
                whetherfinish = 1;
            }
        } else {
            positive.setChecked(true);
            whetherfinish = 0;
        }
        String sRemark = eqptRepairTaskProblemBean.getEqptrepairtaskcompletsituationremark();
        if (sRemark != null && !sRemark.equals(""))
        remark.setText(sRemark);

        if (eqptRepairTaskProblemBean.getFaultmechanismcode() != null) {
            mechanisms.add(eqptRepairTaskProblemBean.getFaultmechanismcode()+": "+eqptRepairTaskProblemBean.getFaultmechanismcontent());
            adapter.notifyDataSetChanged();
        }
    }

    private void initControllers() {
        title = findViewById(R.id.tv_ept_title);
        sbwh = findViewById(R.id.tv_ept_sbwh);
        wtms = findViewById(R.id.tv_ept_wtms);
        gzyy = findViewById(R.id.spinner_ept_gzyy);
        gzjl = findViewById(R.id.tv_ept_gzjl);
        recyclerView = findViewById(R.id.list_ept_gzjl);
        gzclcs = findViewById(R.id.spinner_ept_gzclcs);
        gzbw = findViewById(R.id.tv_ept_gzbw);
        list_gzbw = findViewById(R.id.list_ept_gzbw);
        sTime = findViewById(R.id.tv_ept_taskstart);
        eTime = findViewById(R.id.tv_ept_taskend);
        complete = findViewById(R.id.rg_ept_complete);
        positive = findViewById(R.id.rb_ept_positive);
        negative = findViewById(R.id.rb_ept_negative);
        remark = findViewById(R.id.et_ept_taskremark);
        save = findViewById(R.id.btn_ept_submit);
        cancel = findViewById(R.id.btn_ept_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> dataList;

        public LocationAdapter(List<String> dataList) {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(EqptProblemTaskActivity.this)
                            .setMessage("是否删除？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    locations.remove(position);
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

    private class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> dataList;

        public TaskAdapter(List<String> dataList) {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(EqptProblemTaskActivity.this)
                            .setMessage("是否删除？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mechanisms.remove(position);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if(requestCode == REQUEST_CODE) {
            if (resultCode == SUCCESS_CHOOSE) {
                ArrayList<String> mechanismDatas = data.getStringArrayListExtra("mechanism");
                mechanisms.clear();
                mechanisms.addAll(mechanismDatas);
                adapter.notifyDataSetChanged();
            } else if (resultCode == SUCCESS_CHOOSE_LOCATION) {
                ArrayList<String> locationDatas = data.getStringArrayListExtra("location");
                locations.clear();
                locations.addAll(locationDatas);
                //locations.add(data.getStringExtra("location"));
                locationAdapter.notifyDataSetChanged();
            }
        }
    }
}
