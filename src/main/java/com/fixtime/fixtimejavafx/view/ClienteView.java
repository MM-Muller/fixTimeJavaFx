package com.fixtime.fixtimejavafx.view;

import javafx.scene.Parent; // Importação para o tipo Parent que será retornado
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.collections.FXCollections; // Necessário para FXCollections.observableArrayList
import javafx.geometry.Insets; // Para padding
import javafx.geometry.Pos;   // Para alinhamento
import com.fixtime.fixtimejavafx.model.Cliente;
import com.fixtime.fixtimejavafx.persistence.ClienteDAO;

import java.util.ArrayList;

public class ClienteView {
    private ArrayList<Cliente> lista = new ArrayList<>();
    private TableView<Cliente> tabela = new TableView<>();

    // O método principal agora retorna um Parent, não mais um void e não recebe Stage
    public Parent createView() { // O nome do método pode ser qualquer um, createView é comum

        // Carrega os clientes e atualiza a tabela ao criar a view
        carregarClientes();
        atualizarTabela(); // Isso garante que a tabela já vem populada

        // --- Componentes da UI (sem alteração lógica, apenas organização) ---
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
                // A lógica de ID deve ser mais robusta em um ambiente de produção (ex: auto-incremento do DB)
                // Para este exemplo, lista.size() + 1 serve.
                Cliente cliente = new Cliente(lista.size() + 1, txtNome.getText(), txtCPF.getText(),
                        txtTelefone.getText(), txtEmail.getText(), txtSenha.getText());
                lista.add(cliente);
                ClienteDAO.salvar(lista); // Salva a lista completa
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
                    ClienteDAO.salvar(lista); // Salva a lista atualizada
                    atualizarTabela();
                    alertInfo("Cliente excluído com sucesso!");
                } catch (Exception ex) {
                    alert("Erro ao excluir: " + ex.getMessage());
                }
            } else {
                alert("Selecione um cliente para excluir.");
            }
        });

        // --- Configuração da Tabela (mantida) ---
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
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajusta colunas automaticamente

        // --- Layout dos Componentes ---
        // VBox para o formulário
        VBox form = new VBox(10); // Espaçamento de 10px entre os elementos do formulário
        form.setPadding(new Insets(20)); // Padding ao redor do formulário
        form.setAlignment(Pos.TOP_LEFT); // Alinha o formulário ao topo/esquerda (padrão para formulários)
        form.getChildren().addAll(lblNome, txtNome, lblCPF, txtCPF, lblTelefone, txtTelefone,
                lblEmail, txtEmail, lblSenha, txtSenha, btnSalvar, btnExcluir);
        // Definir largura preferencial para os TextFields para consistência visual
        txtNome.setMaxWidth(200);
        txtCPF.setMaxWidth(200);
        txtTelefone.setMaxWidth(200);
        txtEmail.setMaxWidth(200);
        txtSenha.setMaxWidth(200);

        // HBox para os botões do formulário, se quiser que fiquem lado a lado
        // HBox buttonsBox = new HBox(10, btnSalvar, btnExcluir);
        // buttonsBox.setAlignment(Pos.CENTER_LEFT); // Alinha os botões dentro do HBox
        // form.getChildren().addAll(buttonsBox); // Adiciona o HBox de botões ao formulário

        // BorderPane como layout raiz para esta View
        BorderPane viewRoot = new BorderPane();
        viewRoot.setLeft(form); // Coloca o formulário à esquerda
        viewRoot.setCenter(tabela); // Coloca a tabela no centro

        // Opcional: Adicionar um título ou cabeçalho para a view de clientes
        Label titleLabel = new Label("Gerenciamento de Clientes");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10px;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER); // Centraliza o título
        viewRoot.setTop(titleLabel);

        return viewRoot; // Retorna o BorderPane que contém toda a UI do ClienteView
    }

    // --- Métodos de Apoio (mantidos, mas sem 'start' nem 'Scene' ou 'Stage') ---

    private void carregarClientes() {
        try {
            lista = ClienteDAO.carregar();
        } catch (Exception e) {
            // Se houver erro ao carregar (ex: arquivo não existe), inicializa a lista vazia
            lista = new ArrayList<>();
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
            // alert("Não foi possível carregar os clientes. Criando uma nova lista."); // Opcional: alertar o usuário
        }
    }

    private void atualizarTabela() {
        // Usa FXCollections.observableArrayList para garantir que as atualizações sejam observáveis pela TableView
        tabela.setItems(FXCollections.observableArrayList(lista));
    }

    private void limparCampos(TextField... campos) {
        for (TextField campo : campos) campo.clear();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.setHeaderText(null); // Remove o cabeçalho padrão
        a.setTitle("Erro");    // Define o título da caixa de diálogo
        a.showAndWait();
    }

    private void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.setHeaderText(null); // Remove o cabeçalho padrão
        a.setTitle("Informação"); // Define o título da caixa de diálogo
        a.showAndWait();
    }
}