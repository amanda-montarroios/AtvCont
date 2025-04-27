package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class TesteSinistroDAO extends TesteDAO{

    private SinistroDAO dao = new SinistroDAO();

    protected Class<?> getClasse() {
        return Sinistro.class;
    }

    Veiculo veiculo = new Veiculo("ABC123", 2025, null, null, null);

    @Test
    public void teste01() {
        String numero = "0";

        cadastro.incluir(
                new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO),
                numero);

        Sinistro sin = dao.buscar(numero);
        Assertions.assertNotNull(sin);
    }

    @Test
    public void teste02() {
        String numero = "1";

        cadastro.incluir(
                new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO),
                numero);
        Sinistro sin = dao.buscar("11");
        Assertions.assertNull(sin);
    }

    @Test
    public void teste03() {
        String numero = "2";

        cadastro.incluir(
                new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO),
                numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
    }

    @Test
    public void teste04() {
        String numero = "3";

        cadastro.incluir(
                new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO),
                numero);
        boolean ret = dao.excluir("33");
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste05() {
        String numero = "4";

        Sinistro sinistro = new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO);
        boolean ret = dao.incluir(sinistro);
        Assertions.assertTrue(ret);
        Sinistro sin = dao.buscar(numero);
        Assertions.assertNotNull(sin);
    }

    @Test
    public void teste06() {
        String numero = "5";

        Sinistro sinistro = new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO);
        cadastro.incluir(sinistro, numero);
        boolean ret = dao.incluir(sinistro);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste07() {
        String numero = "6";

        boolean ret = dao.alterar(new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO));
        Assertions.assertFalse(ret);
        Sinistro sin = dao.buscar(numero);
        Assertions.assertNull(sin);
    }

    @Test
    public void teste08() {
        String numero = "7";

        Sinistro sin = new Sinistro(numero, veiculo, null, null, null, new BigDecimal("0"), TipoSinistro.COLISAO);
        cadastro.incluir(sin, numero);
        sin = new Sinistro(numero, veiculo, null, null, null, new BigDecimal("1"), TipoSinistro.FURTO);
        boolean ret = dao.alterar(sin);
        Assertions.assertTrue(ret);
    }
}
