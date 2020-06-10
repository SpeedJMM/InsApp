package edu.sdust.insapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.ResultBean;
import edu.sdust.insapp.service.DLDeviceService;
import edu.sdust.insapp.utils.AesUtils;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.btn_login)
    Button login;
    @BindView(R.id.et_login_username)
    EditText usernameText;
    @BindView(R.id.et_login_password)
    EditText passwordText;
    @BindView(R.id.cb_login_remember)
    CheckBox remember;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;//权限变量
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };//申明读写权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        helper = DbManager.getInstance(LoginActivity.this);
        download();
        init();
        onClick();
        verifyStoragePermissions(this);
    }


    /**
     *获取存储权限
     */
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                        1);}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void download() {
        Intent dldevice  = new Intent(this, DLDeviceService.class);
        startService(dldevice);
    }

    private void init(){
        db = helper.getReadableDatabase();
        String sel_sql = "select * from "+DbConstant.USER_TABLE;
        Cursor cursor = db.rawQuery(sel_sql, null);
        if(cursor.moveToFirst()){
            if(cursor.getString(cursor.getColumnIndex("isremember")).equals("1")){
                Intent intent = new Intent(LoginActivity.this, mainView.class);
                startActivity(intent);
                finish();
            }else{
                String username = cursor.getString(cursor.getColumnIndex("username"));
                usernameText.setText(username);
            }
        }
        db.close();
    }
    //登录
    private void onClick(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameText.getText().toString();
                final String password = passwordText.getText().toString();
                String encryptPassword = AesUtils.encrypt(password);
                final int isChecked = remember.isChecked()?1:0;
                String url = UrlConstant.loginURL+"?username="+username+"&password="+password;
                //String url = UrlConstant.loginURL+"?username="+username+"&password="+encryptPassword.replace("+", "%2B");
                VolleyRequest.RequestGet(url, "login", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new GsonBuilder().create();
                        ResultBean loginResult = gson.fromJson(result, ResultBean.class);
                        if(loginResult.getResult() == 0){
                            db = helper.getWritableDatabase();
                            db.beginTransaction();
                            String user_sql = "insert into "+ DbConstant.USER_TABLE + "(username, password, isremember) values ('" + username + "','"+ password +"',"+isChecked+");";
                            try{
                                db.execSQL("delete from "+DbConstant.USER_TABLE);
                                db.execSQL(user_sql);
                                db.setTransactionSuccessful();
                            }finally {
                                db.endTransaction();
                                db.close();
                            }

                            Intent intent = new Intent(LoginActivity.this, mainView.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(loginResult.getResult() == -1){
                            Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();
                        }
                        else if(loginResult.getResult() == -2){
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                        }
                        else if(loginResult.getResult() == -3){
                            Toast.makeText(LoginActivity.this, "系统错误", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(VolleyError result) {
                        Toast.makeText(LoginActivity.this, "网络请求错误", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void jsonOnSuccess(JSONObject result) {

                    }
                });
            }
        });
    }
}
