package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netease.imageSelector.ImageSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.DeviceFaultBean;
import edu.sdust.insapp.bean.DeviceFaultRequest;
import edu.sdust.insapp.bean.EqptTag;
import edu.sdust.insapp.bean.Problemphenomenon;
import edu.sdust.insapp.bean.Problemscenetreatmentmode;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.MyAdapter;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.PostUploadRequest;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

import static com.netease.imageSelector.ImageSelectorConstant.OUTPUT_LIST;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_IMAGE;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_PREVIEW;


public class ProblemRegisterActivity extends AppCompatActivity implements MyAdapter.MyAdapterListener {

    @BindView(R.id.sv_problem_register_scrollview)
    ScrollView scrollView;
    //设备ID
    @BindView(R.id.tv_problem_register_device_id_choose)
    AutoCompleteTextView deviceId;
    //问题发现时间
    @BindView(R.id.tv_problem_register_time_value)
    TextView time;
    //问题描述
    @BindView(R.id.et_problem_register_description_value)
    EditText description;
    //问题现象
    @BindView(R.id.tv_problem_register_appearance_value)
    Spinner appearance;
    //问题部位
    @BindView(R.id.tv_problem_register_view_part)
    TextView problemPart;
    //问题部位显示
    @BindView(R.id.listview_problem_register_problem_position)
    ListView PositionList;
    //专业
    @BindView(R.id.tv_problem_register_appearance_value1)
    Spinner cate;
    //处理方式
    @BindView(R.id.spinner_problem_register_method)
    Spinner solveMethod;
    //取消
    @BindView(R.id.btn_problem_register_cancel)
    Button cancelButton;
    @BindView(R.id.btn_problem_register_save)
    Button submitBtn;



    private static final int REQUEST_CODE = 0;
    private static final int SUCCESS_CHOOSE = 2;
    //问题现象下拉列表
    private List<String> appearances;
    //处理方式下拉列表
    private List<String> methods;
    private List<String> cates;
    //选中的问题现象
    private String selectAppearance;
    private String selectMethod;
    private String selectcate;
    private GridView mImagesWall;   //照片墙，显示勾选的照片
    private MyAdapter mAdapter;
    private ArrayList<String> paths;
    private List<String> deviceIds;
    private List<String> problemPositions;
    private List<Integer> pictureIds;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private ArrayAdapter<String> positionAdapter;
    private List<String> p_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_register);
        ButterKnife.bind(this);
        helper = DbManager.getInstance(this);
        appearances = new ArrayList<>();
        cates = new ArrayList<>();
        methods = new ArrayList<>();
        paths = new ArrayList<>();
        deviceIds = new ArrayList<>();
        p_id = new ArrayList<>();
        problemPositions = new ArrayList<>();
        pictureIds = new ArrayList<>();
        positionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, problemPositions);
        initViews();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProblemRegisterActivity.this, R.layout.item_dropdown, deviceIds);
        deviceId.setAdapter(adapter);
        PositionList.setAdapter(positionAdapter);
        //initViews();
        initEvents();
    }



    private void initViews(){
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time.setText(df.format(new Date(System.currentTimeMillis())));
        getIds();
        getAppearances();
        getMethods();
        cate();
        mImagesWall = (GridView) findViewById(R.id.ngiv_problem_register_choose_picture);
        mAdapter = new MyAdapter(getApplicationContext(), paths, this);
        mImagesWall.setAdapter(mAdapter);
        mImagesWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position) != null) {
                    // 跳转到预览界面
                    ImageSelector.getInstance().launchDeletePreview(ProblemRegisterActivity.this, paths, position);
                }
            }
        });

    }


    private void initEvents(){
        appearance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectAppearance = appearances.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectcate = cates.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        solveMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectMethod = methods.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        problemPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProblemRegisterActivity.this, ProblemPositionActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //解决listView的touch事件和scrollView的touch事件冲突问题
        PositionList.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }else{
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        PositionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                AlertDialog dialog = new AlertDialog.Builder(ProblemRegisterActivity.this)
                        .setMessage("是否删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                problemPositions.remove(index);
                                positionAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                dialog.show();

                return true;
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceFaultRequest deviceFaultRequest = new DeviceFaultRequest();   //这个是上传的参数
                DeviceFaultBean deviceFaultBean = new DeviceFaultBean();
                String device = deviceId.getText().toString();
                if("".equals(device) || device == null){
                    Toast.makeText(ProblemRegisterActivity.this, "设备位号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkDeviceTag(device)){
                    Toast.makeText(ProblemRegisterActivity.this, "设备位号不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] temp = device.split("/");
                int deviceID = Integer.valueOf(temp[0]);
                //String deviceName = temp[1];
                String describe = description.getText().toString();
                if (describe == null || describe.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "问题描述不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String faultPhenoID = selectAppearance.split(":")[0];
                String cateId = selectcate.split(":")[0];
                String treatMethodID = selectMethod.split(":")[0];


                db  = helper.getReadableDatabase();

                Cursor cursor = db.rawQuery("select username,password from " + DbConstant.USER_TABLE, null);
                if (cursor.moveToFirst()) {
                    String username = cursor.getString(cursor.getColumnIndex("username"));
                    String password = cursor.getString(cursor.getColumnIndex("password"));
                    deviceFaultRequest.setUsername(username);                                //用户名和密码在本地数据库中可以取到
                    deviceFaultRequest.setPassword(password);
                }
                deviceFaultRequest.setDeviceFault(deviceFaultBean);                 //这句话不用动
                deviceFaultBean.setDescribe(describe);                             //设置设备问题的信息
                deviceFaultBean.setDiscoveryTime(new Date());                       //同上
                deviceFaultBean.setDeviceID(deviceID);//同上，其他参数同理，按类型set即可，不一一列举（bean类以前功能使用过，无需修改）
                deviceFaultBean.setAttachmentIDs(null);//!!!此参数是给批量添加所用，此处无需设置。甚至这句话也可以不写。
                deviceFaultBean.setTreatMethodID(treatMethodID);
                deviceFaultBean.setMajor(Integer.parseInt(cateId));
                deviceFaultBean.setFaultPhenoID(faultPhenoID);
                for(String s:problemPositions){
                    p_id.add(s.split(": ")[0]);
                    Log.i("问题",s.split(": ")[0]);
                }
                List<String> faultPositionIDs = p_id;
                deviceFaultBean.setFaultPositionIDs(faultPositionIDs);
                LinkedList<String[]> files = new LinkedList<>();//这部分是要上传的文件，按格式写即可
                int i= 0;
                for(String p:paths){
                    i++;
                    files.add(new String[]{p , String.valueOf(i)});
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();        //这一部分通常无需改动
                HashMap<String, String> stringMap = new HashMap<>();
                stringMap.put("params",gson.toJson(deviceFaultRequest));


                //修改url，然后在回调函数里处理结果即可。
                //成功会返回{"result":0}，失败则是http500或者{"result":-1}
                MyApplication.getHttpQueue().add(new PostUploadRequest(UrlConstant.upload, files, stringMap
                        , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int resultCode = 0;
                        try {
                            resultCode = response.getInt("result");
                            if (resultCode == 0) {
                                Toast.makeText(ProblemRegisterActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                }));

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //在此处设置好设备问题的参数
        //可以参见InspectionActivity

        //非设备问题也是同理，相应的Request和Bean已经建好，NonDeviceFaultBean和NonDeviceFaultRequest
        //url是/app/fault/uploadNonEqptFault
    }


    //检查所选设备位号是否合格
    private boolean checkDeviceTag(String tag){
        for(String s:deviceIds){
            if(s.equals(tag)){
                return true;
            }
        }
        return false;
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

    private void getIds() {

//        List<EqptTag> ef = this.getAllEqpt();
//        for (EqptTag e : ef) {
//            deviceIds.add(e.toString());
//        }
        deviceIds.addAll(getEqptAccntTree());
    }

    public void getAppearances() {
        String url = UrlConstant.problemPhenomenonURL;
        VolleyRequest.RequestGet(url, "appearance", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(result).getAsJsonArray();
                appearances.clear();
                for(JsonElement json:jsonarray){
                    Problemphenomenon p = gson.fromJson(json, Problemphenomenon.class);
                    appearances.add(p.toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProblemRegisterActivity.this, R.layout.spinner_child, appearances);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                appearance.setAdapter(adapter);
                selectAppearance = appearances.get(0);
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(ProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {
            }
        });
    }
    public void cate(){
    String  ur  = UrlConstant.ectg;
        VolleyRequest.RequestGet(ur, "getCate", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(result).getAsJsonArray();
                cates.clear();
                for(JsonElement json:jsonarray){
                    JsonObject itemJson = json.getAsJsonObject();
                    String temp = itemJson.get("id").getAsString()+":"+itemJson.get("label").getAsString();
                    cates.add(temp);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProblemRegisterActivity.this, R.layout.spinner_child, cates);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cate.setAdapter(adapter);
                selectcate = cates.get(0);
            }
            @Override
            public void onError(VolleyError result) {
                Toast.makeText(ProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    public void getMethods() {
        String url=UrlConstant.inspproblemscenetreatmentmodeURL;
        VolleyRequest.RequestGet(url, "getMethods", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(result).getAsJsonArray();
                methods.clear();
                for(JsonElement json:jsonarray){
                    Problemscenetreatmentmode p = gson.fromJson(json, Problemscenetreatmentmode.class);
                    methods.add(p.toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProblemRegisterActivity.this, R.layout.spinner_child, methods);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                solveMethod.setAdapter(adapter);
                selectMethod = methods.get(0);
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(ProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        // 接收图片选择器返回结果，更新所选图片集合
        if (requestCode == REQUEST_PREVIEW || requestCode == REQUEST_IMAGE) {
            ArrayList<String> newFiles = data.getStringArrayListExtra(OUTPUT_LIST);
            if (newFiles != null) {
                updateImages(newFiles);
            }
        }
        else if(requestCode == REQUEST_CODE) {
            if (resultCode == SUCCESS_CHOOSE) {
                problemPositions.add(data.getStringExtra("position"));
                positionAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        paths = null;
    }
    /**
     * 更新所选图片集合
     *
     * @param images
     */
    private void updateImages(List<String> images) {
        if (paths == null) {
            paths = new ArrayList<>();
        }
        paths.clear();
        for (String s : images) {
            paths.add(s);
        }
        if (mAdapter == null) {
            mAdapter = new MyAdapter(ProblemRegisterActivity.this, paths,this);
        }
        mImagesWall.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onAddButtonClick() {
        ImageSelector.getInstance().launchSelector(ProblemRegisterActivity.this,paths);
    }
}
