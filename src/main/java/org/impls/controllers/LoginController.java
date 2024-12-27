package org.impls.controllers;

import auth.Rental;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.concurrent.CountDownLatch;

public class LoginController extends BaseController {

    @FXML
    private Button loginButton, registerButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordTextField;

    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> switchToRegisterScene());
    }

    private void handleLogin() {

        String login = loginTextField.getText();
        String password = passwordTextField.getText();






        mainController.getClient().sendLoginRequest(login, password, new StreamObserver<Rental.LoginResponse>() {
            @Override
            public void onNext(Rental.LoginResponse loginResponse) {
                mainController.id_user = loginResponse.getUserId();
                //mainController.isAdmin = mainController.id_user == 1;
                System.out.println("User ID: " + mainController.id_user);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error during login request: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
//                System.out.println("Login request completed.");
                System.out.println("!!!!!!!!!!!!! " + mainController.id_user);

                Platform.runLater(() -> {
                    if (mainController.id_user == 1) {
                        switchScene("admin_view.fxml");
                    } else {
                        switchScene("data_picker.fxml");
                    }
                });
            }
        });





    }

    private void switchToRegisterScene() {
//        if (mainController.isAdmin) {
//            switchScene("admin_view.fxml");
//            return;
//        }
        switchScene("register.fxml");
    }
}
