package com.xmu.rtsp;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;

import static android.content.Context.MODE_PRIVATE;
import static android.content.SharedPreferences.Editor;
import static org.joda.time.DateTime.now;

public class Configuration {

    public static final String PREFERENCES_WIFI_CONFIGURATION = "WifiConfiguration";
    public static final String PREFERENCES_LOGIN_TIME = "LoginTime";
    public static final String PREFERENCES_PN = "PN";
    public static final String PREFERENCES_NEED_UPLOAD_DEVICE_INFO_TODAY = "UploadDeviceInfoToday";
    private static final String KEY_LAST_LOGIN_TIME_IN_MILLIS = "LastLoginTimeInMillis";
    private static final String KEY_CURRENT_LOGIN_TIME_IN_MILLIS = "CurrentLoginTimeInMillis";
    public static final int DEFAULT_LOGIN_RETRY_MAX_COUNT = Integer.MAX_VALUE;

    private static final String KEY_WIFI_SSID = "wifi_ssid";
    private static final String DEFAULT_WIFI_SSID = "";
    private static final String KEY_WIFI_PWD = "wifi_pwd";
    private static final String DEFAULT_WIFI_PWD = "";
    private static final String KEY_PN_TOKEN = "pn_token";

    private static final String KEY_NEED_UPLOAD_DEVICE_INFO = "upload_device_info";

    public static final String LOAD_STATUS = "load_status";
    public static final String LOAD_STATUS_INFO = "is_loading";
    public static final String APP_PREVIEWS = "app_previews";
    public static final String THROTTLE_STATUS = "throttle_status";
    public static final String POSITION_UP = "position_up";
    public static final String POSITION_LEFT = "POSITION_LEFT";
    private static String POSITION_DOWN = "POSITION_DOWN";
    private static String POSITION_RIGHT = "POSITION_RIGHT";
    private static String POSITION_FORWARD = "POSITION_FORWARD";
    private static String POSITION_BACK = "POSITION_BACK";

    private static Editor getEditor(Context applicationContext, String preferencesName) {
        SharedPreferences preferences = applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE);
        return preferences.edit();
    }

    private static SharedPreferences getPreferences(Context applicationContext, String preferencesName) {
        return applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE);
    }

    private static long fourAmOfToday() {
        return now().withTime(4, 0, 0, 0).getMillis();
    }

    private static long fourAmOfYesterday() {
        return now().minusDays(1).withTime(4, 0, 0, 0).getMillis();
    }

    private static long zeroAmOfToday() {
        return now().withTime(0, 0, 0, 0).getMillis();
    }

    public static boolean isFirstTimeUseAppToday(Context context) {
        return beforeFourAmOfSpecifiedDay(context, KEY_LAST_LOGIN_TIME_IN_MILLIS);
    }

    public static boolean isCurrentLoginExpired(Context context) {
        return beforeFourAmOfSpecifiedDay(context, KEY_CURRENT_LOGIN_TIME_IN_MILLIS);
    }

    private static boolean beforeFourAmOfSpecifiedDay(Context context, String keyOfLoginTime) {
        SharedPreferences lastUsedTime = getPreferences(context, PREFERENCES_LOGIN_TIME);
        DateTime lastLoginTime = new DateTime(lastUsedTime.getLong(keyOfLoginTime, 0l));
        boolean beforeFourAmOfToday = now().isAfter(zeroAmOfToday()) && now().isBefore(fourAmOfToday());
        long target = beforeFourAmOfToday ? fourAmOfYesterday() : fourAmOfToday();
        return lastLoginTime.isBefore(target);
    }

    public static void updateLoginTime(Context context, DateTime dateTime) {
        updateLastLoginTime(context);
        updateCurrentLoginTime(context, dateTime);
    }

    public static void updateLastLoginTime(Context context) {
        SharedPreferences loginTimePreferences = getPreferences(context, PREFERENCES_LOGIN_TIME);
        long currentLoginTimeLong = loginTimePreferences.getLong(KEY_CURRENT_LOGIN_TIME_IN_MILLIS, 0l);

        Editor editor = getEditor(context, PREFERENCES_LOGIN_TIME);
        editor.putLong(KEY_LAST_LOGIN_TIME_IN_MILLIS, currentLoginTimeLong);
        editor.commit();
    }

    private static void updateCurrentLoginTime(Context context, DateTime dateTime) {
        Editor editor = getEditor(context, PREFERENCES_LOGIN_TIME);
        editor.putLong(KEY_CURRENT_LOGIN_TIME_IN_MILLIS, dateTime != null ? dateTime.getMillis() : 0);
        editor.commit();
    }

    public static boolean hasSetWifi(Context context) {
        return !getWifiSsid(context).equals(DEFAULT_WIFI_SSID);
    }

    public static void setWifiSsid(Context applicationContext, String wifiSSID) {
        Editor editor = getEditor(applicationContext, PREFERENCES_WIFI_CONFIGURATION);
        editor.putString(KEY_WIFI_SSID, wifiSSID);
        editor.commit();
    }

    public static void setWifiPwd(Context applicationContext, String pwd) {
        Editor editor = getEditor(applicationContext, PREFERENCES_WIFI_CONFIGURATION);
        editor.putString(KEY_WIFI_PWD, pwd);
        editor.commit();
    }

    public static String getWifiSsid(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, PREFERENCES_WIFI_CONFIGURATION);
        return preferences.getString(KEY_WIFI_SSID, DEFAULT_WIFI_SSID);
    }

    public static String getWifiPwd(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, PREFERENCES_WIFI_CONFIGURATION);
        return preferences.getString(KEY_WIFI_PWD, DEFAULT_WIFI_PWD);
    }

    public static String getPnToken(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, PREFERENCES_PN);
        return preferences.getString(KEY_PN_TOKEN, "");
    }

    public static void setPnToken(Context applicationContext, String token) {
        Editor editor = getEditor(applicationContext, PREFERENCES_PN);
        editor.putString(KEY_PN_TOKEN, token);
        editor.commit();
    }

    public static void setNeedUploadDeviceInfo(Context applicationContext, boolean isNeed) {
        Editor editor = getEditor(applicationContext, PREFERENCES_NEED_UPLOAD_DEVICE_INFO_TODAY);
        editor.putBoolean(KEY_NEED_UPLOAD_DEVICE_INFO, isNeed);
        editor.commit();
    }

    public static void updateLoadStatus(Context applicationContext, boolean isLoading) {
        Editor editor = getEditor(applicationContext, LOAD_STATUS);
        editor.putBoolean(LOAD_STATUS_INFO, isLoading);
        editor.commit();
    }

    public static void keepAppPreview(Context applicationContext, String appId, String previews) {
        Editor editor = getEditor(applicationContext, APP_PREVIEWS);
        editor.putString(appId, previews);
        editor.commit();
    }

    public static boolean getLoadingStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, LOAD_STATUS);
        return preferences.getBoolean(LOAD_STATUS_INFO, false);
    }

    public static String getAppPreviews(Context applicationContext, String appId) {
        SharedPreferences preferences = getPreferences(applicationContext, APP_PREVIEWS);
        return preferences.getString(appId, "");
    }

    public static void keepThrottleStatus(Context context, int progress) {
        Editor editor = getEditor(context, THROTTLE_STATUS);
        editor.putInt(THROTTLE_STATUS, progress);
        editor.commit();
    }

    public static int getThrottleStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, THROTTLE_STATUS);
        return preferences.getInt(THROTTLE_STATUS, 0);
    }

    public static void keepPositionUpCmd(Context context, boolean isUp) {
        Editor editor = getEditor(context, POSITION_UP);
        editor.putBoolean(POSITION_UP, isUp);
        editor.commit();
    }

    public static boolean getPositionUpStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_UP);
        return preferences.getBoolean(POSITION_UP, false);
    }

    public static void keepPositiondownCmd(Context context, boolean isDown) {
        Editor editor = getEditor(context, POSITION_DOWN);
        editor.putBoolean(POSITION_DOWN, isDown);
        editor.commit();
    }

    public static boolean getPositionDownStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_DOWN);
        return preferences.getBoolean(POSITION_DOWN, false);
    }

    public static void keepPositionLeftCmd(Context context, boolean isLeft) {
        Editor editor = getEditor(context, POSITION_LEFT);
        editor.putBoolean(POSITION_LEFT, isLeft);
        editor.commit();
    }

    public static boolean getPositionLeftStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_LEFT);
        return preferences.getBoolean(POSITION_LEFT, false);
    }


    public static void keepPositionRightCmd(Context context, boolean isRight) {
        Editor editor = getEditor(context, POSITION_RIGHT);
        editor.putBoolean(POSITION_RIGHT, isRight);
        editor.commit();
    }

    public static boolean getPositionRightStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_RIGHT);
        return preferences.getBoolean(POSITION_RIGHT, false);
    }

    public static void keepPositionForwardCmd(Context context, boolean isGoAhead) {
        Editor editor = getEditor(context, POSITION_FORWARD);
        editor.putBoolean(POSITION_FORWARD, isGoAhead);
        editor.commit();
    }

    public static boolean getPositionForwardStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_FORWARD);
        return preferences.getBoolean(POSITION_FORWARD, false);
    }


    public static void keepPositionBackCmd(Context context, boolean isBack) {
        Editor editor = getEditor(context, POSITION_BACK);
        editor.putBoolean(POSITION_BACK, isBack);
        editor.commit();
    }

    public static boolean getPositionBackStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_BACK);
        return preferences.getBoolean(POSITION_BACK, false);
    }


}