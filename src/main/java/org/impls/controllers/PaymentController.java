package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PaymentController extends BaseController{

    @FXML
    private Button backButton, payButton;

    public void initialize() {
        backButton.setOnAction(e -> clickOnBack());
    }

    private void clickOnBack() {
        switchScene("automobile.fxml");
    }
}
