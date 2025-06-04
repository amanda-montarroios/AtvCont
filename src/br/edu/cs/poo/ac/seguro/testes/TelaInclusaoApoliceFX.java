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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException; //para caso precise
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
        primaryStage.setTitle("Inclusão de Apólice");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        initComponents();
        setupLayout(grid);
        addListeners();
        setupTabOrder();

        Scene scene = new Scene(grid, 500, 380); // Tamanho ajustado
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        txtCpfCnpj = new TextField();
        txtCpfCnpj.setPromptText("CPF ou CNPJ");
        txtCpfCnpj.setMaxWidth(150);

        txtPlaca = new TextField();
        txtPlaca.setPromptText("Ex: ABC1234");
        txtPlaca.setMaxWidth(100);

        txtAno = new TextField();
        txtAno.setPromptText("Ex: 2023");
        txtAno.setMaxWidth(80);
        setupYearMask(txtAno); // Máscara para ano

        txtValorMaximoSegurado = new TextField();
        txtValorMaximoSegurado.setPromptText("Ex: 100000,00");
        setupCurrencyMask(txtValorMaximoSegurado); // Máscara para valor

        // Combo Box para CategoriaVeiculo
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

        btnIncluir = new Button("Incluir");
        btnLimpar = new Button("Limpar");
    }

    private void setupLayout(GridPane grid) {
        grid.add(new Label("CPF/CNPJ Segurado:"), 0, 0);
        grid.add(txtCpfCnpj, 1, 0);

        grid.add(new Label("Placa Veículo:"), 0, 1);
        grid.add(txtPlaca, 1, 1);

        grid.add(new Label("Ano Veículo:"), 0, 2);
        grid.add(txtAno, 1, 2);

        grid.add(new Label("Valor Máximo Segurado:"), 0, 3);
        grid.add(txtValorMaximoSegurado, 1, 3);

        grid.add(new Label("Categoria Veículo:"), 0, 4);
        grid.add(cmbCategoriaVeiculo, 1, 4);

        HBox hbButtons = new HBox(10);
        hbButtons.setAlignment(Pos.BOTTOM_RIGHT);
        hbButtons.getChildren().addAll(btnIncluir, btnLimpar);
        grid.add(hbButtons, 1, 5);
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
        // Permite apenas 4 dígitos numéricos
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
            if (!newVal) { // Se perdeu o foco
                String text = textField.getText().trim();
                if (!text.isEmpty()) {
                    try {
                        int ano = Integer.parseInt(text);
                        if (ano < 2020 || ano > 2025) { // Validação de ano conforme ApoliceMediator
                            textField.setStyle("-fx-border-color: red;");
                            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Ano tem que estar entre 2020 e 2025, incluindo estes.");
                        } else {
                            textField.setStyle("");
                        }
                    } catch (NumberFormatException e) {
                        textField.setStyle("-fx-border-color: red;");
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
                        BigDecimal value = new BigDecimal(cleanText); // Usar BigDecimal para precisão monetária
                        textField.setText(DECIMAL_FORMAT.format(value));
                        textField.setStyle("");
                    } catch (NumberFormatException e) {
                        textField.setStyle("-fx-border-color: red;");
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
            // Validar formatos antes de chamar o mediator
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

            // Criar DadosVeiculo para o mediator
            DadosVeiculo dados = new DadosVeiculo(cpfCnpj, placa, ano, valorMaximoSegurado, codigoCategoria);

            RetornoInclusaoApolice retorno = mediator.incluirApolice(dados);

            // A lógica de sucesso/erro agora é baseada na sua RetornoInclusaoApolice
            if (retorno.getMensagemErro() == null) { // Sucesso se mensagemErro é null
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Apólice incluída com sucesso! Anote o número da apólice: " + retorno.getNumeroApolice());
                limparCampos();
            } else { // Erro se mensagemErro não é null
                showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Problemas na inclusão da apólice:\n" + retorno.getMensagemErro());
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique se o Ano e Valor Máximo Segurado estão corretos. Detalhes: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) { // Captura as RuntimeExceptions do construtor de RetornoInclusaoApolice
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

    public static void main(String[] args) { // Este main lançará esta aplicação diretamente
        // Inicializa DAOs e adiciona dados de teste para a Apolice
        try {
            SeguradoPessoaDAO segPesDAO = new SeguradoPessoaDAO();
            SeguradoEmpresaDAO segEmpDAO = new SeguradoEmpresaDAO();

            // Adicionar Segurado Pessoa para teste (CPF válido)
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

            // Adicionar Segurado Empresa para teste (CNPJ válido)
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