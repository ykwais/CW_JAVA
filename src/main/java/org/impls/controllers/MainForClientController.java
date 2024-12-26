package org.impls.controllers;
import org.impls.VehicleDetails;

import auth.Rental;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainForClientController extends BaseController{
    @FXML
    private ListView<HBox> contentListView;

    @FXML
    private Button bookingButton, changeDataButton, refreshButton, exitButton;

    public void initialize() {

        bookingButton.setOnAction(event -> clickOnBooking());
        changeDataButton.setOnAction(event -> clickOnChange());
        refreshButton.setOnAction(e -> clickOnRefresh());
        exitButton.setOnAction(event -> clickOnExit());
        Platform.runLater(() -> {

            mainController.getClient().photosForMainScreen(
                    mainController.startDate,
                    mainController.endDate,
                    new StreamObserver<Rental.PhotosForMainScreenResponse>() {
                        private Map<Long, VehicleDetails> vehicleDetailsMap = new HashMap<>();

                        @Override
                        public void onNext(Rental.PhotosForMainScreenResponse response) {
                            long vehicleId = response.getVehicleId();
                            ByteString chunk = response.getChunk();


                            vehicleDetailsMap.putIfAbsent(vehicleId, new VehicleDetails(
                                    vehicleId,
                                    response.getBrand(),
                                    response.getModel(),
                                    response.getPricePerDay()
                            ));


                            VehicleDetails vehicleDetails = vehicleDetailsMap.get(vehicleId);


                            try {
                                ByteArrayOutputStream photoStream = new ByteArrayOutputStream();
                                photoStream.write(chunk.toByteArray());
                                byte[] photoData = photoStream.toByteArray();
                                vehicleDetails.setPhotoData(photoData);
                            } catch (IOException e) {
                                System.err.println("Error writing chunk for vehicle ID: " + vehicleId);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            System.err.println("Error during photos for main screen request: " + throwable.getMessage());
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("Photos for main screen request completed.");

                            Platform.runLater(() -> {

                                for (VehicleDetails vehicleDetails : vehicleDetailsMap.values()) {
                                    HBox item = createItem(vehicleDetails);
                                    contentListView.getItems().add(item);
                                }
                            });
                        }
                    });
        });
    }



    private void clickOnBooking() {
        switchScene("table_bookings.fxml");
    }

    private void clickOnChange() {
        switchScene("data_picker_2.fxml");
    }

    private void clickOnRefresh() {
        switchScene("main_for_client.fxml");
    }

    private void clickOnExit() {
        switchScene("login.fxml");
    }

//    private Image loadImage(String imagePath) {
//        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
//    }

    private HBox createItem(VehicleDetails vehicleDetails) {

        HBox itemBox = new HBox(20);
        itemBox.setAlignment(javafx.geometry.Pos.CENTER);


        String labelText = vehicleDetails.toString();
        Label label = new Label(labelText);


        ImageView imageView = new ImageView();


        itemBox.setOnMouseClicked(event -> handleItemClick(vehicleDetails.getVehicleId()));


        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                return new Image(new ByteArrayInputStream(vehicleDetails.getPhotoData()));
            }
        };


        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            Platform.runLater(() -> {
                imageView.setImage(image);
                imageView.setFitHeight(200);
                imageView.setFitWidth(300);
                imageView.setPreserveRatio(true);
            });
        });


        loadImageTask.setOnFailed(event -> {
            System.err.println("Ошибка загрузки изображения для ID: " + vehicleDetails.getVehicleId());
        });


        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(loadImageTask);
        executor.shutdown();


        itemBox.getChildren().addAll(imageView, label);
        return itemBox;
    }

    private void handleItemClick(long id) {
        mainController.id_current_vehicle = id;
        switchScene("automobile.fxml");
    }

}
