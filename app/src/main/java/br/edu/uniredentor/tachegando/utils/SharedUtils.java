package br.edu.uniredentor.tachegando.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtils {

    private static SharedPreferences sharedPreferences;

    public static void save(String id, Activity activity){
        getShared(activity).edit().putString("viagemId", id).apply();
    }

    public static String getId(Activity activity){
        return getShared(activity).getString("viagemid", "0");
    }

    private static SharedPreferences getShared(Activity activity) {
        return activity.getSharedPreferences("tachegando", Context.MODE_PRIVATE);
    }
}
