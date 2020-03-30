package br.uece.peoo;

import br.uece.peoo.controler.DisciplinaControler;
import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;
import br.uece.peoo.util.Menu;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.*;

import static br.uece.peoo.controler.DisciplinaControler.DOC_DISCIPLINAS;

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
        menu.addOption(3, "Gerar Resultado de uma Disciplina.", () -> { });
        menu.addOption(4, "Criar Histórico de dos Alunos", () -> { /* nada por enquanto */});
        menu.addOption(5, "Criar Criar Gabarito", () -> criarGabaritoMenu());

        menu.addOption(99, "Sair do programa.", () -> System.exit(0)); // fechar o programa

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

        System.out.println("viewDisciplinas");
        File file = new File(DOC_DISCIPLINAS);

        DisciplinaControler controler = DisciplinaControler.getInstance();

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
        DisciplinaControler controler = DisciplinaControler.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nome da Disciplina");
        String discName = scanner.nextLine();
        Disciplina disciplina = new Disciplina(discName); // nova disciplina.
        boolean flag = true;
        while (flag) {
            Aluno aluno;
            System.out.println("Digite o aluno (Ex.: Fulano de Tal)");
            String alunoName = scanner.nextLine();
            System.out.println("Digite o as resposta do aluno (Ex.: FVFVVVFVFF)");
            String alunoRespostas = scanner.nextLine();
            disciplina.addAluno(new Aluno(alunoName, alunoRespostas.toCharArray()));
            System.out.println("Digite fim para parar ou nada para continuar");
            if (scanner.nextLine().equals("fim")) {
                flag = false;
            }
        }
        controler.fileFromDisciplina(disciplina);
    }

    private static void criarGabaritoMenu() {
        // nada ainda
    }

}
