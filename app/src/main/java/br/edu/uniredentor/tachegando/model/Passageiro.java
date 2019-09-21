package br.edu.uniredentor.tachegando.model;

public class Passageiro {

    private String foto;
    private String nome;
    private String tempo;

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
}
