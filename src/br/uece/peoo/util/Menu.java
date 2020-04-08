package br.uece.peoo.util;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Classe responsável por gerenciar as opções de um programa.
 */
public class Menu {

    public Runnable getRunnable(int id) {
        try {
            Optional<Option> option = Optional.ofNullable(options.stream().filter(o -> o.idOp == id).findFirst().get());
            return option.get().R;
        } catch (Exception e) {
            return null;
        }
    }

    // Classe interna representando uma opcao.
    private static class Option{
        int idOp;
        String msgOp;
        Runnable R;

        public Option(int idOp, String msgOp, Runnable r) {
            this.idOp = idOp;
            this.msgOp = msgOp;
            R = r;
        }
    }

    private ArrayList<Option> options;

    public Menu(){
        this.options = new ArrayList();
    }

    public void addOption(int id, String msg, Runnable R){
        this.options.add(new Option(id,id + " - " + msg, R));
    }

    public void printMenu(){
        System.out.println("------------------- Menu ---------------------");
        this.options.forEach(option -> System.out.println(option.msgOp));
    }


}
