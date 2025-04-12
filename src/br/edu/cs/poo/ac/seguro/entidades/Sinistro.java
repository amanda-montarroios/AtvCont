package br.edu.cs.poo.ac.seguro.entidades;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Sinistro {
    private String numero;
    private Veiculo veiculo;
    private LocalDate dataHoraSinistro;
    private LocalDate dataHoraRegistro;
    private String usuarioRegistro;
    private BigDecimal valorSinistro;
    private TipoSinistro tipo;
}
