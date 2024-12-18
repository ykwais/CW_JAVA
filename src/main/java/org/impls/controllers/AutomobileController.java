package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AutomobileController extends BaseController {

    @FXML
    private Button backButton, selectButton;

    public void initialize() {
        backButton.setOnAction(event -> clickOnBack());
        selectButton.setOnAction(event -> clickOnSelect());
    }

    private void clickOnBack() {
        switchScene("main_for_client.fxml");
    }

    private void clickOnSelect() {
        switchScene("payment.fxml");
    }
}
