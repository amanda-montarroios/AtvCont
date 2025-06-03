package br.edu.cs.poo.ac.seguro.testes;


import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
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

public class TelaCRUDSeguradoEmpresaFX extends Application {

    private SeguradoEmpresaDAO seguradoEmpresaDAO;

    // Campos de Segurado Empresa
    private TextField txtCnpj;
    private TextField txtNome;
    private TextField txtDataAbertura;
    private TextField txtBonus;
    private TextField txtFaturamento;
    private CheckBox chkEhLocadoraDeVeiculos;

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

    public TelaCRUDSeguradoEmpresaFX() {
        this.seguradoEmpresaDAO = new SeguradoEmpresaDAO();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CRUD de Segurado Empresa");

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

        Scene scene = new Scene(grid, 600, 680); // Tamanho ajustado para acomodar o novo campo
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        txtCnpj = new TextField();
        txtCnpj.setPromptText("Ex: 11222333000144");
        txtCnpj.setMaxWidth(180);
        setupCnpjMask(txtCnpj);

        txtNome = new TextField();
        txtNome.setPromptText("Razão Social ou Nome Fantasia");
        txtNome.setMaxWidth(250);

        txtDataAbertura = new TextField(); // Campo de Data Abertura
        txtDataAbertura.setPromptText("dd/MM/yyyy");
        txtDataAbertura.setMaxWidth(100);
        setupDateMask(txtDataAbertura);

        txtBonus = new TextField();
        txtBonus.setPromptText("Ex: 100.00");
        txtBonus.setMaxWidth(100);
        setupCurrencyMask(txtBonus);

        txtFaturamento = new TextField(); // NOVO CAMPO: Faturamento
        txtFaturamento.setPromptText("Ex: 50000.00");
        txtFaturamento.setMaxWidth(100);
        setupCurrencyMask(txtFaturamento);

        chkEhLocadoraDeVeiculos = new CheckBox("É Locadora de Veículos");

        // Campos de Endereco (inalterados)
        txtLogradouro = new TextField();
        txtLogradouro.setPromptText("Rua, Av., Alameda...");
        txtLogradouro.setMaxWidth(250);

        txtNumero = new TextField();
        txtNumero.setPromptText("Número");
        txtNumero.setMaxWidth(80);

        txtComplemento = new TextField();
        txtComplemento.setPromptText("Apto, Bloco, Sala");
        txtComplemento.setMaxWidth(150);



        txtCidade = new TextField();
        txtCidade.setPromptText("Cidade");
        txtCidade.setMaxWidth(150);

        txtEstado = new TextField();
        txtEstado.setPromptText("UF (Ex: SP)");
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
        grid.add(new Label("CNPJ:"), 0, row);
        grid.add(txtCnpj, 1, row);
        grid.add(btnBuscar, 2, row);
        row++;

        grid.add(new Label("Nome/Razão Social:"), 0, row);
        grid.add(txtNome, 1, row, 2, 1);
        row++;

        grid.add(new Label("Data Abertura:"), 0, row); // Label ajustado
        grid.add(txtDataAbertura, 1, row);
        row++;

        grid.add(new Label("Bônus:"), 0, row);
        grid.add(txtBonus, 1, row);
        row++;

        grid.add(new Label("Faturamento:"), 0, row); // Layout do novo campo
        grid.add(txtFaturamento, 1, row);
        row++;

        grid.add(chkEhLocadoraDeVeiculos, 1, row, 2, 1);
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
        txtCnpj.setFocusTraversable(true);
        txtNome.setFocusTraversable(true);
        txtDataAbertura.setFocusTraversable(true); // Ajustado
        txtBonus.setFocusTraversable(true);
        txtFaturamento.setFocusTraversable(true); // NOVO CAMPO: tab order
        chkEhLocadoraDeVeiculos.setFocusTraversable(true);
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
        boolean cnpjEditavel = false;
        boolean camposEditaveis = false;
        boolean btnBuscarHabilitado = false;
        boolean btnIncluirHabilitado = false;
        boolean btnAlterarHabilitado = false;
        boolean btnExcluirHabilitado = false;
        boolean btnLimparHabilitado = true;

        switch (estado) {
            case INICIAL:
                cnpjEditavel = true;
                btnBuscarHabilitado = true;
                break;
            case BUSCA_SUCESSO:
                cnpjEditavel = false;
                camposEditaveis = true;
                btnAlterarHabilitado = true;
                btnExcluirHabilitado = true;
                break;
            case INCLUSAO_NOVO:
                cnpjEditavel = false;
                camposEditaveis = true;
                btnIncluirHabilitado = true;
                break;
        }

        txtCnpj.setEditable(cnpjEditavel);
        txtNome.setEditable(camposEditaveis);
        txtDataAbertura.setEditable(camposEditaveis); // Ajustado
        txtBonus.setEditable(camposEditaveis);
        txtFaturamento.setEditable(camposEditaveis); // NOVO CAMPO: editabilidade
        chkEhLocadoraDeVeiculos.setDisable(!camposEditaveis);
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

    // --- Métodos de Validação de Formato e Máscaras (Reutilizados e Adaptados) ---

    private void setupCnpjMask(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        UnaryOperator<Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                if (c.getControlNewText().length() > 14) return null;
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

    // --- Máscara de CEP corrigida para o formato 99999-999 (ao digitar) ---
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

    // Método auxiliar para formatar CEP (para exibição final)
    private String formatCep(String cep) {
        if (cep == null || cep.length() != 8) {
            return cep;
        }
        return cep.substring(0, 5) + "-" + cep.substring(5, 8);
    }

    // Método auxiliar para limpar CEP (remover formatação para lógica de negócio/DAO)
    private String cleanCep(String cep) {
        return cep != null ? cep.replaceAll("\\D", "") : null;
    }




    // --- Operações CRUD ---

    private void buscarSegurado() {
        String cnpj = txtCnpj.getText().trim();

        if (StringUtils.ehNuloOuBranco(cnpj)) {
            showAlert(Alert.AlertType.WARNING, "Busca", "CNPJ deve ser informado para a busca.");
            setEstado(EstadoTela.INICIAL);
            return;
        }
        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Busca", "CNPJ inválido.");
            setEstado(EstadoTela.INICIAL);
            return;
        }

        SeguradoEmpresa segurado = seguradoEmpresaDAO.buscar(cnpj);

        if (segurado != null) {
            preencherCampos(segurado);
            setEstado(EstadoTela.BUSCA_SUCESSO);
            showAlert(Alert.AlertType.INFORMATION, "Busca", "Segurado Empresa encontrado!");
        } else {
            limparCamposComCNPJ();
            setEstado(EstadoTela.INCLUSAO_NOVO);
            showAlert(Alert.AlertType.INFORMATION, "Busca", "Segurado Empresa não encontrado. Você pode incluí-lo.");
            txtNome.requestFocus();
        }
    }

    private void incluirSegurado() {
        String cnpj = txtCnpj.getText().trim();
        if (validarCamposComuns() && validarCamposEndereco() && validarCnpjParaInclusao(cnpj)) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataAbertura = LocalDate.parse(txtDataAbertura.getText().trim(), DATE_FORMATTER); // Ajustado para dataAbertura
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double faturamento = Double.parseDouble(txtFaturamento.getText().trim().replace(".", "").replace(",", ".")); // Lendo o faturamento como double
                boolean ehLocadora = chkEhLocadoraDeVeiculos.isSelected();

                // Construtor SeguradoEmpresa: (nome, endereco, dataAbertura, bonus, cnpj, faturamento, ehLocadoraDeVeiculos)
                SeguradoEmpresa novaEmpresa = new SeguradoEmpresa(
                        txtNome.getText().trim(),
                        endereco,
                        dataAbertura,
                        bonus,
                        cnpj,
                        faturamento,
                        ehLocadora
                );

                if (seguradoEmpresaDAO.incluir(novaEmpresa)) {
                    showAlert(Alert.AlertType.INFORMATION, "Inclusão", "Segurado Empresa incluído com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Inclusão", "Erro ao incluir segurado Empresa (CNPJ já existe ou erro de persistência).");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Abertura, Bônus e Faturamento."); // Mensagem ajustada
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void alterarSegurado() {
        String cnpj = txtCnpj.getText().trim();
        if (validarCamposComuns() && validarCamposEndereco()) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataAbertura = LocalDate.parse(txtDataAbertura.getText().trim(), DATE_FORMATTER); // Ajustado para dataAbertura
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double faturamento = Double.parseDouble(txtFaturamento.getText().trim().replace(".", "").replace(",", ".")); // Lendo o faturamento como double
                boolean ehLocadora = chkEhLocadoraDeVeiculos.isSelected();

                // Construtor SeguradoEmpresa: (nome, endereco, dataAbertura, bonus, cnpj, faturamento, ehLocadoraDeVeiculos)
                SeguradoEmpresa empresaAlterada = new SeguradoEmpresa(
                        txtNome.getText().trim(),
                        endereco,
                        dataAbertura,
                        bonus,
                        cnpj,
                        faturamento,
                        ehLocadora
                );

                if (seguradoEmpresaDAO.alterar(empresaAlterada)) {
                    showAlert(Alert.AlertType.INFORMATION, "Alteração", "Segurado Empresa alterado com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Alteração", "Erro ao alterar segurado Empresa (CNPJ não encontrado ou erro de persistência).");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Abertura, Bônus e Faturamento."); // Mensagem ajustada
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void excluirSegurado() {
        String cnpj = txtCnpj.getText().trim();
        if (StringUtils.ehNuloOuBranco(cnpj) || !ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Exclusão", "CNPJ inválido para exclusão.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar Exclusão",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Tem certeza que deseja excluir o segurado empresa " + cnpj + "?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (seguradoEmpresaDAO.excluir(cnpj)) {
                showAlert(Alert.AlertType.INFORMATION, "Exclusão", "Segurado Empresa excluído com sucesso!");
                limparCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Exclusão", "Erro ao excluir segurado Empresa (CNPJ não encontrado ou erro de persistência).");
            }
        }
    }

    private void limparCampos() {
        txtCnpj.clear();
        txtNome.clear();
        txtDataAbertura.clear(); // Ajustado
        txtBonus.clear();
        txtFaturamento.clear(); // Limpeza do novo campo
        chkEhLocadoraDeVeiculos.setSelected(false);
        txtLogradouro.clear();
        txtNumero.clear();
        txtComplemento.clear();

        txtCidade.clear();
        txtEstado.clear();
        txtCep.clear();
        txtPais.clear();
        setEstado(EstadoTela.INICIAL);
        txtCnpj.requestFocus();
    }

    private void limparCamposComCNPJ() {
        txtNome.clear();
        txtDataAbertura.clear(); // Ajustado
        txtBonus.clear();
        txtFaturamento.clear(); // Limpeza do novo campo
        chkEhLocadoraDeVeiculos.setSelected(false);
        txtLogradouro.clear();
        txtNumero.clear();
        txtComplemento.clear();

        txtCidade.clear();
        txtEstado.clear();
        txtCep.clear();
        txtPais.clear();
    }

    private void preencherCampos(SeguradoEmpresa segurado) {
        txtCnpj.setText(segurado.getCnpj());
        txtNome.setText(segurado.getNome());
        txtDataAbertura.setText(segurado.getDataAbertura().format(DATE_FORMATTER)); // Ajustado
        txtBonus.setText(DECIMAL_FORMAT.format(segurado.getBonus()));
        txtFaturamento.setText(DECIMAL_FORMAT.format(segurado.getFaturamento())); // Preenchimento do novo campo
        chkEhLocadoraDeVeiculos.setSelected(segurado.getEhLocadoraDeVeiculos());

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
            showAlert(Alert.AlertType.ERROR, "Validação", "Nome/Razão Social deve ser informado.");
            return false;
        }
        if (txtDataAbertura.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Data de Abertura inválida.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtDataAbertura.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Data de Abertura deve ser informada.");
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

        // Validação do Faturamento
        if (txtFaturamento.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Faturamento inválido.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtFaturamento.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Faturamento deve ser informado.");
            return false;
        }
        try {
            double faturamento = Double.parseDouble(txtFaturamento.getText().trim().replace(".", "").replace(",", "."));
            if (faturamento <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validação", "Faturamento deve ser maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validação", "Formato de faturamento inválido.");
            return false;
        }
        return validarCamposEndereco();
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

    private boolean validarCnpjParaInclusao(String cnpj) {
        if (StringUtils.ehNuloOuBranco(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Validação", "CNPJ deve ser informado.");
            return false;
        }
        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Validação", "CNPJ inválido.");
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
        // Inicializa DAOs e adiciona dados de teste para Segurado Empresa
        try {
            SeguradoEmpresaDAO segEmpDAO = new SeguradoEmpresaDAO();

            // Adicionar Segurado Empresa para teste (CNPJ válido e com todos os dados)
            Endereco endEmpresa1 = new Endereco("Av. das Empresas", "01000-000", "1000", "Conj. 50", "Brasil", "SP", "São Paulo");
            // Construtor: (nome, endereco, dataAbertura, bonus, cnpj, faturamento, ehLocadoraDeVeiculos)
            SeguradoEmpresa empresa1 = new SeguradoEmpresa(
                    "Empresa Alpha Ltda",
                    endEmpresa1,
                    LocalDate.of(2005, 1, 1),
                    new BigDecimal("1500.00"),
                    "11222333000144",
                    100000.00, // Faturamento
                    false
            );
            if (segEmpDAO.buscar(empresa1.getIdUnico()) == null) {
                if (segEmpDAO.incluir(empresa1)) {
                    System.out.println("Segurado Empresa 11222333000144 incluído para teste.");
                } else {
                    System.out.println("Erro ao incluir Segurado Empresa 11222333000144.");
                }
            } else {
                System.out.println("Segurado Empresa 11222333000144 já existe.");
            }

            // Exemplo de outra empresa, locadora de veículos
            Endereco endEmpresa2 = new Endereco("Rua dos Carros", "02000-000", "50", "Galpão", "Brasil", "RJ", "Rio de Janeiro");
            SeguradoEmpresa empresa2 = new SeguradoEmpresa(
                    "Loca Mais S.A.",
                    endEmpresa2,
                    LocalDate.of(2018, 6, 20),
                    new BigDecimal("5000.00"),
                    "99888777000100",
                    500000.00, // Faturamento
                    true
            );
            if (segEmpDAO.buscar(empresa2.getIdUnico()) == null) {
                if (segEmpDAO.incluir(empresa2)) {
                    System.out.println("Segurado Empresa 99888777000100 (Locadora) incluído para teste.");
                } else {
                    System.out.println("Erro ao incluir Segurado Empresa 99888777000100.");
                }
            } else {
                System.out.println("Segurado Empresa 99888777000100 (Locadora) já existe.");
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
