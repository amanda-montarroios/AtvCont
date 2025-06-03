package br.edu.cs.poo.ac.seguro.testes;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.StringUtils;
import br.edu.cs.poo.ac.seguro.mediators.ValidadorCpfCnpj;

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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class TelaCRUDSeguradoPessoaFX extends Application {

    private SeguradoPessoaDAO seguradoPessoaDAO;

    // Campos de Segurado Pessoa
    private TextField txtCpf;
    private TextField txtNome;
    private TextField txtDataNascimento;
    private TextField txtBonus;
    private TextField txtRenda;
    private Label lblIdUnicoValue; // Novo campo para exibir o ID único (CPF)

    // Campos de Endereco
    private TextField txtLogradouro;
    private TextField txtNumero;
    private TextField txtComplemento;
    private TextField txtCidade;
    private TextField txtEstado;
    private TextField txtCep;
    private TextField txtPais;

    // Botões
    private Button btnBuscar;
    private Button btnIncluir;
    private Button btnAlterar;
    private Button btnExcluir;
    private Button btnLimpar;

    // Estados da tela
    private enum EstadoTela {
        INICIAL, BUSCA_SUCESSO, INCLUSAO_NOVO
    }
    private EstadoTela estadoAtual;

    // Formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    public TelaCRUDSeguradoPessoaFX() {
        this.seguradoPessoaDAO = new SeguradoPessoaDAO();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gerenciamento de Segurados Pessoas");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 30, 30, 30));

        // Configuração das colunas para melhor alinhamento
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(120); // Largura fixa para labels
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(200); // Largura preferencial para campos de texto
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(100); // Largura para botões
        grid.getColumnConstraints().addAll(col1, col2, col3);

        initComponents();
        setupLayout(grid);
        addListeners();
        setupTabOrder();
        setEstado(EstadoTela.INICIAL);

        Scene scene = new Scene(grid, 700, 700); // Tamanho ajustado
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        txtCpf = new TextField();
        txtCpf.setPromptText("Digite o CPF");
        txtCpf.setMaxWidth(150);
        setupCpfMask(txtCpf);

        lblIdUnicoValue = new Label(""); // Inicialmente vazio

        txtNome = new TextField();
        txtNome.setPromptText("Nome Completo");
        txtNome.setMaxWidth(300);

        txtDataNascimento = new TextField();
        txtDataNascimento.setPromptText("DD/MM/AAAA");
        txtDataNascimento.setMaxWidth(100);
        setupDateMask(txtDataNascimento);

        txtBonus = new TextField();
        txtBonus.setPromptText("Ex: 100,00");
        txtBonus.setMaxWidth(120);
        setupCurrencyMask(txtBonus);

        txtRenda = new TextField();
        txtRenda.setPromptText("Ex: 5000,00");
        txtRenda.setMaxWidth(120);
        setupCurrencyMask(txtRenda);

        txtLogradouro = new TextField();
        txtLogradouro.setPromptText("Nome da Rua, Avenida, etc.");
        txtLogradouro.setMaxWidth(300);

        txtNumero = new TextField();
        txtNumero.setPromptText("Número");
        txtNumero.setMaxWidth(80);

        txtComplemento = new TextField();
        txtComplemento.setPromptText("Apto, Bloco, Casa (opcional)");
        txtComplemento.setMaxWidth(200);

        txtCidade = new TextField();
        txtCidade.setPromptText("Cidade");
        txtCidade.setMaxWidth(150);

        txtEstado = new TextField();
        txtEstado.setPromptText("UF (Ex: PE)");
        txtEstado.setMaxWidth(80);

        txtCep = new TextField();
        txtCep.setPromptText("Ex: 12345-678");
        txtCep.setMaxWidth(120);
        setupCepMask(txtCep);

        txtPais = new TextField();
        txtPais.setPromptText("País");
        txtPais.setMaxWidth(150);

        btnBuscar = new Button("Buscar");
        btnBuscar.setPrefWidth(90);
        btnIncluir = new Button("Incluir");
        btnIncluir.setPrefWidth(90);
        btnAlterar = new Button("Alterar");
        btnAlterar.setPrefWidth(90);
        btnExcluir = new Button("Excluir");
        btnExcluir.setPrefWidth(90);
        btnLimpar = new Button("Limpar Tudo");
        btnLimpar.setPrefWidth(100);
    }

    private void setupLayout(GridPane grid) {
        int row = 0;

        // Título Principal
        Label mainTitle = new Label("Cadastro de Segurado Pessoa");
        mainTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");
        GridPane.setHalignment(mainTitle, javafx.geometry.HPos.CENTER);
        grid.add(mainTitle, 0, row, 3, 1);
        row++;

        // Separador visual
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // Seção Segurado Pessoa
        Label lblSeguradoPessoaTitle = new Label("Dados Pessoais");
        lblSeguradoPessoaTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10px 0 5px 0;");
        GridPane.setHalignment(lblSeguradoPessoaTitle, javafx.geometry.HPos.LEFT);
        grid.add(lblSeguradoPessoaTitle, 0, row, 3, 1);
        row++;


        grid.add(new Label("CPF:"), 0, row);
        grid.add(txtCpf, 1, row);
        grid.add(btnBuscar, 2, row);
        row++;

        grid.add(new Label("ID Único:"), 0, row);
        grid.add(lblIdUnicoValue, 1, row); // Exibe o CPF como ID Único
        row++;

        grid.add(new Label("Nome Completo:"), 0, row);
        grid.add(txtNome, 1, row, 2, 1);
        row++;

        grid.add(new Label("Data Nasc.:"), 0, row);
        grid.add(txtDataNascimento, 1, row);
        row++;

        grid.add(new Label("Bônus:"), 0, row);
        grid.add(txtBonus, 1, row);
        row++;

        grid.add(new Label("Renda Mensal:"), 0, row);
        grid.add(txtRenda, 1, row);
        row++;

        // Separador visual
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // Seção Endereço
        Label lblEnderecoTitle = new Label("Dados de Endereço");
        lblEnderecoTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10px 0 5px 0;");
        GridPane.setHalignment(lblEnderecoTitle, javafx.geometry.HPos.LEFT);
        grid.add(lblEnderecoTitle, 0, row, 3, 1);
        row++;

        grid.add(new Label("Logradouro:"), 0, row);
        grid.add(txtLogradouro, 1, row, 2, 1);
        row++;

        grid.add(new Label("Número:"), 0, row);
        grid.add(txtNumero, 1, row);
        row++;

        grid.add(new Label("Complemento:"), 0, row);
        grid.add(txtComplemento, 1, row, 2, 1);
        row++;

        grid.add(new Label("CEP:"), 0, row);
        grid.add(txtCep, 1, row);
        row++;

        grid.add(new Label("Cidade:"), 0, row);
        grid.add(txtCidade, 1, row);
        row++;

        grid.add(new Label("Estado (UF):"), 0, row);
        grid.add(txtEstado, 1, row);
        row++;

        grid.add(new Label("País:"), 0, row);
        grid.add(txtPais, 1, row);
        row++;

        // Separador visual
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // Botões de Ação
        HBox hbCrudButtons = new HBox(10);
        hbCrudButtons.setAlignment(Pos.BOTTOM_RIGHT);
        hbCrudButtons.getChildren().addAll(btnIncluir, btnAlterar, btnExcluir);
        grid.add(hbCrudButtons, 1, row, 2, 1);
        row++;

        HBox hbClearButton = new HBox(10);
        hbClearButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbClearButton.getChildren().add(btnLimpar);
        grid.add(hbClearButton, 1, row, 2, 1);
    }

    private void addListeners() {
        btnBuscar.setOnAction(e -> buscarSegurado());
        btnIncluir.setOnAction(e -> incluirSegurado());
        btnAlterar.setOnAction(e -> alterarSegurado());
        btnExcluir.setOnAction(e -> excluirSegurado());
        btnLimpar.setOnAction(e -> limparCampos());
    }

    private void setupTabOrder() {
        txtCpf.setFocusTraversable(true);
        btnBuscar.setFocusTraversable(true);
        txtNome.setFocusTraversable(true);
        txtDataNascimento.setFocusTraversable(true);
        txtBonus.setFocusTraversable(true);
        txtRenda.setFocusTraversable(true);
        txtLogradouro.setFocusTraversable(true);
        txtNumero.setFocusTraversable(true);
        txtComplemento.setFocusTraversable(true);
        txtCep.setFocusTraversable(true);
        txtCidade.setFocusTraversable(true);
        txtEstado.setFocusTraversable(true);
        txtPais.setFocusTraversable(true);
        btnIncluir.setFocusTraversable(true);
        btnAlterar.setFocusTraversable(true);
        btnExcluir.setFocusTraversable(true);
        btnLimpar.setFocusTraversable(true);
    }

    // --- Controle de Estados da Tela ---
    private void setEstado(EstadoTela estado) {
        this.estadoAtual = estado;
        boolean cpfEditavel = false;
        boolean camposEditaveis = false;
        boolean btnBuscarHabilitado = false;
        boolean btnIncluirHabilitado = false;
        boolean btnAlterarHabilitado = false;
        boolean btnExcluirHabilitado = false;
        boolean btnLimparHabilitado = true;

        switch (estado) {
            case INICIAL:
                cpfEditavel = true;
                btnBuscarHabilitado = true;
                lblIdUnicoValue.setText(""); // Limpa o ID Único
                break;
            case BUSCA_SUCESSO:
                cpfEditavel = false;
                camposEditaveis = true;
                btnAlterarHabilitado = true;
                btnExcluirHabilitado = true;
                lblIdUnicoValue.setText(txtCpf.getText()); // Exibe o CPF como ID Único
                break;
            case INCLUSAO_NOVO:
                cpfEditavel = false;
                camposEditaveis = true;
                btnIncluirHabilitado = true;
                lblIdUnicoValue.setText("Novo"); // Indica que é um novo registro
                break;
        }

        txtCpf.setEditable(cpfEditavel);
        txtNome.setEditable(camposEditaveis);
        txtDataNascimento.setEditable(camposEditaveis);
        txtBonus.setEditable(camposEditaveis);
        txtRenda.setEditable(camposEditaveis);
        txtLogradouro.setEditable(camposEditaveis);
        txtNumero.setEditable(camposEditaveis);
        txtComplemento.setEditable(camposEditaveis);
        txtCidade.setEditable(camposEditaveis);
        txtEstado.setEditable(camposEditaveis);
        txtCep.setEditable(camposEditaveis);
        txtPais.setEditable(camposEditaveis);

        btnBuscar.setDisable(!btnBuscarHabilitado);
        btnIncluir.setDisable(!btnIncluirHabilitado);
        btnAlterar.setDisable(!btnAlterarHabilitado);
        btnExcluir.setDisable(!btnExcluirHabilitado);
        btnLimpar.setDisable(!btnLimparHabilitado);
    }

    // --- Métodos de Validação de Formato e Máscaras ---

    private void setupCpfMask(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        UnaryOperator<Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                if (c.getControlNewText().length() > 11) return null;
                return c;
            } else {
                return null;
            }
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    private void setupDateMask(TextField textField) {
        final String format = "dd/MM/yyyy";
        Pattern pattern = Pattern.compile("[0-9/]*");
        UnaryOperator<Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                if (c.getControlNewText().length() > 10) return null;
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
                        LocalDate.parse(text, DATE_FORMATTER);
                        textField.setStyle("");
                    } catch (DateTimeParseException e) {
                        textField.setStyle("-fx-border-color: red;");
                        showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Data inválida. Use o formato " + format);
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
                        new BigDecimal(cleanText);
                        textField.setText(DECIMAL_FORMAT.format(new BigDecimal(cleanText)));
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

    private void setupCepMask(TextField textField) {
        textField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            String cleanedText = newText.replaceAll("\\D", "");

            if (cleanedText.length() > 8) {
                return null;
            }

            StringBuilder formattedText = new StringBuilder();
            for (int i = 0; i < cleanedText.length(); i++) {
                formattedText.append(cleanedText.charAt(i));
                if (i == 4 && cleanedText.length() > 5) {
                    formattedText.append("-");
                }
            }
            change.setText(formattedText.toString());
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(formattedText.length());
            change.setAnchor(formattedText.length());

            return change;
        }));

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String rawCep = textField.getText().trim();
                String cleanCep = rawCep.replaceAll("\\D", "");
                if (!cleanCep.isEmpty()) {
                    if (cleanCep.length() == 8) {
                        textField.setText(formatCep(cleanCep));
                        textField.setStyle("");
                    } else {
                        textField.setStyle("-fx-border-color: red;");
                        showAlert(Alert.AlertType.ERROR, "Erro de Validação", "CEP deve ter 8 dígitos.");
                    }
                } else {
                    textField.setStyle("");
                }
            }
        });
    }

    private String formatCep(String cep) {
        if (cep == null || cep.length() != 8) {
            return cep;
        }
        return cep.substring(0, 5) + "-" + cep.substring(5, 8);
    }

    private String cleanCep(String cep) {
        return cep != null ? cep.replaceAll("\\D", "") : null;
    }


    // --- Operações CRUD ---

    private void buscarSegurado() {
        String cpf = txtCpf.getText().trim();

        if (StringUtils.ehNuloOuBranco(cpf)) {
            showAlert(Alert.AlertType.WARNING, "Busca", "O campo CPF é obrigatório para realizar a busca.");
            setEstado(EstadoTela.INICIAL);
            return;
        }
        if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Busca", "CPF inválido. Verifique o número digitado.");
            setEstado(EstadoTela.INICIAL);
            return;
        }

        SeguradoPessoa segurado = seguradoPessoaDAO.buscar(cpf);

        if (segurado != null) {
            preencherCampos(segurado);
            setEstado(EstadoTela.BUSCA_SUCESSO);
            showAlert(Alert.AlertType.INFORMATION, "Busca Concluída", "Segurado Pessoa encontrado com sucesso!");
        } else {
            limparCamposComCPF();
            setEstado(EstadoTela.INCLUSAO_NOVO);
            showAlert(Alert.AlertType.INFORMATION, "Segurado Não Encontrado", "Nenhum segurado pessoa com este CPF foi encontrado. Você pode incluir um novo registro.");
            txtNome.requestFocus();
        }
    }

    private void incluirSegurado() {
        String cpf = txtCpf.getText().trim();
        if (validarCamposComuns() && validarCamposEndereco() && validarCpfCnpjParaInclusao(cpf)) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataNascimento = LocalDate.parse(txtDataNascimento.getText().trim(), DATE_FORMATTER);
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double renda = Double.parseDouble(txtRenda.getText().trim().replace(".", "").replace(",", "."));

                SeguradoPessoa novoSegurado = new SeguradoPessoa(
                        txtNome.getText().trim(),
                        endereco,
                        dataNascimento,
                        bonus,
                        cpf,
                        renda
                );

                if (seguradoPessoaDAO.incluir(novoSegurado)) {
                    showAlert(Alert.AlertType.INFORMATION, "Inclusão Realizada", "Segurado Pessoa incluído com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Falha na Inclusão", "Erro ao incluir segurado Pessoa. Verifique se o CPF já existe ou tente novamente.");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Nascimento, Bônus e Renda. Assegure que os valores numéricos usem vírgula para centavos.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void alterarSegurado() {
        String cpf = txtCpf.getText().trim();
        if (validarCamposComuns() && validarCamposEndereco()) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataNascimento = LocalDate.parse(txtDataNascimento.getText().trim(), DATE_FORMATTER);
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double renda = Double.parseDouble(txtRenda.getText().trim().replace(".", "").replace(",", "."));

                SeguradoPessoa seguradoAlterado = new SeguradoPessoa(
                        txtNome.getText().trim(),
                        endereco,
                        dataNascimento,
                        bonus,
                        cpf,
                        renda
                );

                if (seguradoPessoaDAO.alterar(seguradoAlterado)) {
                    showAlert(Alert.AlertType.INFORMATION, "Alteração Realizada", "Segurado Pessoa alterado com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Falha na Alteração", "Erro ao alterar segurado Pessoa. O CPF não foi encontrado ou ocorreu um erro de persistência.");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Nascimento, Bônus e Renda. Assegure que os valores numéricos usem vírgula para centavos.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void excluirSegurado() {
        String cpf = txtCpf.getText().trim();
        if (StringUtils.ehNuloOuBranco(cpf) || !ValidadorCpfCnpj.ehCpfValido(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Exclusão Inválida", "CPF inválido ou não informado para exclusão.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar Exclusão", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Tem certeza que deseja excluir o segurado com CPF: " + cpf + "?");
        alert.setContentText("Esta ação não poderá ser desfeita.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (seguradoPessoaDAO.excluir(cpf)) {
                showAlert(Alert.AlertType.INFORMATION, "Exclusão Concluída", "Segurado Pessoa excluído com sucesso!");
                limparCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Falha na Exclusão", "Erro ao excluir segurado Pessoa. O CPF pode não ter sido encontrado.");
            }
        }
    }

    private void limparCampos() {
        txtCpf.clear();
        txtNome.clear();
        txtDataNascimento.clear();
        txtBonus.clear();
        txtRenda.clear();
        txtLogradouro.clear();
        txtNumero.clear();
        txtComplemento.clear();
        txtCidade.clear();
        txtEstado.clear();
        txtCep.clear();
        txtPais.clear();
        setEstado(EstadoTela.INICIAL);
        txtCpf.requestFocus();
    }

    private void limparCamposComCPF() {
        txtNome.clear();
        txtDataNascimento.clear();
        txtBonus.clear();
        txtRenda.clear();
        txtLogradouro.clear();
        txtNumero.clear();
        txtComplemento.clear();
        txtCidade.clear();
        txtEstado.clear();
        txtCep.clear();
        txtPais.clear();
    }

    private void preencherCampos(SeguradoPessoa segurado) {
        txtCpf.setText(segurado.getCpf());
        txtNome.setText(segurado.getNome());
        txtDataNascimento.setText(segurado.getDataNascimento().format(DATE_FORMATTER));
        txtBonus.setText(DECIMAL_FORMAT.format(segurado.getBonus()));
        txtRenda.setText(DECIMAL_FORMAT.format(segurado.getRenda()));

        Endereco endereco = segurado.getEndereco();
        if (endereco != null) {
            txtLogradouro.setText(endereco.getLogradouro());
            txtNumero.setText(endereco.getNumero());
            txtComplemento.setText(endereco.getComplemento());
            txtCidade.setText(endereco.getCidade());
            txtEstado.setText(endereco.getEstado());
            txtCep.setText(formatCep(endereco.getCep()));
            txtPais.setText(endereco.getPais());
        } else {
            txtLogradouro.clear();
            txtNumero.clear();
            txtComplemento.clear();
            txtCidade.clear();
            txtEstado.clear();
            txtCep.clear();
            txtPais.clear();
        }
    }

    private Endereco criarObjetoEndereco() {
        return new Endereco(
                txtLogradouro.getText().trim(),
                cleanCep(txtCep.getText()),
                txtNumero.getText().trim(),
                txtComplemento.getText().trim(),
                txtPais.getText().trim(),
                txtEstado.getText().trim(),
                txtCidade.getText().trim()
        );
    }

    // --- Validações de Campos Comuns ---
    private boolean validarCamposComuns() {
        if (StringUtils.ehNuloOuBranco(txtNome.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Nome Completo' é obrigatório.");
            return false;
        }
        if (txtDataNascimento.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Data de Nascimento inválida. Verifique o formato.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtDataNascimento.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Data de Nascimento' é obrigatório.");
            return false;
        }
        if (txtBonus.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Bônus inválido. Verifique o formato numérico.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtBonus.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Bônus' é obrigatório.");
            return false;
        }
        try {
            BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
            if (bonus.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O valor do Bônus não pode ser negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Formato de bônus inválido. Use apenas números.");
            return false;
        }

        if (txtRenda.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Renda inválida. Verifique o formato numérico.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtRenda.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Renda' é obrigatório.");
            return false;
        }
        try {
            double renda = Double.parseDouble(txtRenda.getText().trim().replace(".", "").replace(",", "."));
            if (renda <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validação Necessária", "A Renda deve ser um valor maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Formato de renda inválido. Use apenas números.");
            return false;
        }
        return true;
    }

    private boolean validarCamposEndereco() {
        if (StringUtils.ehNuloOuBranco(txtLogradouro.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Logradouro' é obrigatório.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtNumero.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Número' é obrigatório.");
            return false;
        }
        if (txtCep.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "CEP inválido. Verifique o formato.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtCep.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'CEP' é obrigatório.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtCidade.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Cidade' é obrigatório.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtEstado.getText()) || txtEstado.getText().trim().length() != 2) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Estado (UF)' é obrigatório e deve ter 2 letras.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtPais.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'País' é obrigatório.");
            return false;
        }
        return true;
    }

    private boolean validarCpfCnpjParaInclusao(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'CPF' é obrigatório para inclusão.");
            return false;
        }
        if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "CPF inválido. Não é possível incluir com um CPF inválido.");
            return false;
        }
        return true;
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

            // Adicionar Segurado Pessoa para teste (CPF válido e com todos os dados)
            Endereco endTeste = new Endereco("Rua da Programação", "50000-000", "123", "Casa A", "Brasil", "PE", "Recife");
            SeguradoPessoa pessoaTeste = new SeguradoPessoa(
                    "Fulano de Tal",
                    endTeste,
                    LocalDate.of(1990, 1, 1),
                    new BigDecimal("250.50"),
                    "11122233344",
                    5000.00
            );
            if (segPesDAO.buscar(pessoaTeste.getIdUnico()) == null) {
                if (segPesDAO.incluir(pessoaTeste)) {
                    System.out.println("Segurado Pessoa 11122233344 incluído para teste.");
                } else {
                    System.out.println("Erro ao incluir Segurado Pessoa 11122233344.");
                }
            } else {
                System.out.println("Segurado Pessoa 11122233344 já existe.");
            }

            // Exemplo de outro segurado para teste
            Endereco endTeste2 = new Endereco("Av. das Codificações", "60000-000", "456", "Apto 202", "Brasil", "SP", "São Paulo");
            SeguradoPessoa pessoaTeste2 = new SeguradoPessoa(
                    "Ciclano da Silva",
                    endTeste2,
                    LocalDate.of(1985, 7, 15),
                    new BigDecimal("100.00"),
                    "55566677788",
                    3000.00
            );
            if (segPesDAO.buscar(pessoaTeste2.getIdUnico()) == null) {
                if (segPesDAO.incluir(pessoaTeste2)) {
                    System.out.println("Segurado Pessoa 55566677788 incluído para teste.");
                } else {
                    System.out.println("Erro ao incluir Segurado Pessoa 55566677788.");
                }
            } else {
                System.out.println("Segurado Pessoa 55566677788 já existe.");
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