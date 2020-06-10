package edu.sdust.insapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.insapp.R;
import edu.sdust.insapp.utils.queryfragementadpter;

public class queryview extends AppCompatActivity {
    private ViewPager viewPager;
    private queryfragementadpter adapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();

    private TextView onlinetext, offlinetext;
    private ImageView id_tab_line_iv;
    private AllComEqptRepairDisOrderActivity allComEqptRepairDisOrderActivity;
    private AllComNonEqptRepairDisOrderActivity allComNonEqptRepairDisOrderActivity;
    private int currentIndex;
    private int screenWidth;

    private int bmpW;
    private int offset = 0;
    private int one;
    private ImageView toolbar;
    private RadioGroup rg;
    private RadioButton rb_0, rb_1;
    private FloatingActionButton fab;
    private static final int REQUEST_CODE = 3;
    private static final int REFRESH_CODE = 8;
    //private CatchExcep application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queryview);
        findById();
        initTabLineWidth();
        init();
    }

    private void findById() {
        onlinetext = (TextView) findViewById(R.id.online);
        offlinetext = (TextView) findViewById(R.id.offline);
        viewPager = (ViewPager) findViewById(R.id.viewpage);
        id_tab_line_iv = (ImageView) findViewById(R.id.id_tab_line_iv);
        toolbar = (ImageView) findViewById(R.id.back);
        rg = findViewById(R.id.order_rg);
        rb_0 = findViewById(R.id.order_rb_0);
        rb_1 = findViewById(R.id.order_rb_1);
        fab = findViewById(R.id.order_fab);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void init() {
        allComEqptRepairDisOrderActivity = new AllComEqptRepairDisOrderActivity();
        allComNonEqptRepairDisOrderActivity = new AllComNonEqptRepairDisOrderActivity();
        mFragmentList.add(allComEqptRepairDisOrderActivity);
        mFragmentList.add(allComNonEqptRepairDisOrderActivity);
        adapter = new queryfragementadpter(this.getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
        //viewPager.setCurrentItem(0);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.order_rb_0:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.order_rb_1:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(queryview.this);
                builder.setMessage("选择派工单类型");
                builder.setPositiveButton("设备维修派工单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), EqptRepairDispatchActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                builder.setNegativeButton("非设备维修派工单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), NonEqptRepairDispatchActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                builder.show();
            }
        });

        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    animation = new TranslateAnimation(one, offset, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one+offset, 0, 0);
                    break;
            }
            currentIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(200);
            id_tab_line_iv.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            switch (arg0) {
                case 0:
                    onlinetext.setTextColor(Color.parseColor("#0099FF"));
                    offlinetext.setTextColor(Color.parseColor("#888888"));
                    rb_0.setBackgroundResource(R.drawable.radio_button_border_selected);
                    rb_1.setBackgroundResource(R.drawable.radio_button_border);
                    break;
                case 1:
                    onlinetext.setTextColor(Color.parseColor("#888888"));
                    offlinetext.setTextColor(Color.parseColor("#0099FF"));
                    rb_0.setBackgroundResource(R.drawable.radio_button_border);
                    rb_1.setBackgroundResource(R.drawable.radio_button_border_selected);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    // 设置滑动条的宽度为屏幕的1/2(根据Tab的个数而定)
    private void initTabLineWidth() {
        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.slidingcursor)
                .getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW / 2 - bmpW) / 2;
        one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        Animation animation = null;
        animation = new TranslateAnimation(0, offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(2000);
        id_tab_line_iv.startAnimation(animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case REFRESH_CODE:
                    init();
                    break;
            }
        }
    }
}
