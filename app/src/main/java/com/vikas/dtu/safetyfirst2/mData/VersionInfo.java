package com.vikas.dtu.safetyfirst2.mData;

/**
 * Created by Vikas on 08-04-2017.
 */

public class VersionInfo {
    public String versionName;
    public int versionCode;
    public String appURL;

    public VersionInfo(){}
    public VersionInfo(String versionName, int versionCode, String appURL) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.appURL = appURL;
    }

public String getVersionName(){
    return versionName;
}

public int getVersionCode(){
    return versionCode;
}

    public String getAppURL() {
        return appURL;
    }
}
