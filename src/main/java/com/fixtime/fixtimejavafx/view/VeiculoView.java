package com.fixtime.fixtimejavafx.view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.fixtime.fixtimejavafx.model.Veiculo;
import com.fixtime.fixtimejavafx.persistence.VeiculoDAO;

import java.util.ArrayList;

public class VeiculoView {
    private ArrayList<Veiculo> lista = new ArrayList<>();
    private TableView<Veiculo> tabela = new TableView<>();

    public void start(Stage stage) {
        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("carro", "moto", "caminhao", "van", "onibus");

        TextField txtMarca = new TextField();
        TextField txtModelo = new TextField();
        TextField txtAno = new TextField();
        TextField txtCor = new TextField();
        TextField txtPlaca = new TextField();
        TextField txtKm = new TextField();

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            if (cmbTipo.getValue() == null || txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() ||
                    txtAno.getText().isEmpty() || txtCor.getText().isEmpty() || txtPlaca.getText().isEmpty() || txtKm.getText().isEmpty()) {
                alert("Preencha todos os campos.");
                return;
            }
            try {
                Veiculo v = new Veiculo(lista.size() + 1, cmbTipo.getValue(), txtMarca.getText(), txtModelo.getText(),
                        Integer.parseInt(txtAno.getText()), txtCor.getText(), txtPlaca.getText(), Double.parseDouble(txtKm.getText()));
                lista.add(v);
                VeiculoDAO.salvar(lista);
                atualizarTabela();
                limparCampos(txtMarca, txtModelo, txtAno, txtCor, txtPlaca, txtKm);
                cmbTipo.setValue(null);
                alertInfo("Veículo salvo com sucesso!");
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

        TableColumn<Veiculo, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TableColumn<Veiculo, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        TableColumn<Veiculo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        TableColumn<Veiculo, String> colPlaca = new TableColumn<>("Placa");
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));

        tabela.getColumns().addAll(colTipo, colMarca, colModelo, colPlaca);

        VBox form = new VBox(5, new Label("Tipo:"), cmbTipo,
                new Label("Marca:"), txtMarca,
                new Label("Modelo:"), txtModelo,
                new Label("Ano:"), txtAno,
                new Label("Cor:"), txtCor,
                new Label("Placa:"), txtPlaca,
                new Label("Km:"), txtKm,
                btnSalvar, btnExcluir);

        BorderPane root = new BorderPane();
        root.setLeft(form);
        root.setCenter(tabela);

        carregarVeiculos();
        atualizarTabela();

        Scene scene = new Scene(root, 850, 450);
        stage.setTitle("Cadastro de Veículos");
        stage.setScene(scene);
        stage.show();
    }

    private void carregarVeiculos() {
        try {
            lista = VeiculoDAO.carregar();
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
