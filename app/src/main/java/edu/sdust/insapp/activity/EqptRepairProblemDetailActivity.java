package edu.sdust.insapp.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
 * Created by Administrator on 2018/6/7.
 */

public class EqptRepairProblemDetailActivity extends AppCompatActivity {
    @BindView(R.id.eqpt_tepair_task)
    TextView eqptTepairTask;
    @BindView(R.id.eqpt_tepair_task_des)
    TextView eqptTepairTaskDes;
    @BindView(R.id.ovepargroupname)
    TextView oveparGroupName;
    @BindView(R.id.dispaordercontroldispatime)
    TextView dispaOrderControldispaTime;
    @BindView(R.id.completordersubmittime)
    TextView completOrderSubmitTime;
    @BindView(R.id.problemphenomenonname)
    TextView problemPhenomenonName;
    @BindView(R.id.inspproblemscenetreatmentmodeentry)
    TextView inspProblemsCenetreatmentmodeentry;
    @BindView(R.id.faultcausecontent)
    TextView faultCauseContent;
    @BindView(R.id.faultmechanismcontent)
    TextView faultMechanismContent;
    @BindView(R.id.faulttreatmentmeascontent)
    TextView faultTreatMentMeasContent;
    @BindView(R.id.inspproblemstatename)
    TextView inspProblemStateName;
    @BindView(R.id.vi_dispa_staff)
    TextView viDispaStaff;


    private String eqpttepairtask;
    private String eqpttepairtaskdes;
    private String ovepargroupname;
    private String dispaordercontroldispatime;
    private String completordersubmittime;
    private String problemphenomenonname;
    private String inspproblemscenetreatmentmodeentry;
    private String faultcausecontent;
    private String faultmechanismcontent;
    private String faulttreatmentmeascontent;
    private String inspproblemstatename;
    private String vidispastaff;
    private String dispaordersecdispatime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eqpt_repair_problem_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        dispaordersecdispatime = bundle.getString("dispaordersecdispatime");
        completordersubmittime = bundle.getString("completordersubmittime");

        ForProblem(getIntent().getExtras());
        initView();

    }

    private void initView() {
        inspProblemsCenetreatmentmodeentry.setVisibility(View.GONE);
        eqptTepairTask.setText(eqpttepairtask);
        viDispaStaff.setText(vidispastaff);
        eqptTepairTaskDes.setText(eqpttepairtaskdes);
        oveparGroupName.setText(ovepargroupname);
        dispaOrderControldispaTime.setText(dispaordersecdispatime);
        completOrderSubmitTime.setText(completordersubmittime);
        problemPhenomenonName.setText(problemphenomenonname);
        inspProblemsCenetreatmentmodeentry.setText(inspproblemscenetreatmentmodeentry);
        faultCauseContent.setText(faultcausecontent);
        faultMechanismContent.setText(faultmechanismcontent);
        faultTreatMentMeasContent.setText(faulttreatmentmeascontent);
        inspProblemStateName.setText(inspproblemstatename);
    }

    private void ForProblem(Bundle bundle) {
        String eqptdis = bundle.getString("eqptrepairdispaorderid");
        String eqptproblem = UrlConstant.Listeqptproblem + "?eqptdispaorder=" + eqptdis;
        idp(eqptdis);
        VolleyRequest.RequestGet(eqptproblem, "eqptcomorder", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                JsonArray positionJson = new JsonParser().parse(result).getAsJsonArray();
                for (JsonElement json : positionJson) {
                    JsonObject itemJson = json.getAsJsonObject();
                    faultcausecontent = itemJson.get("faultcausecontent").getAsString();
                    faulttreatmentmeascontent = itemJson.get("faulttreatmentmeascontent").getAsString();
                    eqpttepairtaskdes = itemJson.get("eqptproblemdesc").getAsString();
                    problemphenomenonname = itemJson.get("problemphenomenonname").getAsString();
                    //completordersubmittime = itemJson.get("eqptrepairtaskendtime").getAsString();
                    inspproblemstatename = itemJson.get("inspproblemstatename").getAsString();
                    eqpttepairtask = itemJson.get("attribvalue").getAsString() + "/" + itemJson.get("deviceabbre").getAsString();
                    //dispaordercontroldispatime = itemJson.get("eqptproblemfindtime").getAsString();
                    faultmechanismcontent = itemJson.get("faultmechanismcontent").getAsString();
                }
                initView();
            }


            @Override
            public void onError(VolleyError result) {

            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    public void idp(String ipsd) {
        String problemUrl = UrlConstant.eqptproblem + "?eqptdispaorderId=" + ipsd;
        VolleyRequest.RequestGet(problemUrl, "eqptcomorder1", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                JsonArray positionJson = new JsonParser().parse(result).getAsJsonArray();
                for (JsonElement json : positionJson) {
                    JsonObject itemJson = json.getAsJsonObject();
                    ovepargroupname = itemJson.get("ovepargroupname").getAsString();
                    vidispastaff = itemJson.get("ViDispaStaff").getAsString();
                }
                initView();
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