package org.impls.controllers;

public class BaseController {
    protected MainController mainController;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void switchScene(String fxmlFilePath) {
        mainController.loadScene(fxmlFilePath);
    }

    public void send(String text) {
        mainController.sendRequestToServer(text);
    }
}
