package edu.sdust.insapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/10/15.
 */

public class OrderDBHelper extends SQLiteOpenHelper {
    /**
     * @param context 上下文对象
     * @param name   数据库名字
     * @param factory  工厂
     * @param version  版本
     */
    public OrderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public OrderDBHelper(Context context){
        super(context, DbConstant.DATABASE_NAME, null, DbConstant.DATABASE_VERSION);
    }

    //创建时回调对象
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbConstant.CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_DEVICE_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_POINTS_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_DEVICE_FAULT_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_NON_DEVICE_FAULT_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_PICTURES_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_COMPMAC_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_DOWNLOADDEVICE_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_PROBLEM_POSITION);
        sqLiteDatabase.execSQL(DbConstant.CREATE_ORDERINFO_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREAT_ELECINSPENCLO_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREAT_EQPTACCNTTREE_TABLE);

    }
    //数据库版本更新时回调对象
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.USER_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.DEVICE_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.POINT_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.DEVICE_FAULT_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.NON_DEVICE_FAULT_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.PICTURE_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.COMPMAC_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.POSITION_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.DOWNLOADDEVICE_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.ORDERINFO_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.ELECINSPENCLO_TABLE);
        sqLiteDatabase.execSQL("drop table IF EXISTS "+DbConstant.EQPTACCNTTREE_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_DEVICE_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_POINTS_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_DEVICE_FAULT_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_NON_DEVICE_FAULT_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_PICTURES_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_COMPMAC_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_PROBLEM_POSITION);
        sqLiteDatabase.execSQL(DbConstant.CREATE_DOWNLOADDEVICE_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREATE_ORDERINFO_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREAT_ELECINSPENCLO_TABLE);
        sqLiteDatabase.execSQL(DbConstant.CREAT_EQPTACCNTTREE_TABLE);

    }
}
