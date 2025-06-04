package br.edu.cs.poo.ac.seguro.testes;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.*;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class TelaInclusaoSinistroFX extends Application {

    private SinistroMediator mediator;

    private TextField txtPlaca;
    private TextField txtDataHoraSinistro;
    private TextField txtUsuarioRegistro;
    private TextField txtValorSinistro;
    private ComboBox<TipoSinistro> cmbTipoSinistro;

    private Button btnIncluir;
    private Button btnLimpar;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    public TelaInclusaoSinistroFX() {
        this.mediator = SinistroMediator.getInstancia();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inclusão de Sinistro");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        initComponents();
        setupLayout(grid);
        addListeners();
        setupTabOrder();

        Scene scene = new Scene(grid, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        txtPlaca = new TextField();
        txtPlaca.setPromptText("Ex: ABC1234");
        txtPlaca.setMaxWidth(120);

        txtDataHoraSinistro = new TextField();
        txtDataHoraSinistro.setPromptText("dd/MM/yyyy HH:mm:ss");
        setupDateTimeMask(txtDataHoraSinistro);

        txtUsuarioRegistro = new TextField();
        txtUsuarioRegistro.setPromptText("Nome do Usuário");

        txtValorSinistro = new TextField();
        txtValorSinistro.setPromptText("Ex: 1234,56");
        setupCurrencyMask(txtValorSinistro);

        cmbTipoSinistro = new ComboBox<>();
        cmbTipoSinistro.getItems().addAll(
                Arrays.stream(TipoSinistro.values())
                        .sorted(Comparator.comparing(TipoSinistro::getNome))
                        .collect(java.util.stream.Collectors.toList())
        );
        cmbTipoSinistro.setConverter(new StringConverter<TipoSinistro>() {
            @Override
            public String toString(TipoSinistro tipo) {
                return tipo != null ? tipo.getNome() : "";
            }

            @Override
            public TipoSinistro fromString(String string) {
                return null;
            }
        });
        if (!cmbTipoSinistro.getItems().isEmpty()) {
            cmbTipoSinistro.getSelectionModel().selectFirst();
        }

        btnIncluir = new Button("Incluir");
        btnLimpar = new Button("Limpar");
    }

    private void setupLayout(GridPane grid) {
        grid.add(new Label("Placa:"), 0, 0);
        grid.add(txtPlaca, 1, 0);

        grid.add(new Label("Data/Hora Sinistro:"), 0, 1);
        grid.add(txtDataHoraSinistro, 1, 1);

        grid.add(new Label("Usuário Registro:"), 0, 2);
        grid.add(txtUsuarioRegistro, 1, 2);

        grid.add(new Label("Valor Sinistro:"), 0, 3);
        grid.add(txtValorSinistro, 1, 3);

        grid.add(new Label("Tipo Sinistro:"), 0, 4);
        grid.add(cmbTipoSinistro, 1, 4);

        HBox hbButtons = new HBox(10);
        hbButtons.setAlignment(Pos.BOTTOM_RIGHT);
        hbButtons.getChildren().addAll(btnIncluir, btnLimpar);
        grid.add(hbButtons, 1, 5);
    }

    private void addListeners() {
        btnIncluir.setOnAction(e -> incluirSinistro());
        btnLimpar.setOnAction(e -> limparCampos());
    }

    private void setupTabOrder() {
        txtPlaca.setFocusTraversable(true);
        txtDataHoraSinistro.setFocusTraversable(true);
        txtUsuarioRegistro.setFocusTraversable(true);
        txtValorSinistro.setFocusTraversable(true);
        cmbTipoSinistro.setFocusTraversable(true);
        btnIncluir.setFocusTraversable(true);
        btnLimpar.setFocusTraversable(true);
    }

    private void setupDateTimeMask(TextField textField) {
        final String format = "dd/MM/yyyy HH:mm:ss";
        Pattern pattern = Pattern.compile("[0-9/ :]*");
        UnaryOperator<Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                return c;
            } else {
                return null;
            }
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = textField.getText().trim();
                if (!text.isEmpty()) {
                    try {
                        LocalDateTime.parse(text, DATE_TIME_FORMATTER);
                        textField.setStyle("");
                    } catch (DateTimeParseException e) {
                        textField.setStyle("-fx-border-color: red;");
                        showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Data/Hora do sinistro inválida. Use o formato " + format);
                    }
                } else {
                    textField.setStyle("");
                }
            }
        });
    }

    private void setupCurrencyMask(TextField textField) {
        Pattern pattern = Pattern.compile("[0-9.,]*");
        UnaryOperator<Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                return c;
            } else {
                return null;
            }
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = textField.getText().trim();
                if (!text.isEmpty()) {
                    try {
                        String cleanText = text.replace(".", "").replace(",", ".");
                        double value = Double.parseDouble(cleanText);
                        textField.setText(DECIMAL_FORMAT.format(value));
                        textField.setStyle("");
                    } catch (NumberFormatException e) {
                        textField.setStyle("-fx-border-color: red;");
                        showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Valor do sinistro inválido. Use apenas números, vírgula para centavos e ponto para milhares (opcional).");
                    }
                } else {
                    textField.setStyle("");
                }
            }
        });
    }

    private void incluirSinistro() {
        try {
            if (txtDataHoraSinistro.getStyle().contains("red") || txtValorSinistro.getStyle().contains("red")) {
                showAlert(Alert.AlertType.ERROR, "Erro de Entrada", "Corrija os campos com formato inválido antes de incluir.");
                return;
            }

            String placa = txtPlaca.getText().trim();
            String usuarioRegistro = txtUsuarioRegistro.getText().trim();

            LocalDateTime dataHoraSinistro = null;
            if (!txtDataHoraSinistro.getText().trim().isEmpty()) {
                dataHoraSinistro = LocalDateTime.parse(txtDataHoraSinistro.getText().trim(), DATE_TIME_FORMATTER);
            }

            double valorSinistro = 0.0;
            if (!txtValorSinistro.getText().trim().isEmpty()) {
                String cleanValor = txtValorSinistro.getText().trim().replace(".", "").replace(",", ".");
                valorSinistro = Double.parseDouble(cleanValor);
            }

            TipoSinistro tipoSinistroSelecionado = cmbTipoSinistro.getSelectionModel().getSelectedItem();
            int codigoTipoSinistro = (tipoSinistroSelecionado != null) ? tipoSinistroSelecionado.getCodigo() : 0;

            DadosSinistro dados = new DadosSinistro(placa, dataHoraSinistro, usuarioRegistro, valorSinistro, codigoTipoSinistro);

            String numeroSinistro = mediator.incluirSinistro(dados, LocalDateTime.now());

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Sinistro incluído com sucesso! Anote o número do sinistro: " + numeroSinistro);
            limparCampos();

        } catch (ExcecaoValidacaoDados e) {
            String mensagensErro = String.join("\n", e.getMensagens());
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Problemas na inclusão do sinistro:\n" + mensagensErro);
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Data/Hora do sinistro inválida. Use o formato dd/MM/yyyy HH:mm:ss.");
            txtDataHoraSinistro.setStyle("-fx-border-color: red;");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Valor do sinistro inválido. Use apenas números, vírgula para centavos e ponto para milhares (opcional).");
            txtValorSinistro.setStyle("-fx-border-color: red;");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtPlaca.clear();
        txtDataHoraSinistro.clear();
        txtUsuarioRegistro.clear();
        txtValorSinistro.clear();
        if (!cmbTipoSinistro.getItems().isEmpty()) {
            cmbTipoSinistro.getSelectionModel().selectFirst();
        }
        txtPlaca.setStyle("");
        txtDataHoraSinistro.setStyle("");
        txtUsuarioRegistro.setStyle("");
        txtValorSinistro.setStyle("");
        txtPlaca.requestFocus();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) { // Este main lançará esta aplicação diretamente
        // Inicializa as DAOs e adiciona alguns dados de exemplo para teste
        try {
            // Instancie os DAOs que serão usados para incluir dados de teste
            VeiculoDAO veiculoDAO = new VeiculoDAO();
            ApoliceDAO apoliceDAO = new ApoliceDAO();
            SeguradoPessoaDAO segPesDAO = new SeguradoPessoaDAO();
            SeguradoEmpresaDAO segEmpDAO = new SeguradoEmpresaDAO(); // Adicionado para inicialização, se necessário

            // --- Dados de Teste para Sinistro (para testar inclusão de sinistro) ---
            // Crie um endereço e segurado de exemplo
            Endereco enderecoExemplo = new Endereco("Rua Exemplo", "50000-000", "123", "Ap. 101", "Brasil", "PE", "Recife");

            // Crie uma instância de SeguradoPessoa (que é uma subclasse concreta de Segurado)
            SeguradoPessoa seguradoPessoaTeste = new SeguradoPessoa( "Carlos Teste", enderecoExemplo, LocalDate.of(1980, 1, 1), new BigDecimal("100.00"),"98765432100",10000);
            if (segPesDAO.buscar(seguradoPessoaTeste.getIdUnico()) == null) {
                if (segPesDAO.incluir(seguradoPessoaTeste)) {
                    System.out.println("Segurado Pessoa de Teste 98765432100 incluído.");
                } else {
                    System.out.println("Erro ao incluir Segurado Pessoa de Teste 98765432100.");
                }
            } else {
                System.out.println("Segurado Pessoa de Teste 98765432100 já existe.");
            }

            // Crie um veículo de exemplo e inclua no DAO
            Veiculo veiculoExemplo = new Veiculo("ABC1234", 2023, seguradoPessoaTeste, CategoriaVeiculo.BASICO);
            if (veiculoDAO.buscar(veiculoExemplo.getIdUnico()) == null) {
                if (!veiculoDAO.incluir(veiculoExemplo)) {
                    System.out.println("Erro ao incluir veículo ABC1234.");
                } else {
                    System.out.println("Veículo ABC1234 incluído para teste.");
                }
            } else {
                System.out.println("Veículo ABC1234 já existe.");
            }

            // Crie uma apólice vigente para o veículo de exemplo e inclua no DAO
            // Data atual: Monday, June 2, 2025 at 5:31:19 PM -03.
            // A apólice deve ser vigente para a data atual (02/06/2025).
            Apolice apoliceExemplo = new Apolice(
                    "APOLICE001",
                    veiculoExemplo,
                    new BigDecimal("500.00"),
                    new BigDecimal("1200.00"),
                    new BigDecimal("100000.00"), // Valor Máximo Segurado (alto para não dar erro fácil)
                    LocalDate.of(2025, 1, 1) // Data de Início de Vigência: 01/01/2025 (vigente em 02/06/2025)
            );
            if (apoliceDAO.buscar(apoliceExemplo.getIdUnico()) == null) {
                if (!apoliceDAO.incluir(apoliceExemplo)) {
                    System.out.println("Erro ao incluir apólice APOLICE001.");
                } else {
                    System.out.println("Apólice APOLICE001 incluída para teste.");
                }
            } else {
                System.out.println("Apólice APOLICE001 já existe.");
            }

        } catch (RuntimeException e) {
            System.err.println("Erro na inicialização dos dados de teste: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado na inicialização: " + e.getMessage());
            e.printStackTrace();
        }

        launch(args);
    }
}