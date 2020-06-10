package edu.sdust.insapp.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.ViOveParStaffExtendBean;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

/**
 * Created by chenhw on 2017/12/27.
 */

public class UserInformationActivity extends AppCompatActivity {
    @BindView(R.id.user_namereal)//姓名
    TextView usernamereal;
    @BindView(R.id.user_number)//工号
    TextView usernumber;
    @BindView(R.id.user_zc)//职称
    TextView userzc;
    @BindView(R.id.user_phone)//电话
    TextView userphone;
    @BindView(R.id.user_gz)//工种
    TextView usergz;
    @BindView(R.id.user_bz)//班组
    TextView userbz;
    @BindView(R.id.user_bm)//部门
    TextView userbm;
    @BindView(R.id.yhxx_back)//返回
    ImageButton yhxxback;
    @BindView(R.id.bumen_text)
    TextView bumentext;
    @BindView(R.id.user_bu_l)
    RelativeLayout userbu1;
    @BindView(R.id.gz_01)
    RelativeLayout gz01;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private ViOveParStaffExtendBean viOveParStaffExtendBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        //insOrder = isUserInforBean.getInstance();
        helper = DbManager.getInstance(this);
        ButterKnife.bind(this);
        ivew();
        username();
    }

    private void ivew() {
        bumentext.setVisibility(View.GONE);
        userbu1.setVisibility(View.GONE);
    }

    public void username() {
        String find_sql = "select * from " + DbConstant.USER_TABLE;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(find_sql, null);
        final String username, password;
        String url = UrlConstant.userInformationURL;
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex("username"));
            password = cursor.getString(cursor.getColumnIndex("password"));
            db.close();
            url = url + "?username=" + username + "&password=" + password;
            //用户信息
            VolleyRequest.RequestGet(url, "userInfor", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    viOveParStaffExtendBean = new Gson().fromJson(result, ViOveParStaffExtendBean.class);
                       // Log.i("工作",viOveParStaffExtendBean.getJobNameList().toString());
                    //if(viOveParStaffExtendBean.getJobNameList()!=null){
                        //String j  = viOveParStaffExtendBean.getJobNameList().toString();
                        //usergz.setText(j.substring(1, j.length()-1)!= null?j.substring(1, j.length()-1):"没有工种");
                    //}else{
                        usergz.setVisibility(View.GONE);
                        gz01.setVisibility(View.GONE);
                    //}
                    if(viOveParStaffExtendBean.getGroupNameList()!=null){
                        String g  = viOveParStaffExtendBean.getGroupNameList().toString();
                        userbz.setText(g.substring(1, g.length()-1)!= null?g.substring(1, g.length()-1):"没有班组");
                    }else{
                        userbz.setVisibility(View.GONE);
                    }
                    //String s =  viOveParStaffExtendBean.getPostNameList().toString();
                    usernamereal.setText(viOveParStaffExtendBean.getOveparstaffname()!=null?viOveParStaffExtendBean.getOveparstaffname():"没有用户名");
                    userphone.setText(viOveParStaffExtendBean.getOveparstaffmobile()!=null?viOveParStaffExtendBean.getOveparstaffmobile():"没有联系方式");//电话号码
                    usernumber.setText(viOveParStaffExtendBean.getOveparstaffjobnum()!=null?viOveParStaffExtendBean.getOveparstaffjobnum():"无工号");//工号
                    userzc.setText(viOveParStaffExtendBean.getTitlecode()!=null?viOveParStaffExtendBean.getTitlecode():"没有职称");


//                    if(viOveParStaffExtendBean.getDepartName().equals("班组")){
//                        bumentext.setVisibility(View.GONE);
//                        userbu1.setVisibility(View.GONE);
//                    }else {
//                        userbm.setText(viOveParStaffExtendBean.getDepartName() != null ? viOveParStaffExtendBean.getDepartName() : "没有班组");
//                    }
                }
                @Override
                public void onError(VolleyError result) {
                    Toast.makeText(UserInformationActivity.this, "网络请求失败", Toast.LENGTH_LONG).show();
                }

                @Override
                public void jsonOnSuccess(JSONObject result) {

                }

            });
        }
        yhxxback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}
