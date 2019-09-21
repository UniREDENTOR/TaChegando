package br.edu.uniredentor.tachegando;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import br.edu.uniredentor.tachegando.fragments.InformacaoOnibusDialogFragment;

public class MapasActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int CODIGO_PERMISSAO = 123;
    private static final float ZOOM_CAMERA = 18f;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocation;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 10000, FASTEST_INTERVAL = 5000; // = 5 seconds
    private LocationCallback locationCallback;
    private Marker meuOnibus;
    private double latitude = -21.200;
    private double longitude = -41.888;
    private ArrayList<LatLng> locais;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        criaDemo();
        if(possuiPermissao()) {
            mapFragment.getMapAsync(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {

                        LatLng latLng = locais.get(contador);
                        meuOnibus.setPosition(latLng);
                        moveCamera(latLng);
                        if(contador == locais.size() - 1){
                            contador = 0;
                        }else{
                            contador++;
                        }
                    }
                }
            };
        }

        new InformacaoOnibusDialogFragment().show(getSupportFragmentManager(), "info");

    }

    private void criaDemo(){
        locais = new ArrayList<>();
        locais.add(new LatLng(-21.209075, -41.886608));
        locais.add(new LatLng(-21.208900, -41.886715));
        locais.add(new LatLng(-21.208635, -41.886994));
        locais.add(new LatLng(-21.208345, -41.887273));
        locais.add(new LatLng(-21.208000, -41.887552));
        locais.add(new LatLng(-21.207686, -41.887865));
        locais.add(new LatLng(-21.207448, -41.888050));
        locais.add(new LatLng(-21.207245, -41.888222));

    }

    private void getMinhaLocalizacao() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        try{
            final Task localizacao = fusedLocation.getLastLocation();
            localizacao.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Location localizacaoAtual = (Location) task.getResult();
                        moveCamera(new LatLng(localizacaoAtual.getLatitude(), localizacaoAtual.getLongitude()));
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        iniciaAtualizacaoDaLocalizacao();
    }


    private boolean possuiPermissao(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            String[] permissoes = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(MapasActivity.this, permissoes, CODIGO_PERMISSAO);
            return false;
        }
        return false;
    }

    private void moveCamera(LatLng latLng){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(latitude, longitude);
        meuOnibus = mMap.addMarker(new MarkerOptions().position(sydney).title("Vinhosa - Cehab"));
        moveCamera(sydney);
        mMap.setMyLocationEnabled(true);
        getMinhaLocalizacao();


    }

    private void iniciaAtualizacaoDaLocalizacao() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        fusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

}
