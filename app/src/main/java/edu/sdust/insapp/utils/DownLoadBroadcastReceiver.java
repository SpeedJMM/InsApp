package edu.sdust.insapp.utils;


import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2017/12/13.
 */

public class DownLoadBroadcastReceiver extends BroadcastReceiver {
    @Override
    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        long myDwonloadID = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        Log.i("下载完成 ID = " , String.valueOf(myDwonloadID));
        SharedPreferences sPreferences = context.getSharedPreferences(
                "downloadplato", 0);
        long refernece = sPreferences.getLong("plato", 0);
        Log.i("refernece", String.valueOf(refernece));
        Log.i("myDwonloadID", String.valueOf(myDwonloadID));
        if (refernece == myDwonloadID) {
            String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager dManager = (DownloadManager) context
                    .getSystemService(serviceString);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager
                    .getUriForDownloadedFile(myDwonloadID);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String path = UriToFile.getRealPathFromURI(downloadFileUri, context);
                File apkfile = new File(path);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    Uri appPath = FileProvider.getUriForFile(context, "edu.sdust.insapp.fileprovider", apkfile);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.setDataAndType(appPath, "application/vnd.android.package-archive");
                }else{
                    install.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
                }

            }else{
                install.setDataAndType(downloadFileUri,
                        "application/vnd.android.package-archive");
            }
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }

    }
}
