package edu.sdust.insapp.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import  edu.sdust.insapp.R;

public class mainView extends AppCompatActivity {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private long start;
    private RadioButton home_rb_work, home_rb_message, home_rb_data, home_rb_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        //db.close();
        home_rb_work = (RadioButton)findViewById(R.id.home_rb_work);
        home_rb_message = (RadioButton)findViewById(R.id.home_rb_message);
        home_rb_data = (RadioButton)findViewById(R.id.home_rb_data);
        home_rb_user = (RadioButton)findViewById(R.id.home_rb_user);

        fm = getFragmentManager();
        ft = fm.beginTransaction();

        ft.replace(R.id.fragment, new HomePageActivity());
        ft.commit();
        //工作按钮点击事件
        home_rb_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initback();
                Drawable top1 = getResources().getDrawable(R.mipmap.btn_normal_work);
                top1.setBounds(0, 0, top1.getMinimumWidth(), top1.getMinimumHeight());
                home_rb_work.setCompoundDrawables(null, top1, null, null);
                home_rb_work.setTextColor(Color.parseColor("#0099ff"));
                home_rb_work.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment, new HomePageActivity());
                ft.commit();
            }
        });
        home_rb_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        home_rb_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initback();
                Drawable top1 = getResources().getDrawable(R.mipmap.btn_normal_data);
                top1.setBounds(0, 0, top1.getMinimumWidth(), top1.getMinimumHeight());
                home_rb_data.setCompoundDrawables(null, top1, null, null);
                home_rb_data.setTextColor(Color.parseColor("#0099ff"));
                home_rb_data.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment, new ChooseInsDateAndDeviceidActivity());
                ft.commit();
            }
        });
        //用户的点击事件
        home_rb_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initback();
                Drawable top1 = getResources().getDrawable(R.mipmap.btn_normal_user);
                top1.setBounds(0, 0, top1.getMinimumWidth(), top1.getMinimumHeight());
                home_rb_user.setCompoundDrawables(null, top1, null, null);
                home_rb_user.setTextColor(Color.parseColor("#0099ff"));
                home_rb_user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment, new UserFragment());
                ft.commit();
            }
        });
    }
    //监听手机屏幕上的按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - start >= 2000) {
            Toast.makeText(getApplicationContext(),"请再按一次退出！",Toast.LENGTH_LONG).show();
            start = currentTimeMillis;
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void initback(){
        home_rb_work.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        home_rb_message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        home_rb_data.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        home_rb_user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        home_rb_work.setTextColor(Color.parseColor("#666666"));
        home_rb_message.setTextColor(Color.parseColor("#666666"));
        home_rb_data.setTextColor(Color.parseColor("#666666"));
        home_rb_user.setTextColor(Color.parseColor("#666666"));
        Drawable top1 = getResources().getDrawable(R.mipmap.btn_normal_work);
        top1.setBounds(0, 0, top1.getMinimumWidth(), top1.getMinimumHeight());
        home_rb_work.setCompoundDrawables(null, top1, null, null);

        Drawable top2 = getResources().getDrawable(R.mipmap.btn_normal_message);
        top2.setBounds(0, 0, top2.getMinimumWidth(), top2.getMinimumHeight());
        home_rb_message.setCompoundDrawables(null, top2, null, null);

        Drawable top3 = getResources().getDrawable(R.mipmap.btn_normal_data);
        top3.setBounds(0, 0, top3.getMinimumWidth(), top3.getMinimumHeight());
        home_rb_data.setCompoundDrawables(null, top3, null, null);

        Drawable top4 = getResources().getDrawable(R.mipmap.btn_normal_user);
        top4.setBounds(0, 0, top4.getMinimumWidth(), top4.getMinimumHeight());
        home_rb_user.setCompoundDrawables(null, top4, null, null);
    }
}


