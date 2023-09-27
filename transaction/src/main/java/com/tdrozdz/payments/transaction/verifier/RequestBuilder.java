package com.tdrozdz.payments.transaction.verifier;

/**
 * Defines a contract for building requests of type {@code S} with a client identifier.
 *
 * @param <S>
 *     The type of requests to be built.
 */
interface RequestBuilder<S> {

  RequestBuilder<S> setClientId(String clientId);

  /**
   * Builds the request with the configured parameters.
   *
   * @return The constructed request of type {@code S}.
   */
  S build();
}
