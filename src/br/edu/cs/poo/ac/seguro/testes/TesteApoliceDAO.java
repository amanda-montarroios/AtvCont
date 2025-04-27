package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class TesteApoliceDAO extends TesteDAO {
    private ApoliceDAO dao = new ApoliceDAO();

    protected Class<Apolice> getClasse() {
        return Apolice.class;
    }
    Veiculo veiculo = new Veiculo("ABC123", 2025, null, null, null);

    @Test
    public void teste01() {
        String numero = "0";

        cadastro.incluir(
                new Apolice(veiculo, new BigDecimal("1000.00"), new BigDecimal("500.00"), new BigDecimal("10000.00")),
                numero);

        Apolice apo = dao.buscar(numero);
        Assertions.assertNotNull(apo);
    }

    @Test
    public void teste02() {
        String numero = "1";

        cadastro.incluir(
                new Apolice(veiculo, new BigDecimal("2000.00"), new BigDecimal("600.00"), new BigDecimal("20000.00")),
                numero);

        Apolice apo = dao.buscar("2");
        Assertions.assertNull(apo);
    }

    @Test
    public void teste03() {
        String numero = "2";

        cadastro.incluir(
                new Apolice(veiculo, new BigDecimal("3000.00"), new BigDecimal("700.00"), new BigDecimal("30000.00")),
                numero);

        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
    }

    @Test
    public void teste04() {
        String numero = "3";

        cadastro.incluir(
                new Apolice(veiculo, new BigDecimal("4000.00"), new BigDecimal("800.00"), new BigDecimal("40000.00")),
                numero);

        boolean ret = dao.excluir("33");
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste05() {
        String numero = "4";

        Apolice apolice = new Apolice(veiculo, new BigDecimal("5000.00"), new BigDecimal("900.00"), new BigDecimal("50000.00"));
        apolice.setNumero(numero);
        boolean ret = dao.incluir(apolice);
        Assertions.assertTrue(ret);
        Apolice apo = dao.buscar(numero);
        Assertions.assertNotNull(apo);
    }

    @Test
    public void teste06() {
        String numero = "5";

        Apolice apo = new Apolice(veiculo, new BigDecimal("6000.00"), new BigDecimal("1000.00"),
                new BigDecimal("60000.00"));
        apo.setNumero(numero);
        cadastro.incluir(apo, numero);
        boolean ret = dao.incluir(apo);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste07() {
        String numero = "6";

        boolean ret = dao
                .alterar(new Apolice(veiculo, new BigDecimal("7000.00"), new BigDecimal("1100.00"),
                        new BigDecimal("70000.00")));
        Assertions.assertFalse(ret);
        Apolice apo = dao.buscar(numero);
        Assertions.assertNull(apo);
    }

    @Test
    public void teste08() {
        String numero = "7";

        Apolice apo = new Apolice(veiculo, new BigDecimal("8000.00"), new BigDecimal("1200.00"),
                new BigDecimal("80000.00"));
        apo.setNumero(numero);
        cadastro.incluir(apo, numero);
        apo = new Apolice(veiculo, new BigDecimal("9000.00"), new BigDecimal("1300.00"),
                new BigDecimal("90000.00"));
        apo.setNumero(numero);
        boolean ret = dao.alterar(apo);
        Assertions.assertTrue(ret);
    }
}
