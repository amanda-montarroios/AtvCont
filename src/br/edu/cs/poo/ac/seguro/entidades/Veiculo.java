package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Veiculo implements Serializable, Registro {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private String placa;

    private int ano;
    private Segurado proprietario;
    private CategoriaVeiculo categoria;

    @Override
    public String getIdUnico() {
        return this.placa;
    }
}