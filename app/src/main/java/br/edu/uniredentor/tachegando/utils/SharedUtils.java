package br.edu.uniredentor.tachegando.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import br.edu.uniredentor.tachegando.MapasActivity;

public class SharedUtils {

    private static final String SHARED_NOME= "tachegando";
    private static final String VIAGEM_ID = "viagemId";

    public static void save(String id, Activity activity){
        getShared(activity).edit().putString(VIAGEM_ID, id).apply();
    }

    public static void save(String valor, String id, Activity activity){
        getShared(activity).edit().putString(id, valor).apply();
    }

    public static String getId(Activity activity){
        return getShared(activity).getString(VIAGEM_ID, "");
    }

    private static SharedPreferences getShared(Activity activity) {
        return activity.getSharedPreferences(SHARED_NOME, Context.MODE_PRIVATE);
    }

    public static String getLatitude(MapasActivity activity) {
        return getShared(activity).getString(ConstantsUtils.LATITUDE, "");
    }

    public static String getLongitude(MapasActivity activity) {
        return getShared(activity).getString(ConstantsUtils.LONGITUDE, "");
    }
}
