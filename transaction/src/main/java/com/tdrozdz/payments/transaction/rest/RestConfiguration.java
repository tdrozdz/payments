package com.tdrozdz.payments.transaction.rest;

import com.tdrozdz.payments.transaction.model.AccountRepository;
import com.tdrozdz.payments.transaction.verifier.VerifyService;
import net.javacrumbs.shedlock.core.LockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfiguration {

  @Bean
  AccountService provideAccountService(AccountRepository accountRepository, LockProvider lockProvider, VerifyService verifyService) {
    return new AccountService(accountRepository, lockProvider, verifyService);
  }
}
