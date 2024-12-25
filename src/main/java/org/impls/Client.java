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

//    public List<Rental.ListPhotosResponse> listPhotos() {
//        List<Rental.ListPhotosResponse> photos = new ArrayList<>();
//        Iterator<Rental.ListPhotosResponse> responses = stub.listPhotos(Rental.EmptyRequest.newBuilder().build());
//        while (responses.hasNext()) {
//            photos.add(responses.next());
//        }
//        return photos;
//    }



    public void shutdown() {
        channel.shutdownNow();
    }

//    public static void main(String[] args) {
//        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();
//
//        GreetingGrpc.GreetingBlockingStub stub = GreetingGrpc.newBlockingStub(channel);
//
//        Rental.HelloRequest req = Rental.HelloRequest.newBuilder()
//                .setLogin("exampleUser")
//                .setPassword("examplePassword")
//                .build();
//
//        Rental.HelloResponse response = stub.login(req);
//
//        System.out.println("Сервер ответил: " + response);
//
//        channel.shutdownNow();
//
//    }
}