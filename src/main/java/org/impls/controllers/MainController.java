package org.impls.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.impls.Client;

import java.io.IOException;


public class MainController {
    private Stage primaryStage;
    private Client client; // Ваш клиент для взаимодействия с сервером


//        private LoginController loginController;
//        private RegisterController registerController;
//        private MainViewController mainViewController;

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

            primaryStage.setScene(new Scene(root));
//            primaryStage.setMinWidth(800);
//            primaryStage.setMinHeight(600);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


//        public String sendRequestToServer(String request) {
//            //return client.sendRequest(request);
//
//            return null;
//        }
}

