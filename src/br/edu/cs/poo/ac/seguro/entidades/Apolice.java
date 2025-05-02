package br.edu.cs.poo.ac.seguro.entidades;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class Apolice implements Serializable{

    private static final long serialVersionUID = 1L;

    private String numero;
    @NonNull private Veiculo veiculo;
    @NonNull private BigDecimal valorFranquia;
    @NonNull private BigDecimal valorPremio;
    @NonNull private BigDecimal valorMaximoSegurado;

    public Apolice(String numero, Veiculo veiculo, BigDecimal valorPremio, BigDecimal valorFranquia, BigDecimal valorMaximoSegurado) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.valorPremio = valorPremio;
        this.valorFranquia = valorFranquia;
        this.valorMaximoSegurado = valorMaximoSegurado;
    }


}