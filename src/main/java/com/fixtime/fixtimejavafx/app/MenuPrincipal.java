package com.fixtime.fixtimejavafx.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.fixtime.fixtimejavafx.view.ClienteView;
import com.fixtime.fixtimejavafx.view.VeiculoView;
import com.fixtime.fixtimejavafx.view.OficinaView;

public class MenuPrincipal extends Application {
    @Override
    public void start(Stage stage) {
        Button btnCliente = new Button("Gerenciar Clientes");
        btnCliente.setOnAction(e -> new ClienteView().start(new Stage()));

        Button btnVeiculo = new Button("Gerenciar Veículos");
        btnVeiculo.setOnAction(e -> new VeiculoView().start(new Stage()));

        Button btnOficina = new Button("Gerenciar Oficinas");
        btnOficina.setOnAction(e -> new OficinaView().start(new Stage()));

        VBox root = new VBox(10, btnCliente, btnVeiculo, btnOficina);
        Scene scene = new Scene(root, 300, 250);
        stage.setTitle("Menu Principal - FixTime");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
