package com.fixtime.fixtimejavafx.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.fixtime.fixtimejavafx.view.ClienteView;

public class MenuPrincipal extends Application {
    @Override
    public void start(Stage stage) {
        Button btnCliente = new Button("Gerenciar Clientes");
        btnCliente.setOnAction(e -> new ClienteView().start(new Stage()));

        VBox root = new VBox(10, btnCliente);
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("Menu Principal - FixTime");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}