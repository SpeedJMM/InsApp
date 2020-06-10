package edu.sdust.insapp.utils;

import androidx.annotation.NonNull;

import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.NonEqptComOrderBean;

/**
 * Created by Administrator on 2018/6/13.
 */

public class NonELAdapter extends CommonAdapter<NonEqptComOrderBean> {
    public NonELAdapter(@NonNull List<NonEqptComOrderBean> dataList) {
        super(dataList, R.layout.item_non_eqpt_list);
    }

    @Override
    public void convert(BaseViewHolder holder, NonEqptComOrderBean nonEqptComOrderBean, int position) {
        holder.setText(R.id.title_name, nonEqptComOrderBean.getNonEqptRepairTask() == null ? " " : nonEqptComOrderBean.getNonEqptRepairTask());

        holder.setText(R.id.text_content2, nonEqptComOrderBean.getOvepargroupname() == null ? " " : nonEqptComOrderBean.getOvepargroupname());
        holder.setText(R.id.text_content3, nonEqptComOrderBean.getViDispaStaff() == null ? " " : nonEqptComOrderBean.getViDispaStaff());
        String s = nonEqptComOrderBean.getDispaordercontroldispatime() == null ? " " : nonEqptComOrderBean.getDispaordercontroldispatime();
        if (s.equals(" ")) {
            holder.setText(R.id.non_eqpt_tv_date, " ");
            holder.setText(R.id.non_eqpt_tv_time, " ");
        } else {
            String[] t = s.split(" ");
            holder.setText(R.id.non_eqpt_tv_date, t[0]);
            holder.setText(R.id.non_eqpt_tv_time, t[1]);
        }

        //派工单状态代码
        holder.setText(R.id.non_eqpt_tag, nonEqptComOrderBean.getDispaorderstatecode() == null ? " " : nonEqptComOrderBean.getDispaorderstatecode());
        holder.setText(R.id.text_content4, nonEqptComOrderBean.getDispaorderstatename() == null ? " " : nonEqptComOrderBean.getDispaorderstatename());
    }
}
