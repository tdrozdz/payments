package com.tdrozdz.payments.verification.grpc;

import com.tdrozdz.payments.api.*;
import com.tdrozdz.payments.api.IsRegistrationFinishedResponse.RegistrationStep;
import com.tdrozdz.payments.api.VerificationGrpc.VerificationImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class VerificationGrpcService extends VerificationImplBase {


  @Override
  public void isRegistrationFinished(
      IsRegistrationFinishedRequest request,
      StreamObserver<IsRegistrationFinishedResponse> responseObserver) {
    var reply = IsRegistrationFinishedResponse.newBuilder().setStep(RegistrationStep.FINISHED).build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  @Override
  public void isBlocked(
      IsBlockedRequest request, StreamObserver<IsBlockedResponse> responseObserver) {
    var reply = IsBlockedResponse.newBuilder().setBlocked(false).build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  @Override
  public void isSanctioned(
      IsSanctionedRequest request, StreamObserver<IsSanctionedResponse> responseObserver) {
    var reply = IsSanctionedResponse.newBuilder().setSanctioned(false).build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }
}
