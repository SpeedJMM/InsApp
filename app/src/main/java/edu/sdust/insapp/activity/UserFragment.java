package edu.sdust.insapp.activity;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import edu.sdust.insapp.R;
import edu.sdust.insapp.utils.DbConstant;
import edu.sdust.insapp.utils.DbManager;
import edu.sdust.insapp.utils.OrderDBHelper;

/**
 * Created by chenhw on 2017/12/19.
 */

public class UserFragment extends Fragment {
    private TextView tv_frag_me_add;
    private TextView tv_frag_me_mesg;
    private TextView tv_frag_me_setting;
    private TextView tv_frag_me_name;
    private TextView tv_frag_me_loginout;
    private OrderDBHelper helper;
    private SQLiteDatabase db;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);
        helper = DbManager.getInstance(getActivity());
        return view;
    }

    public void initView(View mRootView) {
        tv_frag_me_add = (TextView) mRootView.findViewById(R.id.tv_frag_me_add);
        tv_frag_me_mesg = (TextView) mRootView.findViewById(R.id.tv_frag_me_mesg);
        tv_frag_me_setting = (TextView) mRootView.findViewById(R.id.tv_frag_me_setting);
        tv_frag_me_name = (TextView) mRootView.findViewById(R.id.tv_frag_me_name);
        tv_frag_me_loginout = (TextView) mRootView.findViewById(R.id.tv_frag_me_loginout);
        setListener();
    }

    private void setListener() {
        tv_frag_me_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),UserInformationActivity.class);
                startActivity(intent);
            }
        });
        tv_frag_me_mesg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tv_frag_me_loginout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     Logout();
                     UserFragment.this.getActivity().finish();
                    }
                }

        );
    }

    private void Logout() {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try{
            db.execSQL("delete from " + DbConstant.USER_TABLE);
            db.execSQL("delete from " + DbConstant.POINT_TABLE);
            db.execSQL("delete from " + DbConstant.DEVICE_TABLE);
            db.execSQL("delete from " + DbConstant.DEVICE_FAULT_TABLE);
            db.execSQL("delete from " + DbConstant.NON_DEVICE_FAULT_TABLE);
            db.execSQL("delete from " + DbConstant.PICTURE_TABLE);
            db.execSQL("delete from " + DbConstant.COMPMAC_TABLE);
            db.execSQL("delete from " + DbConstant.POSITION_TABLE);
            db.execSQL("delete from " + DbConstant.ELECINSPENCLO_TABLE);
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
            db.close();
        }
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
