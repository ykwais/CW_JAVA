package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegisterController extends BaseController{

    @FXML
    private Button enterButton, backButton;

    @FXML
    private TextField nameTextField;


    public void initialize() {
        enterButton.setOnAction(event -> clickEnterButton());
        backButton.setOnAction(event -> clickBackButton());
    }

    private void clickEnterButton() {
        switchScene("main_for_client.fxml");
    }

    private void clickBackButton() {
        switchScene("login.fxml");
    }


}
