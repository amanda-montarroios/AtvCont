package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;

public class TestesEntidades {

    @Test
    public void teste01() {
        TipoSinistro ts = TipoSinistro.getTipoSinistro(1);
        Assertions.assertEquals(ts, TipoSinistro.COLISAO);
        ts = TipoSinistro.getTipoSinistro(5);
        Assertions.assertEquals(ts, TipoSinistro.DEPREDACAO);
        ts = TipoSinistro.getTipoSinistro(4);
        Assertions.assertEquals(ts, TipoSinistro.ENCHENTE);
        ts = TipoSinistro.getTipoSinistro(3);
        Assertions.assertEquals(ts, TipoSinistro.FURTO);
        ts = TipoSinistro.getTipoSinistro(2);
        Assertions.assertEquals(ts, TipoSinistro.INCENDIO);
    }
    @Test
    public void teste02() {
        TipoSinistro ts = TipoSinistro.getTipoSinistro(7);
        Assertions.assertNull(ts);
    }
    @Test
    public void teste03() {
        SeguradoEmpresa seg = new SeguradoEmpresa("JOCA", null, null, BigDecimal.ZERO, null, 0.0, false);
        seg.creditarBonus(new BigDecimal("100.00"));
        seg.creditarBonus(new BigDecimal("50.00"));
        Assertions.assertEquals(seg.getBonus(), new BigDecimal("150.00"));
    }
    @Test
    public void teste04() {
        SeguradoPessoa seg = new SeguradoPessoa("MARIA", null, null, new BigDecimal("200.00"), null, 0.0);
        seg.debitarBonus(new BigDecimal("40.00"));
        seg.debitarBonus(new BigDecimal("20.00"));
        Assertions.assertEquals(seg.getBonus(), new BigDecimal("140.00"));
    }
    @Test
    public void teste05() {
        int ano = LocalDate.now().getYear() - 10;
        int mes = LocalDate.now().getMonth().getValue();
        int dia = LocalDate.now().getDayOfMonth();
        SeguradoPessoa seg = new SeguradoPessoa("ACB LTDA", null, LocalDate.of(ano, mes, dia), null, null, 0.0);
        Assertions.assertEquals(seg.getIdade(), 10);
    }
}