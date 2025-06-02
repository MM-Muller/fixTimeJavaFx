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

public class ClienteView {
    private ArrayList<Cliente> lista = new ArrayList<>();
    private TableView<Cliente> tabela = new TableView<>();

    // O método principal agora retorna um Parent, não mais um void e não recebe Stage
    public Parent createView() {

        carregarClientes();
        atualizarTabela();

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
        colNome.setPrefWidth(120); // Define largura preferencial

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

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_LEFT);
        form.getChildren().addAll(lblNome, txtNome, lblCPF, txtCPF, lblTelefone, txtTelefone,
                lblEmail, txtEmail, lblSenha, txtSenha, btnSalvar, btnExcluir);
        txtNome.setMaxWidth(200);
        txtCPF.setMaxWidth(200);
        txtTelefone.setMaxWidth(200);
        txtEmail.setMaxWidth(200);
        txtSenha.setMaxWidth(200);


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
        a.setTitle("Erro");
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