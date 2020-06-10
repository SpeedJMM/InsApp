package edu.sdust.insapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.netease.imageSelector.ImageSelector;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
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

public class NonProblemDetailActivity extends AppCompatActivity implements MyAdapter.MyAdapterListener {
    @BindView(R.id.tv_non_problem_detail_time_value)
    TextView time;
    //问题现象
    @BindView(R.id.tv_non_problem_detail_appearance_value)
    Spinner appearance;
    //问题描述
    @BindView(R.id.et_non_problem_detail_description_value)
    EditText description;
    //处理方式
    @BindView(R.id.spinner_non_problem_detail_method)
    Spinner solveMethod;
    //问题发现地点
    @BindView(R.id.tv_non_problem_detail_view_part)
    EditText problemPosition;
    //删除
    @BindView(R.id.btn_non_problem_detail_delete)
    Button deleteButton;
    //取消
    @BindView(R.id.btn_non_problem_detail_cancel)
    Button cancelButton;
    @BindView(R.id.btn_non_problem_detail_modify)
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
    private String new_selectAppearance;
    private String new_selectMethod;
    private GridView mImagesWall;   //照片墙，显示勾选的照片
    private MyAdapter mAdapter;
    private ArrayList<String> paths;
    private SQLiteDatabase db;
    private OrderDBHelper helper;
    private String faultdeviceId;
    private  String treatMethodId;
    private String describe;
    private String discoveryTime;
    private List<Integer> attachmentIds;
    private String faultposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_problem_detail);
        ButterKnife.bind(this);
        attachmentIds = new ArrayList<>();
        Bundle bundle = this.getIntent().getExtras();
        problemId = (int) bundle.get("id");
        helper = DbManager.getInstance(this);
        appearances = new ArrayList<>();
        methods = new ArrayList<>();
        paths = new ArrayList<>();
        init();
        initEvents();
    }
    private void init() {
        initData();
        initViews();

    }
    private void initData() {
        paths = new ArrayList<>();
    }

    private void initViews(){
        getAppearances();
        getMethods();
        getInfo();
        time.setText(discoveryTime);
        description.setText(describe);
        problemPosition.setText(faultposition);
        mImagesWall = (GridView) findViewById(R.id.ngiv_non_problem_detail_choose_picture);
        mAdapter = new MyAdapter(getApplicationContext(), paths, this);
        mImagesWall.setAdapter(mAdapter);
        mImagesWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position) != null) {
                    // 跳转到预览界面
                    ImageSelector.getInstance().launchDeletePreview(NonProblemDetailActivity.this, paths, position);
                }
            }
        });

    }
    private void initEvents(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getReadableDatabase();
                db.beginTransaction();
                try{
                    db.execSQL("delete from "+DbConstant.PICTURE_TABLE+" where nondeviceId=?", new Object[]{
                            problemId
                    });
                    db.execSQL("delete from "+DbConstant.NON_DEVICE_FAULT_TABLE+" where _id="+problemId);
                    db.setTransactionSuccessful();
                    Toast.makeText(NonProblemDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
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
                db = helper.getReadableDatabase();
                db.beginTransaction();
                try{
                    db.execSQL("delete from "+DbConstant.PICTURE_TABLE+" where nondeviceId=?", new Object[]{
                            problemId
                    });
//                    db.execSQL("delete from "+DbConstant.NON_DEVICE_FAULT_TABLE+" where _id="+problemId);
                    String new_time = time.getText().toString();
                    String new_describe = description.getText().toString();
                    String new_appear = new_selectAppearance;
                    String new_method = new_selectMethod;
                    attachmentIds.clear();
                    //存储非设备问题信息
                    String update_sql = "update "+DbConstant.NON_DEVICE_FAULT_TABLE+" set faultdeviceId=? and faultposition=? and treatmethodId=? and describe=? and discoveryTime=? where _id="+problemId;
//                    String sql = "insert into "+ DbConstant.NON_DEVICE_FAULT_TABLE+"(faultdeviceId, faultposition, treatmethodId, describe, discoveryTime) values (?,?,?,?,?);";
                    db.execSQL(update_sql, new Object[]{
                            new_selectAppearance.split(":")[0],
                            problemPosition.getText().toString(),
                            new_selectMethod.split(":")[0],
                            description.getText().toString(),
                            discoveryTime,
                    });
//                    //获取_id
//                    Cursor temp_cursor = db.rawQuery("select _id from "+DbConstant.NON_DEVICE_FAULT_TABLE+" where faultdeviceId=? and faultposition=? and treatmethodId=? and describe=? and discoveryTime=?", new String[]{
//                            new_selectAppearance.split(":")[0],
//                            problemPosition.getText().toString(),
//                            new_selectMethod.split(":")[0],
//                            description.getText().toString(),
//                            discoveryTime,
//                    });
//                    int id = temp_cursor.getInt(temp_cursor.getColumnIndex("_id"));
                    int id = problemId;
                    //照片路径存储到本地数据库
                    for(String p:paths){
                        db.execSQL("insert into "+DbConstant.PICTURE_TABLE+" (path,nondeviceId) values (?,?)", new Object[]{
                                p,
                                id
                        });
                        Cursor cursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE+" where path=? and nondeviceId="+id, new String[]{
                                p
                        });
                        if(cursor.moveToFirst()){
                            attachmentIds.add(cursor.getInt(cursor.getColumnIndex("_id")));
                        }
                    }
                    //更新记录
                    db.execSQL("update "+DbConstant.NON_DEVICE_FAULT_TABLE+" set attachmentIds=?", new Object[]{
                            attachmentIds.toString()
                    });
                    Toast.makeText(NonProblemDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

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
    }
    private void getInfo(){
        db = helper.getReadableDatabase();
        String sql = "select * from "+ DbConstant.NON_DEVICE_FAULT_TABLE+" where _id = "+problemId;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            faultdeviceId = cursor.getString(cursor.getColumnIndex("faultdeviceId"));
            faultposition = cursor.getString(cursor.getColumnIndex("faultposition"));
            treatMethodId = cursor.getString(cursor.getColumnIndex("treatmethodId"));
            describe = cursor.getString(cursor.getColumnIndex("describe"));
            discoveryTime = cursor.getString(cursor.getColumnIndex("discoveryTime"));
            String temp = cursor.getString(cursor.getColumnIndex("attachmentIds"));
            String[] t;
            if(!temp.equals("[]")){
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
            Toast.makeText(NonProblemDetailActivity.this, "数据读取错误", Toast.LENGTH_SHORT).show();
        }
        db.close();
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
            mAdapter = new MyAdapter(NonProblemDetailActivity.this, paths, this);
        }
        mImagesWall.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NonProblemDetailActivity.this, R.layout.spinner_child, appearances);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                appearance.setAdapter(adapter);
                for(String s:appearances){
                    if(s.split(":")[0].equals(faultdeviceId)){
                        selectAppearance = adapter.getPosition(s);
                        appearance.setSelection(selectAppearance);
                        new_selectAppearance = s;
                        break;
                    }
                }
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(NonProblemDetailActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NonProblemDetailActivity.this, R.layout.spinner_child, methods);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                solveMethod.setAdapter(adapter);
                for(String s:methods){
                    if(s.split(":")[0].equals(treatMethodId)){
                        selectMethod = adapter.getPosition(s);
                        new_selectMethod = s;
                        solveMethod.setSelection(selectMethod);
                        break;
                    }
                }
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(NonProblemDetailActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });

    }
    @Override
    public void onAddButtonClick() {
        ImageSelector.getInstance().launchSelector(NonProblemDetailActivity.this,paths);
    }
}
