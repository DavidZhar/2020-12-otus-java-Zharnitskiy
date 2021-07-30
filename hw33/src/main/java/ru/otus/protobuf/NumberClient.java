package ru.otus.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.RequestMessage;

import java.util.concurrent.atomic.AtomicLong;

public class NumberClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final AtomicLong number = new AtomicLong(0);
    private static final Logger log = LoggerFactory.getLogger(NumberClient.class);

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();


        RemoteServiceGrpc.RemoteServiceStub newStub = RemoteServiceGrpc.newStub(channel);
        newStub.getNumbers(RequestMessage.newBuilder().setFirstValue(0).setLastValue(30).build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(NumberMessage numberMessage) {
                        log.info("RECEIVED NUMBER FROM SERVER: " + numberMessage.getNumber());
                        number.set(numberMessage.getNumber());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t);
                    }

                    @Override
                    public void onCompleted() {
                        log.info("\n\nUSED ALL NUMBERS FROM SERVER");
                    }
                });

        log.info("STARTING LOOP");
        long currentNumber = 0;
        long currentNumberFromServer = 0;
        for (int i = 0; i < 50; i++) {
            currentNumber++;
            long currentFromServer = number.get();
            if (currentNumberFromServer != currentFromServer) {
                currentNumberFromServer = currentFromServer;
                currentNumber += currentFromServer;
            }
            log.info("CURRENT NUMBER: " + currentNumber);
            Thread.sleep(1000);
        }
        log.info("LOOP ENDED");

        channel.shutdown();
    }

}
