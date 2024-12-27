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
    public boolean isAdmin = false;



    public MainController(Stage primaryStage, Client client) {
        this.primaryStage = primaryStage;
        this.client = client;
    }

    public void loadScene(String fxmlFilePath) {
        try {
            String title = getStageTitle(fxmlFilePath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();

            BaseController controller = loader.getController();
            controller.setMainController(this);

            Scene scene = new Scene(root);

            String css = Objects.requireNonNull(this.getClass().getResource("/org/impls/styles/login.css")).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    public Client getClient() {
        return client;
    }

    private String  getStageTitle(String fxmlName) {
        String[] parts = fxmlName.split("\\.");
        switch (parts[0]) {
            case "login":
                return "Login";
            case "register":
                return "Registration";
            case "admin_view" :
                return "Specially for admin";
            case "automobile" :
                return "Select this Automobile!";
            case "data_picker" :
                return "Choose Data for booking";
            case "data_picker_2" :
                return "Choose Data for booking";
            case "main_for_client" :
                return "Choose your Automobile";
            case "table_bookings" :
                return "Your bookings";
            case "table_info_for_admin":
                return "Information for admin";
            case "table_users_for_admin":
                return "Information about users";
            default:
                return parts[0];

        }
    }
}

