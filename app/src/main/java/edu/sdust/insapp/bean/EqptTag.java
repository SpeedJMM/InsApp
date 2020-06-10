package edu.sdust.insapp.bean;

/**
 * Created by Administrator on 2017/11/18.
 */

public class EqptTag {
    private int eqptaccntid;
    private String eqpttag;
    private String deviceabbre;
    private String eqptcatgname;


    public EqptTag() {

    }

    public EqptTag(Integer eqptaccntid,String eqpttag,String eqptcatgname,String deviceabbre){
        this.eqptaccntid = eqptaccntid;
        this.eqpttag=eqpttag;
        this.eqptcatgname = eqptcatgname;
        this.deviceabbre = deviceabbre;

    }
    public int getEqptaccntid() {
        return eqptaccntid;
    }

    public void setEqptaccntid(int eqptaccntid) {
        this.eqptaccntid = eqptaccntid;
    }

    public String getEqpttag() {
        return eqpttag;
    }

    public void setEqpttag(String eqpttag) {
        this.eqpttag = eqpttag;
    }

    public String getEqptcatgname() {
        return eqptcatgname;
    }

    public void setEqptcatgname(String eqptcatgname) {
        this.eqptcatgname = eqptcatgname;
    }

    public String getDeviceabbre() {
        return deviceabbre;
    }

    public void setDeviceabbre(String deviceabbre) {
        this.deviceabbre = deviceabbre;
    }


    @Override
    public String toString() {
        return ""+eqptaccntid+"/ "+eqpttag+"/"+eqptcatgname+"/"+deviceabbre;
    }
}
