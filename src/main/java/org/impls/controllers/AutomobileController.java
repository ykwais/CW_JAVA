package org.impls.controllers;

import auth.Rental;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;


public class AutomobileController extends BaseController {

    @FXML
    private Button backButton, selectButton;

    @FXML
    private ListView<ImageView> listView;

    private List<ImageItem> listItems = new ArrayList<>();

    private Stage imageStage; // Для окна с изображением

    public void initialize() {
        backButton.setOnAction(event -> clickOnBack());
        selectButton.setOnAction(event -> clickOnSelect());

        Platform.runLater(() -> {
            mainController.getClient().getPhotosAutmobile(mainController.id_current_vehicle, new StreamObserver<Rental.PhotosOfAutomobileResponse>() {
                private Map<String, ByteArrayOutputStream> photoChunks = new HashMap<>();

                @Override
                public void onNext(Rental.PhotosOfAutomobileResponse response) {
                    String photoName = response.getPhotoName();
                    ByteString chunk = response.getChunk();


                    photoChunks.computeIfAbsent(photoName, k -> new ByteArrayOutputStream());

                    try {
                        photoChunks.get(photoName).write(chunk.toByteArray());
                    } catch (IOException e) {
                        System.err.println("Error writing chunk for photo: " + photoName);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error during PhotosOfAutomobile request: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("PhotosOfAutomobile request completed.");

                    Platform.runLater(() -> {
                        for (Map.Entry<String, ByteArrayOutputStream> entry : photoChunks.entrySet()) {
                            byte[] photoData = entry.getValue().toByteArray();


                            ImageItem imageItem = new ImageItem(photoData);
                            listView.getItems().add(imageItem.getImageView());
                            listItems.add(imageItem);
                        }
                    });
                }
            });
        });

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {

                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                if (selectedIndex != -1) {

                    ImageItem selectedImageItem = listItems.get(selectedIndex);


                    byte[] photoData = selectedImageItem.getPhotoData();
                    System.out.println("открытие изображения " + selectedIndex);
                    openImageInWindow(new ByteArrayInputStream(photoData));
                }
            }
        });
    }



    private void clickOnBack() {
        switchScene("main_for_client.fxml");
    }

    private void clickOnSelect() {
        long userId = mainController.id_user;
        long vehicleId = mainController.id_current_vehicle;
        String startTime = mainController.startDate;
        String endTime = mainController.endDate;

        System.out.println("user id: " + userId);
        System.out.println("vehicle id: " + vehicleId);
        System.out.println("start date: " + startTime);
        System.out.println("end date: " + endTime);

        mainController.getClient().sendSelectRequest(userId, vehicleId, startTime, endTime, new StreamObserver<Rental.SelectAutoResponse>() {

            @Override
            public void onNext(Rental.SelectAutoResponse response) {
                System.out.println("booking ID: " + response.getVehicleId());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error during booking request: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("select request completed.");
            }
        });

        switchScene("main_for_client.fxml");


    }

    private void openImageInWindow(ByteArrayInputStream inputStream) {
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView();




            imageStage = new Stage();
            imageStage.initModality(Modality.APPLICATION_MODAL);
            imageStage.setTitle("Просмотр изображения");

            Button closeButton = new Button("Close");
            closeButton.setOnAction(event -> imageStage.close());

            StackPane pane = new StackPane();
            pane.getChildren().add(imageView);

            VBox layout = new VBox();
            layout.getChildren().addAll(pane, closeButton);

            Scene scene = new Scene(layout);
            imageStage.setScene(scene);

            imageStage.setMinWidth(200);
            imageStage.setMinHeight(200);
            imageStage.setMaxWidth(1600);
            imageStage.setMaxHeight(1000);

            imageView.fitWidthProperty().bind(scene.widthProperty());
            imageView.fitHeightProperty().bind(scene.heightProperty().subtract(closeButton.getHeight()));


        imageView.setImage(image);

        imageStage.show();
    }
}
