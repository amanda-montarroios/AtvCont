package br.edu.cs.poo.ac.seguro.testes;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TesteSinistroDAO extends TesteDAO {

    private SinistroDAO dao = new SinistroDAO();

    @Override
    protected Class getClasse() {
        return Sinistro.class;
    }

    static {
        String sep = File.separator;
        File dir = new File("." + sep +Sinistro.class.getSimpleName());
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    TipoSinistro tiposinistro = TipoSinistro.COLISAO;
    TipoSinistro tipo = TipoSinistro.getTipoSinistro(tiposinistro.getCodigo());

    private Veiculo veiculo = new Veiculo("JQK3B92",2005,null,null,null);

    @Test
    public void teste01() {
        String numero = "0";
        Sinistro sinistro = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_101", BigDecimal.ZERO, tipo);
        cadastro.incluir(sinistro, numero);
        Sinistro seg = dao.buscar(numero);
        Assertions.assertNotNull(seg);
    }

    @Test
    public void teste02() {
        String numero = "0";
        Sinistro sinistro = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_102", BigDecimal.ZERO, tipo);
        boolean ret = dao.alterar(sinistro);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste03() {
        String numero = "0";
        Sinistro sinistro = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_101", BigDecimal.ZERO, tipo);
        cadastro.incluir(sinistro, numero);
        Sinistro seg = dao.buscar(numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
    }

    @Test
    public void teste04() {
        String numero = "0";
        Sinistro sinistro = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_101", BigDecimal.ZERO, tipo);
        cadastro.incluir(sinistro, numero);
        boolean ret = dao.excluir("10");
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste05() {
        String numero = "0";
        Sinistro sinistro = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_101", BigDecimal.ZERO, tipo);
        boolean ret = dao.incluir(sinistro);
        Assertions.assertTrue(ret);
        Sinistro sinistro1 = dao.buscar(numero);
        Assertions.assertNotNull(sinistro1);
    }

    @Test
    public void teste06() {
        String numero = "0";
        Sinistro sinistro = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_101", BigDecimal.ZERO, tipo);
        cadastro.incluir(sinistro, numero);
        boolean ret = dao.incluir(sinistro);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste07() {
        String numero = "0";
        Sinistro sinistro = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_101", BigDecimal.ZERO, tipo);
        boolean ret = dao.alterar(sinistro);
        Assertions.assertFalse(ret);
        Sinistro apo = dao.buscar(numero);
        Assertions.assertNull(apo);
    }

    @Test
    public void teste08() {
        String numero = "0";
        Sinistro apo = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_101", BigDecimal.ZERO, tipo);
        cadastro.incluir(apo, numero);

        apo = new Sinistro(numero, veiculo, LocalDateTime.now(), LocalDateTime.now(), "br_102", new BigDecimal("10"), tipo);
        boolean ret = dao.alterar(apo);
        Assertions.assertTrue(ret);
    }
}