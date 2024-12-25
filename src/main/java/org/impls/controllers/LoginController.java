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

       // final long[] userIDs = new long[1];


        mainController.getClient().sendLoginRequest(login, password, new StreamObserver<Rental.LoginResponse>() {
            @Override
            public void onNext(Rental.LoginResponse loginResponse) {
                //userIDs[0] = loginResponse.getUserId();
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



//        long userID = userIDs[0];
//
//        System.out.println("!!!!!!!!!!!!!!!!User ID: " + userID);


        switchScene("data_picker.fxml");






        //switchScene("main_for_client.fxml");

    }

    private void switchToRegisterScene() {
        switchScene("register.fxml");
    }
}
