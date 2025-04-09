package br.edu.cs.poo.ac.seguro.entidades;

public class PrecoAno {
    private int ano;
    private double preco;

    public PrecoAno(int ano, double preco) {
        super();
        this.ano = ano;
        this.preco = preco;
    }
    public int getAno() {
        return ano;
    }
    public double getPreco() {
        return preco;
    }
}
