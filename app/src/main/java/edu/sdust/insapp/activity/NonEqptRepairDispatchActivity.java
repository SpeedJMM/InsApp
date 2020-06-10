package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.TreeNode;
import edu.sdust.insapp.bean.ViNonEqptProblemBean;
import edu.sdust.insapp.bean.ViNonEqptRepairDisOrderForDisBean;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

import static edu.sdust.insapp.activity.AllComEqptRepairDisOrderActivity.WAITING_SECONDARY_DISPATCH;

public class NonEqptRepairDispatchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView team, problem;
    private EditText remark;
    private SimpleDateFormat uploadStandard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<String> problemList;
    private List<Integer> problemIdList;
    private int selectedArray = -1;
    private List<TreeNode> teamList;
    private TeamAdapter teamAdapter;
    private AlertDialog teamDialog;
    private static final int REFRESH_CODE = 8;

    private void initControllers() {
        //保存
        fab = findViewById(R.id.fab_nerd);
        //派工班组
        team = findViewById(R.id.tv_cnerd_team);
        //设备问题
        problem = findViewById(R.id.tv_cnerd_problem);
        //派工单备注
        remark = findViewById(R.id.et_cnerd_remark);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_eqpt_repair_dispatch);
        toolbar = findViewById(R.id.toolbar_nerd);
        setSupportActionBar(toolbar);

        initControllers();
        initData();
        initEvents();
    }

    private void initEvents() {
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dialog_recycleview, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(NonEqptRepairDispatchActivity.this);
                builder.setTitle("派工班组");
                builder.setView(v);
                RecyclerView recyclerView = v.findViewById(R.id.rv_item_dialog);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                teamAdapter = new TeamAdapter(teamList);
                recyclerView.setAdapter(teamAdapter);
                teamDialog = builder.create();
                teamDialog.show();
            }
        });

        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String[] problemArray = problemList.toArray(new String[problemList.size()]);
                if (problemArray == null || problemArray.length == 0) {
                    Snackbar.make(view, "无设备问题", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(NonEqptRepairDispatchActivity.this)
                        .setTitle("设备问题");
                builder.setSingleChoiceItems(
                        problemArray,
                        selectedArray,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedArray = i;
                            }
                        }
                )
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (selectedArray != -1) {
                                    problem.setText(problemArray[selectedArray]);
                                    problem.setTag(problemIdList.get(selectedArray));
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (team.getText().equals("选择派工班组")) {
                    Snackbar.make(view, "未选择派工班组", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (problem.getText().equals("选择非设备问题")) {
                    Snackbar.make(view, "未选择非设备问题", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                addViNonEqptRepairDisOrderForDisJson viNonEqptRepairDisOrderForDisJson = new addViNonEqptRepairDisOrderForDisJson();
                viNonEqptRepairDisOrderForDisJson.setNoneqptproblemid(Integer.valueOf(problem.getTag().toString()));
                viNonEqptRepairDisOrderForDisJson.setOvepargroupid(Integer.valueOf(team.getTag().toString()));
                viNonEqptRepairDisOrderForDisJson.setDispaordercontroldispatime(uploadStandard.format(new Date()));
                viNonEqptRepairDisOrderForDisJson.setDispaorderstatecode(WAITING_SECONDARY_DISPATCH);
                viNonEqptRepairDisOrderForDisJson.setDispaorderremark(remark.getText().toString());
                List<String> repairauxiliarytaskdiccodeList = new ArrayList<>();
                viNonEqptRepairDisOrderForDisJson.setRepairauxiliarytaskdiccodeList(repairauxiliarytaskdiccodeList);
                Gson gson = new Gson();
                Map<String, String> params = new HashMap<>();
                params.put("params", gson.toJson(viNonEqptRepairDisOrderForDisJson));
                String url = UrlConstant.addViNonEqptRepairDisOrderAppURL;
                VolleyRequest.RequestPost(url, "addViNonEqptRepairDisOrderApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                        int resultCode = jsonObject.get("result").getAsInt();
                        if (resultCode == 0) {
                            Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            setResult(REFRESH_CODE);
                            finish();
                        }
                    }

                    @Override
                    public void onError(VolleyError result) {
                        Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void jsonOnSuccess(JSONObject result) {

                    }
                });
            }
        });
    }

    private void initData() {
        problemList = new ArrayList<>();
        problemIdList = new ArrayList<>();
        teamList = new ArrayList<>();
        teamAdapter = new TeamAdapter(teamList);

        getProblemData();
        //getTeamData();
    }

    private void getTeamData() {
        String url = UrlConstant.getOverParGroupTreeUrl;
        VolleyRequest.RequestGet(url, "getOverParGroupTree", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<TreeNode> treeNodes = new Gson().fromJson(result, new TypeToken<List<TreeNode>>(){}.getType());
                teamList.addAll(treeNodes.get(0).getChildren());
                teamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    private void getProblemData() {
        String url = UrlConstant.listAllViNonEqptProblemWiOuPageURL;
        VolleyRequest.RequestGet(url, "listAllViNonEqptProblemWiOuPage", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                List<ViNonEqptProblemBean> viNonEqptProblemBeans = new Gson().fromJson(result, new TypeToken<List<ViNonEqptProblemBean>>(){}.getType());
                for (ViNonEqptProblemBean v : viNonEqptProblemBeans) {
                    //problemList.add(v.getDeviceabbre() == null ? "空" + "/" + v.getProblemphenomenonname() : v.getDeviceabbre() + "/" + v.getProblemphenomenonname());
                    problemList.add(v.getNoneqptproblemabs());
                    problemIdList.add(v.getNoneqptproblemid());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_child, problemList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                getTeamData();
            }

            @Override
            public void onError(VolleyError result) {
                Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    private class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<TreeNode> mdataList;

        public TeamAdapter(List<TreeNode> mdataList) {
            this.mdataList = mdataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            View teamView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                teamView = itemView;
                textView = itemView.findViewById(R.id.tv_first_level_data);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_team, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            String label = mdataList.get(position).getLabel();
            viewHolder.textView.setText(label);
            viewHolder.teamView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mdataList.get(position).getChildren() != null) {
                        mdataList = mdataList.get(position).getChildren();
                        notifyDataSetChanged();
                    } else {
                        team.setText(mdataList.get(position).getLabel());
                        team.setTag(mdataList.get(position).getValue());
                        teamDialog.dismiss();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mdataList.size();
        }
    }

    private class addViNonEqptRepairDisOrderForDisJson extends ViNonEqptRepairDisOrderForDisBean {
        private Integer noneqpthiddendangerid;
        private String monthlyeqptrepairtaskidAndtaskid;
        private Integer noneqptproblemid;
        private List<String> repairauxiliarytaskdiccodeList;

        public Integer getNoneqpthiddendangerid() {
            return noneqpthiddendangerid;
        }

        public void setNoneqpthiddendangerid(Integer noneqpthiddendangerid) {
            this.noneqpthiddendangerid = noneqpthiddendangerid;
        }

        public String getMonthlyeqptrepairtaskidAndtaskid() {
            return monthlyeqptrepairtaskidAndtaskid;
        }

        public void setMonthlyeqptrepairtaskidAndtaskid(String monthlyeqptrepairtaskidAndtaskid) {
            this.monthlyeqptrepairtaskidAndtaskid = monthlyeqptrepairtaskidAndtaskid;
        }

        public Integer getNoneqptproblemid() {
            return noneqptproblemid;
        }

        public void setNoneqptproblemid(Integer noneqptproblemid) {
            this.noneqptproblemid = noneqptproblemid;
        }

        public List<String> getRepairauxiliarytaskdiccodeList() {
            return repairauxiliarytaskdiccodeList;
        }

        public void setRepairauxiliarytaskdiccodeList(List<String> repairauxiliarytaskdiccodeList) {
            this.repairauxiliarytaskdiccodeList = repairauxiliarytaskdiccodeList;
        }
    }

}
