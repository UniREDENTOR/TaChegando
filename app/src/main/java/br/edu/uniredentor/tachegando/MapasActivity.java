package br.edu.uniredentor.tachegando;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.edu.uniredentor.tachegando.fragments.NovaViagemManualDialogFragment;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;

public class MapasActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int CODIGO_PERMISSAO = 123;
    private static final float ZOOM_CAMERA = 17f;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocation;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 10000, FASTEST_INTERVAL = 5000; // = 5 seconds
    private LocationCallback locationCallback;
    private ArrayList<LatLng> locais;
    private int contador = 0;
    private double latitude, longitude;
    private boolean isCriador = true;
    private ArrayList<LatLng> locais2;
    private ArrayList<Marker> listaDeOnibus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        Toolbar toolbarPrincipal = findViewById(R.id.toolbar_principal);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        criaDemo();
        if(possuiPermissao()) {
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {

                        LatLng latLng = locais.get(contador);
                        Viagem viagem = new Viagem();
                        viagem.setIdUsuario("1");
                        viagem.setId(contador+"");
                        viagem.setNome("Teste");
                        viagem.setLatLng(latLng);
                        FirebaseUtils.salva(viagem);

                        if(contador == locais.size() - 1){
                            contador = 0;
                        }else{
                            contador++;
                        }

                        latLng = locais2.get(contador);
                        viagem = new Viagem();
                        viagem.setIdUsuario("2");
                        viagem.setId((contador-1)+"");
                        viagem.setNome("Teste 2");
                        viagem.setLatLng(latLng);
                        FirebaseUtils.salva(viagem);


                    }
                }
            };
        }

        toolbarPrincipal.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nova_viagem:
                        novaViagemDialog();
                        break;
                }
                return false;
            }
        });


        mapeiaViagens();

    }

    private void novaViagemDialog() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(MapasActivity.this);
        alerta.setTitle("Nova viagem").setMessage("Deseja adicionar uma nova viagem de que forma?").setPositiveButton("QRCode", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Manual", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NovaViagemManualDialogFragment.newInstance(latitude, longitude).show(getSupportFragmentManager(), "novaViagem");
            }
        }).show();

    }

    private void mapeiaViagens(){
        FirebaseUtils.getBanco().collection("viagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                List<Viagem> viagens = queryDocumentSnapshots.toObjects(Viagem.class);

                for(Viagem viagem : viagens){
                    if(existe(viagem)){
                        getOnibus(viagem).setPosition(viagem.getLatLng());
                    }else{
                        Marker marker = mMap.addMarker(new MarkerOptions().position(viagem.getLatLng()).title(viagem.getNome()));
                        marker.setTag(viagem.getIdUsuario());
                        listaDeOnibus.add(marker);
                    }
                }

            }
        });
    }

    private boolean existe(Viagem viagem) {
        return getOnibus(viagem) != null;
    }

    private Marker getOnibus(Viagem viagem) {

        for(Marker marker : listaDeOnibus){
            if(marker.getTag().toString().equalsIgnoreCase(viagem.getIdUsuario())){
                return marker;
            }
        }
        return null;
    }

    private void criaDemo(){
        locais = new ArrayList<>();
        locais2 = new ArrayList<>();
        locais.add(new LatLng(-21.209075, -41.886608));
        locais.add(new LatLng(-21.208900, -41.886715));
        locais.add(new LatLng(-21.208635, -41.886994));
        locais.add(new LatLng(-21.208345, -41.887273));
        locais.add(new LatLng(-21.208000, -41.887552));
        locais.add(new LatLng(-21.207686, -41.887865));
        locais.add(new LatLng(-21.207448, -41.888050));
        locais.add(new LatLng(-21.207245, -41.888222));

        locais2.add(new LatLng(-21.207454, -41.888480));
        locais2.add(new LatLng(-21.207982, -41.888006));
        locais2.add(new LatLng(-21.208655, -41.887408));
        locais2.add(new LatLng(-21.209291, -41.886664));
        locais2.add(new LatLng(-21.207454, -41.888480));
        locais2.add(new LatLng(-21.207982, -41.888006));
        locais2.add(new LatLng(-21.208655, -41.887408));
        locais2.add(new LatLng(-21.209291, -41.886664));


    }

    private void getMinhaLocalizacao() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        try{
            final Task localizacao = fusedLocation.getLastLocation();
            localizacao.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && localizacao != null){
                        Location localizacaoAtual = (Location) task.getResult();
                        if(isCriador){

                            latitude = localizacaoAtual.getLatitude();
                            longitude = localizacaoAtual.getLongitude();
                            moveCamera(localizacaoAtual);
                        }
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


        iniciaAtualizacaoDaLocalizacao();
    }

    private void moveCamera(Location localizacaoAtual) {
        LatLng latLng = new LatLng(localizacaoAtual.getLatitude(), localizacaoAtual.getLongitude());
        moveCamera(latLng);
    }


    private boolean possuiPermissao(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            String[] permissoes = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(MapasActivity.this, permissoes, CODIGO_PERMISSAO);
            return false;
        }
        return true;
    }

    private void moveCamera(LatLng latLng){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_CAMERA));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        getMinhaLocalizacao();
        LatLng latLng = new LatLng(-21.209075, -41.886608);
        moveCamera(latLng);


    }

    private void iniciaAtualizacaoDaLocalizacao() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        fusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

}
