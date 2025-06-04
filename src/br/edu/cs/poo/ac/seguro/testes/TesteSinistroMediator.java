package br.edu.cs.poo.ac.seguro.testes;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;

public class TesteSinistroMediator extends TesteMediator {

    private SinistroMediator sinMed = SinistroMediator.getInstancia();
    private CadastroObjetos cadPessoa = new CadastroObjetos(SeguradoPessoa.class);
    private CadastroObjetos cadVeiculo = new CadastroObjetos(Veiculo.class);
    private CadastroObjetos cadSinistro = new CadastroObjetos(Sinistro.class);
    private CadastroObjetos cadApolice = new CadastroObjetos(Apolice.class);
    @Override
    protected Class getClasse() {
        return Sinistro.class;
    }
    @BeforeEach
    public void setUp() {
        super.setUp();
        String sep = File.separator;
        FileUtils.limparDiretorio("." + sep + Veiculo.class.getSimpleName());
        FileUtils.limparDiretorio("." + sep + SeguradoEmpresa.class.getSimpleName());
        FileUtils.limparDiretorio("." + sep + SeguradoPessoa.class.getSimpleName());
        FileUtils.limparDiretorio("." + sep + Apolice.class.getSimpleName());
    }
    @Test
    public void teste01() {
        try {
            sinMed.incluirSinistro(null, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(1, msgs.size());
            Assertions.assertEquals("Dados do sinistro devem ser informados", msgs.get(0));
        }
    }
    @Test
    public void teste02() {
        try {
            DadosSinistro dados = new DadosSinistro(" ", null, "   ", 0, 6);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(5, msgs.size());
            Assertions.assertEquals("Data/hora do sinistro deve ser informada", msgs.get(0));
            Assertions.assertEquals("Placa do Veículo deve ser informada", msgs.get(1));
            Assertions.assertEquals("Usuário do registro de sinistro deve ser informado", msgs.get(2));
            Assertions.assertEquals("Valor do sinistro deve ser maior que zero", msgs.get(3));
            Assertions.assertEquals("Código do tipo de sinistro inválido", msgs.get(4));
        }
        try {
            DadosSinistro dados = new DadosSinistro(null, null, null, 0, 6);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(5, msgs.size());
            Assertions.assertEquals("Data/hora do sinistro deve ser informada", msgs.get(0));
            Assertions.assertEquals("Placa do Veículo deve ser informada", msgs.get(1));
            Assertions.assertEquals("Usuário do registro de sinistro deve ser informado", msgs.get(2));
            Assertions.assertEquals("Valor do sinistro deve ser maior que zero", msgs.get(3));
            Assertions.assertEquals("Código do tipo de sinistro inválido", msgs.get(4));
        }
    }
    @Test
    public void teste03() {
        try {
            DadosSinistro dados = new DadosSinistro("KKK0000", LocalDateTime.now().plusMinutes(10), "ddd.xxx", 100.0, 1);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(2, msgs.size());
            Assertions.assertEquals("Data/hora do sinistro deve ser menor que a data/hora atual", msgs.get(0));
            Assertions.assertEquals("Veículo não cadastrado", msgs.get(1));
        }
    }

    @Test
    public void teste04() {
        try {
            String cpf = "12345678900";
            String placa = "KKK0001";
            SeguradoPessoa sp = new SeguradoPessoa("ED MOTA", null, null, BigDecimal.ZERO, cpf, 10000.0);
            cadPessoa.incluir(sp, cpf);
            Veiculo vei = new Veiculo(placa, 2022, sp, CategoriaVeiculo.BASICO);
            cadVeiculo.incluir(vei, placa);
            DadosSinistro dados = new DadosSinistro(placa, null, "ddd.xxx", 100.0, 1);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(1, msgs.size());
            Assertions.assertEquals("Data/hora do sinistro deve ser informada", msgs.get(0));
        }
    }
    @Test
    public void teste05() {
        try {
            String cpf = "12345678900";
            String placa = "KKK0001";
            SeguradoPessoa sp = new SeguradoPessoa("ED MOTA", null, null, BigDecimal.ZERO, cpf, 10000.0);
            cadPessoa.incluir(sp, cpf);
            Veiculo vei = new Veiculo(placa, 2022, sp, CategoriaVeiculo.BASICO);
            cadVeiculo.incluir(vei, placa);
            DadosSinistro dados = new DadosSinistro(placa, LocalDateTime.now().minusMinutes(10), "ddd.xxx", 100.0, 1);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(1, msgs.size());
            Assertions.assertEquals("Não existe apólice vigente para o veículo", msgs.get(0));
        }
    }
    @Test
    public void teste06() {
        try {
            String cpf = "12345678900";
            String placa = "KKK0001";
            String numero = "123456";
            SeguradoPessoa sp = new SeguradoPessoa("ED MOTA", null, null, BigDecimal.ZERO, cpf, 10000.0);
            cadPessoa.incluir(sp, cpf);
            Veiculo vei = new Veiculo(placa, 2022, sp, CategoriaVeiculo.BASICO);
            cadVeiculo.incluir(vei, placa);
            Apolice ap = new Apolice(numero, vei, null, null, null, LocalDate.now().minusMonths(13));
            cadApolice.incluir(ap, numero);
            DadosSinistro dados = new DadosSinistro(placa, LocalDateTime.now().minusMinutes(10), "ddd.xxx", 100.0, 1);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(1, msgs.size());
            Assertions.assertEquals("Não existe apólice vigente para o veículo", msgs.get(0));
        }
    }
    @Test
    public void teste07() {
        try {
            String cpf = "12345678900";
            String placa = "KKK0001";
            String placa1 = "KKK0000";
            String numero = "123456";
            SeguradoPessoa sp = new SeguradoPessoa("ED MOTA", null, null, BigDecimal.ZERO, cpf, 10000.0);
            cadPessoa.incluir(sp, cpf);
            Veiculo vei = new Veiculo(placa, 2022, sp, CategoriaVeiculo.BASICO);
            Veiculo vei1 = new Veiculo(placa1, 2022, sp, CategoriaVeiculo.BASICO);
            cadVeiculo.incluir(vei, placa);
            cadVeiculo.incluir(vei1, placa1);
            Apolice ap = new Apolice(numero, vei, null, null, null, LocalDate.now().minusMonths(2));
            cadApolice.incluir(ap, numero);
            DadosSinistro dados = new DadosSinistro(placa1, LocalDateTime.now().minusMinutes(10), "ddd.xxx", 100.0, 1);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(1, msgs.size());
            Assertions.assertEquals("Não existe apólice vigente para o veículo", msgs.get(0));
        }
    }
    @Test
    public void teste08() {
        try {
            String cpf = "12345678900";
            String placa = "KKK0001";
            String placa1 = "KKK0000";
            String numero = "123456";
            String numero1 = "123455";
            SeguradoPessoa sp = new SeguradoPessoa("ED MOTA", null, null, BigDecimal.ZERO, cpf, 10000.0);
            cadPessoa.incluir(sp, cpf);
            Veiculo vei = new Veiculo(placa, 2022, sp, CategoriaVeiculo.BASICO);
            Veiculo vei1 = new Veiculo(placa1, 2022, sp, CategoriaVeiculo.BASICO);
            cadVeiculo.incluir(vei, placa);
            cadVeiculo.incluir(vei1, placa1);
            Apolice ap = new Apolice(numero, vei, null, null, new BigDecimal("58000.00"), LocalDate.now().minusMonths(2));
            Apolice ap1 = new Apolice(numero1, vei1, null, null, new BigDecimal("61000.00"), LocalDate.now().minusMonths(3));
            cadApolice.incluir(ap, numero);
            cadApolice.incluir(ap1, numero1);
            DadosSinistro dados = new DadosSinistro(placa, LocalDateTime.now().minusMinutes(10), "ddd.xxx", 60000.00, 1);
            sinMed.incluirSinistro(dados, LocalDateTime.now());
            Assertions.fail();
        } catch (ExcecaoValidacaoDados e) {
            List<String> msgs = e.getMensagens();
            Assertions.assertNotNull(msgs);
            Assertions.assertEquals(1, msgs.size());
            Assertions.assertEquals("Valor do sinistro não pode ultrapassar o valor máximo segurado constante na apólice", msgs.get(0));
        }
    }
    @Test
    public void teste09() {
        try {
            String cpf = "12345678900";
            String placa = "KKK0001";
            String placa1 = "KKK0000";
            String numero = "123456";
            String numero1 = "123455";
            String numeroEsp = "S123456001";
            String usuario = "ddd.xxx";
            double valorSinistro = 58000.00;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dhSinistro = now.minusHours(10);
            SeguradoPessoa sp = new SeguradoPessoa("ED MOTA", null, null, BigDecimal.ZERO, cpf, 10000.0);
            cadPessoa.incluir(sp, cpf);
            Veiculo vei = new Veiculo(placa, 2022, sp, CategoriaVeiculo.BASICO);
            Veiculo vei1 = new Veiculo(placa1, 2022, sp, CategoriaVeiculo.BASICO);
            cadVeiculo.incluir(vei, placa);
            cadVeiculo.incluir(vei1, placa1);
            Apolice ap = new Apolice(numero, vei, null, null, new BigDecimal(valorSinistro), LocalDate.now().minusMonths(2));
            Apolice ap1 = new Apolice(numero1, vei1, null, null, new BigDecimal("61000.00"), LocalDate.now().minusMonths(3));
            cadApolice.incluir(ap, numero);
            cadApolice.incluir(ap1, numero1);
            DadosSinistro dados = new DadosSinistro(placa, dhSinistro, usuario, 58000.00, 1);
            String numeroSin = sinMed.incluirSinistro(dados, now);
            Assertions.assertEquals(numeroEsp, numeroSin);
            Sinistro sinRef = new Sinistro(numeroEsp, vei, dhSinistro, now, usuario, new BigDecimal(valorSinistro), TipoSinistro.COLISAO);
            sinRef.setNumeroApolice(numero);
            sinRef.setSequencial(1);
            Sinistro sinGrav = (Sinistro)cadastro.buscar(numeroSin);
            Assertions.assertNotNull(sinGrav);
            Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(sinRef, sinGrav));
        } catch (ExcecaoValidacaoDados e) {
            Assertions.fail(e.fillInStackTrace());
        }
    }
    @Test
    public void teste10() {
        try {
            String cpf = "12345678900";
            String placa = "KKK0001";
            String placa1 = "KKK0000";
            String numero = "123456";
            String numero1 = "123455";
            String numeroAnt = "S123456001";
            String numeroEsp = "S123456002";
            String usuario = "ddd.xxx";
            double valorSinistro = 58000.00;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dhSinistro = now.minusHours(10);
            SeguradoPessoa sp = new SeguradoPessoa("ED MOTA", null, null, BigDecimal.ZERO, cpf, 10000.0);
            cadPessoa.incluir(sp, cpf);
            Veiculo vei = new Veiculo(placa, 2022, sp, CategoriaVeiculo.BASICO);
            Veiculo vei1 = new Veiculo(placa1, 2022, sp, CategoriaVeiculo.BASICO);
            cadVeiculo.incluir(vei, placa);
            cadVeiculo.incluir(vei1, placa1);
            Apolice ap = new Apolice(numero, vei, null, null, new BigDecimal(valorSinistro), LocalDate.now().minusMonths(2));
            Apolice ap1 = new Apolice(numero1, vei1, null, null, new BigDecimal("61000.00"), LocalDate.now().minusMonths(3));
            cadApolice.incluir(ap, numero);
            cadApolice.incluir(ap1, numero1);
            Sinistro sinAnt = new Sinistro(numeroAnt, vei, dhSinistro.minusMonths(1), now.minusMonths(1), usuario, new BigDecimal("20000.00"), TipoSinistro.DEPREDACAO);
            sinAnt.setNumeroApolice(numero);
            sinAnt.setSequencial(1);
            cadSinistro.incluir(sinAnt, numeroAnt);
            DadosSinistro dados = new DadosSinistro(placa, dhSinistro, usuario, 58000.00, 1);
            String numeroSin = sinMed.incluirSinistro(dados, now);
            Assertions.assertEquals(numeroEsp, numeroSin);
            Sinistro sinRef = new Sinistro(numeroEsp, vei, dhSinistro, now, usuario, new BigDecimal(valorSinistro), TipoSinistro.COLISAO);
            sinRef.setNumeroApolice(numero);
            sinRef.setSequencial(2);
            Sinistro sinGrav = (Sinistro)cadastro.buscar(numeroSin);
            Assertions.assertNotNull(sinGrav);
            Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(sinRef, sinGrav));
        } catch (ExcecaoValidacaoDados e) {
            Assertions.fail(e.fillInStackTrace());
        }
    }
}
