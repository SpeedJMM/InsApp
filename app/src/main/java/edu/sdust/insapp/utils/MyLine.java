package edu.sdust.insapp.utils;

import java.util.List;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by Administrator on 2017/11/3.
 */

public class MyLine extends Line {

    public MyLine() {
        super();
        init();
    }

    public MyLine(List<PointValue> values) {
        super(values);
        init();
    }

    public MyLine(Line line) {
        super(line);
        init();
    }

    public void hide(){
        this.setHasLabels(false).setHasPoints(false).setHasLines(false);
    }

    public void show(){
        this.setHasLabels(true).setHasPoints(true).setHasLines(true);
    }

    private void init(){
        this.setStrokeWidth(1);
        this.setPointRadius(2);
        this.setCubic(false);
        this.setHasLabels(true);
    }
}
