package com.viktorvallmark.crudapp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

  private String username;
  private String password;
  private Account acc;
  private int id;

  public User(String username, String password, int id) {
    this.username = username;
    this.password = password;
    this.acc = new Account(0, this);
    this.id = id;
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

  public Account getAcc() {
    return acc;
  }

  public int getUserId() {
    return id;
  }

  public void addAmount(Scanner scanRegister, Swosh swosh) throws SQLException {
    System.out.println("How much money do you want to deposit? Please use integers: \n");
    int deposit = scanRegister.nextInt();
    String updateString = "UPDATE account SET amount = amount + ? WHERE useraccid = ?";
    try (PreparedStatement updateAmount = swosh.getConnection().prepareStatement(updateString); ) {
      swosh.getConnection().setAutoCommit(false);
      updateAmount.setInt(1, deposit);
      updateAmount.setInt(2, swosh.getUser().getUserId());
      updateAmount.executeUpdate();
      swosh.getConnection().commit();
      System.out.println("You have added " + deposit + " to your account!");
    } catch (SQLException e) {
      System.err.println(
          "Something went wrong when adding money to your account!: " + e.getMessage());
    }
    getAcc().addAmount(deposit);
  }

  public void withdrawAmount(Scanner scanRegister, Swosh swosh) throws SQLException {

    System.out.println("How much money do you want to withdraw? Please use integers: \n");
    int withdraw = scanRegister.nextInt();
    String updateString = "UPDATE account SET amount = amount - ? WHERE useraccid = ?";
    String checkPositiveBalance = "SELECT amount FROM account WHERE useraccid = ?;";
    try (PreparedStatement updateAmount = swosh.getConnection().prepareStatement(updateString);
        PreparedStatement checkPositiveBalanceStmt =
            swosh.getConnection().prepareStatement(checkPositiveBalance); ) {
      swosh.getConnection().setAutoCommit(false);

      // Check if withdraw doesn't bring account < 0
      checkPositiveBalanceStmt.setInt(1, swosh.getUser().getUserId());
      ResultSet rs = checkPositiveBalanceStmt.executeQuery();
      swosh.getConnection().commit();

      double money = 0;
      while (rs.next()) {
        money = rs.getDouble("amount");
      }
      if (money > withdraw) {

        updateAmount.setInt(1, withdraw);
        updateAmount.setInt(2, swosh.getUser().getUserId());
        updateAmount.executeUpdate();

        swosh.getConnection().commit();
        System.out.println("You withdrew " + withdraw + " from your account!");
      } else {
        System.err.println("You do not have enough money in your account!");
      }

    } catch (SQLException e) {
      System.err.println(
          "Something went wrong when withdrawing money from your account!: " + e.getMessage());
    }
    getAcc().withdrawAmount(withdraw);
  }

  public void printAccount(Scanner scan, Swosh swosh) throws SQLException {
    String query = "SELECT useraccid, amount FROM account WHERE useraccid = ?";

    try (PreparedStatement accQuery = swosh.getConnection().prepareStatement(query); ) {
      swosh.getConnection().setAutoCommit(false);
      accQuery.setInt(1, swosh.getUser().getUserId());
      ResultSet res = accQuery.executeQuery();
      while (res.next()) {
        int useraccid = res.getInt("useraccid");
        double amount = res.getDouble("amount");
        System.out.println("UserAccId: " + useraccid + " \nAmount: " + amount + "");
      }
    } catch (SQLException e) {
      System.err.println(
          "Something went wrong trying to show account details: "
              + e.getMessage()
              + " \nPossible cause: "
              + e.getCause());
    }
  }

  public boolean createTransaction(Swosh swosh, Scanner scanRegister) throws SQLException {

    String fromUserQuery = "UPDATE account SET amount = amount - ? WHERE useraccid = ?;";
    String transactionAmountQuery =
        "INSERT INTO transaction(fromUser, toUser, amount) VALUE (?, ?, ?);";
    String toUserQuery = "UPDATE account SET amount = amount + ? WHERE useraccid = ?;";
    String checkPositiveBalance = "SELECT amount FROM account WHERE useraccid = ?;";

    System.out.println("How much money do you want to send?: \n");
    int sendAmount = scanRegister.nextInt();
    System.out.println("To whom do you want to send?: \n");
    int sendToUser = scanRegister.nextInt();
    try (PreparedStatement fromUserStmt = swosh.getConnection().prepareStatement(fromUserQuery);
        PreparedStatement transAmountStmt =
            swosh.getConnection().prepareStatement(transactionAmountQuery);
        PreparedStatement toUserStmt = swosh.getConnection().prepareStatement(toUserQuery);
        PreparedStatement checkPositiveBalanceStmt =
            swosh.getConnection().prepareStatement(checkPositiveBalance)) {
      swosh.getConnection().setAutoCommit(false);
      // Check if sending user has enough in account
      checkPositiveBalanceStmt.setInt(1, swosh.getUser().getUserId());
      ResultSet rs = checkPositiveBalanceStmt.executeQuery();
      int amount = 0;
      while (rs.next()) {
        amount = rs.getInt("amount");
      }
      if (amount < sendAmount) {
        System.err.println("You don't have enough money for this transaction");
        return false;
      }
      swosh.getConnection().commit();
      // From user
      fromUserStmt.setInt(1, sendAmount);
      fromUserStmt.setInt(2, swosh.getUser().getUserId());
      int fromUserResult = fromUserStmt.executeUpdate();
      // Making the amountStmt
      transAmountStmt.setInt(1, swosh.getUser().getUserId());
      transAmountStmt.setInt(2, sendToUser);
      transAmountStmt.setInt(3, sendAmount);
      int amountResult = transAmountStmt.executeUpdate();
      // To User
      toUserStmt.setInt(1, sendAmount);
      toUserStmt.setInt(2, sendToUser);
      int toUserResult = toUserStmt.executeUpdate();
      swosh.getConnection().commit();

      if (fromUserResult == 0 || amountResult == 0 || toUserResult == 0) {
        System.err.println(
            "Something went wrong when trying to send from: "
                + swosh.getUser().getUsername()
                + " \nto user: "
                + sendToUser);
        return false;
      } else {
        return true;
      }
    } catch (SQLException e) {
      System.err.println("SQLException when creating a transaction: " + e.getMessage());
    }
    return false;
  }
}
