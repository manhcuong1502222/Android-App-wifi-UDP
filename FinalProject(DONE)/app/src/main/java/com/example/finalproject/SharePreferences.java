package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferences {
    private static final String PREF_NAME = "MyAppPreferences"; // Tên của SharedPreferences
    private static final String ROOMS_PREFERENCE_KEY = "rooms_preference_key"; // Khóa lưu trữ danh sách phòng
    private static final String DEVICES_PREFERENCE_KEY = "devices_preference_key"; // Khóa lưu trữ danh sách thiết bị

    // Lưu danh sách phòng vào SharedPreferences
    public static void saveRooms(Context context, String roomsJson) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ROOMS_PREFERENCE_KEY, roomsJson); // Lưu danh sách phòng dưới dạng JSON
        editor.apply(); // Áp dụng thay đổi
    }

    // Tải danh sách phòng từ SharedPreferences
    public static String loadRooms(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(ROOMS_PREFERENCE_KEY, ""); // Trả về danh sách phòng dưới dạng JSON
    }
}
