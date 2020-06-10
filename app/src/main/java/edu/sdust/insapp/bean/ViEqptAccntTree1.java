package edu.sdust.insapp.bean;

public class ViEqptAccntTree1 {
    private Integer eqptaccntid;
    private Integer ownerid;
    private String eqptcatgnum;
    private String attribvalue1;
    private String eqptaccntinform;

    public ViEqptAccntTree1() {
    }

    public ViEqptAccntTree1(Integer eqptaccntid, Integer ownerid, String eqptcatgnum, String attribvalue1, String eqptaccntinform) {
        this.eqptaccntid = eqptaccntid;
        this.ownerid = ownerid;
        this.eqptcatgnum = eqptcatgnum;
        this.attribvalue1 = attribvalue1;
        this.eqptaccntinform = eqptaccntinform;
    }

    public Integer getEqptaccntid() {
        return eqptaccntid;
    }

    public void setEqptaccntid(Integer eqptaccntid) {
        this.eqptaccntid = eqptaccntid;
    }

    public Integer getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Integer ownerid) {
        this.ownerid = ownerid;
    }

    public String getEqptcatgnum() {
        return eqptcatgnum;
    }

    public void setEqptcatgnum(String eqptcatgnum) {
        this.eqptcatgnum = eqptcatgnum;
    }

    public String getAttribvalue1() {
        return attribvalue1;
    }

    public void setAttribvalue1(String attribvalue1) {
        this.attribvalue1 = attribvalue1;
    }

    public String getEqptaccntinform() {
        return eqptaccntinform;
    }

    public void setEqptaccntinform(String eqptaccntinform) {
        this.eqptaccntinform = eqptaccntinform;
    }
}
