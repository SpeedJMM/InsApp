package edu.sdust.insapp.utils;

import androidx.annotation.NonNull;

import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.EqptComOrderBean;

public class EqptListAdapter extends CommonAdapter<EqptComOrderBean> {

    public EqptListAdapter(@NonNull List<EqptComOrderBean> dataList) {
        super(dataList, R.layout.item_eqpt_list);
    }

    @Override
    public void convert(BaseViewHolder holder, EqptComOrderBean eqptComOrderBean, int position) {
        holder.setText(R.id.title_name, eqptComOrderBean.getEqptTepairTask() == null ? " " : eqptComOrderBean.getEqptTepairTask());
        holder.setText(R.id.text_content, eqptComOrderBean.getEqptTepairImport() == null ? " " : eqptComOrderBean.getEqptTepairImport());
        holder.setText(R.id.text_content1, eqptComOrderBean.getEqptTepairTaskDes() == null ? " " : eqptComOrderBean.getEqptTepairTaskDes());
        holder.setText(R.id.text_content2, eqptComOrderBean.getOvepargroupname() == null ? " " : eqptComOrderBean.getOvepargroupname());
        holder.setText(R.id.text_content3, eqptComOrderBean.getViDispaStaff() == null ? " " : eqptComOrderBean.getViDispaStaff());
        String s = eqptComOrderBean.getDispaordercontroldispatime() == null ? " " : eqptComOrderBean.getDispaordercontroldispatime();
        if (s.equals(" ")) {
            holder.setText(R.id.eqpt_tv_date, " ");
                    holder.setText(R.id.eqpt_tv_time, " ");
        } else {
            String[] t = s.split(" ");
            holder.setText(R.id.eqpt_tv_date, t[0]);
            holder.setText(R.id.eqpt_tv_time, t[1]);
        }

        //派工单状态代码
        holder.setText(R.id.eqpt_tag, eqptComOrderBean.getDispaorderstatecode() == null ? " " : eqptComOrderBean.getDispaorderstatecode());
        holder.setText(R.id.text_content4, eqptComOrderBean.getDispaorderstatename() == null ? " " : eqptComOrderBean.getDispaorderstatename());
    }
}
