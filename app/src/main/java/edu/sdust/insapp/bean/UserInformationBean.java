package edu.sdust.insapp.bean;

import java.util.Date;

import edu.sdust.insapp.activity.UserInformationActivity;

/**
 * Created by chenhw on 2017/12/27.
 */

public class UserInformationBean {

        private Integer oveparstaffid;
        private String regiresinaturecode;
        private String polcode;
        private String learnmajorcode;
        private String titlecode;
        private String educode;
        private String gendercode;
        private String degreecode;
        private String oveparstaffjobnum;
        private String oveparstaffname;
        private String oveparstaffidnum;
        private Date oveparstaffbirdate;
        private String oveparstaffgradacad;
        private Boolean oveparstaffwhethersigncontract;
        private Boolean oveparstaffwhetherpayinsurance;
        private Boolean oveparstaffwhethersatis183day;
        private Float oveparstaffthyetraintotdura;
        private String oveparstaffoph;
        private String oveparstaffmobile;
        private String oveparstafffamiaddr;
        private String oveparstaffmediinscernum;
        private String oveparstaffstate;
        private Boolean whetherlogicdel;
        private String regiresinaturename;
        private String polname;
        private String learnmajorname;
        private String titlename;
        private String eduname;
        private String gendername;
        private String degreename;
        private String oveparstaffremark;
        public UserInformationBean(){

        }
        public UserInformationBean( String polcode
                ,String learnmajorcode
                ,String titlecode
                ,String educode
                ,String gendercode
                ,String degreecode
                ,String oveparstaffjobnum
                ,String oveparstaffname
                ,String oveparstaffidnum
                ,Date oveparstaffbirdate
                ,String oveparstaffgradacad
                ,Boolean oveparstaffwhethersigncontract
               , Boolean oveparstaffwhetherpayinsurance
                ,Boolean oveparstaffwhethersatis183day
                ,Float oveparstaffthyetraintotdura
                ,String oveparstaffoph
                ,String oveparstaffmobile
                ,String oveparstafffamiaddr
               ,String oveparstaffmediinscernum
               ,String oveparstaffstate
               , Boolean whetherlogicdel
                ,String regiresinaturename
                ,String polname
                ,String learnmajorname
                ,String titlename
                , String eduname
                , String gendername
                ,String degreename
                ,String oveparstaffremark){

            this.learnmajorcode=learnmajorcode;
            this.titlecode=titlecode;
            this.educode = educode;
            this.gendercode = gendercode;
            this.degreecode= degreecode;
            this.oveparstaffjobnum=oveparstaffjobnum;
            this.oveparstaffname=oveparstaffname;
            this.oveparstaffidnum=oveparstaffidnum;
            this.oveparstaffbirdate=oveparstaffbirdate;
            this.oveparstaffgradacad=oveparstaffgradacad;
            this.oveparstaffwhethersigncontract=oveparstaffwhethersigncontract;
            this.oveparstaffwhetherpayinsurance=oveparstaffwhetherpayinsurance;
            this.oveparstaffwhethersatis183day=oveparstaffwhethersatis183day;
            this.oveparstaffthyetraintotdura=oveparstaffthyetraintotdura;
            this.oveparstaffoph=oveparstaffoph;
            this.oveparstaffmobile=oveparstaffmobile;
            this.oveparstafffamiaddr=oveparstafffamiaddr;
            this.oveparstaffmediinscernum=oveparstaffmediinscernum;
            this.oveparstaffstate=oveparstaffstate;
            this.whetherlogicdel=whetherlogicdel;
            this.regiresinaturename=regiresinaturename;
            this.polname=polname;
            this.learnmajorname=learnmajorname;
            this.titlename=titlename;
            this.eduname=eduname;
            this.gendername=gendername;
            this.degreename=degreename;
            this.oveparstaffremark=oveparstaffremark;
        }
        /**
         * @return OveParStaffID
         */
        public Integer getOveparstaffid() {
            return oveparstaffid;
        }

        /**
         * @param oveparstaffid
         */
        public void setOveparstaffid(Integer oveparstaffid) {
            this.oveparstaffid = oveparstaffid;
        }

        /**
         * @return RegiResiNatureCode
         */
        public String getRegiresinaturecode() {
            return regiresinaturecode;
        }

        /**
         * @param regiresinaturecode
         */
        public void setRegiresinaturecode(String regiresinaturecode) {
            this.regiresinaturecode = regiresinaturecode == null ? null : regiresinaturecode.trim();
        }

        /**
         * @return PolCode
         */
        public String getPolcode() {
            return polcode;
        }

        /**
         * @param polcode
         */
        public void setPolcode(String polcode) {
            this.polcode = polcode == null ? null : polcode.trim();
        }

        /**
         * @return LearnMajorCode
         */
        public String getLearnmajorcode() {
            return learnmajorcode;
        }

        /**
         * @param learnmajorcode
         */
        public void setLearnmajorcode(String learnmajorcode) {
            this.learnmajorcode = learnmajorcode == null ? null : learnmajorcode.trim();
        }

        /**
         * @return TitleCode
         */
        public String getTitlecode() {
            return titlecode;
        }

        /**
         * @param titlecode
         */
        public void setTitlecode(String titlecode) {
            this.titlecode = titlecode == null ? null : titlecode.trim();
        }

        /**
         * @return EduCode
         */
        public String getEducode() {
            return educode;
        }

        /**
         * @param educode
         */
        public void setEducode(String educode) {
            this.educode = educode == null ? null : educode.trim();
        }

        /**
         * @return GenderCode
         */
        public String getGendercode() {
            return gendercode;
        }

        /**
         * @param gendercode
         */
        public void setGendercode(String gendercode) {
            this.gendercode = gendercode == null ? null : gendercode.trim();
        }

        /**
         * @return DegreeCode
         */
        public String getDegreecode() {
            return degreecode;
        }

        /**
         * @param degreecode
         */
        public void setDegreecode(String degreecode) {
            this.degreecode = degreecode == null ? null : degreecode.trim();
        }

        /**
         * @return OveParStaffJobNum
         */
        public String getOveparstaffjobnum() {
            return oveparstaffjobnum;
        }

        /**
         * @param oveparstaffjobnum
         */
        public void setOveparstaffjobnum(String oveparstaffjobnum) {
            this.oveparstaffjobnum = oveparstaffjobnum == null ? null : oveparstaffjobnum.trim();
        }

        /**
         * @return OveParStaffName
         */
        public String getOveparstaffname() {
            return oveparstaffname;
        }

        /**
         * @param oveparstaffname
         */
        public void setOveparstaffname(String oveparstaffname) {
            this.oveparstaffname = oveparstaffname == null ? null : oveparstaffname.trim();
        }

        /**
         * @return OveParStaffIDNum
         */
        public String getOveparstaffidnum() {
            return oveparstaffidnum;
        }

        /**
         * @param oveparstaffidnum
         */
        public void setOveparstaffidnum(String oveparstaffidnum) {
            this.oveparstaffidnum = oveparstaffidnum == null ? null : oveparstaffidnum.trim();
        }

        /**
         * @return OveParStaffBirDate
         */
        public Date getOveparstaffbirdate() {
            return oveparstaffbirdate;
        }

        /**
         * @param oveparstaffbirdate
         */
        public void setOveparstaffbirdate(Date oveparstaffbirdate) {
            this.oveparstaffbirdate = oveparstaffbirdate;
        }

        /**
         * @return OveParStaffGradAcad
         */
        public String getOveparstaffgradacad() {
            return oveparstaffgradacad;
        }

        /**
         * @param oveparstaffgradacad
         */
        public void setOveparstaffgradacad(String oveparstaffgradacad) {
            this.oveparstaffgradacad = oveparstaffgradacad == null ? null : oveparstaffgradacad.trim();
        }

        /**
         * @return OveParStaffWhetherSignContract
         */
        public Boolean getOveparstaffwhethersigncontract() {
            return oveparstaffwhethersigncontract;
        }

        /**
         * @param oveparstaffwhethersigncontract
         */
        public void setOveparstaffwhethersigncontract(Boolean oveparstaffwhethersigncontract) {
            this.oveparstaffwhethersigncontract = oveparstaffwhethersigncontract;
        }

        /**
         * @return OveParStaffWhetherPayInsurance
         */
        public Boolean getOveparstaffwhetherpayinsurance() {
            return oveparstaffwhetherpayinsurance;
        }

        /**
         * @param oveparstaffwhetherpayinsurance
         */
        public void setOveparstaffwhetherpayinsurance(Boolean oveparstaffwhetherpayinsurance) {
            this.oveparstaffwhetherpayinsurance = oveparstaffwhetherpayinsurance;
        }

        /**
         * @return OveParStaffWhetherSatis183Day
         */
        public Boolean getOveparstaffwhethersatis183day() {
            return oveparstaffwhethersatis183day;
        }

        /**
         * @param oveparstaffwhethersatis183day
         */
        public void setOveparstaffwhethersatis183day(Boolean oveparstaffwhethersatis183day) {
            this.oveparstaffwhethersatis183day = oveparstaffwhethersatis183day;
        }

        /**
         * @return OveParStaffThYeTrainTotDura
         */
        public Float getOveparstaffthyetraintotdura() {
            return oveparstaffthyetraintotdura;
        }

        /**
         * @param oveparstaffthyetraintotdura
         */
        public void setOveparstaffthyetraintotdura(Float oveparstaffthyetraintotdura) {
            this.oveparstaffthyetraintotdura = oveparstaffthyetraintotdura;
        }

        /**
         * @return OveParStaffOph
         */
        public String getOveparstaffoph() {
            return oveparstaffoph;
        }

        /**
         * @param oveparstaffoph
         */
        public void setOveparstaffoph(String oveparstaffoph) {
            this.oveparstaffoph = oveparstaffoph == null ? null : oveparstaffoph.trim();
        }

        /**
         * @return OveParStaffMobile
         */
        public String getOveparstaffmobile() {
            return oveparstaffmobile;
        }

        /**
         * @param oveparstaffmobile
         */
        public void setOveparstaffmobile(String oveparstaffmobile) {
            this.oveparstaffmobile = oveparstaffmobile == null ? null : oveparstaffmobile.trim();
        }

        /**
         * @return OveParStaffFamiAddr
         */
        public String getOveparstafffamiaddr() {
            return oveparstafffamiaddr;
        }

        /**
         * @param oveparstafffamiaddr
         */
        public void setOveparstafffamiaddr(String oveparstafffamiaddr) {
            this.oveparstafffamiaddr = oveparstafffamiaddr == null ? null : oveparstafffamiaddr.trim();
        }

        /**
         * @return OveParStaffMediInsCerNum
         */
        public String getOveparstaffmediinscernum() {
            return oveparstaffmediinscernum;
        }

        /**
         * @param oveparstaffmediinscernum
         */
        public void setOveparstaffmediinscernum(String oveparstaffmediinscernum) {
            this.oveparstaffmediinscernum = oveparstaffmediinscernum == null ? null : oveparstaffmediinscernum.trim();
        }

        /**
         * @return OveParStaffState
         */
        public String getOveparstaffstate() {
            return oveparstaffstate;
        }

        /**
         * @param oveparstaffstate
         */
        public void setOveparstaffstate(String oveparstaffstate) {
            this.oveparstaffstate = oveparstaffstate == null ? null : oveparstaffstate.trim();
        }

        /**
         * @return WhetherLogicDel
         */
        public Boolean getWhetherlogicdel() {
            return whetherlogicdel;
        }

        /**
         * @param whetherlogicdel
         */
        public void setWhetherlogicdel(Boolean whetherlogicdel) {
            this.whetherlogicdel = whetherlogicdel;
        }

        /**
         * @return RegiResiNatureName
         */
        public String getRegiresinaturename() {
            return regiresinaturename;
        }

        /**
         * @param regiresinaturename
         */
        public void setRegiresinaturename(String regiresinaturename) {
            this.regiresinaturename = regiresinaturename == null ? null : regiresinaturename.trim();
        }

        /**
         * @return PolName
         */
        public String getPolname() {
            return polname;
        }

        /**
         * @param polname
         */
        public void setPolname(String polname) {
            this.polname = polname == null ? null : polname.trim();
        }

        /**
         * @return LearnMajorName
         */
        public String getLearnmajorname() {
            return learnmajorname;
        }

        /**
         * @param learnmajorname
         */
        public void setLearnmajorname(String learnmajorname) {
            this.learnmajorname = learnmajorname == null ? null : learnmajorname.trim();
        }

        /**
         * @return TitleName
         */
        public String getTitlename() {
            return titlename;
        }

        /**
         * @param titlename
         */
        public void setTitlename(String titlename) {
            this.titlename = titlename == null ? null : titlename.trim();
        }

        /**
         * @return EduName
         */
        public String getEduname() {
            return eduname;
        }

        /**
         * @param eduname
         */
        public void setEduname(String eduname) {
            this.eduname = eduname == null ? null : eduname.trim();
        }

        /**
         * @return GenderName
         */
        public String getGendername() {
            return gendername;
        }

        /**
         * @param gendername
         */
        public void setGendername(String gendername) {
            this.gendername = gendername == null ? null : gendername.trim();
        }

        /**
         * @return DegreeName
         */
        public String getDegreename() {
            return degreename;
        }

        /**
         * @param degreename
         */
        public void setDegreename(String degreename) {
            this.degreename = degreename == null ? null : degreename.trim();
        }

        /**
         * @return OveParStaffRemark
         */
        public String getOveparstaffremark() {
            return oveparstaffremark;
        }

        /**
         * @param oveparstaffremark
         */
        public void setOveparstaffremark(String oveparstaffremark) {
            this.oveparstaffremark = oveparstaffremark == null ? null : oveparstaffremark.trim();
        }
    }