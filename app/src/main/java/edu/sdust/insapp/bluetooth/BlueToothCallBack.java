package edu.sdust.insapp.bluetooth;

/**
 * Created by 35139 on 2017/10/17.
 */

public interface BlueToothCallBack {
    void onReadSuccess(String value);
    void onConnProcessChange(int newStatus);
    void onNotification(short[] sampleIntData);
   /* {
        Complex[] mData = new Complex[128];
        //mData就是傅里叶变换之后的数据。
        mData =  FFT.getFrequency(sampleIntData);
        Log.d("FFT","mData[2]-------->"+mData[2].toString());
    }*/

    void onReadLength(byte[] sampleAndLength);
}
