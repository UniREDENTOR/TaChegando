package br.edu.uniredentor.tachegando.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import br.edu.uniredentor.tachegando.utils.GeralUtils;
import lombok.Data;

@Data
public class Denuncia implements Serializable {

    private String idDenunciante;
    private String data;
    private String horario;

    public Denuncia(){
        Calendar dia = Calendar.getInstance();
        setData(GeralUtils.getDataFirebase(dia));
        setHorario(GeralUtils.getHorario(dia));
    }
}
