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

        switchScene("payment.fxml");
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


//public class AutomobileController extends BaseController {
//
//    @FXML
//    private Button backButton, selectButton;
//
//    @FXML
//    private ListView<ImageView> listView;
//
//    private List<ImageItem> listItems = new ArrayList<>();
//
//
//
//    private Stage imageStage; // Для окна с изображением
//
//    public void initialize() {
//        backButton.setOnAction(event -> clickOnBack());
//        selectButton.setOnAction(event -> clickOnSelect());
//
//
//
//
//        Platform.runLater(() -> {
//            mainController.getClient().getPhotosAutmobile(mainController.id_current_vehicle, new StreamObserver<Rental.PhotosOfAutomobileResponse>() {
//                private Map<String, ByteArrayOutputStream> photoChunks = new HashMap<>();
//
//                @Override
//                public void onNext(Rental.PhotosOfAutomobileResponse response) {
//                    String photoName = response.getPhotoName();
//                    ByteString chunk = response.getChunk();
//
//                    // Сохраняем части данных в HashMap
//                    photoChunks.computeIfAbsent(photoName, k -> new ByteArrayOutputStream());
//
//                    try {
//                        photoChunks.get(photoName).write(chunk.toByteArray());
//                    } catch (IOException e) {
//                        System.err.println("Error writing chunk for photo: " + photoName);
//                    }
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    System.err.println("Error during PhotosOfAutomobile request: " + throwable.getMessage());
//                }
//
//                @Override
//                public void onCompleted() {
//                    System.out.println("PhotosOfAutomobile request completed.");
//
//                    Platform.runLater(() -> {
//                        for (Map.Entry<String, ByteArrayOutputStream> entry : photoChunks.entrySet()) {
//                            byte[] photoData = entry.getValue().toByteArray();
//
//                            // Создаем ImageItem и добавляем в ListView
//                            ImageItem imageItem = new ImageItem(photoData);
//                            listView.getItems().add(imageItem.getImageView()); // добавляем ImageItem, а не ImageView
//                            listItems.add(imageItem);
//                        }
//                    });
//                }
//            });
//        });
//
//
//
//
//        listView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 1) {
//                // Получаем выбранный элемент
//                ImageView selectedImageView = listView.getSelectionModel().getSelectedItem();
//                if (selectedImageView != null) {
//                    // Находим соответствующий ImageItem по photoData
//                    for (ImageItem item : listItems) {
//                        if (item.getImageView() == selectedImageView) {
//                            byte[] photoData = item.getPhotoData();  // Получаем данные фотографии
//                            openImageInWindow(new Image(new ByteArrayInputStream(photoData)));  // Открываем изображение
//                            break;
//                        }
//                    }
//                }
//            }
//        });
//
//    }
//
//
//
//
//    private Image loadImage(String imagePath) {
//        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
//    }
//
//    private ImageView createImageView(byte[] photoData) {
//        ImageView imageView = new ImageView();
//
//        // Устанавливаем изображение напрямую
//        try {
//            Image image = new Image(new ByteArrayInputStream(photoData));
//            imageView.setImage(image);
//            imageView.setFitWidth(300);
//            imageView.setFitHeight(300);
//            imageView.setUserData(photoData); // Сохраняем photoData для последующего использования
//        } catch (Exception e) {
//            System.err.println("Ошибка загрузки изображения: " + e.getMessage());
//        }
//
//        return imageView;
//    }
//
//
//
//
//    private void clickOnBack() {
//        switchScene("main_for_client.fxml");
//    }
//
//    private void clickOnSelect() {
//        switchScene("payment.fxml");
//    }
//
//
//
//    private void openImageInWindow(Image image) {
//        ImageView imageView = new ImageView(image);
//        imageView.setPreserveRatio(true);
//
//
//        if (imageStage == null) {
//            imageStage = new Stage();
//            imageStage.initModality(Modality.APPLICATION_MODAL);
//            imageStage.setTitle("Просмотр изображения");
//
//
//            Button closeButton = new Button("Close");
//            closeButton.setOnAction(event -> imageStage.close());
//
//
//            StackPane pane = new StackPane();
//            pane.getChildren().add(imageView);
//
//
//            VBox layout = new VBox();
//            layout.getChildren().addAll(pane, closeButton);
//
//
//            Scene scene = new Scene(layout);
//            imageStage.setScene(scene);
//
//
//            imageStage.setMinWidth(200);
//            imageStage.setMinHeight(200);
//            imageStage.setMaxWidth(1600);
//            imageStage.setMaxHeight(1000);
//
//
//            imageView.fitWidthProperty().bind(scene.widthProperty());
//            imageView.fitHeightProperty().bind(scene.heightProperty().subtract(closeButton.getHeight()));
//        }
//
//        imageView.setImage(image);
//
//        imageStage.show();
//    }
//}


//        Platform.runLater(() -> {
//            mainController.getClient().getPhotosAutmobile(mainController.id_current_vehicle, new StreamObserver<Rental.PhotosOfAutomobileResponse>() {
//                private Map<String, ByteArrayOutputStream> photoChunks = new HashMap<>();
//
//                @Override
//                public void onNext(Rental.PhotosOfAutomobileResponse response) {
//                    String photoName = response.getPhotoName();
//                    ByteString chunk = response.getChunk();
//
//                    photoChunks.computeIfAbsent(photoName, k -> new ByteArrayOutputStream());
//
//                    try {
//                        photoChunks.get(photoName).write(chunk.toByteArray());
//                    } catch (IOException e) {
//                        System.err.println("Error writing chunk for photo: " + photoName);
//                    }
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    System.err.println("Error during PhotosOfAutomobile request: " + throwable.getMessage());
//                }
//
//                @Override
//                public void onCompleted() {
//                    System.out.println("PhotosOfAutomobile request completed.");
//
//                    Platform.runLater(() -> {
//                        for (Map.Entry<String, ByteArrayOutputStream> entry : photoChunks.entrySet()) {
//                            byte[] photoData = entry.getValue().toByteArray();
//                            ImageView imageView = createImageView(photoData);
//                            imageView.setUserData(photoData);  // Устанавливаем уникальные данные для каждого элемента
//                            listView.getItems().add(imageView);
//                        }
//                    });
//
////                    Platform.runLater(() -> {
////                        for (Map.Entry<String, ByteArrayOutputStream> entry : photoChunks.entrySet()) {
////                            byte[] photoData = entry.getValue().toByteArray();
////
////                            // Создаём ImageView и прикрепляем фото
////                            ImageView imageView = new ImageView();
////                            try {
////                                Image image = new Image(new ByteArrayInputStream(photoData));
////                                imageView.setImage(image);
////                                imageView.setFitWidth(300);
////                                imageView.setFitHeight(300);
////
////                                // Прикрепляем уникальные данные
////                                imageView.setUserData(photoData);
////
////                            } catch (Exception e) {
////                                System.err.println("Ошибка при создании изображения: " + e.getMessage());
////                                continue;
////                            }
////
////                            // Добавляем ImageView в ListView
////                            listView.getItems().add(imageView);
////                        }
////                    });
//                }
//            });
//        });

// Обработка кликов


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

//    private ImageView createImageView(byte[] byteArray) {
//        ImageView imageView = new ImageView();
//        imageView.setFitWidth(300);
//        imageView.setFitHeight(300);
//
//        Task<Image> loadImageTask = new Task<>() {
//            @Override
//            protected Image call() {
//                return new Image(new ByteArrayInputStream(byteArray));
//            }
//        };
//
//        loadImageTask.setOnSucceeded(event -> {
//            Image image = loadImageTask.getValue();
//            Platform.runLater(() -> {
//                imageView.setImage(image);
//                imageView.setFitHeight(200);
//                imageView.setFitWidth(300);
//            });
//        });
//
//        loadImageTask.setOnFailed(event -> {
//            System.err.println("Ошибка загрузки изображения");
//        });
//
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        executor.submit(loadImageTask);
//        executor.shutdown();
//
//        return imageView;
//
//    }


//    public void initialize() {
//        backButton.setOnAction(event -> clickOnBack());
//        selectButton.setOnAction(event -> clickOnSelect());
//
//
//
//        List<Rental.PhotosOfAutomobileResponse> photos = new ArrayList<>();
//
//        Platform.runLater(() -> {
//                    mainController.getClient().getPhotosAutmobile(mainController.id_current_vehicle, new StreamObserver<Rental.PhotosOfAutomobileResponse>() {
//                        private Map<String, ByteArrayOutputStream> photoChunks = new HashMap<>();
//
//                        @Override
//                        public void onNext(Rental.PhotosOfAutomobileResponse response) {
//                            String photoName = response.getPhotoName();
//                            ByteString chunk = response.getChunk();
//
//
//                            photoChunks.computeIfAbsent(photoName, k -> new ByteArrayOutputStream());
//
//                            try {
//                                photoChunks.get(photoName).write(chunk.toByteArray());
//                            } catch (IOException e) {
//                                System.err.println("Error writing chunk for photo: " + photoName);
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            System.err.println("Error during PhotosOfAutomobile request: " + throwable.getMessage());
//                        }
//
//                        @Override
//                        public void onCompleted() {
//                            System.out.println("PhotosOfAutomobile request completed.");
//
//                            Platform.runLater(() -> {
//                                listView.getItems().clear();
//                                for (Map.Entry<String, ByteArrayOutputStream> entry : photoChunks.entrySet()) {
//                                    byte[] photoData = entry.getValue().toByteArray();
//
//                                    // Создаём ImageView с уникальными данными
//                                    ImageView imageView = new ImageView();
//                                    try {
//                                        Image image = new Image(new ByteArrayInputStream(photoData));
//                                        imageView.setImage(image);
//                                        imageView.setFitWidth(300);
//                                        imageView.setFitHeight(300);
//
//                                        // Ассоциируем уникальные данные
//                                        imageView.setUserData(photoData);
//                                    } catch (Exception e) {
//                                        System.err.println("Ошибка при создании изображения: " + e.getMessage());
//                                    }
//
//                                    // Добавляем ImageView в список
//                                    listView.getItems().add(imageView);
//                                }
//                            });
//                        }
//
//
////                        @Override
////                        public void onCompleted() {
////                            System.out.println("PhotosOfAutomobile request completed.");
////
////                            Platform.runLater(() -> {
////                                for (Map.Entry<String, ByteArrayOutputStream> entry : photoChunks.entrySet()) {
////                                    byte[] photoData = entry.getValue().toByteArray();
////                                    ImageView imageView = createImageView(photoData);
////                                    imageView.setUserData(photoData);
////                                    listView.getItems().add(imageView);
////                                }
////                            });
////                        }
//                    });
//                });
//
//        listView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 1) {
//                ImageView selectedImageView = (ImageView) listView.getSelectionModel().getSelectedItem();
//                if (selectedImageView != null) {
//                    // Извлекаем уникальные данные байтов для этого ImageView
//                    byte[] photoData = (byte[]) selectedImageView.getUserData();
//                    if (photoData != null) {
//                        openImageInWindow(new Image(new ByteArrayInputStream(photoData)));
//                    } else {
//                        System.err.println("Нет данных для выбранного изображения.");
//                    }
//                }
//            }
//        });
//
//
//
//
////        listView.setOnMouseClicked(event -> {
////            if (event.getClickCount() == 1) {
////                ImageView selectedImageView = (ImageView) listView.getSelectionModel().getSelectedItem();
////                if (selectedImageView != null) {
////                    byte[] photoData = (byte[]) selectedImageView.getUserData();
////                    openImageInWindow(new Image(new ByteArrayInputStream(photoData)));
////                }
////            }
////        });
//
//
//    }


//        listView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 1) {
//                ImageView selectedImageView = (ImageView) listView.getSelectionModel().getSelectedItem();
//                if (selectedImageView != null) {
//                    // Получаем уникальные данные для выбранной фотографии
//                    byte[] photoData = (byte[]) selectedImageView.getUserData();
//                    openImageInWindow(new Image(new ByteArrayInputStream(photoData)));
//                }
//            }
//        });


//        listView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 1) {
//                // Получаем выбранный элемент списка
//                ImageView selectedImageView = (ImageView) listView.getSelectionModel().getSelectedItem();
//                if (selectedImageView != null) {
//                    // Извлекаем данные для выбранного изображения
//                    byte[] photoData = (byte[]) selectedImageView.getUserData();
//                    if (photoData != null) {
//                        // Открываем окно с изображением
//                        openImageInWindow(new Image(new ByteArrayInputStream(photoData)));
//                    } else {
//                        System.err.println("Нет данных для выбранного изображения.");
//                    }
//                }
//            }
//        });