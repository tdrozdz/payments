package com.tdrozdz.payments.transaction.verifier;

import com.google.common.base.Preconditions;
import com.tdrozdz.payments.api.IsBlockedRequest;

final class IsBlockedRequestBuilder implements RequestBuilder<IsBlockedRequest> {

  private String clientId;

  @Override
  public RequestBuilder<IsBlockedRequest> setClientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  @Override
  public IsBlockedRequest build() {
    Preconditions.checkNotNull(clientId, "ClientId is required");
    return IsBlockedRequest.newBuilder().setClientId(clientId).build();
  }
}
