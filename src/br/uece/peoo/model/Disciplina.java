package br.uece.peoo.model;

import br.uece.peoo.controler.DisciplinaControler;

import java.io.*;
import java.util.List;

public class Disciplina {

    private String nome;

    private List<Aluno> alunos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    /**
     *
     * @param nome
     * @param alunos
     */
    public Disciplina(String nome, List<Aluno> alunos) {
        this.nome = nome;
        this.alunos = alunos;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "nome='" + nome + '\'' +
                ", alunos=" + alunos +
                '}';
    }
}

