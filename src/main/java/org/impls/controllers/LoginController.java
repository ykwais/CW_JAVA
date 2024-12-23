package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

        //long serverResponse = mainController.getClient().sendLoginRequest(login, password);

        //System.out.println("Сервер ответил: " + serverResponse);

        switchScene("main_for_client.fxml");

    }

    private void switchToRegisterScene() {
        switchScene("register.fxml");
    }
}
