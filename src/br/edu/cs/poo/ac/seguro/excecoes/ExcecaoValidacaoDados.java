package br.edu.cs.poo.ac.seguro.excecoes;

import java.util.ArrayList;
import java.util.List;

public class ExcecaoValidacaoDados extends Exception {

    private List<String> mensagens;

    public ExcecaoValidacaoDados() {
        super();
        mensagens = new ArrayList<>();
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    @Override
    public String getMessage() {
        // Pode concatenar todas as mensagens para exibir junto
        return String.join("; ", mensagens);
    }
}
