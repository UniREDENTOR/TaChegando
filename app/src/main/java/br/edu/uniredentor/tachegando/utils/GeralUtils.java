package br.edu.uniredentor.tachegando.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.edu.uniredentor.tachegando.activity.LoginPassageiroActivity;

public class GeralUtils {
    public static void show(String s) {
        Log.v("tachegando", s);
    }

    public static void mostraImagemCircular(Context context, ImageView imageView, String url){
        Glide.with(context).load(url).circleCrop().into(imageView);
    }

    public static String getEndereco(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);
            add = add + "," + obj.getAdminArea();
            add = add + "," + obj.getCountryName();

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getData(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
        return format.format(calendar.getTime());
    }

    public static String getDataFirebase(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM");
        return format.format(calendar.getTime());
    }

    public static String getHorario(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(calendar.getTime());
    }

    public static void mostraMensagem(FragmentActivity activity, String mensagem) {
        Toast.makeText(activity, mensagem, Toast.LENGTH_LONG).show();
    }

    public static boolean ehUsuario(Activity activity) {
        boolean user = FirebaseUtils.usuarioCadastrado();
        if(!user){
            activity.startActivity(new Intent(activity, LoginPassageiroActivity.class));
            return false;
        }
        return true;
    }

    public static void mostraAlerta(String titulo, String mensagem, Context context) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(context);
        alerta.setTitle(titulo).setMessage(mensagem).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    public String getLocalizacaoPeloEndereco(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            return lat + "," + lng;
        } catch (Exception e) {
            return null;
        }
    }
    public static boolean esconderTeclado(ImageView imageView, Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(imageView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return true;
    }

    public static String getIdDoUsuario(){
        return FirebaseUtils.getAuth().getCurrentUser().getUid();
    }
}
