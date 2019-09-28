package br.edu.uniredentor.tachegando.utils;

public class Singleton {

    private static Singleton instance;
    private String idViagem;

    public static Singleton getInstance() {
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public String getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(String idViagem) {
        this.idViagem = idViagem;
    }
}
