package org.impls;


import javafx.application.Application;
import javafx.stage.Stage;
import org.impls.controllers.MainController;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        Client client = new Client();
        MainController mainController = new MainController(primaryStage, client);
        mainController.loadScene("login.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}