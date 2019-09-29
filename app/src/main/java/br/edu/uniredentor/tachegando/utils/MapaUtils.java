package br.edu.uniredentor.tachegando.utils;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapaUtils {

    private static final float ZOOM_CAMERA = 17f;

    public static void moveCamera(GoogleMap map, LatLng latLng){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA));
    }

    public static Polyline mostrarTrajeto(GoogleMap mMap, ArrayList<LatLng> locais){
        Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(locais).color(Color.RED));
        return polyline;
    }
}
