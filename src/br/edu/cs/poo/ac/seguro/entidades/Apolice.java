package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Apolice implements Serializable, Registro{

    private static final long serialVersionUID = 1L;

    private String numero;
    private Veiculo veiculo;
    private BigDecimal valorFranquia;
    private BigDecimal valorPremio;
    private BigDecimal valorMaximoSegurado;
    private LocalDate dataInicioVigencia;

    @Override
    public String getIdUnico() {
        return numero;
    }
}