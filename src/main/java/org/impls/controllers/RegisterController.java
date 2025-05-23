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
    private TextField nameTextField, passTextField, emailTextField, realNameTextField;


    public void initialize() {
        enterButton.setOnAction(event -> clickEnterButton());
        backButton.setOnAction(event -> clickBackButton());
    }

    private void clickEnterButton() {


            String login = nameTextField.getText();
            String password = passTextField.getText();

            String email = emailTextField.getText();
            String realName = realNameTextField.getText();

            mainController.getClient().sendRegisterRequest(login, password, email, realName, new StreamObserver<Rental.RegisterResponse>() {
                private long userID;
                @Override
                public void onNext(Rental.RegisterResponse registerResponse) {
                    userID = registerResponse.getUserId();
                    mainController.id_user = userID;
                    if (mainController.id_user == 1) {
                        mainController.isAdmin = true;
                    } else {
                        mainController.isAdmin = false;
                    }
                    System.out.println("User ID: " + userID);
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error during login request: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Register request completed.");
                }
            });



            if (mainController.isAdmin) {
                switchScene("admin_view.fxml");
                return;
            }
            switchScene("data_picker.fxml");



    }

    private void clickBackButton() {
        switchScene("login.fxml");
    }


}
