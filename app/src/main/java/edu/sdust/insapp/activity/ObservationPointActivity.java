package edu.sdust.insapp.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.sdust.insapp.R;

public class ObservationPointActivity extends AppCompatActivity {
    @BindView(R.id.lv_observation_point_info)
    ListView listView;

    private String[] commonFunList = new String[]{

            "温度 12",
            "震动 12",
            "加速度 0.2",
            "位移 50"
    };
    private String[] listKey = new String[]{
            "温度",
            "震动",
            "加速度",
            "位移"
    };
    private String[] listValue = new String[]{
            "12",
            "12",
            "0.2",
            "50"
    };
    private List<Map<String,Object>> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_point);

        ButterKnife.bind(this);
        data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"point", "info"};
        int[] to = {R.id.point_info_key, R.id.point_info_value};
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commonFunList));
        listView.setAdapter(new SimpleAdapter(this, data_list, R.layout.point_info, from, to));
    }

    public List<Map<String, Object>> getData(){


        for(int i = 0; i<listKey.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("point", listKey[i]);
            map.put("info", listValue[i]);
            data_list.add(map);
        }

        return data_list;
    }
}
