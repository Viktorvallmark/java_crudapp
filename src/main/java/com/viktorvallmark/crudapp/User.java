package com.viktorvallmark.crudapp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
  private String username;
  private String password;
  private Role role;
  private int accid = 1;
  private Account acc;
  private long transactionId = 1;

  public User(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.role = role;
    this.acc = new Account(0, this, generateAccId());
  }

  public enum Role {
    Customer,
    Admin,
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

  public Account getAcc() {
    return acc;
  }

  private long generateTransactionId() {
    if (transactionId == 1) {
      return transactionId;
    } else {
      transactionId++;
      return transactionId;
    }
  }

  private long generateAccId() {
    if (accid == 1) {
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

  public void addAmount(Scanner scanRegister, Swosh swosh) {
    try {
      System.out.println("How much money do you want to deposit?: \n");
      int deposit = scanRegister.nextInt();
      String stmt = "UPDATE account SET amount = amount " + deposit + " WHERE acc_id = " + acc.getId() + ";";
      int results = swosh.getConnection().createStatement().executeUpdate(stmt);
      if (results == 0) {
        throw new SQLException("Something went wrong!");
      } else {
        System.out.println("You have deposited: " + deposit + " :-");
      }
      acc.addOrWithdrawAmount(0, deposit);
    } catch (SQLException e) {
      System.err.println("Error adding money to account: " + e.getMessage());
    }
  }

  public void withdrawAmount(Scanner scanRegister, Swosh swosh) {
    try {
      System.out.println("How much money do you want to withdraw?: \n");
      int withdraw = scanRegister.nextInt();
      String stmtWithdraw = "UPDATE account SET amount = amount - " + withdraw + ";";
      int resultsWithdraw = swosh.getConnection().createStatement().executeUpdate(stmtWithdraw);
      if (resultsWithdraw == 0) {
        throw new SQLException("Something went wrong!");
      } else {
        System.out.println("You withdrew: " + withdraw + " :-");
      }
    } catch (SQLException e) {
      System.err.println("Error withdrawing money from account: " + e.getMessage());
    }
  }

  public void printAccount(Scanner scan, Swosh swosh) {
    try {
      System.out.println("What id does the account have?: ");
      int id = scan.nextInt();
      String printStmt = "SELECT * FROM account WHERE user_id = " + id + ";";
      ResultSet printResult = swosh.getConnection().createStatement().executeQuery(printStmt);
      while (printResult.next()) {
        System.out.println("Account information: " + printResult.toString());
      }
    } catch (SQLException e) {
      System.err.println(
          "Something went wrong trying to print account statement: " + e.getMessage());
    }
  }

  public boolean createTransaction(Swosh swosh, Scanner scanRegister) {
    try {
      System.out.println("How much money do you want to send?: \n");
      int sendAmount = scanRegister.nextInt();
      System.out.println("To whom do you want to send?: \n");
      int sendToUser = scanRegister.nextInt();
      String stmtSend = "UPDATE account SET amount = amount -"
          + sendAmount
          + "WHERE accid = "
          + this.accid
          + "; UPDATE account SET amount = amount +"
          + sendAmount
          + " WHERE accid = "
          + sendToUser
          + ";";
      int resultsSend = swosh.getConnection().createStatement().executeUpdate(stmtSend);
      String stmtTrans = "INSERT INTO transaction(transaction, accid, toUser, amount) VALUES ("
          + generateTransactionId()
          + ", "
          + this.accid
          + ", "
          + sendToUser
          + ", "
          + sendAmount
          + ");";
      int resultStatment = swosh.getConnection().createStatement().executeUpdate(stmtTrans);
      if (resultsSend == 0 && resultStatment == 0) {
        System.err.println("Something went wrong with the transfer, please try again!");
        return false;
      } else {
        System.out.println(
            "You have sent: " + sendAmount + " :- \n You have sent it to user: " + sendToUser);
        return true;
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
    return false;
  }
}
