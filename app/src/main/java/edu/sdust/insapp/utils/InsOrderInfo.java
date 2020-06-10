package edu.sdust.insapp.utils;

import edu.sdust.insapp.bean.InsOrderBean;
import edu.sdust.insapp.bean.OrderInfoBean;

/**
 * Created by Administrator on 2017/10/21.
 */

public class InsOrderInfo {
    private static InsOrderBean insOrder;


    public static InsOrderBean getInstance(){
        if(insOrder == null){
            insOrder = new InsOrderBean();

        }

        return insOrder;
    }


}
