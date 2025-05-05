package br.edu.cs.poo.ac.seguro.mediators;

import lombok.Getter;

@Getter
public class RetornoInclusaoApolice {
    private String numeroApolice;
    private String mensagemErro;

    public RetornoInclusaoApolice(String numeroApolice, String mensagemErro) {
        if (numeroApolice == null && mensagemErro == null) {
            throw new RuntimeException(
                    "Número da apólice e mensagem de erro não podem ser ambas nulas");
        }
        if (numeroApolice != null && mensagemErro != null) {
            throw new RuntimeException(
                    "Número da apólice e mensagem de erro não podem ser ambas preenchidas");
        }
        this.numeroApolice = numeroApolice;
        this.mensagemErro = mensagemErro;
    }
}