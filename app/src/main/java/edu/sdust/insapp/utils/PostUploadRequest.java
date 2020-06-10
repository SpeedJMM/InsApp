package edu.sdust.insapp.utils;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by 35139 on 2017/11/10.
 */

public class PostUploadRequest extends Request<JSONObject> {

    private final String prefix = "--";
    private final String end = "\r\n";
    private final String boundary = "--------------" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private Response.Listener<JSONObject> mListener;
    private List<String[]> fileMap;
    private Map<String,String> textMap;
    private String multiFileAttriName = "files";

    public String getMultiFileAttriName() {
        return multiFileAttriName;
    }

    public void setMultiFileAttriName(String multiFileAttriName) {
        this.multiFileAttriName = multiFileAttriName;
    }

    public PostUploadRequest(String url, List<String[]> fileMap, Map<String,String> textMap, Response.Listener<JSONObject> mListener, Response.ErrorListener listener) {
        super(Request.Method.POST, url, listener);
        this.mListener = mListener;
        this.fileMap = fileMap;
        this.textMap = textMap;
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        mListener.onResponse(jsonObject);
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(bos);
            buildTextPart(ds);
            buildFilePart(ds);
            ds.write((prefix + boundary + prefix + end).getBytes("UTF-8"));
            ds.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String,String> getParams(){
        return this.textMap;
    }

    private void buildFilePart(DataOutputStream ds) {
        for (String[] val : fileMap) {
            StringBuilder sb = new StringBuilder();
            String path = val[0];
            String filename = val[1];
            try {
                sb.append(prefix);
                sb.append(boundary);
                sb.append(end);
                sb.append("Content-Disposition: form-data;name=\"");
                sb.append(multiFileAttriName);
                sb.append("\";filename=\"");
                sb.append(filename);
                sb.append("\"" + end);//filename是文件名，如xxx.jpg，file是服务器传递参数的名字
                sb.append("Content-Type: application/octet-stream;charset=UTF-8" + end);
                sb.append(end);
                Log.e("1111111111111",sb.toString());
                ds.write(sb.toString().getBytes("UTF-8"));
                /* 取得文件的FileInputStream */
                FileInputStream fStream = new FileInputStream(path);//path是文件本地地址
                /* 设置每次写入1024bytes */
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length;
                /* 从文件读取数据至缓冲区 */
                while ((length = fStream.read(buffer)) != -1) {
                    /* 将资料写入DataOutputStream中 */
                    ds.write(buffer, 0, length);
                }
                ds.write(end.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildTextPart(DataOutputStream ds) {
        try {
            Map<String, String> map = getParams();
            Object[] key_arr = map.keySet().toArray();
            for (Object name : key_arr) {
                String val = map.get(name.toString());
                ds.write((prefix + boundary + end).getBytes("UTF-8"));
                ds.write(("Content-Disposition: form-data;name=\"" + name.toString() + "\"" + end).getBytes("UTF-8"));
                ds.write(("Content-Type: text/plain;charset=UTF-8" + end).getBytes("UTF-8"));
                ds.write((end).getBytes("UTF-8"));
                ds.write((val + end).getBytes("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}