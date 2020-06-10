package edu.sdust.insapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ViNonEqptRepairTaskMonTaskTotalBean implements Parcelable {
    private Integer noneqptrepairtaskid;//非设备维修任务id
    private Integer noneqptrepairdispaorderid;//非设备维修派工单id
    private Integer monthlynoneqptrepairtaskid;//月度非设备维修任务id
    private Integer monthlyrepairtaskid;//月度维修任务id
    private Integer monthlyrepairmaintainplanid;//月度维修维护计划id
    private Float monthlyrepairtaskstufftotpri;//月度维修任务...总价
    private Float monthlyrepairtasklabcostbudget;//月度维修任务实验室成本预算
    private Float monthlyrepairtaskbudgettotal;//月度维修任务预算总额
    private Boolean whetherneedvisa;///是否需要签证
    private String monthlynoneqptrepairtaskplace;//月度非设备维修任务地点
    private String monthlynoneqptrepairtaskcontent;//月度非设备维修任务内容
    private Integer ownerworkshopstaffid;//业主车间人员id
    private String ownerworkshopstaffname;//业主车间人员名称
    private String ownerworkshopstaffoph;
    private Integer ownerworkshopid;//业主车间id
    private String ownerworkshopname;//业主车间名称
    private String inspproblemstatecode;//巡检问题状态代码
    private String inspproblemstatename;//巡检问题状态名称
    private String plantagcode;//计划类型代码
    private Integer noneqptrepairtaskcompletsituationid;//非设备维修任务完成情况id
    private Integer noneqptrepaircompletorderid;//非设备维修完工单id
    private String noneqptrepairtaskstarttime;
    private String noneqptrepairtaskendtime;
    private Boolean problemwhetheralreadysol;//问题是否已经解决
    private Boolean whetherlogicdel;
    private String faulttreatmentmeascode;
    private String faulttreatmentmeascatgcode;
    private String faulttreatmentmeascontent;
    private String deviceabbre;
    private Integer deviceid;
    private String oveparfilialedeptname;
    private Integer oveparfilialedeptid;
    private String monthlynoneqptrepairtaskabs;//月度非设备维修任务abs
    private String noneqptrepairtaskremark;
    private String monthlyrepairtaskremark;//月度维修任务备注
    private String monthlynoneqptrepairtaskremark;
    private String noneqptrepairtaskcompletsituationremark;
    private String plancode;//计划类型名称

    protected ViNonEqptRepairTaskMonTaskTotalBean(Parcel in) {
        if (in.readByte() == 0) {
            noneqptrepairtaskid = null;
        } else {
            noneqptrepairtaskid = in.readInt();
        }
        if (in.readByte() == 0) {
            noneqptrepairdispaorderid = null;
        } else {
            noneqptrepairdispaorderid = in.readInt();
        }
        if (in.readByte() == 0) {
            monthlynoneqptrepairtaskid = null;
        } else {
            monthlynoneqptrepairtaskid = in.readInt();
        }
        if (in.readByte() == 0) {
            monthlyrepairtaskid = null;
        } else {
            monthlyrepairtaskid = in.readInt();
        }
        if (in.readByte() == 0) {
            monthlyrepairmaintainplanid = null;
        } else {
            monthlyrepairmaintainplanid = in.readInt();
        }
        if (in.readByte() == 0) {
            monthlyrepairtaskstufftotpri = null;
        } else {
            monthlyrepairtaskstufftotpri = in.readFloat();
        }
        if (in.readByte() == 0) {
            monthlyrepairtasklabcostbudget = null;
        } else {
            monthlyrepairtasklabcostbudget = in.readFloat();
        }
        if (in.readByte() == 0) {
            monthlyrepairtaskbudgettotal = null;
        } else {
            monthlyrepairtaskbudgettotal = in.readFloat();
        }
        byte tmpWhetherneedvisa = in.readByte();
        whetherneedvisa = tmpWhetherneedvisa == 0 ? null : tmpWhetherneedvisa == 1;
        monthlynoneqptrepairtaskplace = in.readString();
        monthlynoneqptrepairtaskcontent = in.readString();
        if (in.readByte() == 0) {
            ownerworkshopstaffid = null;
        } else {
            ownerworkshopstaffid = in.readInt();
        }
        ownerworkshopstaffname = in.readString();
        ownerworkshopstaffoph = in.readString();
        if (in.readByte() == 0) {
            ownerworkshopid = null;
        } else {
            ownerworkshopid = in.readInt();
        }
        ownerworkshopname = in.readString();
        inspproblemstatecode = in.readString();
        inspproblemstatename = in.readString();
        plantagcode = in.readString();
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
        byte tmpWhetherlogicdel = in.readByte();
        whetherlogicdel = tmpWhetherlogicdel == 0 ? null : tmpWhetherlogicdel == 1;
        faulttreatmentmeascode = in.readString();
        faulttreatmentmeascatgcode = in.readString();
        faulttreatmentmeascontent = in.readString();
        deviceabbre = in.readString();
        if (in.readByte() == 0) {
            deviceid = null;
        } else {
            deviceid = in.readInt();
        }
        oveparfilialedeptname = in.readString();
        if (in.readByte() == 0) {
            oveparfilialedeptid = null;
        } else {
            oveparfilialedeptid = in.readInt();
        }
        monthlynoneqptrepairtaskabs = in.readString();
        noneqptrepairtaskremark = in.readString();
        monthlyrepairtaskremark = in.readString();
        monthlynoneqptrepairtaskremark = in.readString();
        noneqptrepairtaskcompletsituationremark = in.readString();
        plancode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (noneqptrepairtaskid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(noneqptrepairtaskid);
        }
        if (noneqptrepairdispaorderid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(noneqptrepairdispaorderid);
        }
        if (monthlynoneqptrepairtaskid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(monthlynoneqptrepairtaskid);
        }
        if (monthlyrepairtaskid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(monthlyrepairtaskid);
        }
        if (monthlyrepairmaintainplanid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(monthlyrepairmaintainplanid);
        }
        if (monthlyrepairtaskstufftotpri == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(monthlyrepairtaskstufftotpri);
        }
        if (monthlyrepairtasklabcostbudget == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(monthlyrepairtasklabcostbudget);
        }
        if (monthlyrepairtaskbudgettotal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(monthlyrepairtaskbudgettotal);
        }
        dest.writeByte((byte) (whetherneedvisa == null ? 0 : whetherneedvisa ? 1 : 2));
        dest.writeString(monthlynoneqptrepairtaskplace);
        dest.writeString(monthlynoneqptrepairtaskcontent);
        if (ownerworkshopstaffid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ownerworkshopstaffid);
        }
        dest.writeString(ownerworkshopstaffname);
        dest.writeString(ownerworkshopstaffoph);
        if (ownerworkshopid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ownerworkshopid);
        }
        dest.writeString(ownerworkshopname);
        dest.writeString(inspproblemstatecode);
        dest.writeString(inspproblemstatename);
        dest.writeString(plantagcode);
        if (noneqptrepairtaskcompletsituationid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(noneqptrepairtaskcompletsituationid);
        }
        if (noneqptrepaircompletorderid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(noneqptrepaircompletorderid);
        }
        dest.writeString(noneqptrepairtaskstarttime);
        dest.writeString(noneqptrepairtaskendtime);
        dest.writeByte((byte) (problemwhetheralreadysol == null ? 0 : problemwhetheralreadysol ? 1 : 2));
        dest.writeByte((byte) (whetherlogicdel == null ? 0 : whetherlogicdel ? 1 : 2));
        dest.writeString(faulttreatmentmeascode);
        dest.writeString(faulttreatmentmeascatgcode);
        dest.writeString(faulttreatmentmeascontent);
        dest.writeString(deviceabbre);
        if (deviceid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(deviceid);
        }
        dest.writeString(oveparfilialedeptname);
        if (oveparfilialedeptid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(oveparfilialedeptid);
        }
        dest.writeString(monthlynoneqptrepairtaskabs);
        dest.writeString(noneqptrepairtaskremark);
        dest.writeString(monthlyrepairtaskremark);
        dest.writeString(monthlynoneqptrepairtaskremark);
        dest.writeString(noneqptrepairtaskcompletsituationremark);
        dest.writeString(plancode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ViNonEqptRepairTaskMonTaskTotalBean> CREATOR = new Creator<ViNonEqptRepairTaskMonTaskTotalBean>() {
        @Override
        public ViNonEqptRepairTaskMonTaskTotalBean createFromParcel(Parcel in) {
            return new ViNonEqptRepairTaskMonTaskTotalBean(in);
        }

        @Override
        public ViNonEqptRepairTaskMonTaskTotalBean[] newArray(int size) {
            return new ViNonEqptRepairTaskMonTaskTotalBean[size];
        }
    };

    public Integer getNoneqptrepairtaskid() {
        return noneqptrepairtaskid;
    }

    public void setNoneqptrepairtaskid(Integer noneqptrepairtaskid) {
        this.noneqptrepairtaskid = noneqptrepairtaskid;
    }

    public Integer getNoneqptrepairdispaorderid() {
        return noneqptrepairdispaorderid;
    }

    public void setNoneqptrepairdispaorderid(Integer noneqptrepairdispaorderid) {
        this.noneqptrepairdispaorderid = noneqptrepairdispaorderid;
    }

    public Integer getMonthlynoneqptrepairtaskid() {
        return monthlynoneqptrepairtaskid;
    }

    public void setMonthlynoneqptrepairtaskid(Integer monthlynoneqptrepairtaskid) {
        this.monthlynoneqptrepairtaskid = monthlynoneqptrepairtaskid;
    }

    public Integer getMonthlyrepairtaskid() {
        return monthlyrepairtaskid;
    }

    public void setMonthlyrepairtaskid(Integer monthlyrepairtaskid) {
        this.monthlyrepairtaskid = monthlyrepairtaskid;
    }

    public Integer getMonthlyrepairmaintainplanid() {
        return monthlyrepairmaintainplanid;
    }

    public void setMonthlyrepairmaintainplanid(Integer monthlyrepairmaintainplanid) {
        this.monthlyrepairmaintainplanid = monthlyrepairmaintainplanid;
    }

    public Float getMonthlyrepairtaskstufftotpri() {
        return monthlyrepairtaskstufftotpri;
    }

    public void setMonthlyrepairtaskstufftotpri(Float monthlyrepairtaskstufftotpri) {
        this.monthlyrepairtaskstufftotpri = monthlyrepairtaskstufftotpri;
    }

    public Float getMonthlyrepairtasklabcostbudget() {
        return monthlyrepairtasklabcostbudget;
    }

    public void setMonthlyrepairtasklabcostbudget(Float monthlyrepairtasklabcostbudget) {
        this.monthlyrepairtasklabcostbudget = monthlyrepairtasklabcostbudget;
    }

    public Float getMonthlyrepairtaskbudgettotal() {
        return monthlyrepairtaskbudgettotal;
    }

    public void setMonthlyrepairtaskbudgettotal(Float monthlyrepairtaskbudgettotal) {
        this.monthlyrepairtaskbudgettotal = monthlyrepairtaskbudgettotal;
    }

    public Boolean getWhetherneedvisa() {
        return whetherneedvisa;
    }

    public void setWhetherneedvisa(Boolean whetherneedvisa) {
        this.whetherneedvisa = whetherneedvisa;
    }

    public String getMonthlynoneqptrepairtaskplace() {
        return monthlynoneqptrepairtaskplace;
    }

    public void setMonthlynoneqptrepairtaskplace(String monthlynoneqptrepairtaskplace) {
        this.monthlynoneqptrepairtaskplace = monthlynoneqptrepairtaskplace;
    }

    public String getMonthlynoneqptrepairtaskcontent() {
        return monthlynoneqptrepairtaskcontent;
    }

    public void setMonthlynoneqptrepairtaskcontent(String monthlynoneqptrepairtaskcontent) {
        this.monthlynoneqptrepairtaskcontent = monthlynoneqptrepairtaskcontent;
    }

    public Integer getOwnerworkshopstaffid() {
        return ownerworkshopstaffid;
    }

    public void setOwnerworkshopstaffid(Integer ownerworkshopstaffid) {
        this.ownerworkshopstaffid = ownerworkshopstaffid;
    }

    public String getOwnerworkshopstaffname() {
        return ownerworkshopstaffname;
    }

    public void setOwnerworkshopstaffname(String ownerworkshopstaffname) {
        this.ownerworkshopstaffname = ownerworkshopstaffname;
    }

    public String getOwnerworkshopstaffoph() {
        return ownerworkshopstaffoph;
    }

    public void setOwnerworkshopstaffoph(String ownerworkshopstaffoph) {
        this.ownerworkshopstaffoph = ownerworkshopstaffoph;
    }

    public Integer getOwnerworkshopid() {
        return ownerworkshopid;
    }

    public void setOwnerworkshopid(Integer ownerworkshopid) {
        this.ownerworkshopid = ownerworkshopid;
    }

    public String getOwnerworkshopname() {
        return ownerworkshopname;
    }

    public void setOwnerworkshopname(String ownerworkshopname) {
        this.ownerworkshopname = ownerworkshopname;
    }

    public String getInspproblemstatecode() {
        return inspproblemstatecode;
    }

    public void setInspproblemstatecode(String inspproblemstatecode) {
        this.inspproblemstatecode = inspproblemstatecode;
    }

    public String getInspproblemstatename() {
        return inspproblemstatename;
    }

    public void setInspproblemstatename(String inspproblemstatename) {
        this.inspproblemstatename = inspproblemstatename;
    }

    public String getPlantagcode() {
        return plantagcode;
    }

    public void setPlantagcode(String plantagcode) {
        this.plantagcode = plantagcode;
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

    public Boolean getWhetherlogicdel() {
        return whetherlogicdel;
    }

    public void setWhetherlogicdel(Boolean whetherlogicdel) {
        this.whetherlogicdel = whetherlogicdel;
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

    public String getDeviceabbre() {
        return deviceabbre;
    }

    public void setDeviceabbre(String deviceabbre) {
        this.deviceabbre = deviceabbre;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
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

    public String getMonthlynoneqptrepairtaskabs() {
        return monthlynoneqptrepairtaskabs;
    }

    public void setMonthlynoneqptrepairtaskabs(String monthlynoneqptrepairtaskabs) {
        this.monthlynoneqptrepairtaskabs = monthlynoneqptrepairtaskabs;
    }

    public String getNoneqptrepairtaskremark() {
        return noneqptrepairtaskremark;
    }

    public void setNoneqptrepairtaskremark(String noneqptrepairtaskremark) {
        this.noneqptrepairtaskremark = noneqptrepairtaskremark;
    }

    public String getMonthlyrepairtaskremark() {
        return monthlyrepairtaskremark;
    }

    public void setMonthlyrepairtaskremark(String monthlyrepairtaskremark) {
        this.monthlyrepairtaskremark = monthlyrepairtaskremark;
    }

    public String getMonthlynoneqptrepairtaskremark() {
        return monthlynoneqptrepairtaskremark;
    }

    public void setMonthlynoneqptrepairtaskremark(String monthlynoneqptrepairtaskremark) {
        this.monthlynoneqptrepairtaskremark = monthlynoneqptrepairtaskremark;
    }

    public String getNoneqptrepairtaskcompletsituationremark() {
        return noneqptrepairtaskcompletsituationremark;
    }

    public void setNoneqptrepairtaskcompletsituationremark(String noneqptrepairtaskcompletsituationremark) {
        this.noneqptrepairtaskcompletsituationremark = noneqptrepairtaskcompletsituationremark;
    }

    public String getPlancode() {
        return plancode;
    }

    public void setPlancode(String plancode) {
        this.plancode = plancode;
    }
}
