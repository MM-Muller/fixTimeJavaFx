package com.fixtime.fixtimejavafx.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.fixtime.fixtimejavafx.model.Cliente;
import com.fixtime.fixtimejavafx.persistence.ClienteDAO;

import java.util.ArrayList;

public class ClienteView {
    private ArrayList<Cliente> lista = new ArrayList<>();
    private TableView<Cliente> tabela = new TableView<>();

    public void start(Stage stage) {
        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField();

        Label lblCPF = new Label("CPF:");
        TextField txtCPF = new TextField();

        Label lblTelefone = new Label("Telefone:");
        TextField txtTelefone = new TextField();

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();

        Label lblSenha = new Label("Senha:");
        PasswordField txtSenha = new PasswordField();

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            if (txtNome.getText().isEmpty() || txtCPF.getText().isEmpty() || txtTelefone.getText().isEmpty()
                    || txtEmail.getText().isEmpty() || txtSenha.getText().isEmpty()) {
                alert("Preencha todos os campos.");
                return;
            }
            try {
                Cliente cliente = new Cliente(lista.size() + 1, txtNome.getText(), txtCPF.getText(),
                        txtTelefone.getText(), txtEmail.getText(), txtSenha.getText());
                lista.add(cliente);
                ClienteDAO.salvar(lista);
                atualizarTabela();
                limparCampos(txtNome, txtCPF, txtTelefone, txtEmail, txtSenha);
                alertInfo("Cliente salvo com sucesso!");
            } catch (Exception ex) {
                alert("Erro ao salvar: " + ex.getMessage());
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
                }
            } else {
                alert("Selecione um cliente para excluir.");
            }
        });

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        TableColumn<Cliente, String> colCPF = new TableColumn<>("CPF");
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabela.getColumns().addAll(colNome, colEmail, colTelefone, colCPF);

        VBox form = new VBox(5, lblNome, txtNome, lblCPF, txtCPF, lblTelefone, txtTelefone,
                lblEmail, txtEmail, lblSenha, txtSenha, btnSalvar, btnExcluir);
        BorderPane root = new BorderPane();
        root.setLeft(form);
        root.setCenter(tabela);

        carregarClientes();
        atualizarTabela();

        Scene scene = new Scene(root, 850, 450);
        stage.setTitle("Cadastro de Clientes");
        stage.setScene(scene);
        stage.show();
    }

    private void carregarClientes() {
        try {
            lista = ClienteDAO.carregar();
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