package edu.sdust.insapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import edu.sdust.insapp.R;
import edu.sdust.insapp.utils.AMapManager;

/**
 * Created by chenhw on 2017/11/17.
 */

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //关联布局   需要让整个页面实现 动画效果 和 跳转 功能  所以先要得到一个视图view，对视图进行操作
        LayoutInflater inflater = LayoutInflater.from(this);
        View root = inflater.inflate(R.layout.activity_welcome, null);

        setContentView(root);
        //渐变展示启动屏    加载XML文件的方法
        Animation aa = AnimationUtils.loadAnimation(this, R.anim.alpha);
        root.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                //在动画结束时执行跳转页面操作
                doJump();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        });

        //初始化定位系统
        AMapManager.initAMapManager(getApplicationContext());
        AMapManager.getInstance().start();
    }

    private void doJump() {
       /* if(-1 == SystemHelper.getNetWorkType(WelcomeActivity.this)){
            //先弹出对话框提示
            //使用隐式Intent打开网络设置程序

            Toast.makeText(WelcomeActivity.this, "网络不可用，请检查！", Toast.LENGTH_LONG).show();
            return ;
        }*/
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        WelcomeActivity.this.finish();
    }

}
