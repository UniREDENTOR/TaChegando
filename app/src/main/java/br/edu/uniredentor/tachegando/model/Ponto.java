package br.edu.uniredentor.tachegando.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import lombok.Data;

@Data
public class Ponto implements Serializable {

    private String id = "", nome, idUsuario;
    private double latitude, longitude;

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }


}


