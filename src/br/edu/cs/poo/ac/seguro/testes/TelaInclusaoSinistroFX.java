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
import javafx.scene.layout.ColumnConstraints; // Adicionado para controle de colunas
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
        primaryStage.setTitle("Sistema de Inclusão de Sinistro"); // Título da janela principal

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT); // Alinhamento consistente
        grid.setHgap(15); // Espaçamento horizontal consistente
        grid.setVgap(10); // Espaçamento vertical consistente
        grid.setPadding(new Insets(30, 30, 30, 30)); // Padding consistente

        // Configuração das colunas para melhor alinhamento (igual ao CRUDSeguradoPessoaFX)
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(150); // Largura um pouco maior para labels
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(220); // Largura preferencial para campos de texto
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(100); // Espaço para botões (se houver, aqui não tem em todas as linhas)
        grid.getColumnConstraints().addAll(col1, col2, col3);

        initComponents();
        setupLayout(grid);
        addListeners();
        setupTabOrder();

        Scene scene = new Scene(grid, 580, 420); // Tamanho ajustado para o novo layout
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        txtPlaca = new TextField();
        txtPlaca.setPromptText("Ex: ABC1234");
        txtPlaca.setMaxWidth(120);

        txtDataHoraSinistro = new TextField();
        txtDataHoraSinistro.setPromptText("dd/MM/yyyy HH:mm:ss");
        txtDataHoraSinistro.setMaxWidth(180); // Ajuste de largura
        setupDateTimeMask(txtDataHoraSinistro);

        txtUsuarioRegistro = new TextField();
        txtUsuarioRegistro.setPromptText("Nome do Usuário");
        txtUsuarioRegistro.setMaxWidth(200); // Ajuste de largura

        txtValorSinistro = new TextField();
        txtValorSinistro.setPromptText("Ex: 1234,56");
        txtValorSinistro.setMaxWidth(150); // Ajuste de largura
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
        cmbTipoSinistro.setPrefWidth(200); // Define uma largura preferencial

        btnIncluir = new Button("Incluir Sinistro");
        btnIncluir.setPrefWidth(130); // Largura padronizada
        // Removido: btnIncluir.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");

        btnLimpar = new Button("Limpar Campos");
        btnLimpar.setPrefWidth(130); // Largura padronizada
        // Removido: btnLimpar.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");
    }

    private void setupLayout(GridPane grid) {
        int row = 0;

        // Título Principal (igual ao CRUDSeguradoPessoaFX)
        Label mainTitle = new Label("Inclusão de Sinistro");
        mainTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");
        GridPane.setHalignment(mainTitle, javafx.geometry.HPos.CENTER);
        grid.add(mainTitle, 0, row, 3, 1); // Ocupa 3 colunas
        row++;

        // Separador visual (igual ao CRUDSeguradoPessoaFX)
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // Campos de entrada
        grid.add(new Label("Placa:"), 0, row);
        grid.add(txtPlaca, 1, row);
        row++;

        grid.add(new Label("Data/Hora Sinistro:"), 0, row);
        grid.add(txtDataHoraSinistro, 1, row);
        row++;

        grid.add(new Label("Usuário Registro:"), 0, row);
        grid.add(txtUsuarioRegistro, 1, row, 2, 1); // Ocupa 2 colunas
        row++;

        grid.add(new Label("Valor Sinistro:"), 0, row);
        grid.add(txtValorSinistro, 1, row);
        row++;

        grid.add(new Label("Tipo Sinistro:"), 0, row);
        grid.add(cmbTipoSinistro, 1, row);
        row++;

        // Separador visual antes dos botões
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // HBox para os botões (similar ao CRUDSeguradoPessoaFX)
        HBox hbButtons = new HBox(15); // Espaçamento maior entre os botões
        hbButtons.setAlignment(Pos.BOTTOM_RIGHT); // Alinha os botões à direita
        hbButtons.getChildren().addAll(btnIncluir, btnLimpar);
        grid.add(hbButtons, 1, row, 2, 1); // Adiciona na coluna 1, ocupando 2 colunas
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
                        textField.setStyle("-fx-border-color: red; -fx-border-width: 2;"); // Borda mais grossa
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
                        textField.setStyle("-fx-border-color: red; -fx-border-width: 2;"); // Borda mais grossa
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
            txtDataHoraSinistro.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Valor do sinistro inválido. Use apenas números, vírgula para centavos e ponto para milhares (opcional).");
            txtValorSinistro.setStyle("-fx-border-color: red; -fx-border-width: 2;");
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

    public static void main(String[] args) {
        try {
            VeiculoDAO veiculoDAO = new VeiculoDAO();
            ApoliceDAO apoliceDAO = new ApoliceDAO();
            SeguradoPessoaDAO segPesDAO = new SeguradoPessoaDAO();
            SeguradoEmpresaDAO segEmpDAO = new SeguradoEmpresaDAO();

            Endereco enderecoExemplo = new Endereco("Rua Exemplo", "50000-000", "123", "Ap. 101", "Brasil", "PE", "Recife");

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

            Apolice apoliceExemplo = new Apolice(
                    "APOLICE001",
                    veiculoExemplo,
                    new BigDecimal("500.00"),
                    new BigDecimal("1200.00"),
                    new BigDecimal("100000.00"),
                    LocalDate.of(2025, 1, 1)
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