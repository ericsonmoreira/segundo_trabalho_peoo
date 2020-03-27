package br.uece.peoo.controler;

import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class DisciplinaControler {

    private static DisciplinaControler controler;

    private static final String LOCATE = "doc/disciplinas/";

    private DisciplinaControler() { /* nada aqui */}

    /**
     * Usando aqui o padão de projeto Singleton.
     * Por que usar esse padrão de projeto? Resposta: para garantir que não tenha mais de um acesso aos arquivos ao
     * mesmo tempo.
     * @return
     */
    public static DisciplinaControler getInstance() {
        if (controler == null) {
            controler = new DisciplinaControler();
        }
        return controler;
    }

    public ArrayList<Disciplina> allDisciplinas() {
        ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
        File folderDisciplinas = new File(LOCATE); // instancia a pasta das disciplinas
        if (folderDisciplinas.isDirectory()) {
            for (File file: folderDisciplinas.listFiles()) {
                disciplinas.add(disciplinaFromFile(file));
            }
        }
        return disciplinas;
    }

    public Disciplina findDisciplina(String name) {
        File file = new File(LOCATE + name + ".txt");
        if (!file.exists())
            return null;
        else
            return disciplinaFromFile(file);
    }

    /**
     * Cria um arquivo em doc/disciplinas/ da {@link Disciplina}
     * @return arquivo da disciplina em formato de texto(txt).
     */
    public File fileFromDisciplina(Disciplina disciplina) {
        File fileDisciplia = new File("doc/disciplinas/" + disciplina.getNome() + ".txt");
        try {
            FileWriter fileWriter = new FileWriter(fileDisciplia);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Aluno aluno: disciplina.getAlunos()) {
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

    public Disciplina disciplinaFromFile(File file) {
        ArrayList<Aluno> alunos = new ArrayList<Aluno>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            reader.lines().forEach(line -> alunos.add(new Aluno(line)));
            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Disciplina(file.getName(), alunos);
    }

    // Main para testes rápidos
    public static void main(String[] args) {

        DisciplinaControler controler = getInstance();

        controler.allDisciplinas().forEach(disciplina -> System.out.println(disciplina));

        Disciplina disciplina = controler.findDisciplina("PEOO");

        disciplina.getAlunos().forEach(aluno -> System.out.println(aluno));

    }

}
