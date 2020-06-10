package edu.sdust.insapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

/**
 * Created by Administrator on 2018/6/10.
 */

public class NonEqptRepairTaskProblemDetailActivity extends AppCompatActivity{
    @BindView(R.id.non_eqpt_problem_desc)
    TextView nonEptProblemDesc;
    @BindView(R.id.non_eqpt_problem_place)
    TextView nonEqptProblemPlace;
    //@BindView(R.id.fault_treatment_meas_content)
    //TextView faultTreatmentMeasContent;
    @BindView(R.id.non_eqpt_problem_soltime)
    TextView nonEqptProblemSoltime;
    @BindView(R.id.insp_problem_state_name)
    TextView inspProblemStateName;
    @BindView(R.id.ViDispa_Staff)
    TextView ViDispaStaff;
    @BindView(R.id.non_eqpt_problem_find_time)
    TextView nonEqptProblemFindTime;


    private String noneptproblemdesc;
    private String noneqptproblemplace;
    //private String faulttreatmentmeascontent;
    private String noneqptproblemsoltime;
    private String inspproblemstatename;
    private String vidispastaff;
    private String noneqptproblemfindtime;
    private String dispaordersecdispatime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_eqpt_repair_problem_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        dispaordersecdispatime = bundle.getString("dispaordersecdispatime");
        ForNonProblem(getIntent().getExtras());
    }

    private void init(){
        nonEptProblemDesc.setText(noneptproblemdesc);
        nonEqptProblemPlace.setText(noneqptproblemplace);
        //faultTreatmentMeasContent.setText(faulttreatmentmeascontent);
        nonEqptProblemSoltime.setText(noneqptproblemsoltime);
        inspProblemStateName.setText(inspproblemstatename);
        ViDispaStaff.setText(vidispastaff);
        nonEqptProblemFindTime.setText(noneqptproblemfindtime);
    }
    private void ForNonProblem(Bundle bundle) {
        String eqptdis = bundle.getString("noneqptrepairdispaorderid");
        vidispastaff= bundle.getString("disordername");
        noneqptproblemsoltime=bundle.getString("completordersubmittime");
        String eqptproblem = UrlConstant.noneqptproblem+"?eqptdispaorder="+eqptdis;
        VolleyRequest.RequestGet(eqptproblem, "eqptcomorder", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                JsonArray positionJson = new JsonParser().parse(result).getAsJsonArray();
                for (JsonElement json : positionJson) {
                    JsonObject itemJson = json.getAsJsonObject();
                    noneptproblemdesc =itemJson.get("noneqptproblemdesc").getAsString();
                    inspproblemstatename=itemJson.get("inspproblemstatename").getAsString();
                    /*if(itemJson.get("faulttreatmentmeascontent") instanceof  JsonNull){
                        //faulttreatmentmeascontent = itemJson.get("faulttreatmentmeascontent").getAsString();
                        faulttreatmentmeascontent="无";
                    }else {
                        faulttreatmentmeascontent = itemJson.get("faulttreatmentmeascontent").getAsString();
                        //faulttreatmentmeascontent="无";
                    }*/
                    noneqptproblemplace=itemJson.get("noneqptproblemplace").getAsString();
                    noneqptproblemfindtime =itemJson.get("noneqptproblemfindtime").getAsString();
                    /*if(itemJson.get("noneqptproblemsoltime") instanceof JsonNull){
                        noneqptproblemsoltime = "无";
                    }else{
                        noneqptproblemsoltime=itemJson.get("noneqptproblemsoltime").getAsString();
                    }*/

                }
                init();
            }

            @Override
            public void onError(VolleyError result) {

            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }
}
