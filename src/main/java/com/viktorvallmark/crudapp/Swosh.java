package com.viktorvallmark.crudapp;

import java.sql.*;
import java.util.Scanner;

public class Swosh {
  private User user;
  private Connection conn;

  public Swosh() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swosh", "root", "123");
    } catch (Exception e) {
      System.err.println("SQLException: " + e.getMessage());
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void createDatabase() {
    try {
      String createDb = "CREATE DATABASE IF NOT EXISTS swosh;";
      String useDb = "use swosh;";
      String createUserTable = "CREATE TABLE IF NOT EXISTS users( userid INT(32) NOT NULL AUTO_INCREMENT, name"
          + " VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, PRIMARY KEY (userid));";
      String createAccountTable = "CREATE TABLE IF NOT EXISTS account( useraccid INT(32) NOT NULL, amount DOUBLE NOT"
          + " NULL);";
      String createTransactionTable = "CREATE TABLE IF NOT EXISTS transaction( transactionid INT(32) NOT NULL AUTO_INCREMENT,"
          + " fromUser INT(32), toUser INT(32) NOT NULL, amount INT(64) NOT NULL,"
          + " transactiondate DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
          + " PRIMARY KEY (transactionid));";
      /*
       * System.out.println(createDb);
       * System.out.println(createUserTable);
       * System.out.println(createAccountTable);
       * System.out.println(createTransactionTable);
       *
       */
      System.out.println("Creating database...");
      int results = conn.createStatement().executeUpdate(createDb);

      boolean useDbRes = conn.createStatement().execute(useDb);
      int userResults = conn.createStatement().executeUpdate(createUserTable);

      int accountResults = conn.createStatement().executeUpdate(createAccountTable);

      int transactionResults = conn.createStatement().executeUpdate(createTransactionTable);

      System.out.println(
          "Database created! DBResult: "
              + results
              + " Using db: "
              + useDbRes
              + " UserResults: "
              + userResults
              + " AccountResults: "
              + accountResults
              + " TransactionResults: "
              + transactionResults);
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      System.err.println("SQLError: " + e.getErrorCode());
      System.err.println("SQL: " + e.getSQLState());
      System.exit(-1);
    }
  }

  public Connection getConnection() {
    return conn;
  }

  public int addUserAccount(String name, String password, double amount) throws SQLException {

    String addUserQuery = "INSERT INTO users(name, password) VALUES (?, ?);";
    String addAccountQuery = "INSERT INTO account(useraccid, amount) VALUES (?, ?);";
    String getUseridQuery = "SELECT userid FROM users WHERE name = ? AND password = ?;";

    try (PreparedStatement addUserStmt = conn.prepareStatement(addUserQuery);
        PreparedStatement addAccountStmt = conn.prepareStatement(addAccountQuery);
        PreparedStatement getUseridStmt = conn.prepareStatement(getUseridQuery)) {

      conn.setAutoCommit(false);
      // Create a user
      addUserStmt.setString(1, name);
      addUserStmt.setString(2, password);
      int resUser = addUserStmt.executeUpdate();

      // Get the id from the newly created user
      getUseridStmt.setString(1, name);
      getUseridStmt.setString(2, password);
      ResultSet rsUserid = getUseridStmt.executeQuery();
      int userid = 0;
      while (rsUserid.next()) {
        userid = rsUserid.getInt("userid");
      }
      if (userid == 0) {
        System.err.println("failure to get userid in addUserAccount method");
      } else {
        user = new User(name, password, userid);
      }
      conn.commit();
      // Use the userid from prev query to make an account
      addAccountStmt.setInt(1, userid);
      addAccountStmt.setDouble(2, amount);
      int resAccount = addAccountStmt.executeUpdate();
      conn.commit();

      if (resUser == 0 || resAccount == 0) {
        return -1;
      } else {
        return 0;
      }

    } catch (SQLException e) {
      System.err.println(
          "Something went wrong when creating a user and account: "
              + e.getMessage()
              + "\n "
              + e.getCause());
    }
    return -1;
  }

  public boolean removeUserAccount(Swosh this, User user, Scanner scanRegister)
      throws SQLException {
    System.out.println("Are you sure you want to delete your Swosh account?: \n 1. Yes \n 2. No");
    int deletionChoice = scanRegister.nextInt();
    if (deletionChoice == 1) {
      String deleteUserQuery = "DELETE FROM users WHERE name = ? AND password = ? AND userid = ?;";

      try (PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery)) {

        conn.setAutoCommit(false);

        // Delete user
        deleteUserStmt.setString(1, user.getUsername());
        deleteUserStmt.setString(2, user.getPassword());
        deleteUserStmt.setInt(3, getUser().getUserId());
        int deleteResult = deleteUserStmt.executeUpdate();
        conn.commit();

        if (deleteResult == 0) {
          System.err.println("Something went wrong while deleting the user!");
          return false;
        }
      } catch (SQLException e) {
        System.err.println(
            "Something went wrong with the SQL transaction: "
                + e.getMessage()
                + "\n Cause: "
                + e.getCause());
      }
      return true;
    } else {
      return false;
    }
  }
}
