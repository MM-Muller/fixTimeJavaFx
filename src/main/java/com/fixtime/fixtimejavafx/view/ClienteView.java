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
    private Cliente clienteEmEdicao = null; // var para controlar o cliente que está sendo editado

    public Parent createView() {
        carregarClientes();
        atualizarTabela();

        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do cliente");

        Label lblCPF = new Label("CPF:");
        TextField txtCPF = new TextField();
        txtCPF.setPromptText("CPF (somente números)");
        txtCPF.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,11}")) {
                return change;
            }
            return null;
        }));

        Label lblTelefone = new Label("Telefone:");
        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("(ex: DD9XXXXXXXX)");
        txtTelefone.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,11}")) {
                return change;
            }
            return null;
        }));

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail do cliente");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().isEmpty() || txtCPF.getText().isEmpty() || txtTelefone.getText().isEmpty()
                    || txtEmail.getText().isEmpty()) {
                alert("Preencha todos os campos.");
                return;
            }

            String nome = txtNome.getText();
            String cpf = txtCPF.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();

            if (!cpf.matches("\\d{11}")) {
                alert("CPF inválido. Deve conter exatamente 11 dígitos numéricos.");
                return;
            }

            if (!telefone.matches("\\d{10,11}")) {
                alert("Telefone inválido. Deve conter 10 ou 11 dígitos numéricos (com DDD).");
                return;
            }

            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            Matcher emailMatcher = emailPattern.matcher(email);
            if (!emailMatcher.matches()) {
                alert("Formato de e-mail inválido.");
                return;
            }

            // validacao de CPF ja cadastrado (exceto o proprio cliente em edicao)
            if (lista.stream().anyMatch(c -> !c.equals(clienteEmEdicao) && c.getCpf().equals(cpf))) {
                alert("CPF já cadastrado.");
                return;
            }

            if (lista.stream().anyMatch(c -> !c.equals(clienteEmEdicao) && c.getEmail().equalsIgnoreCase(email))) {
                alert("E-mail já cadastrado.");
                return;
            }

            boolean sucessoNoSalvamento = false;
            try {
                if (clienteEmEdicao == null) {
                    Cliente cliente = new Cliente(lista.size() + 1, nome, cpf, telefone, email);
                    lista.add(cliente);
                    alertInfo("Cliente salvo com sucesso!");
                } else {
                    clienteEmEdicao.setNome(nome);
                    clienteEmEdicao.setCpf(cpf);
                    clienteEmEdicao.setTelefone(telefone);
                    clienteEmEdicao.setEmail(email);
                    alertInfo("Cliente atualizado com sucesso!");
                }
                ClienteDAO.salvar(lista);
                sucessoNoSalvamento = true;
            } catch (Exception ex) {
                alert("Erro ao salvar: " + ex.getMessage());
                ex.printStackTrace(); // aparece so pra mim
            } finally {
                if (sucessoNoSalvamento) {
                    atualizarTabela();
                    limparCampos(txtNome, txtCPF, txtTelefone, txtEmail);
                    clienteEmEdicao = null;
                    btnSalvar.setText("Salvar");
                } else {
                    System.out.println("Tentativa de salvar cliente falhou.");
                }
            }
        });

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
                } finally {
                    limparCampos(txtNome, txtCPF, txtTelefone, txtEmail);
                    clienteEmEdicao = null;
                    btnSalvar.setText("Salvar");
                }
            } else {
                alert("Selecione um cliente para excluir.");
            }
        });

        Button btnEditar = new Button("Editar Selecionado");
        btnEditar.setOnAction(e -> {
            Cliente selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                clienteEmEdicao = selecionado;
                txtNome.setText(selecionado.getNome());
                txtCPF.setText(selecionado.getCpf());
                txtTelefone.setText(selecionado.getTelefone());
                txtEmail.setText(selecionado.getEmail());
                btnSalvar.setText("Atualizar");
                alertInfo("Modifique os campos e clique em 'Atualizar'.");
            } else {
                alert("Selecione um cliente para editar.");
            }
        });


        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(100);

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(120);

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(80);

        TableColumn<Cliente, String> colCPF = new TableColumn<>("CPF");
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCPF.setPrefWidth(80);

        tabela.getColumns().addAll(colNome, colEmail, colTelefone, colCPF);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_LEFT);
        form.getChildren().addAll(lblNome, txtNome, lblCPF, txtCPF, lblTelefone, txtTelefone,
                lblEmail, txtEmail,
                btnSalvar, btnEditar, btnExcluir);
        txtNome.setMaxWidth(250);
        txtCPF.setMaxWidth(250);
        txtTelefone.setMaxWidth(250);
        txtEmail.setMaxWidth(250);

        BorderPane viewRoot = new BorderPane();
        viewRoot.setLeft(form);
        viewRoot.setCenter(tabela);

        Label titleLabel = new Label("Gerenciamento de Clientes");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        viewRoot.setTop(titleLabel);

        return viewRoot;
    }

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