package br.edu.uniredentor.tachegando.model;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.HashMap;

import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class MensagemChat {

    private String idUsuario;
    private String nomeUsuario;
    private String fotoUsuario;
    private String texto;
    private String diaEHora;

    public MensagemChat(){
        setDiaEHora(GeralUtils.getData(Calendar.getInstance()));
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDiaEHora() {
        return diaEHora;
    }

    public void setDiaEHora(String diaEHora) {
        this.diaEHora = diaEHora;
    }


    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }


    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("idUsuario", getIdUsuario());
        map.put("nomeUsuario", getNomeUsuario());
        map.put("fotoUsuario", getFotoUsuario());
        map.put("texto", getTexto());
        map.put("diaEHora", getDiaEHora());
        return map;
    }

    @NonNull
    @Override
    public String toString() {
        return nomeUsuario;
    }
}
