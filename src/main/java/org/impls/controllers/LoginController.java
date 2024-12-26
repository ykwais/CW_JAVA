package org.impls.controllers;

import auth.Rental;
import io.grpc.stub.StreamObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.concurrent.CountDownLatch;

public class LoginController extends BaseController {

    @FXML
    private Button loginButton, registerButton;

    @FXML
    private TextField loginTextField, passwordTextField;

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
                System.out.println("User ID: " + loginResponse.getUserId());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error during login request: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Login request completed.");
            }
        });


        switchScene("data_picker.fxml");



    }

    private void switchToRegisterScene() {
        switchScene("register.fxml");
    }
}
