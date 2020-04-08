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

    /**
     *
     * @param nome
     * @param respostas
     */
    public Aluno(String nome, char[] respostas) {
        this.nome = nome;
        this.respostas = respostas;
    }

    /**
     * Calcula os acertos do {@link Aluno} por um gabarito.
     * @param gabarito {@link String} como o Gabarito.
     * @return
     */
    public int getAcertos(String gabarito) {
        if (this.respostasTodasIguais()) return 0;
        int count = 0;
        char[] gabChars = gabarito.toCharArray(); // caracteres da String gabarito
        for (int i = 0; i < gabChars.length; i++) if (this.respostas[i] == gabChars[i]) count++;
        return count;
    }

    /**
     * Método interno para verificar se Respostas é VVVVVVVVVV ou FFFFFFFFFF.
     * @return
     */
    private boolean respostasTodasIguais() {
        char primeiro = respostas[0];
        for (int i = 1; i < respostas.length; i++) {
            if (respostas[i] != primeiro) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String result = new String(respostas);
        return result + "\t" + this.nome;
    }
}
