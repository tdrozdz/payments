package com.tdrozdz.payments.transaction.verifier;

import lombok.AllArgsConstructor;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.tdrozdz.payments.transaction.rest.OperationType;

@AllArgsConstructor
final class RemoteVerifier<T, S> implements Verifier {

  private final RemoteCaller<T, S> remoteCaller;
  private final RequestBuilder<S> requestBuilder;
  private final ResponseConverter<T> responseConverter;

  @Override
  public ListenableFuture<Boolean> isClientVerified(
      String clientId, Integer amoount, OperationType operationType) {
    return Futures.transform(
        remoteCaller.call(requestBuilder.setClientId(clientId).build()),
        responseConverter::convertResponse,
        MoreExecutors.directExecutor());
  }
}
