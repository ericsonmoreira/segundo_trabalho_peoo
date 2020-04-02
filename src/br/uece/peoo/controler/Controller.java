package br.uece.peoo.controler;

import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {

    private static Controller controler;

    public static final String DOC_ALUNOS = "doc/alunos/";
    public static final String DOC_DISCIPLINAS = "doc/disciplinas/";
    public static final String DOC_GABARITOS = "doc/gabaritos/";
    public static final String DOC_RESULTADOS = "doc/disciplinas/resultados/";
    public static final String DOC_RESULTADOS_POR_NOTAS = "doc/disciplinas/resultados/ord_nota/";
    public static final String DOC_RESULTADOS_POR_NOMES = "doc/disciplinas/resultados/ord_nome/";

    private Controller() { /* nada aqui */}

    /**
     * Usando aqui o padão de projeto Singleton.
     * Por que usar esse padrão de projeto? Resposta: para garantir que não tenha mais de um acesso aos arquivos ao
     * mesmo tempo.
     * @return
     */
    public static Controller getInstance() {
        if (controler == null) {
            controler = new Controller();
        }
        return controler;
    }

    /**
     * Retorna todas as disciplinas.
     * @return
     */
    public ArrayList<Disciplina> getAllDisciplinas() {
        ArrayList<Disciplina> disciplinas = new ArrayList<Disciplina>();
        File folderDisciplinas = new File(DOC_DISCIPLINAS); // instancia a pasta das disciplinas
        if (folderDisciplinas.isDirectory()) {
            for (File file: folderDisciplinas.listFiles()) {
                disciplinas.add(disciplinaFromFile(file));
            }
        }
        return disciplinas;
    }

    public Disciplina findDisciplina(String name) {
        File file = new File(DOC_DISCIPLINAS + name + ".txt");
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
        return new Disciplina(file.getName().replace(".txt", ""), alunos);
    }

    /**
     * Lé o resultado de um arquivo com gabarito
     * @param file
     * @return
     */
    public String lendoGabarito(File file) {
        String gab = "";
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            gab = reader.readLine();
            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gab;
    }

    public void gerarResultado(Disciplina disciplina, String gabarito) {
        List<Aluno> alunosOAlfa = disciplina.getAlunos().stream().
                sorted(Comparator.comparing(Aluno::getNome)). // Comparado por nome
                collect(Collectors.toList());

        List<Aluno> alunosAcertos = disciplina.getAlunos().stream().
                sorted(Comparator.comparing(aluno -> aluno.getAcertos(gabarito), Comparator.reverseOrder())).
                collect(Collectors.toList());
        // Média geram dos alunos
        double mediaGeral = alunosOAlfa.stream().
                mapToDouble(aluno -> aluno.getAcertos(gabarito)).average().getAsDouble();

        File alunosOAlfaFile = new File(DOC_RESULTADOS_POR_NOMES + disciplina.getNome() + ".txt");
        File alunosAcertosFile = new File(DOC_RESULTADOS_POR_NOTAS + disciplina.getNome() + ".txt");

        try {
            FileWriter fileWriter = new FileWriter(alunosOAlfaFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Aluno aluno: alunosOAlfa) {
                bufferedWriter.write(aluno.getNome() + "\t" + aluno.getAcertos(gabarito));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("Média\t" + mediaGeral);

            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fileWriter = new FileWriter(alunosAcertosFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Aluno aluno: alunosAcertos) {
                bufferedWriter.write(aluno.getNome() + "\t" + aluno.getAcertos(gabarito));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("Média\t" + mediaGeral);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para ler um arquivo de gabarito.
     * @param file
     * @return vertor de char de tamanho 10 contendo V ou F apenas.
     */
    public char[] lerFileGabarito(File file) {
        char[] value = null;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            value = bufferedReader.readLine().toCharArray();
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *  Leia o resultado de cada disciplina e crie um arquivo para cada aluno
     *  contendo as disciplinas e notas obtida por ele.
     *  Ao final, grave também a média do aluno.
     *  O nome do arquivo é o nome do aluno.
     */
    public void gerarHistoricoAlunos() {
        clearDocAlunos(); // limpa os históricos dos alunos
        File resultados = new File(DOC_RESULTADOS_POR_NOTAS);
        for (File file : resultados.listFiles()) { // para cada arquivo de resultado
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                ArrayList<String> lines = new ArrayList<String>();
                bufferedReader.lines().forEach(s -> lines.add(s));
                lines.remove(lines.size() - 1); // removendo o ultimo.
                lines.forEach(line -> {
                    String nomeAluno = line.split("\t")[0];
                    String notaAluno = line.split("\t")[1];

                    File aluno = new File(DOC_ALUNOS + nomeAluno + ".txt");
                    try {
                        FileWriter fileWriter = new FileWriter(aluno, true);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        bufferedWriter.append(file.getName().replace(".txt", "") + "\t" + notaAluno);
                        bufferedWriter.newLine();

                        bufferedWriter.close();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                bufferedReader.close();
                fileReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // Colocando as médias em cada arquivo de aluno gerado.
        File alunos = new File(DOC_ALUNOS);

        for (File alunoFile : alunos.listFiles()) {
            double media = getMediaAluno(alunoFile); // obtendo a média
            try {
                FileWriter fileWriter = new FileWriter(alunoFile, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("MEDIA\t" + media);
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gera a média de uma arquivo de aluno.
     * @param file
     * @return
     */
    private double getMediaAluno(File file) {
        double avarege = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            avarege = bufferedReader.lines().
                    mapToDouble(value -> Double.parseDouble(value.split("\t")[1])).
                    average().getAsDouble();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return avarege;
    }

    /**
     * Deletar todos os arquivos de alunos
     */
    private void clearDocAlunos() {
        File alunos = new File(DOC_ALUNOS);
        for (File alunoFile : alunos.listFiles()) {
            alunoFile.delete();
        }
    }

    /**
     * Retorna todos os nomes dos arquivos de gabarito que estão em DOC_GABARITOS.
     * @return Lista de Strings
     */
    public List<String> getNomesGabaritos(){
        File gabsFolder = new File(DOC_GABARITOS);
        return Arrays.stream(gabsFolder.listFiles()).
                map(file -> file.getName().replace(".txt", "")).collect(Collectors.toList());
    }

    /**
     * Retorna todos os nomes dos arquivos de discipolinas que estão em DOC_DISCIPLINAS.
     * @return Lista de Strings
     */
    public List<String> getNomesDisciplinas(){
        File discFolder = new File(DOC_DISCIPLINAS);
        return Arrays.stream(discFolder.listFiles()).
                map(file -> file.getName().replace(".txt", "")).collect(Collectors.toList());
    }
}
