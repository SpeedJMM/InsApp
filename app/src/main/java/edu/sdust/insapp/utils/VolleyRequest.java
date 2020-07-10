package edu.sdust.insapp.utils;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.Map;

import edu.sdust.insapp.activity.MyApplication;

/**
 * Created by Administrator on 2017/10/15.
 */
//volleyRequest 类
public class VolleyRequest {
    public static StringRequest stringRequest;
    public static JsonObjectRequest jsonObjectRequest;
    //get方法
    public static void RequestGet(String url, String tag, VolleyInterface vif){
        MyApplication.getHttpQueue().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.GET, url, vif.success(), vif.error());
        stringRequest.setTag(tag);
        MyApplication.getHttpQueue().add(stringRequest);
    }
    //post方法
    public static void RequestPost(String url, String tag, final Map<String, String> params, VolleyInterface vif){
        MyApplication.getHttpQueue().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.POST, url, vif.success(), vif.error()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getHttpQueue().add(stringRequest);
    }
    //jsonObjectPost
    public static void jsonPost(String url, String tag, final JSONObject params, VolleyInterface vif){
        MyApplication.getHttpQueue().cancelAll(tag);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, vif.jsonSuccess(), vif.error());
        jsonObjectRequest.setTag(tag);
        MyApplication.getHttpQueue().add(jsonObjectRequest);
    }
}
