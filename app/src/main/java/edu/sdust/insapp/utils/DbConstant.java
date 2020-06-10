package edu.sdust.insapp.utils;

/**
 * Created by Administrator on 2017/10/15.
 */

public class DbConstant {
    public static final String DATABASE_NAME = "order.db";
    public static final int DATABASE_VERSION = 16;
    public static final String CREATE_USER_TABLE = "CREATE TABLE userinfo(username VARCHAR(20) PRIMARY KEY NOT NULL, password VARCHAR(30) NOT NULL, isremember boolean default 0)";//用户表
    public static final String CREATE_DEVICE_TABLE = "CREATE TABLE devicesinfo(deviceId integer primary key,username VARCHAR(20), countId integer, deviceTag varchar(50) not null, pointSum integer default 0, orderId integer not null, status integer default 0, remark text, pointInsTime numeric, compMacInsTime numeric)";//设备列表信息
    public static final String CREATE_POINTS_TABLE = "CREATE TABLE pointsinfo(_id integer primary key autoincrement, pointName integer, insTime varchar(50), temperature varchar(20), vibration varchar(20), speed varchar(20), acceleration varchar(20), deviceId varchar(50)," +
            " foreign key(deviceId) references devicesinfo(deviceId))";//设备测点表

    public static final String CREAT_EQPTACCNTTREE_TABLE = "CREATE TABLE eqptaccnttree(_id integer primary key autoincrement, EqptAccntID integer, OwnerID integer, EqpptCatgNum varchar(200), AttribValue1 varchar(200), EqptAccntInform varchar(200))";//新设备列表190924
    public static final String CREAT_ELECINSPENCLO_TABLE = "CREATE TABLE elecinspenclo(_id integer primary key autoincrement, dispaOrderId integer, inspEqptId integer, elecInspEncloRoute varchar(500), elecInspRemark text, whetherUpload integer)";//电气巡检照片附件和备注
    public static final String CREATE_DOWNLOADDEVICE_TABLE = "CREATE TABLE dldevice(_id integer primary key autoincrement, deviceId integer, deviceName varchar(200), eqptCatgName varchar(200), deviceAbbre varchar(200))";//所有设备列表
    public static final String CREATE_DEVICE_FAULT_TABLE = "CREATE TABLE devicefault(_id integer primary key autoincrement, deviceId integer,deviceName varchar(200) , faultPhenoId text, faultPositionIds varchar(200), treatMethodId text, describe text, discoveryTime numeric, attachmentIds varchar(100))";//设备问题表
    public static final String CREATE_NON_DEVICE_FAULT_TABLE = "CREATE TABLE nondevicefault(_id integer primary key autoincrement, faultdeviceId varchar(100), faultposition text, treatmethodId varchar(100), describe text, discoveryTime numeric, attachmentIds varchar(100))";//非设备问题表
    public static final String CREATE_PICTURES_TABLE = "create table pictures(_id integer primary key autoincrement, path text, deviceId integer, nondeviceId integer, whetherUpload integer)";//照片表
    public static final String CREATE_COMPMAC_TABLE = "create table compmac(_id integer primary key autoincrement, name text, value text, deviceId integer)";//整机数据
    public static final String CREATE_PROBLEM_POSITION = "create table position(_id integer primary key autoincrement, pID text, label text, deviceId integer, nondeviceId integer)";//故障部位
    public static final String CREATE_ORDERINFO_TABLE = "create table orderinfo(_id integer primary key autoincrement, orderId integer not null, insRouteName varchar(50), dispatchingTime datetime, secondaryDispatchingTime datetime, description varchar(20), insPeople varchar(30))";//工单信息
    public static final String USER_TABLE = "userinfo";
    public static final String DEVICE_TABLE = "devicesinfo";
    public static final String POINT_TABLE = "pointsinfo";
    public static final String DEVICE_FAULT_TABLE = "devicefault";
    public static final String NON_DEVICE_FAULT_TABLE = "nondevicefault";
    public static final String PICTURE_TABLE = "pictures";
    public static final String COMPMAC_TABLE = "compmac";
    public static final String POSITION_TABLE = "position";
    public static final String DOWNLOADDEVICE_TABLE = "dldevice";
    public static final String ORDERINFO_TABLE  = "orderinfo";
    public static final String ELECINSPENCLO_TABLE = "elecinspenclo";
    public static final String EQPTACCNTTREE_TABLE = "eqptaccnttree";

}
