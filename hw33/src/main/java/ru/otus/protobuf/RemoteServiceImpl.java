package ru.otus.protobuf;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.RemoteServiceGrpc;
import ru.otus.protobuf.generated.NumberMessage;
import ru.otus.protobuf.generated.RequestMessage;

public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {
    Logger log = LoggerFactory.getLogger(RemoteServiceImpl.class);

    @Override
    public void getNumbers(RequestMessage request, StreamObserver<NumberMessage> responseObserver) {
        for (long i = request.getFirstValue(); i < request.getLastValue(); i++) {
            log.info("SENDING NUMBER: " + i);
            responseObserver.onNext(numberToMessage(i));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
        }
        responseObserver.onCompleted();
    }

    private NumberMessage numberToMessage(long number) {
        return NumberMessage.newBuilder()
                .setNumber(number)
                .build();
    }
}
