package br.edu.uniredentor.tachegando.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import lombok.Data;

@Data
public class Viagem implements Serializable {

    private String id;
    private String nome;
    private List<Passageiro> passageiros = new ArrayList<>();
    private List<Denuncia> denuncias = new ArrayList<>();
    private boolean ativa;
    private String proximoIdDaViagem;
    private double latitude, longitude, latitudeInicial, longitudeInicial;

    public void setLatLng(LatLng latLng) {
        setLatitude(latLng.latitude);
        setLongitude(latLng.longitude);
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public Map<String, Object> getLocalizacao() {
        Map<String, Object> viagemMap = new HashMap<>();
        viagemMap.put(ConstantsUtils.LATITUDE, getLatitude());
        viagemMap.put(ConstantsUtils.LONGITUDE, getLongitude());
        return viagemMap;
    }

    public Map<String, Object> getInicialMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantsUtils.LATITUDE, getLatitude());
        map.put(ConstantsUtils.LONGITUDE, getLongitude());
        map.put(ConstantsUtils.NOME, getNome());
        map.put(ConstantsUtils.LATITUDE_INICIAL, getLatitude());
        map.put(ConstantsUtils.LONGITUDE_INICIAL, getLongitude());
        map.put(ConstantsUtils.PASSAGEIROS, getPassageiros());
        map.put(ConstantsUtils.ID, getId());
        map.put(ConstantsUtils.PROXIMO_ID_VIAGEM, getProximoIdDaViagem());
        map.put(ConstantsUtils.DENUNCIAS, getDenuncias());
        map.put(ConstantsUtils.ATIVA, isAtiva());
        return map;
    }

    public void addPassageiro(Passageiro passageiro) {

        if(passageiros == null){
            passageiros = new ArrayList<>();
        }
        passageiros.add(passageiro);
    }

    public void removePassageiro(String id) {
        if(isPassageiro(id)){
            passageiros.remove(encontraPassageiro(id));
        }

    }

    private Passageiro encontraPassageiro(String id) {
        for(Passageiro passageiro : passageiros){
            if(passageiro.getId().equalsIgnoreCase(id)){
                return passageiro;
            }
        }
        return new Passageiro();
    }

    public boolean isPassageiro(String id){
        for(Passageiro passageiro : passageiros){
            if(passageiro.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public Passageiro getCriador(Viagem viagem) {
        Passageiro passageiro = viagem.getPassageiros().get(0);
        return passageiro;
    }
}
