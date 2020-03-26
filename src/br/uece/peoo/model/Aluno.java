package br.uece.peoo.model;

import java.util.Arrays;
import java.util.List;

public class Aluno {

    private String nome;

    private char[] respostas;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public char[] getRespostas() {
        return respostas;
    }

    public void setRespostas(char[] respostas) {
        this.respostas = respostas;
    }

    /**
     * @param cod
     */
    public Aluno(String cod) {
        String[] result =  cod.split("\t");
        this.nome = result[1];
        respostas = result[0].toCharArray();
    }

    @Override
    public String toString() {
        String result = new String(respostas);
        return result + "\t" + this.nome;
    }
}
