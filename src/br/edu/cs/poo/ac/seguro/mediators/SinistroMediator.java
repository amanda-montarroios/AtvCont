package br.edu.cs.poo.ac.seguro.mediators;

import java.time.LocalDateTime;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
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
    /* - esta classe deve ser um Singleton.
     * - o método recebe como um dos parâmetros o tipo DadosSinistro, que deve ser criado no
     *   pacote br.edu.cs.poo.ac.seguro.mediators, e ter os seguintes atributos, com set e
     *   get e construtor que inicializa os atributos, todos públicos:
     *    	private String placa;
     * 		private LocalDateTime dataHoraSinistro;
     * 		private String usuarioRegistro;
     * 		private double valorSinistro;
     * 		private int codigoTipoSinistro;
     * - as mensagens de erros de validação devem ser acumuladas em uma lista, que é atributo da classe
     *   ExcecaoValidacaoDados. Este atributo deve se chamar mensagens, ter um get público, e
     *   ser inicializado no construtor da classe. Esta classe deve ser criada no pacote
     *   br.edu.cs.poo.ac.seguro.excecoes.
     *   - a exceção será lançada se o sinistro não for gerado e incluído no DAO.
     * - dados não pode ser null
     * - data/hora do sinistro não pode ser null
     * - data/hora do sinistro deve ser menor que a data/hora atual
     * - placa do veículo não pode ser null nem branco
     * - placa, se informada, deve ser de um veículo cadastrado
     * - usuário do registro não pode ser null nem branco
     * - valor do sinistro deve ser maior que zero
     * - o código do tipo de sinistro deve ser um correspondente aos tipos de sinistro
     *   especificados no enum TipoSinistro
     * - um sinistro só deve ser registrado se houver apólice vigente em relação à data e hora do sinistro
     *   (a apólice tem vigência de 1 ano, a contar da sua data de início de vigência), que cubra o veículo em questão
     * - para saber isso, deve-se buscar todas as apólices, e procurar a que esteja vigente e que
     *   tenha veículo associado cuja placa é igual à placa informada nos dados do sinistro.
     * - uma vez encontrada a apólice, deve-se validar o valor do sinistro, que não pode ser maior
     *   do que o valor máximo segurado constante na apólice encontrada.
     * - após estas validações, deve-se formar o número do sinistro, que é:
     *   "S" + numeroDaApoliceDeCobertura + sequencial
     * - o sequencial deve formar o número do sinistro sempre com 3 dígitos, completando-se com zeros à
     *   esquerda números que são menores do que 100.
     * - o sequencial deve ser inferido da seguinte forma:
     * - se não existir gravado sinistro com mesmo número de apólice do sinsitro a ser incluído, sequencial = 1
     * - se existir gravado sinistro com mesmo número de apólice do sinsitro a ser incluído, sequencial =
     *   maior sequencial entre os sinistros encontrados mais um.
     * - uma forma de encontrar o maior sequencial dentre os sinistros é ordená-los por sequencial. para isso,
     *   deve-se usar o esquema de ordenação do JAVA (Collections.sort) e um Comparator específico,
     *   que deve ficar no pacote dos mediators, e se chamar ComparadorSinistroSequencial.
     * - por fim, deve-se instanciar o sinistro com os dados recebidos, e o veículo lido, setar nele o número da
     *   apólice e o sequencial.
     */
    public String incluirSinistro(DadosSinistro dados, LocalDateTime dataHoraAtual) throws ExcecaoValidacaoDados {
        return null;
    }
}