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
        primaryStage.setTitle("CRUD de Segurado Pessoa");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        initComponents();
        setupLayout(grid);
        addListeners();
        setupTabOrder();
        setEstado(EstadoTela.INICIAL);

        Scene scene = new Scene(grid, 600, 650); // Tamanho ajustado para acomodar o novo campo
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        txtCpf = new TextField();
        txtCpf.setPromptText("Ex: 12345678909");
        txtCpf.setMaxWidth(150);
        setupCpfMask(txtCpf);

        txtNome = new TextField();
        txtNome.setPromptText("Nome Completo");
        txtNome.setMaxWidth(250);

        txtDataNascimento = new TextField(); // Nome do campo ajustado
        txtDataNascimento.setPromptText("dd/MM/yyyy");
        txtDataNascimento.setMaxWidth(100);
        setupDateMask(txtDataNascimento);

        txtBonus = new TextField();
        txtBonus.setPromptText("Ex: 100.00");
        txtBonus.setMaxWidth(100);
        setupCurrencyMask(txtBonus);

        txtRenda = new TextField(); // Inicialização do novo campo
        txtRenda.setPromptText("Ex: 5000.00");
        txtRenda.setMaxWidth(100);
        setupCurrencyMask(txtRenda); // Reutiliza máscara de moeda, ou pode criar uma para double

        // Campos de Endereco (inalterados)
        txtLogradouro = new TextField();
        txtLogradouro.setPromptText("Rua, Av., Alameda...");
        txtLogradouro.setMaxWidth(250);

        txtNumero = new TextField();
        txtNumero.setPromptText("Número");
        txtNumero.setMaxWidth(80);

        txtComplemento = new TextField();
        txtComplemento.setPromptText("Apto, Bloco, Casa");
        txtComplemento.setMaxWidth(150);



        txtCidade = new TextField();
        txtCidade.setPromptText("Cidade");
        txtCidade.setMaxWidth(150);

        txtEstado = new TextField();
        txtEstado.setPromptText("UF (Ex: PE)");
        txtEstado.setMaxWidth(80);

        txtCep = new TextField();
        txtCep.setPromptText("Ex: 12345-678");
        txtCep.setMaxWidth(100);
        setupCepMask(txtCep);

        txtPais = new TextField();
        txtPais.setPromptText("País");
        txtPais.setMaxWidth(150);

        btnBuscar = new Button("Buscar");
        btnIncluir = new Button("Incluir");
        btnAlterar = new Button("Alterar");
        btnExcluir = new Button("Excluir");
        btnLimpar = new Button("Limpar");
    }

    private void setupLayout(GridPane grid) {
        int row = 0;
        grid.add(new Label("CPF:"), 0, row);
        grid.add(txtCpf, 1, row);
        grid.add(btnBuscar, 2, row);
        row++;

        grid.add(new Label("Nome:"), 0, row);
        grid.add(txtNome, 1, row, 2, 1);
        row++;

        grid.add(new Label("Data Nasc.:"), 0, row); // Label ajustado
        grid.add(txtDataNascimento, 1, row);
        row++;

        grid.add(new Label("Bônus:"), 0, row);
        grid.add(txtBonus, 1, row);
        row++;

        grid.add(new Label("Renda:"), 0, row); // Layout do novo campo
        grid.add(txtRenda, 1, row);
        row++;

        Label lblEndereco = new Label("Endereço:");
        lblEndereco.setStyle("-fx-font-weight: bold;");
        grid.add(lblEndereco, 0, row, 3, 1);
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



        grid.add(new Label("Cidade:"), 0, row);
        grid.add(txtCidade, 1, row);
        row++;

        grid.add(new Label("Estado (UF):"), 0, row);
        grid.add(txtEstado, 1, row);
        row++;

        grid.add(new Label("CEP:"), 0, row);
        grid.add(txtCep, 1, row);
        row++;

        grid.add(new Label("País:"), 0, row);
        grid.add(txtPais, 1, row);
        row++;

        HBox hbButtons = new HBox(10);
        hbButtons.setAlignment(Pos.BOTTOM_RIGHT);
        hbButtons.getChildren().addAll(btnIncluir, btnAlterar, btnExcluir, btnLimpar);
        grid.add(hbButtons, 1, row, 2, 1);
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
        txtNome.setFocusTraversable(true);
        txtDataNascimento.setFocusTraversable(true); // Ajustado
        txtBonus.setFocusTraversable(true);
        txtRenda.setFocusTraversable(true); // NOVO CAMPO na ordem de tabulação
        txtLogradouro.setFocusTraversable(true);
        txtNumero.setFocusTraversable(true);
        txtComplemento.setFocusTraversable(true);

        txtCidade.setFocusTraversable(true);
        txtEstado.setFocusTraversable(true);
        txtCep.setFocusTraversable(true);
        txtPais.setFocusTraversable(true);
        btnBuscar.setFocusTraversable(true);
        btnIncluir.setFocusTraversable(true);
        btnAlterar.setFocusTraversable(true);
        btnExcluir.setFocusTraversable(true);
        btnLimpar.setFocusTraversable(true);
    }

    // --- Controle de Estados da Tela ---
    private void setEstado(EstadoTela estado) {
        this.estadoAtual = estado;
        boolean cpfEditavel = false;
        boolean camposEditaveis = false; // Todos os campos de Nome, Data, Bônus, Endereço, Renda
        boolean btnBuscarHabilitado = false;
        boolean btnIncluirHabilitado = false;
        boolean btnAlterarHabilitado = false;
        boolean btnExcluirHabilitado = false;
        boolean btnLimparHabilitado = true;

        switch (estado) {
            case INICIAL:
                cpfEditavel = true;
                btnBuscarHabilitado = true;
                break;
            case BUSCA_SUCESSO:
                cpfEditavel = false;
                camposEditaveis = true;
                btnAlterarHabilitado = true;
                btnExcluirHabilitado = true;
                break;
            case INCLUSAO_NOVO:
                cpfEditavel = false;
                camposEditaveis = true;
                btnIncluirHabilitado = true;
                break;
        }

        txtCpf.setEditable(cpfEditavel);
        txtNome.setEditable(camposEditaveis);
        txtDataNascimento.setEditable(camposEditaveis); // Ajustado
        txtBonus.setEditable(camposEditaveis);
        txtRenda.setEditable(camposEditaveis); // NOVO CAMPO: editabilidade
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

    // --- Métodos de Validação de Formato e Máscaras --- (inalterados, apenas usando o novo campo)

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


    // --- Operações CRUD ---

    private void buscarSegurado() {
        String cpf = txtCpf.getText().trim();

        if (StringUtils.ehNuloOuBranco(cpf)) {
            showAlert(Alert.AlertType.WARNING, "Busca", "CPF deve ser informado para a busca.");
            setEstado(EstadoTela.INICIAL);
            return;
        }
        if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Busca", "CPF inválido, deve ter no minimo 11 digitos.");
            setEstado(EstadoTela.INICIAL);
            return;
        }

        SeguradoPessoa segurado = seguradoPessoaDAO.buscar(cpf);

        if (segurado != null) {
            preencherCampos(segurado);
            setEstado(EstadoTela.BUSCA_SUCESSO);
            showAlert(Alert.AlertType.INFORMATION, "Busca", "Segurado Pessoa encontrado!");
        } else {
            limparCamposComCPF();
            setEstado(EstadoTela.INCLUSAO_NOVO);
            showAlert(Alert.AlertType.INFORMATION, "Busca", "Segurado Pessoa não encontrado. Você pode incluí-lo.");
            txtNome.requestFocus();
        }
    }

    private void incluirSegurado() {
        String cpf = txtCpf.getText().trim();
        // Validar todos os campos antes de incluir
        if (validarCamposComuns() && validarCamposEndereco() && validarCpfCnpjParaInclusao(cpf)) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataNascimento = LocalDate.parse(txtDataNascimento.getText().trim(), DATE_FORMATTER); // Ajustado para dataNascimento
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double renda = Double.parseDouble(txtRenda.getText().trim().replace(".", "").replace(",", ".")); // Lendo a renda como double

                // AQUI ESTÁ A CORREÇÃO NA ORDEM E PARÂMETROS DO CONSTRUTOR:
                SeguradoPessoa novoSegurado = new SeguradoPessoa(
                        txtNome.getText().trim(),
                        endereco,
                        dataNascimento,
                        bonus,
                        cpf,
                        renda
                );

                if (seguradoPessoaDAO.incluir(novoSegurado)) {
                    showAlert(Alert.AlertType.INFORMATION, "Inclusão", "Segurado Pessoa incluído com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Inclusão", "Erro ao incluir segurado Pessoa (CPF já existe ou erro de persistência).");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Nascimento, Bônus e Renda."); // Mensagem ajustada
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void alterarSegurado() {
        String cpf = txtCpf.getText().trim();
        if (validarCamposComuns() && validarCamposEndereco()) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataNascimento = LocalDate.parse(txtDataNascimento.getText().trim(), DATE_FORMATTER); // Ajustado para dataNascimento
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double renda = Double.parseDouble(txtRenda.getText().trim().replace(".", "").replace(",", ".")); // Lendo a renda como double

                // AQUI ESTÁ A CORREÇÃO NA ORDEM E PARÂMETROS DO CONSTRUTOR:
                SeguradoPessoa seguradoAlterado = new SeguradoPessoa(
                        txtNome.getText().trim(),
                        endereco,
                        dataNascimento,
                        bonus,
                        cpf,
                        renda
                );

                if (seguradoPessoaDAO.alterar(seguradoAlterado)) {
                    showAlert(Alert.AlertType.INFORMATION, "Alteração", "Segurado Pessoa alterado com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Alteração", "Erro ao alterar segurado Pessoa (CPF não encontrado ou erro de persistência).");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Nascimento, Bônus e Renda."); // Mensagem ajustada
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void excluirSegurado() {
        String cpf = txtCpf.getText().trim();
        if (StringUtils.ehNuloOuBranco(cpf) || !ValidadorCpfCnpj.ehCpfValido(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Exclusão", "CPF inválido para exclusão.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar Exclusão",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Tem certeza que deseja excluir o segurado " + cpf + "?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (seguradoPessoaDAO.excluir(cpf)) {
                showAlert(Alert.AlertType.INFORMATION, "Exclusão", "Segurado Pessoa excluído com sucesso!");
                limparCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Exclusão", "Erro ao excluir segurado Pessoa (CPF não encontrado ou erro de persistência).");
            }
        }
    }

    private void limparCampos() {
        txtCpf.clear();
        txtNome.clear();
        txtDataNascimento.clear(); // Ajustado
        txtBonus.clear();
        txtRenda.clear(); // Limpeza do novo campo
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
        txtDataNascimento.clear(); // Ajustado
        txtBonus.clear();
        txtRenda.clear(); // Limpeza do novo campo
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
        txtDataNascimento.setText(segurado.getDataNascimento().format(DATE_FORMATTER)); // Ajustado para getDataNascimento
        txtBonus.setText(DECIMAL_FORMAT.format(segurado.getBonus()));
        txtRenda.setText(DECIMAL_FORMAT.format(segurado.getRenda())); // Preenchimento do novo campo

        Endereco endereco = segurado.getEndereco();
        if (endereco != null) {
            txtLogradouro.setText(endereco.getLogradouro());
            txtNumero.setText(endereco.getNumero());
            txtComplemento.setText(endereco.getComplemento());

            txtCidade.setText(endereco.getCidade());
            txtEstado.setText(endereco.getEstado());
            txtCep.setText(endereco.getCep());
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
                txtCep.getText().trim().replace("-", ""),
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
            showAlert(Alert.AlertType.ERROR, "Validação", "Nome deve ser informado.");
            return false;
        }
        if (txtDataNascimento.getStyle().contains("red")) { // Checa se a máscara já marcou como erro
            showAlert(Alert.AlertType.ERROR, "Validação", "Data de Nascimento inválida."); // Mensagem ajustada
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtDataNascimento.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Data de Nascimento deve ser informada."); // Mensagem ajustada
            return false;
        }
        if (txtBonus.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Bônus inválido.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtBonus.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Bônus deve ser informado.");
            return false;
        }
        try {
            BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
            if (bonus.compareTo(BigDecimal.ZERO) < 0) {
                showAlert(Alert.AlertType.ERROR, "Validação", "Bônus não pode ser negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Formato de bônus inválido.");
            return false;
        }

        // Validação da Renda
        if (txtRenda.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Renda inválida.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtRenda.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Renda deve ser informada.");
            return false;
        }
        try {
            double renda = Double.parseDouble(txtRenda.getText().trim().replace(".", "").replace(",", "."));
            if (renda <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validação", "Renda deve ser maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Formato de renda inválido.");
            return false;
        }
        return true;
    }

    private boolean validarCamposEndereco() {
        if (StringUtils.ehNuloOuBranco(txtLogradouro.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Logradouro deve ser informado.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtNumero.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Número deve ser informado.");
            return false;
        }
        if (txtCep.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação", "CEP inválido.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtCep.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "CEP deve ser informado.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtCidade.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Cidade deve ser informada.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtEstado.getText()) || txtEstado.getText().trim().length() != 2) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Estado deve ser informado com 2 letras (UF).");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtPais.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "País deve ser informado.");
            return false;
        }
        return true;
    }

    private boolean validarCpfCnpjParaInclusao(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Validação", "CPF deve ser informado.");
            return false;
        }
        if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
            showAlert(Alert.AlertType.ERROR, "Validação", "CPF inválido.");
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
            // CONSTRUTOR CORRIGIDO: nome, endereco, dataNascimento, bonus, cpf, renda
            SeguradoPessoa pessoaTeste = new SeguradoPessoa(
                    "Fulano de Tal",
                    endTeste,
                    LocalDate.of(1990, 1, 1), // Data de Nascimento
                    new BigDecimal("250.50"),
                    "11122233344", // CPF
                    5000.00 // Renda
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
                    LocalDate.of(1985, 7, 15), // Data de Nascimento
                    new BigDecimal("100.00"),
                    "55566677788", // CPF
                    3000.00 // Renda
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
