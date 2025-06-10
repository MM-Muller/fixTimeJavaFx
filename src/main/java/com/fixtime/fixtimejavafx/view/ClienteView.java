package com.fixtime.fixtimejavafx.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import com.fixtime.fixtimejavafx.model.Cliente;
import com.fixtime.fixtimejavafx.persistence.ClienteDAO;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClienteView {
    private ArrayList<Cliente> lista = new ArrayList<>();
    private TableView<Cliente> tabela = new TableView<>();

    public Parent createView() {
        carregarClientes();
        atualizarTabela();

        // Campo para o Nome
        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome completo do cliente");

        // Campo para o CPF
        Label lblCPF = new Label("CPF:");
        TextField txtCPF = new TextField();
        txtCPF.setPromptText("CPF (somente números)");
        txtCPF.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,11}")) { // Permite até 11 dígitos numéricos
                return change;
            }
            return null;
        }));

        // Campo para o Telefone
        Label lblTelefone = new Label("Telefone:");
        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Telefone (ex: DD9XXXXXXXX)");
        txtTelefone.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,11}")) { // Permite até 11 dígitos numéricos
                return change;
            }
            return null;
        }));

        // Campo para o Email
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail do cliente");

        // Botão Salvar
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            // Validação de campos vazios (Senha removida)
            if (txtNome.getText().isEmpty() || txtCPF.getText().isEmpty() || txtTelefone.getText().isEmpty()
                    || txtEmail.getText().isEmpty()) { // Removido txtSenha.getText().isEmpty()
                alert("Preencha todos os campos.");
                return;
            }

            String cpf = txtCPF.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();

            // Validação de formato do CPF (exatamente 11 dígitos)
            if (!cpf.matches("\\d{11}")) {
                alert("CPF inválido. Deve conter exatamente 11 dígitos numéricos.");
                return;
            }

            // Validação de formato do Telefone (10 ou 11 dígitos)
            if (!telefone.matches("\\d{10,11}")) {
                alert("Telefone inválido. Deve conter 10 ou 11 dígitos numéricos (com DDD).");
                return;
            }

            // Validação de formato do E-mail (Expressão Regular)
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            Matcher emailMatcher = emailPattern.matcher(email);
            if (!emailMatcher.matches()) {
                alert("Formato de e-mail inválido.");
                return;
            }

            // Validação de CPF já cadastrado
            if (lista.stream().anyMatch(c -> c.getCpf().equals(cpf))) {
                alert("CPF já cadastrado.");
                return;
            }
            // Validação de E-mail já cadastrado
            if (lista.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(email))) {
                alert("E-mail já cadastrado.");
                return;
            }

            boolean sucessoNoSalvamento = false;
            try {
                // Cria um novo objeto Cliente (Senha removida do construtor)
                Cliente cliente = new Cliente(lista.size() + 1, txtNome.getText(), cpf, telefone, email); // Removido txtSenha.getText()
                lista.add(cliente); // Adiciona o cliente à lista em memória
                ClienteDAO.salvar(lista); // Salva a lista atualizada no arquivo
                sucessoNoSalvamento = true;
                alertInfo("Cliente salvo com sucesso!");
            } catch (Exception ex) {
                alert("Erro ao salvar: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                if (sucessoNoSalvamento) {
                    atualizarTabela(); // Atualiza a tabela na UI
                    // Limpa os campos após o salvamento (Senha removida)
                    limparCampos(txtNome, txtCPF, txtTelefone, txtEmail); // Removido txtSenha
                } else {
                    System.out.println("Tentativa de salvar cliente falhou.");
                }
            }
        });

        // Botão Excluir
        Button btnExcluir = new Button("Excluir Selecionado");
        btnExcluir.setOnAction(e -> {
            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                lista.remove(selecionado);
                try {
                    ClienteDAO.salvar(lista);
                    atualizarTabela();
                    alertInfo("Cliente excluído com sucesso!");
                } catch (Exception ex) {
                    alert("Erro ao excluir: " + ex.getMessage());
                }
            } else {
                alert("Selecione um cliente para excluir.");
            }
        });

        // --- Configuração das Colunas da Tabela ---
        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(120);

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(150);

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(100);

        TableColumn<Cliente, String> colCPF = new TableColumn<>("CPF");
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCPF.setPrefWidth(100);

        tabela.getColumns().addAll(colNome, colEmail, colTelefone, colCPF);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- Organização do Layout ---
        VBox form = new VBox(10); // Espaçamento entre os elementos
        form.setPadding(new Insets(20)); // Preenchimento interno
        form.setAlignment(Pos.TOP_LEFT); // Alinhamento
        form.getChildren().addAll(lblNome, txtNome, lblCPF, txtCPF, lblTelefone, txtTelefone,
                lblEmail, txtEmail, // Removido lblSenha e txtSenha
                btnSalvar, btnExcluir);
        // Largura máxima para os campos (txtSenha removido)
        txtNome.setMaxWidth(250);
        txtCPF.setMaxWidth(250);
        txtTelefone.setMaxWidth(250);
        txtEmail.setMaxWidth(250);

        BorderPane viewRoot = new BorderPane();
        viewRoot.setLeft(form); // Formulário à esquerda
        viewRoot.setCenter(tabela); // Tabela ao centro

        Label titleLabel = new Label("Gerenciamento de Clientes");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        viewRoot.setTop(titleLabel); // Título no topo

        return viewRoot; // Retorna o layout completo da view
    }

    // --- Métodos Auxiliares ---

    private void carregarClientes() {
        try {
            lista = ClienteDAO.carregar();
        } catch (Exception e) {
            lista = new ArrayList<>();
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void atualizarTabela() {
        tabela.setItems(FXCollections.observableArrayList(lista));
    }

    private void limparCampos(TextField... campos) {
        for (TextField campo : campos) campo.clear();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.setHeaderText(null);
        a.setTitle("Erro de Validação");
        a.showAndWait();
    }

    private void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.setHeaderText(null);
        a.setTitle("Informação");
        a.showAndWait();
    }
}