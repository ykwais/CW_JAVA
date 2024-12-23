package org.impls;


import auth.Rental;
import auth.ServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {

    private final ManagedChannel channel;
    private final ServiceGrpc.ServiceBlockingStub stub;

    public Client() {
        this.channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();
        this.stub = ServiceGrpc.newBlockingStub(channel);
    }

    public long sendLoginRequest(String login, String password) {
        Rental.RegisterRequest request = Rental.RegisterRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build();
        Rental.RegisterResponse response = stub.register(request);
        return response.getUserId();
    }

    public List<Rental.ListPhotosResponse> listPhotos() {
        List<Rental.ListPhotosResponse> photos = new ArrayList<>();
        Iterator<Rental.ListPhotosResponse> responses = stub.listPhotos(Rental.EmptyRequest.newBuilder().build());
        while (responses.hasNext()) {
            photos.add(responses.next());
        }
        return photos;
    }



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