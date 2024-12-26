package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdminController extends BaseController {
    @FXML
    private Button  viewUsersButton, viewInfoButton, backupButton, backButton;

    @FXML
    private TextField adminTextField;

    public void initialize() {
        viewInfoButton.setOnAction(event -> clickOnInfo());
        backButton.setOnAction(e -> clickOnBack());
    }

    private void clickOnInfo() {
        switchScene("table_info_for_admin.fxml");
    }

    private void clickOnBack() {
        switchScene("register.fxml");
    }
}
