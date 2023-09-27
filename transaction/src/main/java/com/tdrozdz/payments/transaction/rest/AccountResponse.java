package com.tdrozdz.payments.transaction.rest;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountResponse {

  private Integer cash;
  private String clientId;
}
