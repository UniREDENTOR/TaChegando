package br.edu.uniredentor.tachegando.utils;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import br.edu.uniredentor.tachegando.model.Viagem;

public class MapaUtils {

    private static final float ZOOM_CAMERA = 17f;

    public static void moveCamera(GoogleMap map, LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA));
    }

    public static Polyline mostrarTrajeto(GoogleMap mMap, ArrayList<LatLng> locais) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(locais).color(Color.RED));
        return polyline;
    }

    public static Marker criaMarker(GoogleMap mMap, Viagem viagem) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(viagem.getLatLng()).title(viagem.getNome()));
        marker.setTag(viagem.getIdUsuario());
        return marker;
    }
}
