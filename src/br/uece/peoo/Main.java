package br.uece.peoo;

import br.uece.peoo.controler.Controller;
import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;
import br.uece.peoo.util.Menu;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static br.uece.peoo.controler.Controller.*;

/**
 * Considere um arquivo texto onde cada linha representa as respostas de uma prova objetiva de um aluno.
 * Essa prova contém 10 questões, todas do tipo V ou F. O final de cada linha contém o nome do aluno que respondeu
 * aquelas opções separadas das respostas por um “tab”.
 */
public class Main {

    public static void main(String[] args) {

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

    // Exemplo de como pegar o caminho do arquivo onde está o gabarito.
    public static void teste() {
        JFrame frame = new JFrame("Robos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); // Não pode mudar o tramano do JFrame
        frame.setContentPane(frame.getContentPane());
        frame.pack();
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JFileChooser arquivo = new JFileChooser();
        FileNameExtensionFilter filtroPDF = new FileNameExtensionFilter("Gabarito", "txt");
        arquivo.setCurrentDirectory(new File("doc/"));
        arquivo.addChoosableFileFilter(filtroPDF);
        arquivo.setAcceptAllFileFilterUsed(false);
        arquivo.showOpenDialog(frame);
        System.out.println(arquivo.getSelectedFile().getAbsolutePath());

        System.exit(0);
    }

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
            char[] alunoRespostas = pegarGabaritoValido("Digite as resposta do aluno (Ex.: FVFVVVFVFF)");
            disciplina.addAluno(new Aluno(alunoName, alunoRespostas));
            System.out.println("Digite fim para parar ou nada para continuar");
            if (scanner.nextLine().equals("fim")) {
                flag = false;
            }
        }
        controler.fileFromDisciplina(disciplina);
    }

    private static void criarGabaritoMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do gabarito");
        String gabName = scanner.nextLine().toUpperCase();
        char[] gabResp = pegarGabaritoValido("Digite as respostas do Gabarito ex.: FFVVFFVFVF");
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

    private static void gerarResultadoDisciplinaMenu() {
        Controller controller = Controller.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome da Discplina");
        viewDisciplinasMenu(); // Mostar aqui as disciplinas que existem
        String nomeDisc = scanner.nextLine().toUpperCase();
        File fileDisc = new File(DOC_DISCIPLINAS + nomeDisc + ".txt");
        System.out.println("Digite o nome do Gabarito");
        viewGabaritosMenu(); // Mostrar aqui os gabaritos que existem
        String nomeGab = scanner.nextLine().toUpperCase();
        File fileGab = new File(DOC_GABARITOS + nomeGab + ".txt");
        Disciplina disciplina = controller.disciplinaFromFile(fileDisc);
        controller.gerarResultado(disciplina, controller.lendoGabarito(fileGab));
    }

    public static void gerarHistoricoAlunosMenu() {
        Controller controller = Controller.getInstance();
        controller.gerarHistoricoAlunos();
    }

    /**
     *
     * @param msg
     * @return
     */
    public static char[] pegarGabaritoValido(String msg) {
        Scanner scanner = new Scanner(System.in);
        char[] gab;
        System.out.println(msg);
        try {
            gab = scanner.nextLine().toUpperCase().toCharArray();
            if (gab.length != 10) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            System.err.println("Esse padão de Respostas não é válido.");
            gab = pegarGabaritoValido(msg);
        }
        return gab;
    }

    /**
     * Mostra os resultados
     */
    public static void viewResultadosDisciplinasMenu() {
        File filePorNome = new File(DOC_RESULTADOS_POR_NOME);
        System.out.println("Resultados ordenados por Nome");
        for (File resultadoFile: filePorNome.listFiles()) {
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

}
