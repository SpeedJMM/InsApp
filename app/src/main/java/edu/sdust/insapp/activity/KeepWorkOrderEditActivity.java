package edu.sdust.insapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.NightKeepWorkAndStaffBean;
import edu.sdust.insapp.bean.ViKeepWorkKeepWatchStaffBean;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class KeepWorkOrderEditActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RadioGroup type, complete;
    private RadioButton eqpt, noneqpt, positive, negative;
    private Spinner profession;
    private AutoCompleteTextView attribute;
    private EditText workcontent, remark;
    private TextView start, end, staff;
    private RecyclerView rv_staff;
    private NightKeepWorkAndStaffBean nightKeepWorkAndStaffBean;
    private List<ViKeepWorkKeepWatchStaffBean> viKeepWorkKeepWatchStaffList;
    private LinearLayout layout_attribute;
    private int typeMark, whetherFinish = 0;
    private List<String> professionList, attributeList, staffList, selectedStaffList;
    private StaffAdapter staffAdapter;
    private List<Integer> staffIdList, selectedStaffIdList;
    private List<Boolean> selectedList;
    private String selectedProfession;
    private static final int REFRESH_CODE = 9;

    private void initControllers() {
        //done
        fab = findViewById(R.id.fab_kwoe);
        //类型
        type = findViewById(R.id.rg_ckwoe_type);
        //设备
        eqpt = findViewById(R.id.rb_ckwoe_eqpt);
        //非设备
        noneqpt = findViewById(R.id.rb_ckwoe_noneqpt);
        //专业
        profession = findViewById(R.id.spinner_ckwoe_profession);
        //设备位号
        attribute = findViewById(R.id.actv_ckwoe_attribute);
        layout_attribute = findViewById(R.id.layout_ckwoe_attribute);
        //工作内容
        workcontent = findViewById(R.id.et_ckwoe_workcontent);
        //开始时间
        start = findViewById(R.id.tv_ckwoe_start);
        //结束时间
        end = findViewById(R.id.tv_ckwoe_end);
        //完成情况
        complete = findViewById(R.id.rg_ckwoe_complete);
        //已完成
        positive = findViewById(R.id.rb_ckwoe_positive);
        //未完成
        negative = findViewById(R.id.rb_ckwoe_negative);
        //选择工作人员
        staff = findViewById(R.id.tv_ckwoe_staff);
        //工作人员列表
        rv_staff = findViewById(R.id.rv_ckwoe_staff);
        //备注
        remark = findViewById(R.id.et_ckwoe_remark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_work_order_edit);
        toolbar = findViewById(R.id.toolbar_kwoe);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }

        nightKeepWorkAndStaffBean = (NightKeepWorkAndStaffBean) getIntent().getSerializableExtra("nightKeepWorkAndStaffBean");
        viKeepWorkKeepWatchStaffList = (List<ViKeepWorkKeepWatchStaffBean>) getIntent().getSerializableExtra("viKeepWorkKeepWatchStaffList");

        initControllers();
        initData();
        initView();
        initEvents();
    }

    private void initEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String attributeValue = attribute.getText().toString();
                if (typeMark == 0) {
                    if (attributeValue == null || attributeValue.equals("")) {
                        Snackbar.make(view, "设备位号不能为空", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if (isAttributeIncorrect(attributeValue)) {
                        Snackbar.make(view, "设备位号不存在", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }
                NightKeepWorkAndStaffBean bean = new NightKeepWorkAndStaffBean();
                bean.setNightkeepworkworkid(nightKeepWorkAndStaffBean.getNightkeepworkworkid());
                bean.setKeepworkworkrecordid(nightKeepWorkAndStaffBean.getKeepworkworkrecordid());
                String code = "01";
                switch (selectedProfession) {
                    case "动设备":
                        code = "01";
                        break;
                    case "静设备":
                        code = "02";
                        break;
                    case "电气":
                        code = "03";
                        break;
                    case "仪表":
                        code = "04";
                        break;
                }
                bean.setMaintenancemajorclscode(code);
                String type = "设备";
                if (typeMark == 1) {
                    type = "非设备";
                }
                bean.setNightkeepworktype(type);
                bean.setNightkeepworkworkcontent(workcontent.getText().toString());
                bean.setNightkeepworkworkstarttime(start.getText().toString());
                bean.setNightkeepworkworkendtime(end.getText().toString());
                bean.setNightkeepworkworkfinishsituation(whetherFinish == 0 ? true : false);
                bean.setNightkeepworkworkremark(remark.getText().toString());
                bean.setOveparstaffidList(selectedStaffIdList);
                List<Integer> eqptAccntIds = new ArrayList<>();
                if (typeMark == 0) {
                    eqptAccntIds.add(Integer.valueOf(attributeValue.split("/")[0]));
                }
                bean.setEqptAccntIds(eqptAccntIds);
                Map<String, String> params = new HashMap<>();
                params.put("params", new Gson().toJson(bean));
                String url = UrlConstant.updateNightKeepWorkAndStaffAppURL;
                VolleyRequest.RequestPost(url, "updateNightKeepWorkAndStaffApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                        int resultCode = jsonObject.get("result").getAsInt();
                        if (resultCode == 0) {
                            setResult(REFRESH_CODE);
                            Snackbar.make(view, "编辑成功", Snackbar.LENGTH_SHORT).show();
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

        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] staffArray = staffList.toArray(new String[staffList.size()]);
                boolean[] selectedArray = new boolean[selectedList.size()];
                for (int i = 0; i < selectedList.size(); i++) {
                    selectedArray[i] = selectedList.get(i);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("选择工作人员")
                        .setMultiChoiceItems(staffArray, selectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                selectedList.set(i, b);
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedStaffList.clear();
                                selectedStaffIdList.clear();
                                for (int t = 0; t < staffIdList.size(); t++) {
                                    if (selectedList.get(t)) {
                                        selectedStaffList.add(staffList.get(t));
                                        selectedStaffIdList.add(staffIdList.get(t));
                                    }
                                }
                                staffAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", null);
                builder.show();
            }
        });

        complete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_ckwoe_positive:
                        whetherFinish = 0;
                        break;
                    case R.id.rb_ckwoe_negative:
                        whetherFinish = 1;
                        break;
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final int start_year = year;
                        int start_month = month + 1;
                        int start_day = day;
                        final String MM = String.format("%02d", start_month);
                        final String dd = String.format("%02d", start_day);
                        new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                int start_hour = hour;
                                int start_minute = minute;
                                String hh = String.format("%02d", start_hour);
                                String mm = String.format("%02d", start_minute);
                                String tt = start_year + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + "00";
                                start.setText(tt);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), true).show();
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final int end_year = year;
                        int end_month = month + 1;
                        int end_day = day;
                        final String MM = String.format("%02d", end_month);
                        final String dd = String.format("%02d", end_day);
                        new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                int end_hour = hour;
                                int end_minute = minute;
                                String hh = String.format("%02d", end_hour);
                                String mm = String.format("%02d", end_minute);
                                String tt = end_year + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + "00";
                                end.setText(tt);
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), true).show();
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        profession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProfession = professionList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_ckwoe_eqpt:
                        typeMark = 0;
                        break;
                    case R.id.rb_ckwoe_noneqpt:
                        typeMark = 1;
                        break;
                }
                whetherHiddenAttribute();
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
    }

    private void initData() {
        attributeList = new ArrayList<>();
        professionList = new ArrayList<>();
        staffList = new ArrayList<>();
        staffIdList = new ArrayList<>();
        selectedStaffList = new ArrayList<>();
        selectedStaffIdList = new ArrayList<>();
        selectedList = new ArrayList<>();
        staffAdapter = new StaffAdapter(selectedStaffList);

        if (nightKeepWorkAndStaffBean.getNightkeepworktype().equals("设备")) {
            eqpt.setChecked(true);
            typeMark = 0;
        } else {
            noneqpt.setChecked(true);
            typeMark = 1;
        }
        whetherHiddenAttribute();
        professionList.add("动设备");
        professionList.add("静设备");
        professionList.add("电气");
        professionList.add("仪表");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_edit, professionList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profession.setAdapter(spinnerAdapter);
        for (int i = 0; i < professionList.size(); i++) {
            if (professionList.get(i).equals(nightKeepWorkAndStaffBean.getMaintenancemajorclsname())) {
                profession.setSelection(i, true);
                selectedProfession = professionList.get(i);
                break;
            }
        }

        //设备位号
        attributeList.addAll(getEqptAccntTree());
        ArrayAdapter<String> attriibuteAdapter = new ArrayAdapter(getApplicationContext(), R.layout.item_dropdown, attributeList);
        attribute.setAdapter(attriibuteAdapter);
        if (typeMark == 0) {
            for (String s : attributeList) {
                if (nightKeepWorkAndStaffBean.getEqptAccntIds().get(0).toString().equals(s.split("/")[0])) {
                    attribute.setText(s);
                    break;
                }
            }
        }

        workcontent.setText(nightKeepWorkAndStaffBean.getNightkeepworkworkcontent());
        start.setText(nightKeepWorkAndStaffBean.getNightkeepworkworkstarttime());
        end.setText(nightKeepWorkAndStaffBean.getNightkeepworkworkendtime());
        if (nightKeepWorkAndStaffBean.getNightkeepworkworkfinishsituation()) {
            positive.setChecked(true);
            whetherFinish = 0;
        } else {
            negative.setChecked(true);
            whetherFinish = 1;
        }

        //工作人员
        selectedStaffIdList.addAll(nightKeepWorkAndStaffBean.getOveparstaffidList());
        for (ViKeepWorkKeepWatchStaffBean bean : viKeepWorkKeepWatchStaffList) {
            if (bean.getKeepwatchcatgcode().equals("ZBLX01"))
                continue;
            staffIdList.add(bean.getOveparstaffid());
            staffList.add(bean.getOveparstaffnameandmajor());
            selectedList.add(false);
        }
        for (Integer oveparstaffid : selectedStaffIdList) {
            int i = 0;
            for (ViKeepWorkKeepWatchStaffBean bean : viKeepWorkKeepWatchStaffList) {
                if (oveparstaffid.equals(bean.getOveparstaffid())) {
                    selectedStaffList.add(bean.getOveparstaffnameandmajor());
                    selectedList.set(i, true);
                }
                i++;
            }
        }
        staffAdapter.notifyDataSetChanged();

        if (nightKeepWorkAndStaffBean.getNightkeepworkworkremark() != null) {
            remark.setText(nightKeepWorkAndStaffBean.getNightkeepworkworkremark());
        }
    }

    private void whetherHiddenAttribute() {
        switch (typeMark) {
            case 0:
                layout_attribute.setVisibility(View.VISIBLE);
                break;
            case 1:
                layout_attribute.setVisibility(View.GONE);
                break;
        }
    }

    private boolean isAttributeIncorrect(String selected) {
        for (String value : attributeList) {
            if (selected.equals(value)) {
                return false;
            }
        }
        return true;
    }

    //#ViEqptAccntTree1#
    private List<String> getEqptAccntTree() {
        String sql = "select EqptAccntID, EqptAccntInform from eqptaccnttree";
        OrderDBHelper helper = DbManager.getInstance(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        List<String> pointList = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            pointList.add(cursor.getInt(cursor.getColumnIndex("EqptAccntID"))+"/ "+cursor.getString(cursor.getColumnIndex("EqptAccntInform")));
        }
        cursor.close();
        db.close();
        return pointList;
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
