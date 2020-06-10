package edu.sdust.insapp.utils;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.TreeNode;

public class SecondLevelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TreeNode> dataList;
    private List<Boolean> statusList;
    private boolean checkboxIsChecked;
    private OnItemClickListener listener;

    public SecondLevelAdapter(List<TreeNode> dataList, List<Boolean> statusList) {
        this.dataList = dataList;
        this.statusList = statusList;
    }

    public boolean isCheckboxIsChecked() {
        return checkboxIsChecked;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private View dataView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dataView = itemView;
            checkBox = itemView.findViewById(R.id.cb_second_level);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second_level, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.checkBox.setText(dataList.get(position).getLabel());
        viewHolder.checkBox.setChecked(statusList.get(position));
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkboxIsChecked = statusList.get(position);
                if (checkboxIsChecked) {
                    viewHolder.checkBox.setChecked(false);
                    statusList.set(position, false);
                } else {
                    viewHolder.checkBox.setChecked(true);
                    statusList.set(position, true);
                }
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
