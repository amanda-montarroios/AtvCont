package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;

public class SinistroMediator {

    private VeiculoDAO daoVeiculo = new VeiculoDAO();
    private ApoliceDAO daoApolice = new ApoliceDAO();
    private SinistroDAO daoSinistro = new SinistroDAO();
    private static SinistroMediator instancia;
    public static SinistroMediator getInstancia() {
        if (instancia == null)
            instancia = new SinistroMediator();
        return instancia;
    }
    private SinistroMediator() {}

    public String incluirSinistro(DadosSinistro dados, LocalDateTime dataHoraAtual) throws ExcecaoValidacaoDados {
        ExcecaoValidacaoDados excecao = new ExcecaoValidacaoDados();

        if (dados == null) {
            excecao.getMensagens().add("Dados do sinistro devem ser informados");
            throw excecao;
        }

        if (dados.getDataHoraSinistro() == null) {
            excecao.getMensagens().add("Data/hora do sinistro deve ser informada");
        } else if (!dados.getDataHoraSinistro().isBefore(dataHoraAtual)) {
            excecao.getMensagens().add("Data/hora do sinistro deve ser menor que a data/hora atual");
        }

        if (dados.getPlaca() == null || dados.getPlaca().trim().isEmpty()) {
            excecao.getMensagens().add("Placa do Veículo deve ser informada");
        }

        if (dados.getUsuarioRegistro() == null || dados.getUsuarioRegistro().trim().isEmpty()) {
            excecao.getMensagens().add("Usuário do registro de sinistro deve ser informado");
        }

        if (dados.getValorSinistro() <= 0) {
            excecao.getMensagens().add("Valor do sinistro deve ser maior que zero");
        }

        boolean tipoValido = false;
        for (TipoSinistro tipo : TipoSinistro.values()) {
            if (tipo.getCodigo() == dados.getCodigoTipoSinistro()) {
                tipoValido = true;
                break;
            }
        }
        if (!tipoValido) {
            excecao.getMensagens().add("Código do tipo de sinistro inválido");
        }

        Veiculo veiculo = null;
        Apolice apoliceVigente = null;

        if (dados.getPlaca() != null && !dados.getPlaca().trim().isEmpty()) {
            veiculo = daoVeiculo.buscar(dados.getPlaca());
            if (veiculo == null) {
                excecao.getMensagens().add("Veículo não cadastrado");
            } else if (dados.getDataHoraSinistro() != null && dados.getDataHoraSinistro().isBefore(dataHoraAtual)) {
                Apolice[] apolicesArray = Optional.ofNullable(daoApolice.buscarTodos()).orElse(new Apolice[0]);
                List<Apolice> apolices = Arrays.asList(apolicesArray);
                apoliceVigente = apolices.stream()
                        .filter(ap -> ap.getVeiculo().getPlaca().equals(dados.getPlaca()))
                        .filter(ap -> {
                            LocalDate inicio = ap.getDataInicioVigencia();
                            LocalDate fim = inicio.plusYears(1);
                            LocalDate dataSinistro = dados.getDataHoraSinistro().toLocalDate();
                            return (dataSinistro.isEqual(inicio) || dataSinistro.isAfter(inicio)) &&
                                    (dataSinistro.isBefore(fim) || dataSinistro.isEqual(fim));
                        })
                        .findFirst()
                        .orElse(null);

                if (apoliceVigente == null) {
                    excecao.getMensagens().add("Não existe apólice vigente para o veículo");
                } else if (dados.getValorSinistro() > apoliceVigente.getValorMaximoSegurado().doubleValue()) {
                    excecao.getMensagens().add("Valor do sinistro não pode ultrapassar o valor máximo segurado constante na apólice");
                }
            }
        }

        if (!excecao.getMensagens().isEmpty()) {
            throw excecao;
        }

        Sinistro[] sinistrosArray = Optional.ofNullable(daoSinistro.buscarPorNumeroApolice(apoliceVigente.getNumero()))
                .orElse(new Sinistro[0]);
        List<Sinistro> sinistros = Arrays.asList(sinistrosArray);

        int sequencial = 1;
        if (!sinistros.isEmpty()) {
            sinistros.sort(new ComparadorSinistroSequencial());
            sequencial = sinistros.get(sinistros.size() - 1).getSequencial() + 1;
        }

        String numeroSinistro = String.format("S%s%03d", apoliceVigente.getNumero(), sequencial);

        TipoSinistro tipoSinistro = null;
        for (TipoSinistro tipo : TipoSinistro.values()) {
            if (tipo.getCodigo() == dados.getCodigoTipoSinistro()) {
                tipoSinistro = tipo;
                break;
            }
        }

        Sinistro sinistro = new Sinistro(numeroSinistro, veiculo, dados.getDataHoraSinistro(), dataHoraAtual,
                dados.getUsuarioRegistro(), new BigDecimal(dados.getValorSinistro()), tipoSinistro);

        sinistro.setNumeroApolice(apoliceVigente.getNumero());
        sinistro.setSequencial(sequencial);

        daoSinistro.incluir(sinistro);

        return numeroSinistro;
    }
}