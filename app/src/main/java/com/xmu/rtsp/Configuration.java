package com.xmu.rtsp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static android.content.SharedPreferences.Editor;

public class Configuration {

    public static final String THROTTLE_STATUS = "throttle_status";
    public static final String POSITION_LEFT_RIGHT = "POSITION_LEFT_RIGHT";
    private static String POSITION_MOVE_FORWARD = "POSITION_MOVE_FORWARD";
    private static String POSITION_FORWARD_BACK = "POSITION_FORWARD_BACK";

    private static Editor getEditor(Context applicationContext, String preferencesName) {
        SharedPreferences preferences = applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE);
        return preferences.edit();
    }

    private static SharedPreferences getPreferences(Context applicationContext, String preferencesName) {
        return applicationContext.getSharedPreferences(preferencesName, MODE_PRIVATE);
    }

    public static void keepThrottleStatus(Context context, int status) {
        Editor editor = getEditor(context, THROTTLE_STATUS);
        editor.putInt(THROTTLE_STATUS, status);
        editor.commit();
    }

    public static int getThrottleStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, THROTTLE_STATUS);
        return preferences.getInt(THROTTLE_STATUS, 50);
    }

    public static void keepLeftRightStatus(Context context, int status) {
        Editor editor = getEditor(context, POSITION_LEFT_RIGHT);
        editor.putInt(POSITION_LEFT_RIGHT, status);
        editor.commit();
    }

    public static int getLeftRightStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_LEFT_RIGHT);
        return preferences.getInt(POSITION_LEFT_RIGHT, 50);
    }

    public static int getTowardLeftRightStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_MOVE_FORWARD);
        return preferences.getInt(POSITION_MOVE_FORWARD, 50);
    }

    public static void keepTowardLeftRightStatus(Context context, int status) {
        Editor editor = getEditor(context, POSITION_MOVE_FORWARD);
        editor.putInt(POSITION_MOVE_FORWARD, status);
        editor.commit();
    }

    public static void keepForwardBackStatus(Context context, int status) {
        Editor editor = getEditor(context, POSITION_FORWARD_BACK);
        editor.putInt(POSITION_FORWARD_BACK, status);
        editor.commit();
    }

    public static int getForwardBackStatus(Context applicationContext) {
        SharedPreferences preferences = getPreferences(applicationContext, POSITION_FORWARD_BACK);
        return preferences.getInt(POSITION_FORWARD_BACK, 50);
    }

}