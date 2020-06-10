package edu.sdust.insapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.sdust.insapp.bean.InsOrderBean;

/**
 * Created by Administrator on 2017/10/16.
 */

public class AnalysisInsOrder {
    private String insOrderData;

    public AnalysisInsOrder(String insOrderData) {
        this.insOrderData = insOrderData;
    }

    /**
     * 解析下载的派工单并存储到本地
     * @return  返回码
     */
    public InsOrderBean analysisAndstorage(){
        Gson gsonData = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        InsOrderBean insOrder = gsonData.fromJson(this.insOrderData, InsOrderBean.class);

        return insOrder;
    }

}
