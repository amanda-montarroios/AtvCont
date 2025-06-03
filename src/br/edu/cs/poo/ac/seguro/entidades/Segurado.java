package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public abstract class Segurado implements Serializable, Registro {
    private static final long serialVersionUID = 1L;
    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus;

    public Segurado(String nome, Endereco endereco, LocalDate dataCriacao, BigDecimal bonus) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCriacao = dataCriacao;
        this.bonus = (bonus == null) ? BigDecimal.ZERO : bonus;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    protected LocalDate getDataCriacao() { return dataCriacao; }
    protected void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
    public BigDecimal getBonus() { return bonus; }

    public int getIdade() {
        if (this.dataCriacao == null) {
            return 0;
        }
        return Period.between(this.dataCriacao, LocalDate.now()).getYears();
    }

    public void creditarBonus(BigDecimal valor) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) > 0) {
            if (this.bonus == null) { // Garante inicialização se for nulo
                this.bonus = BigDecimal.ZERO;
            }
            this.bonus = this.bonus.add(valor);
        }
    }

    public void debitarBonus(BigDecimal valor) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) > 0) {
            if (this.bonus == null) { // Não pode debitar de nulo
                this.bonus = BigDecimal.ZERO;
                return;
            }
            if (this.bonus.compareTo(valor) >= 0) {
                this.bonus = this.bonus.subtract(valor);
            } else {
                this.bonus = BigDecimal.ZERO;
            }
        }
    }

    public abstract boolean isEmpresa();

    @Override
    public abstract String getIdUnico();
}