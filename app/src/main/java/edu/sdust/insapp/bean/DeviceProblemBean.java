package edu.sdust.insapp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class DeviceProblemBean {
    public String problemphenomenon;
    public String  inspproblemstate;
    public String major;
    public ArrayList<EqptFaultPositionBean> eqptfaultpositions;
    public List<String> problemenclos;
    public int eqptproblemid;
    public int eqptaccntid;
    public String problemphenomenonnum;
    public String inspproblemstatecode;
    public int oveparfilialedeptid;
    public String eqptproblemrepman;
    public String eqptproblemdesc;
    public String eqptproblemfindtime;

    public String getProblemphenomenon() {
        return problemphenomenon;
    }

    public void setProblemphenomenon(String problemphenomenon) {
        this.problemphenomenon = problemphenomenon;
    }

    public String getInspproblemstate() {
        return inspproblemstate;
    }

    public void setInspproblemstate(String inspproblemstate) {
        this.inspproblemstate = inspproblemstate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public ArrayList<EqptFaultPositionBean> getEqptfaultpositions() {
        return eqptfaultpositions;
    }

    public void setEqptfaultpositions(ArrayList<EqptFaultPositionBean> eqptfaultpositions) {
        this.eqptfaultpositions = eqptfaultpositions;
    }

    public List<String> getProblemenclos() {
        return problemenclos;
    }

    public void setProblemenclos(List<String> problemenclos) {
        this.problemenclos = problemenclos;
    }

    public int getEqptproblemid() {
        return eqptproblemid;
    }

    public void setEqptproblemid(int eqptproblemid) {
        this.eqptproblemid = eqptproblemid;
    }

    public int getEqptaccntid() {
        return eqptaccntid;
    }

    public void setEqptaccntid(int eqptaccntid) {
        this.eqptaccntid = eqptaccntid;
    }

    public String getProblemphenomenonnum() {
        return problemphenomenonnum;
    }

    public void setProblemphenomenonnum(String problemphenomenonnum) {
        this.problemphenomenonnum = problemphenomenonnum;
    }

    public String getInspproblemstatecode() {
        return inspproblemstatecode;
    }

    public void setInspproblemstatecode(String inspproblemstatecode) {
        this.inspproblemstatecode = inspproblemstatecode;
    }

    public int getOveparfilialedeptid() {
        return oveparfilialedeptid;
    }

    public void setOveparfilialedeptid(int oveparfilialedeptid) {
        this.oveparfilialedeptid = oveparfilialedeptid;
    }

    public String getEqptproblemrepman() {
        return eqptproblemrepman;
    }

    public void setEqptproblemrepman(String eqptproblemrepman) {
        this.eqptproblemrepman = eqptproblemrepman;
    }

    public String getEqptproblemdesc() {
        return eqptproblemdesc;
    }

    public void setEqptproblemdesc(String eqptproblemdesc) {
        this.eqptproblemdesc = eqptproblemdesc;
    }

    public String getEqptproblemfindtime() {
        return eqptproblemfindtime;
    }

    public void setEqptproblemfindtime(String eqptproblemfindtime) {
        this.eqptproblemfindtime = eqptproblemfindtime;
    }
}
