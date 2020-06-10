package edu.sdust.insapp.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.HistoryDataBean;
import edu.sdust.insapp.bean.PointInfoBean;
import edu.sdust.insapp.utils.ColorFactory;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.MyAxis;
import edu.sdust.insapp.utils.MyLine;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.TimeConvert;
import edu.sdust.insapp.utils.URLAppend;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class HistoryDataChartActivity extends AppCompatActivity {
    @BindView(R.id.line_chart_history_data_chart_main)
    LineChartView historyChart;
    @BindView(R.id.rg_history_data_chart_choose_group)
    RadioGroup radioGroup;
    @BindView(R.id.rb_history_data_chart_tempe)
    RadioButton tempe;
    @BindView(R.id.rb_history_data_chart_vibra)
    RadioButton vibra;
    @BindView(R.id.rb_history_data_chart_speed)
    RadioButton speed;
    @BindView(R.id.rb_history_data_chart_acce)
    RadioButton acce;
    private int tag;
    //private String deviceTag;
    private String deviceId;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private HistoryDataBean his;
    private String startTime;
    private String endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data_chart);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        //deviceTag = bundle.getString("deviceTag");
        deviceId = bundle.getString("id");
        //Log.i("de3333",String.valueOf(deviceId));
        startTime = bundle.getString("startTime");
        endTime = bundle.getString("endTime");
        helper = DbManager.getInstance(this);
        initEvents();
        showCharts();
    }
    private void initEvents(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == speed.getId()){
                    tag = 1;
                }else if(i == tempe.getId()){
                    tag = 0;
                }else if(i == vibra.getId()){
                    tag = 3;
                }else if(i == acce.getId()){
                    tag = 2;
                }
                showCharts();
            }
        });
    }

    //请求历史数据
    private void getPointInfos(){
        StringBuffer urlBuffer =  new StringBuffer("/app/ins/downloadMeaPointDataByDeviceId");
        //Log.i("devi111",String.valueOf(deviceId));
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select username,password from "+ DbConstant.USER_TABLE, null);
        if(cursor.moveToFirst()){
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            //获取过去一周的时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, -1);
            //c.set(Calendar.HOUR, 0);
            //Log.i("时间", String.valueOf(c.getTime()));


            if(startTime == null || startTime.equals(""))
                startTime = simpleDateFormat.format(c.getTime());
            if(endTime == null || endTime.equals(""))
                endTime = simpleDateFormat.format(new Date());

            String url = String.valueOf(new URLAppend(urlBuffer, this)
                    .addParam("username", username)
                    .addParam("password", password)
                    .addParam("startTime",startTime)
                    .addParam("endTime", endTime)
                    .addParam("deviceID",deviceId));
            VolleyRequest.RequestGet(url, "historyData", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                    his = gson.fromJson(result, HistoryDataBean.class);
                    if(his.getResult()==0){
                        showCharts();
                    }else if(his.getResult() == -1){
                        Toast.makeText(HistoryDataChartActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                    }else if(his.getResult() == -2){
                        Toast.makeText(HistoryDataChartActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }else if(his.getResult()==-3){
                        Toast.makeText(HistoryDataChartActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
                    }else if(his.getResult() == -10){
                        Toast.makeText(HistoryDataChartActivity.this, "设备位号重复", Toast.LENGTH_SHORT).show();
                    }else if(his.getResult()==-11){
                        Toast.makeText(HistoryDataChartActivity.this, "不存在的设备位号", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(VolleyError result) {
                    Toast.makeText(HistoryDataChartActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void jsonOnSuccess(JSONObject result) {

                }
            });
        }
        db.close();
    }
//初始化折线图
    private void showCharts() {
        if(his == null){
            getPointInfos();
        }else{
            ColorFactory colorFactory = new ColorFactory();
            List<Line> lines = new LinkedList<>();
            LinkedList<AxisValue> axisValues = new LinkedList<>();
            if(his.getResult() == 0){
                List<PointInfoBean> pointInfos = his.getPointInfos();
                int sum = 0;
                for(PointInfoBean point:pointInfos){
                    sum = Math.max(sum, point.getPointNum());
                }
                for(int i=0; i<sum; i++){
                    LinkedList<PointValue> lineValues = new LinkedList<>();
                    for(PointInfoBean point:pointInfos){
                        if(point.getPointNum() == i+1){
                            int days = TimeConvert.getDaysByMill(point.getInsTime());
                            PointValue pointValue = new PointValue();
                            if(tag == 0){
                                pointValue.set(days, Float.valueOf(point.getTempe()));
                            }else if(tag == 1){
                                pointValue.set(days, Float.valueOf(point.getSpeed()));
                            }else if(tag == 2){
                                pointValue.set(days, Float.valueOf(point.getAccele()));
                            }else if(tag == 3){
                                pointValue.set(days, Float.valueOf(point.getVibra()));
                            }
                            pointValue.setLabel("");
                            lineValues.add(pointValue);
                            Collections.sort(lineValues, new Comparator<PointValue>() {
                                @Override
                                public int compare(PointValue o1, PointValue o2) {
                                    if(o1.getX() > o2.getX()) {
                                        return 1;
                                    }
                                    if(o1.getX() < o2.getX()) {
                                        return -1;
                                    }
                                    return 0;
                                }
                            });
                            axisValues.add(new AxisValue((float)days));
                        }
                    }
                    lineValues.getLast().setLabel(String.valueOf(i+1));
                    Line line = new MyLine();
                    line.setValues(lineValues);
                    line.setColor(colorFactory.nextColor());
                    lines.add(line);
                }
                final LineChartData data = new LineChartData(lines);
                String textY = null;
                if(tag == 0){
                    textY = new String("温度");
                }else if(tag == 1){
                    textY = new String("速度");
                }else if(tag == 2){
                    textY = new String("加速度");
                }else if(tag == 3){
                    textY = new String("位移");
                }
                data.setAxisXBottom(new MyAxis(true).setName("日期").setValues(axisValues).setAutoGenerated(true));
                data.setAxisYLeft(new MyAxis().setName(textY));
                historyChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
                    @Override
                    public void onValueSelected(int lineIndex, int pointIndex, PointValue pointValue) {
                        ((MyLine)data.getLines().get(lineIndex)).hide();
                    }

                    @Override
                    public void onValueDeselected() {

                    }
                });
                historyChart.startDataAnimation();
                historyChart.setLineChartData(data);
            }
        }

    }
}
