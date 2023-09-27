package com.tdrozdz.payments.transaction.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountRequest {

  private Integer cashAmount;
  private String clientId;
}
