package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class HistoryDataBean {

    private int result;

    private List<PointInfoBean> pointInfos;

    public HistoryDataBean() {
    }

    public HistoryDataBean(int result, List<PointInfoBean> pointInfos) {
        this.result = result;
        this.pointInfos = pointInfos;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<PointInfoBean> getPointInfos() {
        return pointInfos;
    }

    public void setPointInfos(List<PointInfoBean> pointInfos) {
        this.pointInfos = pointInfos;
    }
}
