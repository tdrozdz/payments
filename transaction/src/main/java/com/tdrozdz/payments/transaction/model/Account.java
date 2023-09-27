package com.tdrozdz.payments.transaction.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Account {

  @Id
  private String id;

  private String firstName;

  private String lastName;

  private Integer cashAmount;

}
