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

        String gabarito = controler.lendoGabarito(
                new File(DOC_GABARITOS + "gabarito01.txt"));

        gerarResultado(peoo, gabarito);

    }

    public static void gerarResultado(Disciplina disciplina, String gabarito) {
        /* Seu programa deve permitir ao usuário escolher a disciplina e então informar a localização do arquivo
        contendo o gabarito oficial da prova (apenas uma linha com as 10 respostas corretas) da disciplina escolhida.
        Em seguida, deve produzir como resposta dois outros arquivos: um contendo a lista dos alunos e seus
        respectivos pontos (número de acertos) ordenadas por ordem alfabética, e outro contendo as mesmas informações,
        porém ordenado por ordem decrescente de notas (quantidade de acertos) e mostrando ao final a média da turma.
        Caso o aluno tenha marcado todas as questões com V ou F, o aluno receberá a 0. Permita ao usuário visualizar
        esses dados na tela. */


        disciplina.getAlunos().forEach(aluno -> System.out.println(aluno.getAcertos(gabarito)));




        // produzir como resposta dois outros arquivos:
        // primeiro com a lista dos alunos e seus respectivos pontos (número de acertos) ordenadas por ordem alfabética.
        List<Aluno> alunosOAlfa = disciplina.getAlunos().stream().
                sorted(Comparator.comparing(Aluno::getNome)). // Comparado por nome
                collect(Collectors.toList());

        // Média geram dos alunos
        double mediaGeral = alunosOAlfa.stream().
                mapToDouble(aluno -> aluno.getAcertos(gabarito)).average().getAsDouble();

        File alunosOAlfaFile = new File(DOC_RESULTADOS + disciplina.getNome() + "_alfa.txt");

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

        System.out.println();

        alunosOAlfa.forEach(aluno -> {
            System.out.println(aluno.getNome() + " Nota:"+ aluno.getAcertos(gabarito));
        });

        // com a lista dos alunos e seus respectivos pontos ordenado por ordem decrescente de notas (quantidade de acertos)
        List<Aluno> alunosAcertos = disciplina.getAlunos().stream().
                sorted(Comparator.comparing(aluno -> aluno.getAcertos(gabarito), Comparator.reverseOrder())).
                collect(Collectors.toList());

        File alunosAcertosFile = new File(DOC_RESULTADOS + disciplina.getNome() + "_acertos.txt");

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

        System.out.println();

        alunosAcertos.forEach(aluno -> {
            System.out.println(aluno.getNome() + " Nota:"+ aluno.getAcertos(gabarito));
        });
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
