package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DadosVeiculo {
    private String cpfOuCnpj;
    private String placa;
    private int ano;
    private BigDecimal valorMaximoSegurado;
    private int codigoCategoria;
    void setCodigoCategoria(int codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }
    void setAno(int ano) {
        this.ano = ano;
    }
}