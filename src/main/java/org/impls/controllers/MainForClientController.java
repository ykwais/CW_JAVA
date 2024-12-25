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
    private Button dtpButton;

    public void initialize() {
        dtpButton.setOnAction(event -> clickOnDTP());
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

                            // Если машина ещё не добавлена, создаём новый объект VehicleDetails
                            vehicleDetailsMap.putIfAbsent(vehicleId, new VehicleDetails(
                                    vehicleId,
                                    response.getBrand(),
                                    response.getModel(),
                                    response.getPricePerDay()
                            ));

                            // Получаем объект VehicleDetails для текущего автомобиля
                            VehicleDetails vehicleDetails = vehicleDetailsMap.get(vehicleId);

                            // Добавляем данные фотографии в объект VehicleDetails
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



    private void clickOnDTP() {
        switchScene("accident.fxml");

    }

    private Image loadImage(String imagePath) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    private HBox createItem(VehicleDetails vehicleDetails) {

        HBox itemBox = new HBox(20);
        itemBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Создаем метку с информацией о транспортном средстве
        String labelText = vehicleDetails.toString(); // Используем toString для строки вида "Brand Model (Price)"
        Label label = new Label(labelText);

        // Изображение
        ImageView imageView = new ImageView();

        // Обработчик кликов для элемента
        itemBox.setOnMouseClicked(event -> handleItemClick(vehicleDetails.getVehicleId()));

        // Асинхронная загрузка изображения
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                return new Image(new ByteArrayInputStream(vehicleDetails.getPhotoData()));
            }
        };

        // Успешная загрузка изображения
        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            Platform.runLater(() -> {
                imageView.setImage(image);
                imageView.setFitHeight(200); // Высота изображения
                imageView.setFitWidth(300);  // Ширина изображения
                imageView.setPreserveRatio(true); // Сохраняем пропорции
            });
        });

        // Обработка ошибок загрузки изображения
        loadImageTask.setOnFailed(event -> {
            System.err.println("Ошибка загрузки изображения для ID: " + vehicleDetails.getVehicleId());
        });

        // Запуск задачи через пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(loadImageTask);
        executor.shutdown();

        // Добавляем метку и изображение в HBox
        itemBox.getChildren().addAll(imageView, label);
        return itemBox;
    }

    private void handleItemClick(long id) {
        mainController.id_current_vehicle = id;
        switchScene("automobile.fxml");
    }

//    private HBox createItem(VehicleDetails details) {
//
//        HBox itemBox = new HBox(20); // Установим расстояние между элементами
//        itemBox.setAlignment(javafx.geometry.Pos.CENTER);
//
//        // Создаем метку с информацией о транспортном средстве
//        String labelText = details.getBrand() + " " + details.getModel() + " - " + details.getPricePerDay() + "/day";
//        Label label = new Label(labelText);
//
//        // Изображение
//        ImageView imageView = new ImageView();
//
//        // Обработчик кликов для элемента
//        itemBox.setOnMouseClicked(event -> handleItemClick(details.getVehicleId()));
//
//        // Асинхронная загрузка изображения
//        Task<Image> loadImageTask = new Task<>() {
//            @Override
//            protected Image call() {
//                return new Image(new ByteArrayInputStream(details.getPhotoData()));
//            }
//        };
//
//        // Успешная загрузка изображения
//        loadImageTask.setOnSucceeded(event -> {
//            Image image = loadImageTask.getValue();
//            Platform.runLater(() -> {
//                imageView.setImage(image);
//                imageView.setFitHeight(200); // Высота изображения
//                imageView.setFitWidth(300);  // Ширина изображения
//                imageView.setPreserveRatio(true); // Сохраняем пропорции
//            });
//        });
//
//        // Обработка ошибок загрузки изображения
//        loadImageTask.setOnFailed(event -> {
//            System.err.println("Ошибка загрузки изображения для ID: " + details.getVehicleId());
//        });
//
//        // Запуск задачи через пул потоков
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        executor.submit(loadImageTask);
//        executor.shutdown();
//
//        // Добавляем метку и изображение в HBox
//        itemBox.getChildren().addAll(imageView, label);
//        return itemBox;
//    }
//




    //    public void initialize() {
//        //contentVBox.setAlignment(javafx.geometry.Pos.CENTER);
//
//        dtpButton.setOnAction(event -> clickOnDTP());
//        Platform.runLater(() -> {
//
//            List<Rental.ListPhotosResponse> photos = new ArrayList<>();
//
//            mainController.getClient().listPhotos(new StreamObserver<Rental.ListPhotosResponse>() {
//                private Map<String, ByteArrayOutputStream> photoChunks = new HashMap<>();
//
//                @Override
//                public void onNext(Rental.ListPhotosResponse response) {
//                    String photoName = response.getPhotoName();
//                    ByteString chunk = response.getChunk();
//
//
//                    photoChunks.computeIfAbsent(photoName, k -> new ByteArrayOutputStream());
//
//                    try {
//                        // Добавляем данные в поток
//                        photoChunks.get(photoName).write(chunk.toByteArray());
//                    } catch (IOException e) {
//                        System.err.println("Error writing chunk for photo: " + photoName);
//                    }
//                }
//
//
//                @Override
//                public void onError(Throwable throwable) {
//                    System.err.println("Error during list photos request: " + throwable.getMessage());
//                }
//
//                @Override
//                public void onCompleted() {
//                    System.out.println("List photos request completed.");
//
//
//                    Platform.runLater(() -> {
//
//                        for (Map.Entry<String, ByteArrayOutputStream> entry : photoChunks.entrySet()) {
//                            String photoName = entry.getKey();
//                            byte[] photoData = entry.getValue().toByteArray();
//
//
//                            HBox item = createItem(photoName, photoData);
//                            contentListView.getItems().add(item);
//                        }
//                    });
//                }
//            });
//        });
//    }

//    private HBox createItem(String labelText, byte[] byteArray) {
//
//        HBox itemBox = new HBox(100);
//        itemBox.setAlignment(javafx.geometry.Pos.CENTER);
//
//        ImageView imageView = new ImageView();
//        Label label = new Label(labelText);
//
//        itemBox.setOnMouseClicked(event -> handleItemClick());
//
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
//        itemBox.getChildren().addAll(imageView, label);
//        return itemBox;
//    }



}
