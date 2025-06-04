package br.edu.cs.poo.ac.seguro.testes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.SeguradoDAO;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

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
    @Test
    public void teste06() {
        Segurado seg = new SeguradoEmpresa("ED", null, LocalDate.now(), null, "1354654", 0, false);
        Veiculo v = new Veiculo("KKK0000", 2021, seg, CategoriaVeiculo.SUPER_LUXO);
        Assertions.assertTrue(seg == v.getProprietario());
        seg = new SeguradoPessoa("ED1", null, LocalDate.now(), null, "12344", 0);
        v.setProprietario(seg);
        Assertions.assertTrue(seg == v.getProprietario());
    }
    @Test
    public void teste07() {
        int seq = 1212;
        String apo = "4567";
        Sinistro s = new Sinistro("123", null, LocalDateTime.now(), null,
                null, null, TipoSinistro.COLISAO);
        s.setSequencial(seq);
        s.setNumeroApolice(apo);
        Assertions.assertEquals(seq, s.getSequencial());
        Assertions.assertEquals(apo, s.getNumeroApolice());
    }
    @Test
    public void teste08() {
        Assertions.assertTrue(Modifier.isAbstract(Segurado.class.getModifiers()));
        Segurado seg = new SeguradoEmpresa("ED", null, LocalDate.now(), null, "1354654", 0, false);
        Assertions.assertTrue(seg.isEmpresa());
        seg = new SeguradoPessoa("ED1", null, LocalDate.now(), null, "12344", 0);
        Assertions.assertFalse(seg.isEmpresa());
        try {
            Method method = Segurado.class.getDeclaredMethod("isEmpresa");
            Assertions.assertTrue(Modifier.isAbstract(method.getModifiers()));
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}