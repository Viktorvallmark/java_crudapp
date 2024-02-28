package com.viktorvallmark.crudapp;

public class Account {
  private long amount;
  private User owner;

  public Account(long amount, User owner) {
    this.amount = amount;
    this.owner = owner;
  }

  public User getUser() {
    return owner;
  }

  public double getAmount() {
    return amount;
  }

  public void addAmount(long amount) {
    this.amount += amount;
  }

  public void withdrawAmount(long amount) {
    this.amount -= amount;
  }
}
