package com.tdrozdz.payments.transaction.verifier;

/**
 * Defines a way for converting response of a generic type to boolean used by {@RemoteVerifier}.
 *
 * @param <T>
 *     The type of response objects that can be converted.
 */
interface ResponseConverter<T> {

  /**
   * Converts response object of the specified type to boolen.
   *
   * @param response
   *     The response object to be converted.
   *
   * @return {@code true} if the response returned by {@RemoteVerifier} indicates the user is
   *     verified, {@code false} otherwise.
   */
  boolean convertResponse(T response);
}
