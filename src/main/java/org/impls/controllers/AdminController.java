package org.impls.controllers;

import auth.Rental;
import io.grpc.stub.StreamObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdminController extends BaseController {
    @FXML
    private Button  viewUsersButton, viewInfoButton, backupButton, backButton, rollBackButton;

    @FXML
    private TextField adminTextField;

    public void initialize() {
        viewInfoButton.setOnAction(event -> clickOnInfo());
        backButton.setOnAction(e -> clickOnBack());
        viewUsersButton.setOnAction(e -> clickOnUsers());

        backupButton.setOnAction(e -> sendDoBackUpRequest());
        rollBackButton.setOnAction(e -> sendDoRollBackRequest());
    }

    private void sendDoBackUpRequest() {
        mainController.getClient().sendDoBackUpRequest(new StreamObserver<Rental.DoBackUpResponse>() {
            @Override
            public void onNext(Rental.DoBackUpResponse value) {
                System.out.println("Backup completed: ");
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error during backup: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Backup request completed.");
            }
        });
    }

    private void sendDoRollBackRequest() {
        mainController.getClient().sendDoRollBackRequest(new StreamObserver<Rental.DoRollBackResponse>() {
            @Override
            public void onNext(Rental.DoRollBackResponse value) {
                System.out.println("Rollback completed: ");
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error during rollback: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Rollback request completed.");
            }
        });
    }

    private void clickOnInfo() {
        switchScene("table_info_for_admin.fxml");
    }

    private void clickOnBack() {
        switchScene("register.fxml");
    }

    private void clickOnUsers() {
        switchScene("table_users_for_admin.fxml");
    }
}
