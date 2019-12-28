package br.edu.uniredentor.tachegando.model;

import java.io.Serializable;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Passageiro implements Serializable {

    private String id;
    private String nome;
    private String foto;
    private int reputacao;
    private String titulo;
    private double credito;
    private int viagem;

    public int getQtdViagem() {
        return viagem;
    }

    public void setQtdViagem(int qtdViagem) {
        this.viagem = qtdViagem;
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
}
