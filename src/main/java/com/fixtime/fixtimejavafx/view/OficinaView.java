package com.fixtime.fixtimejavafx.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import com.fixtime.fixtimejavafx.model.Oficina;
import com.fixtime.fixtimejavafx.persistence.OficinaDAO;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OficinaView {
    private ArrayList<Oficina> lista = new ArrayList<>();
    private TableView<Oficina> tabela = new TableView<>();
    private Oficina oficinaSelecionada = null;

    public Parent createView() {
        carregarOficinas();
        atualizarTabela();

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome da Oficina");

        ComboBox<String> cmbCategoria = new ComboBox<>();
        cmbCategoria.getItems().addAll("Borracharia", "Auto Elétrica", "Oficina Mecânica", "Lava Car");
        cmbCategoria.setPromptText("Selecione a Categoria");

        TextField txtCnpj = new TextField();
        txtCnpj.setPromptText("CNPJ (somente números)");
        txtCnpj.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,14}")) {
                return change;
            }
            return null;
        }));

        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Telefone (ex: DD9XXXXXXXX)");
        txtTelefone.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,11}")) {
                return change;
            }
            return null;
        }));

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail");

        TextField txtCep = new TextField();
        txtCep.setPromptText("CEP (somente números)");
        txtCep.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,8}")) {
                return change;
            }
            return null;
        }));

        TextField txtEndereco = new TextField();
        txtEndereco.setPromptText("Endereço completo");


        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                oficinaSelecionada = newSelection;
                txtNome.setText(newSelection.getNome());
                cmbCategoria.setValue(newSelection.getCategoria());
                txtCnpj.setText(newSelection.getCnpj());
                txtTelefone.setText(newSelection.getTelefone());
                txtEmail.setText(newSelection.getEmail());
                txtCep.setText(newSelection.getCep());
                txtEndereco.setText(newSelection.getEndereco());
            }
        });

        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setOnAction(e -> {
            if (txtNome.getText().isEmpty() || cmbCategoria.getValue() == null || txtCnpj.getText().isEmpty() ||
                    txtTelefone.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCep.getText().isEmpty() ||
                    txtEndereco.getText().isEmpty()){
                alert("Preencha todos os campos.");
                return;
            }

            String cnpj = txtCnpj.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            String cep = txtCep.getText();

            if (!cnpj.matches("\\d{14}")) {
                alert("CNPJ inválido. Deve conter exatamente 14 dígitos numéricos.");
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

            if (!cep.matches("\\d{8}")) {
                alert("CEP inválido. Deve conter exatamente 8 dígitos numéricos.");
                return;
            }

            try {
                Oficina o = new Oficina(lista.size() + 1, txtNome.getText(), cmbCategoria.getValue(),
                        cnpj, telefone, email, cep, txtEndereco.getText());
                lista.add(o);
                OficinaDAO.salvar(lista);
                atualizarTabela();
                limparCampos(txtNome, txtCnpj, txtTelefone, txtEmail, txtCep, txtEndereco);
                cmbCategoria.setValue(null);
                alertInfo("Oficina cadastrada com sucesso!");
            } catch (Exception ex) {
                alert("Erro ao cadastrar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button btnEditar = new Button("Editar");
        btnEditar.setOnAction(e -> {
            if (oficinaSelecionada == null) {
                alert("Selecione uma oficina para editar.");
                return;
            }

            if (txtNome.getText().isEmpty() || cmbCategoria.getValue() == null || txtCnpj.getText().isEmpty() ||
                    txtTelefone.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCep.getText().isEmpty() ||
                    txtEndereco.getText().isEmpty()) {
                alert("Preencha todos os campos.");
                return;
            }

            String cnpj = txtCnpj.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            String cep = txtCep.getText();

            if (!cnpj.matches("\\d{14}")) {
                alert("CNPJ inválido. Deve conter exatamente 14 dígitos numéricos.");
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

            if (!cep.matches("\\d{8}")) {
                alert("CEP inválido. Deve conter exatamente 8 dígitos numéricos.");
                return;
            }

            try {
                oficinaSelecionada.setNome(txtNome.getText());
                oficinaSelecionada.setCategoria(cmbCategoria.getValue());
                oficinaSelecionada.setCnpj(cnpj);
                oficinaSelecionada.setTelefone(telefone);
                oficinaSelecionada.setEmail(email);
                oficinaSelecionada.setCep(cep);
                oficinaSelecionada.setEndereco(txtEndereco.getText());
                OficinaDAO.salvar(lista);
                tabela.getItems().clear();
                tabela.setItems(FXCollections.observableArrayList(lista));
                tabela.getSelectionModel().clearSelection();
                limparCampos(txtNome, txtCnpj, txtTelefone, txtEmail, txtCep, txtEndereco);
                cmbCategoria.setValue(null);
                oficinaSelecionada = null;
                alertInfo("Oficina atualizada com sucesso!");
            } catch (Exception ex) {
                alert("Erro ao editar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button btnExcluir = new Button("Excluir");
        btnExcluir.setOnAction(e -> {
            Oficina o = tabela.getSelectionModel().getSelectedItem();
            if (o != null) {
                lista.remove(o);
                try {
                    OficinaDAO.salvar(lista);
                    atualizarTabela();
                    limparCampos(txtNome, txtCnpj, txtTelefone, txtEmail, txtCep, txtEndereco);
                    cmbCategoria.setValue(null);
                    oficinaSelecionada = null;
                    alertInfo("Oficina excluída com sucesso!");
                } catch (Exception ex) {
                    alert("Erro ao excluir: " + ex.getMessage());
                }
            } else {
                alert("Selecione uma oficina para excluir.");
            }
        });

        Button btnLimpar = new Button("Limpar");
        btnLimpar.setOnAction(e -> {
            limparCampos(txtNome, txtCnpj, txtTelefone, txtEmail, txtCep, txtEndereco);
            cmbCategoria.setValue(null);
            oficinaSelecionada = null;
            tabela.getSelectionModel().clearSelection();
        });

        TableColumn<Oficina, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(110);

        TableColumn<Oficina, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setPrefWidth(120);

        TableColumn<Oficina, String> colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colCnpj.setPrefWidth(120);

        TableColumn<Oficina, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(100);

        TableColumn<Oficina, String> colCep = new TableColumn<>("CEP");
        colCep.setCellValueFactory(new PropertyValueFactory<>("cep"));
        colCep.setPrefWidth(80);

        TableColumn<Oficina, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colEndereco.setPrefWidth(110);

        TableColumn<Oficina, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(110);

        tabela.getColumns().addAll(colNome, colCategoria, colCnpj, colTelefone, colCep, colEndereco, colEmail);
        tabela.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tabela.setPrefWidth(800);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_LEFT);
        form.getChildren().addAll(
                new Label("Nome:"), txtNome,
                new Label("Categoria:"), cmbCategoria,
                new Label("CNPJ:"), txtCnpj,
                new Label("Telefone:"), txtTelefone,
                new Label("CEP:"), txtCep,
                new Label("Endereço:"), txtEndereco,
                new Label("Email:"), txtEmail,
                new HBox(10, btnLimpar, btnCadastrar, btnEditar, btnExcluir)
        );
        txtNome.setMaxWidth(250);
        cmbCategoria.setMaxWidth(250);
        txtCnpj.setMaxWidth(250);
        txtTelefone.setMaxWidth(250);
        txtEmail.setMaxWidth(250);
        txtCep.setMaxWidth(250);
        txtEndereco.setMaxWidth(250);

        BorderPane viewRoot = new BorderPane();
        viewRoot.setLeft(form);
        viewRoot.setCenter(tabela);

        Label titleLabel = new Label("Gerenciamento de Oficinas");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        viewRoot.setTop(titleLabel);

        return viewRoot;
    }

    private void carregarOficinas() {
        try {
            lista = OficinaDAO.carregar();
        } catch (Exception e) {
            lista = new ArrayList<>();
            System.err.println("Erro ao carregar oficinas: " + e.getMessage());
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