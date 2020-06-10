package edu.sdust.insapp.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.DeviceProblemBean;
import edu.sdust.insapp.bean.EqptTag;
import edu.sdust.insapp.bean.ProblemListBean;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

/**
 * Created by Administrator on 2018/5/3.
 */

public class ProblemListActivity extends AppCompatActivity{
    @BindView(R.id.problem_list)
    ListView problemlist;

    private OrderDBHelper helper;
    private SQLiteDatabase db;
    private  ProblemListBean problemListBean;
    private List<String> problemList;
    private ArrayAdapter<String> MyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_list);
        ButterKnife.bind(this);
        helper = DbManager.getInstance(this);
        problemList = new ArrayList<>();
        initView();
        problemList();
    }

    private void initView(){
        MyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, problemList);
        problemlist.setAdapter(MyAdapter);
    }
    private void  problemList(){
        String find_sql = "select * from " + DbConstant.USER_TABLE;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(find_sql, null);
        String problemListUrl;
        String username, password;
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex("username"));
            password = cursor.getString(cursor.getColumnIndex("password"));
            problemListUrl = UrlConstant.listEqptFault + "?username=" + username + "&password=" + password;
            VolleyRequest.RequestGet(problemListUrl,"fdv",new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    problemListBean = new Gson().fromJson(result, ProblemListBean.class);
                    List<DeviceProblemBean> proList =problemListBean.getData();
                    for(DeviceProblemBean deviceProblemBean:proList){
                       int ss = deviceProblemBean.getEqptaccntid();
                       List<EqptTag> ss1 = getAllEqpt(ss);
                        EqptTag eqptTag = ss1.get(0);
                       String se =eqptTag.getEqpttag()+ "/" +deviceProblemBean.getEqptproblemdesc();
                        problemList.add(se);
                    }
                    MyAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(VolleyError result) {

                }

                @Override
                public void jsonOnSuccess(JSONObject result) {

                }
            });
        }
        db.close();
    }

    public List<EqptTag> getAllEqpt(Integer eqptId) {
        String sql = "select deviceId ,deviceName ,eqptCatgName ,deviceAbbre from dldevice where deviceId ="+eqptId+";";
        SQLiteDatabase db = helper.getWritableDatabase();
        List<EqptTag> pointList = new ArrayList<EqptTag>();
        EqptTag point = null;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            point = new EqptTag();
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
}
