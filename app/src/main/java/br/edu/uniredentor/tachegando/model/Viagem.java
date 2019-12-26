package br.edu.uniredentor.tachegando.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Viagem implements Serializable {

    private String id;
    private String nome;
    private List<Passageiro> passageiros = new ArrayList<>();
    private List<Denuncia> denuncias = new ArrayList<>();
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
        Map<String, Object> map = new HashMap<>();
        map.put("latitude", getLatitude());
        map.put("longitude", getLongitude());
        map.put("nome", getNome());
        map.put("latitudeInicial", getLatitude());
        map.put("longitudeInicial", getLongitude());
        map.put("passageiros", getPassageiros());
        map.put("id", getId());
        map.put("denuncias", getDenuncias());
        return map;
    }

    public void addPassageiro(Passageiro passageiro) {

        if(passageiros == null){
            passageiros = new ArrayList<>();
        }
        passageiros.add(passageiro);
    }

    public void addDenuncia(Denuncia denuncia) {
        if(denuncias == null){
            denuncias = new ArrayList<>();
        }
        this.denuncias.add(denuncia);
    }

    public List<Denuncia> getDenuncias() {
        return denuncias;
    }

    public void setDenuncias(ArrayList<Denuncia> denuncias) {
        this.denuncias = denuncias;
    }

    public List<Passageiro> getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(ArrayList<Passageiro> passageiros) {
        this.passageiros = passageiros;
    }


}
