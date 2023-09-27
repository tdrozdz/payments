package com.tdrozdz.payments.transaction.rest;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
final class TransactionRest {

  private AccountService accountService;

  @GetMapping("/accountDetail")
  public AccountResponse accountDetails(@RequestParam(value = "clientId") String clientId) {
    var account = accountService.get(clientId);
    return AccountResponse.builder().cash(account.getCashAmount()).clientId(clientId).build();
  }

    @PostMapping("/withdraw")
  public AccountResponse withdraw(@RequestBody AccountRequest request) {
    var account = accountService.withdraw(request.getClientId(), request.getCashAmount());
    return AccountResponse
        .builder()
        .cash(account.getCashAmount())
        .clientId(request.getClientId())
        .build();
  }

  @PostMapping("/deposit")
  public AccountResponse deposit(@RequestBody AccountRequest request) {
    var account = accountService.deposite(request.getClientId(), request.getCashAmount());
    return AccountResponse
        .builder()
        .cash(account.getCashAmount())
        .clientId(request.getClientId())
        .build();
  }
}
