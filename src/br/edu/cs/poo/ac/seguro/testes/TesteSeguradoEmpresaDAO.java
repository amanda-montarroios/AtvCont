//package br.edu.cs.poo.ac.seguro.testes;
//
//import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
//import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//public class TesteSeguradoEmpresaDAO extends TesteDAO {
//	private SeguradoEmpresaDAO dao = new SeguradoEmpresaDAO();
//	protected Class getClasse() {
//		return SeguradoEmpresa.class;
//	}
//
//	@Test
//	public void teste01() {
//		String cnpj = "00000000";
//		cadastro.incluir(new SeguradoEmpresa("TESTE1", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1000.0, false), cnpj);
//		SeguradoEmpresa seg = dao.buscar(cnpj);
//		Assertions.assertNotNull(seg);
//	}
//	@Test
//	public void teste02() {
//		String cnpj = "10000000";
//		cadastro.incluir(new SeguradoEmpresa("TESTE2", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1001.0, false), cnpj);
//		SeguradoEmpresa seg = dao.buscar("11000000");
//		Assertions.assertNull(seg);
//	}
//	@Test
//	public void teste03() {
//		String cnpj = "22000000";
//		cadastro.incluir(new SeguradoEmpresa("TESTE3", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1002.0, false), cnpj);
//		boolean ret = dao.excluir(cnpj);
//		Assertions.assertTrue(ret);
//	}
//	@Test
//	public void teste04() {
//		String cnpj = "33000000";
//		cadastro.incluir(new SeguradoEmpresa("TESTE4", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1003.0, false), cnpj);
//		boolean ret = dao.excluir("33100000");
//		Assertions.assertFalse(ret);
//	}
//	@Test
//	public void teste05() {
//		String cnpj = "44000000";
//		boolean ret = dao.incluir(new SeguradoEmpresa("TESTE5", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1004.0, false));
//		Assertions.assertTrue(ret);
//		SeguradoEmpresa seg = dao.buscar(cnpj);
//		Assertions.assertNotNull(seg);
//	}
//
//	@Test
//	public void teste06() {
//		String cnpj = "55000000";
//		SeguradoEmpresa seg = new SeguradoEmpresa("TESTE6", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1005.0, false);
//		cadastro.incluir(seg, cnpj);
//		boolean ret = dao.incluir(seg);
//		Assertions.assertFalse(ret);
//	}
//	@Test
//	public void teste07() {
//		String cnpj = "66000000";
//		boolean ret = dao.alterar(new SeguradoEmpresa("TESTE7", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1006.0, false));
//		Assertions.assertFalse(ret);
//		SeguradoEmpresa seg = dao.buscar(cnpj);
//		Assertions.assertNull(seg);
//	}
//
//	@Test
//	public void teste08() {
//		String cnpj = "77000000";
//		SeguradoEmpresa seg = new SeguradoEmpresa("TESTE8", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1007.0, false);
//		cadastro.incluir(seg, cnpj);
//		seg = new SeguradoEmpresa("TESTE9", null, LocalDate.now(), BigDecimal.ZERO,
//				cnpj, 1008.0, false);
//		boolean ret = dao.alterar(seg);
//		Assertions.assertTrue(ret);
//	}
//}