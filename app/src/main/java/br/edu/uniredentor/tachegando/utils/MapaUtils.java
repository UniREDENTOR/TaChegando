package br.edu.uniredentor.tachegando.utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapaUtils {

    private static final float ZOOM_CAMERA = 17f;

    public static void moveCamera(GoogleMap map, LatLng latLng){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA));
    }
}
