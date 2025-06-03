package br.edu.cs.poo.ac.seguro.testes;

import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class TelaInclusaoApoliceFX extends Application {

    private ApoliceMediator mediator;

    private TextField txtCpfCnpj;
    private TextField txtPlaca;
    private TextField txtAno;
    private TextField txtValorMaximoSegurado;
    private ComboBox<CategoriaVeiculo> cmbCategoriaVeiculo;

    private Button btnIncluir;
    private Button btnLimpar;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    public TelaInclusaoApoliceFX() {
        this.mediator = ApoliceMediator.getInstancia();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Apólices de Seguro");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 30, 30, 30));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(150);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(220);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(100);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        initComponents();
        setupLayout(grid);
        addListeners();
        setupTabOrder();

        Scene scene = new Scene(grid, 580, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        txtCpfCnpj = new TextField();
        txtCpfCnpj.setPromptText("CPF ou CNPJ do Segurado");
        txtCpfCnpj.setMaxWidth(200);

        txtPlaca = new TextField();
        txtPlaca.setPromptText("Ex: ABC1234");
        txtPlaca.setMaxWidth(120);

        txtAno = new TextField();
        txtAno.setPromptText("Ex: 2023");
        txtAno.setMaxWidth(100);
        setupYearMask(txtAno);

        txtValorMaximoSegurado = new TextField();
        txtValorMaximoSegurado.setPromptText("Ex: 100.000,00");
        setupCurrencyMask(txtValorMaximoSegurado);
        txtValorMaximoSegurado.setMaxWidth(180);

        cmbCategoriaVeiculo = new ComboBox<>();
        cmbCategoriaVeiculo.getItems().addAll(
                Arrays.stream(CategoriaVeiculo.values())
                        .sorted(Comparator.comparing(CategoriaVeiculo::getNome))
                        .collect(java.util.stream.Collectors.toList())
        );
        cmbCategoriaVeiculo.setConverter(new StringConverter<CategoriaVeiculo>() {
            @Override
            public String toString(CategoriaVeiculo categoria) {
                return categoria != null ? categoria.getNome() : "";
            }

            @Override
            public CategoriaVeiculo fromString(String string) {
                return null;
            }
        });
        if (!cmbCategoriaVeiculo.getItems().isEmpty()) {
            cmbCategoriaVeiculo.getSelectionModel().selectFirst();
        }
        cmbCategoriaVeiculo.setPrefWidth(200);

        btnIncluir = new Button("Incluir Apólice");
        btnIncluir.setPrefWidth(130);
        // btnIncluir.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;"); // REMOVIDO

        btnLimpar = new Button("Limpar Campos");
        btnLimpar.setPrefWidth(130);
        // btnLimpar.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;"); // REMOVIDO
    }

    private void setupLayout(GridPane grid) {
        int row = 0;

        // Título Principal
        Label mainTitle = new Label("Inclusão de Apólice de Seguro");
        mainTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");
        GridPane.setHalignment(mainTitle, javafx.geometry.HPos.CENTER);
        grid.add(mainTitle, 0, row, 3, 1);
        row++;

        // Separador visual
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // Campos de entrada
        grid.add(new Label("CPF/CNPJ Segurado:"), 0, row);
        grid.add(txtCpfCnpj, 1, row, 2, 1);
        row++;

        grid.add(new Label("Placa Veículo:"), 0, row);
        grid.add(txtPlaca, 1, row);
        row++;

        grid.add(new Label("Ano Veículo:"), 0, row);
        grid.add(txtAno, 1, row);
        row++;

        grid.add(new Label("Valor Máximo Segurado:"), 0, row);
        grid.add(txtValorMaximoSegurado, 1, row);
        row++;

        grid.add(new Label("Categoria Veículo:"), 0, row);
        grid.add(cmbCategoriaVeiculo, 1, row);
        row++;

        // Separador visual antes dos botões
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // HBox para os botões
        HBox hbButtons = new HBox(15);
        hbButtons.setAlignment(Pos.BOTTOM_RIGHT);
        hbButtons.getChildren().addAll(btnIncluir, btnLimpar);
        grid.add(hbButtons, 1, row, 2, 1);
    }

    private void addListeners() {
        btnIncluir.setOnAction(e -> incluirApolice());
        btnLimpar.setOnAction(e -> limparCampos());
    }

    private void setupTabOrder() {
        txtCpfCnpj.setFocusTraversable(true);
        txtPlaca.setFocusTraversable(true);
        txtAno.setFocusTraversable(true);
        txtValorMaximoSegurado.setFocusTraversable(true);
        cmbCategoriaVeiculo.setFocusTraversable(true);
        btnIncluir.setFocusTraversable(true);
        btnLimpar.setFocusTraversable(true);
    }

    // --- Máscaras e Validações ---

    private void setupYearMask(TextField textField) {
        Pattern pattern = Pattern.compile("\\d{0,4}");
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
                        int ano = Integer.parseInt(text);
                        if (ano < 2020 || ano > 2025) {
                            textField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Ano tem que estar entre 2020 e 2025, incluindo estes.");
                        } else {
                            textField.setStyle("");
                        }
                    } catch (NumberFormatException e) {
                        textField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                        showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Ano inválido. Digite apenas números.");
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
                        BigDecimal value = new BigDecimal(cleanText);
                        textField.setText(DECIMAL_FORMAT.format(value));
                        textField.setStyle("");
                    } catch (NumberFormatException e) {
                        textField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                        showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Valor inválido. Use apenas números, vírgula para centavos e ponto para milhares (opcional).");
                    }
                } else {
                    textField.setStyle("");
                }
            }
        });
    }

    // --- Lógica de Negócio e UI ---

    private void incluirApolice() {
        try {
            if (txtAno.getStyle().contains("red") || txtValorMaximoSegurado.getStyle().contains("red")) {
                showAlert(Alert.AlertType.ERROR, "Erro de Entrada", "Corrija os campos com formato inválido antes de incluir.");
                return;
            }

            String cpfCnpj = txtCpfCnpj.getText().trim();
            String placa = txtPlaca.getText().trim();
            int ano = 0;
            if (!txtAno.getText().trim().isEmpty()) {
                ano = Integer.parseInt(txtAno.getText().trim());
            }

            BigDecimal valorMaximoSegurado = null;
            if (!txtValorMaximoSegurado.getText().trim().isEmpty()) {
                String cleanValor = txtValorMaximoSegurado.getText().trim().replace(".", "").replace(",", ".");
                valorMaximoSegurado = new BigDecimal(cleanValor);
            }

            CategoriaVeiculo categoriaSelecionada = cmbCategoriaVeiculo.getSelectionModel().getSelectedItem();
            int codigoCategoria = (categoriaSelecionada != null) ? categoriaSelecionada.getCodigo() : 0;

            DadosVeiculo dados = new DadosVeiculo(cpfCnpj, placa, ano, valorMaximoSegurado, codigoCategoria);

            RetornoInclusaoApolice retorno = mediator.incluirApolice(dados);

            if (retorno.getMensagemErro() == null) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Apólice incluída com sucesso! Anote o número da apólice: " + retorno.getNumeroApolice());
                limparCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Problemas na inclusão da apólice:\n" + retorno.getMensagemErro());
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique se o Ano e Valor Máximo Segurado estão corretos. Detalhes: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Erro Interno", "Erro inesperado na construção do retorno: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtCpfCnpj.clear();
        txtPlaca.clear();
        txtAno.clear();
        txtValorMaximoSegurado.clear();
        if (!cmbCategoriaVeiculo.getItems().isEmpty()) {
            cmbCategoriaVeiculo.getSelectionModel().selectFirst();
        }
        txtCpfCnpj.setStyle("");
        txtPlaca.setStyle("");
        txtAno.setStyle("");
        txtValorMaximoSegurado.setStyle("");
        txtCpfCnpj.requestFocus();
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
            SeguradoPessoaDAO segPesDAO = new SeguradoPessoaDAO();
            SeguradoEmpresaDAO segEmpDAO = new SeguradoEmpresaDAO();

            Endereco endPessoa = new Endereco("Rua P", "11111-111", "10", "", "Brasil", "PE", "Recife");
            SeguradoPessoa pessoa = new SeguradoPessoa("Cliente Pessoa", endPessoa, LocalDate.of(1980, 5, 10), new BigDecimal("500.00"),"12345678909",1000);
            if (segPesDAO.buscar(pessoa.getIdUnico()) == null) {
                if (segPesDAO.incluir(pessoa)) {
                    System.out.println("Segurado Pessoa 12345678909 incluído.");
                } else {
                    System.out.println("Erro ao incluir Segurado Pessoa 12345678909.");
                }
            } else {
                System.out.println("Segurado Pessoa 12345678909 já existe.");
            }

            Endereco endEmpresa = new Endereco("Av. E", "22222-222", "200", "Sala 1", "Brasil", "SP", "São Paulo");
            SeguradoEmpresa empresa = new SeguradoEmpresa( "Empresa Teste", endEmpresa, LocalDate.of(2000, 1, 1), new BigDecimal("1000.00"), "11222333000144",1000,false);
            if (segEmpDAO.buscar(empresa.getIdUnico()) == null) {
                if (segEmpDAO.incluir(empresa)) {
                    System.out.println("Segurado Empresa 11222333000144 incluído.");
                } else {
                    System.out.println("Erro ao incluir Segurado Empresa 11222333000144.");
                }
            } else {
                System.out.println("Segurado Empresa 11222333000144 já existe.");
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