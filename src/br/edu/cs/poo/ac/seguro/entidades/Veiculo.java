package br.edu.cs.poo.ac.seguro.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Veiculo {
    private String placa;
    private int ano;
    private SeguradoEmpresa proprietarioEmpresa;
    private SeguradoPessoa proprietarioPessoa;
    private CategoriaVeiculo categoria;
}
