package br.edu.uniredentor.tachegando.model;

import java.util.HashMap;

public class Passageiro {

    private String foto;
    private String nome;
    private String tempo;
    private String id;

    public Passageiro(){}

    public Passageiro(String foto, String tempo) {
        setFoto(foto);
        setTempo(tempo);
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foto", getFoto());
        map.put("nome", getNome());
        map.put("tempo", getTempo());
        map.put("id", getId());
        return map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
