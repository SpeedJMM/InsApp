package edu.sdust.insapp.bean;

/**
 * Created by Administrator on 2017/11/17.
 */

public class Problemphenomenon {
    private String problemphenomenoncode;
    private String problemphenomenonname;

    public String getProblemphenomenoncode() {
        return problemphenomenoncode;
    }

    public void setProblemphenomenoncode(String problemphenomenoncode) {
        this.problemphenomenoncode = problemphenomenoncode;
    }

    public String getProblemphenomenonname() {
        return problemphenomenonname;
    }

    public void setProblemphenomenonname(String problemphenomenonname) {
        this.problemphenomenonname = problemphenomenonname;
    }

    @Override
    public String toString() {
        return ""+problemphenomenoncode+": "+problemphenomenonname;
    }
}
