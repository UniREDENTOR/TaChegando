package br.edu.uniredentor.tachegando.model;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.HashMap;

import br.edu.uniredentor.tachegando.utils.GeralUtils;
import lombok.Data;

@Data
public class MensagemChat {

    private String idUsuario;
    private String nomeUsuario;
    private String fotoUsuario;
    private String texto;
    private String diaEHora;
    private Long dataDeCriacao;

    public MensagemChat(){
        setDiaEHora(GeralUtils.getData(Calendar.getInstance()));
        setDataDeCriacao(Calendar.getInstance().getTimeInMillis());
    }

    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("idUsuario", getIdUsuario());
        map.put("nomeUsuario", getNomeUsuario());
        map.put("fotoUsuario", getFotoUsuario());
        map.put("texto", getTexto());
        map.put("diaEHora", getDiaEHora());
        map.put("dataCriacao", getDataDeCriacao());
        return map;
    }

    @NonNull
    @Override
    public String toString() {
        return nomeUsuario;
    }
}
