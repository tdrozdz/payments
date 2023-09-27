package com.tdrozdz.payments.transaction.verifier;

import com.google.common.base.Preconditions;
import com.tdrozdz.payments.api.IsSanctionedRequest;

final class IsSanctionedRequestBuilder implements RequestBuilder<IsSanctionedRequest> {

  private String clientId;

  @Override
  public RequestBuilder<IsSanctionedRequest> setClientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  @Override
  public IsSanctionedRequest build() {
    Preconditions.checkNotNull(clientId, "ClientId is required");
    return IsSanctionedRequest.newBuilder().setClientId(clientId).build();
  }
}
