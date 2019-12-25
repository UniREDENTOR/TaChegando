package br.edu.uniredentor.tachegando.model;

import java.util.HashMap;

public class Passageiro {

    private String foto;
    private String nome;
    private String tempo;
    private String id;
    private String telefone;
    private String titulo;
    private double credito;
    private int reputacao;
    private int viagem;

    public Passageiro() {}

    public Passageiro(String telefone) {
        this.telefone = telefone;
    }
    public Passageiro(String id, String foto, String nome) {
        this.id = id;
        this.foto = foto;
        this.nome = nome;
    }

    public Passageiro(String id, String telefone, String nome, String foto, String tempo, int reputacao, String titulo, double credito, int viagem) {
        this.id = id;
        this.telefone = telefone;
        this.nome = nome;
        this.foto = foto;
        this.tempo = tempo;
        this.reputacao = reputacao;
        this.titulo = titulo;
        this.credito = credito;
        this.viagem = viagem;
    }


    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public int getViagem() {
        return viagem;
    }

    public void setViagem(int viagem) {
        this.viagem = viagem;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public int getQtdViagem() {
        return viagem;
    }

    public void setQtdViagem(int qtdViagem) {
        this.viagem = qtdViagem;
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
        usuario.put("titulo", getTitulo());
        usuario.put("viagem", getQtdViagem());
        return usuario;
    }
}
