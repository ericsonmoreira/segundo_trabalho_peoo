package br.uece.peoo;

import br.uece.peoo.controler.Controller;
import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;
import br.uece.peoo.util.Menu;

import java.io.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static br.uece.peoo.controler.Controller.*;

/**
 * Considere um arquivo texto onde cada linha representa as respostas de uma prova objetiva de um aluno.
 * Essa prova contém 10 questões, todas do tipo V ou F. O final de cada linha contém o nome do aluno que respondeu
 * aquelas opções separadas das respostas por um “tab”.
 */
public class Main {

    public static void main(String[] args) {

        // criando estrutura do projeto, caso não exista
        criarEstruturaPastas();

        // Criando um Menu.
        Menu menu = new Menu();

        menu.addOption(1, "Criar Disciplina.", () -> criarDisciplinaMenu());
        menu.addOption(2, "Visualizar Disciplinas.", () -> viewDisciplinasMenu());
        menu.addOption(3, "Gerar Resultado de uma Disciplina.", () -> gerarResultadoDisciplinaMenu());
        menu.addOption(4, "Visualizar Resultados", () -> viewResultadosDisciplinasMenu());
        menu.addOption(5, "Criar Histórico de dos Alunos", () -> gerarHistoricoAlunosMenu());
        menu.addOption(6, "Criar Gabarito", () -> criarGabaritoMenu());
        menu.addOption(7, "Visualizar Gabaritos", () -> viewGabaritosMenu());
        menu.addOption(8, "Visualizar Alunos", () -> viewAlunosMenu());

        menu.addOption(99, "Sair do programa.", () -> System.exit(0)); // opção para fechar o programa

        Scanner scanner = new Scanner(System.in);

        while (true) {
            menu.printMenu();
            System.out.println("Digite a opção:");
            try {
                int op = scanner.nextInt();
                menu.getRunnable(op).run();
            } catch (InputMismatchException | NullPointerException err) {
                System.err.println("Opção invalida");
            }
        }

    }

    /**
     * Mostra as disciplinas.
     */
    public static void viewDisciplinasMenu() {
        File file = new File(DOC_DISCIPLINAS);
        Controller controler = Controller.getInstance();
        for (File disciplinaFile: file.listFiles()) {
            if (!disciplinaFile.isDirectory()) { // apenas os arquivos que não são diretórios.
                Disciplina disciplina = controler.disciplinaFromFile(disciplinaFile);
                System.out.println(disciplina);
            }
        }
    }

    /**
     * Chamado quando o usuário deseja criar uma disciplina nova.
     */
    private static void criarDisciplinaMenu() {
        Controller controler = Controller.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nome da Disciplina");
        String discName = scanner.nextLine().toUpperCase();
        Disciplina disciplina = new Disciplina(discName); // nova disciplina.
        boolean flag = true;
        while (flag) {
            Aluno aluno;
            System.out.println("Digite o aluno (Ex.: Fulano de Tal)");
            String alunoName = scanner.nextLine().toUpperCase();
            System.out.println();
            char[] alunoRespostas = validarGabarito("Digite as resposta do aluno (Ex.: FVFVVVFVFF)");
            disciplina.addAluno(new Aluno(alunoName, alunoRespostas));
            System.out.println("Digite fim para parar ou nada para continuar");
            if (scanner.nextLine().equals("fim")) {
                flag = false;
            }
        }
        controler.fileFromDisciplina(disciplina);
    }

    /**
     * Cria um gabarito e o salva na pasta DOC_GABARITOS
     */
    private static void criarGabaritoMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do gabarito");
        String gabName = scanner.nextLine().toUpperCase();
        char[] gabResp = validarGabarito("Digite as respostas do Gabarito ex.: FFVVFFVFVF");
        File gabFile = new File(DOC_GABARITOS + gabName + ".txt");
        try {
            FileWriter fileWriter = new FileWriter(gabFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(gabResp);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Mostra todos os gabaritos da pastac DOC_GABARITOS
     */
    private static void viewGabaritosMenu() {
        File file = new File(DOC_GABARITOS); // diretorio como os arquivos dos gabaritos.
        Controller controller = Controller.getInstance();
        for (File gabFile: file.listFiles()) {
            if (!gabFile.isDirectory()) {
                String gabName = gabFile.getName().replace(".txt", "");
                String gabResp = controller.lendoGabarito(gabFile);
                System.out.println(gabName + "\t" + gabResp);
            }
        }
    }

    /**
     * Gera os resultados das disciplias na pasta DOC_RESULTADOS.
     * DOC_RESULTADOS_POR_NOMES ---> Alunos ordenados pelo nome.
     * DOC_RESULTADOS_POR_NOTAS ---> Alunos ordenados pela nota (Ordem decrescente).
     */
    private static void gerarResultadoDisciplinaMenu() {
        Controller controller = Controller.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Disciplinas:");
        viewDisciplinasMenu(); // Mostar aqui as disciplinas que existem
        List<String> nomesDisciplinas = controller.getNomesDisciplinas(); // pega o nome das disciplinas
        String nomeDisc = existeDisciplina("Digite o nome da disciplina", nomesDisciplinas);
        File fileDisc = new File(DOC_DISCIPLINAS + nomeDisc + ".txt");
        System.out.println("Gabaritos:");
        viewGabaritosMenu(); // Mostrar aqui os gabaritos que existem
        List<String> nomesGabaritos = controller.getNomesGabaritos(); // pega os nomes dos gabaritos
        String nomeGab = existeGabarito("Digite o nome do Gabarito", nomesGabaritos);
        File fileGab = new File(DOC_GABARITOS + nomeGab + ".txt");
        Disciplina disciplina = controller.disciplinaFromFile(fileDisc);
        controller.gerarResultado(disciplina, controller.lendoGabarito(fileGab));
    }

    /**
     *  Gera os arquivos de históricos de alunos que ficam no DOC_ALUNOS
     */
    public static void gerarHistoricoAlunosMenu() {
        Controller controller = Controller.getInstance();
        controller.gerarHistoricoAlunos();
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
            for (char c: gab) if (c != 'F' || c != 'V') throw new IllegalArgumentException();
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
     * Mostra os resultados
     */
    public static void viewResultadosDisciplinasMenu() {
        File filePorNome = new File(DOC_RESULTADOS_POR_NOMES);
        System.out.println("Resultados ordenados por Nome");
        for (File resultadoFile: filePorNome.listFiles()) {
            if (!resultadoFile.isDirectory()) {
                System.out.println(resultadoFile.getName().replace(".txt", ""));
                try {
                    FileReader fileReader = new FileReader(resultadoFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    bufferedReader.lines().forEach(System.out::println);
                    bufferedReader.close();
                    fileReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
        }
        File filePorNotas = new File(DOC_RESULTADOS_POR_NOTAS);
        System.out.println("Resultados ordenados pelas Notas");
        for (File resultadoFile: filePorNotas.listFiles()) {
            if (!resultadoFile.isDirectory()) {
                System.out.println(resultadoFile.getName());
                try {
                    FileReader fileReader = new FileReader(resultadoFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    bufferedReader.lines().forEach(System.out::println);
                    bufferedReader.close();
                    fileReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
        }
    }

    /**
     * Mostra o contrudo dos arquivos contidos na pasta DOC_ALUNOS
     */
    public static void viewAlunosMenu() {
        File file = new File(DOC_ALUNOS); // diretorio como os arquivos dos gabaritos.
        Controller controller = Controller.getInstance();
        for (File alunoFile: file.listFiles()) {
            if (!alunoFile.isDirectory()) {
                System.out.println(alunoFile.getName().replace(".txt", ""));
                try {
                    FileReader fileReader = new FileReader(alunoFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    bufferedReader.lines().forEach(System.out::println);
                    bufferedReader.close();
                    fileReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println();
            }
        }
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

}
