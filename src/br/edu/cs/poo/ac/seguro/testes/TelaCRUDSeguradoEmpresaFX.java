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

public class TelaCRUDSeguradoEmpresaFX extends Application {

    private SeguradoEmpresaDAO seguradoEmpresaDAO;

    // Campos de Segurado Empresa
    private TextField txtCnpj;
    private TextField txtNome;
    private TextField txtDataAbertura;
    private TextField txtBonus;
    private TextField txtFaturamento;
    private CheckBox chkEhLocadoraDeVeiculos;
    private Label lblIdUnicoValue; // Novo campo para exibir o ID único (CNPJ)

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
    private Button btnLimpar; // Renomeado para "Limpar Tudo" na interface

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
        primaryStage.setTitle("Gerenciamento de Segurados Empresas");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT); // Alinhamento no canto superior esquerdo
        grid.setHgap(15); // Aumentei o espaçamento horizontal
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 30, 30, 30)); // Aumentei o padding

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

        Scene scene = new Scene(grid, 700, 750); // Tamanho ajustado para acomodar os novos elementos e espaçamento
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initComponents() {
        // Título principal da tela
        Label titleLabel = new Label("Cadastro de Segurado Empresa");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 0 15px 0;");
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);


        txtCnpj = new TextField();
        txtCnpj.setPromptText("Digite o CNPJ");
        txtCnpj.setMaxWidth(180);
        setupCnpjMask(txtCnpj);

        lblIdUnicoValue = new Label(""); // Inicialmente vazio
        lblIdUnicoValue.setStyle("-fx-font-weight: bold;");


        txtNome = new TextField();
        txtNome.setPromptText("Razão Social / Nome Fantasia");
        txtNome.setMaxWidth(300); // Aumentei a largura

        txtDataAbertura = new TextField();
        txtDataAbertura.setPromptText("DD/MM/AAAA");
        txtDataAbertura.setMaxWidth(100);
        setupDateMask(txtDataAbertura);

        txtBonus = new TextField();
        txtBonus.setPromptText("Ex: 100,00");
        txtBonus.setMaxWidth(120);
        setupCurrencyMask(txtBonus);

        txtFaturamento = new TextField();
        txtFaturamento.setPromptText("Ex: 50000,00");
        txtFaturamento.setMaxWidth(120);
        setupCurrencyMask(txtFaturamento);

        chkEhLocadoraDeVeiculos = new CheckBox("É Locadora de Veículos?");

        txtLogradouro = new TextField();
        txtLogradouro.setPromptText("Nome da Rua, Avenida, etc.");
        txtLogradouro.setMaxWidth(300); // Aumentei a largura

        txtNumero = new TextField();
        txtNumero.setPromptText("Número");
        txtNumero.setMaxWidth(80);

        txtComplemento = new TextField();
        txtComplemento.setPromptText("Apto, Bloco, Sala (opcional)");
        txtComplemento.setMaxWidth(200);

        txtCidade = new TextField();
        txtCidade.setPromptText("Cidade");
        txtCidade.setMaxWidth(150);

        txtEstado = new TextField();
        txtEstado.setPromptText("UF (Ex: SP)");
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
        btnLimpar = new Button("Limpar Tudo"); // Novo texto
        btnLimpar.setPrefWidth(100);
    }

    private void setupLayout(GridPane grid) {
        int row = 0;

        // Título Principal
        Label mainTitle = new Label("Cadastro de Segurado Empresa");
        mainTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");
        GridPane.setHalignment(mainTitle, javafx.geometry.HPos.CENTER);
        grid.add(mainTitle, 0, row, 3, 1);
        row++;

        // Separador visual
        grid.add(new Separator(), 0, row, 3, 1);
        row++;

        // Seção Segurado Empresa
        Label lblSeguradoEmpresaTitle = new Label("Dados da Empresa");
        lblSeguradoEmpresaTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10px 0 5px 0;");
        GridPane.setHalignment(lblSeguradoEmpresaTitle, javafx.geometry.HPos.LEFT);
        grid.add(lblSeguradoEmpresaTitle, 0, row, 3, 1);
        row++;


        grid.add(new Label("CNPJ:"), 0, row);
        grid.add(txtCnpj, 1, row);
        grid.add(btnBuscar, 2, row);
        row++;

        grid.add(new Label("ID Único:"), 0, row);
        grid.add(lblIdUnicoValue, 1, row); // Exibe o CNPJ como ID Único
        row++;

        grid.add(new Label("Nome/Razão Social:"), 0, row);
        grid.add(txtNome, 1, row, 2, 1);
        row++;

        grid.add(new Label("Data Abertura:"), 0, row);
        grid.add(txtDataAbertura, 1, row);
        row++;

        grid.add(new Label("Bônus:"), 0, row);
        grid.add(txtBonus, 1, row);
        row++;

        grid.add(new Label("Faturamento Anual:"), 0, row);
        grid.add(txtFaturamento, 1, row);
        row++;

        grid.add(chkEhLocadoraDeVeiculos, 1, row, 2, 1);
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
        txtCnpj.setFocusTraversable(true);
        btnBuscar.setFocusTraversable(true);
        txtNome.setFocusTraversable(true);
        txtDataAbertura.setFocusTraversable(true);
        txtBonus.setFocusTraversable(true);
        txtFaturamento.setFocusTraversable(true);
        chkEhLocadoraDeVeiculos.setFocusTraversable(true);
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
                lblIdUnicoValue.setText(""); // Limpa o ID Único
                break;
            case BUSCA_SUCESSO:
                cnpjEditavel = false;
                camposEditaveis = true;
                btnAlterarHabilitado = true;
                btnExcluirHabilitado = true;
                lblIdUnicoValue.setText(txtCnpj.getText()); // Exibe o CNPJ como ID Único
                break;
            case INCLUSAO_NOVO:
                cnpjEditavel = false;
                camposEditaveis = true;
                btnIncluirHabilitado = true;
                lblIdUnicoValue.setText("Novo"); // Indica que é um novo registro
                break;
        }

        txtCnpj.setEditable(cnpjEditavel);
        txtNome.setEditable(camposEditaveis);
        txtDataAbertura.setEditable(camposEditaveis);
        txtBonus.setEditable(camposEditaveis);
        txtFaturamento.setEditable(camposEditaveis);
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
            showAlert(Alert.AlertType.WARNING, "Busca", "O campo CNPJ é obrigatório para realizar a busca.");
            setEstado(EstadoTela.INICIAL);
            return;
        }
        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Busca", "CNPJ inválido. Verifique o número digitado.");
            setEstado(EstadoTela.INICIAL);
            return;
        }

        SeguradoEmpresa segurado = seguradoEmpresaDAO.buscar(cnpj);

        if (segurado != null) {
            preencherCampos(segurado);
            setEstado(EstadoTela.BUSCA_SUCESSO);
            showAlert(Alert.AlertType.INFORMATION, "Busca Concluída", "Segurado Empresa encontrado com sucesso!");
        } else {
            limparCamposComCNPJ();
            setEstado(EstadoTela.INCLUSAO_NOVO);
            showAlert(Alert.AlertType.INFORMATION, "Segurado Não Encontrado", "Nenhum segurado empresa com este CNPJ foi encontrado. Você pode incluir um novo registro.");
            txtNome.requestFocus();
        }
    }

    private void incluirSegurado() {
        String cnpj = txtCnpj.getText().trim();
        if (validarCamposComuns() && validarCamposEndereco() && validarCnpjParaInclusao(cnpj)) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataAbertura = LocalDate.parse(txtDataAbertura.getText().trim(), DATE_FORMATTER);
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double faturamento = Double.parseDouble(txtFaturamento.getText().trim().replace(".", "").replace(",", "."));
                boolean ehLocadora = chkEhLocadoraDeVeiculos.isSelected();

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
                    showAlert(Alert.AlertType.INFORMATION, "Inclusão Realizada", "Segurado Empresa incluído com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Falha na Inclusão", "Erro ao incluir segurado Empresa. Verifique se o CNPJ já existe ou tente novamente.");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Abertura, Bônus e Faturamento. Assegure que os valores numéricos usem vírgula para centavos.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void alterarSegurado() {
        String cnpj = txtCnpj.getText().trim();
        if (validarCamposComuns() && validarCamposEndereco()) {
            try {
                Endereco endereco = criarObjetoEndereco();
                LocalDate dataAbertura = LocalDate.parse(txtDataAbertura.getText().trim(), DATE_FORMATTER);
                BigDecimal bonus = new BigDecimal(txtBonus.getText().trim().replace(".", "").replace(",", "."));
                double faturamento = Double.parseDouble(txtFaturamento.getText().trim().replace(".", "").replace(",", "."));
                boolean ehLocadora = chkEhLocadoraDeVeiculos.isSelected();

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
                    showAlert(Alert.AlertType.INFORMATION, "Alteração Realizada", "Segurado Empresa alterado com sucesso!");
                    limparCampos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Falha na Alteração", "Erro ao alterar segurado Empresa. O CNPJ não foi encontrado ou ocorreu um erro de persistência.");
                }
            } catch (NumberFormatException | DateTimeParseException e) {
                showAlert(Alert.AlertType.ERROR, "Erro de Formato", "Verifique os formatos de Data de Abertura, Bônus e Faturamento. Assegure que os valores numéricos usem vírgula para centavos.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void excluirSegurado() {
        String cnpj = txtCnpj.getText().trim();
        if (StringUtils.ehNuloOuBranco(cnpj) || !ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Exclusão Inválida", "CNPJ inválido ou não informado para exclusão.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar Exclusão", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Tem certeza que deseja excluir o segurado empresa com CNPJ: " + cnpj + "?");
        alert.setContentText("Esta ação não poderá ser desfeita.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if (seguradoEmpresaDAO.excluir(cnpj)) {
                showAlert(Alert.AlertType.INFORMATION, "Exclusão Concluída", "Segurado Empresa excluído com sucesso!");
                limparCampos();
            } else {
                showAlert(Alert.AlertType.ERROR, "Falha na Exclusão", "Erro ao excluir segurado Empresa. O CNPJ pode não ter sido encontrado.");
            }
        }
    }

    private void limparCampos() {
        txtCnpj.clear();
        txtNome.clear();
        txtDataAbertura.clear();
        txtBonus.clear();
        txtFaturamento.clear();
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
        txtDataAbertura.clear();
        txtBonus.clear();
        txtFaturamento.clear();
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
        txtDataAbertura.setText(segurado.getDataAbertura().format(DATE_FORMATTER));
        txtBonus.setText(DECIMAL_FORMAT.format(segurado.getBonus()));
        txtFaturamento.setText(DECIMAL_FORMAT.format(segurado.getFaturamento()));
        chkEhLocadoraDeVeiculos.setSelected(segurado.getEhLocadoraDeVeiculos());

        Endereco endereco = segurado.getEndereco();
        if (endereco != null) {
            txtLogradouro.setText(endereco.getLogradouro());
            txtNumero.setText(endereco.getNumero());
            txtComplemento.setText(endereco.getComplemento());
            txtCidade.setText(endereco.getCidade());
            txtEstado.setText(endereco.getEstado());
            txtCep.setText(formatCep(endereco.getCep())); // Formata o CEP para exibição
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
                cleanCep(txtCep.getText()), // Limpa o CEP antes de passar para o DAO
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
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Nome/Razão Social' é obrigatório.");
            return false;
        }
        if (txtDataAbertura.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Data de Abertura inválida. Verifique o formato.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtDataAbertura.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Data de Abertura' é obrigatório.");
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

        if (txtFaturamento.getStyle().contains("red")) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Faturamento inválido. Verifique o formato numérico.");
            return false;
        }
        if (StringUtils.ehNuloOuBranco(txtFaturamento.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'Faturamento' é obrigatório.");
            return false;
        }
        try {
            double faturamento = Double.parseDouble(txtFaturamento.getText().trim().replace(".", "").replace(",", "."));
            if (faturamento <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O Faturamento deve ser um valor maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "Formato de faturamento inválido. Use apenas números.");
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

    private boolean validarCnpjParaInclusao(String cnpj) {
        if (StringUtils.ehNuloOuBranco(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "O campo 'CNPJ' é obrigatório para inclusão.");
            return false;
        }
        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            showAlert(Alert.AlertType.ERROR, "Validação Necessária", "CNPJ inválido. Não é possível incluir com um CNPJ inválido.");
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
            SeguradoEmpresa empresa1 = new SeguradoEmpresa(
                    "Empresa Alpha Ltda",
                    endEmpresa1,
                    LocalDate.of(2005, 1, 1),
                    new BigDecimal("1500.00"),
                    "11222333000144",
                    100000.00,
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
                    500000.00,
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
