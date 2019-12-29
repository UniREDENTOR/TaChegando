package br.edu.uniredentor.tachegando.utils;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import br.edu.uniredentor.tachegando.model.Viagem;

public class Singleton {

    private static Singleton instance;
    private String idViagem;
    private List<Viagem> viagemList;
    private GoogleMap googleMap;

    public static Singleton getInstance() {
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public String getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(String idViagem) {
        this.idViagem = idViagem;
    }

    public List<Viagem> getViagemList() {
        return viagemList;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setViagemListMap(GoogleMap googleMap, List<Viagem> viagemList) {
        this.googleMap = googleMap;
        this.viagemList = viagemList;
    }
}
