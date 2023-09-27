package com.tdrozdz.payments.transaction.verifier;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Defines a contract for making asynchronous remote calls to verify a client. It is used by
 * {@RemoteVerifier}. The call is done with a request of type {@code S} and receiving a response of
 * type {@code T}.
 *
 * @param <T>
 *     The type of response objects received from the remote call.
 * @param <S>
 *     The type of request objects sent to the remote service.
 */
interface RemoteCaller<T, S> {

  /**
   * Initiates an asynchronous remote call with the provided request and returns a
   * {@code ListenableFuture} representing the response of type {@code T}.
   *
   * @param request
   *     The request object to be sent to the remote service.
   *
   * @return A {@code ListenableFuture} representing the response of type {@code T} for the remote
   *     call.
   */
  ListenableFuture<T> call(S request);
}
