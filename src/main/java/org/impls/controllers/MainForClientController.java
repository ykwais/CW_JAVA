package org.impls.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MainForClientController extends BaseController{
    @FXML
    private VBox contentVBox;

    @FXML
    private Button dtpButton;

    public void initialize() {
        //contentVBox.setAlignment(javafx.geometry.Pos.CENTER);

        dtpButton.setOnAction(event -> clickOnDTP());

        for (int i = 0; i < 15; i++) {
            contentVBox.getChildren().add(createItem("Car " + (i + 1)));
        }
    }

    private void clickOnDTP() {
        switchScene("accident.fxml");

    }

    private HBox createItem(String labelText) {

        HBox itemBox = new HBox(100);
        itemBox.setAlignment(javafx.geometry.Pos.CENTER);


        ImageView imageView = new ImageView();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/impls/img/img.png"))));
        //imageView.setStyle("-fx-background-color: gray");
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
//        imageView.setFitWidth(100);
//        imageView.setFitHeight(100);


        Label label = new Label(labelText);

        itemBox.setOnMouseClicked(event ->handleItemClick());


        itemBox.getChildren().addAll(imageView, label);
        return itemBox;
    }

    private void handleItemClick() {
        switchScene("automobile.fxml");
    }

}
