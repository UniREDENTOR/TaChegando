package br.edu.uniredentor.tachegando.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Viagem implements Serializable {

    private String id, nome, idUsuario;
    private double latitude, longitude, latitudeInicial, longitudeInicial;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setLatLng(LatLng latLng) {
        setLatitude(latLng.latitude);
        setLongitude(latLng.longitude);
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public Map<String, Object> getMap() {
        Map<String, Object> viagemMap = new HashMap<>();
        viagemMap.put("id", getId());
        viagemMap.put("idUsuario", getIdUsuario());
        viagemMap.put("nome", getNome());
        viagemMap.put("latitude", getLatitude());
        viagemMap.put("longitude", getLongitude());
        return viagemMap;
    }

    public double getLongitudeInicial() {
        return longitudeInicial;
    }

    public void setLongitudeInicial(double longitudeInicial) {
        this.longitudeInicial = longitudeInicial;
    }

    public double getLatitudeInicial() {
        return latitudeInicial;
    }

    public void setLatitudeInicial(double latitudeInicial) {
        this.latitudeInicial = latitudeInicial;
    }

    public Map<String, Object> getMapInicial() {
        Map<String, Object> map = getMap();
        map.put("latitudeInicial", getLatitude());
        map.put("longitudeInicial", getLongitude());
        return map;
    }
}
