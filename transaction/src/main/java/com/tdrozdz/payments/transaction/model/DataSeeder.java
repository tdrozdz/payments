package com.tdrozdz.payments.transaction.model;

import lombok.AllArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Component
public class DataSeeder implements CommandLineRunner {

  private final AccountRepository accountRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    Account account1 = craeteAccount("1", "John", "Doe", 1000);
    Account account2 = craeteAccount("2", "Jane", "Smith", 1500);
    Account account3 = craeteAccount("3", "Bob", "Johnson", 800);
    Account account4 = craeteAccount("4", "Micheal", "Bradley", 0);

    accountRepository.save(account1);
    accountRepository.save(account2);
    accountRepository.save(account3);
    accountRepository.save(account4);
  }

  private Account craeteAccount(String id, String firstName, String lastName, Integer cashAmount) {
    var account = new Account();
    account.setId(id);
    account.setFirstName(firstName);
    account.setLastName(lastName);
    account.setCashAmount(cashAmount);
    return account;
  }
}
