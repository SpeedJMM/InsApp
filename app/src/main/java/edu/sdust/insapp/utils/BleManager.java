package edu.sdust.insapp.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.UUID;

import edu.sdust.insapp.bluetooth.BlueToothCallBack;

/**
 * Created by 35139 on 2017/10/17.
 */

public class BleManager {
    private static BleManager mBleManager;
    public static BleManager getBleManager(Context context,BlueToothCallBack blueToothCallBack){
        if(mBleManager == null)
            mBleManager = new BleManager(context,blueToothCallBack);
        else{
            mBleManager.mContext = context.getApplicationContext();
            mBleManager.mBlueToothCallBack = blueToothCallBack;
        }
        return mBleManager;
    }



    private BleManager(Context context,BlueToothCallBack blueToothCallBack){
        context = context.getApplicationContext();
        this.mContext = context;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);  //BluetoothManager只在android4.3以上有
        if (bluetoothManager == null) {
            Log.e(TAG, "Unable to initialize BluetoothManager.");
            return;
        }

        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBlueToothCallBack = blueToothCallBack;
    }


    private static final  String TAG = "BleManager";
    public static final int STOP_LESCAN = 10000;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothDevice mBluetoothDevice;

    private BluetoothGatt mBluetoothGatt;

    private Context mContext;
    public int mConnectionState = STATE_DISCONNECTED;//标志位

    private int symbol = 0;
    private  byte[] sampleData = new byte[257];//一次读取256个字节 即128个采样点

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_DISCOVER_SERVER = 3;
    public static final int STATE_OUT_OF_TIME = 4;
    public static final int STATE_FIND_DEVICE = 5;

    private boolean requireDisconnect = false;

    private boolean isScanning = false;
    private BlueToothCallBack mBlueToothCallBack = null;


    public void startLeScan() {
        requireDisconnect = false;
        //返回进度
        changeState(STATE_FIND_DEVICE);
        if (mBluetoothAdapter == null) {
            return;
        }

        if (isScanning) {
            return;
        }
        isScanning = true;
        mBluetoothAdapter.startLeScan(mLeScanCallback);   //此mLeScanCallback为回调函数
        //mHandler.sendEmptyMessageDelayed(STOP_LESCAN, 10000);  //这个搜索10秒，如果搜索不到则停止搜索
    }

    public void stopLeScan(){
//        if(mConnectionState == STATE_DISCOVER_SERVER
//                ||mConnectionState == STATE_FIND_DEVICE
//                ||mConnectionState == STATE_CONNECTING){
//            mHandler.sendEmptyMessage(STOP_LESCAN);
//        }else{
            try{
                mHandler.sendEmptyMessage(STOP_LESCAN);
                isScanning = false;
            }catch (Exception e){

            }
//        }
    }

    private LeScanCallback mLeScanCallback = new LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int arg1, byte[] arg2) {
            Log.i(TAG, "onLeScan() DeviceName------>"+device.getName());  //在这里可通过device这个对象来获取到搜索到的ble设备名称和一些相关信息
            if(device.getName() == null){
                return;
            }
            if (device.getName().contains("Vibration Measurer")) {    //判断是否搜索到你需要的ble设备
                Log.i(TAG, "onLeScan() DeviceAddress------>"+device.getAddress());
                mBluetoothDevice = device;   //获取到周边设备
                mHandler.sendEmptyMessage(STOP_LESCAN);   //1、当找到对应的设备后，立即停止扫描；2、不要循环搜索设备，为每次搜索设置适合的时间限制。避免设备不在可用范围的时候持续不停扫描，消耗电量。
                connect();  //连接
            }
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case STOP_LESCAN:
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    changeState(STATE_OUT_OF_TIME);
//                    broadcastUpdate(Config.ACTION_GATT_DISCONNECTED);
                    isScanning = false;
                    if(requireDisconnect)
                        changeState(STATE_DISCONNECTED);
                    break;
            }
        };
    };

    public boolean connect() {
        if (mBluetoothDevice == null) {
            Log.i(TAG, "BluetoothDevice is null.");
            return false;
        }

        //两个设备通过BLE通信，首先需要建立GATT连接。这里我们讲的是Android设备作为client端，连接GATT Server

        try{
            mBluetoothGatt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        mBluetoothGatt = mBluetoothDevice.connectGatt(mContext, false, mGattCallback);  //mGattCallback为回调接口

        if (mBluetoothGatt != null) {

            if (mBluetoothGatt.connect()) {
                Log.d(TAG, "Connect succeed.");
                mBluetoothGatt.discoverServices();
                //返回进度
                changeState(STATE_DISCOVER_SERVER);
                return true;
            } else {
                Log.d(TAG, "Connect fail.");
                return false;
            }
        } else {
            Log.d(TAG, "BluetoothGatt null.");
            return false;
        }
    }



    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices(); //执行到这里其实蓝牙已经连接成功了

                Log.i(TAG, "Connected to GATT server.");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED && requireDisconnect == false) {
                if(mBluetoothDevice != null){
                    Log.i(TAG, "重新连接");
                    connect();
                }else{
                    Log.i(TAG, "Disconnected from GATT server.");
                }
            } else if(newState == BluetoothProfile.STATE_DISCONNECTED && requireDisconnect == true){
//                mBluetoothGatt.close();
//                requireDisconnect = false;
            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onServicesDiscovered");

                changeState(STATE_CONNECTED);
//                byte[] writeBytes = new byte[1];
//                writeBytes[0] = 0x01;
//                BleManager.this.write(writeBytes);
            } else {
                Log.i(TAG, "onServicesDiscovered status------>" + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead------>" + HexUtil.encodeHexStr(characteristic.getValue()));

        //判断UUID是否相等
            if (Values.COUNT_FOUR.equals(characteristic.getUuid().toString())) {
                if(mBlueToothCallBack != null)
                    mBlueToothCallBack.onReadSuccess(HexUtil.encodeHexStr(characteristic.getValue()));
            }
            if(Values.FFT_INFO.equals(characteristic.getUuid().toString())){
                if(mBlueToothCallBack != null)
                    mBlueToothCallBack.onReadLength(characteristic.getValue());
            }
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged------>" + HexUtil.encodeHexStr(characteristic.getValue()));
            byte[] value =characteristic.getValue();
//判断UUID是否相等
//          if(mBlueToothCallBack != null)
            for(int j=4 ;j<value.length ;j++) {

                sampleData[symbol++] = value[j];
                Log.d(TAG,value.length+" "+symbol+" "+j);
                if(symbol >= 256){
                    short[] sampleShortData = toShortArray(sampleData);
                /*int[] sampleIntData = new int[sampleShortData.length];
                for (int i = 0;i < sampleIntData.length; i++){
                    sampleIntData[i] = getUnsignedShort(sampleShortData[i]);
                }*/
                    symbol = 0;
                    mBlueToothCallBack.onNotification(sampleShortData);
                    Log.d(TAG,sampleShortData[1]+"");

                }


            }
        }

        //接受Characteristic被写的通知,收到蓝牙模块的数据后会触发onCharacteristicWrite
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG,"status = " + status);
            Log.d(TAG, "onCharacteristicWrite------>" + HexUtil.encodeHexStr(characteristic.getValue()));
        }
    };


//    public void getBatteryLevel() {
//        BluetoothGattCharacteristic batteryLevelGattC = getCharcteristic(
//                Values.UUID_KEY_BATTERY_LEVEL_SERVICE, Values.UUID_KEY_BATTERY_LEVEL_CHARACTERISTICS);
//        if (batteryLevelGattC != null) {
//            readCharacteristic(batteryLevelGattC);
//            setCharacteristicNotification(batteryLevelGattC, true); //设置当指定characteristic值变化时，发出通知。
//        }
//    }


    //获取
    //a.获取服务
    public BluetoothGattService getService(UUID uuid) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return null;
        }
        return mBluetoothGatt.getService(uuid);
    }

    //b.获取特征

    private BluetoothGattCharacteristic getCharcteristic(String serviceUUID, String characteristicUUID) {

    //得到服务对象
        BluetoothGattService service = getService(UUID.fromString(serviceUUID));  //调用上面获取服务的方法

        if (service == null) {
            Log.e(TAG, "Can not find 'BluetoothGattService'");
            return null;
        }

    //得到此服务结点下Characteristic对象
        final BluetoothGattCharacteristic gattCharacteristic = service.getCharacteristic(UUID.fromString(characteristicUUID));
        if (gattCharacteristic != null) {
            return gattCharacteristic;
        } else {
            Log.e(TAG, "Can not find 'BluetoothGattCharacteristic'");
            return null;
        }
    }



//获取数据
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        try{
            mBluetoothGatt.readCharacteristic(characteristic);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    //获取数据
    public void readCharacteristic() {
        BluetoothGattCharacteristic characteristic = getCharcteristic(Values.COUNT_SERVICE,Values.COUNT_FOUR);

        readCharacteristic(characteristic);
    }

    public void CharacteristicNotification(boolean enable){
        BluetoothGattCharacteristic characteristic = getCharcteristic(Values.COUNT_SERVICE,Values.FFT);

        mBluetoothGatt.setCharacteristicNotification(characteristic,enable);
        //打开或者关闭通知，写入描述。
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
        symbol = 0;
    }

    //写入
    public void write(byte[] data) {   //一般都是传byte
        //得到可写入的characteristic Utils.isAIRPLANE(mContext) &&
//        if(!mBleManager.isEnabled()){
//            Log.e(TAG, "writeCharacteristic 开启飞行模式");
//            //closeBluetoothGatt();
//            isGattConnected = false;
//            broadcastUpdate(Config.ACTION_GATT_DISCONNECTED);
//            return;
//        }
        BluetoothGattCharacteristic writeCharacteristic = getCharcteristic(Values.COUNT_SERVICE, Values.COUNT_WRITE);  //这个UUID都是根据协议号的UUID
        if (writeCharacteristic == null) {
            Log.e(TAG, "Write failed. GattCharacteristic is null.");
            return;
        }
        writeCharacteristic.setValue(data); //为characteristic赋值
        writeCharacteristicWrite(writeCharacteristic);

    }



    public void writeCharacteristicWrite(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        Log.e(TAG, "BluetoothAdapter 写入数据");
        boolean isBoolean = false;
        isBoolean = mBluetoothGatt.writeCharacteristic(characteristic);
        Log.e(TAG, "BluetoothAdapter_writeCharacteristic = " +isBoolean);  //如果isBoolean返回的是true则写入成功
    }

    public void disConnect(){
        requireDisconnect = true;
        try{
            mBluetoothDevice = null;
            mHandler.sendEmptyMessage(STOP_LESCAN);
            mBluetoothGatt.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        changeState(STATE_DISCONNECTED);
    }

    private void changeState(int newState){
        mConnectionState = newState;
        if(mBlueToothCallBack != null)
            mBlueToothCallBack.onConnProcessChange(mConnectionState);
    }
    //byte转short
    public short[] toShortArray(byte[] src) {

        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) ((src[i * 2] & 0xff) | ((src[2 * i + 1] & 0xff) << 8));
        }
        return dest;
    }
    //转为无符号
    public int getUnsignedShort (short data){      //将short数据转换为0~65535 (0xFFFF 即short)。

        return data&0xFFFF;
    }

}


