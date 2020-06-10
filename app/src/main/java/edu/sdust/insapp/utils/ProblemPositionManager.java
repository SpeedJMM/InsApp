package edu.sdust.insapp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ProblemPositionManager {
    private static List<String> positions;

    public static List<String> getInstance(){
        if(positions==null){
            positions = new ArrayList<>();
        }
        return positions;
    }
}
