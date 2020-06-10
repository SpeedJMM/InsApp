package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.netease.imageSelector.ImageSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.NonEqptRepairTaskProblemBean;
import edu.sdust.insapp.bean.ViNonEqptRepairTaskMonTaskTotalBean;
import edu.sdust.insapp.utils.CustomImagePickRecyclerAdapter;
import edu.sdust.insapp.utils.ImageUtils;
import edu.sdust.insapp.utils.PostUploadRequest;
import edu.sdust.insapp.utils.TicketAdapter;
import edu.sdust.insapp.utils.UrlConstant;
import edu.sdust.insapp.utils.VolleyInterface;
import edu.sdust.insapp.utils.VolleyRequest;

import static com.netease.imageSelector.ImageSelectorConstant.OUTPUT_LIST;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_IMAGE;
import static com.netease.imageSelector.ImageSelectorConstant.REQUEST_PREVIEW;

public class NonEqptRepairDisOrderActivity extends AppCompatActivity implements TicketAdapter.TicketAdapterListener {
    private GridView normal, special;
    private TicketAdapter normalAdapter, specialAdapter;
    private ArrayList<String> normalPaths, specialPaths;
    private int imageWallId = 1;
    public final static String CAMERA_PATH = Environment.getExternalStorageDirectory().getPath() + "/InsPhotoCache";//拍照路径
    private String eqptrepairdispaorderid, dispaorderid, dispaorderstatecode, completorderid, ovepargroupid, whetherworkticket, noneqptrepairdispaorderid, dispaordersecdispatime;
    private Button specialBtn, normalBtn,  comSec, upload;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<String> dataList;
    private List<NonEqptRepairTaskProblemBean> noneqptRepairTaskProblemBeans;
    private boolean isUpload = false;
    private LinearLayout layout;//上传作业票布局
    private TextView status;//上传状态
    private String statusString;
    public final static String FINISHED = "PGDZT04";
    private RecyclerView normal_neo, special_neo, annex;
    private CustomImagePickRecyclerAdapter normalAdapter_neo, specialAdapter_neo, annexAdapter;
    private static final int REFRESH_CODE = 7;
    private int taskType = 0;//1问题任务 2隐患任务 3月度任务
    private List<ViNonEqptRepairTaskMonTaskTotalBean> viNonEqptRepairTaskMonTaskTotalBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_eqpt_repair_dis_order);

        Bundle bundle = getIntent().getExtras();
        noneqptrepairdispaorderid = bundle.getString("noneqptrepairdispaorderid");
        dispaorderid = bundle.getString("dispaorderid");
        dispaorderstatecode = bundle.getString("dispaorderstatecode");
        completorderid = bundle.getString("completorderid");
        ovepargroupid = bundle.getString("ovepargroupid");
        dispaordersecdispatime = bundle.getString("dispaordersecdispatime");
        //whetherworkticket = bundle.getString("whetherworkticket");
        initControllers();
        initImageWall();
        dataList = new ArrayList<>();
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        orderAdapter = new OrderAdapter(dataList);
        recyclerView.setAdapter(orderAdapter);

        normalAdapter_neo = new CustomImagePickRecyclerAdapter(NonEqptRepairDisOrderActivity.this, new ArrayList<String>(), 5, ImagePickType.MULTI);
        normal_neo.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
        normal_neo.setAdapter(normalAdapter_neo);
        specialAdapter_neo = new CustomImagePickRecyclerAdapter(NonEqptRepairDisOrderActivity.this, new ArrayList<String>(), 6, ImagePickType.MULTI);
        special_neo.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
        special_neo.setAdapter(specialAdapter_neo);
        annexAdapter = new CustomImagePickRecyclerAdapter(NonEqptRepairDisOrderActivity.this, new ArrayList<String>(), 7, ImagePickType.MULTI);
        annex.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
        annex.setAdapter(annexAdapter);

        //isWorkTicket();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = UrlConstant.uploadRepairDisOrderAnnexAppURL;
                LinkedList<String[]> files = new LinkedList<>();
                List<String> paths = new ArrayList<>();
                paths.addAll(annexAdapter.getPhotoStringPathList());
                int i = 0;
                for (String path : paths) {
                    String[] t = path.split("\\.");
                    path = path.replace("file:///", "/");
                    i++;
                    files.add(new String[]{path, String.valueOf(i)});
                }
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("dispaorderid", dispaorderid);
                MyApplication.getHttpQueue().add(new PostUploadRequest(url, files, stringMap, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        int resultCode = 0;
                        try {
                            resultCode = jsonObject.getInt("result");
                            if (resultCode == 0) {
                                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        });

        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = UrlConstant.uploadEqptRepairWorkTicketAppURL;
                LinkedList<String[]> files = new LinkedList<>();
                String filetypes = new String();
                normalPaths.clear();
                normalPaths.addAll(normalAdapter_neo.getPhotoStringPathList());
                int i = 0;
                for (String path : normalPaths) {
                    String[] t = path.split("\\.");
                    if (i > 0){
                        filetypes = filetypes + "|" + t[t.length-1];
                    } else {
                        filetypes = t[t.length-1];
                    }
                    path = path.replace("file:///", "/");//去掉‘file://’
                    i++;
                    files.add(new String[]{path, String.valueOf(i)});
                }
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("filetypes", String.valueOf(filetypes));
                stringMap.put("eqptrepairdispaorderid", eqptrepairdispaorderid);
                stringMap.put("worktickettype", "YBZYP");
                MyApplication.getHttpQueue().add(new PostUploadRequest(url, files, stringMap
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int resultCode = 0;
                        try {
                            resultCode = response.getInt("result");
                            if (resultCode == 0) {
                                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                isWorkTicket();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        });

        specialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = UrlConstant.uploadEqptRepairWorkTicketAppURL;
                LinkedList<String[]> files = new LinkedList<>();
                String filetypes = new String();
                specialPaths.clear();
                specialPaths.addAll(specialAdapter_neo.getPhotoStringPathList());
                int i = 0;
                for (String path : specialPaths) {
                    String[] t = path.split("\\.");
                    if (i > 0){
                        filetypes = filetypes + "|" + t[t.length-1];
                    } else {
                        filetypes = t[t.length-1];
                    }
                    path = path.replace("file:///", "/");//去掉‘file://’
                    i++;
                    files.add(new String[]{path, String.valueOf(i)});
                }
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("filetypes", String.valueOf(filetypes));
                stringMap.put("eqptrepairdispaorderid", eqptrepairdispaorderid);
                stringMap.put("worktickettype", "TSZYP");
                MyApplication.getHttpQueue().add(new PostUploadRequest(url, files, stringMap
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int resultCode = 0;
                        try {
                            resultCode = response.getInt("result");
                            if (resultCode == 0) {
                                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                isWorkTicket();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        });

        comSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fillinUrl = UrlConstant.weatherFillinNonEqptProblemTaskAppURL + "?noneqptrepairdispaorderid=" + noneqptrepairdispaorderid;
                VolleyRequest.RequestGet(fillinUrl, "weatherFillinNonEqptProblemTaskApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                    @Override
                    public void onSuccess(String result) {
                        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                        int resultCode = jsonObject.get("result").getAsInt();
                        if (resultCode == 1) {
                            Toast.makeText(getApplicationContext(), "请先完善工单任务信息", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (resultCode == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NonEqptRepairDisOrderActivity.this);
                            builder.setMessage("填写完工单信息");
                            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dialog_edittext, null);
                            builder.setView(v);
                            final EditText remark = v.findViewById(R.id.et_item_dialog);
                            builder.setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String url = UrlConstant.comSecDisOrderAppURL;
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String completordersubmittime = sdf.format(new Date());
                                    String completorderremark = remark.getText().toString() + "#App";
                                    ComJson comJson = new ComJson();
                                    comJson.setDispaorderid(Integer.valueOf(dispaorderid));
                                    comJson.setNoneqptrepairdispaorderid(Integer.valueOf(noneqptrepairdispaorderid));
                                    comJson.setDispaorderstatecode(FINISHED);
                                    comJson.setCompletorderid(Integer.valueOf(completorderid));
                                    comJson.setCompletordersubmittime(completordersubmittime);
                                    if (completorderremark != null && !completorderremark.equals("")) {
                                        comJson.setCompletorderremark(completorderremark);
                                    }
                                    Gson gson = new Gson();
                                    Map<String, String> params = new HashMap<>();
                                    params.put("params", gson.toJson(comJson));
                                    VolleyRequest.RequestPost(url, "comSecDisOrderApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
                                        @Override
                                        public void onSuccess(String result) {
                                            int resultInt = Integer.valueOf(result);
                                            String toastString = new String();
                                            switch (resultInt) {
                                                case 1:
                                                    toastString = "该派工单已完工";
                                                    setResult(REFRESH_CODE);
                                                    break;
                                                case 2:
                                                    toastString = "请填写任务完成情况后完工";
                                                    break;
                                                case 3:
                                                    toastString = "请填写设备问题任务的故障部位，之后才可完工";
                                                    break;
                                                default :
                                                    break;
                                            }
                                            Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError(VolleyError result) {

                                        }

                                        @Override
                                        public void jsonOnSuccess(JSONObject result) {

                                        }
                                    });
                                }
                            });
                            builder.show();
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

    private class ComJson {
        private Integer dispaorderid;
        private Integer noneqptrepairdispaorderid;
        private Integer eqptrepairdispaorderid;
        private String dispaorderstatecode;
        private Integer completorderid;
        private String completordersubmittime;
        private String completorderremark;

        public Integer getDispaorderid() {
            return dispaorderid;
        }

        public void setDispaorderid(Integer dispaorderid) {
            this.dispaorderid = dispaorderid;
        }

        public Integer getNoneqptrepairdispaorderid() {
            return noneqptrepairdispaorderid;
        }

        public void setNoneqptrepairdispaorderid(Integer noneqptrepairdispaorderid) {
            this.noneqptrepairdispaorderid = noneqptrepairdispaorderid;
        }

        public Integer getEqptrepairdispaorderid() {
            return eqptrepairdispaorderid;
        }

        public void setEqptrepairdispaorderid(Integer eqptrepairdispaorderid) {
            this.eqptrepairdispaorderid = eqptrepairdispaorderid;
        }

        public String getDispaorderstatecode() {
            return dispaorderstatecode;
        }

        public void setDispaorderstatecode(String dispaorderstatecode) {
            this.dispaorderstatecode = dispaorderstatecode;
        }

        public Integer getCompletorderid() {
            return completorderid;
        }

        public void setCompletorderid(Integer completorderid) {
            this.completorderid = completorderid;
        }

        public String getCompletordersubmittime() {
            return completordersubmittime;
        }

        public void setCompletordersubmittime(String completordersubmittime) {
            this.completordersubmittime = completordersubmittime;
        }

        public String getCompletorderremark() {
            return completorderremark;
        }

        public void setCompletorderremark(String completorderremark) {
            this.completorderremark = completorderremark;
        }
    }

    private void isWorkTicket() {
        if (whetherworkticket.equals("否")) {
            isUpload = true;
            statusString = "无需作业票";
            hiddenLayout();
        } else {
            isUploaded();
        }
    }

    private void hiddenLayout() {
        if (isUpload) {
            layout.setVisibility(View.GONE);
            status.setBackgroundResource(R.color.color_Cgreen);
            if (statusString != null && !statusString.equals("")) {
                status.setText(statusString);
            }
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            status.setBackgroundResource(R.color.color_Gblue);
            if (statusString != null && !statusString.equals("")) {
                status.setText(statusString);
            }
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void isUploaded() {
        String url = UrlConstant.isUploadedEqptRepairWorkTicketAppURL + "?eqptrepairdispaorderid=" + eqptrepairdispaorderid;
        VolleyRequest.RequestGet(url, "isUploadedEqptRepairWorkTicketApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
                int resultCode = jsonObject.get("result").getAsInt();

                if (resultCode == 0) {
                    isUpload = false;
                    statusString = "未上传作业票";
                } else if (resultCode == 1) {
                    isUpload = true;
                    statusString = "已上传作业票";
                    //String resultData = jsonObject.get("data").getAsString();
                }
                hiddenLayout();
            }

            @Override
            public void onError(VolleyError result) {

            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initData();
    }

    private void initData() {
        ChangeTaskJson json = new ChangeTaskJson();
        json.setNoneqptrepairdispaorderid(Integer.valueOf(noneqptrepairdispaorderid));
        Map<String, String> params = new HashMap<>();
        params.put("params", new Gson().toJson(json));
        String url = UrlConstant.getNonChangeTaskAppURL;
        VolleyRequest.RequestPost(url, "getNonChangeTaskApp", params, new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                taskType = Integer.valueOf(result);
                if (taskType == 1) {
                    getNonEqptProblemTask();
                } else if (taskType == 3) {
                    getNonEqptMonthlyTask();
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

    public class ChangeTaskJson {
        private Integer noneqptrepairdispaorderid;

        public Integer getnoneqptrepairdispaorderid() {
            return noneqptrepairdispaorderid;
        }
        public void setNoneqptrepairdispaorderid(Integer noneqptrepairdispaorderid) {
            this.noneqptrepairdispaorderid = noneqptrepairdispaorderid;
        }
    }

    private void getNonEqptMonthlyTask() {
        dataList.clear();
        String url = UrlConstant.listAllViNonEqptRepairTaskMonTaskAppURL + "?noneqptrepairdispaorderid=" + noneqptrepairdispaorderid;
        VolleyRequest.RequestGet(url, "listAllViNonEqptRepairTaskMonTaskApp", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                viNonEqptRepairTaskMonTaskTotalBeans = new Gson().fromJson(result, new TypeToken<List<ViNonEqptRepairTaskMonTaskTotalBean>>(){}.getType());
                for (int i = 0; i < viNonEqptRepairTaskMonTaskTotalBeans.size(); i++) {
                    dataList.add(viNonEqptRepairTaskMonTaskTotalBeans.get(i).getMonthlynoneqptrepairtaskabs());
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError result) {

            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    private void getNonEqptProblemTask() {
        dataList.clear();
        String listnoneqptproblem = UrlConstant.noneqptproblem + "?eqptdispaorder=" + noneqptrepairdispaorderid;
        VolleyRequest.RequestGet(listnoneqptproblem, "listnoneqptproblem", new VolleyInterface(VolleyInterface.mListener, VolleyInterface.errorListener, VolleyInterface.jsonListener) {
            @Override
            public void onSuccess(String result) {
                noneqptRepairTaskProblemBeans = new Gson().fromJson(result, new TypeToken<List<NonEqptRepairTaskProblemBean>>(){}.getType());
                //for (EqptRepairTaskProblemBean eqptRepairTaskProblemBean : eqptRepairTaskProblemBeans) { }
                for (int i = 0; i < noneqptRepairTaskProblemBeans.size(); i++) {
                    //"设备问题任务" + (i+1) + "：" +
                    dataList.add(noneqptRepairTaskProblemBeans.get(i).getNoneqptproblemdesc());
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError result) {

            }

            @Override
            public void jsonOnSuccess(JSONObject result) {

            }
        });
    }

    private class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> mdataList;

        public OrderAdapter(List<String> mdataList) {
            this.mdataList = mdataList;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            View orderView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                orderView = itemView;
                textView = itemView.findViewById(R.id.tv_item_order);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            String title = mdataList.get(position);
            viewHolder.textView.setText(title);
            viewHolder.orderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (taskType) {
                        case 1:
                            Intent intent = new Intent(getApplicationContext(), NonEqptProblemTaskActivity.class);
                            intent.putExtra("ovepargroupid", ovepargroupid);
                            intent.putExtra("dispaordersecdispatime", dispaordersecdispatime);
                            intent.putExtra("dispaorderid", dispaorderid);
                            intent.putExtra("noneqptRepairTaskProblemBean", noneqptRepairTaskProblemBeans.get(position));
                            startActivity(intent);
                            break;
                        case 3:
                            Intent intentMonthly = new Intent(getApplicationContext(), NonEqptMonthlyTaskActivity.class);
                            intentMonthly.putExtra("ovepargroupid", ovepargroupid);
                            intentMonthly.putExtra("dispaordersecdispatime", dispaordersecdispatime);
                            intentMonthly.putExtra("dispaorderid", dispaorderid);
                            intentMonthly.putExtra("viNonEqptRepairTaskMonTaskTotalBean", viNonEqptRepairTaskMonTaskTotalBeans.get(position));
                            startActivity(intentMonthly);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mdataList.size();
        }
    }

    /**
     * 照片处理
     */
    public List<String> saveBitmap(ArrayList<String> files) throws FileNotFoundException {
        File destdir = new File(CAMERA_PATH+"/temp");
        if (!destdir.exists()){
            destdir.mkdirs();
        }
        OutputStream out;
        List<String> loadfiles  = new ArrayList<>();
        for (int i=0; i<files.size(); i++){
            File file = new File(files.get(i));
            out = new FileOutputStream(CAMERA_PATH+"/temp/"+file.getName());
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(files.get(i),480,800);//按比例缩放
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);//压缩比
            loadfiles.add(CAMERA_PATH+"/temp/"+file.getName());
        }
        return loadfiles;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        //接收图片选择器返回结果，更新所选图片集合
        if (requestCode == REQUEST_PREVIEW || requestCode == REQUEST_IMAGE) {
            ArrayList<String> newFiles = data.getStringArrayListExtra(OUTPUT_LIST);
            if (newFiles != null) {
                if (imageWallId == 1) {
                    try {
                        updateNormalTicket(saveBitmap(newFiles));
                    } catch (FileNotFoundException e) {
                        Log.e("ERDO :", "onActivityResult: NormalTicket");
                        updateNormalTicket(newFiles);
                    }
                } else if (imageWallId == 2) {
                    try {
                        updateSpecialTicket(saveBitmap(newFiles));
                    } catch (FileNotFoundException e) {
                        Log.e("ERDO :", "onActivityResult: SpecialTicket");
                        updateSpecialTicket(newFiles);
                    }
                }
            }
        }

        if (requestCode == 5) {
            List<String> photos = data.getStringArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            for (int i = 0; i < photos.size(); i++) {
                normalAdapter_neo.add(photos.get(i));
            }
        }
        if (requestCode == 6) {
            List<String> photos = data.getStringArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            for (int i = 0; i < photos.size(); i++) {
                specialAdapter_neo.add(photos.get(i));
            }
        }
        if (requestCode == 7) {
            List<String> photos = data.getStringArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            for (int i = 0; i < photos.size(); i++) {
                annexAdapter.add(photos.get(i));
            }
        }
    }

    /**
     * 更新所选图片集合
     * @param images
     */
    private void updateNormalTicket(List<String> images) {
        if (normalPaths == null) {
            normalPaths = new ArrayList<>();
        }
        normalPaths.clear();
        for (String s : images) {
            normalPaths.add(s);
        }
        if (normalAdapter == null) {
            normalAdapter = new TicketAdapter(NonEqptRepairDisOrderActivity.this, normalPaths, this);
        }
        normal.setAdapter(normalAdapter);
        normalAdapter.notifyDataSetChanged();
    }

    private void updateSpecialTicket(List<String> images) {
        if (specialPaths == null) {
            specialPaths = new ArrayList<>();
        }
        specialPaths.clear();
        for (String s : images) {
            specialPaths.add(s);
        }
        if (specialAdapter == null) {
            specialAdapter = new TicketAdapter(NonEqptRepairDisOrderActivity.this, normalPaths, this);
        }
        special.setAdapter(specialAdapter);
        specialAdapter.notifyDataSetChanged();
    }

    private void initImageWall() {
        normalPaths = new ArrayList<>();
        normalAdapter = new TicketAdapter(getApplicationContext(), normalPaths, this);
        normal.setAdapter(normalAdapter);
        normal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 跳转到预览界面
                ImageSelector.getInstance().launchDeletePreview(NonEqptRepairDisOrderActivity.this, normalPaths, i);
            }
        });

        specialPaths = new ArrayList<>();
        specialAdapter = new TicketAdapter(getApplicationContext(), specialPaths, this);
        special.setAdapter(specialAdapter);
        special.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageSelector.getInstance().launchDeletePreview(NonEqptRepairDisOrderActivity.this, specialPaths, i);
            }
        });
    }

    private void initControllers() {
        normal = findViewById(R.id.gv_nerdo_normal);
        special = findViewById(R.id.gv_nerdo_spical);
        normalBtn = findViewById(R.id.btn_nerdo_normal);
        specialBtn = findViewById(R.id.btn_nerdo_special);
        recyclerView = findViewById(R.id.rv_nerdo);
        layout = findViewById(R.id.ll_nerdo_layout);
        status = findViewById(R.id.tv_nerdo_status);
        comSec = findViewById(R.id.btn_nerdo_comSec);
        normal_neo = findViewById(R.id.rv_nerdo_normal);
        special_neo = findViewById(R.id.rv_nerdo_spical);
        annex = findViewById(R.id.rv_nerdo_annex);
        upload = findViewById(R.id.btn_nerdo_upload);
    }

    @Override
    public void onAddButtonClick() {
        switch (TicketAdapter.getItemId()) {
            case R.id.gv_nerdo_normal:
                imageWallId = 1;
                ImageSelector.getInstance().launchSelector(NonEqptRepairDisOrderActivity.this, normalPaths);
                break;
            case R.id.gv_nerdo_spical:
                imageWallId = 2;
                ImageSelector.getInstance().launchSelector(NonEqptRepairDisOrderActivity.this, specialPaths);
                break;
        }
    }
}
