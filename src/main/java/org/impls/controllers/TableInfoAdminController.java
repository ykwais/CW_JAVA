package org.impls.controllers;

import auth.Rental;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableInfoAdminController extends BaseController {

    @FXML
    private Button backButton;

    @FXML
    private TableView<DataForAdminRow> tableView;

    @FXML
    private TableColumn<DataForAdminRow, String> loginColumn;

    @FXML
    private TableColumn<DataForAdminRow, String> emailColumn;

    @FXML
    private TableColumn<DataForAdminRow, String> realNameColumn;

    @FXML
    private TableColumn<DataForAdminRow, String> brandColumn;

    @FXML
    private TableColumn<DataForAdminRow, String> modelColumn;

    @FXML
    private TableColumn<DataForAdminRow, String> startDateColumn;

    @FXML
    private TableColumn<DataForAdminRow, String> endDateColumn;

    @FXML
    private TableColumn<DataForAdminRow, Double> priceColumn;

    public void initialize() {

        backButton.setOnAction(event -> handleBack());


        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        realNameColumn.setCellValueFactory(new PropertyValueFactory<>("realName"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));


        Platform.runLater(() -> {
            mainController.getClient().sendGetInfoAdminRequest(new StreamObserver<Rental.GetDataForAdminResponse>() {
                @Override
                public void onNext(Rental.GetDataForAdminResponse response) {
                    Platform.runLater(() -> {
                        for (Rental.DataForAdmin adminData : response.getDataForAdminList()) {
                            DataForAdminRow row = new DataForAdminRow(
                                    adminData.getLogin(),
                                    adminData.getUserEmail(),
                                    adminData.getRealName(),
                                    adminData.getBrand(),
                                    adminData.getModel(),
                                    adminData.getDataStart(),
                                    adminData.getDataEnd(),
                                    adminData.getPricePerDay()
                            );
                            tableView.getItems().add(row);
                        }
                    });
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error fetching admin data: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("GetAdminData request completed.");
                }
            });
        });
    }

    private void handleBack() {
        switchScene("admin_view.fxml");
    }

    public static class DataForAdminRow {
        private final String login;
        private final String email;
        private final String realName;
        private final String brand;
        private final String model;
        private final String startDate;
        private final String endDate;
        private final double pricePerDay;

        public DataForAdminRow(String login, String email, String realName, String brand, String model, String startDate, String endDate, double pricePerDay) {
            this.login = login;
            this.email = email;
            this.realName = realName;
            this.brand = brand;
            this.model = model;
            this.startDate = startDate;
            this.endDate = endDate;
            this.pricePerDay = pricePerDay;
        }

        public String getLogin() {
            return login;
        }

        public String getEmail() {
            return email;
        }

        public String getRealName() {
            return realName;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public double getPricePerDay() {
            return pricePerDay;
        }
    }
}
