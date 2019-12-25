package br.edu.uniredentor.tachegando.model;

import java.util.Calendar;
import java.util.HashMap;

import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class Denuncia {

    private String idDenunciante;
    private String data;
    private String horario;

    public Denuncia(){
        Calendar dia = Calendar.getInstance();
        setData(GeralUtils.getDataFirebase(dia));
        setHorario(GeralUtils.getHorario(dia));
    }

    public String getIdDenunciante() {
        return idDenunciante;
    }

    public void setIdDenunciante(String idDenunciante) {
        this.idDenunciante = idDenunciante;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
