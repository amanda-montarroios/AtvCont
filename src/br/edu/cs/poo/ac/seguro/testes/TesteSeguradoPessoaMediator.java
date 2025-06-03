//package br.edu.cs.poo.ac.seguro.testes;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;
//import org.junit.jupiter.api.Test;
//
//import br.edu.cs.poo.ac.seguro.entidades.Endereco;
//import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
//
//public class TesteSeguradoPessoaMediator extends TesteMediator {
//
//    private SeguradoPessoaMediator med = SeguradoPessoaMediator.getInstancia();
//
//    @Override
//    protected Class getClasse() {
//        return SeguradoPessoa.class;
//    }
//
//    @Test
//    public void test01() {
//        String msg = "CPF deve ser informado";
//        String ret = med.validarCpf(null);
//        assertEquals(msg, ret);
//        ret = med.validarCpf(" ");
//        assertEquals(msg, ret);
//    }
//    @Test
//    public void test02() {
//        String ret = med.validarCpf("123456789012");
//        assertEquals("CPF deve ter 11 caracteres", ret);
//    }
//    @Test
//    public void test03() {
//        String ret = med.validarCpf("07255431081");
//        assertEquals("CPF com dígito inválido", ret);
//    }
//    @Test
//    public void test04() {
//        String ret = med.validarCpf("07255431089");
//        assertEquals(null, ret);
//    }
//    @Test
//    public void test05() {
//        String msg = "Renda deve ser maior ou igual à zero";
//        String ret = med.validarRenda(-10.0);
//        assertEquals(msg, ret);
//    }
//    @Test
//    public void test06() {
//        String ret = med.validarRenda(10.0);
//        assertEquals(null, ret);
//        ret = med.validarRenda(0.0);
//        assertEquals(null, ret);
//    }
//
//    @Test
//    public void test07() {
//        String cpf = "07255431089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpf, 1000.0);
//        cadastro.incluir(seg, cpf);
//        SeguradoPessoa segBuscado = med.buscarSeguradoPessoa(cpf);
//        assertNotNull(segBuscado);
//    }
//    @Test
//    public void test08() {
//        String cpf = "07255431089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpf, 1000.0);
//        cadastro.incluir(seg, cpf);
//        SeguradoPessoa segBuscado = med.buscarSeguradoPessoa("17255431089");
//        assertNull(segBuscado);
//    }
//
//    @Test
//    public void test09() {
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa(" ", end, LocalDate.now(),
//                BigDecimal.ZERO, "07255431089", 1000.0);
//        String ret = med.incluirSeguradoPessoa(seg);
//        assertEquals("Nome deve ser informado", ret);
//        seg = new SeguradoPessoa("PAULA", null, LocalDate.now(),
//                BigDecimal.ZERO, "07255431089", 1000.0);
//        ret = med.incluirSeguradoPessoa(seg);
//        assertEquals("Endereço deve ser informado", ret);
//        seg = new SeguradoPessoa("PAULA", end, null,
//                BigDecimal.ZERO, "07255431089", 1000.0);
//        ret = med.incluirSeguradoPessoa(seg);
//        assertEquals("Data do nascimento deve ser informada", ret);
//        seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, "07255431081", 1000.0);
//        ret = med.incluirSeguradoPessoa(seg);
//        assertEquals("CPF com dígito inválido", ret);
//        seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, "07255431089", -12.0);
//        ret = med.incluirSeguradoPessoa(seg);
//        assertEquals("Renda deve ser maior ou igual à zero", ret);
//    }
//    @Test
//    public void test10() {
//        String cpf = "07255431089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpf, 1000.0);
//        String ret = med.incluirSeguradoPessoa(seg);
//        assertEquals(null, ret);
//        SeguradoPessoa segBuscado = med.buscarSeguradoPessoa(cpf);
//        assertNotNull(segBuscado);
//    }
//    @Test
//    public void test11() {
//        String cpf = "07255431089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpf, 1000.0);
//        cadastro.incluir(seg, cpf);
//        String ret = med.incluirSeguradoPessoa(seg);
//        assertEquals("CPF do segurado pessoa já existente", ret);
//        SeguradoPessoa segBuscado = med.buscarSeguradoPessoa(cpf);
//        assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(seg, segBuscado));
//        assertNotNull(segBuscado);
//    }
//
//    @Test
//    public void test12() {
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa(" ", end, LocalDate.now(),
//                BigDecimal.ZERO, "07255431089", 1000.0);
//        cadastro.incluir(seg, seg.getCpf());
//        String ret = med.alterarSeguradoPessoa(seg);
//        assertEquals("Nome deve ser informado", ret);
//        seg = new SeguradoPessoa("PAULA", null, LocalDate.now(),
//                BigDecimal.ZERO, "07255431089", 1000.0);
//        ret = med.alterarSeguradoPessoa(seg);
//        assertEquals("Endereço deve ser informado", ret);
//        seg = new SeguradoPessoa("PAULA", end, null,
//                BigDecimal.ZERO, "07255431089", 1000.0);
//        ret = med.alterarSeguradoPessoa(seg);
//        assertEquals("Data do nascimento deve ser informada", ret);
//        seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, "07255431081", 1000.0);
//        ret = med.alterarSeguradoPessoa(seg);
//        assertEquals("CPF com dígito inválido", ret);
//        seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, "07255431089", -12.0);
//        ret = med.alterarSeguradoPessoa(seg);
//        assertEquals("Renda deve ser maior ou igual à zero", ret);
//    }
//    @Test
//    public void test13() {
//        String cpf = "07255431089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpf, 1000.0);
//        cadastro.incluir(seg, cpf);
//        end = new Endereco("B Street", "51000000", "44", "ap 302", "USA", "FL",
//                "Miami");
//        seg = new SeguradoPessoa("PAULA 1", end, LocalDate.now(),
//                BigDecimal.ONE, cpf, 2000.0);
//        String ret = med.alterarSeguradoPessoa(seg);
//        assertEquals(null, ret);
//        SeguradoPessoa segBuscado = med.buscarSeguradoPessoa(cpf);
//        assertNotNull(segBuscado);
//        assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(seg, segBuscado));
//    }
//    @Test
//    public void test14() {
//        String cpfOri = "07255432089";
//        String cpf = "07255431089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpfOri, 1000.0);
//        cadastro.incluir(seg, cpfOri);
//        end = new Endereco("B Street", "51000000", "44", "ap 302", "USA", "FL",
//                "Miami");
//        seg = new SeguradoPessoa("PAULA 1", end, LocalDate.now(),
//                BigDecimal.ONE, cpf, 2000.0);
//        String ret = med.alterarSeguradoPessoa(seg);
//        assertEquals("CPF do segurado pessoa não existente", ret);
//    }
//    @Test
//    public void test15() {
//        String cpf = "07255431089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpf, 1000.0);
//        cadastro.incluir(seg, cpf);
//        String ret = med.excluirSeguradoPessoa(cpf);
//        assertEquals(null, ret);
//        SeguradoPessoa segBuscado = med.buscarSeguradoPessoa(cpf);
//        assertNull(segBuscado);
//    }
//    @Test
//    public void test16() {
//        String cpfOri = "07255431089";
//        String cpf = "07255432089";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoPessoa seg = new SeguradoPessoa("PAULA", end, LocalDate.now(),
//                BigDecimal.ZERO, cpfOri, 1000.0);
//        cadastro.incluir(seg, cpfOri);
//        String ret = med.excluirSeguradoPessoa(cpf);
//        assertEquals("CPF do segurado pessoa não existente", ret);
//        SeguradoPessoa segBuscado = med.buscarSeguradoPessoa(cpfOri);
//        assertNotNull(segBuscado);
//    }
//}