package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import lombok.Getter;

public class ApoliceMediator {
    private SeguradoPessoaDAO daoSegPes;
    private SeguradoEmpresaDAO daoSegEmp;
    private VeiculoDAO daoVel;
    private ApoliceDAO daoApo;
    private SinistroDAO daoSin;

    private ApoliceMediator() {
    }

    @Getter
    public static ApoliceMediator instancia = new ApoliceMediator();

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        // Initial validations
        if (dados == null) return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
        if (dados.getPlaca() == null || dados.getPlaca().isBlank())
            return new RetornoInclusaoApolice(null, "Placa do veículo deve ser informada");

        if (dados.getCpfOuCnpj() == null || dados.getCpfOuCnpj().isBlank())
            return new RetornoInclusaoApolice(null, "CPF ou CNPJ deve ser informado");

        if (dados.getCpfOuCnpj().length() == 14) {
            if (!ValidadorCpfCnpj.ehCnpjValido(dados.getCpfOuCnpj())) {
                return new RetornoInclusaoApolice(null, "CNPJ inválido");
            }
        } else {
            if (!ValidadorCpfCnpj.ehCpfValido(dados.getCpfOuCnpj())) {
                return new RetornoInclusaoApolice(null, "CPF inválido");
            }
        }

        if(dados.getAno()<2020 || dados.getAno()>2025)
            return new RetornoInclusaoApolice(null, "Ano tem que estar entre 2020 e 2025, incluindo estes");

        if(dados.getValorMaximoSegurado() == null)
            return new RetornoInclusaoApolice(null, "Valor máximo segurado deve ser informado");

        CategoriaVeiculo categoria = null;
        for (CategoriaVeiculo cat : CategoriaVeiculo.values()) {
            if (cat.getCodigo() == dados.getCodigoCategoria()) {
                categoria = cat;
                break;
            }
        }
        if (categoria == null) {
            return new RetornoInclusaoApolice(null, "Categoria inválida");
        }

        PrecoAno precoAno = null;
        for (PrecoAno pa : categoria.getPrecosAnos()) {
            if (pa.getAno() == dados.getAno()) {
                precoAno = pa;
                break;
            }
        }

        if (precoAno == null) {
            return new RetornoInclusaoApolice(null, "Preço não encontrado para o ano informado");
        }

        BigDecimal minValor = BigDecimal.valueOf(precoAno.getPreco()).multiply(new BigDecimal("0.75"));
        BigDecimal maxValor = BigDecimal.valueOf(precoAno.getPreco());
        if (dados.getValorMaximoSegurado().compareTo(minValor) < 0 ||
                dados.getValorMaximoSegurado().compareTo(maxValor) > 0) {
            return new RetornoInclusaoApolice(null,
                    "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria");
        }

        boolean isEmpresa = dados.getCpfOuCnpj().length() != 11;
        Segurado seg = isEmpresa
                ? daoSegEmp.buscar(dados.getCpfOuCnpj())
                : daoSegPes.buscar(dados.getCpfOuCnpj());

        if (seg == null) {
            return new RetornoInclusaoApolice(null,
                    isEmpresa ? "CNPJ inexistente" : "CPF inexistente");
        }

        Veiculo veiculo = daoVel.buscar(dados.getPlaca());
        if (veiculo != null) {

            boolean validOwner = isEmpresa
                    ? veiculo.getProprietarioEmpresa() != null && veiculo.getProprietarioEmpresa().getCnpj().equals(dados.getCpfOuCnpj())
                    : veiculo.getProprietarioPessoa() != null && veiculo.getProprietarioPessoa().getCpf().equals(dados.getCpfOuCnpj());

            if (!validOwner) {
                return new RetornoInclusaoApolice(null, "CPF/CNPJ não corresponde ao proprietário do veículo");
            }
        } else {

            veiculo = new Veiculo(
                    dados.getPlaca(),
                    dados.getAno(),
                    isEmpresa ? (SeguradoEmpresa) seg : null,
                    !isEmpresa ? (SeguradoPessoa) seg : null,
                    categoria
            );
            daoVel.incluir(veiculo);
        }


        BigDecimal VPA = dados.getValorMaximoSegurado().multiply(new BigDecimal("0.03"));
        BigDecimal VPB = isEmpresa && ((SeguradoEmpresa) seg).ehLocadoraDeVeiculos()
                ? VPA.multiply(new BigDecimal("1.2"))
                : VPA;

        BigDecimal bonusSegurado = seg.getBonus();
        BigDecimal VPC = VPB.subtract(bonusSegurado.divide(new BigDecimal("10"), RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
        VPC = VPC.max(BigDecimal.ZERO);


        LocalDate dataInicio = LocalDate.now();
        BigDecimal franquia = VPB.multiply(new BigDecimal("1.3")).setScale(2, RoundingMode.HALF_UP);
        Apolice apolice = new Apolice(
                generateNumeroApolice(dados, isEmpresa),
                veiculo,
                VPC,
                franquia,
                dados.getValorMaximoSegurado()
        );

        daoApo.incluir(apolice);

        return new RetornoInclusaoApolice(apolice.getNumero(), null);
    }

    private String generateNumeroApolice(DadosVeiculo dados, boolean isEmpresa) {
        int anoAtual = LocalDate.now().getYear();
        return isEmpresa
                ? anoAtual + dados.getCpfOuCnpj() + dados.getPlaca()
                : anoAtual + "000" + dados.getCpfOuCnpj() + dados.getPlaca();
    }

    public Apolice buscarApolice(String numero) {
        if (numero == null || numero.isBlank()) {
            return null;
        }
        return daoApo.buscar(numero);
    }

    public String excluirApolice(String numero) {
        return null;
    }
}