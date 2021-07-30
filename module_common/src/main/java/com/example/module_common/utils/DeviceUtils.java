package com.example.module_common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

public class DeviceUtils {
    /**
     * 保存mac地址
     */
    private static final String PHONE_ID_ADDRESS = "phone_id_address";


    public static final int NETWORK_TYPE_UNKNOWN = -1;
    public static final int NETWORK_TYPE_2G = 0;
    public static final int NETWORK_TYPE_3G = 1;
    public static final int NETWORK_TYPE_4G = 2;
    public static final int NETWORK_TYPE_WIFI = 3;

    @IntDef(value = {NETWORK_TYPE_UNKNOWN, NETWORK_TYPE_2G, NETWORK_TYPE_3G, NETWORK_TYPE_4G, NETWORK_TYPE_WIFI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkType {


    }

    public static String getIMEI(Context context) {
        try {
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String id = telephonyManager.getDeviceId();
//            if (StringUtils.isEmpty(id) || id.equals("000000000000000")) return null;
//            return telephonyManager.getDeviceId();
            return getUniqueID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //用于在API 29后获取唯一标识符
    //此处使用实例ID
    private static String getUniqueID(){
        return UUID.randomUUID().toString();

    }

    /**
     * 获取设备序列号，会进行 MD5 处理
     *
     * @param context Context
     * @return 优先级 mac->android Id->serial Number->UUID
     */
    public static synchronized String getDeviceId(Context context) {
        if (!StringUtils.isEmpty(PreferenceUtil.getString(context, PHONE_ID_ADDRESS, ""))) {
            return PreferenceUtil.getString(context, PHONE_ID_ADDRESS, "");
        }
        String deviceId = getIMEI(context);
        if (!TextUtils.isEmpty(deviceId)) {
            PreferenceUtil.saveValue(context, PHONE_ID_ADDRESS, deviceId);
            return deviceId;
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getLocalMacAddress(context);
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getAndroidId(context);
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getSerialNumber();
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getUUID(context);
        }
        deviceId = MD5Util.getMD5(deviceId);
        PreferenceUtil.saveValue(context, PHONE_ID_ADDRESS, deviceId);
        return deviceId;
    }


    public static String getLocalMacAddress(Context context) {
        String mac = null;
        try {
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wm.getConnectionInfo();
            mac = wifiInfo.getMacAddress();
            if (!TextUtils.equals(mac, "02:00:00:00:00:00")
                    && !TextUtils.equals(mac, "00:00:00:00:00:00")) {
                return mac;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (interfaces != null && interfaces.hasMoreElements()) {
            NetworkInterface iF = interfaces.nextElement();

            byte[] address = new byte[0];
            try {
                address = iF.getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            if (address == null || address.length == 0) {
                continue;
            }

            StringBuilder buf = new StringBuilder();
            for (byte b : address) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            mac = buf.toString();
            if ("wlan0".equals(iF.getName())) {
                return mac;
            }
        }
        return mac;
    }

    /**
     * 返回屏幕DisplayMetrics
     *
     * @return 当前屏幕DisplayMetrics
     */
    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        WindowManager mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        // 有时候密度出错，所以通过Dpi来判断
        mWm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 网络(0:2G;1:3G;2:4G;3:WIFI;-1:未知)
     *
     * @return
     */
    public static int getNetworkType(NetworkInfo networkInfo) {
        int strNetworkType = NETWORK_TYPE_UNKNOWN;
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = NETWORK_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = NETWORK_TYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = NETWORK_TYPE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = NETWORK_TYPE_4G;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = NETWORK_TYPE_4G;
                        } else if (_strSubTypeName.equals("TDS_HSDPA")) {
                            strNetworkType = NETWORK_TYPE_3G;
                        } else {
                            strNetworkType = NETWORK_TYPE_UNKNOWN;
                        }

                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 网络(0:2G;1:3G;2:4G;3:WIFI;-1:未知)
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return NETWORK_TYPE_UNKNOWN;
        }
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return getNetworkType(networkInfo);
    }

    /**
     * 获取 ANDROID_ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        try {
            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(android_id) || TextUtils.equals(android_id, "9774d56d682e549c")) {
                return null;
            }
            return android_id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSerialNumber() {
        return Build.SERIAL;
    }

    /**
     * 获取 UUID
     *
     * @return
     */
    public static String getUUID(Context context) {
        String uuid = PreferenceUtil.getString(context, "uuid", "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            PreferenceUtil.saveValue(context, "uuid", uuid);
        }
        return uuid;
    }


}
