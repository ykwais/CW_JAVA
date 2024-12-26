package org.impls;


import auth.Rental;
import auth.ServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {

    private final ManagedChannel channel;
    private final ServiceGrpc.ServiceStub asyncStub;

    public Client() {
        this.channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();
        this.asyncStub = ServiceGrpc.newStub(channel);
    }

    public void sendSelectRequest(long userID, long vehicleID, String startTime, String endTime, StreamObserver<Rental.SelectAutoResponse> responseObserver) {
        Rental.SelectAutoRequest request = Rental.SelectAutoRequest.newBuilder()
                .setUserId(userID)
                .setVehicleId(vehicleID)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .build();
        asyncStub.selectAuto(request, responseObserver);
    }

    public void sendGetUserBookingsRequest(long userId, StreamObserver<Rental.UserBookingsResponse> responseObserver) {

        Rental.UserBookingsRequest request = Rental.UserBookingsRequest.newBuilder()
                .setUserId(userId)
                .build();


        asyncStub.getUserBookings(request, responseObserver);
    }

    public void sendCancelBooking(long userID, long vehicleId, StreamObserver<Rental.CancelBookingResponse> responseObserver) {
        Rental.CancelBookingRequest request = Rental.CancelBookingRequest.newBuilder()
                .setUserId(userID)
                .setVehicleId(vehicleId)
                .build();
        asyncStub.cancelBooking(request, responseObserver);
    }

    public void sendRegisterRequest(String login, String password, String email, String real_name, StreamObserver<Rental.RegisterResponse> responseObserver) {
        Rental.RegisterRequest request = Rental.RegisterRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .setEmail(email)
                .setRealName(real_name)
                .build();
        asyncStub.register(request, responseObserver);
    }

    public void sendLoginRequest(String login, String password, StreamObserver<Rental.LoginResponse> responseObserver) {
        Rental.LoginRequest request = Rental.LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build();
        asyncStub.login(request, responseObserver);
    }

    public void listPhotos(StreamObserver<Rental.ListPhotosResponse> responseObserver) {
        asyncStub.listPhotos(Rental.EmptyRequest.newBuilder().build(), responseObserver);
    }

    public void getPhotosAutmobile(long id, StreamObserver<Rental.PhotosOfAutomobileResponse> responseObserver) {
        asyncStub.photosOfAutomobile(Rental.PhotosOfAutomobileRequest.newBuilder().setId(id).build(), responseObserver);
    }

    public void photosForMainScreen(String dateBegin, String dateEnd, StreamObserver<Rental.PhotosForMainScreenResponse> responseObserver) {
        System.out.println(dateBegin);
        System.out.println(dateEnd);
        Rental.PhotosForMainScreenRequest request = Rental.PhotosForMainScreenRequest.newBuilder()
                .setDateBegin(dateBegin)
                .setDateEnd(dateEnd)
                .build();
        asyncStub.photosForMainScreen(request, responseObserver);
    }


    public void shutdown() {
        channel.shutdownNow();
    }

}