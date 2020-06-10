package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class ProblemDeviceActivity extends AppCompatActivity {
    @BindView(R.id.tv_problem_position_title)
    TextView titleText;
    @BindView(R.id.listview_problem_position_list)
    ListView deviceList;
    private String deviceString;
    private List<String> deviceListData;
    private int REQUEST_CODE = 0;
    private int SUCCESS_CHOOSE = 2;
    private int ERROR_CHOOSE = 4;
    private ArrayAdapter<String> MyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_position);
        ButterKnife.bind(this);
        titleText.setText("问题所属装置");
        deviceListData = new ArrayList<>();
        getDeviceString();
        initView();
        initEvents();
    }

    private void initView(){
        MyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceListData);
        deviceList.setAdapter(MyAdapter);
        setAdapterData();
    }
    private void initEvents(){
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String item = ((TextView)view).getText().toString();
                if(hasChildren(item)){
                    String childrenString=getChildren(item);
                    deviceString = childrenString;
                    setAdapterData();
                }else{
                    AlertDialog dialog = new AlertDialog.Builder(ProblemDeviceActivity.this)
                            .setMessage("是否选择？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    intent.putExtra("device", item);
                                    setResult(SUCCESS_CHOOSE, intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create();
                    dialog.show();
                }
            }
        });
    }
//是否还含有子节点
    private boolean hasChildren(String item) {
        boolean hasChi = false;
        JsonArray deviceJson = new JsonParser().parse(deviceString).getAsJsonArray();
        for(JsonElement json:deviceJson){
            JsonObject itemJson = json.getAsJsonObject();
            String temp = itemJson.get("id").getAsString()+": "+itemJson.get("label").getAsString();
            if(item.equals(temp)&&itemJson.has("children")){
                hasChi=true;
                break;
            }
        }
        return hasChi;
    }
    //获取子节点的json串
    private String getChildren(String item){
        String childrenString = null;
        JsonArray deviceJson = new JsonParser().parse(deviceString).getAsJsonArray();
        for(JsonElement json:deviceJson){
            JsonObject itemJson = json.getAsJsonObject();
            String temp = itemJson.get("id").getAsString()+": "+itemJson.get("label").getAsString();
            if(item.equals(temp)){
                childrenString = itemJson.get("children").getAsJsonArray().toString();
            }
        }
        return childrenString;
    }

    //获取问题部位
    private void getDeviceString(){
        if(deviceString==null){
            VolleyRequest.RequestGet(UrlConstant.getDeviceTreeURL, "getDeviceTree", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    deviceString=result;
                    setAdapterData();
                }

                @Override
                public void onError(VolleyError result) {
                    Toast.makeText(ProblemDeviceActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void jsonOnSuccess(JSONObject result) {

                }
            });
        }else{

        }
    }
    private void setAdapterData(){
        deviceListData.clear();
        //list列表
        if(deviceString!=null){
            JsonArray deviceJson = new JsonParser().parse(deviceString).getAsJsonArray();
            for(JsonElement json:deviceJson){
                JsonObject itemJson = json.getAsJsonObject();
                String item = itemJson.get("id").getAsString()+": "+itemJson.get("label").getAsString();
                deviceListData.add(item);
            }
            MyAdapter.notifyDataSetChanged();
        }

    }

}
