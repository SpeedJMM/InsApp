package edu.sdust.insapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import edu.sdust.insapp.R;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyVisualView extends View {
    //**************绘制坐标轴*****************
    private int mXPoint; //原点的X坐标
    private int mYPoint; //原点的Y坐标
    private int mXScale=70; //X的刻度长度
    private int mYScale=50; //Y的刻度长度
    private int XLength=920; //X轴的长度
    private int YLength=650; //Y轴的长度
    private String[] XLabel; //X的刻度
    private String[] YLabel; //Y的刻度
    private String[] mData; //数据
    private String Title; //显示的标题
    private int mSpectrumNum = 64;// 傅里叶变换之后是对称的，只截取前一半

    private short[] shortsArray;
    private float[] mPoints;
    private Rect mRect = new Rect();

    private Paint mForePaint = new Paint();
    private Paint linesPaint = new Paint();// 频谱线画笔
    private Paint paintAxis  = new Paint();//坐标轴
    private Paint paintTitle = new Paint();

    public MyVisualView(Context context) {
        super(context);
        init();
    }

    public MyVisualView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void SetInfo(String[] XLabel, String[] YLable, String[] mData, String Title) {
        this.XLabel = XLabel;
        this.YLabel = YLable;
        this.mData = mData;
        this.Title = Title;
        mXPoint=35;
        mYPoint=650;
    }

    private void init() {
        shortsArray = null;


        paintAxis.setStyle(Paint.Style.STROKE);
        paintAxis.setAntiAlias(true);//去锯齿
        paintAxis.setColor(Color.DKGRAY);//颜色parseColor("#ff02f2")
        paintAxis.setTextSize(16);  //设置文字大小


        paintTitle.setStyle(Paint.Style.STROKE);
        paintTitle.setAntiAlias(true);//去锯齿
        paintTitle.setColor(Color.DKGRAY);
        paintTitle.setTextSize(30);


        mForePaint.setStrokeWidth(5f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(getResources().getColor(R.color.info_button_color));

        linesPaint = new Paint();
        linesPaint.setStrokeWidth(13f);
        linesPaint.setAntiAlias(true);
        linesPaint.setColor(getResources().getColor(R.color.info_button_color));//柱状图颜色
    }

    private byte type = 2;
   /* @Override
    public boolean onTouchEvent(MotionEvent me)
    {
        // 当用户触碰该组件时，切换波形类型
        if(me.getAction() != MotionEvent.ACTION_DOWN)
        {
            return false;
        }
        type ++;
        if(type >= 3)
        {
            type = 1;
        }
        return true;
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (shortsArray == null) {
            return;
        }

        //*******************绘制坐标轴*******************

        //设置Y轴
        canvas.drawLine(mXPoint, mYPoint,mXPoint, mYPoint-YLength,  paintAxis);   //轴线
        for(int i=0;i*mYScale<YLength ;i++)
        {
            canvas.drawLine(mXPoint,mYPoint-i*mYScale, mXPoint+5, mYPoint-i*mYScale, paintAxis);  //刻度
            try
            {
                canvas.drawText(YLabel[i] , mXPoint-28, mYPoint-i*mYScale+5, paintAxis);  //文字
            }
            catch(Exception e)
            {
            }
        }
        canvas.drawLine(mXPoint,mYPoint-YLength,mXPoint-3,mYPoint-YLength+6,paintAxis);  //箭头
        canvas.drawLine(mXPoint,mYPoint-YLength,mXPoint+3,mYPoint-YLength+6,paintAxis);
        //设置X轴
        canvas.drawLine(mXPoint,mYPoint,mXPoint+XLength,mYPoint,paintAxis);   //轴线
        for(int i=0;i*mXScale<XLength;i++)
        {
            canvas.drawLine(mXPoint+i*mXScale, mYPoint, mXPoint+i*mXScale, mYPoint-5, paintAxis);  //刻度
            try
            {
                canvas.drawText(XLabel[i] , mXPoint+i*mXScale-10, mYPoint+20, paintAxis);  //文字
            }
            catch(Exception e)
            {
            }
        }
        canvas.drawLine(mXPoint+XLength,mYPoint,mXPoint+XLength-6,mYPoint-3,paintAxis);    //箭头
        canvas.drawLine(mXPoint+XLength,mYPoint,mXPoint+XLength-6,mYPoint+3,paintAxis);
        canvas.drawText(Title, 400, 50, paintTitle);

        mRect.set(0,0,getWidth()-50,getHeight());
        switch(type) {
            // -------绘制柱状的波形图-------
            //暂时保留 还没做好
            case 1:
                for (int i = 0; i < shortsArray.length - 1; i++) {
                    float left = mRect.width() * i / (shortsArray.length - 1);
                    // 根波形值计算该矩形的高度
                    float top = mRect.height()/2 - (byte) (shortsArray[i + 1] + 128)
                            * (mRect.height()/2) / 128;
                    float right = left + 6;
                    float bottom = mRect.height()/2;
                    canvas.drawRect(left, top, right, bottom, linesPaint);
                }
                break;
            // -------绘制曲线波形图-------
            case 2:

                // 如果point数组还未初始化
                if (mPoints == null || mPoints.length < mSpectrumNum * 4) {
                    mPoints = new float[mSpectrumNum * 4];
                }

                for (int i = 1; i < mSpectrumNum  ; i++) {
                    // 计算第i个点的x坐标
                    mPoints[i * 4] = mRect.width() * i / mSpectrumNum + 35;
                    // 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
                    mPoints[i * 4 + 1] = mRect.height() - 40
                            - shortsArray[i] ;//* mRect.height()  / 512
                    // 计算第i+1个点的x坐标
                    mPoints[i * 4 + 2] = mRect.width() * (i + 1) / mSpectrumNum + 35;
                    // 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
                    mPoints[i * 4 + 3] = mRect.height() - 40
                            - shortsArray[i + 1];
                }
                // 绘制波形曲线
                canvas.drawLines(mPoints, mForePaint);
                break;
        }
    }
    public void updateVisualizer(Complex[] fft) {

        short[] model = new short[fft.length ];

        for (int i = 0; i < model.length; i++) {
            model[i] = (short) fft[i].getMod();
            Log.d("MyVisualView",(int)model[i]+"");
        }

        shortsArray = model;
        invalidate();
    }
}
