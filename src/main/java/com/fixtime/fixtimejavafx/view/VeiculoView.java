package com.fixtime.fixtimejavafx.view;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import com.fixtime.fixtimejavafx.model.Veiculo;
import com.fixtime.fixtimejavafx.model.Cliente;
import com.fixtime.fixtimejavafx.persistence.VeiculoDAO;
import com.fixtime.fixtimejavafx.persistence.ClienteDAO;

import java.time.Year;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VeiculoView {
    private ArrayList<Veiculo> lista = new ArrayList<>();
    private TableView<Veiculo> tabela = new TableView<>();

    public Parent createView() {

        carregarVeiculos();
        atualizarTabela();

        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("carro", "moto", "caminhao", "van", "onibus");
        cmbTipo.setPromptText("Selecione o Tipo");

        TextField txtMarca = new TextField();
        txtMarca.setPromptText("Marca do veículo");

        TextField txtModelo = new TextField();
        txtModelo.setPromptText("Modelo do veículo");

        TextField txtAno = new TextField();
        txtAno.setPromptText("Ano (entre 1900 e 2030)");
        txtAno.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,4}")) {
                return change;
            }
            return null;
        }));

        TextField txtCor = new TextField();
        txtCor.setPromptText("Cor do veículo");
        txtCor.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        }));

        TextField txtPlaca = new TextField();
        txtPlaca.setPromptText("Placa: ABC1234 ou ABC1D23");
        txtPlaca.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().toUpperCase();
            if (newText.matches("[A-Z0-9]{0,7}")) {
                return change;
            }
            return null;
        }));

        TextField txtKm = new TextField();
        txtKm.setPromptText("Quilometragem");
        txtKm.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        }));

        TextField txtCpfDono = new TextField();
        txtCpfDono.setPromptText("CPF do dono");
        txtCpfDono.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,11}")) {
                return change;
            }
            return null;
        }));

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            if (cmbTipo.getValue() == null || txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() ||
                    txtAno.getText().isEmpty() || txtCor.getText().isEmpty() || txtPlaca.getText().isEmpty() || 
                    txtKm.getText().isEmpty() || txtCpfDono.getText().isEmpty()) {
                alert("Preencha todos os campos.");
                return;
            }

            String cpfDono = txtCpfDono.getText();
            if (!cpfDono.matches("\\d{11}")) {
                alert("CPF inválido. Deve conter exatamente 11 dígitos numéricos.");
                return;
            }

            // Verificar se o cliente existe
            try {
                ArrayList<Cliente> clientes = ClienteDAO.carregar();
                boolean clienteExiste = clientes.stream()
                    .anyMatch(c -> c.getCpf().equals(cpfDono));
                
                if (!clienteExiste) {
                    alert("Não existe cliente cadastrado com este CPF.");
                    return;
                }
            } catch (Exception ex) {
                alert("Erro ao verificar CPF: " + ex.getMessage());
                return;
            }

            String placa = txtPlaca.getText().toUpperCase();
            Pattern placaPattern = Pattern.compile("^[A-Z]{3}\\d[A-Z]\\d{2}$|^[A-Z]{3}\\d{4}$");
            Matcher placaMatcher = placaPattern.matcher(placa);
            if (!placaMatcher.matches()) {
                alert("Formato de placa inválido. Use ABC1234 (antiga) ou ABC1D23 (Mercosul).");
                return;
            }

            String cor = txtCor.getText().trim();
            if (!cor.matches("[a-zA-Z\\s]+")) {
                alert("Cor inválida. Deve conter apenas letras e espaços.");
                return;
            }

            if (!txtAno.getText().matches("\\d{4}")) {
                alert("Ano inválido. Deve conter 4 dígitos numéricos.");
                return;
            }

            boolean sucessoNoSalvamento = false;
            try {
                int ano = Integer.parseInt(txtAno.getText());
                double km = Double.parseDouble(txtKm.getText());

                if (ano < 1900 || ano > 2030) {
                    alert("Ano inválido. Deve estar entre 1900 e 2030.");
                    return;
                }

                Veiculo v = new Veiculo(lista.size() + 1, cmbTipo.getValue(), txtMarca.getText(), txtModelo.getText(),
                        ano, cor, placa, km, cpfDono);
                lista.add(v);
                VeiculoDAO.salvar(lista);
                sucessoNoSalvamento = true;
                alertInfo("Veículo salvo com sucesso!");
            } catch (NumberFormatException ex) {
                alert("Erro de formato: Ano ou KM devem ser números válidos.");
            } catch (Exception ex) {
                alert("Erro ao salvar: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                if (sucessoNoSalvamento) {
                    atualizarTabela();
                    limparCampos(txtMarca, txtModelo, txtAno, txtCor, txtPlaca, txtKm, txtCpfDono);
                    cmbTipo.setValue(null);
                }
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
        colTipo.setPrefWidth(80);

        TableColumn<Veiculo, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colMarca.setPrefWidth(115);

        TableColumn<Veiculo, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colModelo.setPrefWidth(115);

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

        TableColumn<Veiculo, String> colCpfDono = new TableColumn<>("CPF do Dono");
        colCpfDono.setCellValueFactory(new PropertyValueFactory<>("cpfDono"));
        colCpfDono.setPrefWidth(120);

        tabela.getColumns().addAll(colTipo, colMarca, colModelo, colPlaca, colAno, colCor, colKm, colCpfDono);
        tabela.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tabela.setPrefWidth(800);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_LEFT);
        form.getChildren().addAll(
                new Label("Tipo:"), cmbTipo,
                new Label("Marca:"), txtMarca,
                new Label("Modelo:"), txtModelo,
                new Label("Placa:"), txtPlaca,
                new Label("Ano:"), txtAno,
                new Label("Cor:"), txtCor,
                new Label("Km:"), txtKm,
                new Label("CPF do Dono:"), txtCpfDono,
                btnSalvar, btnExcluir
        );
        cmbTipo.setMaxWidth(250);
        txtMarca.setMaxWidth(250);
        txtModelo.setMaxWidth(250);
        txtPlaca.setMaxWidth(250);
        txtAno.setMaxWidth(250);
        txtCor.setMaxWidth(250);
        txtKm.setMaxWidth(250);
        txtCpfDono.setMaxWidth(250);

        BorderPane viewRoot = new BorderPane();
        viewRoot.setLeft(form);
        viewRoot.setCenter(tabela);

        Label titleLabel = new Label("Gerenciamento de Veículos");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        viewRoot.setTop(titleLabel);

        return viewRoot;
    }

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