package edu.sdust.insapp.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import edu.sdust.insapp.R;

/**
 * Created by 35139 on 2017/11/19.
 */

public class DatePickerDialog {

    public static void showDatePickerDialog(final EditText editText, Context context) {
        AlertDialog.Builder datePickerDialog =
                new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_date_picker,null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.dp_global);
        //datePickerDialog.setTitle("时间选择");
        datePickerDialog.setView(dialogView);
        datePickerDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText(datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth());
                    }
                });
        datePickerDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        datePickerDialog.show();
    }
}
