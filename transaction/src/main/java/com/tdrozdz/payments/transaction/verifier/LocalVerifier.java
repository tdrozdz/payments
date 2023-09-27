package com.tdrozdz.payments.transaction.verifier;

import lombok.AllArgsConstructor;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.tdrozdz.payments.transaction.rest.OperationType;

@AllArgsConstructor
final class LocalVerifier implements Verifier {

  private final LocalCaller localCaller;

  @Override
  public ListenableFuture<Boolean> isClientVerified(String clientId, Integer amount, OperationType operationType) {
    return Futures.immediateFuture(localCaller.call(clientId, amount, operationType));
  }
}
