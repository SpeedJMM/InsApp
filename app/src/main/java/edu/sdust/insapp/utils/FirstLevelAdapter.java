package edu.sdust.insapp.utils;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.bean.TreeNode;

public class FirstLevelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TreeNode> dataList;
    int selectedPosition;
    private OnItemClickListener listener;

    public FirstLevelAdapter(List<TreeNode> dataList) {
        this.dataList = dataList;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private View dataView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dataView = itemView;
            textView = itemView.findViewById(R.id.tv_first_level_data);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_level, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.textView.setText(dataList.get(position).getLabel());
        if (position == selectedPosition) {
            viewHolder.textView.setBackgroundColor(Color.WHITE);
        } else {
            viewHolder.textView.setBackgroundResource(R.color.color_eeeeee);
        }

        viewHolder.dataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.textView.setBackgroundColor(Color.WHITE);
                listener.onClick(position);
                FirstLevelAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
