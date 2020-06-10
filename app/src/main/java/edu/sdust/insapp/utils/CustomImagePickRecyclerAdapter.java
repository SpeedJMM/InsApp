package edu.sdust.insapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;

import java.util.ArrayList;

import edu.sdust.insapp.R;
import edu.sdust.insapp.activity.ImageDetailCommonActivity;


public class CustomImagePickRecyclerAdapter extends RecyclerView.Adapter<CustomImagePickRecyclerAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private Activity mActivity;
    private Fragment mFragment;
    private ArrayList<String> list;
    private RecyclerView recyclerView;
    private boolean isActivity;
    private int REQUEST_IMAGE_TAKE;
    private ImagePickType mImagePickType;
    public final static String APP_PATH_PICTURE = Environment.getExternalStorageDirectory().getPath() + "/InsPhotoCache";
    private static final int MAX_IMAGE_SIZE = 5;

    public CustomImagePickRecyclerAdapter(Activity activity, ArrayList<String> list, int code, ImagePickType imagePickType) {
        this.mActivity = activity;
        mContext = activity;
        isActivity = true;
        this.list = list;
        if (list.size() == 0) {
            add(0, "添加按钮");
        }
        REQUEST_IMAGE_TAKE = code;
        mImagePickType = imagePickType;
    }

    public CustomImagePickRecyclerAdapter(Fragment fragment, ArrayList<String> list, int code, ImagePickType imagePickType) {
        this.mFragment = fragment;
        mContext = fragment.getContext();
        isActivity = false;
        this.list = list;
        if (list.size() == 0) {
            add(0, "添加按钮");
        }
        REQUEST_IMAGE_TAKE = code;
        mImagePickType = imagePickType;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_pick, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == list.size() - 1) {
//            Glide.with(mContext)
//                    .load(R.drawable.ic_add_a_photo)
//                    .into(holder.img);
            //holder.img.setImageResource(R.drawable.ic_add_a_photo);
            holder.img.setImageResource(R.drawable.btn_add_normal);

        } else {
            holder.img.setPadding(0, 0, 0, 0);
            Glide.with(mContext)
                    .load(list.get(position))
                    .into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void remove(String data) {
        int i = list.indexOf(data);
        if (i >= 0) {
            remove(i);
        }
    }

    public void clearPhoto() {
        if (list.size() > 1) {
            int flag = list.size() - 1;
            for (int i = 0; i < flag; i++) {
                list.remove(0);
            }
        }
        notifyDataSetChanged();
//        for (int i = 0; i < list.size(); i++) {
//            list.remove(0);
//        }
//        this.notifyDataSetChanged();
    }
    public void clearPhotos() {
        if (list.size() > 1) {
            int flag = list.size() - 1;
            for (int i = 0; i < flag; i++) {
                list.remove(0);
            }
        }
    }


    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void add(String data) {
        add(list.size() - 1, data);
    }

    private void add(int position, String data) {
        if (position < list.size()) {
            list.add(position, data);
        } else {
            list.add(data);
        }
        notifyItemInserted(position);
    }

    public ArrayList<String> getPhotoStringPathList() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(this.list);
        if (list.size() >= 1) {
            list.remove(list.size() - 1);
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        if (recyclerView != null) {
            int position = recyclerView.getChildAdapterPosition(view);
            if (position < list.size() - 1) {
                if (list.size() > 1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", position);
                    ArrayList<String> list = new ArrayList<>();
                    list.addAll(this.list);
                    list.remove(list.size() - 1);
                    bundle.putSerializable("imageList", list);
                    Intent intent=new Intent(mContext, ImageDetailCommonActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            } else if (position == list.size() - 1) {
                int num = MAX_IMAGE_SIZE - list.size() + 1;
                if (num <= 0) {
                    Toast.makeText(mContext, "只能选择"+MAX_IMAGE_SIZE+"张照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isActivity) {
                    //发起图片选择
                    new ImagePicker()
                            .pickType(mImagePickType) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTI)
                            .maxNum(num) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
                            .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
                            .cachePath(APP_PATH_PICTURE) //自定义缓存路径(拍照和裁剪都需要用到缓存)
                            .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                            .start(mActivity, REQUEST_IMAGE_TAKE); //自定义RequestCode
                } else {
                    new ImagePicker()
                            .pickType(mImagePickType) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTI)
                            .maxNum(num) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
                            .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
                            .cachePath(APP_PATH_PICTURE) //自定义缓存路径(拍照和裁剪都需要用到缓存)
                            .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                            .start(mFragment, REQUEST_IMAGE_TAKE); //自定义RequestCode
                }
            }
        }
    }

    @Override
    public boolean onLongClick(final View view) {
        if (recyclerView != null) {
            final int position = recyclerView.getChildAdapterPosition(view);
            if (position < list.size() - 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(position);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setTitle("提示");
                builder.setMessage("是否删除当前图片?");
                final AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        }

        return true;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_item_pick);
        }
    }

}