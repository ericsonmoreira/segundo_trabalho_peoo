package br.uece.peoo.model;

public class Aluno {

    private String nome;

    private char[] respostas;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public char[] getRespostas() {
        return respostas;
    }

    public void setRespostas(char[] respostas) {
        this.respostas = respostas;
    }

    /**
     * Construtor padrão.
     * @param nome Nome do aluno
     * @param respostas vertor com as resposatas do aluno
     */
    public Aluno(String nome, char[] respostas) {
        this.nome = nome;
        this.respostas = respostas;
    }

    /**
     * Calcula os acertos do {@link Aluno} por um gabarito.
     * @param gabarito {@link String} como o Gabarito.
     * @return
     */
    public int getAcertos(String gabarito) {
        if (this.respostasTodasIguais()) return 0;
        int count = 0;
        char[] gabChars = gabarito.replace("\n", "").toCharArray(); // caracteres da String gabarito
        for (int i = 0; i < gabChars.length; i++) if (this.respostas[i] == gabChars[i]) count++;
        return count;
    }

    /**
     * Verifica se todas as respostas são iguais.
     * @return
     */
    private boolean respostasTodasIguais() {
        char primeiro = respostas[0];
        for (int i = 1; i < respostas.length; i++) {
            if (respostas[i] != primeiro) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String result = new String(respostas);
        return result + "\t" + this.nome;
    }
}
