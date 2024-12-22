package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegisterController extends BaseController{

    @FXML
    private Button enterButton, backButton;

    @FXML
    private TextField nameTextField, passTextField;


    public void initialize() {
        enterButton.setOnAction(event -> clickEnterButton());
        backButton.setOnAction(event -> clickBackButton());
    }

    private void clickEnterButton() {


            String login = nameTextField.getText();
            String password = passTextField.getText();

            long serverResponse = mainController.getClient().sendLoginRequest(login, password);

            System.out.println("Сервер ответил: " + serverResponse);

            switchScene("main_for_client.fxml");


    }

    private void clickBackButton() {
        switchScene("login.fxml");
    }


}
