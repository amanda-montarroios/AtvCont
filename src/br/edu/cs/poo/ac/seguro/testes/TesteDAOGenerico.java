package br.edu.cs.poo.ac.seguro.testes;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.DAOGenerico;
import br.edu.cs.poo.ac.seguro.daos.SeguradoDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class TesteDAOGenerico extends TesteMediator {
    private CadastroObjetos cadastro = new CadastroObjetos(RegistroImpl.class);
    private DAORegistroImpl dao = new DAORegistroImpl();

    @Test
    public void test00() {
        Assertions.assertTrue(new SeguradoEmpresaDAO() instanceof SeguradoDAO);
        Assertions.assertTrue(new SeguradoPessoaDAO() instanceof SeguradoDAO);
        Assertions.assertTrue(new ApoliceDAO() instanceof DAOGenerico);
        Assertions.assertTrue(new SinistroDAO() instanceof DAOGenerico);
        Assertions.assertTrue(new VeiculoDAO() instanceof DAOGenerico);
        SeguradoDAO seg = new SeguradoDAO() {
            public Class getClasseEntidade() {
                return null;
            }
        };
        Assertions.assertTrue(seg instanceof DAOGenerico);
        Registro r = new Registro() {
            public String getIdUnico() {
                return null;
            }
        };
        Assertions.assertTrue(r instanceof Serializable);
    }
    @Test
    public void test01() {
        String cnpj = "12345678901234";
        String cpf = "12345678901";
        String placa = "KKK0066";
        String numeroApolice = "1234567890";
        String numeroSinistro = "0987654321";
        Registro r1 = new SeguradoEmpresa("ACME", null, LocalDate.now(), null,
                cnpj, 0, false);
        Assertions.assertEquals(cnpj, r1.getIdUnico());
        r1 = new SeguradoPessoa("ED", null, LocalDate.now(), null,
                cpf, 0.0);
        Assertions.assertEquals(cpf, r1.getIdUnico());
        r1 = new Veiculo(placa, 2020, null, null);
        Assertions.assertEquals(placa, r1.getIdUnico());
        r1 = new Apolice(numeroApolice, null, null, null, null, null);
        Assertions.assertEquals(numeroApolice, r1.getIdUnico());
        r1 = new Sinistro(numeroSinistro, null, null, null, null, null, null);
        Assertions.assertEquals(numeroSinistro, r1.getIdUnico());
    }
    @Test
    public void test02() {
        String codigo = "123";
        RegistroImpl reg = new RegistroImpl(codigo, "EDT");
        cadastro.incluir(reg, codigo);
        Assertions.assertFalse(dao.incluir(reg));
    }
    @Test
    public void test03() {
        String codigo = "123";
        RegistroImpl reg = new RegistroImpl(codigo, "EDT");
        cadastro.incluir(reg, codigo);
        RegistroImpl reg1 = new RegistroImpl("456", "EDT");
        Assertions.assertTrue(dao.incluir(reg1));
        RegistroImpl reg2 = (RegistroImpl)cadastro.buscar(reg1.codigo);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(reg2, reg1));
    }
    @Test
    public void test04() {
        String codigo = "123";
        RegistroImpl reg = new RegistroImpl(codigo, "EDT");
        Assertions.assertFalse(dao.alterar(reg));
    }
    @Test
    public void test05() {
        String codigo = "123";
        RegistroImpl reg = new RegistroImpl(codigo, "EDT");
        cadastro.incluir(reg, codigo);
        RegistroImpl reg1 = new RegistroImpl(codigo, "EDT1");
        Assertions.assertTrue(dao.alterar(reg1));
        RegistroImpl reg2 = (RegistroImpl)cadastro.buscar(reg1.codigo);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(reg2, reg1));
    }
    @Test
    public void test06() {
        String codigo = "123";
        Assertions.assertFalse(dao.excluir(codigo));
    }
    @Test
    public void test07() {
        String codigo = "123";
        RegistroImpl reg = new RegistroImpl(codigo, "EDT");
        cadastro.incluir(reg, codigo);
        Assertions.assertTrue(dao.excluir(codigo));
        RegistroImpl reg2 = (RegistroImpl)cadastro.buscar(codigo);
        Assertions.assertNull(reg2);
    }
    @Test
    public void test08() {
        String codigo = "123";
        Assertions.assertNull(dao.buscar(codigo));
    }
    @Test
    public void test09() {
        String codigo = "123";
        RegistroImpl reg = new RegistroImpl(codigo, "EDT");
        cadastro.incluir(reg, codigo);
        RegistroImpl reg2 = dao.buscar(codigo);
        Assertions.assertNotNull(reg2);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(reg2, reg));
    }
    @Test
    public void test10() {
        RegistroImpl reg1 = new RegistroImpl("123", "EDT1");
        RegistroImpl reg2 = new RegistroImpl("456", "EDT2");
        cadastro.incluir(reg1, "123");
        cadastro.incluir(reg2, "465");
        Registro[] regs = dao.buscarTodos();
        Assertions.assertNotNull(regs);
        Assertions.assertEquals(2, regs.length);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(regs[0], reg1));
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(regs[1], reg2));
    }
    @Test
    public void test11() {
        Registro[] regs = dao.buscarTodos();
        Assertions.assertNotNull(regs);
        Assertions.assertEquals(0, regs.length);
    }
    @Test
    public void test12() {
        Assertions.assertTrue(Modifier.isAbstract(DAOGenerico.class.getModifiers()));
        Assertions.assertTrue(Modifier.isAbstract(SeguradoDAO.class.getModifiers()));
        try {
            Method method = DAOGenerico.class.getDeclaredMethod("getClasseEntidade");
            Assertions.assertTrue(Modifier.isAbstract(method.getModifiers()));
        } catch (Exception e) {
            Assertions.fail();
        }
    }
    static class RegistroImpl implements Registro {
        String codigo;
        String nome;
        public RegistroImpl(String codigo, String nome) {
            this.codigo = codigo;
            this.nome = nome;
        }
        public String getIdUnico() {
            return codigo;
        }
    }
    static class DAORegistroImpl extends DAOGenerico<RegistroImpl> {
        public Class getClasseEntidade() {
            return RegistroImpl.class;
        }
    }
    @Override
    protected Class getClasse() {
        return RegistroImpl.class;
    }
}