package com.example.module_common.device;

import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.example.module_common.BaseApplication;
import com.example.module_common.utils.ChannelUtils;
import com.example.module_common.utils.DeviceUtils;
import com.zhenai.lib.ZACodeUtils;

import java.util.Arrays;
import java.util.UUID;

public class DeviceInfoManager {
    private static DeviceInfoManager instance;
    private static final String EMPTY = "";
    private static final String SEPARATOR = "/";
    private static final String COMMA = ",";

    private DeviceInfoManager() {

    }

    public static synchronized DeviceInfoManager getInstance() {
        if (instance == null) {
            instance = new DeviceInfoManager();
        }
        return instance;
    }

    /**
     * app名字
     */
    private static final String appName = "quyuehui";
    /**
     * App版本
     */
    private String versionName;
    /**
     * 系统平台
     */
    public static final int SYSTEM_PLATFORM = 17;
    /**
     * 系统版本
     */
    private String systemVersion;
    /**
     * 手机型号
     */
    private String phoneModel;
    /**
     * wifi mac地址
     */
    private String macAddress;
    /**
     * 渠道号
     */
    private String channelId;
    /**
     * 子渠道号
     */
    private String subChannelId;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 屏幕高度
     */
    private int screenHeightPixels;
    /**
     * 屏幕宽度
     */
    private int screenWidthPixels;
    /**
     * deviceId 唯一识别
     */
    private String deviceId;
    private String imei;
    private String oaId;

    public String getAppName() {
        return appName;
    }

    public String getVersionName() {
        if (TextUtils.isEmpty(versionName)) {
            versionName = BuildConfig.VERSION_NAME;
        }
        return versionName;
    }

    public int getSystemPlatform() {
        return SYSTEM_PLATFORM;
    }

    public String getSystemVersion() {
        if (TextUtils.isEmpty(systemVersion)) {
            systemVersion = Build.VERSION.RELEASE;
        }
        return systemVersion;
    }

    public String getPhoneModel() {
        if (TextUtils.isEmpty(phoneModel)) {
            phoneModel = Build.BRAND + "_" + Build.MODEL;
        }
        return phoneModel;
    }

    public String getMacAddress(boolean isAddIMEI) {
        if (!isAddIMEI) {
            return EMPTY;
        }

        if (TextUtils.isEmpty(macAddress)) {
            macAddress = DeviceUtils.getLocalMacAddress(BaseApplication.getContext());
        }
        return macAddress;
    }

    public String getMainChannelId() {
        if (TextUtils.isEmpty(channelId)) {
            channelId = ChannelUtils.getMainChannel(BaseApplication.getContext());
        }
        return channelId;
    }

    public String getSubChannelId() {
        if (TextUtils.isEmpty(subChannelId)) {
            subChannelId = ChannelUtils.getSubChannel(BaseApplication.getContext());
        }
        return subChannelId;
    }

    public void setChannel(String channelId, String subChannelId) {
        if (!TextUtils.isEmpty(channelId)) {
            this.channelId = channelId;
        }
        if (!TextUtils.isEmpty(subChannelId)) {
            this.subChannelId = subChannelId;
        }
    }

    public String getPackageName() {
        if (TextUtils.isEmpty(packageName)) {
            packageName = BaseApplication.getContext().getPackageName();
        }
        return packageName;
    }

    public int getScreenHeightPixels() {
        if (screenHeightPixels == 0) {
            screenHeightPixels = DeviceUtils.getScreenDisplayMetrics(BaseApplication.getContext()).heightPixels;
        }
        return screenHeightPixels;
    }

    public int getScreenWidthPixels() {
        if (screenWidthPixels == 0) {
            screenWidthPixels = DeviceUtils.getScreenDisplayMetrics(BaseApplication.getContext()).widthPixels;
        }
        return screenWidthPixels;
    }

    public String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public int getNetworkType() {
        return DeviceUtils.getNetworkType(BaseApplication.getContext());
    }

    public int getNetworkType(NetworkInfo networkInfo) {
        return DeviceUtils.getNetworkType(networkInfo);
    }

    public String getDeviceId() {
        return getDeviceId(true);
    }

    public String getDeviceId(boolean isAddIMEI) {
        if (!isAddIMEI) {
            return EMPTY;
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = DeviceUtils.getDeviceId(BaseApplication.getContext());
        }
        return deviceId;
    }

    public String getIMEI() {
        if (TextUtils.isEmpty(imei)) {
            imei = DeviceUtils.getIMEI(BaseApplication.getContext());
            if (imei == null) {
                imei = "";
            }
        }
        return imei;
    }


    public void setOaId(String oaId) {
        if (oaId == null) {
            oaId = "";
        }
        this.oaId = oaId;
    }

    /**
     * 广告标识
     *
     * @return
     */
    public String getOAid() {
        return oaId == null ? "" : oaId;
    }

    /**
     * imei 和oaid
     *
     * @return
     */
    private String getADId(boolean isAddIMEI) {
        try {
            return (isAddIMEI ? getIMEI() : EMPTY) + COMMA + getOAid();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isA(int x) {
        if(x < 0) {
            return false;
        }
        String str = String.valueOf(x);
        char[] chars = str.toCharArray();
        char[] newChars = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            newChars[i] = chars[chars.length - i - 1];
        }
        String newStr = Arrays.toString(newChars);
        return newStr.equals(str);
    }

    /**
     * 校验码
     *
     * @param randomUUID
     * @return
     */
    public String getCheckCode(String randomUUID, String key) {
        //return "1024";
        return ZACodeUtils.getCheckCode(getVersionName() + getSystemPlatform() + randomUUID, key);
    }

    public String getUAInfo(String key) {
        return getUAInfo(key, true);
    }

    /**
     * 获取ua信息
     * zhanai/App版本/系统平台(Android、ios)/系统版本/手机型号(格式：品牌_型号)/wifi mac/渠道号/子渠道号/
     * 包名/屏幕高度/屏幕宽度/唯一请求id/网络情况网络(0:2G;1:3G;2:4G;3:WIFI;-1:未知)/
     * 唯一识别/校验码/广告标示(imei,oaid)
     *
     * @return ua
     */
    public String getUAInfo(String key, boolean isAddIMEI) {
        StringBuilder sb = new StringBuilder();
        String randomUUID = getRandomUUID();
        sb.append(getAppName()).append(SEPARATOR).append(getVersionName()).append(SEPARATOR)
                .append(getSystemPlatform()).append(SEPARATOR)
                .append(getSystemVersion()).append(SEPARATOR)
                .append(getPhoneModel()).append(SEPARATOR)
                .append(getMacAddress(isAddIMEI)).append(SEPARATOR)
                .append(getMainChannelId()).append(SEPARATOR)
                .append(getSubChannelId()).append(SEPARATOR)
                .append(getPackageName()).append(SEPARATOR)
                .append(getScreenHeightPixels()).append(SEPARATOR)
                .append(getScreenWidthPixels()).append(SEPARATOR)
                .append(randomUUID).append(SEPARATOR)
                .append(getNetworkType()).append(SEPARATOR)
                .append(getDeviceId(isAddIMEI)).append(SEPARATOR)
                .append(getCheckCode(randomUUID, key)).append(SEPARATOR)//TODO:
                .append(getADId(isAddIMEI));
        return sb.toString();
    }


}
