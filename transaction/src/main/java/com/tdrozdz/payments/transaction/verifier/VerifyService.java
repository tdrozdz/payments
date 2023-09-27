package com.tdrozdz.payments.transaction.verifier;

import lombok.AllArgsConstructor;

import com.google.common.util.concurrent.Futures;
import com.tdrozdz.payments.transaction.rest.OperationType;

import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class VerifyService {

  private final List<Verifier> verifiers;

  public Boolean verify(String id, Integer amount, OperationType operationType) {
    try {
      for (Boolean checkerResponse : Futures
          .allAsList(verifiers.stream().map(ch -> ch.isClientVerified(id, amount, operationType)).toList())
          .get()) {
        if (Boolean.FALSE.equals(checkerResponse)) {
          return false;
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
}
