package ru.otus.protobuf;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class NumberServer {
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        RemoteServiceImpl remoteService = new RemoteServiceImpl();

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
