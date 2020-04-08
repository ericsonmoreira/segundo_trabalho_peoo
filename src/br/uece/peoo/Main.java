package br.uece.peoo;

import br.uece.peoo.control.Controller;
import br.uece.peoo.model.Aluno;
import br.uece.peoo.model.Disciplina;
import br.uece.peoo.util.Menu;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static br.uece.peoo.control.Controller.*;

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
        for (File disciplinaFile: file.listFiles()) {
            if (!disciplinaFile.isDirectory()) { // apenas os arquivos que não são diretórios.
                Disciplina disciplina = disciplinaFromFile(disciplinaFile);
                System.out.println(disciplina);
            }
        }
    }

    /**
     * Cria uma disciplina e a salca na pasta DOC_DISCIPLINAS
     */
    private static void criarDisciplinaMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nome da Disciplina");
        String discName = scanner.nextLine().toUpperCase();
        Disciplina disciplina = new Disciplina(discName); // nova disciplina.
        boolean flag = true;
        while (flag) {
            Aluno aluno;
            System.out.println("Digite o aluno (Ex.: Fulano de Tal)");
            String alunoName = scanner.nextLine().toUpperCase();
            char[] alunoRespostas = validarGabarito("Digite as resposta do aluno (Ex.: FVFVVVFVFF)");
            disciplina.addAluno(new Aluno(alunoName, alunoRespostas));
            System.out.println("Digite fim para parar ou nada para continuar");
            if (scanner.nextLine().equals("fim")) {
                flag = false;
            }
        }
        fileFromDisciplina(disciplina);
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
     *  Mostra todos os gabaritos da pasta DOC_GABARITOS
     */
    private static void viewGabaritosMenu() {
        File file = new File(DOC_GABARITOS); // diretorio como os arquivos dos gabaritos.
        for (File gabFile: file.listFiles()) {
            if (!gabFile.isDirectory()) {
                String gabName = gabFile.getName().replace(".txt", "");
                String gabResp = lerArquivo(gabFile);
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
        // verifica se há disciplinas cadastradas
        if (!existeDiscipliaCadastrada()) {
            System.err.println("Nenhuma Disciplina Cadastrasda. Use a opção [Criar Disciplina].");
            return;
        }
        // verifica se há gabaritos cadastrados
        if (!existeGabaritoCadastrado()) {
            System.err.println("Nenhum Gabarito Cadastrasda. Use a opção [Criar Gabarito].");
            return;
        }
        Scanner scanner = new Scanner(System.in);

        // Seleciona a disciplina
        System.out.println("Disciplinas:");
        viewDisciplinasMenu(); // Mostar aqui as disciplinas que existem
        List<String> nomesDisciplinas = getNomesDisciplinas(); // pega os nomes das disciplinas
        String nomeDisc = existeDisciplina("Digite o nome da disciplina", nomesDisciplinas);
        File fileDisc = new File(DOC_DISCIPLINAS + nomeDisc + ".txt");
        Disciplina disciplina = disciplinaFromFile(fileDisc); // pegando a disciplina

        // Seleciona o gabarito
        System.out.println("Gabaritos:");
        viewGabaritosMenu(); // Mostrar aqui os gabaritos que existem
        List<String> nomesGabaritos = getNomesGabaritos(); // pega os nomes dos gabaritos
        String nomeGab = existeGabarito("Digite o nome do Gabarito", nomesGabaritos);
        File fileGab = new File(DOC_GABARITOS + nomeGab + ".txt");
        String gabarito = lerArquivo(fileGab); // pegando o gabarito

        // Lista de alunos ordenados pelo nome
        List<Aluno> alunosPorNomes = disciplina.getAlunos().stream().
                sorted(Comparator.comparing(Aluno::getNome)). // Comparado por nome
                collect(Collectors.toList());

        // Lista de alunos ordenados pela nota decrescentemente
        List<Aluno> alunosPorNotas = disciplina.getAlunos().stream().
                sorted(Comparator.comparing(aluno -> aluno.getAcertos(gabarito), Comparator.reverseOrder())).
                collect(Collectors.toList());

        // Média geram dos alunos
        double mediaGeral = alunosPorNomes.stream().
                mapToDouble(aluno -> aluno.getAcertos(gabarito)).average().getAsDouble();

        // Arquivos de resultados
        File fileResultadoPorNomes = new File(DOC_RESULTADOS_POR_NOMES + disciplina.getNome() + ".txt");
        File fileResultadoPorNotas = new File(DOC_RESULTADOS_POR_NOTAS + disciplina.getNome() + ".txt");

        // Cria o conteudo do resultado por ordenado por nomes
        String texto = "";
        for (Aluno aluno: alunosPorNomes) {
            texto += aluno.getNome() + "\t" + aluno.getAcertos(gabarito) + "\n";
        }
        texto += "Média\t" + mediaGeral;
        escreverArquivo(fileResultadoPorNomes, texto);

        // Cria o conteudo do resultado ordenado por notas
        texto = "";
        for (Aluno aluno: alunosPorNotas) {
            texto += aluno.getNome() + "\t" + aluno.getAcertos(gabarito) + "\n";
        }
        texto += "Média\t" + mediaGeral;
        escreverArquivo(fileResultadoPorNotas, texto);
    }

    /**
     *  Gera os arquivos de históricos de alunos que ficam no DOC_ALUNOS
     */
    public static void gerarHistoricoAlunosMenu() {
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
        System.out.println("Historicos dos Alunos Gerados. Para visualizar use a opção [Visualizar Alunos.]");
    }

    /**
     * Mostra os resultados contidos nas pastas
     * DOC_RESULTADOS_POR_NOMES
     * DOC_RESULTADOS_POR_NOTAS
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
        for (File alunoFile: file.listFiles()) {
            if (!alunoFile.isDirectory()) {
                System.out.println(alunoFile.getName().replace(".txt", ""));
                System.out.println(lerArquivo(alunoFile));
            }
        }
    }

}
