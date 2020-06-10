package edu.sdust.insapp.utils;

import android.graphics.Color;

import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;

/**
 * Created by Administrator on 2017/11/3.
 */

public class MyAxis extends Axis {
    private static int LINE_COLOR = Color.BLACK;
    private static int TEXT_COLOR = Color.BLACK;
    public MyAxis() {
        super();
        init(false);
    }

    public MyAxis(boolean needDateFormat){
        super();
        init(needDateFormat);
    }

    public MyAxis(List<AxisValue> values) {
        super(values);
        init(false);
    }


    public MyAxis(List<AxisValue> values,boolean needDateFormat) {
        super(values);
        init(needDateFormat);
    }

    public MyAxis(Axis axis) {
        super(axis);
        init(false);
    }

    private void init(boolean needDateFormat){
        if(needDateFormat)
            this.setFormatter(MyDateFormatter.getInstance());

        this.setTextColor(TEXT_COLOR);
        this.setLineColor(LINE_COLOR);
        this.setHasLines(true);
        this.setHasTiltedLabels(true);

    }
}
