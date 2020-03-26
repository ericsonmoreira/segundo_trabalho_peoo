package br.uece.peoo;

import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Considere um arquivo texto onde cada linha representa as respostas de uma prova objetiva de um aluno.
 * Essa prova contém 10 questões, todas do tipo V ou F. O final de cada linha contém o nome do aluno que respondeu
 * aquelas opções separadas das respostas por um “tab”.
 */
public class Main {

    public static void main(String[] args) {

        ArrayList<Aluno> alunos = new ArrayList<Aluno>();

        File file = new File("doc/alunos.txt");

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            reader.lines().forEach(line -> alunos.add(new Aluno(line))); // imprimendo todas as linhas

            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Disciplina peoo = new Disciplina("PEOO", alunos);

        peoo.criarFileDisciplina(); // criarndo arquivo

    }

}
