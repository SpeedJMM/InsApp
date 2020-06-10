package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class ProblemPositionActivity extends AppCompatActivity {
    @BindView(R.id.tv_problem_position_title)
    TextView titleText;
    @BindView(R.id.listview_problem_position_list)
    ListView positionList;
    private String positionString;
    private List<String> positionListData;
    private int REQUEST_CODE = 0;
    private int SUCCESS_CHOOSE = 2;
    private int ERROR_CHOOSE = 4;
    private ArrayAdapter<String> MyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_position);
        ButterKnife.bind(this);
        positionListData = new ArrayList<>();
        getPositionString();
        initView();
        initEvents();
    }

    private void initView(){
        MyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, positionListData);
        positionList.setAdapter(MyAdapter);
        setAdapterData();
    }
    private void initEvents(){
        positionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String item = ((TextView)view).getText().toString();
                if(hasChildren(item)){
                    String childrenString=getChildren(item);
                    positionString = childrenString;
                    setAdapterData();
                }else{
                    AlertDialog dialog = new AlertDialog.Builder(ProblemPositionActivity.this)
                            .setMessage("是否选择？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    ProblemPositionManager.getInstance().add(item.split(":")[0]);
                                    Intent intent = new Intent();
//                                    intent.putExtra("SUCCESS_CHOOSE", SUCCESS_CHOOSE);
                                    intent.putExtra("position", item);
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
        JsonArray positionJson = new JsonParser().parse(positionString).getAsJsonArray();
        for(JsonElement json:positionJson){
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
        JsonArray positionJson = new JsonParser().parse(positionString).getAsJsonArray();
        for(JsonElement json:positionJson){
            JsonObject itemJson = json.getAsJsonObject();
            String temp = itemJson.get("id").getAsString()+": "+itemJson.get("label").getAsString();
            if(item.equals(temp)){
                childrenString = itemJson.get("children").getAsJsonArray().toString();
            }
        }
        return childrenString;
    }

    //获取问题部位
    private void getPositionString(){
        if(positionString==null){
            VolleyRequest.RequestGet(UrlConstant.eqptfaultpositionURl, "getProblemPosition", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    positionString=result;
                    setAdapterData();
                }

                @Override
                public void onError(VolleyError result) {
                    Toast.makeText(ProblemPositionActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void jsonOnSuccess(JSONObject result) {

                }
            });
        }else{

        }
    }
    private void setAdapterData(){
        positionListData.clear();
        //list列表
        if(positionString!=null){
            JsonArray positionJson = new JsonParser().parse(positionString).getAsJsonArray();
            for(JsonElement json:positionJson){
                JsonObject itemJson = json.getAsJsonObject();
                String item = itemJson.get("id").getAsString()+": "+itemJson.get("label").getAsString();
                positionListData.add(item);
            }
            MyAdapter.notifyDataSetChanged();
        }

    }

}
