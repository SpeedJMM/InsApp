package edu.sdust.insapp.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.sdust.insapp.activity.MyApplication;
import edu.sdust.insapp.bean.ViEqptAccntTree1;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;
import edu.sdust.insapp.utils.UrlConstant;

public class DLDeviceService extends Service {
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    public DLDeviceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        helper = DbManager.getInstance(this);

        //String url = UrlConstant.eqptTagURL;
        String url = UrlConstant.getEqptAccntTreeURL;
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        System.out.println("start download == " + simpleDateFormat.format(new Date()));
        Log.i("DLDeviceService", "onCreate: start download");
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("DLDeviceService", "onResponse: start");
                //System.out.println("end download == " + simpleDateFormat.format(new Date()));
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonarray = parser.parse(response).getAsJsonArray();
                db = helper.getReadableDatabase();
                db.beginTransaction();
                int f = 0;
                for(int h = 0; h<jsonarray.size(); h++){
                    f++;
                }
                //Cursor cursor = db.rawQuery("select _id from "+DbConstant.DOWNLOADDEVICE_TABLE, null);
                Cursor cursor = db.rawQuery("select _id from "+DbConstant.EQPTACCNTTREE_TABLE, null);
                int noFinish = cursor.getCount();
                if(noFinish==f) {
                    db.endTransaction();
                    db.close();
                }else{
                    try {
                        db.execSQL("delete from "+DbConstant.DOWNLOADDEVICE_TABLE);
                        db.execSQL("delete from "+DbConstant.EQPTACCNTTREE_TABLE);
                        for (JsonElement json : jsonarray) {
//                            EqptTag e = gson.fromJson(json, EqptTag.class);
//                            Integer deID = e.getEqptaccntid();
//                            String Dename = e.getEqpttag();
//                            String eqptcatg = e.getEqptcatgname();
//                            String deviceabbre = e.getDeviceabbre();
//                            db.execSQL("insert into " + DbConstant.DOWNLOADDEVICE_TABLE + " (deviceId,deviceName,eqptCatgName,deviceAbbre) values (?,?,?,?)", new Object[]{
//                                    deID,
//                                    Dename,
//                                    eqptcatg,
//                                    deviceabbre
//                            });
                            ViEqptAccntTree1 t = gson.fromJson(json, ViEqptAccntTree1.class);
                            Integer eqptaccntid = t.getEqptaccntid();
                            Integer ownerid = t.getOwnerid();
                            String eqptcatgnum = t.getEqptcatgnum();
                            String attribvalue1 = t.getAttribvalue1();
                            String eqptaccntinform = t.getEqptaccntinform();
                            db.execSQL("insert into " + DbConstant.EQPTACCNTTREE_TABLE + " (EqptAccntID,OwnerID,EqpptCatgNum,AttribValue1,EqptAccntInform) values (?,?,?,?,?)", new Object[]{
                                    eqptaccntid,
                                    ownerid,
                                    eqptcatgnum,
                                    attribvalue1,
                                    eqptaccntinform
                            });
                        }
                        db.setTransactionSuccessful();
                        Log.i("DLDeviceService", "onResponse: Success");
                    } finally {
                        db.endTransaction();
                        db.close();
                        Log.i("DLDeviceService", "onResponse: finish");
                    }
                }
                //System.out.println("end insert == " + simpleDateFormat.format(new Date()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("aa", "get请求失败" + error.toString());
            }
        });
        // 设置Volley超时重试策略
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 0, 1.0f));
        //RequestQueue requestQueue = VolleyManager.getInstance(context).getRequestQueue();
        //requestQueue.add(stringRequest);
        MyApplication.getHttpQueue().add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
