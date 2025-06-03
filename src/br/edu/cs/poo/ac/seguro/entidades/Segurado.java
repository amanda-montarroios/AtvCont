package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public abstract class Segurado implements Serializable, Registro{

    private static final long serialVersionUID = 1L;

    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus;

    public Segurado(String nome, Endereco endereco, LocalDate dataCriacao, BigDecimal bonus) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCriacao = dataCriacao;
        this.bonus = bonus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    protected LocalDate getDataCriacao() {
        return dataCriacao;
    }

    protected void setDataCriacao(LocalDate data) {
        this.dataCriacao = data;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public int getIdade() {
        LocalDate agora = LocalDate.now();
        int idade = Period.between(getDataCriacao(), agora).getYears();
        return idade;
    }

    public void creditarBonus(BigDecimal valor) {
        bonus = bonus.add(valor);
    }

    public void debitarBonus(BigDecimal valor) {
        if (bonus.compareTo(valor) >= 0) {
            bonus = bonus.subtract(valor);
        }
    }

    public abstract boolean isEmpresa();
}