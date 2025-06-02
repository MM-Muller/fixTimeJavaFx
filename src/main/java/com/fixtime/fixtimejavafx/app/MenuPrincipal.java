package com.fixtime.fixtimejavafx.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.fixtime.fixtimejavafx.view.ClienteView;
import com.fixtime.fixtimejavafx.view.VeiculoView;
import com.fixtime.fixtimejavafx.view.OficinaView;

public class MenuPrincipal extends Application {

    private BorderPane rootLayout;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        rootLayout = new BorderPane();

        HBox menuButtons = createMenuButtons();
        rootLayout.setTop(menuButtons);

        showMainMenu();

        Scene scene = new Scene(rootLayout, 950, 650);
        primaryStage.setTitle("Sistema FixTime");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createMenuButtons() {
        Button btnCliente = new Button("Gerenciar Clientes");
        btnCliente.setOnAction(e -> showClienteView());
        btnCliente.setStyle("-fx-font-size: 14px; -fx-padding: 8 15;");

        Button btnVeiculo = new Button("Gerenciar Veículos");
        btnVeiculo.setOnAction(e -> showVeiculoView());
        btnVeiculo.setStyle("-fx-font-size: 14px; -fx-padding: 8 15;");

        Button btnOficina = new Button("Gerenciar Oficinas");
        btnOficina.setOnAction(e -> showOficinaView());
        btnOficina.setStyle("-fx-font-size: 14px; -fx-padding: 8 15;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnVoltarMenu = new Button("Voltar ao Menu");
        btnVoltarMenu.setOnAction(e -> showMainMenu());
        btnVoltarMenu.setStyle("-fx-font-size: 14px; -fx-padding: 8 15; -fx-background-color: #f0f0f0;");

        HBox menuBar = new HBox(15, btnCliente, btnVeiculo, btnOficina, spacer, btnVoltarMenu);
        menuBar.setAlignment(Pos.CENTER_LEFT);
        menuBar.setPadding(new Insets(10, 20, 10, 20));
        menuBar.setStyle("-fx-background-color: #ADD8E6; -fx-border-color: #6495ED; -fx-border-width: 0 0 1 0;");

        return menuBar;
    }

    private void showClienteView() {
        ClienteView clienteView = new ClienteView();
        Parent clienteContent = clienteView.createView(); // Chama o método createView()

        rootLayout.setCenter(clienteContent);

        primaryStage.setTitle("Sistema FixTime - Gerenciar Clientes");
    }

    private void showVeiculoView() {
        VeiculoView veiculoView = new VeiculoView();
        Parent veiculoContent = veiculoView.createView(); // Chama o método createView()
        rootLayout.setCenter(veiculoContent);
        primaryStage.setTitle("Sistema FixTime - Gerenciar Veículos");
    }

    private void showOficinaView() {
        OficinaView oficinaView = new OficinaView();
        Parent oficinaContent = oficinaView.createView(); // Chama o método createView()
        rootLayout.setCenter(oficinaContent);
        primaryStage.setTitle("Sistema FixTime - Gerenciar Oficinas");
    }

    private void showMainMenu() {
        VBox mainMenuContent = new VBox(20);
        mainMenuContent.setAlignment(Pos.CENTER);
        mainMenuContent.setPadding(new Insets(50));
        mainMenuContent.setStyle("-fx-background-color: #F8F8FF;");

        Label welcomeLabel = new Label("Bem-vindo ao Sistema FixTime!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label instructionLabel = new Label("Utilize o menu acima para navegar entre as opções.");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");

        mainMenuContent.getChildren().addAll(welcomeLabel, instructionLabel);
        rootLayout.setCenter(mainMenuContent);
        primaryStage.setTitle("Sistema FixTime - Menu Principal");
    }

    public static void main(String[] args) {
        launch(args);
    }
}