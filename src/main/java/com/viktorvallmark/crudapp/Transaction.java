package com.viktorvallmark.crudapp;

import java.time.LocalDateTime;

public class Transaction {

  private LocalDateTime datetime;
  private double amount;
  private User fromUser;
  private User toUser;
  private long id;

  public Transaction(LocalDateTime datetime, double amount, User fromUser, User toUser, long id) {
    this.datetime = datetime;
    this.amount = amount;
    this.fromUser = fromUser;
    this.toUser = toUser;
    this.id = id;
  }

  @Override
  public String toString() {
    return "Transaction datetime: "
        + datetime
        + "\n Transaction amount: "
        + amount
        + "\n To user: "
        + toUser;
  }
}
