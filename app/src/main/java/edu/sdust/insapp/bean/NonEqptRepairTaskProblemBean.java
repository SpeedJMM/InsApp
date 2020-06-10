package edu.sdust.insapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NonEqptRepairTaskProblemBean implements Parcelable {
    private Integer noneqptrepairtaskid;//非设备维修任务id
    private Integer noneqptproblemid;//非设备问题id
    private String problemphenomenoncode;//问题现象代码
    private String inspproblemstatecode;//巡检问题状态代码
    private String inspproblemscenetreatmentmodecode;
    private String problemphenomenonname;//问题现象名称
    private String noneqptproblemrepman;
    private String noneqptproblemplace;//非设备问题地点
    private String noneqptproblemdesc;//非设备问题描述
    private String inspproblemstatename;//巡检问题状态名称
    private String inspproblemscenetreatmentmodeentry;
    private String noneqptproblemfindtime;//非设备问题发现时间
    private String noneqptproblemsoltime;//非设备问题解决时间
    private Integer noneqptrepairdispaorderid;//非设备维修派工单id
    private Integer noneqptrepairtaskcompletsituationid;//非设备维修任务完成情况id
    private Integer noneqptrepaircompletorderid;//非设备维修完工单id
    private String noneqptrepairtaskstarttime;//非设备维修任务开始时间
    private String noneqptrepairtaskendtime;//非设备维修任务结束时间
    private Boolean problemwhetheralreadysol;//是否完成
    private String faulttreatmentmeascode;
    private String faulttreatmentmeascatgcode;
    private String faulttreatmentmeascontent;
    private Integer deviceid;//设备id
    private String deviceabbre;//所属装置
    private String oveparfilialedeptname;//班组名称
    private Integer oveparfilialedeptid;//班组id
    private String noneqptrepairtaskremark;
    private String noneqptrepairtaskcompletsituationremark;

    public NonEqptRepairTaskProblemBean() {
    }

    protected NonEqptRepairTaskProblemBean(Parcel in) {
        if (in.readByte() == 0) {
            noneqptrepairtaskid = null;
        } else {
            noneqptrepairtaskid = in.readInt();
        }
        if (in.readByte() == 0) {
            noneqptproblemid = null;
        } else {
            noneqptproblemid = in.readInt();
        }
        problemphenomenoncode = in.readString();
        inspproblemstatecode = in.readString();
        inspproblemscenetreatmentmodecode = in.readString();
        problemphenomenonname = in.readString();
        noneqptproblemrepman = in.readString();
        noneqptproblemplace = in.readString();
        noneqptproblemdesc = in.readString();
        inspproblemstatename = in.readString();
        inspproblemscenetreatmentmodeentry = in.readString();
        noneqptproblemfindtime = in.readString();
        noneqptproblemsoltime = in.readString();
        if (in.readByte() == 0) {
            noneqptrepairdispaorderid = null;
        } else {
            noneqptrepairdispaorderid = in.readInt();
        }
        if (in.readByte() == 0) {
            noneqptrepairtaskcompletsituationid = null;
        } else {
            noneqptrepairtaskcompletsituationid = in.readInt();
        }
        if (in.readByte() == 0) {
            noneqptrepaircompletorderid = null;
        } else {
            noneqptrepaircompletorderid = in.readInt();
        }
        noneqptrepairtaskstarttime = in.readString();
        noneqptrepairtaskendtime = in.readString();
        byte tmpProblemwhetheralreadysol = in.readByte();
        problemwhetheralreadysol = tmpProblemwhetheralreadysol == 0 ? null : tmpProblemwhetheralreadysol == 1;
        faulttreatmentmeascode = in.readString();
        faulttreatmentmeascatgcode = in.readString();
        faulttreatmentmeascontent = in.readString();
        if (in.readByte() == 0) {
            deviceid = null;
        } else {
            deviceid = in.readInt();
        }
        deviceabbre = in.readString();
        oveparfilialedeptname = in.readString();
        if (in.readByte() == 0) {
            oveparfilialedeptid = null;
        } else {
            oveparfilialedeptid = in.readInt();
        }
        noneqptrepairtaskremark = in.readString();
        noneqptrepairtaskcompletsituationremark = in.readString();
    }

    public static final Creator<NonEqptRepairTaskProblemBean> CREATOR = new Creator<NonEqptRepairTaskProblemBean>() {
        @Override
        public NonEqptRepairTaskProblemBean createFromParcel(Parcel in) {
            return new NonEqptRepairTaskProblemBean(in);
        }

        @Override
        public NonEqptRepairTaskProblemBean[] newArray(int size) {
            return new NonEqptRepairTaskProblemBean[size];
        }
    };

    public Integer getNoneqptrepairtaskid() {
        return noneqptrepairtaskid;
    }

    public void setNoneqptrepairtaskid(Integer noneqptrepairtaskid) {
        this.noneqptrepairtaskid = noneqptrepairtaskid;
    }

    public Integer getNoneqptproblemid() {
        return noneqptproblemid;
    }

    public void setNoneqptproblemid(Integer noneqptproblemid) {
        this.noneqptproblemid = noneqptproblemid;
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

    public String getProblemphenomenonname() {
        return problemphenomenonname;
    }

    public void setProblemphenomenonname(String problemphenomenonname) {
        this.problemphenomenonname = problemphenomenonname;
    }

    public String getNoneqptproblemrepman() {
        return noneqptproblemrepman;
    }

    public void setNoneqptproblemrepman(String noneqptproblemrepman) {
        this.noneqptproblemrepman = noneqptproblemrepman;
    }

    public String getNoneqptproblemplace() {
        return noneqptproblemplace;
    }

    public void setNoneqptproblemplace(String noneqptproblemplace) {
        this.noneqptproblemplace = noneqptproblemplace;
    }

    public String getNoneqptproblemdesc() {
        return noneqptproblemdesc;
    }

    public void setNoneqptproblemdesc(String noneqptproblemdesc) {
        this.noneqptproblemdesc = noneqptproblemdesc;
    }

    public String getInspproblemstatename() {
        return inspproblemstatename;
    }

    public void setInspproblemstatename(String inspproblemstatename) {
        this.inspproblemstatename = inspproblemstatename;
    }

    public String getInspproblemscenetreatmentmodeentry() {
        return inspproblemscenetreatmentmodeentry;
    }

    public void setInspproblemscenetreatmentmodeentry(String inspproblemscenetreatmentmodeentry) {
        this.inspproblemscenetreatmentmodeentry = inspproblemscenetreatmentmodeentry;
    }

    public String getNoneqptproblemfindtime() {
        return noneqptproblemfindtime;
    }

    public void setNoneqptproblemfindtime(String noneqptproblemfindtime) {
        this.noneqptproblemfindtime = noneqptproblemfindtime;
    }

    public String getNoneqptproblemsoltime() {
        return noneqptproblemsoltime;
    }

    public void setNoneqptproblemsoltime(String noneqptproblemsoltime) {
        this.noneqptproblemsoltime = noneqptproblemsoltime;
    }

    public Integer getNoneqptrepairdispaorderid() {
        return noneqptrepairdispaorderid;
    }

    public void setNoneqptrepairdispaorderid(Integer noneqptrepairdispaorderid) {
        this.noneqptrepairdispaorderid = noneqptrepairdispaorderid;
    }

    public Integer getNoneqptrepairtaskcompletsituationid() {
        return noneqptrepairtaskcompletsituationid;
    }

    public void setNoneqptrepairtaskcompletsituationid(Integer noneqptrepairtaskcompletsituationid) {
        this.noneqptrepairtaskcompletsituationid = noneqptrepairtaskcompletsituationid;
    }

    public Integer getNoneqptrepaircompletorderid() {
        return noneqptrepaircompletorderid;
    }

    public void setNoneqptrepaircompletorderid(Integer noneqptrepaircompletorderid) {
        this.noneqptrepaircompletorderid = noneqptrepaircompletorderid;
    }

    public String getNoneqptrepairtaskstarttime() {
        return noneqptrepairtaskstarttime;
    }

    public void setNoneqptrepairtaskstarttime(String noneqptrepairtaskstarttime) {
        this.noneqptrepairtaskstarttime = noneqptrepairtaskstarttime;
    }

    public String getNoneqptrepairtaskendtime() {
        return noneqptrepairtaskendtime;
    }

    public void setNoneqptrepairtaskendtime(String noneqptrepairtaskendtime) {
        this.noneqptrepairtaskendtime = noneqptrepairtaskendtime;
    }

    public Boolean getProblemwhetheralreadysol() {
        return problemwhetheralreadysol;
    }

    public void setProblemwhetheralreadysol(Boolean problemwhetheralreadysol) {
        this.problemwhetheralreadysol = problemwhetheralreadysol;
    }

    public String getFaulttreatmentmeascode() {
        return faulttreatmentmeascode;
    }

    public void setFaulttreatmentmeascode(String faulttreatmentmeascode) {
        this.faulttreatmentmeascode = faulttreatmentmeascode;
    }

    public String getFaulttreatmentmeascatgcode() {
        return faulttreatmentmeascatgcode;
    }

    public void setFaulttreatmentmeascatgcode(String faulttreatmentmeascatgcode) {
        this.faulttreatmentmeascatgcode = faulttreatmentmeascatgcode;
    }

    public String getFaulttreatmentmeascontent() {
        return faulttreatmentmeascontent;
    }

    public void setFaulttreatmentmeascontent(String faulttreatmentmeascontent) {
        this.faulttreatmentmeascontent = faulttreatmentmeascontent;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceabbre() {
        return deviceabbre;
    }

    public void setDeviceabbre(String deviceabbre) {
        this.deviceabbre = deviceabbre;
    }

    public String getOveparfilialedeptname() {
        return oveparfilialedeptname;
    }

    public void setOveparfilialedeptname(String oveparfilialedeptname) {
        this.oveparfilialedeptname = oveparfilialedeptname;
    }

    public Integer getOveparfilialedeptid() {
        return oveparfilialedeptid;
    }

    public void setOveparfilialedeptid(Integer oveparfilialedeptid) {
        this.oveparfilialedeptid = oveparfilialedeptid;
    }

    public String getNoneqptrepairtaskremark() {
        return noneqptrepairtaskremark;
    }

    public void setNoneqptrepairtaskremark(String noneqptrepairtaskremark) {
        this.noneqptrepairtaskremark = noneqptrepairtaskremark;
    }

    public String getNoneqptrepairtaskcompletsituationremark() {
        return noneqptrepairtaskcompletsituationremark;
    }

    public void setNoneqptrepairtaskcompletsituationremark(String noneqptrepairtaskcompletsituationremark) {
        this.noneqptrepairtaskcompletsituationremark = noneqptrepairtaskcompletsituationremark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (noneqptrepairtaskid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(noneqptrepairtaskid);
        }
        if (noneqptproblemid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(noneqptproblemid);
        }
        parcel.writeString(problemphenomenoncode);
        parcel.writeString(inspproblemstatecode);
        parcel.writeString(inspproblemscenetreatmentmodecode);
        parcel.writeString(problemphenomenonname);
        parcel.writeString(noneqptproblemrepman);
        parcel.writeString(noneqptproblemplace);
        parcel.writeString(noneqptproblemdesc);
        parcel.writeString(inspproblemstatename);
        parcel.writeString(inspproblemscenetreatmentmodeentry);
        parcel.writeString(noneqptproblemfindtime);
        parcel.writeString(noneqptproblemsoltime);
        if (noneqptrepairdispaorderid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(noneqptrepairdispaorderid);
        }
        if (noneqptrepairtaskcompletsituationid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(noneqptrepairtaskcompletsituationid);
        }
        if (noneqptrepaircompletorderid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(noneqptrepaircompletorderid);
        }
        parcel.writeString(noneqptrepairtaskstarttime);
        parcel.writeString(noneqptrepairtaskendtime);
        parcel.writeByte((byte) (problemwhetheralreadysol == null ? 0 : problemwhetheralreadysol ? 1 : 2));
        parcel.writeString(faulttreatmentmeascode);
        parcel.writeString(faulttreatmentmeascatgcode);
        parcel.writeString(faulttreatmentmeascontent);
        if (deviceid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(deviceid);
        }
        parcel.writeString(deviceabbre);
        parcel.writeString(oveparfilialedeptname);
        if (oveparfilialedeptid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(oveparfilialedeptid);
        }
        parcel.writeString(noneqptrepairtaskremark);
        parcel.writeString(noneqptrepairtaskcompletsituationremark);
    }
}
