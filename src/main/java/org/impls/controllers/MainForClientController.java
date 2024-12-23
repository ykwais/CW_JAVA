package org.impls.controllers;

import auth.Rental;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainForClientController extends BaseController{
    @FXML
    private ListView<HBox> contentListView;

    @FXML
    private Button dtpButton;

    public void initialize() {
        //contentVBox.setAlignment(javafx.geometry.Pos.CENTER);

        dtpButton.setOnAction(event -> clickOnDTP());
        Platform.runLater(() -> {
            // Создаем пустой список для хранения фотографий
            List<Rental.ListPhotosResponse> photos = new ArrayList<>();

            // Асинхронно загружаем фотографии
            mainController.getClient().listPhotos(new StreamObserver<Rental.ListPhotosResponse>() {
                @Override
                public void onNext(Rental.ListPhotosResponse listPhotosResponse) {
                    // Добавляем полученную фотографию в список
                    photos.add(listPhotosResponse);
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error during list photos request: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("List photos request completed.");

                    // После завершения загрузки, обновляем UI
                    Platform.runLater(() -> {
                        // Добавляем фотографии в список View
                        for (Rental.ListPhotosResponse photo : photos) {
                            HBox item = createItem(photo.getPhotoName(), photo.getChunk().toByteArray());
                            contentListView.getItems().add(item);
                        }
                    });
                }
            });
        });
    }

    private void clickOnDTP() {
        switchScene("accident.fxml");

    }

    private Image loadImage(String imagePath) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    private HBox createItem(String labelText, byte[] byteArray) {

        HBox itemBox = new HBox(100);
        itemBox.setAlignment(javafx.geometry.Pos.CENTER);

        ImageView imageView = new ImageView();
        Label label = new Label(labelText);

        itemBox.setOnMouseClicked(event -> handleItemClick());


        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                return new Image(new ByteArrayInputStream(byteArray));
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            Platform.runLater(() -> {
                imageView.setImage(image);
                imageView.setFitHeight(200);
                imageView.setFitWidth(300);
            });
        });

        loadImageTask.setOnFailed(event -> {
            System.err.println("Ошибка загрузки изображения");
        });

        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(loadImageTask);
        executor.shutdown();

        itemBox.getChildren().addAll(imageView, label);
        return itemBox;
    }

    private void handleItemClick() {
        switchScene("automobile.fxml");
    }

}
