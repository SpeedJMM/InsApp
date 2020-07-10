package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.netease.imageSelector.ImageSelector;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.EqptTag;
import edu.sdust.insapp.bean.Problemphenomenon;
import edu.sdust.insapp.bean.Problemscenetreatmentmode;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.MyAdapter;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

import static com.netease.imageSelector.ImageSelectorConstant.OUTPUT_LIST;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_IMAGE;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_PREVIEW;

public class ProblemDetailActivity extends AppCompatActivity implements MyAdapter.MyAdapterListener{
    @BindView(R.id.sv_problem_detail_scrollview)
    ScrollView scrollView;
    //设备id
    @BindView(R.id.tv_problem_detail_chooseId)
    AutoCompleteTextView deviceID;
    //问题发现时间
    @BindView(R.id.tv_problem_detail_time_value)
    TextView findtime;
    //问题描述
    @BindView(R.id.et_problem_detail_description_value)
    EditText description;
    //问题现象
    @BindView(R.id.tv_problem_detail_appearance_value)
    Spinner appearance;
    //问题部位
    @BindView(R.id.tv_problem_detail_view_part)
    TextView problemPart;
    @BindView(R.id.lv_problem_detail_problem_position)
    ListView positionList;
    //处理方式
    @BindView(R.id.spinner_problem_detail_method)
    Spinner solveMethod;
    //删除按钮
    @BindView(R.id.btn_problem_detail_delete)
    Button deleteButton;
    //取消按钮
    @BindView(R.id.btn_problem_detail_cancel)
    Button cancelButton;
    //修改按钮
    @BindView(R.id.btn_problem_detail_modify)
    Button modifyButton;
    //sqlite中问题id
    private int problemId;
    //问题现象下拉列表
    private List<String> appearances;
    //处理方式下拉列表
    private List<String> methods;
    //选中的问题现象
    private int selectAppearance;
    private int selectMethod;
    private GridView mImagesWall;   //照片墙，显示勾选的照片
    private MyAdapter mAdapter;
    private String new_selectAppearance;
    private String new_selectMethod;
    private List<String> deviceIds;
    private ArrayList<String> paths;
    private SQLiteDatabase db;
    private OrderDBHelper helper;
    private int deviceId;
    private String faultPhenoId;
    private List<String> faultPositionIds;
    private  String treatMethodId;
    private String describe;
    private String discoveryTime;
    private String selectDevice;
    private List<Integer> attachmentIds;
    private List<String> p_id;//问题部位id
    private String deviceTag;
    private ArrayAdapter<String> positionAdapter;
    private List<String> problemPositions;
    private static int REQUEST_CODE = 5;
    private static final int SUCCESS_CHOOSE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_detail);
        ButterKnife.bind(this);
        faultPositionIds = new ArrayList<>();
        attachmentIds = new ArrayList<>();
        Bundle bundle = this.getIntent().getExtras();
        problemId = (int) bundle.get("id");
        helper = DbManager.getInstance(this);
        appearances = new ArrayList<>();
        methods = new ArrayList<>();
        p_id = new ArrayList<>();
        problemPositions = new ArrayList<>();
        deviceIds = new ArrayList<>();
        paths = new ArrayList<>();
        //readAndshowInfo();
//        init();

        getIds();
        initClickEvents();

    }

    private void init() {
//        initData();
        initViews();
    }
    private void initData() {
        paths = new ArrayList<>();
    }

    private void initViews() {
        //readAndshowInfo();
        getAppearances();
        getMethods();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProblemDetailActivity.this,android.R.layout.simple_list_item_1, deviceIds);
        deviceID.setAdapter(adapter);
        findtime.setText(discoveryTime);
        description.setText(describe);
        mImagesWall = (GridView) findViewById(R.id.ngiv_problem_detail_choose_picture);
        mAdapter = new MyAdapter(getApplicationContext(), paths, this);
        mImagesWall.setAdapter(mAdapter);
        mImagesWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position) != null) {
                    // 跳转到预览界面
                    ImageSelector.getInstance().launchDeletePreview(ProblemDetailActivity.this, paths, position);
                }
            }
        });
    }

    //初始化点击事件
    private void initClickEvents(){
        //查看问题部位
        problemPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProblemDetailActivity.this, ProblemPositionActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //问题部位列表，长按删除
        positionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                AlertDialog dialog = new AlertDialog.Builder(ProblemDetailActivity.this)
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
        //解决listView的touch事件和scrollView的touch事件冲突问题
        positionList.setOnTouchListener(new View.OnTouchListener() {

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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getReadableDatabase();
                db.beginTransaction();
                try{
                    db.execSQL("delete from "+DbConstant.PICTURE_TABLE+" where deviceId=?", new Object[]{
                            problemId
                    });
                    db.execSQL("delete from "+DbConstant.POSITION_TABLE+" where deviceId=?", new Object[]{
                            problemId
                    });
                    db.execSQL("delete from "+DbConstant.DEVICE_FAULT_TABLE+" where _id="+problemId);
                    db.setTransactionSuccessful();
                    Toast.makeText(ProblemDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                }finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设备id
                String temp_device=null;
                if(deviceID.getText().toString().equals(null) ||deviceID.getText().toString().equals("")){
                    temp_device = deviceID.getHint().toString();
                }else{
                    temp_device = deviceID.getText().toString();
                }
                if(temp_device == null || temp_device.equals("")){
                    Toast.makeText(ProblemDetailActivity.this, "设备位号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkDeviceTag(temp_device)){
                    Toast.makeText(ProblemDetailActivity.this, "设备位号不合法", Toast.LENGTH_SHORT).show();
                    return;
                }

                db = helper.getReadableDatabase();
                db.beginTransaction();
                try{
                    db.execSQL("delete from "+DbConstant.PICTURE_TABLE+" where deviceId=?", new Object[]{
                            problemId
                    });
                    //故障部位
                    db.execSQL("delete from "+DbConstant.POSITION_TABLE+" where deviceId=?", new Object[]{
                            problemId
                    });
//                    db.execSQL("delete from "+DbConstant.DEVICE_FAULT_TABLE+" where _id="+problemId);
                    //设备id
                    int temp_deviceID = Integer.valueOf(temp_device.split(": ")[0]);
                    //问题描述
                    String temp_describe = description.getText().toString();
                    //问题现象
                    String temp_faultPhenoID =  new_selectAppearance.split(":")[0];
                    //处理方式
                    String temp_treatMethodID = new_selectMethod.split(":")[0];
                    attachmentIds.clear();
                    //存储问题信息
                    String update_sql = "update "+DbConstant.DEVICE_FAULT_TABLE+" set deviceId=? and faultPhenoId=? and treatMethodId=? and describe=? and discoveryTime=? where _id="+problemId;
//                    String sql = "insert into "+ DbConstant.DEVICE_FAULT_TABLE+"(deviceId, faultPhenoId, treatMethodId, describe, discoveryTime) values (?,?,?,?,?);";
                    db.execSQL(update_sql, new Object[]{
                            temp_deviceID,
                            temp_faultPhenoID,
                            temp_treatMethodID,
                            temp_describe,
                            discoveryTime,
                    });
//                    //获取_id
//                    Cursor temp_cursor = db.rawQuery("select _id from "+DbConstant.DEVICE_FAULT_TABLE+" where deviceId="+temp_deviceID+" and faultPhenoId=? and treatMethodId=? and describe=? and discoveryTime=?", new String[]{
//                            temp_faultPhenoID,
//                            temp_treatMethodID,
//                            temp_describe,
//                            discoveryTime,
//                    });
//                    int id = temp_cursor.getInt(temp_cursor.getColumnIndex("_id"));
                    int id = problemId;
                    //照片路径存储到本地数据库
                    for(String p:paths){
                        db.execSQL("insert into "+DbConstant.PICTURE_TABLE+" (path,deviceId) values (?,?)", new Object[]{
                                p,
                                id
                        });
                        Cursor cursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE+" where path=? and deviceId="+id, new String[]{
                                p
                        });
                        if(cursor.moveToFirst()){
                            attachmentIds.add(cursor.getInt(cursor.getColumnIndex("_id")));
                        }
                    }
                    //问题部位存储到本地数据库
                    for(String p:problemPositions){
                        db.execSQL("insert into "+DbConstant.POSITION_TABLE+" (pID, label, deviceId) values (?,?,?)", new Object[]{
                                p.split(": ")[0],
                                p.split(": ")[1],
                                id
                        });
                        p_id.add(p.split(": ")[0]);
                    }
                    db.execSQL("update "+DbConstant.DEVICE_FAULT_TABLE+" set faultPositionIds=?, attachmentIds=? where _id="+id, new Object[]{
                            p_id.toString(),
                            attachmentIds.toString()
                    });
                    Toast.makeText(ProblemDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    db.setTransactionSuccessful();
                    finish();
                }finally {
                    db.endTransaction();
                    db.close();

                }

            }
        });
        appearance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new_selectAppearance = appearances.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        solveMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new_selectMethod = methods.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }//检查设备位号合法性
    private boolean checkDeviceTag(String tag){
        for(String s:deviceIds){
            if(s.equals(tag)){
                return true;
            }
        }
        return false;
    }
    //读取并显示问题信息
    private void readAndshowInfo(){
        db = helper.getReadableDatabase();
        String sql = "select * from "+DbConstant.DEVICE_FAULT_TABLE+" where _id = "+problemId;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            deviceId = cursor.getInt(cursor.getColumnIndex("deviceId"));
            faultPhenoId = cursor.getString(cursor.getColumnIndex("faultPhenoId"));
            String temp = cursor.getString(cursor.getColumnIndex("faultPositionIds"));
            String[] t;
            if(!temp.equals("[]")){
                temp = temp.substring(1, temp.length()-1);
                t = temp.split(", ");
                faultPositionIds = Arrays.asList(t);
                problemPositions.clear();
                for(String s:faultPositionIds){
                    Cursor tempCursor = db.rawQuery("select * from "+DbConstant.POSITION_TABLE+" where pID=? and deviceId="+problemId, new String[]{
                            s
                    });
                    while(tempCursor.moveToNext()){
                        String positionId = tempCursor.getString(tempCursor.getColumnIndex("pID"));
                        String label = tempCursor.getString(tempCursor.getColumnIndex("label"));
                        problemPositions.add(positionId+": "+label);
                    }
                    positionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, problemPositions);
                    positionList.setAdapter(positionAdapter);
                }
            }

            treatMethodId = cursor.getString(cursor.getColumnIndex("treatMethodId"));
            describe = cursor.getString(cursor.getColumnIndex("describe"));
            discoveryTime = cursor.getString(cursor.getColumnIndex("discoveryTime"));
            temp = cursor.getString(cursor.getColumnIndex("attachmentIds"));
            if(!"[]".equals(temp)){
                temp = temp.substring(1, temp.length()-1);
                t = temp.split(",");
                for(String s:t){
                    s = s.trim();
                    attachmentIds.add(Integer.valueOf(s));
                }

                Cursor cursorTemp;
                for(int i:attachmentIds){
                    cursorTemp = db.rawQuery("select path from "+DbConstant.PICTURE_TABLE+" where _id="+i, null);
                    if(cursorTemp.moveToFirst()){
                        paths.add(cursorTemp.getString(0));
                    }
                }
            }

        }else{
            Toast.makeText(ProblemDetailActivity.this, "数据读取错误", Toast.LENGTH_SHORT).show();
        }
        db.close();

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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProblemDetailActivity.this, R.layout.spinner_child, appearances);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                appearance.setAdapter(adapter);
                for(String s:appearances){
                    if(s.split(":")[0].equals(faultPhenoId)){
                        selectAppearance = adapter.getPosition(s);
                        new_selectAppearance = s;
                        appearance.setSelection(selectAppearance);
                        break;
                    }
                }
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(ProblemDetailActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProblemDetailActivity.this, R.layout.spinner_child, methods);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                solveMethod.setAdapter(adapter);
                for(String s:methods){
                    if(s.split(":")[0].equals(treatMethodId)){
                        selectMethod = adapter.getPosition(s);
                        solveMethod.setSelection(selectMethod);
                        new_selectMethod = s;
                        break;
                    }
                }
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(ProblemDetailActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 接收图片选择器返回结果，更新所选图片集合
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PREVIEW || requestCode == REQUEST_IMAGE) {
            ArrayList<String> newFiles = data.getStringArrayListExtra(OUTPUT_LIST);
            if (newFiles != null) {
                updateImages(newFiles);
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
            mAdapter = new MyAdapter(ProblemDetailActivity.this, paths, this);
        }
        mImagesWall.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onAddButtonClick() {
        ImageSelector.getInstance().launchSelector(ProblemDetailActivity.this,paths);
    }

    private void getIds() {
        db = helper.getReadableDatabase();
        String sql = "select * from "+DbConstant.DEVICE_FAULT_TABLE+" where _id = "+problemId;
        Cursor cursor = db.rawQuery(sql, null);
        int d_id = -1;
        if(cursor.moveToFirst()){
            d_id = cursor.getInt(cursor.getColumnIndex("deviceId"));
        }
        String url = UrlConstant.eqptTagURL;
        final int finalD_id = d_id;
        VolleyRequest.RequestGet(url, "getDeviceID", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(result).getAsJsonArray();
                deviceIds.clear();
                for(JsonElement json:jsonarray){
                    EqptTag e = gson.fromJson(json, EqptTag.class);
                    if(e.getEqptaccntid()== finalD_id){
                        selectDevice=e.toString();
                        deviceID.setHint(selectDevice);
                    }
                    deviceIds.add(e.toString());
                }
                readAndshowInfo();
                findtime.setText(discoveryTime);
                description.setText(describe);
                initViews();

            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(ProblemDetailActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }
    //    //读取问题部位多选
//    private void getData(){
//        List<Integer> p_id = new ArrayList<>();
//        db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select faultPositionIds from "+DbConstant.DEVICE_FAULT_TABLE+" _id="+problemId, null);
//        if(cursor.moveToFirst()){
//            String temp = cursor.getString(0);
//
//        }
//
//    }
}
