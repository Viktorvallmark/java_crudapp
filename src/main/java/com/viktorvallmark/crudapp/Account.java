package com.viktorvallmark.crudapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Account {
  private int id;
  private double amount;
  private User owner;
  private ArrayList<Transaction> transAcc = new ArrayList<>();

  public Account(double amount, User owner, int id) {
    this.id = id;
    this.amount = amount;
    this.owner = owner;
  }

  public int getId() {
    return id;
  }

  public User getUser() {
    return owner;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(int flag, double amount) {

    switch (flag) {
      case 0:
        this.amount += amount;
      case 1:
        this.amount -= amount;
      default:
        throw new IllegalArgumentException("Use 0 to add money to account and 1 to remove.");
    }
  }

  public void createTransaction(double amount, User fromUser, User toUser) {
    LocalDateTime datetime = LocalDateTime.now();
    Transaction transaction = new Transaction(datetime, amount, fromUser, toUser);

    transAcc.add(transaction);

    System.out.println("You have transfered " + amount + " to " + toUser.toString());
  }

  public void getAllTransactions() {
    for (Transaction trans : transAcc) {
      System.out.println(trans.toString() + "\n");
    }
  }
}
