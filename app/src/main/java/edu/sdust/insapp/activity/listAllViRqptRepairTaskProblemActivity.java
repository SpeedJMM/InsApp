package edu.sdust.insapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

/**
 * Created by Administrator on 2018/6/5.
 */

public class listAllViRqptRepairTaskProblemActivity extends AppCompatActivity {
    @BindView(R.id.all_vi_repair_task_problem_list)
    ListView RepairTaskProblem;

    private List<Map<String, String>> eqptProblemList;
    private SimpleAdapter dd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_vi_repair_task_problem);
        ButterKnife.bind(this);
        eqptProblemList=  new ArrayList<Map<String, String>>();
        ForDevice(getIntent().getExtras());
        initView();
    }
    private void initView(){
        String[] from = {"banzu"};
        int [] to = {R.id.tv_problem_list_item_key};
        dd = new SimpleAdapter(this, eqptProblemList, R.layout.problem_list_child, from, to);
        RepairTaskProblem.setAdapter(dd);
        RepairTaskProblem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ss12 = eqptProblemList.get(i).get("wenti");
                Bundle bundle = new Bundle();
                bundle.putString("eqptrepairdispaorderid",ss12);
                Intent intent = new Intent(listAllViRqptRepairTaskProblemActivity.this,EqptRepairProblemDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private  void ForDevice(Bundle bundle){
        String eqptreId= bundle.getString("eqptrepairdispaorderid");
        String eqptproblem= UrlConstant.Listeqptproblem+"?eqptdispaorder="+eqptreId;
        VolleyRequest.RequestGet(eqptproblem, "eqptcomorder", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                JsonArray positionJson = new JsonParser().parse(result).getAsJsonArray();
                for(JsonElement json:positionJson) {
                    JsonObject itemJson = json.getAsJsonObject();
                    String i11 = itemJson.get("problemphenomenonname").getAsString()+"/"+itemJson.get("eqptproblemdesc").getAsString();
                    String I22 = itemJson.get("eqptrepairdispaorderid").getAsString();
                    getData(i11,I22);
                }
            }

            @Override
            public void onError(VolleyError result) {

            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }
    public List<Map<String, String>> getData(String d, String e){
        Map<String, String> map = new HashMap<String, String>();
        map.put("banzu", d);
        map.put("wenti", e);
        eqptProblemList.add(map);
        dd.notifyDataSetChanged();
        return eqptProblemList;
    }
}
