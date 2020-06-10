package edu.sdust.insapp.utils;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.sdust.insapp.R;

/**
 * Created by Administrator on 2017/11/3.
 */

public class URLAppend {
    private StringBuffer mURL;
    private Context mCtx;
    private boolean isFirst = true;

    public URLAppend(String URL, Context context){
        this(new StringBuffer(URL),context);
    }

    public URLAppend(StringBuffer URL,Context context){
        mURL = new StringBuffer(UrlConstant.BASE_URL).append(URL);
        mCtx = context;
    }

    public URLAppend addParam(String key,String value){
        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(isFirst){
            isFirst = false;
            mURL.append("?");
        }else{
            mURL.append("&");
        }

        mURL.append(key).append("=").append(value);

        return this;
    }

    @Override
    public String toString() {
        return mURL.toString();
    }
}
