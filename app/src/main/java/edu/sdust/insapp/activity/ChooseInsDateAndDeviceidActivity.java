package edu.sdust.insapp.activity;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.EqptTag;
import edu.sdust.insapp.dialog.DatePickerDialog;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;

public class ChooseInsDateAndDeviceidActivity extends Fragment {
    @BindView(R.id.btn_choose_ins_date_and_deviceid_oneweek)
    Button selectOneWeekBtn;

    @BindView(R.id.btn_choose_ins_date_and_deviceid_onemonth)
    Button selectOneMonthBtn;

    @BindView(R.id.btn_choose_ins_date_and_deviceid_select)
    Button selectSelectInputBtn;

    @BindView(R.id.et_choose_ins_date_and_deviceid_start_time)
    EditText startTimeET;

    @BindView(R.id.et_choose_ins_date_and_deviceid_end_time)
    EditText endTimeET;

    @BindView(R.id.autotv_choose_ins_date_and_deviceid_id)
    AutoCompleteTextView deviceTagACTV;

    private OrderDBHelper helper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_choose_ins_date_and_deviceid, container, false);
        //setContentView(R.layout.activity_choose_ins_date_and_deviceid);
        ButterKnife.bind(this,view);
        helper = DbManager.getInstance(getActivity());
        selectOneMonthBtn.setOnClickListener(handler);
        selectOneWeekBtn.setOnClickListener(handler);
        selectSelectInputBtn.setOnClickListener(handler);
        startTimeET.setOnClickListener(handler);
        endTimeET.setOnClickListener(handler);
        initACTV();
        return view;
    }


    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            //选择时间的框
            switch (v.getId()) {
                case R.id.et_choose_ins_date_and_deviceid_start_time:
                    DatePickerDialog.showDatePickerDialog(startTimeET, getActivity());
                    return;
                case R.id.et_choose_ins_date_and_deviceid_end_time:
                    DatePickerDialog.showDatePickerDialog(endTimeET, getActivity());
                    return;
            }


            //其他
//        String deviceTagNumber = etDeviceTagNumber.getText().toString().trim();
            String deviceTagNumber = deviceTagACTV.getText().toString().trim();
            if (deviceTagNumber == null || "".equals(deviceTagNumber)) {
                Toast.makeText(getActivity(), "设备不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            //Log.i("shebei",deviceTagNumber);
            String[] temp = deviceTagNumber.split("/");
            int deviceID = Integer.valueOf(temp[0]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            switch (v.getId()) {
                case R.id.btn_choose_ins_date_and_deviceid_onemonth:
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.HOUR, 0);
                    Log.i("时间1", String.valueOf(calendar.getTime()));
                    executeSelect(calendar.getTime(), new Date(), deviceID);
                    break;

                case R.id.btn_choose_ins_date_and_deviceid_oneweek:
                    calendar.add(Calendar.DAY_OF_WEEK, -7);
                    calendar.set(Calendar.HOUR, 0);

                    executeSelect(calendar.getTime(), new Date(), deviceID);
                    break;

                case R.id.btn_choose_ins_date_and_deviceid_select:
                    String startDate = startTimeET.getText().toString().trim();
                    String endDate = endTimeET.getText().toString().trim();
                    if (startDate == null || "".equals(startDate) || endDate == null || "".equals(endDate)) {
                        Toast.makeText(getActivity(), "使用手动查询时，必须指定开始和结束时间", Toast.LENGTH_LONG).show();
                        return;
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        executeSelect(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate), deviceID);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e(getClass().getName(), "内部错误：日期解析异常");
                        Toast.makeText(getActivity(), "内部错误，请尝试重新选择时间", Toast.LENGTH_LONG).show();
                    }

            }
        }
    };

    public void executeSelect(Date startTime, Date endTime,Integer deviceID) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Bundle bundle = new Bundle();
            //bundle.putString("deviceTag", deviceTag);
            bundle.putString("id", String.valueOf(deviceID));
            //Log.i("de2222",String.valueOf(deviceID));
            bundle.putString("startTime", simpleDateFormat.format(startTime));
            bundle.putString("endTime", simpleDateFormat.format(endTime));
            Intent intent = new Intent(getActivity(), HistoryDataChartActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
    }

    public List<EqptTag> getAllEqpt() {
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

    //#ViEqptAccntTree1#
    public List<String> getEqptAccntTree() {
        String sql = "select EqptAccntID, EqptAccntInform from eqptaccnttree";
        SQLiteDatabase db = helper.getWritableDatabase();
        List<String> pointList = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            pointList.add(cursor.getInt(cursor.getColumnIndex("EqptAccntID"))+"/ "+cursor.getString(cursor.getColumnIndex("EqptAccntInform")));
        }
        return pointList;
    }

    public void initACTV() {
            //网络部分
//            final String url = eqptTagURL;
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Gson gson = new Gson();
//                    JsonParser parser = new JsonParser();
//                    JsonArray jsonarray = parser.parse(response).getAsJsonArray();
                   List<String> eqptTags = new ArrayList<>();
                    //List<EqptTag> jsonarray = this.getAllEqpt();
//                    for (EqptTag e : jsonarray) {
//                        eqptTags.add(e.toString());
//                    }
        eqptTags.addAll(getEqptAccntTree());

                    if (deviceTagACTV != null&&getActivity() != null) {
                        deviceTagACTV.setEnabled(true);
                        deviceTagACTV.setHint("请输入设备位号");
                        deviceTagACTV.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_dropdown, eqptTags));
                    }

            deviceTagACTV.setTransformationMethod(new ReplacementTransformationMethod() {
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

    }
//            , new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if (deviceTagACTV != null)
//                        deviceTagACTV.setHint("设备位号请求失败");
//                }
//            });
            //MyApplication.getHttpQueue().add(stringRequest);


            //静态部分
            ////大小写自动转化


}
