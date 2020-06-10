package edu.sdust.insapp.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.insapp.R;

public class ImageDetailCommonActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView count;
    private List<String> imageList;
    private int index;
    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        initView();
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        imageList = (List<String>) intent.getSerializableExtra("imageList");
        //一共几张图片，当前是第几个
        count.setText((index + 1) + "/" + imageList.size());
        //确定ViewPager中的组件的数量
        viewList = new ArrayList<>();

        for (int i = 0; i < imageList.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.image_zoom, null);
            viewList.add(view);
        }

        viewPager.setAdapter(new MyPager());
        //刚打开页面的时候，加载点击的那张图片
        loadImage(index);
        //设置当前展示的页面
        viewPager.setCurrentItem(index);
        //当ViewPager滑动时 做的操作
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                //一共几张图片，当前是第几个
                count.setText((arg0 + 1) + "/" + imageList.size());
                loadImage(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 初始化组件
     */
    private void initView() {
        count = (TextView) findViewById(R.id.tv_count);
        viewPager = (ViewPager) findViewById(R.id.vp_image);
    }

    /**
     * 加载图片
     *
     * @param position
     */
    private void loadImage(int position) {
        //获取视图集合中对应下标的ImageView
        View view = viewList.get(position);
        final PhotoView image = (PhotoView) view.findViewById(R.id.item_img);
        //启用缩放
        image.enable();
        Glide.with(this)
                .load(imageList.get(position))
                .into(image);
        ImageView close_image = (ImageView) view.findViewById(R.id.item_img_close);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 给ViewPager填充图片
     */
    private class MyPager extends PagerAdapter {
        @Override
        public int getCount() {
            return viewList.size();
        }

        /**
         * 被隐藏视图的处理，不显示的组件要移除
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        /**
         * 确认ViewPager的视图的数量  以及内容物  相当于BaseAdapter里的getView方法
         *
         * @param container 父容器
         * @param position  对应下表
         * @return 对应下表的组件
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //给PagerAdapter填充组件，是从视图数组中获取的
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        /**
         * 确认当前视图是否是你想展示的
         *
         * @param arg0
         * @param arg1
         * @return
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

}
