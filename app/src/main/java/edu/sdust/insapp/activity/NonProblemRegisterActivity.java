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
import edu.sdust.insapp.bean.NonDeviceFaultBean;
import edu.sdust.insapp.bean.NonDeviceFaultRequest;
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

public class NonProblemRegisterActivity extends AppCompatActivity implements MyAdapter.MyAdapterListener {
    @BindView(R.id.tv_non_problem_register_time_value)
    TextView time;
    //问题描述
    @BindView(R.id.et_non_problem_register_description_value)
    EditText describe;
    //专业
    @BindView(R.id.tv_no_problem_register_appearance_value)
    Spinner major;
    //问题现象
    @BindView(R.id.tv_non_problem_register_appearance_value)
    Spinner appearance;
    //处理方式
    @BindView(R.id.spinner_non_problem_register_method)
    Spinner solveMethod;
    //问题发现部位
    @BindView(R.id.tv_non_problem_register_view_part)
    EditText problemPosition;
    //保存
    @BindView(R.id.btn_non_problem_register_save)
    Button saveButton;
    //取消
    @BindView(R.id.btn_non_problem_register_cancel)
    Button cancelButton;
    //选择问题所属装置
    @BindView(R.id.tv_non_problem_register_device)
    TextView device;


    private List<String> appearances;
    private String selectAppearance;
    private List<String> methods;
    private List<String> majors;
    private String selectMethod;
    private String selectmajor;
    private GridView mImagesWall;   //照片墙，显示勾选的照片
    private MyAdapter mAdapter;
    private static ArrayList<String> paths;
    private List<Integer> pictureIds;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private static final int REQUEST_CODE = 0;
    private static final int SUCCESS_CHOOSE = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_problem_register);
        ButterKnife.bind(this);
        appearances = new ArrayList<>();
        methods = new ArrayList<>();
        majors = new ArrayList<>();
        pictureIds = new ArrayList<>();
        paths = new ArrayList<>();
        helper = DbManager.getInstance(this);
        initViews();
        initEvents();
        init();
    }

    private void init() {
        initView();
        //initData();
    }

    /*private void initData() {

    }*/

    private void initViews() {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time.setText(df.format(new Date(System.currentTimeMillis())));
        getAppearances();
        getMajors();
        getMethods();
    }
    private void initEvents(){
        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProblemDeviceActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        appearance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectAppearance = appearances.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectmajor = majors.get(i);
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NonDeviceFaultRequest nondeviceFaultRequest = new NonDeviceFaultRequest();   //这个是上传的参数
                NonDeviceFaultBean nondeviceFaultBean = new NonDeviceFaultBean();
                String faultPhenoID = selectAppearance.split(":")[0];
                String treatMethodID = selectMethod.split(":")[0];
                String Major = selectmajor.split(":")[0];
                String Describe = describe.getText().toString();
                if (Describe == null || Describe.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "问题描述不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String problemposition = problemPosition.getText().toString();
                String deviceId = device.getText().toString().split(":")[0];
                if (deviceId.equals("选择问题所属装置")) {
                    Toast.makeText(getApplicationContext(), "请选择问题所属装置", Toast.LENGTH_SHORT).show();
                    return;
                }
                //String discoveryTime = time.getText().toString();

                db  = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select username,password from " + DbConstant.USER_TABLE, null);
                if (cursor.moveToFirst()) {
                    String username = cursor.getString(cursor.getColumnIndex("username"));
                    String password = cursor.getString(cursor.getColumnIndex("password"));
                    nondeviceFaultRequest.setUsername(username);                                //用户名和密码在本地数据库中可以取到
                    nondeviceFaultRequest.setPassword(password);
                }
                nondeviceFaultRequest.setNonDeviceFault(nondeviceFaultBean);
                nondeviceFaultBean.setFaultPhenoID(faultPhenoID);
                nondeviceFaultBean.setDiscoveryTime(new Date());
                nondeviceFaultBean.setFaultPosition(problemposition);
                nondeviceFaultBean.setMajor(Integer.parseInt(Major));
                nondeviceFaultBean.setTreatMethodID(treatMethodID);
                nondeviceFaultBean.setDescribe(Describe);
                nondeviceFaultBean.setDeviceID(Integer.valueOf(deviceId));

                LinkedList<String[]> files = new LinkedList<>();//这部分是要上传的文件，按格式写即可
                int i= 0;
                for(String p:paths){
                    i++;
                    files.add(new String[]{p , String.valueOf(i)});
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();        //这一部分通常无需改动
                HashMap<String, String> stringMap = new HashMap<>();
                stringMap.put("params",gson.toJson(nondeviceFaultRequest));

                MyApplication.getHttpQueue().add(new PostUploadRequest(UrlConstant.noupLoad, files, stringMap
                        , new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int resultCode = 0;
                        try {
                            resultCode = response.getInt("result");
                            if (resultCode == 0) {
                                Toast.makeText(NonProblemRegisterActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NonProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
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
    }

    private void initView() {
        mImagesWall = (GridView) findViewById(R.id.id_grid_view_commit_answers);
        mAdapter = new MyAdapter(getApplicationContext(), paths, this);
        mImagesWall.setAdapter(mAdapter);
        mImagesWall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position) != null) {
                    // 跳转到预览界面
                    ImageSelector.getInstance().launchDeletePreview(NonProblemRegisterActivity.this, paths, position);
                }
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
              device.setText(data.getStringExtra("device"));
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
            mAdapter = new MyAdapter(NonProblemRegisterActivity.this, paths, this);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NonProblemRegisterActivity.this, R.layout.spinner_child, appearances);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                appearance.setAdapter(adapter);
                selectAppearance = appearances.get(0);
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(NonProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }


    public void getMajors(){
        String  ur  = UrlConstant.ectg;
        VolleyRequest.RequestGet(ur, "getCate", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(result).getAsJsonArray();
                majors.clear();
                for(JsonElement json:jsonarray){
                    JsonObject itemJson = json.getAsJsonObject();
                    String temp = itemJson.get("id").getAsString()+":"+itemJson.get("label").getAsString();
                    majors.add(temp);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NonProblemRegisterActivity.this, R.layout.spinner_child, majors);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                major.setAdapter(adapter);
                selectmajor = majors.get(0);
            }
            @Override
            public void onError(VolleyError result) {
                Toast.makeText(NonProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NonProblemRegisterActivity.this, R.layout.spinner_child, methods);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                solveMethod.setAdapter(adapter);
                selectMethod = methods.get(0);
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(NonProblemRegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });

    }

    @Override
    public void onAddButtonClick() {
        ImageSelector.getInstance().launchSelector(NonProblemRegisterActivity.this,paths);
    }
}
