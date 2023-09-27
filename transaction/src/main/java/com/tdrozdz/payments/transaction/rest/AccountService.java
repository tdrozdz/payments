package com.tdrozdz.payments.transaction.rest;

import lombok.AllArgsConstructor;

import com.tdrozdz.payments.transaction.model.Account;
import com.tdrozdz.payments.transaction.model.AccountRepository;
import com.tdrozdz.payments.transaction.verifier.VerifyService;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.time.Instant;

/**
 * The {@code AccountService} class provides methods for managing user accounts, including
 * depositing and withdrawing funds. It uses the {@code AccountRepository} to access account data,
 * the {@code LockProvider} for locking operations, and the {@code VerifyService} to verify user
 * access.
 */
@AllArgsConstructor
public class AccountService {

  private static final Integer LOCK_AT_MOST_IN_MILISECOND = 100;

  private final AccountRepository accountRepository;

  private final LockProvider lockProvider;

  private final VerifyService verifyService;

  /**
   * Deposits a specified amount of money into the user's account.
   *
   * @param clientId
   *     The unique identifier of the client account.
   * @param amount
   *     The amount of money to deposit.
   *
   * @throws OperationNotAllowed
   *     If the client's access is not allowed.
   */
  @Retryable(retryFor = IllegalStateException.class, maxAttempts = 3, backoff = @Backoff(delay = 50))
  public Account deposite(String clientId, Integer amount) {
    var simpleLock = lock(clientId);
    if (!verifyService.verify(clientId, amount, OperationType.DEPOSITE)) {
      simpleLock.unlock();
      throw new OperationNotAllowed();
    }
    var account = accountRepository.findById(clientId);
    account.setCashAmount(account.getCashAmount() + amount);
    accountRepository.save(account);
    simpleLock.unlock();
    return account;
  }

  /**
   * Withdraws a specified amount of money from the user's account.
   *
   * @param clientId
   *     The unique identifier of the client account.
   * @param amount
   *     The amount of money to withdraw.
   *
   * @throws OperationNotAllowed
   *     If the client's access is not allowed.
   */
  @Retryable(retryFor = IllegalStateException.class, maxAttempts = 3, backoff = @Backoff(delay = 50))
  public Account withdraw(String clientId, Integer amount) {
    var simpleLock = lock(clientId);
    if (!verifyService.verify(clientId, amount, OperationType.WITHDRAW)) {
      simpleLock.unlock();
      throw new OperationNotAllowed();
    }
    var account = accountRepository.findById(clientId);
    account.setCashAmount(account.getCashAmount() - amount);
    accountRepository.save(account);
    simpleLock.unlock();
    return account;
  }

  /**
   * Retrieves account information for a specified client.
   *
   * @param clientId
   *     The unique identifier of the client account.
   */
  public Account get(String clientId) {
    return accountRepository.findById(clientId);
  }


  private SimpleLock lock(String clientId) {
    var simpleLockOptional =
        lockProvider.lock(
            new LockConfiguration(clientId, Instant.now().plusMillis(LOCK_AT_MOST_IN_MILISECOND)));
    return simpleLockOptional.orElseThrow(
        () -> new IllegalStateException("Client is lockend and the operation can not be executed"));
  }
}
