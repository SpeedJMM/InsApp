package edu.sdust.insapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.FaultTreatmentMeasBean;
import edu.sdust.insapp.bean.NonAutoDetailsJson;
import edu.sdust.insapp.bean.NonEqptRepairTaskCompletSituationBean;
import edu.sdust.insapp.bean.ViNonEqptRepairTaskMonTaskTotalBean;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class NonEqptMonthlyTaskActivity extends AppCompatActivity {
    /**
     * wtdd:问题地点
     * wtms:问题描述
     * gzclcs:故障处理措施
     * sTime:任务开始时间
     * eTime:任务结束时间
     * complete:是否完成
     */
    private TextView title, wtdd, wtms, sTime, eTime;
    private Spinner gzclcs;
    private EditText remark;
    private Button save, cancel;
    private RadioGroup complete;
    private RadioButton positive, negative;

    private int start_year,start_month,start_day ,end_year, end_month, end_day = 0;
    private int start_hour,start_minute,start_second,end_hour,end_minute,end_second = 0;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat uploadStandard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //private NonEqptRepairTaskProblemBean noneqptRepairTaskProblemBean;
    private String ovepargroupid, dispaordersecdispatime, dispaorderid;
    //故障处理措施
    private List<String> measures;
    private String selectedMeasure;
    //是否完成
    private int whetherfinish;
    private static final String faulttreatmentmeascatgcode = "GZCLCSFL02";
    //
    private ViNonEqptRepairTaskMonTaskTotalBean viNonEqptRepairTaskMonTaskTotalBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_eqpt_problem_task);

        //noneqptRepairTaskProblemBean = getIntent().getParcelableExtra("noneqptRepairTaskProblemBean");
        ovepargroupid = getIntent().getStringExtra("ovepargroupid");
        dispaordersecdispatime = getIntent().getStringExtra("dispaordersecdispatime");
        dispaorderid = getIntent().getStringExtra("dispaorderid");
        viNonEqptRepairTaskMonTaskTotalBean = getIntent().getParcelableExtra("viNonEqptRepairTaskMonTaskTotalBean");

        initControllers();
        calendar = Calendar.getInstance();

        measures = new ArrayList<>();
        sTime.setEnabled(false);
        eTime.setEnabled(false);
        initData();
        getMeasureData();

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
                        new TimePickerDialog(NonEqptMonthlyTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                        new TimePickerDialog(NonEqptMonthlyTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        gzclcs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMeasure = measures.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                final String url = UrlConstant.updatenonEqptRepairTaskComAppURL;
                NonEqptRepairTaskCompletSituationBean parameter = new NonEqptRepairTaskCompletSituationBean();
                parameter.setNoneqptrepairtaskcompletsituationid(viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairtaskcompletsituationid());
                parameter.setNoneqptrepaircompletorderid(viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepaircompletorderid());
                parameter.setNoneqptrepairtaskid(viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairtaskid());
                parameter.setFaulttreatmentmeascode(selectedMeasure.split(":")[0]);
                parameter.setNoneqptrepairtaskstarttime(sTime.getText().toString());
                String endTime = uploadStandard.format(new Date());
                parameter.setNoneqptrepairtaskendtime(endTime);
                parameter.setProblemwhetheralreadysol(whetherfinish == 0 ? true : false);
                parameter.setNoneqptrepairtaskcompletsituationremark(remark.getText().toString());
                Gson gson = new Gson();
                final Map<String, String> params = new HashMap<>();
                params.put("params", gson.toJson(parameter));

                final String nonAutoDetails = UrlConstant.nonAutoDetailsAppURL;
                NonAutoDetailsJson json = new NonAutoDetailsJson();
                json.setChangeTask(3);
                json.setDispaorderid(Integer.valueOf(dispaorderid));
                json.setNoneqptrepairdispaorderid(viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairdispaorderid());
                json.setMonthlynoneqptrepairtaskid_m(viNonEqptRepairTaskMonTaskTotalBean.getMonthlynoneqptrepairtaskid());
                json.setMonthlynoneqptrepairtaskcontent_m(viNonEqptRepairTaskMonTaskTotalBean.getMonthlynoneqptrepairtaskcontent());
                json.setNoneqptrepairtaskstarttime_m(sTime.getText().toString());
                json.setNoneqptrepairtaskendtime_m(endTime);
                json.setNoneqptrepairtaskcompletsituationremark_m(remark.getText().toString());
                final Map<String, String> params1 = new HashMap<>();
                params1.put("params", gson.toJson(json));

                VolleyRequest.RequestPost(url, "updatenonEqptRepairTaskComApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                        int resultCode = jsonObject.get("result").getAsInt();
                        if (resultCode == 0) {
                            VolleyRequest.RequestPost(nonAutoDetails, "nonAutoDetailsApp", params1, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
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

                String faulttreatmentmeascode = viNonEqptRepairTaskMonTaskTotalBean.getFaulttreatmentmeascode();
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

    private void initData () {
        title.setText("非设备月度任务");
        wtdd.setText(viNonEqptRepairTaskMonTaskTotalBean.getMonthlynoneqptrepairtaskplace());
        wtms.setText(viNonEqptRepairTaskMonTaskTotalBean.getMonthlynoneqptrepairtaskabs());
        if (viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairtaskstarttime() != null) {
            sTime.setText(viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairtaskstarttime());
        } else {
            sTime.setText(dispaordersecdispatime);
        }
        if (viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairtaskendtime() != null) {
            eTime.setText(viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairtaskendtime());
        } else {
            eTime.setText(uploadStandard.format(new Date()));
        }

        if (viNonEqptRepairTaskMonTaskTotalBean.getProblemwhetheralreadysol() != null) {
            if (viNonEqptRepairTaskMonTaskTotalBean.getProblemwhetheralreadysol()) {
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
        String sRemark = viNonEqptRepairTaskMonTaskTotalBean.getNoneqptrepairtaskcompletsituationremark();
        if (sRemark != null && !sRemark.equals(""))
        remark.setText(sRemark);

    }

    private void initControllers() {
        title = findViewById(R.id.tv_nept_title);
        wtdd = findViewById(R.id.tv_nept_wtdd);
        wtms = findViewById(R.id.tv_nept_wtms);
        gzclcs = findViewById(R.id.spinner_nept_gzclcs);
        sTime = findViewById(R.id.tv_nept_taskstart);
        eTime = findViewById(R.id.tv_nept_taskend);
        complete = findViewById(R.id.rg_nept_complete);
        positive = findViewById(R.id.rb_nept_positive);
        negative = findViewById(R.id.rb_nept_negative);
        remark = findViewById(R.id.et_nept_taskremark);
        save = findViewById(R.id.btn_nept_submit);
        cancel = findViewById(R.id.btn_nept_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
