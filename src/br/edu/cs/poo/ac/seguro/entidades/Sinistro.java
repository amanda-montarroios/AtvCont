package br.edu.cs.poo.ac.seguro.entidades;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class Sinistro implements Serializable {

    private String numero;
    @NonNull private Veiculo veiculo;
    @NonNull private LocalDateTime dataHoraSinistro;
    @NonNull private LocalDateTime dataHoraRegistro;
    @NonNull private String usuarioRegistro;
    @NonNull private BigDecimal valorSinistro;
    @NonNull private TipoSinistro tipo;
}
