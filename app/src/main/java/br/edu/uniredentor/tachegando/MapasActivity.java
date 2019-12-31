package br.edu.uniredentor.tachegando;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.edu.uniredentor.tachegando.activity.PerfilPassageiroActivity;
import br.edu.uniredentor.tachegando.activity.ViagensAtivasActivity;
import br.edu.uniredentor.tachegando.controller.BuscarOnibusController;
import br.edu.uniredentor.tachegando.controller.NovaViagemController;
import br.edu.uniredentor.tachegando.fragments.InformacaoOnibusDialogFragment;
import br.edu.uniredentor.tachegando.fragments.NovaViagemManualDialogFragment;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GPSUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.MapaUtils;
import br.edu.uniredentor.tachegando.utils.SharedUtils;
import br.edu.uniredentor.tachegando.utils.Singleton;
import br.edu.uniredentor.tachegando.viewmodel.ViewModelPassageiro;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapasActivity extends FragmentActivity implements OnMapReadyCallback, InformacaoOnibusDialogFragment.MarcacaoUpdate {

    private static final int CODIGO_PERMISSAO = 123;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    protected double latitude, longitude;
    private ArrayList<Marker> listaDeOnibus = new ArrayList<>();
    private SupportMapFragment mapFragment;
    private Polyline polyline;
    private List<Viagem> listaViagens;
    private int REQUEST_CODE = 0;
    private int quantidadeDeViagens = 0;

    @BindView (R.id.toolbar_principal) Toolbar toolbarPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        ButterKnife.bind(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        criaToolBar();
        iniciaMapa();

        if(possuiPermissao()){
            iniciaMapa();
        }else{
            chamaPermissoes();
        }

        if(!SharedUtils.getId(this).equalsIgnoreCase("")){
            final DocumentReference docRef = FirebaseUtils.getViagem(SharedUtils.getId(this));

            docRef.addSnapshotListener((documentSnapshot, e) -> {

                try{
                    Viagem viagem = documentSnapshot.toObject(Viagem.class);
                    if(viagem.isAtiva()){
                        double latitudeDaViagem = viagem.getLatitude();
                        double longitudeDaViagem = viagem.getLongitude();
                        Marker marker = getOnibus(viagem);
                        LatLng latLng = new LatLng(latitudeDaViagem, longitudeDaViagem);
                        marker.setPosition(latLng);
                        MapaUtils.moveCamera(mMap, latLng);
                    }else{
                        SharedUtils.save(viagem.getProximoIdDaViagem(), MapasActivity.this);
                    }


                }catch (Exception e1){
                    e1.printStackTrace();
                    SharedUtils.save("", MapasActivity.this);
                }
            });
        }

        ViewModelPassageiro viewModelPassageiro = ViewModelProviders.of(this).get(ViewModelPassageiro.class);
        LiveData<QuerySnapshot> liveData = viewModelPassageiro.getQuerySnapshotLiveDataViagens();
        liveData.observe(this, queryDocumentSnapshots -> {
            criaMarkers(queryDocumentSnapshots);
        });

    }

    private void iniciaMapa() {
   //     buscarViagens();
        mostraMapa();
    }

    @OnClick(R.id.fab_menu_lista_viagem)
    public void listaViagens(){
        Singleton.getInstance().setViagemListMap(mMap, listaViagens);
        startActivity(new Intent(getApplicationContext(),ViagensAtivasActivity.class));
    }

    @OnClick(R.id.fab_menu_nova_viagem)
    public void novaViagem(){
        if(GeralUtils.ehUsuario(MapasActivity.this)){
            if(possuiLocalizacao()){
                NovaViagemManualDialogFragment.novaInstancia(latitude, longitude)
                        .show(getSupportFragmentManager(), "novaViagem");
            }else{
                GeralUtils.mostraAlerta("Atenção", "Não encontramos sua localização. Por favor, verifique seu GPS.", MapasActivity.this);
            }
        }
    }

    @OnClick(R.id.fab_menu_procurar_viagem)
    public void procuraViagem(){
        BuscarOnibusController.alertaDeBusca(MapasActivity.this, listaViagens, mMap);
    }

    private void criaToolBar() {
        toolbarPrincipal.setTitle(getString(R.string.app_name));
        toolbarPrincipal.inflateMenu(R.menu.menu_principal);
        toolbarPrincipal.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.perfil:
                    if(GeralUtils.ehUsuario(MapasActivity.this)){
                        startActivity(new Intent(getApplicationContext(), PerfilPassageiroActivity.class));
                    }
                    break;
            }
            return false;
        });
    }

    private boolean possuiLocalizacao() {
        return (latitude != 0 && longitude != 0);
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
                        if(souLocalizador()){
                            try{
                                if(location.getLatitude() != SharedUtils.getLatitude(MapasActivity.this) &&
                                        location.getLongitude() != SharedUtils.getLongitude(MapasActivity.this)){
                                    SharedUtils.save((long) location.getLatitude(), ConstantsUtils.LATITUDE, MapasActivity.this);
                                    SharedUtils.save((long) location.getLongitude(), ConstantsUtils.LONGITUDE, MapasActivity.this);
                                    FirebaseUtils.atualizaLocalizacao(SharedUtils.getId(MapasActivity.this), location);
                                    GeralUtils.show("Atualizao loca " + location);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
        }
    }

    private boolean souLocalizador() {
        return SharedUtils.getId(MapasActivity.this).equals(GeralUtils.getIdDoUsuario());
    }

    private void chamaPermissoes() {
        String[] permissoes = {Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(MapasActivity.this, permissoes, CODIGO_PERMISSAO);
    }


    private void buscarViagens() {
        FirebaseUtils.getBanco().collection(ConstantsUtils.VIAGENS).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if(queryDocumentSnapshots.getDocuments().size() != quantidadeDeViagens){
                this.quantidadeDeViagens = queryDocumentSnapshots.getDocumentChanges().size();
                mMap.clear();
                criaMarkers(queryDocumentSnapshots);
                GeralUtils.show("Buscanod localizacao");
            }


        });
    }

    private void criaMarkers(QuerySnapshot queryDocumentSnapshots) {
        try{
            listaViagens = queryDocumentSnapshots.toObjects(Viagem.class);
            for (Viagem viagem : listaViagens) {
                if (existe(viagem)) {
                    getOnibus(viagem).setPosition(viagem.getLatLng());
                } else {
                    try {
                        Marker marker = MapaUtils.criaMarker(mMap, viagem);
                        listaDeOnibus.add(marker);
                    } catch (Exception ex) {

                    }
                }
            }
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    private boolean existe(Viagem viagem) {
        return getOnibus(viagem) != null;
    }

    private Marker getOnibus(Viagem viagem) {

        for (Marker marker : listaDeOnibus) {
            try {
                if (isViagem(marker.getTag().toString(), viagem.getId())) {
                    return marker;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean isViagem(String s, String id) {
        return s.equalsIgnoreCase(id);
    }

    private void getMinhaLocalizacao() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(this);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertaGpsDesligado();
        } else {
            try {
                final Task localizacao = fusedLocation.getLastLocation();
                localizacao.addOnCompleteListener(task -> {
                    if (task.isSuccessful() && localizacao != null) {
                        Location localizacaoAtual = (Location) task.getResult();
                        latitude = localizacaoAtual.getLatitude();
                        longitude = localizacaoAtual.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        GeralUtils.show("Minha localizacao " + latLng);
                        MapaUtils.moveCamera(mMap, latLng);
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
        alertDialog.setMessage(getString(R.string.seu_gps_esta_desligado)).setPositiveButton(getString(R.string.ativar), (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE);
        }).setNegativeButton(getString(R.string.nao_ativar), (dialog, which) -> dialog.dismiss()).create().show();
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
                        .setPositiveButton(getString(R.string.ativar), (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, REQUEST_CODE);
                        }).setNegativeButton(getString(R.string.nao_ativar), (dialog, which) -> dialog.dismiss()).create().show();
            }
        }
    }

    private boolean possuiPermissao() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));

        mMap.setOnInfoWindowClickListener(marker -> {
            try{
                removePolyline();
                Viagem viagem = getViagem(marker.getTag().toString());
                new InformacaoOnibusDialogFragment()
                        .setMapa(mMap)
                        .setMarcacaoUpdate(MapasActivity.this)
                        .setViagem(viagem)
                        .setLocalizacao(latitude, longitude)
                        .show(getSupportFragmentManager(), "informacao");

            }catch (Exception e){
                e.printStackTrace();
                GeralUtils.mostraAlerta("Atenção", "Algum erro aconteceu com esta viagem. Estamos tentando identificar o problema.", MapasActivity.this);
            }

        });

        mMap.setOnMarkerClickListener( marker -> {
            removePolyline();
            return false;
        });

        mMap.setOnMapClickListener(latLng -> removePolyline());
        getMinhaLocalizacao();
    }

    //Sempre chamar antes das interações de click no mapa. Isso evitará ser traçada dois trojetos no mapa.
    private void removePolyline() {
        if (polyline != null) {
            polyline.remove();
        }
    }

    private Viagem getViagem(String id) {
        for (Viagem viagem : listaViagens) {
            if (viagem.getId().equalsIgnoreCase(id)) {
                return viagem;
            }
        }
        return new Viagem();
    }

    private void iniciaAtualizacaoDaLocalizacao() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    //    locationRequest.setSmallestDisplacement(100);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
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
