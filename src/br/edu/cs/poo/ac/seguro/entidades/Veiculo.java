package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Veiculo implements Serializable, Registro{

    private static final long serialVersionUID = 1L;

    private String placa;
    private int ano;
    private Segurado proprietario;
    private CategoriaVeiculo categoria;

    @Override
    public String getIdUnico() {
        return placa;
    }
}