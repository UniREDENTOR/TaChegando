package br.edu.uniredentor.tachegando.model;

import java.util.HashMap;

public class Passageiro {

    private String foto;
    private String nome;
    private String tempo;
    private String id;
    private String telefone;
    private double credito;
    private int reputacao;
    private String tituloReputacao;

    public Passageiro() {}


    public Passageiro(String id, String telefone, String nome, String foto, String tempo, int reputacao, String tituloReputacao, Double credito) {
        this.id = id;
        this.telefone = telefone;
        this.nome = nome;
        this.foto = foto;
        this.tempo = tempo;
        this.reputacao = reputacao;
        this.tituloReputacao = tituloReputacao;
        this.credito = credito;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
    }

    public int getReputacao() {
        return reputacao;
    }

    public void setReputacao(int reputacao) {
        this.reputacao = reputacao;
    }

    public String getTituloReputacao() {
        return tituloReputacao;
    }

    public void setTituloReputacao(String tituloReputacao) {
        this.tituloReputacao = tituloReputacao;
    }

    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foto", getFoto());
        map.put("nome", getNome());
        map.put("tempo", getTempo());
        map.put("id", getId());
        map.put("telefone", getTelefone());
        return map;
    }

    public HashMap<String, Object> getUser() {
        HashMap<String, Object> usuario = new HashMap<>();
        usuario.put("foto", getFoto());
        usuario.put("nome", getNome());
        usuario.put("tempo", getTempo());
        usuario.put("id", getId());
        usuario.put("telefone", getTelefone());
        usuario.put("credito", getCredito());
        usuario.put("reputacao", getReputacao());
        usuario.put("titulo", getTituloReputacao());
        return usuario;
    }
}
