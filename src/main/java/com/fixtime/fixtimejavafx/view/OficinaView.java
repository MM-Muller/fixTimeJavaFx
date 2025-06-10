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
            if (newText.matches("\\d{0,14}")) { // CNPJ tem 14 dígitos numéricos
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
            if (newText.matches("\\d{0,8}")) { // CEP tem 8 dígitos numéricos
                return change;
            }
            return null;
        }));

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().isEmpty() || cmbCategoria.getValue() == null || txtCnpj.getText().isEmpty() ||
                    txtTelefone.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCep.getText().isEmpty()) {
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

            boolean sucessoNoSalvamento = false;
            try {
                Oficina o = new Oficina(lista.size() + 1, txtNome.getText(), cmbCategoria.getValue(),
                        cnpj, telefone, email, cep);
                lista.add(o);
                OficinaDAO.salvar(lista);
                sucessoNoSalvamento = true;
                alertInfo("Oficina salva com sucesso!");
            } catch (Exception ex) {
                alert("Erro ao salvar: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                if (sucessoNoSalvamento) {
                    atualizarTabela();
                    limparCampos(txtNome, txtCnpj, txtTelefone, txtEmail, txtCep);
                    cmbCategoria.setValue(null);
                }
            }
        });

        Button btnExcluir = new Button("Excluir Selecionado");
        btnExcluir.setOnAction(e -> {
            Oficina o = tabela.getSelectionModel().getSelectedItem();
            if (o != null) {
                lista.remove(o);
                try {
                    OficinaDAO.salvar(lista);
                    atualizarTabela();
                    alertInfo("Oficina excluída com sucesso!");
                } catch (Exception ex) {
                    alert("Erro ao excluir: " + ex.getMessage());
                }
            } else {
                alert("Selecione uma oficina para excluir.");
            }
        });

        TableColumn<Oficina, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(150);

        TableColumn<Oficina, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setPrefWidth(120);

        TableColumn<Oficina, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(100);

        TableColumn<Oficina, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(150);

        TableColumn<Oficina, String> colCep = new TableColumn<>("CEP");
        colCep.setCellValueFactory(new PropertyValueFactory<>("cep"));
        colCep.setPrefWidth(80);

        tabela.getColumns().addAll(colNome, colCategoria, colTelefone, colEmail, colCep);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_LEFT);
        form.getChildren().addAll(
                new Label("Nome:"), txtNome,
                new Label("Categoria:"), cmbCategoria,
                new Label("CNPJ:"), txtCnpj,
                new Label("Telefone:"), txtTelefone,
                new Label("Email:"), txtEmail,
                new Label("CEP:"), txtCep,
                btnSalvar, btnExcluir
        );
        txtNome.setMaxWidth(250);
        cmbCategoria.setMaxWidth(250);
        txtCnpj.setMaxWidth(250);
        txtTelefone.setMaxWidth(250);
        txtEmail.setMaxWidth(250);
        txtCep.setMaxWidth(250);

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