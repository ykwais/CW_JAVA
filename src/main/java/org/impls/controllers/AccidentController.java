package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AccidentController extends BaseController{
    @FXML
    private Button  backButton, sendButton;

    public void initialize() {
        backButton.setOnAction(e -> clickOnBack());
    }

    private void clickOnBack() {
        switchScene("main_for_client.fxml");
    }
}
