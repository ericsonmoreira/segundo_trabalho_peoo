package br.uece.peoo.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    /**
     * Cria um arquivo em doc/disciplinas/ da {@link Disciplina}
     * @return arquivo da disciplina em formato de texto(txt).
     */
    public File criarFileDisciplina() {
        File fileDisciplia = new File("doc/disciplinas/" + nome + ".txt");
        try {
            FileWriter fileWriter = new FileWriter(fileDisciplia);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Aluno aluno: alunos) {
                bufferedWriter.write(aluno.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDisciplia;
    }
}

