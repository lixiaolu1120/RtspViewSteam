package com.xmu.rtsp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static android.content.SharedPreferences.Editor;

public class Configuration {

    public static final String THROTTLE_STATUS = "throttle_status";
    public static final String POSITION_UP = "POSITION_UP";
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

    public static void keepPositionDownCmd(Context context, boolean isDown) {
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