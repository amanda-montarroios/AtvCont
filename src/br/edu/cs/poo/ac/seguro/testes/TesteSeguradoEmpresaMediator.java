//package br.edu.cs.poo.ac.seguro.testes;
//
//
//import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import br.edu.cs.poo.ac.seguro.entidades.Endereco;
//import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
//
//public class TesteSeguradoEmpresaMediator extends TesteMediator {
//    private SeguradoEmpresaMediator med = SeguradoEmpresaMediator.getInstancia();
//    protected Class getClasse() {
//        return SeguradoEmpresa.class;
//    }
//
//    @Test
//    public void test01() {
//        String msg = "CNPJ deve ser informado";
//        String ret = med.validarCnpj(null);
//        assertEquals(msg, ret);
//        ret = med.validarCnpj(" ");
//        assertEquals(msg, ret);
//    }
//    @Test
//    public void test02() {
//        String ret = med.validarCnpj("123456789012");
//        assertEquals("CNPJ deve ter 14 caracteres", ret);
//    }
//    @Test
//    public void test03() {
//        String ret = med.validarCnpj("11851715000171");
//        assertEquals("CNPJ com dígito inválido", ret);
//    }
//    @Test
//    public void test04() {
//        String ret = med.validarCnpj("11851715000174");
//        assertEquals(null, ret);
//    }
//    @Test
//    public void test05() {
//        String msg = "Faturamento deve ser maior que zero";
//        String ret = med.validarFaturamento(-10.0);
//        assertEquals(msg, ret);
//        ret = med.validarFaturamento(0.0);
//        assertEquals(msg, ret);
//    }
//    @Test
//    public void test06() {
//        String ret = med.validarFaturamento(10.0);
//        assertEquals(null, ret);
//    }
//
//    @Test
//    public void test07() {
//        String cnpj = "11851715000174";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpj, 1000.0, false);
//        cadastro.incluir(seg, cnpj);
//        SeguradoEmpresa segBuscado = med.buscarSeguradoEmpresa(cnpj);
//        assertNotNull(segBuscado);
//    }
//    @Test
//    public void test08() {
//        String cnpj = "11851715000274";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpj, 1000.0, false);
//        cadastro.incluir(seg, cnpj);
//        SeguradoEmpresa segBuscado = med.buscarSeguradoEmpresa("11851715000174");
//        assertNull(segBuscado);
//    }
//
//    @Test
//    public void test09() {
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa(null, end, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000174", 1000.0, false);
//        String ret = med.incluirSeguradoEmpresa(seg);
//        assertEquals("Nome deve ser informado", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", null, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000174", 1000.0, false);
//        ret = med.incluirSeguradoEmpresa(seg);
//        assertEquals("Endereço deve ser informado", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", end, null,
//                BigDecimal.ZERO, "11851715000174", 1000.0, false);
//        ret = med.incluirSeguradoEmpresa(seg);
//        assertEquals("Data da abertura deve ser informada", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000171", 1000.0, false);
//        ret = med.incluirSeguradoEmpresa(seg);
//        assertEquals("CNPJ com dígito inválido", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000174", 0.0, false);
//        ret = med.incluirSeguradoEmpresa(seg);
//        assertEquals("Faturamento deve ser maior que zero", ret);
//    }
//    @Test
//    public void test10() {
//        String cnpj = "11851715000174";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpj, 1000.0, false);
//        String ret = med.incluirSeguradoEmpresa(seg);
//        assertEquals(null, ret);
//        SeguradoEmpresa segBuscado = med.buscarSeguradoEmpresa(cnpj);
//        assertNotNull(segBuscado);
//    }
//    @Test
//    public void test11() {
//        String cnpj = "11851715000174";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpj, 1000.0, false);
//        cadastro.incluir(seg, cnpj);
//        String ret = med.incluirSeguradoEmpresa(seg);
//        assertEquals("CNPJ do segurado empresa já existente", ret);
//        SeguradoEmpresa segBuscado = med.buscarSeguradoEmpresa(cnpj);
//        assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(seg, segBuscado));
//        assertNotNull(segBuscado);
//    }
//
//    @Test
//    public void test12() {
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA.", end, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000174", 1000.0, false);
//        cadastro.incluir(seg, seg.getCnpj());
//        seg = new SeguradoEmpresa("    ", end, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000174", 1000.0, false);
//        String ret = med.alterarSeguradoEmpresa(seg);
//        assertEquals("Nome deve ser informado", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", null, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000174", 1000.0, false);
//        ret = med.alterarSeguradoEmpresa(seg);
//        assertEquals("Endereço deve ser informado", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", end, null,
//                BigDecimal.ZERO, "11851715000174", 1000.0, false);
//        ret = med.alterarSeguradoEmpresa(seg);
//        assertEquals("Data da abertura deve ser informada", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000171", 1000.0, false);
//        ret = med.alterarSeguradoEmpresa(seg);
//        assertEquals("CNPJ com dígito inválido", ret);
//        seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, "11851715000174", 0.0, false);
//        ret = med.alterarSeguradoEmpresa(seg);
//        assertEquals("Faturamento deve ser maior que zero", ret);
//    }
//    @Test
//    public void test13() {
//        String cnpj = "11851715000174";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpj, 1000.0, false);
//        cadastro.incluir(seg, cnpj);
//        end = new Endereco("B Street", "51000000", "44", "ap 302", "USA", "FL",
//                "Miami");
//        seg = new SeguradoEmpresa("ACME LTDA 1", end, LocalDate.now(),
//                BigDecimal.ONE, cnpj, 2000.0, true);
//        String ret = med.alterarSeguradoEmpresa(seg);
//        assertEquals(null, ret);
//        SeguradoEmpresa segBuscado = med.buscarSeguradoEmpresa(cnpj);
//        assertNotNull(segBuscado);
//        assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(seg, segBuscado));
//    }
//    @Test
//    public void test14() {
//        String cnpjOri = "11851715000274";
//        String cnpj = "11851715000174";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpjOri, 1000.0, false);
//        cadastro.incluir(seg, cnpjOri);
//        end = new Endereco("B Street", "51000000", "44", "ap 302", "USA", "FL",
//                "Miami");
//        seg = new SeguradoEmpresa("ACME LTDA 1", end, LocalDate.now(),
//                BigDecimal.ONE, cnpj, 2000.0, true);
//        String ret = med.alterarSeguradoEmpresa(seg);
//        assertEquals("CNPJ do segurado empresa não existente", ret);
//    }
//    @Test
//    public void test15() {
//        String cnpj = "11851715000174";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpj, 1000.0, false);
//        cadastro.incluir(seg, cnpj);
//        String ret = med.excluirSeguradoEmpresa(cnpj);
//        assertEquals(null, ret);
//        SeguradoEmpresa segBuscado = med.buscarSeguradoEmpresa(cnpj);
//        assertNull(segBuscado);
//    }
//    @Test
//    public void test16() {
//        String cnpjOri = "11851715000274";
//        String cnpj = "11851715000174";
//        Endereco end = new Endereco("Rua A", "51020002", "22", "ap 201", "Brasil", "PE",
//                "Recife");
//        SeguradoEmpresa seg = new SeguradoEmpresa("ACME LTDA", end, LocalDate.now(),
//                BigDecimal.ZERO, cnpjOri, 1000.0, false);
//        cadastro.incluir(seg, cnpjOri);
//        String ret = med.excluirSeguradoEmpresa(cnpj);
//        assertEquals("CNPJ do segurado empresa não existente", ret);
//        SeguradoEmpresa segBuscado = med.buscarSeguradoEmpresa(cnpjOri);
//        assertNotNull(segBuscado);
//    }
//}