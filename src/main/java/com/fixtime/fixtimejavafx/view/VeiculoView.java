package com.fixtime.fixtimejavafx.view;

import javafx.scene.Parent; // Importação para o tipo Parent que será retornado
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections; // Necessário para FXCollections.observableArrayList
import javafx.geometry.Insets; // Para padding
import javafx.geometry.Pos;   // Para alinhamento
import com.fixtime.fixtimejavafx.model.Veiculo;
import com.fixtime.fixtimejavafx.persistence.VeiculoDAO;

import java.util.ArrayList;

public class VeiculoView {
    private ArrayList<Veiculo> lista = new ArrayList<>();
    private TableView<Veiculo> tabela = new TableView<>();

    // O método principal agora retorna um Parent, não mais um void e não recebe Stage
    public Parent createView() {

        // Carrega os veículos e atualiza a tabela ao criar a view
        carregarVeiculos();
        atualizarTabela(); // Isso garante que a tabela já vem populada

        // --- Componentes da UI ---
        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("carro", "moto", "caminhao", "van", "onibus");
        cmbTipo.setPromptText("Selecione o Tipo"); // Texto de dica

        TextField txtMarca = new TextField();
        txtMarca.setPromptText("Marca do veículo");

        TextField txtModelo = new TextField();
        txtModelo.setPromptText("Modelo do veículo");

        TextField txtAno = new TextField();
        txtAno.setPromptText("Ano (ex: 2023)");

        TextField txtCor = new TextField();
        txtCor.setPromptText("Cor do veículo");

        TextField txtPlaca = new TextField();
        txtPlaca.setPromptText("Placa (ex: ABC-1234)");

        TextField txtKm = new TextField();
        txtKm.setPromptText("Quilometragem (ex: 15000.5)");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            if (cmbTipo.getValue() == null || txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() ||
                    txtAno.getText().isEmpty() || txtCor.getText().isEmpty() || txtPlaca.getText().isEmpty() || txtKm.getText().isEmpty()) {
                alert("Preencha todos os campos.");
                return;
            }
            try {
                // Validação básica para Ano e Km
                int ano = Integer.parseInt(txtAno.getText());
                double km = Double.parseDouble(txtKm.getText());

                Veiculo v = new Veiculo(lista.size() + 1, cmbTipo.getValue(), txtMarca.getText(), txtModelo.getText(),
                        ano, txtCor.getText(), txtPlaca.getText(), km);
                lista.add(v);
                VeiculoDAO.salvar(lista);
                atualizarTabela();
                limparCampos(txtMarca, txtModelo, txtAno, txtCor, txtPlaca, txtKm);
                cmbTipo.setValue(null); // Limpa a seleção do ComboBox
                alertInfo("Veículo salvo com sucesso!");
            } catch (NumberFormatException ex) {
                alert("Erro de formato: Ano ou KM devem ser números válidos.");
            } catch (Exception ex) {
                alert("Erro ao salvar: " + ex.getMessage());
            }
        });

        Button btnExcluir = new Button("Excluir Selecionado");
        btnExcluir.setOnAction(e -> {
            Veiculo v = tabela.getSelectionModel().getSelectedItem();
            if (v != null) {
                lista.remove(v);
                try {
                    VeiculoDAO.salvar(lista);
                    atualizarTabela();
                    alertInfo("Veículo excluído com sucesso!");
                } catch (Exception ex) {
                    alert("Erro ao excluir: " + ex.getMessage());
                }
            } else {
                alert("Selecione um veículo para excluir.");
            }
        });

        // --- Configuração da Tabela ---
        TableColumn<Veiculo, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(80);

        TableColumn<Veiculo, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colMarca.setPrefWidth(120);

        TableColumn<Veiculo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colModelo.setPrefWidth(150);

        TableColumn<Veiculo, String> colPlaca = new TableColumn<>("Placa");
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colPlaca.setPrefWidth(100);

        TableColumn<Veiculo, Integer> colAno = new TableColumn<>("Ano");
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colAno.setPrefWidth(60);

        TableColumn<Veiculo, String> colCor = new TableColumn<>("Cor");
        colCor.setCellValueFactory(new PropertyValueFactory<>("cor"));
        colCor.setPrefWidth(80);

        TableColumn<Veiculo, Double> colKm = new TableColumn<>("KM");
        colKm.setCellValueFactory(new PropertyValueFactory<>("km"));
        colKm.setPrefWidth(80);


        tabela.getColumns().addAll(colTipo, colMarca, colModelo, colPlaca, colAno, colCor, colKm);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajusta colunas automaticamente

        // --- Layout dos Componentes ---
        VBox form = new VBox(10); // Espaçamento de 10px entre os elementos do formulário
        form.setPadding(new Insets(20)); // Padding ao redor do formulário
        form.setAlignment(Pos.TOP_LEFT); // Alinha o formulário ao topo/esquerda
        form.getChildren().addAll(
                new Label("Tipo:"), cmbTipo,
                new Label("Marca:"), txtMarca,
                new Label("Modelo:"), txtModelo,
                new Label("Ano:"), txtAno,
                new Label("Cor:"), txtCor,
                new Label("Placa:"), txtPlaca,
                new Label("Km:"), txtKm,
                btnSalvar, btnExcluir
        );
        // Definir largura preferencial para os TextFields e ComboBox
        cmbTipo.setMaxWidth(200);
        txtMarca.setMaxWidth(200);
        txtModelo.setMaxWidth(200);
        txtAno.setMaxWidth(200);
        txtCor.setMaxWidth(200);
        txtPlaca.setMaxWidth(200);
        txtKm.setMaxWidth(200);

        // BorderPane como layout raiz para esta View
        BorderPane viewRoot = new BorderPane();
        viewRoot.setLeft(form); // Coloca o formulário à esquerda
        viewRoot.setCenter(tabela); // Coloca a tabela no centro

        // Opcional: Adicionar um título ou cabeçalho para a view de veículos
        Label titleLabel = new Label("Gerenciamento de Veículos");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER); // Centraliza o título
        viewRoot.setTop(titleLabel);

        return viewRoot; // Retorna o BorderPane que contém toda a UI do VeiculoView
    }

    // --- Métodos de Apoio ---
    private void carregarVeiculos() {
        try {
            lista = VeiculoDAO.carregar();
        } catch (Exception e) {
            lista = new ArrayList<>();
            System.err.println("Erro ao carregar veículos: " + e.getMessage());
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