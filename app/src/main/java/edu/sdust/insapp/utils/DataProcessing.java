package edu.sdust.insapp.utils;

import java.util.LinkedList;
import java.util.List;

import edu.sdust.insapp.bean.ObservationsPointsBean;
import edu.sdust.insapp.bluetooth.ObservationsPoint;

/**
 * Created by WangShuQiang on 2017/10/17.
 */

public class DataProcessing {

    public static List<ObservationsPoint> convertBleDataToList(String string){
        List<ObservationsPoint> observationsPoints = new LinkedList<>();
        int length = Integer.parseInt(string.substring(2,4) + string.substring(0,2),16);

        for(int i = 0;i < length;i++){
            ObservationsPoint observationsPoint = new ObservationsPoint();

            int accele = Integer.parseInt(string.substring(16 * i + 6, 16 * i + 8) + string.substring(16 * i + 4, 16 * i + 6),16);
            int speed = Integer.parseInt(string.substring(16 * i + 10, 16 * i + 12) + string.substring(16 * i + 8, 16 * i + 10),16);
            int vibra = Integer.parseInt(string.substring(16 * i + 14, 16 * i + 16) + string.substring(16 * i + 12, 16 * i + 14),16);
            int tempe = Integer.parseInt(string.substring(16 * i + 18, 16 * i + 20) + string.substring(16 * i + 16, 16 * i + 18),16);

            if(accele >= 65000){
                accele = 65536 - accele;
            }

            observationsPoint.setTemperature(0.1 * tempe);
            observationsPoint.setSpeed(0.1 * speed);
            observationsPoint.setVibration(0.001 * vibra);
            observationsPoint.setAcceleration(0.1 * accele);
            observationsPoints.add(observationsPoint);
        }

        return observationsPoints;
//
//        for(int i = 1,x = 0,l = 0,m = 0,n = 0; i<=string.length()/2; i++){
//            double tem = 0 ;
//            tem = Integer.parseInt(string.substring(2*i-1,2), 16);
//            switch (i%4) {
//                case 1:
//                    temp[x++] =tem;
//                    break;
//                case 2:
//                    spe[l++] =tem;
//                    break;
//                case 3:
//                    vib[m++] =tem;
//                    break;
//                case 4:
//                    acce[n++] =tem;
//                    break;
//
//                default:
//                    break;
//            }
//        }
    }
}
