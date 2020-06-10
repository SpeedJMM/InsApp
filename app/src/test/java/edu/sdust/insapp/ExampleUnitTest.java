package edu.sdust.insapp;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.List;

import edu.sdust.insapp.bluetooth.ObservationsPoint;
import edu.sdust.insapp.utils.DataProcessing;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void convertData(){
        String a = "0100c80bc20d667eea00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        List<ObservationsPoint> observationsPoints = DataProcessing.convertBleDataToList(a);
        String s = new Gson().toJson(observationsPoints);
        System.out.println("s = " + s);
    }

    @Test
    public void subString(){
        String a = "012345";
        String b = a.substring(0,2);
        String c = a.substring(2,3);
    }

    @Test
    public void hex(){
        String string = "1000";
        int length = Integer.parseInt(string.substring(2,4) + string.substring(0,2),16);
        System.out.println("length = " + length);
    }
}