package br.edu.uniredentor.tachegando.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Passageiro implements Serializable {

    private String foto;
    private String nome;
    private String id;
    private String titulo;
    private double credito;
    private int reputacao;
    private int viagem;

    public Passageiro() {}

    public Passageiro(String id, String foto, String nome) {
        this.id = id;
        this.foto = foto;
        this.nome = nome;
    }

    public Passageiro(String id, String nome, String foto, int reputacao, String titulo, double credito, int viagem) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
        this.reputacao = reputacao;
        this.titulo = titulo;
        this.credito = credito;
        this.viagem = viagem;
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

    public HashMap<String, Object> retornaMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("foto", getFoto());
        map.put("nome", getNome());
        map.put("id", getId());
        return map;
    }

    public HashMap<String, Object> retornaUser() {
        HashMap<String, Object> usuario = new HashMap<>();
        usuario.put("foto", getFoto());
        usuario.put("nome", getNome());
        usuario.put("id", getId());
        usuario.put("credito", getCredito());
        usuario.put("reputacao", getReputacao());
        usuario.put("titulo", getTitulo());
        usuario.put("viagem", getQtdViagem());
        return usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passageiro that = (Passageiro) o;
        return nome.equals(that.nome) &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, id);
    }
}
