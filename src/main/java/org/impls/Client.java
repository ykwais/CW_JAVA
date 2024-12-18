package org.impls;


import com.example.grpc.Rental;
import com.example.grpc.GreetingGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {

    private final ManagedChannel channel;
    private final GreetingGrpc.GreetingBlockingStub stub;

    public Client() {
        this.channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();
        this.stub = GreetingGrpc.newBlockingStub(channel);
    }

    public String sendLoginRequest(String login, String password) {
        Rental.HelloRequest request = Rental.HelloRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build();
        Rental.HelloResponse response = stub.login(request);
        return response.getMessage();
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