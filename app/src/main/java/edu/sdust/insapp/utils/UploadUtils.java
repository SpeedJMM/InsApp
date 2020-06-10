package edu.sdust.insapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import edu.sdust.insapp.bean.CompMacInfoBean;
import edu.sdust.insapp.bean.DeviceFaultBean;
import edu.sdust.insapp.bean.DeviceMeaDataBean;
import edu.sdust.insapp.bean.InsOrderBean;
import edu.sdust.insapp.bean.NonDeviceFaultBean;
import edu.sdust.insapp.bean.PointInfoBean;
import edu.sdust.insapp.bean.StringParams;

/**
 * Created by Administrator on 2017/10/31.
 */

public class UploadUtils {
    private static OrderDBHelper helper;
    private static InsOrderBean ins;



    public static String getUploadParams(Context context) throws ParseException {
        helper = DbManager.getInstance(context);
        ins = InsOrderInfo.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringParams stringParams = new StringParams();
        SQLiteDatabase db = helper.getReadableDatabase();
        String username, password;
        int orderId = ins.getOrderInfo().getOrderId();
        Cursor cursor = db.rawQuery("select username,password from " + DbConstant.USER_TABLE, null);
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex("username"));
            password = cursor.getString(cursor.getColumnIndex("password"));
            stringParams.setUsername(username);
            stringParams.setPassword(password);
            cursor = db.rawQuery("select * from " + DbConstant.DEVICE_TABLE, null);
            List<DeviceMeaDataBean> deviceMeaDataBeen = new ArrayList<>();
            while (cursor.moveToNext()) {
                DeviceMeaDataBean device = new DeviceMeaDataBean();
                int deviceId = cursor.getInt(cursor.getColumnIndex("deviceId"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                boolean isSkip = status == -1;
                String skipReason = cursor.getString(cursor.getColumnIndex("remark"));
                List<PointInfoBean> pointInfoBeen = new ArrayList<>();
                String pointInsTIme = cursor.getString(cursor.getColumnIndex("pointInsTime"));
                String compMacInsTime = cursor.getString(cursor.getColumnIndex("compMacInsTime"));
                Cursor cursor1 = db.rawQuery("select * from " + DbConstant.POINT_TABLE + " where deviceId=" + deviceId, null);
                while (cursor1.moveToNext()) {
                    PointInfoBean point = new PointInfoBean();
                    int pointNum = cursor1.getInt(cursor1.getColumnIndex("pointName"));
                    String tempe = cursor1.getString(cursor1.getColumnIndex("temperature"));
                    String vibra = cursor1.getString(cursor1.getColumnIndex("vibration"));
                    String speed = cursor1.getString(cursor1.getColumnIndex("speed"));
                    String accele = cursor1.getString(cursor1.getColumnIndex("acceleration"));
                    point.setPointNum(pointNum);
                    point.setAccele(accele);
                    point.setSpeed(speed);
                    point.setTempe(tempe);
                    point.setVibra(vibra);
                    pointInfoBeen.add(point);
                }
                List<CompMacInfoBean> compMacInfos = new ArrayList<>();
                Cursor cursor2 = db.rawQuery("select * from " + DbConstant.COMPMAC_TABLE + " where deviceId=" + deviceId, null);
                while (cursor2.moveToNext()) {
                    CompMacInfoBean compMacInfo = new CompMacInfoBean();
                    String name = cursor2.getString(cursor2.getColumnIndex("name"));
                    String value = cursor2.getString(cursor2.getColumnIndex("value"));
                    compMacInfo.setName(name);
                    compMacInfo.setValue(value);
                    compMacInfos.add(compMacInfo);
                }
                device.setCompMacInsTime(compMacInsTime);
                device.setPointInsTime(pointInsTIme);
                device.setCompMacInfos(compMacInfos);
                device.setPointInfos(pointInfoBeen);
                device.setDeviceID(deviceId);
                device.setSkip(isSkip);
                device.setSkipReason(skipReason);
                deviceMeaDataBeen.add(device);

            }
            List<DeviceFaultBean> deviceFaults = new ArrayList<>();
            cursor = db.rawQuery("select * from " + DbConstant.DEVICE_FAULT_TABLE, null);
            while (cursor.moveToNext()) {
                DeviceFaultBean temp = new DeviceFaultBean();
                int id = cursor.getInt(cursor.getColumnIndex("deviceId"));
                String faultPhenoId = cursor.getString(cursor.getColumnIndex("faultPhenoId"));
                String faultPosition = cursor.getString(cursor.getColumnIndex("faultPositionIds"));
                String treatMethodId = cursor.getString(cursor.getColumnIndex("treatMethodId"));
                String describe = cursor.getString(cursor.getColumnIndex("describe"));
                String discoveryTime = cursor.getString(cursor.getColumnIndex("discoveryTime"));
                String attachment = cursor.getString(cursor.getColumnIndex("attachmentIds"));
                List<Integer> attachmentIds = new ArrayList<>();
                if (!attachment.equals("[]")) {
                    attachment = attachment.substring(1, attachment.length() - 1);
                    String[] t;
                    t = attachment.split(",");
                    for (String s : t) {
                        s = s.trim();
                        attachmentIds.add(Integer.valueOf(s));
                    }
                }
                    temp.setAttachmentIDs(attachmentIds);
                    temp.setDescribe(describe);
                    temp.setDiscoveryTime(simpleDateFormat.parse(discoveryTime));
                    temp.setDeviceID(id);
                    temp.setFaultPhenoID(faultPhenoId);
                    temp.setTreatMethodID(treatMethodId);
                    List<String> faultPositionIds = new ArrayList<>();
                    if (!faultPosition.equals("[]")) {
                        faultPosition = faultPosition.substring(1, faultPosition.length() - 1);
                        String[] positions = faultPosition.split(", ");
                        for (String s : positions) {
                            faultPositionIds.add(s);
                        }
                    }
                    temp.setFaultPositionIDs(faultPositionIds);
                deviceFaults.add(temp);
            }
            List<NonDeviceFaultBean> nonDeviceFaults = new ArrayList<>();
            cursor = db.rawQuery("select * from " + DbConstant.NON_DEVICE_FAULT_TABLE, null);
            while (cursor.moveToNext()) {
                NonDeviceFaultBean nonDeviceFault = new NonDeviceFaultBean();
                String faultPhenoId = cursor.getString(cursor.getColumnIndex("faultdeviceId"));
                String faultPosition = cursor.getString(cursor.getColumnIndex("faultposition"));
                String treatMethodID = cursor.getString(cursor.getColumnIndex("treatmethodId"));
                String describe = cursor.getString(cursor.getColumnIndex("describe"));
                String discoveryTime = cursor.getString(cursor.getColumnIndex("discoveryTime"));
                String attachment = cursor.getString(cursor.getColumnIndex("attachmentIds"));
                List<Integer> attachmentIds = new ArrayList<>();
                if (!attachment.equals("[]")) {
                    attachment = attachment.substring(1, attachment.length() - 1);
                    String[] t;
                    t = attachment.split(",");
                    for (String s : t) {
                        s = s.trim();
                        attachmentIds.add(Integer.valueOf(s));
                    }

                }
                nonDeviceFault.setTreatMethodID(treatMethodID);
                nonDeviceFault.setFaultPhenoID(faultPhenoId);
                nonDeviceFault.setDiscoveryTime(simpleDateFormat.parse(discoveryTime));
                nonDeviceFault.setAttachmentIDs(attachmentIds);
                nonDeviceFault.setDescribe(describe);
                nonDeviceFault.setFaultPosition(faultPosition);
                nonDeviceFaults.add(nonDeviceFault);
            }
                stringParams.setDeviceFaults(deviceFaults);
                stringParams.setNonDeviceFaults(nonDeviceFaults);
                stringParams.setOrderID(orderId);
                stringParams.setCompleteTime(simpleDateFormat.format(new Date()));
                stringParams.setDeviceMeaDatas(deviceMeaDataBeen);
            }
            db.close();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
//        System.out.println(gson.toJson(stringParams));
            return gson.toJson(stringParams);

        }

    public static LinkedList<String[]> getFiles(Context context) {
        LinkedList<String[]> files = new LinkedList<>();
        helper = DbManager.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+DbConstant.PICTURE_TABLE, null);
        while(cursor.moveToNext()){
            String path = cursor.getString(cursor.getColumnIndex("path"));
            path.replace("/", "//");
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            files.add(new String[]{
                    path,
                    String.valueOf(id)
            });
        }

        return files;
    }
}
