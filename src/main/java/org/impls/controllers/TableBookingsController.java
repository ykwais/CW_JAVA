package org.impls.controllers;

import auth.Rental;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


public class TableBookingsController extends BaseController {

    @FXML
    private Button backButton;

    @FXML
    private TableView<BookingRow> tableView;

    @FXML
    private TableColumn<BookingRow, String> brandColumn;

    @FXML
    private TableColumn<BookingRow, String> modelColumn;

    @FXML
    private TableColumn<BookingRow, String> startDateColumn;

    @FXML
    private TableColumn<BookingRow, String> endDateColumn;

    @FXML
    private TableColumn<BookingRow, Void> actionColumn;

    public void initialize() {
        backButton.setOnAction(event -> handleBack());


        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));


        setupActionColumn();


        Platform.runLater(() -> {
            mainController.getClient().sendGetUserBookingsRequest(mainController.id_user, new StreamObserver<Rental.UserBookingsResponse>() {
                @Override
                public void onNext(Rental.UserBookingsResponse userBookingsResponse) {
                    Platform.runLater(() -> {
                        for (Rental.BookingInfo booking : userBookingsResponse.getBookingsList()) {
                            BookingRow bookingRow = new BookingRow(booking.getBrandName(), booking.getModelName(), booking.getDateBegin(), booking.getDateEnd());
                            bookingRow.setVehicleID(booking.getVehicleId());
                            tableView.getItems().add(bookingRow);
                        }
                    });
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error fetching bookings: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("GetUserBookings request completed.");
                }
            });
        });
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<BookingRow, Void> call(TableColumn<BookingRow, Void> param) {
                return new TableCell<>() {
                    private final Button cancelButton = new Button("Cancel");

                    {
                        cancelButton.setOnAction(event -> {
                            BookingRow booking = getTableView().getItems().get(getIndex());
                            handleCancelBooking(booking);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox box = new HBox(cancelButton);
                            setGraphic(box);
                        }
                    }
                };
            }
        });
    }

    private void handleCancelBooking(BookingRow booking) {
        System.out.println("Cancel booking: " + booking);

        long vehicle_id = booking.getVehicleID();
        long user_id = mainController.id_user;

        System.out.println("cancel user_id: " + user_id);
        System.out.println("cancel vehicle_id: " + vehicle_id);


        mainController.getClient().sendCancelBooking(user_id, vehicle_id, new StreamObserver<Rental.CancelBookingResponse>() {
            @Override
            public void onNext(Rental.CancelBookingResponse response) {
                Platform.runLater(() -> {
                    tableView.getItems().remove(booking);
                    System.out.println("Booking cancelled: ");
                });
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error cancelling booking: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Cancel booking request completed.");
            }
        });
    }

    private void handleBack() {
        switchScene("main_for_client.fxml");
    }

    public static class BookingRow {
        private long vehicleID;
        private final String brand;
        private final String model;
        private final String startDate;
        private final String endDate;

        public BookingRow(String brand, String model, String startDate, String endDate) {
            this.brand = brand;
            this.model = model;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public long getVehicleID() {
            return vehicleID;
        }

        public void setVehicleID(long vehicleID) {
            this.vehicleID = vehicleID;
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
    }
}


