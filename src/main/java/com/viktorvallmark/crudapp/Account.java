package com.viktorvallmark.crudapp;

import java.util.ArrayList;

public class Account {
  private long id;
  private double amount;
  private User owner;
  private ArrayList<Transaction> transAcc = new ArrayList<>();

  public Account(double amount, User owner, long id) {
    this.id = id;
    this.amount = amount;
    this.owner = owner;
  }

  public long getId() {
    return id;
  }

  public User getUser() {
    return owner;
  }

  public double getAmount() {
    return amount;
  }

  public void addOrWithdrawAmount(int flag, double amount) {

    switch (flag) {
      case 0:
        this.amount += amount;
      case 1:
        this.amount -= amount;
      default:
        throw new IllegalArgumentException("Use 0 to add money to account and 1 to remove.");
    }
  }

  public void getAllTransactions() {
    for (Transaction trans : transAcc) {
      System.out.println(trans.toString() + "\n");
    }
  }

  @Override
  public String toString() {
    return "Account:\n id: "
        + this.id
        + " \n owner: "
        + this.owner.toString()
        + " \n amount: "
        + this.amount;
  }
}
