package edu.sdust.insapp.bluetooth;

import java.util.List;

/**
 * Created by 35139 on 2017/10/11.
 */

public interface MeaDataProvider{
    //获得数据
    public List<ObservationsPoint> getMeaData() throws Exception;
}

