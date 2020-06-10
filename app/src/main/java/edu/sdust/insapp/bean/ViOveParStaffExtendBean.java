package edu.sdust.insapp.bean;

import java.util.List;

/**
 * Created by chenhw on 2018/1/6.
 */

public class ViOveParStaffExtendBean extends UserInformationBean {
    private List<String> groupNameList;
    private List<String> jobNameList;
    private List<String> postNameList;
    private String departName;
    public ViOveParStaffExtendBean(){

    }
    public ViOveParStaffExtendBean(List<String> groupNameList,List<String> jobNameList,List<String> postNameList,String departName){
        this.groupNameList = groupNameList;
        this.jobNameList = jobNameList;
        this.postNameList =postNameList;
        this.departName = departName;
    }
    public List<String> getGroupNameList() {
        return groupNameList;
    }

    public void setGroupNameList(List<String> groupNameList) {
        this.groupNameList = groupNameList;
    }

    public List<String> getJobNameList() {
        return jobNameList;
    }

    public void setJobNameList(List<String> jobNameList) {
        this.jobNameList = jobNameList;
    }

    public List<String> getPostNameList() {
        return postNameList;
    }

    public void setPostNameList(List<String> postNameList) {
        this.postNameList = postNameList;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }
}
