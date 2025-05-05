package br.edu.cs.poo.ac.seguro.testes;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TesteApoliceDAO extends TesteDAO {

    private ApoliceDAO dao = new ApoliceDAO();

    @Override
    protected Class getClasse() {
        return Apolice.class;
    }

    static {
        String sep = File.separator;
        File dir = new File("." + sep + Apolice.class.getSimpleName());
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private Veiculo veiculo = new Veiculo("JQK3B92",2005,null,null,null);

    @Test
    public void teste01() {
        String numero = "0";

        cadastro.incluir(new Apolice("0", veiculo, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now()),numero);
        Apolice seg = dao.buscar(numero);
        Assertions.assertNotNull(seg);
    }

    @Test
    public void teste02() {
        String numero = "0";

        boolean ret = dao.alterar(new Apolice("0", veiculo, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now()));
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste03(){
        String numero = "0";

        cadastro.incluir(new Apolice("0", veiculo, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now()),numero);
        Apolice seg = dao.buscar(numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
    }

    @Test
    public void teste04() {
        String numero = "0";

        cadastro.incluir(
                new Apolice("0", veiculo, new BigDecimal("330.00"), new BigDecimal("500.00"),
                        new BigDecimal("5000.00"), LocalDate.now()), numero);

        boolean ret = dao.excluir("10");
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste05() {
        String numero = "10";
        Apolice apolice = new Apolice(
                "1099",
                veiculo,
                new BigDecimal("320.00"),
                new BigDecimal("400.00"),
                new BigDecimal("523320.00"),
                LocalDate.now()
        );

        apolice.setNumero(numero);
        boolean ret = dao.incluir(apolice);
        Assertions.assertTrue(ret);
        Apolice apo = dao.buscar(numero);
        Assertions.assertNotNull(apo);
    }

    @Test
    public void teste06() {
        String numero = "0";

        Apolice apo = new Apolice(
                "999",
                veiculo,
                new BigDecimal("10000.00"),
                new BigDecimal("4000.00"),
                new BigDecimal("60000.00"),
                LocalDate.now()
        );
        apo.setNumero(numero);
        cadastro.incluir(apo, numero);
        boolean ret = dao.incluir(apo);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste07() {
        String numero = "0";

        Apolice apolice = new Apolice(
                numero,
                veiculo,
                new BigDecimal("4000.00"),
                new BigDecimal("900.00"),
                new BigDecimal("50000.00"),
                LocalDate.now()
        );
        boolean ret = dao.alterar(apolice);
        Assertions.assertFalse(ret);
        Apolice apo = dao.buscar(numero);
        Assertions.assertNull(apo);
    }

    @Test
    public void teste08() {
        String numero = "0";

        Apolice apo = new Apolice(
                numero,
                veiculo,
                new BigDecimal("10000.00"),
                new BigDecimal("1000.00"),
                new BigDecimal("100000.00"),
                LocalDate.now()
        );
        cadastro.incluir(apo, numero);
        apo = new Apolice(
                numero,
                veiculo,
                new BigDecimal("7000.00"),
                new BigDecimal("1000.00"),
                new BigDecimal("70000.00"),
                LocalDate.now()
        );
        boolean ret = dao.alterar(apo);
        Assertions.assertTrue(ret);
    }
}
