package edu.sdust.insapp.utils;

import android.graphics.Color;

/**
 * Created by Administrator on 2017/11/3.
 */

public class ColorFactory {
    static int  colors[] = {
            Color.BLACK,
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.YELLOW,
            Color.DKGRAY,
            Color.GRAY,
            Color.LTGRAY,
            Color.MAGENTA,
            Color.CYAN
    };

    int mCursor = 0;


    /**
     *
     * @param cursor 要获得的颜色在列表中的位置
     * @return 颜色(如果cursor超出范围，则必定返回列表中第0个颜色)
     */
    public static int getColor(int cursor){
        if(cursor >= colors.length)cursor = 0;

        return colors[cursor];
    }

    /**
     * 按列表返回下一个颜色
     * @return 返回下一个颜色，如果调用次数超过列表长度，则会重新循环
     */
    public int nextColor(){
        if(mCursor >= colors.length) mCursor =0;

        return colors[mCursor++];
    }
}
