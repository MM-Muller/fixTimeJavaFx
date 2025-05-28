package com.fixtime.fixtimejavafx.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.fixtime.fixtimejavafx.model.Oficina;
import com.fixtime.fixtimejavafx.persistence.OficinaDAO;

import java.util.ArrayList;

public class OficinaView {
    private ArrayList<Oficina> lista = new ArrayList<>();
    private TableView<Oficina> tabela = new TableView<>();

    public void start(Stage stage) {
        TextField txtNome = new TextField();
        ComboBox<String> cmbCategoria = new ComboBox<>();
        cmbCategoria.getItems().addAll("Borracharia", "Auto Elétrica", "Oficina Mecânica", "Lava Car");
        TextField txtCnpj = new TextField();
        TextField txtTelefone = new TextField();
        TextField txtEmail = new TextField();
        TextField txtCep = new TextField();

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().isEmpty() || cmbCategoria.getValue() == null || txtCnpj.getText().isEmpty() ||
                    txtTelefone.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCep.getText().isEmpty()) {
                alert("Preencha todos os campos.");
                return;
            }
            try {
                Oficina o = new Oficina(lista.size() + 1, txtNome.getText(), cmbCategoria.getValue(),
                        txtCnpj.getText(), txtTelefone.getText(), txtEmail.getText(), txtCep.getText());
                lista.add(o);
                OficinaDAO.salvar(lista);
                atualizarTabela();
                limparCampos(txtNome, txtCnpj, txtTelefone, txtEmail, txtCep);
                cmbCategoria.setValue(null);
                alertInfo("Oficina salva com sucesso!");
            } catch (Exception ex) {
                alert("Erro ao salvar: " + ex.getMessage());
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
        TableColumn<Oficina, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        TableColumn<Oficina, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        TableColumn<Oficina, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Oficina, String> colCep = new TableColumn<>("CEP");
        colCep.setCellValueFactory(new PropertyValueFactory<>("cep"));

        tabela.getColumns().addAll(colNome, colCategoria, colTelefone, colEmail, colCep);

        VBox form = new VBox(5, new Label("Nome:"), txtNome,
                new Label("Categoria:"), cmbCategoria,
                new Label("CNPJ:"), txtCnpj,
                new Label("Telefone:"), txtTelefone,
                new Label("Email:"), txtEmail,
                new Label("CEP:"), txtCep,
                btnSalvar, btnExcluir);

        BorderPane root = new BorderPane();
        root.setLeft(form);
        root.setCenter(tabela);

        carregarOficinas();
        atualizarTabela();

        Scene scene = new Scene(root, 850, 450);
        stage.setTitle("Cadastro de Oficinas");
        stage.setScene(scene);
        stage.show();
    }

    private void carregarOficinas() {
        try {
            lista = OficinaDAO.carregar();
        } catch (Exception e) {
            lista = new ArrayList<>();
        }
    }

    private void atualizarTabela() {
        tabela.getItems().setAll(lista);
    }

    private void limparCampos(TextField... campos) {
        for (TextField campo : campos) campo.clear();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }
}