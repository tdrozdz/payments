import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.tdrozdz.payments.transaction.model.Account;
import com.tdrozdz.payments.transaction.model.AccountRepository;
import com.tdrozdz.payments.transaction.rest.AccountService;
import com.tdrozdz.payments.transaction.rest.OperationNotAllowed;
import com.tdrozdz.payments.transaction.rest.OperationType;
import com.tdrozdz.payments.transaction.verifier.Verifier;
import com.tdrozdz.payments.transaction.verifier.VerifyService;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

  private final VerifyService verifyService = new VerifyService(List.of(new Verifier() {
    @Override
    public ListenableFuture<Boolean> isClientVerified(
        String clientId, Integer amount, OperationType operationType) {
      return Futures.immediateFuture(true);
    }
  }));
  private AccountService accountService;
  @Mock
  private AccountRepository accountRepository;
  @Mock
  private LockProvider lockProvider;

  @BeforeEach
  void setUp() {
    accountService = new AccountService(accountRepository, lockProvider, verifyService);
  }

  @Test
  public void testDeposite() {
    var clientId = "testClient";
    var amount = 100;
    var initAmount = 50;

    SimpleLock simpleLock = mock(SimpleLock.class);
    when(lockProvider.lock(any())).thenReturn(Optional.of(simpleLock));

    Account account = new Account();
    account.setCashAmount(initAmount);
    when(accountRepository.findById(clientId)).thenReturn(account);

    accountService.deposite(clientId, amount);

    verify(lockProvider).lock(any());
    verify(accountRepository).findById(clientId);
    verify(simpleLock).unlock();

    assertEquals(account.getCashAmount(), initAmount + amount);
  }

  @Test
  public void testWithdraw() {
    String clientId = "testClient";
    Integer amount = 50;
    Integer initAmount = 100;

    SimpleLock simpleLock = mock(SimpleLock.class);
    when(lockProvider.lock(any())).thenReturn(Optional.of(simpleLock));

    Account account = new Account();
    account.setCashAmount(initAmount);
    when(accountRepository.findById(clientId)).thenReturn(account);

    accountService.withdraw(clientId, amount);

    verify(lockProvider).lock(any());
    verify(accountRepository).findById(clientId);
    verify(simpleLock).unlock();

    assertEquals(account.getCashAmount(), initAmount - amount);
  }

  @Test
  public void testWithdrawWithoutVerification_throwsException() {

    accountService = new AccountService(accountRepository, lockProvider,
        new VerifyService(List.of(new Verifier() {
          @Override
          public ListenableFuture<Boolean> isClientVerified(
              String clientId, Integer amount, OperationType operationType) {
            return Futures.immediateFuture(false);
          }
        })));
    String clientId = "testClient";
    Integer amount = 50;

    SimpleLock simpleLock = mock(SimpleLock.class);
    when(lockProvider.lock(any())).thenReturn(Optional.of(simpleLock));

    assertThrows(
        OperationNotAllowed.class, () -> accountService.withdraw(clientId, amount));

    verify(lockProvider).lock(any());
    verify(simpleLock).unlock();
  }
}
