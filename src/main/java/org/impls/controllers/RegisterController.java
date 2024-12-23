package org.impls.controllers;

import auth.Rental;
import io.grpc.stub.StreamObserver;
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

            mainController.getClient().sendLoginRequest(login, password, new StreamObserver<Rental.RegisterResponse>() {
                @Override
                public void onNext(Rental.RegisterResponse registerResponse) {
                    System.out.println("User ID: " + registerResponse.getUserId());
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

            //System.out.println("Сервер ответил: " + serverResponse);

            switchScene("main_for_client.fxml");


    }

    private void clickBackButton() {
        switchScene("login.fxml");
    }


}
