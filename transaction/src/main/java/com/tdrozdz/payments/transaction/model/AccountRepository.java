package com.tdrozdz.payments.transaction.model;

import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<Account, String> {

  Account findById(String clientId);

  void save(Account account);
}
