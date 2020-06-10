package edu.sdust.insapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ViEqptRepairTaskMonthlyTotalBean implements Parcelable {
    private Integer eqptrepairtaskid;//设备维修任务id
    private Integer eqptrepairdispaorderid;//设备维修派工单id
    private Integer monthlyeqptrepairtaskid;//月度设备维修任务id
    private String monthlyeqptrepairtaskexistproblemdesc;//月度设备维修任务存在问题描述
    private String monthlyeqptrepairtaskrepaircontent;//月度设备维修任务维修内容
    private Integer eqptaccntid;//设备台账id
    private String attribvalue;//设备位号
    private Integer deviceid;//设备id
    private String deviceabbre;//
    private String devicename;//设备名称
    private Integer monthlyrepairtaskid;//月度维修任务id
    private Integer monthlyrepairmaintainplanid;//月度维修维护计划id
    private Float monthlyrepairtaskstufftotpri;//月度维修任务...总价
    private Float monthlyrepairtasklabcostbudget;//月度维修任务实验室费用预算
    private Float monthlyrepairtaskbudgettotal;//月度维修任务预算总额
    private Boolean whetherneedvisa;//是否需要签证
    private Integer ownerworkshopstaffid;//业主车间人员id
    private String ownerworkshopstaffname;//业主车间人员名称
    private String ownerworkshopstaffoph;
    private Integer ownerworkshopid;//业主车间id
    private String ownerworkshopname;//业主车间名称
    private String inspproblemstatecode;//巡检问题状态代码
    private String inspproblemstatename;//巡检问题状态名称
    private Integer eqptrepairtaskcompletsituationid;//设备维修任务完成情况id
    private Integer eqptrepaircompletorderid;//设备维修完工单id
    private String eqptrepairtaskstarttime;
    private String eqptrepairtaskendtime;
    private Boolean eqptrepairtaskwhetherfinish;//设备维修任务是否完成
    private String faultcausecode;
    private String faultcausecontent;
    private String faultmechanismcode;
    private String faultmechanismcontent;
    private String faulttreatmentmeascode;
    private String faulttreatmentmeascatgcode;
    private String faulttreatmentmeascontent;
    private String parentfaultmechanismcode;
    private String oveparfilialedeptname;//检修方分公司部门名称
    private Integer oveparfilialedeptid;//检修方分公司部门id
    private String eqptrepairtaskremark;
    private String monthlyeqptrepairtaskremark;
    private String monthlyrepairtaskremark;
    private String eqptrepairtaskcompletsituationremark;
    private String plantagcode;//计划类型代码
    private String plancode;//计划类型名称

    protected ViEqptRepairTaskMonthlyTotalBean(Parcel in) {
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
        if (in.readByte() == 0) {
            monthlyeqptrepairtaskid = null;
        } else {
            monthlyeqptrepairtaskid = in.readInt();
        }
        monthlyeqptrepairtaskexistproblemdesc = in.readString();
        monthlyeqptrepairtaskrepaircontent = in.readString();
        if (in.readByte() == 0) {
            eqptaccntid = null;
        } else {
            eqptaccntid = in.readInt();
        }
        attribvalue = in.readString();
        if (in.readByte() == 0) {
            deviceid = null;
        } else {
            deviceid = in.readInt();
        }
        deviceabbre = in.readString();
        devicename = in.readString();
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
        eqptrepairtaskstarttime = in.readString();
        eqptrepairtaskendtime = in.readString();
        byte tmpEqptrepairtaskwhetherfinish = in.readByte();
        eqptrepairtaskwhetherfinish = tmpEqptrepairtaskwhetherfinish == 0 ? null : tmpEqptrepairtaskwhetherfinish == 1;
        faultcausecode = in.readString();
        faultcausecontent = in.readString();
        faultmechanismcode = in.readString();
        faultmechanismcontent = in.readString();
        faulttreatmentmeascode = in.readString();
        faulttreatmentmeascatgcode = in.readString();
        faulttreatmentmeascontent = in.readString();
        parentfaultmechanismcode = in.readString();
        oveparfilialedeptname = in.readString();
        if (in.readByte() == 0) {
            oveparfilialedeptid = null;
        } else {
            oveparfilialedeptid = in.readInt();
        }
        eqptrepairtaskremark = in.readString();
        monthlyeqptrepairtaskremark = in.readString();
        monthlyrepairtaskremark = in.readString();
        eqptrepairtaskcompletsituationremark = in.readString();
        plantagcode = in.readString();
        plancode = in.readString();
    }

    public static final Creator<ViEqptRepairTaskMonthlyTotalBean> CREATOR = new Creator<ViEqptRepairTaskMonthlyTotalBean>() {
        @Override
        public ViEqptRepairTaskMonthlyTotalBean createFromParcel(Parcel in) {
            return new ViEqptRepairTaskMonthlyTotalBean(in);
        }

        @Override
        public ViEqptRepairTaskMonthlyTotalBean[] newArray(int size) {
            return new ViEqptRepairTaskMonthlyTotalBean[size];
        }
    };

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

    public Integer getMonthlyeqptrepairtaskid() {
        return monthlyeqptrepairtaskid;
    }

    public void setMonthlyeqptrepairtaskid(Integer monthlyeqptrepairtaskid) {
        this.monthlyeqptrepairtaskid = monthlyeqptrepairtaskid;
    }

    public String getMonthlyeqptrepairtaskexistproblemdesc() {
        return monthlyeqptrepairtaskexistproblemdesc;
    }

    public void setMonthlyeqptrepairtaskexistproblemdesc(String monthlyeqptrepairtaskexistproblemdesc) {
        this.monthlyeqptrepairtaskexistproblemdesc = monthlyeqptrepairtaskexistproblemdesc;
    }

    public String getMonthlyeqptrepairtaskrepaircontent() {
        return monthlyeqptrepairtaskrepaircontent;
    }

    public void setMonthlyeqptrepairtaskrepaircontent(String monthlyeqptrepairtaskrepaircontent) {
        this.monthlyeqptrepairtaskrepaircontent = monthlyeqptrepairtaskrepaircontent;
    }

    public Integer getEqptaccntid() {
        return eqptaccntid;
    }

    public void setEqptaccntid(Integer eqptaccntid) {
        this.eqptaccntid = eqptaccntid;
    }

    public String getAttribvalue() {
        return attribvalue;
    }

    public void setAttribvalue(String attribvalue) {
        this.attribvalue = attribvalue;
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

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
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

    public String getEqptrepairtaskstarttime() {
        return eqptrepairtaskstarttime;
    }

    public void setEqptrepairtaskstarttime(String eqptrepairtaskstarttime) {
        this.eqptrepairtaskstarttime = eqptrepairtaskstarttime;
    }

    public String getEqptrepairtaskendtime() {
        return eqptrepairtaskendtime;
    }

    public void setEqptrepairtaskendtime(String eqptrepairtaskendtime) {
        this.eqptrepairtaskendtime = eqptrepairtaskendtime;
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

    public String getParentfaultmechanismcode() {
        return parentfaultmechanismcode;
    }

    public void setParentfaultmechanismcode(String parentfaultmechanismcode) {
        this.parentfaultmechanismcode = parentfaultmechanismcode;
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

    public String getEqptrepairtaskremark() {
        return eqptrepairtaskremark;
    }

    public void setEqptrepairtaskremark(String eqptrepairtaskremark) {
        this.eqptrepairtaskremark = eqptrepairtaskremark;
    }

    public String getMonthlyeqptrepairtaskremark() {
        return monthlyeqptrepairtaskremark;
    }

    public void setMonthlyeqptrepairtaskremark(String monthlyeqptrepairtaskremark) {
        this.monthlyeqptrepairtaskremark = monthlyeqptrepairtaskremark;
    }

    public String getMonthlyrepairtaskremark() {
        return monthlyrepairtaskremark;
    }

    public void setMonthlyrepairtaskremark(String monthlyrepairtaskremark) {
        this.monthlyrepairtaskremark = monthlyrepairtaskremark;
    }

    public String getEqptrepairtaskcompletsituationremark() {
        return eqptrepairtaskcompletsituationremark;
    }

    public void setEqptrepairtaskcompletsituationremark(String eqptrepairtaskcompletsituationremark) {
        this.eqptrepairtaskcompletsituationremark = eqptrepairtaskcompletsituationremark;
    }

    public String getPlantagcode() {
        return plantagcode;
    }

    public void setPlantagcode(String plantagcode) {
        this.plantagcode = plantagcode;
    }

    public String getPlancode() {
        return plancode;
    }

    public void setPlancode(String plancode) {
        this.plancode = plancode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
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
        if (monthlyeqptrepairtaskid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(monthlyeqptrepairtaskid);
        }
        parcel.writeString(monthlyeqptrepairtaskexistproblemdesc);
        parcel.writeString(monthlyeqptrepairtaskrepaircontent);
        if (eqptaccntid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(eqptaccntid);
        }
        parcel.writeString(attribvalue);
        if (deviceid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(deviceid);
        }
        parcel.writeString(deviceabbre);
        parcel.writeString(devicename);
        if (monthlyrepairtaskid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(monthlyrepairtaskid);
        }
        if (monthlyrepairmaintainplanid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(monthlyrepairmaintainplanid);
        }
        if (monthlyrepairtaskstufftotpri == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(monthlyrepairtaskstufftotpri);
        }
        if (monthlyrepairtasklabcostbudget == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(monthlyrepairtasklabcostbudget);
        }
        if (monthlyrepairtaskbudgettotal == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(monthlyrepairtaskbudgettotal);
        }
        parcel.writeByte((byte) (whetherneedvisa == null ? 0 : whetherneedvisa ? 1 : 2));
        if (ownerworkshopstaffid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(ownerworkshopstaffid);
        }
        parcel.writeString(ownerworkshopstaffname);
        parcel.writeString(ownerworkshopstaffoph);
        if (ownerworkshopid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(ownerworkshopid);
        }
        parcel.writeString(ownerworkshopname);
        parcel.writeString(inspproblemstatecode);
        parcel.writeString(inspproblemstatename);
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
        parcel.writeString(eqptrepairtaskstarttime);
        parcel.writeString(eqptrepairtaskendtime);
        parcel.writeByte((byte) (eqptrepairtaskwhetherfinish == null ? 0 : eqptrepairtaskwhetherfinish ? 1 : 2));
        parcel.writeString(faultcausecode);
        parcel.writeString(faultcausecontent);
        parcel.writeString(faultmechanismcode);
        parcel.writeString(faultmechanismcontent);
        parcel.writeString(faulttreatmentmeascode);
        parcel.writeString(faulttreatmentmeascatgcode);
        parcel.writeString(faulttreatmentmeascontent);
        parcel.writeString(parentfaultmechanismcode);
        parcel.writeString(oveparfilialedeptname);
        if (oveparfilialedeptid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(oveparfilialedeptid);
        }
        parcel.writeString(eqptrepairtaskremark);
        parcel.writeString(monthlyeqptrepairtaskremark);
        parcel.writeString(monthlyrepairtaskremark);
        parcel.writeString(eqptrepairtaskcompletsituationremark);
        parcel.writeString(plantagcode);
        parcel.writeString(plancode);
    }
}
