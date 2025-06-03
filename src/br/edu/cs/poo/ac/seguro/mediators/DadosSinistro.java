package br.edu.cs.poo.ac.seguro.mediators;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class DadosSinistro {
    private String placa;
    private LocalDateTime dataHoraSinistro;
    private String usuarioRegistro;
    private double valorSinistro;
    private int codigoTipoSinistro;

}
