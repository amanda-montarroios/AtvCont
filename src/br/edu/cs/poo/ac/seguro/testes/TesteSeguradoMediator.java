package br.edu.cs.poo.ac.seguro.testes;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoMediator;

public class TesteSeguradoMediator {
    private SeguradoMediator med = SeguradoMediator.getInstancia();

    @Test
    public void teste00() {
        String msg = "Nome deve ser informado";
        String resultado = med.validarNome(" ");
        assertEquals(msg, resultado);
        resultado = med.validarNome(null);
        assertEquals(msg, resultado);
    }
    @Test
    public void teste01() {
        Endereco endereco = null;
        String resultado = med.validarEndereco(endereco);
        assertEquals("Endereço deve ser informado", resultado);
    }

    @Test
    public void teste02() {
        String msg = "Logradouro deve ser informado";
        Endereco endereco = new Endereco("  ", "12345678", "10", "", "Brasil", "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
        endereco = new Endereco(null, "12345678", "10", "", "Brasil", "PE", "Recife");
        resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
    }

    @Test
    public void teste03() {
        String msg = "CEP deve ser informado";
        Endereco endereco = new Endereco("Rua A", "  ", "10", "", "Brasil", "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
        endereco = new Endereco("Rua A", null, "10", "", "Brasil", "PE", "Recife");
        resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
    }

    @Test
    public void teste04() {
        Endereco endereco = new Endereco("Rua A", "1234567", "10", "", "Brasil", "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals("Tamanho do CEP deve ser 8 caracteres", resultado);
        endereco = new Endereco("Rua A", "A1234567", "10", "", "Brasil", "PE", "Recife");
        resultado = med.validarEndereco(endereco);
        assertEquals("CEP deve ter formato NNNNNNNN", resultado);
    }

    @Test
    public void teste05() {
        String msg = "Cidade deve ser informada";
        Endereco endereco = new Endereco("Rua A", "12345678", "10", "", "Brasil", "PE", "  ");
        String resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
        endereco = new Endereco("Rua A", "12345678", "10", "", "Brasil", "PE", null);
        resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
    }

    @Test
    public void teste06() {
        Endereco endereco = new Endereco("Rua A", "12345678", "10", "", "Brasil", "PE", "A".repeat(101));
        String resultado = med.validarEndereco(endereco);
        assertEquals("Tamanho da cidade deve ser no máximo 100 caracteres", resultado);
    }

    @Test
    public void teste07() {
        String msg = "Sigla do estado deve ser informada";
        Endereco endereco = new Endereco("Rua A", "12345678", "10", "", "Brasil", "", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
        endereco = new Endereco("Rua A", "12345678", "", "", "Brasil", null, "Recife");
        resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
    }

    @Test
    public void teste08() {
        Endereco endereco = new Endereco("Rua A", "12345678", "10", "", "Brasil", "P", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals("Tamanho da sigla do estado deve ser 2 caracteres", resultado);
    }

    @Test
    public void teste09() {
        Endereco endereco = new Endereco("Rua A", "12345678", " ", "", "A".repeat(41), "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals("Tamanho do país deve ser no máximo 40 caracteres", resultado);
    }
    @Test
    public void teste10() {
        String msg = "País deve ser informado";
        Endereco endereco = new Endereco("Rua A", "12345678", null, "", " ", "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
        endereco = new Endereco("Rua A", "12345678", "10", "", null, "PE", "Recife");
        resultado = med.validarEndereco(endereco);
        assertEquals(msg, resultado);
    }
    @Test
    public void teste11() {
        Endereco endereco = new Endereco("Rua A", "12345678", "1".repeat(22), null, "A", "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals("Tamanho do número deve ser no máximo 20 caracteres", resultado);
    }
    @Test
    public void teste12() {
        Endereco endereco = new Endereco("Rua A", "12345678", "122", "A".repeat(32), "A", "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals("Tamanho do complemento deve ser no máximo 30 caracteres", resultado);
    }
    @Test
    public void teste13() {
        Endereco endereco = new Endereco("Rua A", "12345678", null, null, "BR", "PE", "Recife");
        String resultado = med.validarEndereco(endereco);
        assertEquals(null, resultado);
        endereco = new Endereco("Rua A", "12345678", "  ", "    ", "BR", "PE", "Recife");
        resultado = med.validarEndereco(endereco);
        assertEquals(null, resultado);
        endereco = new Endereco("Rua A", "12345678", "12", "AP 201", "BR", "PE", "Recife");
        resultado = med.validarEndereco(endereco);
        assertEquals(null, resultado);
    }

    @Test
    public void teste14() {
        String msg = "Nome deve ser informado";
        String resultado = med.validarNome(null);
        assertEquals(msg, resultado);
        resultado = med.validarNome("  ");
        assertEquals(msg, resultado);
    }
    @Test
    public void teste15() {
        String resultado = med.validarNome("A".repeat(122));
        assertEquals("Tamanho do nome deve ser no máximo 100 caracteres", resultado);
    }
    @Test
    public void teste16() {
        String resultado = med.validarNome("Carlos");
        assertEquals(null, resultado);
    }
    @Test
    public void teste17() {
        String resultado = med.validarDataCriacao(null);
        assertEquals("Data da criação deve ser informada", resultado);
    }
    @Test
    public void teste18() {
        String resultado = med.validarDataCriacao(LocalDate.now().plusDays(1));
        assertEquals("Data da criação deve ser menor ou igual à data atual", resultado);
    }
    @Test
    public void teste19() {
        String resultado = med.validarDataCriacao(LocalDate.now().minusDays(2));
        assertEquals(null, resultado);
    }
    @Test
    public void teste20() {
        BigDecimal bonus = new BigDecimal("100.00");
        BigDecimal valorDebito = new BigDecimal("120.00");
        BigDecimal bonusAjustado = med.ajustarDebitoBonus(bonus, valorDebito);
        assertEquals(bonus, bonusAjustado);
        bonus = new BigDecimal("140.00");
        bonusAjustado = med.ajustarDebitoBonus(bonus, valorDebito);
        assertEquals(valorDebito, bonusAjustado);
    }
}