package com.tdrozdz.payments.transaction.verifier;

import com.tdrozdz.payments.transaction.rest.OperationType;

/**
 * Defines a contract for making local(blocking) call with a client identifier and returning a
 * boolean result indicating if a client is verified user.
 */
interface LocalCaller {

  /**
   * Executes blocking call to check if a client is verified.
   *
   * @return {@code true} if the verifiaction is positive, {@code false} otherwise.
   */
  Boolean call(String clientId, Integer amount, OperationType operationType);
}
