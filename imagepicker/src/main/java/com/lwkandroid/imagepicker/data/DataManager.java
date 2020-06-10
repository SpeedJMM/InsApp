package com.lwkandroid.imagepicker.data;

import java.util.ArrayList;

public class DataManager {
    private ArrayList<ImageBean> resultList;
    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<ImageBean> getResultList() {
        if (resultList == null) {
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    public void setResultList(ArrayList<ImageBean> resultList) {
        this.resultList = resultList;
    }
}
