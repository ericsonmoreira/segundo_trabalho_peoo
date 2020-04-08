package br.uece.peoo.control;

import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Controller {

    public static final String DOC_ALUNOS = "doc/alunos/";
    public static final String DOC_DISCIPLINAS = "doc/disciplinas/";
    public static final String DOC_GABARITOS = "doc/gabaritos/";
    public static final String DOC_RESULTADOS = "doc/disciplinas/resultados/";
    public static final String DOC_RESULTADOS_POR_NOTAS = "doc/disciplinas/resultados/ord_nota/";
    public static final String DOC_RESULTADOS_POR_NOMES = "doc/disciplinas/resultados/ord_nome/";

    /**
     * Cria um arquivo de disciplina em DOC_DISCIPLINAS
     * @return arquivo da disciplina.
     */
    public static File fileFromDisciplina(Disciplina disciplina) {
        File fileDisciplia = new File(DOC_DISCIPLINAS + disciplina.getNome() + ".txt");
        escreverArquivo(fileDisciplia, disciplina.toString());
        return fileDisciplia;
    }

    /**
     * Gera uma instância de {@link Disciplina}
     * @param file aquivo onte estão as informações da disciplina
     * @return
     */
    public static Disciplina disciplinaFromFile(File file) {
        ArrayList<Aluno> alunos = new ArrayList<Aluno>();
        String[] lines = lerArquivo(file).split("\n");
        for (String line: lines) {
            String nomeAluno = line.split("\t")[1];
            char[] respostasAluno = line.split("\t")[0].toCharArray();
            alunos.add(new Aluno(nomeAluno, respostasAluno));
        }
        return new Disciplina(file.getName().replace(".txt", ""), alunos);
    }

    /**
     * Gera a média de uma arquivo de aluno.
     * @param file
     * @return
     */
    public static double getMediaAluno(File file) {
        double avarege = 0;
        String texto = lerArquivo(file);
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
     * Deletar todos os arquivos em DOC_ALUNOS
     */
    public static void clearDocAlunos() {
        File alunos = new File(DOC_ALUNOS);
        for (File alunoFile : alunos.listFiles()) {
            alunoFile.delete();
        }
    }

    /**
     * Retorna todos os nomes dos arquivos de gabarito que estão em DOC_GABARITOS.
     * @return Lista de Strings
     */
    public static List<String> getNomesGabaritos(){
        File gabsFolder = new File(DOC_GABARITOS);
        return Arrays.stream(gabsFolder.listFiles()).
                map(file -> file.getName().replace(".txt", "")).collect(Collectors.toList());
    }

    /**
     * Retorna todos os nomes dos arquivos de discipolinas que estão em DOC_DISCIPLINAS.
     * @return Lista de Strings
     */
    public static List<String> getNomesDisciplinas(){
        File discFolder = new File(DOC_DISCIPLINAS);
        return Arrays.stream(discFolder.listFiles()).
                map(file -> file.getName().replace(".txt", "")).collect(Collectors.toList());
    }

    /**
     * Garante que o valor digitado tenha 10 caracteres e que seja 'F' ou 'V'.
     * @param msg
     * @return uma {@link String} no padão de um gabarito.
     */
    public static char[] validarGabarito(String msg) {
        Scanner scanner = new Scanner(System.in);
        char[] gab;
        System.out.println(msg);
        try {
            gab = scanner.nextLine().toUpperCase().toCharArray();
            // Verifica se tem 10 caracteres.
            if (gab.length != 10) throw new IllegalArgumentException();
            // Verifica se algum desses Caracteres é diferente de V ou F.
            for (char c : gab) if (c != 'F' &&  c != 'V') throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            System.err.println("Esse padão de Respostas não é válido.");
            gab = validarGabarito(msg);
        }
        return gab;
    }

    /**
     * Verifica se a disciplina pedida está entre os nomes de disciplinas passadas como parametro.
     * @param msg mensagem mostrada antes.
     * @param disciplinasNames nomes possíveis para o valor digitado
     * @return um nome de {@link Disciplina} válido.
     */
    public static String existeDisciplina(String msg, List<String> disciplinasNames) {
        Scanner scanner = new Scanner(System.in);
        String disciplina = null;
        System.out.println(msg); // imprime a mensagem
        try {
            // pega a linha digitada
            disciplina = scanner.nextLine().toUpperCase();
            if (!disciplinasNames.contains(disciplina)) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            System.err.println("Diciplina Inválida");
            disciplina = existeDisciplina(msg, disciplinasNames);
        }
        return disciplina;
    }

    /**
     * Verifica se tem arquivos de disciplina em DOC_DISCIPLINAS
     * @return
     */
    public static boolean existeDiscipliaCadastrada() {
        File fileDisc = new File(DOC_DISCIPLINAS);
        return Arrays.stream(fileDisc.listFiles()).filter(file -> !file.isDirectory()).count() > 0;
    }

    /**
     * Verifica se tem arquivos de gabaritos em DOC_GABARITOS
     * @return
     */
    public static boolean existeGabaritoCadastrado() {
        return new File(DOC_GABARITOS).listFiles().length > 0;
    }

    /**
     * Verifica se o gabarito pedido está entre os nomes dos gabaritos passados como parametro.
     * @param msg mensagem mostrada antes.
     * @param disciplinasNames nomes possíveis para o valor digitado
     * @return um nome de um gabarito contido em DOC_GABARITOS.
     */
    public static String existeGabarito(String msg, List<String> gabaritosNames) {
        Scanner scanner = new Scanner(System.in);
        String gabarito = null;
        System.out.println(msg); // imprime a mensagem
        try {
            gabarito = scanner.nextLine().toUpperCase();
            if (!gabaritosNames.contains(gabarito)) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            System.err.println("Gabarito Inválido");
            gabarito = existeGabarito(msg, gabaritosNames);
        }
        return gabarito;
    }

    /**
     * Cria a estrutura de pastas necessárias para o projeto para o caso de não exitir ainda.
     */
    public static void criarEstruturaPastas() {
        List<File> docs = Arrays.asList(
                new File(DOC_ALUNOS),
                new File(DOC_DISCIPLINAS),
                new File(DOC_GABARITOS),
                new File(DOC_RESULTADOS),
                new File(DOC_RESULTADOS_POR_NOTAS),
                new File(DOC_RESULTADOS_POR_NOMES)
        );
        docs.forEach(file -> {
            if (!file.exists()) file.mkdirs();
        });
    }

    // o nome já explica
    public static String lerArquivo(File file) {
        String texto = "";
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            // lé o contudo do arquivo
            while (bufferedReader.ready()) texto += bufferedReader.readLine() + "\n";
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texto;
    }

    // o nome já explica
    public static void escreverArquivo(File file, String texto) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(texto);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // o nome já explica
    public static void addFinalArquivo(File file, String texto) {
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(texto);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
