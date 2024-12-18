package org.impls.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
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

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AutomobileController extends BaseController {

    @FXML
    private Button backButton, selectButton;

    @FXML
    private ListView<ImageView> listView;

//    private Stage fullScreenStage;

    private Stage imageStage; // Для окна с изображением

    public void initialize() {
        backButton.setOnAction(event -> clickOnBack());
        selectButton.setOnAction(event -> clickOnSelect());

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                openImageInWindow(listView.getSelectionModel().getSelectedItem().getImage());
            }
        });

        for (int i = 0; i < 10; ++i) {
            ImageView imageView = createImageView();
            listView.getItems().add(imageView);
        }
    }

    private Image loadImage(String imagePath) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    private ImageView createImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);

        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                return loadImage("/org/impls/img/img.png");
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

        return imageView;

    }

    private void clickOnBack() {
        switchScene("main_for_client.fxml");
    }

    private void clickOnSelect() {
        switchScene("payment.fxml");
    }

//    private void openImageInWindow(Image image) {
//        ImageView imageView = new ImageView(image);
//        imageView.setFitWidth(600);
//        imageView.setPreserveRatio(true);
//
//        if (fullScreenStage == null) {
//            fullScreenStage = new Stage();
//            fullScreenStage.initModality(Modality.APPLICATION_MODAL);
//
//
//            Button closeButton = new Button("Close");
//            closeButton.setOnAction(event -> fullScreenStage.close());
//
//
//            VBox layout = new VBox();
//            layout.getChildren().addAll(imageView, closeButton);
//
//            Scene scene = new Scene(layout);
//            fullScreenStage.setScene(scene);
//        }
//
//        fullScreenStage.show();
//    }

    private void openImageInWindow(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);


        if (imageStage == null) {
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
        }

        imageView.setImage(image);

        imageStage.show();
    }
}
