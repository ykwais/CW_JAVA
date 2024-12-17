package org.impls;


import javafx.application.Application;
import javafx.stage.Stage;
import org.impls.controllers.MainController;

public class Main extends Application {

//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("authorization_scene.fxml"));
//        Parent root = fxmlLoader.load();
//        Scene scene = new Scene(root);
//        //String css = Objects.requireNonNull(this.getClass().getResource("styles.css")).toExternalForm();
//        //scene.getStylesheets().add(css);
//        stage.setScene(scene);
//        stage.setTitle("ykwais Cw");
//        stage.setMinWidth(800);
//        stage.setMinHeight(600);
//        stage.show();
//
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }

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