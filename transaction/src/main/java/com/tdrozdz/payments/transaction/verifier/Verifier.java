package com.tdrozdz.payments.transaction.verifier;

import com.google.common.util.concurrent.ListenableFuture;
import com.tdrozdz.payments.transaction.rest.OperationType;

/**
 * Defines the contract for verifying the client's identity or status.
 */
public interface Verifier {

  /**
   * Asynchronously checks if a client is verified.
   *
   * @param clientId
   *     The unique identifier of the client to be verified.
   * @param amount
   *     The amount of cash required on account.
   * @param operationType
   *     The operationType defined by {@code OperationType} enum.
   *
   * @return A {@code ListenableFuture} representing the verification result
   */
  ListenableFuture<Boolean> isClientVerified(
      String clientId, Integer amount, OperationType operationType);
}
