package org.impls;

public class VehicleDetails {
    private long vehicleId;
    private String brand;
    private String model;
    private double pricePerDay;
    private byte[] photoData;

    public VehicleDetails(long vehicleId, String brand, String model, double pricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }

    @Override
    public String toString() {
        return brand + " " + model + " (" + pricePerDay + " per day)";
    }
}