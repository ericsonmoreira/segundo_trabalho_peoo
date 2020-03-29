package br.uece.peoo;

import br.uece.peoo.controler.DisciplinaControler;
import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static br.uece.peoo.controler.DisciplinaControler.DOC_GABARITOS;
import static br.uece.peoo.controler.DisciplinaControler.DOC_DISCIPLINAS;
import static br.uece.peoo.controler.DisciplinaControler.DOC_RESULTADOS;

/**
 * Considere um arquivo texto onde cada linha representa as respostas de uma prova objetiva de um aluno.
 * Essa prova contém 10 questões, todas do tipo V ou F. O final de cada linha contém o nome do aluno que respondeu
 * aquelas opções separadas das respostas por um “tab”.
 */
public class Main {

    public static void main(String[] args) {
        // teste();

        DisciplinaControler controler = DisciplinaControler.getInstance();

        // Escolhendo a Disciplina
        Disciplina peoo = controler.findDisciplina("PEOO");
        Disciplina peoo2 = controler.findDisciplina("PEOO2");

        String gabarito = controler.lendoGabarito(
                new File(DOC_GABARITOS + "gabarito01.txt"));

        controler.gerarResultado(peoo, gabarito);
        controler.gerarResultado(peoo2, gabarito);


        controler.gerarHistoricoAlunos();

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

}
