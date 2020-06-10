package edu.sdust.insapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.insapp.bean.HistoryDataBean;
import edu.sdust.insapp.bean.PointInfoBean;

/**
 * Created by Administrator on 2017/11/1.
 */

public class ParserHistoryData {

    public static HistoryDataBean parserHistoryData(String result){
        List<PointInfoBean> pointInfos = new ArrayList<>();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        HistoryDataBean historyDataBean = gson.fromJson(result, HistoryDataBean.class);

        return historyDataBean;


    }
}
