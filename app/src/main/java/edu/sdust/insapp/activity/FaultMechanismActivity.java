package edu.sdust.insapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.TreeNode;
import edu.sdust.insapp.utils.FirstLevelAdapter;
import edu.sdust.insapp.utils.SecondLevelAdapter;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

public class FaultMechanismActivity extends AppCompatActivity {
    @BindView(R.id.tv_fault_location_title)
    TextView titleText;
    @BindView(R.id.listview_fault_location_firstlevel)
    RecyclerView firstList;
    @BindView(R.id.listview_fault_location_secondlevel)
    RecyclerView secondList;
    @BindView(R.id.btn_fault_location_confirm)
    Button confirm;
    @BindView(R.id.btn_fault_location_cancel)
    Button cancel;
    private final int REQUEST_CODE = 0;
    private final int SUCCESS_CHOOSE = 2;
    private final int ERROR_CHOOSE = 4;
    private String ovepargroupid;

    private List<TreeNode> treeNodeList, nodeList;
    private List<String> selectedDataList;
    private List<List<Boolean>> selectedStatusList;
    private List<Boolean> statusList;
    private FirstLevelAdapter firstAdapter;
    private SecondLevelAdapter secondAdapter;
    private int firstLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_location);
        ButterKnife.bind(this);
        ovepargroupid = getIntent().getStringExtra("ovepargroupid");

        treeNodeList = new ArrayList<>();
        nodeList = new ArrayList<>();
        selectedDataList = new ArrayList<>();
        selectedStatusList = new ArrayList<>();
        statusList = new ArrayList<>();
        ArrayList<String> mechanismDatas = getIntent().getStringArrayListExtra("mechanismDatas");
        selectedDataList.clear();
        selectedDataList.addAll(mechanismDatas);
        getFaultMechanismList();
        initData();
        initView();
        initEvents();
    }

    private void initView() {
        titleText.setText("故障机理");
        firstList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        firstAdapter = new FirstLevelAdapter(treeNodeList);
        firstList.setAdapter(firstAdapter);
        secondList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (treeNodeList.size() != 0) {
            nodeList.addAll(treeNodeList.get(0).getChildren());
            statusList.addAll(selectedStatusList.get(firstLevel));
        }
        secondAdapter = new SecondLevelAdapter(nodeList, statusList);
        secondList.setAdapter(secondAdapter);
    }

    private void initEvents() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDataList.clear();
                for (int i = 0; i < selectedStatusList.size(); i++) {
                    List<Boolean> booleans = selectedStatusList.get(i);
                    for (int n = 0; n < booleans.size(); n++) {
                        if (booleans.get(n)) {
                            selectedDataList.add(treeNodeList.get(i).getChildren().get(n).toString());
                        }
                    }
                }
                Intent intent = new Intent();
                ArrayList<String> selectedDatas = new ArrayList<>();
                selectedDatas.addAll(selectedDataList);
                intent.putStringArrayListExtra("mechanism", selectedDatas);
                setResult(SUCCESS_CHOOSE, intent);
                finish();
//                AlertDialog.Builder builder = new AlertDialog.Builder(FaultMechanismActivity.this)
//                        .setMessage("是否选择？")
//                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent();
//                                ArrayList<String> selectedDatas = new ArrayList<>();
//                                selectedDatas.addAll(selectedDataList);
//                                intent.putStringArrayListExtra("mechanism", selectedDatas);
//                                setResult(SUCCESS_CHOOSE, intent);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                builder.show();
            }
        });

        firstAdapter.setOnItemClickListener(new FirstLevelAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                firstLevel = position;
                nodeList.clear();
                statusList.clear();
                nodeList.addAll(treeNodeList.get(position).getChildren());
                statusList.addAll(selectedStatusList.get(position));
                secondAdapter.notifyDataSetChanged();
                firstAdapter.setSelectedPosition(position);
            }
        });

        secondAdapter.setOnItemClickListener(new SecondLevelAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                List<Boolean> booleanList = selectedStatusList.get(firstLevel);
                if (secondAdapter.isCheckboxIsChecked()) {
                    booleanList.set(position, false);
                    selectedStatusList.set(firstLevel, booleanList);
                } else {
                    booleanList.set(position, true);
                    selectedStatusList.set(firstLevel, booleanList);
                }
            }
        });
    }

    private void initData() {
        if (treeNodeList.size() != 0) {
            selectedStatusList.clear();
            for (TreeNode treeNode : treeNodeList) {
                List<TreeNode> nodeList = treeNode.getChildren();
                List<Boolean> booleanList = new ArrayList<>();
                for (TreeNode node : nodeList) {
                    Boolean b = false;
                    if (selectedDataList.size() > 0) {
                        for (String str : selectedDataList) {
                            if (node.toString().equals(str)) {
                                b = true;
                                break;
                            }
                        }
                    }
                    booleanList.add(b);
                }
                selectedStatusList.add(booleanList);
            }
        }
    }

    //获取故障机理数据
    private void getFaultMechanismList() {
        if (treeNodeList.size() == 0) {
            String url = UrlConstant.getFaultmechanismTreeAppURL + "?ovepargroupid=" + ovepargroupid;
            VolleyRequest.RequestGet(url, "getFaultmechanismTreeApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                @Override
                public void onSuccess(String result) {
                    List<TreeNode> treeNodes = new Gson().fromJson(result, new TypeToken<List<TreeNode>>(){}.getType());
                    treeNodeList.clear();
                    treeNodeList.addAll(treeNodes.get(0).getChildren());
                    nodeList.addAll(treeNodeList.get(0).getChildren());
                    initData();
                    statusList.addAll(selectedStatusList.get(firstLevel));
                    firstAdapter.notifyDataSetChanged();
                    secondAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(VolleyError result) {
                    Toast.makeText(getApplicationContext(),"数据获取失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void jsonOnSuccess(JSONObject result) {

                }
            });
        }
    }

}
