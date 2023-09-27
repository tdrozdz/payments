package com.example.transaction;

import com.google.common.util.concurrent.Futures;
import com.tdrozdz.payments.api.IsBlockedResponse;
import com.tdrozdz.payments.api.IsRegistrationFinishedResponse;
import com.tdrozdz.payments.api.IsRegistrationFinishedResponse.RegistrationStep;
import com.tdrozdz.payments.api.IsSanctionedResponse;
import com.tdrozdz.payments.api.VerificationGrpc.VerificationFutureStub;
import com.tdrozdz.payments.transaction.TransactionApplication;
import com.tdrozdz.payments.transaction.model.Account;
import com.tdrozdz.payments.transaction.model.AccountRepository;
import com.tdrozdz.payments.transaction.rest.AccountService;
import com.tdrozdz.payments.transaction.rest.OperationNotAllowed;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TransactionApplication.class)
@EnableRetry
class TransactionApplicationTest {

  private static final String TEST_CLIENT_ID = "testClientId";

  @Autowired
  private AccountService accountService;

  @MockBean
  private AccountRepository accountRepository;

  @MockBean
  private LockProvider lockProvider;

  @MockBean
  private VerificationFutureStub stub;

  @BeforeEach
  void setUp() {
    when(stub.isRegistrationFinished(any())).thenReturn(Futures.immediateFuture(
        IsRegistrationFinishedResponse.newBuilder().setStep(RegistrationStep.FINISHED).build()));
    when(stub.isBlocked(any())).thenReturn(
        Futures.immediateFuture(IsBlockedResponse.newBuilder().setBlocked(false).build()));
    when(stub.isSanctioned(any())).thenReturn(
        Futures.immediateFuture(IsSanctionedResponse.newBuilder().setSanctioned(false).build()));
  }

  @Test
  public void testRetryableDeposit() {

    Account testAccount = new Account();
    testAccount.setCashAmount(100);
    SimpleLock simpleLock = mock(SimpleLock.class);
    when(accountRepository.findById(anyString())).thenReturn(testAccount);
    when(lockProvider.lock(any())).thenReturn(Optional.empty()).thenReturn(Optional.of(simpleLock));

    Account result = accountService.deposite(TEST_CLIENT_ID, 50);

    verify(accountRepository).findById(TEST_CLIENT_ID);
    verify(accountRepository).save(testAccount);
    verify(simpleLock).unlock();
    verify(lockProvider, times(2)).lock(any());

    // Assert the result
    assertEquals(150, result.getCashAmount());
  }

  @Test
  public void testRetryableWithdraw() {

    Account testAccount = new Account();
    testAccount.setCashAmount(100);
    SimpleLock simpleLock = mock(SimpleLock.class);
    when(accountRepository.findById(anyString())).thenReturn(testAccount);
    when(lockProvider.lock(any())).thenReturn(Optional.empty()).thenReturn(Optional.of(simpleLock));

    Account result = accountService.withdraw(TEST_CLIENT_ID, 50);

    verify(accountRepository, times(2)).findById(TEST_CLIENT_ID);
    verify(accountRepository).save(testAccount);
    verify(simpleLock).unlock();
    verify(lockProvider, times(2)).lock(any());

    // Assert the result
    assertEquals(50, result.getCashAmount());
  }

  @Test
  public void testNotEnoughtResource_exceptionThrown() {

    Account testAccount = new Account();
    testAccount.setCashAmount(100);
    SimpleLock simpleLock = mock(SimpleLock.class);
    when(accountRepository.findById(anyString())).thenReturn(testAccount);
    when(lockProvider.lock(any())).thenReturn(Optional.of(simpleLock));

    assertThrows(
        OperationNotAllowed.class, () -> accountService.withdraw(TEST_CLIENT_ID, 150));

    verify(accountRepository).findById(TEST_CLIENT_ID);
    verify(accountRepository, never()).save(testAccount);
    verify(simpleLock).unlock();
    verify(lockProvider).lock(any());
  }
}
