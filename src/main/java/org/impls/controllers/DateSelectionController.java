package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class DateSelectionController extends BaseController {

    @FXML
    DatePicker startDatePicker, endDatePicker;

    @FXML
    Button enterButton, backButton;

    public void initialize() {
        enterButton.setOnAction(event -> handleClickEnter());
        backButton.setOnAction(e -> handleClickBack());
    }

    private void handleClickEnter() {
        String startDate = startDatePicker.getValue().toString();
        String endDate = endDatePicker.getValue().toString();

        mainController.startDate = startDate;
        mainController.endDate = endDate;


        switchScene("main_for_client.fxml");
    }

    private void handleClickBack() {
        switchScene("login.fxml");
    }

}
