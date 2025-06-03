package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class ApoliceMediator {
    private final SeguradoPessoaDAO daoSegPes;
    private final SeguradoEmpresaDAO daoSegEmp;
    private final VeiculoDAO daoVel;
    private final ApoliceDAO daoApo;
    private final SinistroDAO daoSin;

    private static final ApoliceMediator instancia = new ApoliceMediator();

    private ApoliceMediator() {
        this.daoSegPes = new SeguradoPessoaDAO();
        this.daoSegEmp = new SeguradoEmpresaDAO();
        this.daoVel = new VeiculoDAO();
        this.daoApo = new ApoliceDAO();
        this.daoSin = new SinistroDAO();
    }

    public static ApoliceMediator getInstancia() {
        return instancia;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        if (dados == null) return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
        if (StringUtils.ehNuloOuBranco(dados.getPlaca())) return new RetornoInclusaoApolice(null, "Placa do veículo deve ser informada");

        String placa = dados.getPlaca().trim();
        String cpfOuCnpj = dados.getCpfOuCnpj();

        if (StringUtils.ehNuloOuBranco(cpfOuCnpj)) return new RetornoInclusaoApolice(null, "CPF ou CNPJ deve ser informado");

        boolean isCpf = cpfOuCnpj.length() == 11;
        boolean isCnpj = cpfOuCnpj.length() == 14;

        if (isCpf && !ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) return new RetornoInclusaoApolice(null, "CPF inválido");
        if (isCnpj && !ValidadorCpfCnpj.ehCnpjValido(cpfOuCnpj)) return new RetornoInclusaoApolice(null, "CNPJ inválido");

        if (dados.getAno() < 2020 || dados.getAno() > 2025) {
            return new RetornoInclusaoApolice(null, "Ano tem que estar entre 2020 e 2025, incluindo estes");
        }

        CategoriaVeiculo categoria = CategoriaVeiculo.buscarPorCodigo(dados.getCodigoCategoria());
        if (categoria == null) return new RetornoInclusaoApolice(null, "Categoria inválida");

        BigDecimal valorTabela = Arrays.stream(categoria.getPrecosAnos())
                .filter(pa -> pa.getAno() == dados.getAno())
                .map(pa -> BigDecimal.valueOf(pa.getPreco()))
                .findFirst()
                .orElse(null);


        if (valorTabela == null) return new RetornoInclusaoApolice(null, "Ano não disponível para a categoria");

        BigDecimal valorInformado = dados.getValorMaximoSegurado();
        if (valorInformado == null || valorInformado.compareTo(BigDecimal.ZERO) <= 0) {
            return new RetornoInclusaoApolice(null, "Valor máximo segurado deve ser informado");
        }

        valorInformado = valorInformado.setScale(2, RoundingMode.HALF_UP);
        BigDecimal limiteInferior = valorTabela.multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal limiteSuperior = valorTabela.setScale(2, RoundingMode.HALF_UP);

        if (valorInformado.compareTo(limiteInferior) < 0 || valorInformado.compareTo(limiteSuperior) > 0) {
            return new RetornoInclusaoApolice(null, "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria");
        }

        Segurado segurado;
        if (isCpf) {
            segurado = daoSegPes.buscar(cpfOuCnpj);
            if (segurado == null) return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
        } else {
            segurado = daoSegEmp.buscar(cpfOuCnpj);
            if (segurado == null) return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
        }

        Veiculo veiculo = daoVel.buscar(placa);
        LocalDate agora = LocalDate.now();

        String numeroApolice = isCpf
                ? agora.getYear() + "000" + cpfOuCnpj + placa
                : agora.getYear() + cpfOuCnpj + placa;

        if (daoApo.buscar(numeroApolice) != null) {
            return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
        }

        if (veiculo == null) {
            veiculo = new Veiculo(placa, dados.getAno(), segurado, categoria);
            daoVel.incluir(veiculo);
        } else {
            veiculo.setProprietario(segurado);
            daoVel.alterar(veiculo);
        }

        BigDecimal vpa = valorInformado.multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vpb = vpa;

        if (segurado instanceof SeguradoEmpresa && ((SeguradoEmpresa) segurado).getEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(new BigDecimal("1.2")).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal bonus = segurado.getBonus();
        BigDecimal vpc = vpb.subtract(bonus.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP));
        BigDecimal premio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO;
        BigDecimal franquia = vpb.multiply(new BigDecimal("1.3")).setScale(2, RoundingMode.HALF_UP);

        Apolice apolice = new Apolice(numeroApolice, veiculo, franquia, premio, valorInformado, agora);
        daoApo.incluir(apolice);

        int anoAnterior = agora.getYear() - 1;
        boolean teveSinistro = Arrays.stream(daoSin.buscarTodos()).anyMatch(sin -> {
            Veiculo vs = sin.getVeiculo();
            return vs != null &&
                    vs.getPlaca().equalsIgnoreCase(placa) &&
                    sin.getDataHoraSinistro().getYear() == anoAnterior &&
                    ((segurado instanceof SeguradoPessoa && vs.getProprietario() instanceof SeguradoPessoa &&
                            ((SeguradoPessoa) vs.getProprietario()).getCpf().equals(cpfOuCnpj)) ||
                            (segurado instanceof SeguradoEmpresa && vs.getProprietario() instanceof SeguradoEmpresa &&
                                    ((SeguradoEmpresa) vs.getProprietario()).getCnpj().equals(cpfOuCnpj)));
        });

        if (!teveSinistro) {
            BigDecimal acrescimo = premio.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.HALF_UP);
            segurado.creditarBonus(acrescimo);
        }

        if (segurado instanceof SeguradoPessoa) daoSegPes.alterar((SeguradoPessoa) segurado);
        else daoSegEmp.alterar((SeguradoEmpresa) segurado);

        return new RetornoInclusaoApolice(numeroApolice, null);
    }

    public Apolice buscarApolice(String numero) {
        return daoApo.buscar(numero);
    }

    public String excluirApolice(String numero) {
        if (StringUtils.ehNuloOuBranco(numero)) return "Número deve ser informado";

        Apolice apolice = daoApo.buscar(numero);
        if (apolice == null) return "Apólice inexistente";

        int anoApolice = apolice.getDataInicioVigencia().getYear();
        Veiculo veiculoApolice = apolice.getVeiculo();

        boolean temSinistroMesmoAno = Arrays.stream(daoSin.buscarTodos()).anyMatch(sin ->
                sin.getVeiculo() != null &&
                        sin.getVeiculo().getPlaca().equalsIgnoreCase(veiculoApolice.getPlaca()) &&  // compara placa, não objeto
                        sin.getDataHoraSinistro().getYear() == anoApolice
        );


        if (temSinistroMesmoAno) {
            return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
        }

        daoApo.excluir(numero);
        return null;
    }
}