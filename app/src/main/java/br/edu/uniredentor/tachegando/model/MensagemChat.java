package br.edu.uniredentor.tachegando.model;

import java.util.Calendar;
import java.util.HashMap;

import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class MensagemChat {

    private String idUsuario;
    private String nomeUsuario;
    private String fotoUsuario;
    private String texto;
    private String diaEHora;
    private String idViagem;

    public MensagemChat(){}

    public MensagemChat(Passageiro passageiro, String mensagem, Calendar calendar) {
        setDiaEHora(GeralUtils.getData(calendar));
        setIdUsuario(passageiro.getId());
        setFotoUsuario(passageiro.getFoto());
        setNomeUsuario(passageiro.getNome());
        setTexto(mensagem);
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

    public String getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(String idViagem) {
        this.idViagem = idViagem;
    }

    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("texto", texto);
        map.put("diaEHora", diaEHora);
        map.put("nomeUsuario", nomeUsuario);
        map.put("fotoUsuario", fotoUsuario);
        map.put("idUsuario", idUsuario);
        return map;
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
}
