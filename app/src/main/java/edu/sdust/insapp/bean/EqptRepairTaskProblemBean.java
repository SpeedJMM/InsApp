package edu.sdust.insapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class EqptRepairTaskProblemBean implements Parcelable {
    private Integer eqptproblemid;//设备问题id
    private Integer inspcompletorderid;
    private Integer eqptaccntid;//设备台账id
    private String problemphenomenoncode;//问题现象代码
    private String inspproblemstatecode;//问题状态代码
    private String inspproblemscenetreatmentmodecode;//问题现场处理方式代码
    private Integer deviceid;
    private String problemphenomenonname;//问题现象
    private String eqptproblemrepman;//问题保修人
    private String eqptproblemdesc;//问题描述 a
    private String eqptproblemfindtime;//问题发现时间
    private String eqptproblemsoltime;//问题解决时间
    private String inspproblemscenetreatmentmodeentry;//问题现场处理方式
    private String inspproblemstatename;//问题状态
    private Boolean whetherlogicdel;
    private String attribvalue;//设备位号
    private String deviceabbre;
    private Integer eqptrepairtaskid;
    private Integer eqptrepairdispaorderid;
    private String faultmechanismcode;
    private String faultmechanismcontent;
    private String faulttreatmentmeascode;
    private String faulttreatmentmeascontent;
    private String faulttreatmentmeascatgcode;
    private Integer eqptrepairtaskcompletsituationid;
    private Integer eqptrepaircompletorderid;
    private String eqptrepairtaskendtime;//任务结束时间
    private String eqptrepairtaskstarttime;//任务开始时间
    private Boolean eqptrepairtaskwhetherfinish;//是否完成
    private String faultcausecode;
    private String faultcausecontent;
    private Integer oveparfilialedeptid;
    private Integer oveparfilialeid;
    private String oveparfilialename;
    private String oveparfilialedeptname;
    private String eqptrepairtaskremark;
    private String eqptrepairtaskcompletsituationremark;//任务完成备注

    public EqptRepairTaskProblemBean() {
    }

    protected EqptRepairTaskProblemBean(Parcel in) {
        if (in.readByte() == 0) {
            eqptproblemid = null;
        } else {
            eqptproblemid = in.readInt();
        }
        if (in.readByte() == 0) {
            inspcompletorderid = null;
        } else {
            inspcompletorderid = in.readInt();
        }
        if (in.readByte() == 0) {
            eqptaccntid = null;
        } else {
            eqptaccntid = in.readInt();
        }
        problemphenomenoncode = in.readString();
        inspproblemstatecode = in.readString();
        inspproblemscenetreatmentmodecode = in.readString();
        if (in.readByte() == 0) {
            deviceid = null;
        } else {
            deviceid = in.readInt();
        }
        problemphenomenonname = in.readString();
        eqptproblemrepman = in.readString();
        eqptproblemdesc = in.readString();
        eqptproblemfindtime = in.readString();
        eqptproblemsoltime = in.readString();
        inspproblemscenetreatmentmodeentry = in.readString();
        inspproblemstatename = in.readString();
        byte tmpWhetherlogicdel = in.readByte();
        whetherlogicdel = tmpWhetherlogicdel == 0 ? null : tmpWhetherlogicdel == 1;
        attribvalue = in.readString();
        deviceabbre = in.readString();
        if (in.readByte() == 0) {
            eqptrepairtaskid = null;
        } else {
            eqptrepairtaskid = in.readInt();
        }
        if (in.readByte() == 0) {
            eqptrepairdispaorderid = null;
        } else {
            eqptrepairdispaorderid = in.readInt();
        }
        faultmechanismcode = in.readString();
        faultmechanismcontent = in.readString();
        faulttreatmentmeascode = in.readString();
        faulttreatmentmeascontent = in.readString();
        faulttreatmentmeascatgcode = in.readString();
        if (in.readByte() == 0) {
            eqptrepairtaskcompletsituationid = null;
        } else {
            eqptrepairtaskcompletsituationid = in.readInt();
        }
        if (in.readByte() == 0) {
            eqptrepaircompletorderid = null;
        } else {
            eqptrepaircompletorderid = in.readInt();
        }
        eqptrepairtaskendtime = in.readString();
        eqptrepairtaskstarttime = in.readString();
        byte tmpEqptrepairtaskwhetherfinish = in.readByte();
        eqptrepairtaskwhetherfinish = tmpEqptrepairtaskwhetherfinish == 0 ? null : tmpEqptrepairtaskwhetherfinish == 1;
        faultcausecode = in.readString();
        faultcausecontent = in.readString();
        if (in.readByte() == 0) {
            oveparfilialedeptid = null;
        } else {
            oveparfilialedeptid = in.readInt();
        }
        if (in.readByte() == 0) {
            oveparfilialeid = null;
        } else {
            oveparfilialeid = in.readInt();
        }
        oveparfilialename = in.readString();
        oveparfilialedeptname = in.readString();
        eqptrepairtaskremark = in.readString();
        eqptrepairtaskcompletsituationremark = in.readString();
    }

    public static final Creator<EqptRepairTaskProblemBean> CREATOR = new Creator<EqptRepairTaskProblemBean>() {
        @Override
        public EqptRepairTaskProblemBean createFromParcel(Parcel in) {
            return new EqptRepairTaskProblemBean(in);
        }

        @Override
        public EqptRepairTaskProblemBean[] newArray(int size) {
            return new EqptRepairTaskProblemBean[size];
        }
    };

    public Integer getEqptproblemid() {
        return eqptproblemid;
    }

    public void setEqptproblemid(Integer eqptproblemid) {
        this.eqptproblemid = eqptproblemid;
    }

    public Integer getInspcompletorderid() {
        return inspcompletorderid;
    }

    public void setInspcompletorderid(Integer inspcompletorderid) {
        this.inspcompletorderid = inspcompletorderid;
    }

    public Integer getEqptaccntid() {
        return eqptaccntid;
    }

    public void setEqptaccntid(Integer eqptaccntid) {
        this.eqptaccntid = eqptaccntid;
    }

    public String getProblemphenomenoncode() {
        return problemphenomenoncode;
    }

    public void setProblemphenomenoncode(String problemphenomenoncode) {
        this.problemphenomenoncode = problemphenomenoncode;
    }

    public String getInspproblemstatecode() {
        return inspproblemstatecode;
    }

    public void setInspproblemstatecode(String inspproblemstatecode) {
        this.inspproblemstatecode = inspproblemstatecode;
    }

    public String getInspproblemscenetreatmentmodecode() {
        return inspproblemscenetreatmentmodecode;
    }

    public void setInspproblemscenetreatmentmodecode(String inspproblemscenetreatmentmodecode) {
        this.inspproblemscenetreatmentmodecode = inspproblemscenetreatmentmodecode;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public String getProblemphenomenonname() {
        return problemphenomenonname;
    }

    public void setProblemphenomenonname(String problemphenomenonname) {
        this.problemphenomenonname = problemphenomenonname;
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

    public String getEqptproblemsoltime() {
        return eqptproblemsoltime;
    }

    public void setEqptproblemsoltime(String eqptproblemsoltime) {
        this.eqptproblemsoltime = eqptproblemsoltime;
    }

    public String getInspproblemscenetreatmentmodeentry() {
        return inspproblemscenetreatmentmodeentry;
    }

    public void setInspproblemscenetreatmentmodeentry(String inspproblemscenetreatmentmodeentry) {
        this.inspproblemscenetreatmentmodeentry = inspproblemscenetreatmentmodeentry;
    }

    public String getInspproblemstatename() {
        return inspproblemstatename;
    }

    public void setInspproblemstatename(String inspproblemstatename) {
        this.inspproblemstatename = inspproblemstatename;
    }

    public Boolean getWhetherlogicdel() {
        return whetherlogicdel;
    }

    public void setWhetherlogicdel(Boolean whetherlogicdel) {
        this.whetherlogicdel = whetherlogicdel;
    }

    public String getAttribvalue() {
        return attribvalue;
    }

    public void setAttribvalue(String attribvalue) {
        this.attribvalue = attribvalue;
    }

    public String getDeviceabbre() {
        return deviceabbre;
    }

    public void setDeviceabbre(String deviceabbre) {
        this.deviceabbre = deviceabbre;
    }

    public Integer getEqptrepairtaskid() {
        return eqptrepairtaskid;
    }

    public void setEqptrepairtaskid(Integer eqptrepairtaskid) {
        this.eqptrepairtaskid = eqptrepairtaskid;
    }

    public Integer getEqptrepairdispaorderid() {
        return eqptrepairdispaorderid;
    }

    public void setEqptrepairdispaorderid(Integer eqptrepairdispaorderid) {
        this.eqptrepairdispaorderid = eqptrepairdispaorderid;
    }

    public String getFaultmechanismcode() {
        return faultmechanismcode;
    }

    public void setFaultmechanismcode(String faultmechanismcode) {
        this.faultmechanismcode = faultmechanismcode;
    }

    public String getFaultmechanismcontent() {
        return faultmechanismcontent;
    }

    public void setFaultmechanismcontent(String faultmechanismcontent) {
        this.faultmechanismcontent = faultmechanismcontent;
    }

    public String getFaulttreatmentmeascode() {
        return faulttreatmentmeascode;
    }

    public void setFaulttreatmentmeascode(String faulttreatmentmeascode) {
        this.faulttreatmentmeascode = faulttreatmentmeascode;
    }

    public String getFaulttreatmentmeascontent() {
        return faulttreatmentmeascontent;
    }

    public void setFaulttreatmentmeascontent(String faulttreatmentmeascontent) {
        this.faulttreatmentmeascontent = faulttreatmentmeascontent;
    }

    public String getFaulttreatmentmeascatgcode() {
        return faulttreatmentmeascatgcode;
    }

    public void setFaulttreatmentmeascatgcode(String faulttreatmentmeascatgcode) {
        this.faulttreatmentmeascatgcode = faulttreatmentmeascatgcode;
    }

    public Integer getEqptrepairtaskcompletsituationid() {
        return eqptrepairtaskcompletsituationid;
    }

    public void setEqptrepairtaskcompletsituationid(Integer eqptrepairtaskcompletsituationid) {
        this.eqptrepairtaskcompletsituationid = eqptrepairtaskcompletsituationid;
    }

    public Integer getEqptrepaircompletorderid() {
        return eqptrepaircompletorderid;
    }

    public void setEqptrepaircompletorderid(Integer eqptrepaircompletorderid) {
        this.eqptrepaircompletorderid = eqptrepaircompletorderid;
    }

    public String getEqptrepairtaskendtime() {
        return eqptrepairtaskendtime;
    }

    public void setEqptrepairtaskendtime(String eqptrepairtaskendtime) {
        this.eqptrepairtaskendtime = eqptrepairtaskendtime;
    }

    public String getEqptrepairtaskstarttime() {
        return eqptrepairtaskstarttime;
    }

    public void setEqptrepairtaskstarttime(String eqptrepairtaskstarttime) {
        this.eqptrepairtaskstarttime = eqptrepairtaskstarttime;
    }

    public Boolean getEqptrepairtaskwhetherfinish() {
        return eqptrepairtaskwhetherfinish;
    }

    public void setEqptrepairtaskwhetherfinish(Boolean eqptrepairtaskwhetherfinish) {
        this.eqptrepairtaskwhetherfinish = eqptrepairtaskwhetherfinish;
    }

    public String getFaultcausecode() {
        return faultcausecode;
    }

    public void setFaultcausecode(String faultcausecode) {
        this.faultcausecode = faultcausecode;
    }

    public String getFaultcausecontent() {
        return faultcausecontent;
    }

    public void setFaultcausecontent(String faultcausecontent) {
        this.faultcausecontent = faultcausecontent;
    }

    public Integer getOveparfilialedeptid() {
        return oveparfilialedeptid;
    }

    public void setOveparfilialedeptid(Integer oveparfilialedeptid) {
        this.oveparfilialedeptid = oveparfilialedeptid;
    }

    public Integer getOveparfilialeid() {
        return oveparfilialeid;
    }

    public void setOveparfilialeid(Integer oveparfilialeid) {
        this.oveparfilialeid = oveparfilialeid;
    }

    public String getOveparfilialename() {
        return oveparfilialename;
    }

    public void setOveparfilialename(String oveparfilialename) {
        this.oveparfilialename = oveparfilialename;
    }

    public String getOveparfilialedeptname() {
        return oveparfilialedeptname;
    }

    public void setOveparfilialedeptname(String oveparfilialedeptname) {
        this.oveparfilialedeptname = oveparfilialedeptname;
    }

    public String getEqptrepairtaskremark() {
        return eqptrepairtaskremark;
    }

    public void setEqptrepairtaskremark(String eqptrepairtaskremark) {
        this.eqptrepairtaskremark = eqptrepairtaskremark;
    }

    public String getEqptrepairtaskcompletsituationremark() {
        return eqptrepairtaskcompletsituationremark;
    }

    public void setEqptrepairtaskcompletsituationremark(String eqptrepairtaskcompletsituationremark) {
        this.eqptrepairtaskcompletsituationremark = eqptrepairtaskcompletsituationremark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (eqptproblemid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(eqptproblemid);
        }
        if (inspcompletorderid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(inspcompletorderid);
        }
        if (eqptaccntid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(eqptaccntid);
        }
        parcel.writeString(problemphenomenoncode);
        parcel.writeString(inspproblemstatecode);
        parcel.writeString(inspproblemscenetreatmentmodecode);
        if (deviceid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(deviceid);
        }
        parcel.writeString(problemphenomenonname);
        parcel.writeString(eqptproblemrepman);
        parcel.writeString(eqptproblemdesc);
        parcel.writeString(eqptproblemfindtime);
        parcel.writeString(eqptproblemsoltime);
        parcel.writeString(inspproblemscenetreatmentmodeentry);
        parcel.writeString(inspproblemstatename);
        parcel.writeByte((byte) (whetherlogicdel == null ? 0 : whetherlogicdel ? 1 : 2));
        parcel.writeString(attribvalue);
        parcel.writeString(deviceabbre);
        if (eqptrepairtaskid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(eqptrepairtaskid);
        }
        if (eqptrepairdispaorderid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(eqptrepairdispaorderid);
        }
        parcel.writeString(faultmechanismcode);
        parcel.writeString(faultmechanismcontent);
        parcel.writeString(faulttreatmentmeascode);
        parcel.writeString(faulttreatmentmeascontent);
        parcel.writeString(faulttreatmentmeascatgcode);
        if (eqptrepairtaskcompletsituationid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(eqptrepairtaskcompletsituationid);
        }
        if (eqptrepaircompletorderid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(eqptrepaircompletorderid);
        }
        parcel.writeString(eqptrepairtaskendtime);
        parcel.writeString(eqptrepairtaskstarttime);
        parcel.writeByte((byte) (eqptrepairtaskwhetherfinish == null ? 0 : eqptrepairtaskwhetherfinish ? 1 : 2));
        parcel.writeString(faultcausecode);
        parcel.writeString(faultcausecontent);
        if (oveparfilialedeptid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(oveparfilialedeptid);
        }
        if (oveparfilialeid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(oveparfilialeid);
        }
        parcel.writeString(oveparfilialename);
        parcel.writeString(oveparfilialedeptname);
        parcel.writeString(eqptrepairtaskremark);
        parcel.writeString(eqptrepairtaskcompletsituationremark);
    }
}
