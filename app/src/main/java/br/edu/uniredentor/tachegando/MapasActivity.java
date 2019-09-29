package br.edu.uniredentor.tachegando;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.edu.uniredentor.tachegando.controller.BuscarOnibusController;
import br.edu.uniredentor.tachegando.controller.NovaViagemController;
import br.edu.uniredentor.tachegando.fragments.InformacaoOnibusDialogFragment;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.MapaUtils;
import br.edu.uniredentor.tachegando.utils.Singleton;

public class MapasActivity extends FragmentActivity implements OnMapReadyCallback, InformacaoOnibusDialogFragment.MarcacaoUpdate {

    private static final int CODIGO_PERMISSAO = 123;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocation;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 10000, FASTEST_INTERVAL = 5000; // = 5 seconds
    private LocationCallback locationCallback;
    private ArrayList<LatLng> locais;
    private int contador = 0;
    private double latitude, longitude;
    private ArrayList<LatLng> locais2;
    private ArrayList<Marker> listaDeOnibus = new ArrayList<>();
    private List<Viagem> viagens;
    private SupportMapFragment mapFragment;
    private Polyline polyline;
    private List<Viagem> listaViagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        Toolbar toolbarPrincipal = findViewById(R.id.toolbar_principal);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        criaDemo();
        mostraMapa();
        buscarViagens();
        toolbarPrincipal.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nova_viagem:
                        NovaViagemController.alertaDeNovaViagem(MapasActivity.this, latitude, longitude);
                        break;
                    case R.id.pesquisar_onibus:
                        BuscarOnibusController.alertaDeBusca(MapasActivity.this, listaViagens);
                        break;

                }
                return false;
            }
        });
        mapeiaViagens();



    }

    private void mostraMapa() {
        if(possuiPermissao()) {
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {

                        try{
                            LatLng latLng = locais.get(contador);
                            Viagem viagem = new Viagem();
                            viagem.setId(Singleton.getInstance().getIdViagem());
                            viagem.setIdUsuario("1");
                            viagem.setLatLng(latLng);
                            FirebaseUtils.atualizaLocalizacao(viagem);
                            if(contador == locais.size() - 1){
                                contador = 0;
                            }else{
                                contador++;
                            }

                            latLng = locais2.get(contador);
                            viagem = new Viagem();
                            viagem.setIdUsuario("2");
                            viagem.setNome("Teste 2");
                            viagem.setLatLng(latLng);
                            //    FirebaseUtils.salva(viagem);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }


                }
            };
        }
    }

    private void mapeiaViagens(){
        FirebaseUtils.getBanco().collection("viagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                viagens = queryDocumentSnapshots.toObjects(Viagem.class);

                for(Viagem viagem : viagens){
                    if(existe(viagem)){
                        getOnibus(viagem).setPosition(viagem.getLatLng());
                    }else{
                        try {
                            Marker marker = mMap.addMarker(new MarkerOptions().position(viagem.getLatLng()).title(viagem.getNome()));
                            marker.setTag(viagem.getIdUsuario());
                            listaDeOnibus.add(marker);

                        }catch (Exception ex) {

                        }
                    }
                }
            }
        });
    }


    private void buscarViagens() {
        FirebaseUtils.getBanco().collection("viagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                 listaViagens = queryDocumentSnapshots.toObjects(Viagem.class);
                 Log.d("locais", listaViagens.toString());
            }
        });
    }


    private boolean existe(Viagem viagem) {
        return getOnibus(viagem) != null;
    }

    private Marker getOnibus(Viagem viagem) {

        for(Marker marker : listaDeOnibus){
            try{
                if(marker.getTag().toString().equalsIgnoreCase(viagem.getIdUsuario())){
                    return marker;
                }
            }catch (Exception e){
                e.printStackTrace();
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
                        latitude = localizacaoAtual.getLatitude();
                        longitude = localizacaoAtual.getLongitude();
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
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                removePolyline();
                Viagem viagem = getViagem(marker.getTag().toString());
                new InformacaoOnibusDialogFragment().setMapa(mMap).setMarcacaoUpdate(MapasActivity.this).show(getSupportFragmentManager(), "informacao");

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                removePolyline();
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                removePolyline();
            }
        });

        getMinhaLocalizacao();
        LatLng latLng = new LatLng(-21.209075, -41.886608);
        MapaUtils.moveCamera(mMap, latLng);


    }

    //Sempre chamar antes das interações de click no mapa. Isso evitará ser traçada dois trojetos no mapa.
    private void removePolyline() {
        if(polyline != null){
            polyline.remove();
        }
    }

    private Viagem getViagem(String id) {
        for(Viagem viagem : viagens){
            if(viagem.getIdUsuario().equalsIgnoreCase(id)){
                return viagem;
            }
        }
        return new Viagem();
    }

    private void iniciaAtualizacaoDaLocalizacao() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        fusedLocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void limpar(Polyline polyline) {
        this.polyline = polyline;
    }
}
