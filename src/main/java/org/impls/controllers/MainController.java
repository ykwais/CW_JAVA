package org.impls.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.impls.Client;

import java.io.IOException;
import java.util.Objects;


public class MainController {
    private Stage primaryStage;
    private Client client;
    public String startDate;
    public String endDate;
    public long id_current_vehicle;
    public long id_user;



    public MainController(Stage primaryStage, Client client) {
        this.primaryStage = primaryStage;
        this.client = client;
    }

    public void loadScene(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();

            BaseController controller = loader.getController();
            controller.setMainController(this);

            Scene scene = new Scene(root);

            String css = Objects.requireNonNull(this.getClass().getResource("/org/impls/styles/login.css")).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    public Client getClient() {
        return client;
    }
}

