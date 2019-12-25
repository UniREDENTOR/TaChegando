package br.edu.uniredentor.tachegando.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Viagem implements Serializable {

    private String id = "";

    private String nome;

    private ArrayList<String> idPassageiros;
    private ArrayList<Denuncia> denuncias;
    private double latitude, longitude, latitudeInicial, longitudeInicial;
    public Map<String, Object> getIdMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        return map;
    }
    public ArrayList<String> getIdPassageiros() {
        return idPassageiros;
    }

    public void setIdPassageiros(ArrayList<String> idPassageiros) {
        this.idPassageiros = idPassageiros;
    }

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

    public void setLatLng(LatLng latLng) {
        setLatitude(latLng.latitude);
        setLongitude(latLng.longitude);
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
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


    public Map<String, Object> getLocalizacao() {
        Map<String, Object> viagemMap = new HashMap<>();
        viagemMap.put("latitude", getLatitude());
        viagemMap.put("longitude", getLongitude());
        return viagemMap;
    }


    public Map<String, Object> getInicialMap() {
        Map<String, Object> map = getLocalizacao();
        map.put("latitude", getLatitude());
        map.put("longitude", getLatitude());
        map.put("nome", getNome());
        map.put("latitudeInicial", getLatitude());
        map.put("longitudeInicial", getLongitude());
        map.put("idPassageiros", getIdPassageiros());
        map.put("id", getId());
        map.put("denuncias", getDenuncias());
        return map;
    }

    public void addPassageiro(String uid) {

        if(idPassageiros == null){
            idPassageiros = new ArrayList<>();
        }
        idPassageiros.add(uid);
    }

    public void addDenuncia(Denuncia denuncia) {
        if(denuncias == null){
            denuncias = new ArrayList<>();
        }
        this.denuncias.add(denuncia);
    }

    public ArrayList<Denuncia> getDenuncias() {
        return denuncias;
    }

    public void setDenuncias(ArrayList<Denuncia> denuncias) {
        this.denuncias = denuncias;
    }
}
