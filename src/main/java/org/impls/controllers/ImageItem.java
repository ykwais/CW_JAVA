package org.impls.controllers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class ImageItem {
    private byte[] photoData;
    private ImageView imageView;

    public ImageItem(byte[] photoData) {

        this.photoData = Arrays.copyOf(photoData, photoData.length); // Создаем копию массива
        this.imageView = createImageView(this.photoData);  // Передаем уникальные данные для создания ImageView
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public ImageView getImageView() {
        return imageView;
    }

    private ImageView createImageView(byte[] photoData) {
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(photoData)));
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        return imageView;
    }
}

