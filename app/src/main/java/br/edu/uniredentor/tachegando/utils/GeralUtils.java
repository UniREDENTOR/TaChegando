package br.edu.uniredentor.tachegando.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
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

    private static String getEndereco(Context context, double lat, double lng) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getData(Long calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
        return format.format(calendar);
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

    public static String getEnderecoCurto(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address obj = addresses.get(0);
            return obj.getThoroughfare();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void getEndereco(Context context, double latitude, double longitude, TextView textViewEndereco, boolean curto) {
        new AsyncEndereco(context, latitude, longitude, textViewEndereco, curto).execute();
    }


    public static boolean esconderTeclado(ImageView imageView, Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(imageView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return true;
    }

    public static String getIdDoUsuario(){
        try{
            return FirebaseUtils.getIdUsuario();
        }catch (Exception e){
            return "1";
        }

    }

    static class AsyncEndereco extends AsyncTask<Void, Void, String> {

        private final boolean curto;
        Context context;
        double latitude, longitude;
        TextView textView;

        public AsyncEndereco(Context context, double latitude, double longitude, TextView textView, boolean curto){
            this.context = context;
            this.latitude = latitude;
            this.longitude = longitude;
            this.textView = textView;
            this.curto = curto;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String endereco = "";
            if(curto){
                endereco = getEnderecoCurto(context, latitude, longitude);
            }else {
                endereco = getEndereco(context, latitude, longitude);
            }
            return endereco;
        }

        @Override
        protected void onPostExecute(String endereco) {
            super.onPostExecute(endereco);
            textView.setText(textView.getText() + endereco);
        }
    }
}
