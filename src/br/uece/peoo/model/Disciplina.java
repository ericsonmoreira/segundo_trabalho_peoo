package br.uece.peoo.model;

import java.util.ArrayList;
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
     * Construtor padão.
     * @param nome Nome da disciplina.
     * @param alunos Lista dos alunos.
     */
    public Disciplina(String nome, List<Aluno> alunos) {
        this.nome = nome;
        this.alunos = alunos;
    }

    /**
     * Construtor de um único parâmetro.
     * @param nome Nome da disciplina.
     */
    public Disciplina(String nome) {
        this(nome, new ArrayList<Aluno>());
    }

    /**
     * Adiona um novo aluno a disciplina.
     * @param aluno
     */
    public void addAluno(Aluno aluno) {
        this.alunos.add(aluno);
    }

    @Override
    public String toString() {
        String aux = "";
        for (Aluno aluno: this.alunos) {
            aux += new String(aluno.getRespostas()) + "\t" + aluno.getNome() + "\n";
        }
        return  aux;
    }
}

