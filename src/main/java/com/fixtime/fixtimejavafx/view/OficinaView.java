package com.fixtime.fixtimejavafx.view;

import javafx.scene.Parent; // Importação para o tipo Parent
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections; // Necessário para FXCollections.observableArrayList
import javafx.geometry.Insets; // Para padding
import javafx.geometry.Pos;   // Para alinhamento
import com.fixtime.fixtimejavafx.model.Oficina;
import com.fixtime.fixtimejavafx.persistence.OficinaDAO;

import java.util.ArrayList;

public class OficinaView {
    private ArrayList<Oficina> lista = new ArrayList<>();
    private TableView<Oficina> tabela = new TableView<>();

    // O método principal agora retorna um Parent, não mais um void e não recebe Stage
    public Parent createView() {

        // Carrega as oficinas e atualiza a tabela ao criar a view
        carregarOficinas();
        atualizarTabela(); // Garante que a tabela já vem populada

        // --- Componentes da UI ---
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome da Oficina");

        ComboBox<String> cmbCategoria = new ComboBox<>();
        cmbCategoria.getItems().addAll("Borracharia", "Auto Elétrica", "Oficina Mecânica", "Lava Car");
        cmbCategoria.setPromptText("Selecione a Categoria");

        TextField txtCnpj = new TextField();
        txtCnpj.setPromptText("CNPJ (ex: XX.XXX.XXX/XXXX-XX)");

        TextField txtTelefone = new TextField();
        txtTelefone.setPromptText("Telefone (ex: (XX) XXXX-XXXX)");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("E-mail");

        TextField txtCep = new TextField();
        txtCep.setPromptText("CEP (ex: XXXXX-XXX)");

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
                cmbCategoria.setValue(null); // Limpa a seleção do ComboBox
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

        // --- Configuração da Tabela ---
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
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajusta colunas automaticamente

        // --- Layout dos Componentes ---
        VBox form = new VBox(10); // Espaçamento de 10px entre os elementos do formulário
        form.setPadding(new Insets(20)); // Padding ao redor do formulário
        form.setAlignment(Pos.TOP_LEFT); // Alinha o formulário ao topo/esquerda
        form.getChildren().addAll(
                new Label("Nome:"), txtNome,
                new Label("Categoria:"), cmbCategoria,
                new Label("CNPJ:"), txtCnpj,
                new Label("Telefone:"), txtTelefone,
                new Label("Email:"), txtEmail,
                new Label("CEP:"), txtCep,
                btnSalvar, btnExcluir
        );
        // Definir largura preferencial para os TextFields e ComboBox
        txtNome.setMaxWidth(250);
        cmbCategoria.setMaxWidth(250);
        txtCnpj.setMaxWidth(250);
        txtTelefone.setMaxWidth(250);
        txtEmail.setMaxWidth(250);
        txtCep.setMaxWidth(250);


        // BorderPane como layout raiz para esta View
        BorderPane viewRoot = new BorderPane();
        viewRoot.setLeft(form); // Coloca o formulário à esquerda
        viewRoot.setCenter(tabela); // Coloca a tabela no centro

        // Opcional: Adicionar um título ou cabeçalho para a view de oficinas
        Label titleLabel = new Label("Gerenciamento de Oficinas");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER); // Centraliza o título
        viewRoot.setTop(titleLabel);

        return viewRoot; // Retorna o BorderPane que contém toda a UI do OficinaView
    }

    // --- Métodos de Apoio ---
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