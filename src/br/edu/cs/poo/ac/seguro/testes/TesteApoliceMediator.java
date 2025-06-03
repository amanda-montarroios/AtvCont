//package br.edu.cs.poo.ac.seguro.testes;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
//import br.edu.cs.poo.ac.seguro.entidades.Apolice;
//import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
//import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
//import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
//import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
//import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
//import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
//import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
//import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
//import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;
//
//public class TesteApoliceMediator extends TesteMediator {
//
//    private static final String NUM_SINISTRO = "1234567";
//    private static final String PLACA_KKK6666 = "KKK6666";
//    private static final String NUM_AP = "123456";
//    private ApoliceMediator mediator = ApoliceMediator.getInstancia();
//    private CadastroObjetos cadPessoa = new CadastroObjetos(SeguradoPessoa.class);
//    private CadastroObjetos cadEmpresa = new CadastroObjetos(SeguradoEmpresa.class);
//    private CadastroObjetos cadVeiculo = new CadastroObjetos(Veiculo.class);
//    private CadastroObjetos cadSinistro = new CadastroObjetos(Sinistro.class);
//
//    @BeforeEach
//    public void setUp() {
//        super.setUp();
//        String sep = File.separator;
//        FileUtils.limparDiretorio("." + sep + Veiculo.class.getSimpleName());
//        FileUtils.limparDiretorio("." + sep + SeguradoEmpresa.class.getSimpleName());
//        FileUtils.limparDiretorio("." + sep + SeguradoPessoa.class.getSimpleName());
//        FileUtils.limparDiretorio("." + sep + Sinistro.class.getSimpleName());
//    }
//
//    /*
//     * Para uso interno! Este teste sempre vai passar, pois testa o construtor da
//     * classe RetornoInclusaoApolice.
//     */
//    @Test
//    public void test000() {
//        try {
//            new RetornoInclusaoApolice(null, null);
//        } catch (RuntimeException e) {
//            Assertions.assertEquals(
//                    "Número da apólice e mensagem de erro não podem ser ambas nulas",
//                    e.getMessage());
//        }
//        try {
//            new RetornoInclusaoApolice("AAA", "VVV");
//        } catch (RuntimeException e) {
//            Assertions.assertEquals(
//                    "Número da apólice e mensagem de erro não podem ser ambas preenchidas",
//                    e.getMessage());
//        }
//        try {
//            new RetornoInclusaoApolice(null, "DDD");
//        } catch (RuntimeException e) {
//            Assertions.fail();
//        }
//        try {
//            new RetornoInclusaoApolice("ZZZ", null);
//        } catch (RuntimeException e) {
//            Assertions.fail();
//        }
//    }
//
//    @Test
//    public void test001() {
//        Assertions.assertEquals("Dados do veículo devem ser informados",
//                mediator.incluirApolice(null).getMensagemErro());
//    }
//    @Test
//    public void test002() {
//        DadosVeiculo dr = new DadosVeiculo(null, "KKK0019", 2020, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("CPF ou CNPJ deve ser informado",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("  ", "KKK0019", 2020, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("CPF ou CNPJ deve ser informado",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("07255431088", "KKK0019", 2020, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("CPF inválido",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("11851715000171", "KKK0019", 2020, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("CNPJ inválido",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    @Test
//    public void test003() {
//        DadosVeiculo dr = new DadosVeiculo("07255431089", null, 2020, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("Placa do veículo deve ser informada",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("07255431089", " ", 2020, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("Placa do veículo deve ser informada",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    @Test
//    public void test004() {
//        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2019, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("Ano tem que estar entre 2020 e 2025, incluindo estes",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("07255431089", "KKK0019", 2026, new BigDecimal("60000.0"), 2);
//        Assertions.assertEquals("Ano tem que estar entre 2020 e 2025, incluindo estes",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    @Test
//    public void test006() {
//        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2020, null, 2);
//        Assertions.assertEquals("Valor máximo segurado deve ser informado",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("07255431089", "KKK0019", 2025, new BigDecimal("60002.0"), 2);
//        Assertions.assertEquals("Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("07255431089", "KKK0019", 2025, new BigDecimal("43000.0"), 2);
//        Assertions.assertEquals("Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    @Test
//    public void test007() {
//        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2024, new BigDecimal("57000.0"), 10);
//        Assertions.assertEquals("Categoria inválida",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    @Test
//    public void test008() {
//        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2020, new BigDecimal("57000.0"), 2);
//        Assertions.assertEquals("CPF inexistente no cadastro de pessoas",
//                mediator.incluirApolice(dr).getMensagemErro());
//        dr = new DadosVeiculo("11851715000174", "KKK0019", 2020, new BigDecimal("57000.0"), 2);
//        Assertions.assertEquals("CNPJ inexistente no cadastro de empresas",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    @Test
//    public void test009() {
//        String cpf = "07255431089";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(),
//                new BigDecimal("0.0"), cpf, 20000.0);
//        cadPessoa.incluir(sp, cpf);
//        Veiculo vel = new Veiculo(placa, ano, null, sp, CategoriaVeiculo.INTERMEDIARIO);
//        cadVeiculo.incluir(vel, placa);
//        String numero = LocalDate.now().getYear() + "000" + cpf + placa;
//        Apolice ap = new Apolice(numero, vel, new BigDecimal("1800.0"),
//                new BigDecimal("2340.0"), new BigDecimal("60000.0"), LocalDate.now());
//        cadastro.incluir(ap, numero);
//        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.0"), 2);
//        Assertions.assertEquals("Apólice já existente para ano atual e veículo",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    public void test010() {
//        String cnpj = "11851715000174";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoEmpresa se = new SeguradoEmpresa("ACME", null, LocalDate.now(),
//                new BigDecimal("0.0"), cnpj, 20000.0, false);
//        cadEmpresa.incluir(se, cnpj);
//        Veiculo vel = new Veiculo(placa, ano, se, null, CategoriaVeiculo.INTERMEDIARIO);
//        cadVeiculo.incluir(vel, placa);
//        String numero = LocalDate.now().getYear() + cnpj + placa;
//        Apolice ap = new Apolice(numero, vel, new BigDecimal("1800.00"),
//                new BigDecimal("2340.00"), new BigDecimal("60000.00"), LocalDate.now());
//        cadastro.incluir(ap, numero);
//        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.00"), 2);
//        Assertions.assertEquals("Apólice já existente para ano atual e veículo",
//                mediator.incluirApolice(dr).getMensagemErro());
//    }
//    @Test
//    public void test011() {
//        String cpf = "07255431089";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(),
//                new BigDecimal("0.00"), cpf, 20000.0);
//        cadPessoa.incluir(sp, cpf);
//        String numero = LocalDate.now().getYear() + "000" + cpf + placa;
//        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo velEsp = new Veiculo(placa, ano, null, sp, CategoriaVeiculo.INTERMEDIARIO);
//        Veiculo vel = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(vel);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, vel));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2223.00"), new BigDecimal("1710.00"),
//                new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//
//    }
//    @Test
//    public void test012() {
//        String cnpj = "11851715000174";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoEmpresa se = new SeguradoEmpresa("ACME", null, LocalDate.now(),
//                new BigDecimal("0.00"), cnpj, 20000.0, false);
//        cadEmpresa.incluir(se, cnpj);
//        String numero = LocalDate.now().getYear() + cnpj + placa;
//        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo velEsp = new Veiculo(placa, ano, se, null, CategoriaVeiculo.INTERMEDIARIO);
//        Veiculo vel = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(vel);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, vel));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2223.00"), new BigDecimal("1710.00"),
//                new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//    }
//    @Test
//    public void test013() {
//        String cpf = "00000000000";
//        String cpfNew = "07255431089";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(),
//                new BigDecimal("0.00"), cpf, 20000.0);
//        SeguradoPessoa spNew = new SeguradoPessoa("Maria", null, LocalDate.now(),
//                new BigDecimal("0.00"), cpfNew, 30000.0);
//        cadPessoa.incluir(sp, cpf);
//        cadPessoa.incluir(spNew, cpfNew);
//        Veiculo vel = new Veiculo(placa, ano, null, sp, CategoriaVeiculo.INTERMEDIARIO);
//        cadVeiculo.incluir(vel, placa);
//        String numero = LocalDate.now().getYear() + "000" + cpfNew + placa;
//        DadosVeiculo dr = new DadosVeiculo(cpfNew, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo velEsp = new Veiculo(placa, ano, null, spNew, CategoriaVeiculo.INTERMEDIARIO);
//        Veiculo velAlt = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(velAlt);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, velAlt));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2223.00"),
//                new BigDecimal("1710.00"), new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//    }
//    @Test
//    public void test014() {
//        String cnpj = "00000000000000";
//        String cnpjNew = "11851715000174";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoEmpresa se = new SeguradoEmpresa("ACME", null, LocalDate.now(),
//                new BigDecimal("0.00"), cnpj, 200000.0, false);
//        SeguradoEmpresa seNew = new SeguradoEmpresa("ORG TABAJARA", null, LocalDate.now(),
//                new BigDecimal("0.00"), cnpjNew, 3006700.0, false);
//        cadEmpresa.incluir(se, cnpj);
//        cadEmpresa.incluir(seNew, cnpjNew);
//        Veiculo vel = new Veiculo(placa, ano, se, null, CategoriaVeiculo.INTERMEDIARIO);
//        cadVeiculo.incluir(vel, placa);
//        String numero = LocalDate.now().getYear() + cnpjNew + placa;
//        DadosVeiculo dr = new DadosVeiculo(cnpjNew, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo velEsp = new Veiculo(placa, ano, seNew, null, CategoriaVeiculo.INTERMEDIARIO);
//        Veiculo velAlt = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(velAlt);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, velAlt));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2223.00"),
//                new BigDecimal("1710.00"), new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//    }
//    @Test
//    public void test015() {
//        String cpf = "07255431089";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(),
//                new BigDecimal("1000.00"), cpf, 20000.0);
//        cadPessoa.incluir(sp, cpf);
//        Veiculo velEsp = new Veiculo(placa, ano, null, sp, CategoriaVeiculo.INTERMEDIARIO);
//        LocalDateTime dhSin = LocalDateTime.now();
//        Sinistro s1 = new Sinistro(NUM_AP, velEsp, dhSin, dhSin,"ego", new BigDecimal("30000.00"), TipoSinistro.COLISAO);
//        cadSinistro.incluir(s1, NUM_AP);
//        String numero = LocalDate.now().getYear() + "000" + cpf + placa;
//        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo vel = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(vel);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, vel));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2223.00"),
//                new BigDecimal("1610.00"), new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//        SeguradoPessoa spBusc = (SeguradoPessoa)cadPessoa.buscar(cpf);
//        Assertions.assertNotNull(spBusc);
//        Assertions.assertEquals(new BigDecimal("1483.00"), spBusc.getBonus());
//    }
//    @Test
//    public void test016() {
//        String cnpj = "11851715000174";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoEmpresa se = new SeguradoEmpresa("ACME", null, LocalDate.now(),
//                new BigDecimal("1000.00"), cnpj, 20000.0, true);
//        cadEmpresa.incluir(se, cnpj);
//        Veiculo velEsp = new Veiculo(placa, ano, se, null, CategoriaVeiculo.INTERMEDIARIO);
//        LocalDateTime dhSin = LocalDateTime.now();
//        Sinistro s1 = new Sinistro(NUM_AP, velEsp, dhSin, dhSin,"ego", new BigDecimal("30000.00"), TipoSinistro.COLISAO);
//        cadSinistro.incluir(s1, NUM_AP);
//        String numero = LocalDate.now().getYear() + cnpj + placa;
//        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo vel = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(vel);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, vel));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2667.60"),
//                new BigDecimal("1952.00"), new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//        SeguradoEmpresa seBusc = (SeguradoEmpresa)cadEmpresa.buscar(cnpj);
//        Assertions.assertNotNull(seBusc);
//        Assertions.assertEquals(new BigDecimal("1585.60"), seBusc.getBonus());
//    }
//    @Test
//    public void test017() {
//        String cpf = "07255431089";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(),
//                new BigDecimal("1000.00"), cpf, 20000.0);
//        cadPessoa.incluir(sp, cpf);
//        Veiculo velEsp = new Veiculo(placa, ano, null, sp, CategoriaVeiculo.INTERMEDIARIO);
//        LocalDateTime dhSin = LocalDateTime.now().minusMonths(12);
//        Sinistro s1 = new Sinistro(NUM_AP, velEsp, dhSin, dhSin,"ego", new BigDecimal("30000.00"), TipoSinistro.COLISAO);
//        cadSinistro.incluir(s1, NUM_AP);
//        String numero = LocalDate.now().getYear() + "000" + cpf + placa;
//        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo vel = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(vel);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, vel));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2223.00"),
//                new BigDecimal("1610.00"), new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//        SeguradoPessoa spBusc = (SeguradoPessoa)cadPessoa.buscar(cpf);
//        Assertions.assertNotNull(spBusc);
//        Assertions.assertEquals(new BigDecimal("1000.00"), spBusc.getBonus());
//    }
//    @Test
//    public void test018() {
//        String cnpj = "11851715000174";
//        String placa = "KKK0019";
//        int ano = 2020;
//        SeguradoEmpresa se = new SeguradoEmpresa("ACME", null, LocalDate.now(),
//                new BigDecimal("1000.00"), cnpj, 20000.0, true);
//        cadEmpresa.incluir(se, cnpj);
//        LocalDateTime dhSin = LocalDateTime.now().minusMonths(12);
//        Veiculo velEsp = new Veiculo(placa, ano, se, null, CategoriaVeiculo.INTERMEDIARIO);
//        Sinistro s1 = new Sinistro(NUM_AP, velEsp, dhSin, dhSin,"ego", new BigDecimal("30000.00"), TipoSinistro.COLISAO);
//        cadSinistro.incluir(s1, NUM_AP);
//        String numero = LocalDate.now().getYear() + cnpj + placa;
//        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.00"), 2);
//        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
//        Assertions.assertEquals(null, ret.getMensagemErro());
//        Assertions.assertEquals(numero, ret.getNumeroApolice());
//        Veiculo vel = (Veiculo)cadVeiculo.buscar(placa);
//        Assertions.assertNotNull(vel);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velEsp, vel));
//        Apolice apEsp = new Apolice(numero, velEsp, new BigDecimal("2667.60"),
//                new BigDecimal("1952.00"), new BigDecimal("57000.00"), LocalDate.now());
//        Apolice ap = (Apolice)cadastro.buscar(numero);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//        SeguradoEmpresa seBusc = (SeguradoEmpresa)cadEmpresa.buscar(cnpj);
//        Assertions.assertNotNull(seBusc);
//        Assertions.assertEquals(new BigDecimal("1000.00"), seBusc.getBonus());
//    }
//    @Test
//    public void test019() {
//        Apolice ap = mediator.buscarApolice(NUM_AP);
//        Assertions.assertNull(ap);
//    }
//    @Test
//    public void test020() {
//        Apolice apEsp = new Apolice(NUM_AP, null, new BigDecimal("2667.60"),
//                new BigDecimal("1952.00"), new BigDecimal("57000.00"), LocalDate.now());
//        cadastro.incluir(apEsp, NUM_AP);
//        Apolice ap = mediator.buscarApolice(NUM_AP);
//        Assertions.assertNotNull(ap);
//        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
//    }
//    @Test
//    public void test021() {
//        String msg = mediator.excluirApolice(" ");
//        Assertions.assertEquals("Número deve ser informado", msg);
//        msg = mediator.excluirApolice(null);
//        Assertions.assertEquals("Número deve ser informado", msg);
//        msg = mediator.excluirApolice(NUM_AP);
//        Assertions.assertEquals("Apólice inexistente", msg);
//    }
//    @Test
//    public void test022() {
//        LocalDate now = LocalDate.now();
//        Veiculo velEsp = new Veiculo(PLACA_KKK6666, 2020, null, null, CategoriaVeiculo.INTERMEDIARIO);
//        Sinistro sin = new Sinistro(NUM_SINISTRO, velEsp, LocalDateTime.now(),
//                LocalDateTime.now(), "ego", new BigDecimal("50000.00"),TipoSinistro.COLISAO);
//        cadVeiculo.incluir(velEsp, PLACA_KKK6666);
//        cadSinistro.incluir(sin, NUM_SINISTRO);
//        Apolice apEsp = new Apolice(NUM_AP, velEsp, new BigDecimal("2667.60"),
//                new BigDecimal("1952.00"), new BigDecimal("57000.00"), now);
//        cadastro.incluir(apEsp, NUM_AP);
//        String msg = mediator.excluirApolice(NUM_AP);
//        Assertions.assertEquals("Existe sinistro cadastrado para o veículo em questão " +
//                "e no mesmo ano da apólice", msg);
//    }
//    @Test
//    public void test023() {
//        LocalDate now = LocalDate.now();
//        Veiculo velEsp = new Veiculo(PLACA_KKK6666, 2020, null, null, CategoriaVeiculo.INTERMEDIARIO);
//        Sinistro sin = new Sinistro(NUM_SINISTRO, velEsp, LocalDateTime.now().minusMonths(13),
//                LocalDateTime.now(), "ego", new BigDecimal("50000.00"),TipoSinistro.COLISAO);
//        cadVeiculo.incluir(velEsp, PLACA_KKK6666);
//        cadSinistro.incluir(sin, NUM_SINISTRO);
//        Apolice apEsp = new Apolice(NUM_AP, velEsp, new BigDecimal("2667.60"),
//                new BigDecimal("1952.00"), new BigDecimal("57000.00"), now);
//        cadastro.incluir(apEsp, NUM_AP);
//        String msg = mediator.excluirApolice(NUM_AP);
//        Assertions.assertEquals(null, msg);
//    }
//    @Override
//    protected Class getClasse() {
//        return Apolice.class;
//    }
//}