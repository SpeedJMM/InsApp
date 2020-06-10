package edu.sdust.insapp.utils;

import android.content.Context;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by Administrator on 2017/10/15.
 */
//volley抽象类
public abstract class VolleyInterface {
    public Context mContext;
    public static Response.Listener<String> mListener;
    public static Response.ErrorListener errorListener;
    public  static Response.Listener<JSONObject> jsonListener;


    public VolleyInterface(Response.Listener<String> mListener, Response.ErrorListener errorListener, Response.Listener<JSONObject> jsonListener) {
        VolleyInterface.mListener = mListener;
        VolleyInterface.errorListener = errorListener;
        VolleyInterface.jsonListener = jsonListener;
    }

    public abstract void onSuccess(String result);
    public abstract void onError(VolleyError result);
    public abstract void jsonOnSuccess(JSONObject result);
    //请求成功监听方法
    public Response.Listener<String> success(){
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onSuccess(response);
            }
        };
        return mListener;
    }
    //请求失败监听方法
    public Response.ErrorListener error(){
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };
        return errorListener;
    }

    //json post 成功监听方法
    public Response.Listener<JSONObject> jsonSuccess(){
        jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonOnSuccess(response);
            }
        };
        return jsonListener;
    }


}
