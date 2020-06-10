package edu.sdust.insapp.utils;


import androidx.annotation.NonNull;

import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.EqptComOrderBean;

/**
 * Created by Next on 2016/8/9.
 */
public class ListAdapter extends CommonAdapter<EqptComOrderBean> {


    public ListAdapter(@NonNull List<EqptComOrderBean> dataList) {
        super(dataList, R.layout.item_list);
    }

    @Override
    public void convert(BaseViewHolder holder, EqptComOrderBean eqptComOrderBean, int position) {
        holder.setText(R.id.group_name,eqptComOrderBean.getEqptTepairTask());
        holder.setText(R.id.eqpt_task,eqptComOrderBean.getOvepargroupname());
    }
}
