package com.viktorvallmark.crudapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class User {
  private String username;
  private String password;
  private Role role;
  private long id;
  private int accid = 1;
  private ArrayList<Account> accList = new ArrayList<>();
  private long transactionId = 1;

  public User(String username, String password, Role role, long id) {
    this.username = username;
    this.password = password;
    this.role = role;
    this.id = id;
  }

  public enum Role {
    Customer,
    Admin,
  }

  public long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public ArrayList<Account> getAccList() {
    return accList;
  }

  private long generateTransactionId() {
    if (transactionId == 1) {
      return transactionId;
    } else {
      transactionId++;
      return transactionId;
    }
  }

  private int generateAccId() {

    if (this.accid == 1) {
      return accid;
    } else {
      accid++;
      return accid;
    }
  }

  @Override
  public String toString() {
    return "User: " + username;
  }

  public Account createAccount(double amount) {

    Account acc = new Account(amount, this, generateAccId());
    accList.add(acc);

    return acc;
  }

  public void deleteAccount(Account acc) {

    try {

      if (accList.contains(acc)) {
        accList.remove(accList.indexOf(acc));
      }
      System.out.println(
          "User deleted account with id: "
              + acc.getId()
              + ". \n Money to be returned: "
              + acc.getAmount());
    } catch (Exception e) {
      System.err.println(e.toString());
    }
  }

  public double addAmount(double amount, Account acc) {
    acc.addOrWithdrawAmount(0, amount);
    return amount;
  }

  public double withdrawAmount(double amount, Account acc) {
    acc.addOrWithdrawAmount(1, amount);
    return amount;
  }

  public Transaction createTransaction(double amount, User toUser) {
    LocalDateTime datetime = LocalDateTime.now();
    Transaction newTransaction =
        new Transaction(datetime, amount, this, toUser, generateTransactionId());

    return newTransaction;
  }
}
