package com.tdrozdz.payments.transaction.verifier;

import com.google.common.base.Preconditions;
import com.tdrozdz.payments.api.IsRegistrationFinishedRequest;

final class IsRegistrationFinishedRequestBuilder
    implements RequestBuilder<IsRegistrationFinishedRequest> {

  private String clientId;

  @Override
  public RequestBuilder<IsRegistrationFinishedRequest> setClientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  @Override
  public IsRegistrationFinishedRequest build() {
    Preconditions.checkNotNull(clientId, "ClientId is required");
    return IsRegistrationFinishedRequest.newBuilder().setClientId(clientId).build();
  }
}
