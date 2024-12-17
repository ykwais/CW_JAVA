package org.impls;


import com.example.grpc.Rental;
import com.example.grpc.RentalServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
//    public static void main(String[] args) {
//        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();
//
//        RentalServiceGrpc.RentalServiceBlockingStub stub = RentalServiceGrpc.newBlockingStub(channel);
//
//        Rental.Empty req = Rental.Empty.newBuilder().build();
//
//        Rental.CarListResponse response = stub.getAvailableCars(req);
//
//        System.out.println(response);
//
//        channel.shutdownNow();
//
//    }
}