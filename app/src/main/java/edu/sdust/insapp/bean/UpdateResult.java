package edu.sdust.insapp.bean;

/**
 * Created by 35139 on 2017/11/23.
 */

public class UpdateResult {
    private boolean newVersion;
    private String versionName;
    private int versionCode;
    private String message;
    private String downloadURL;

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }
}
