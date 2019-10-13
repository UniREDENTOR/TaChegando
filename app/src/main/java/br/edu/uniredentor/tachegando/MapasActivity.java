package br.edu.uniredentor.tachegando;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.edu.uniredentor.tachegando.activity.PerfilPassageiroActivity;
import br.edu.uniredentor.tachegando.controller.BuscarOnibusController;
import br.edu.uniredentor.tachegando.controller.CriarPontoController;
import br.edu.uniredentor.tachegando.controller.NovaViagemController;
import br.edu.uniredentor.tachegando.fragments.InformacaoOnibusDialogFragment;
import br.edu.uniredentor.tachegando.model.Ponto;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GPSUtils;
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
    protected double latitude, longitude;
    private ArrayList<LatLng> locais2;
    private ArrayList<Marker> listaDeOnibus = new ArrayList<>();
    private List<Viagem> viagens;
    private SupportMapFragment mapFragment;
    private Polyline polyline;
    private List<Viagem> listaViagens;
    private int REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        criaDemo();
        iniciaMapa();
    }

    private void iniciaMapa() {
        Toolbar toolbarPrincipal = findViewById(R.id.toolbar_principal);
        toolbarPrincipal.setTitle(getString(R.string.app_name));
        mostraMapa();
        buscarViagens();

        toolbarPrincipal.inflateMenu(R.menu.menu_principal);
        toolbarPrincipal.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nova_viagem:
                        if(GeralUtils.ehUsuario(MapasActivity.this)){
                            NovaViagemController.alertaDeNovaViagem(MapasActivity.this, latitude, longitude);
                        }
                        break;
                    case R.id.pesquisar_onibus:
                        BuscarOnibusController.alertaDeBusca(MapasActivity.this, listaViagens, mMap);
                        break;
                    case R.id.perfil:
                        if(GeralUtils.ehUsuario(MapasActivity.this)){
                            startActivity(new Intent(getApplicationContext(), PerfilPassageiroActivity.class));
                        }

                }
                return false;
            }
        });
        mapeiaViagens();

    }

    private void mostraMapa() {
        if (possuiPermissao()) {
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {

                        try {
                            LatLng latLng = locais.get(contador);
                            Viagem viagem = new Viagem();
                            viagem.setId(Singleton.getInstance().getIdViagem());
                            viagem.setIdUsuario("1");
                            viagem.setLatLng(latLng);
                            FirebaseUtils.atualizaLocalizacao(viagem);
                            if (contador == locais.size() - 1) {
                                contador = 0;
                            } else {
                                contador++;
                            }

                            latLng = locais2.get(contador);
                            viagem = new Viagem();
                            viagem.setIdUsuario("2");
                            viagem.setNome("Teste 2");
                            viagem.setLatLng(latLng);
                            //FirebaseUtils.salva(viagem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            };
        }
    }

    private void mapeiaViagens() {
        FirebaseUtils.getBanco().collection("viagens").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                viagens = queryDocumentSnapshots.toObjects(Viagem.class);

                for (Viagem viagem : viagens) {
                    if (existe(viagem)) {
                        getOnibus(viagem).setPosition(viagem.getLatLng());
                    } else {
                        try {
                            listaDeOnibus.add(MapaUtils.criaMarker(mMap, viagem));

                        } catch (Exception ex) {

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
            }
        });
    }

    private boolean existe(Viagem viagem) {
        return getOnibus(viagem) != null;
    }

    private Marker getOnibus(Viagem viagem) {

        for (Marker marker : listaDeOnibus) {
            try {
                if (marker.getTag().toString().equalsIgnoreCase(viagem.getIdUsuario())) {
                    return marker;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private void criaDemo() {
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
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertaGpsDesligado();
        } else {
            try {
                final Task localizacao = fusedLocation.getLastLocation();
                localizacao.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && localizacao != null) {
                            Location localizacaoAtual = (Location) task.getResult();
                            latitude = localizacaoAtual.getLatitude();
                            longitude = localizacaoAtual.getLongitude();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            iniciaAtualizacaoDaLocalizacao();
        }
    }

    private void alertaGpsDesligado() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(getString(R.string.seu_gps_esta_desligado)).setPositiveButton(getString(R.string.ativar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE);
            }
        }).setNegativeButton(getString(R.string.nao_ativar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == 0) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // gps ligado. Refatorar a sincronização do GPS
                GPSUtils gpsUtils = new GPSUtils();
                gpsUtils.checkLocalizacao(this);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.gps_ainda_desativado))
                        .setPositiveButton(getString(R.string.ativar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        }).setNegativeButton(getString(R.string.nao_ativar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        }
    }

    private boolean possuiPermissao() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
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

        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                removePolyline();
                Viagem viagem = getViagem(marker.getTag().toString());
                new InformacaoOnibusDialogFragment().setMapa(mMap).setMarcacaoUpdate(MapasActivity.this).setViagem(viagem).show(getSupportFragmentManager(), "informacao");

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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                CriarPontoController.alertaDeNovoPonto(MapasActivity.this,latitude, longitude);

            }
        });

        getMinhaLocalizacao();
        LatLng latLng = new LatLng(-21.209075, -41.886608);
        MapaUtils.moveCamera(mMap, latLng);


    }

    //Sempre chamar antes das interações de click no mapa. Isso evitará ser traçada dois trojetos no mapa.
    private void removePolyline() {
        if (polyline != null) {
            polyline.remove();
        }
    }

    private Viagem getViagem(String id) {
        for (Viagem viagem : viagens) {
            if (viagem.getIdUsuario().equalsIgnoreCase(id)) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PERMISSAO) {
            boolean permissoesAceitas = true;
            for (int permission : grantResults) {
                if (permission == PackageManager.PERMISSION_DENIED) {
                    permissoesAceitas = false;
                }
            }
            if (permissoesAceitas) {
                iniciaMapa();
            }
        }

    }
}
