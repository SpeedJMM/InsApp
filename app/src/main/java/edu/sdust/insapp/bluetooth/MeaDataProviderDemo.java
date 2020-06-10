package edu.sdust.insapp.bluetooth;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 35139 on 2017/10/11.
 */

public class MeaDataProviderDemo implements MeaDataProvider{
    @Override
    public List<ObservationsPoint> getMeaData(){

        //模拟耗时操作
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //初始化链表
        LinkedList<ObservationsPoint> observationsPoints = new LinkedList<>();

        //装填模拟数据
        observationsPoints.add(new ObservationsPoint(1.00,2.12,2,1));
        observationsPoints.add(new ObservationsPoint(2.00,3.12,3,2));

        //返回
        return observationsPoints;
    }
}
