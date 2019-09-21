package br.edu.uniredentor.tachegando.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GeralUtils {
    public static void show(String s) {
        Log.v("tachegando", s);
    }

    public static void mostraImagemCircular(Context context, ImageView imageView, String url){
        Glide.with(context).load(url).circleCrop().into(imageView);
    }
}
