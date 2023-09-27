package com.tdrozdz.payments.transaction.verifier;

import com.tdrozdz.payments.api.IsRegistrationFinishedResponse;
import com.tdrozdz.payments.api.IsRegistrationFinishedResponse.RegistrationStep;
import com.tdrozdz.payments.api.VerificationGrpc.VerificationFutureStub;
import com.tdrozdz.payments.transaction.model.AccountRepository;
import com.tdrozdz.payments.transaction.rest.OperationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class VerifierConfiguration {

  @Bean
  VerifyService provideVerifyService(List<Verifier> verifierList) {
    return new VerifyService(verifierList);
  }

  @Bean
  Verifier provideSanctionedVerifier(VerificationFutureStub stub) {
    return new RemoteVerifier<>(
        request -> stub.isBlocked(request),
        new IsBlockedRequestBuilder(),
        (response) -> !response.getBlocked());
  }

  @Bean
  Verifier provideAccountBlockeddVerifier(VerificationFutureStub stub) {
    return new RemoteVerifier<>(
        request -> stub.isSanctioned(request),
        new IsSanctionedRequestBuilder(),
        (response) -> !response.getSanctioned());
  }

  @Bean
  Verifier provideRegistrationVerifier(VerificationFutureStub stub) {
    return new RemoteVerifier<>(
        request -> stub.isRegistrationFinished(request),
        new IsRegistrationFinishedRequestBuilder(),
        (ResponseConverter<IsRegistrationFinishedResponse>) response -> RegistrationStep.FINISHED.equals(
            response.getStep()));
  }

  @Bean
  Verifier provideAmountChecker(AccountRepository accountRepository) {
    return new LocalVerifier((clientId, amount, operationType) -> {
      if (OperationType.DEPOSITE.equals(operationType)) {
        return true;
      }
      return accountRepository.findById(clientId).getCashAmount() >= amount;
    });
  }
}
