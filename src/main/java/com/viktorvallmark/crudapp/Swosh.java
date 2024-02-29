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

  public int login(Scanner scan) {

    System.out.println(
        " Welcome to Swosh!\n"
            + " Please choose an option:\n"
            + " 1. Log in \n"
            + " 2. Register an account \n"
            + " 3. Quit");
    int res = scan.nextInt();
    return res;
  }

  public int menu(Scanner scan) {

    System.out.println(
        "Welcome to your Swosh! Please select an option: \n"
            + " 1. Deposit money \n"
            + " 2. Send money \n"
            + " 3. Withdraw money \n"
            + " 4. Close account \n"
            + " 5. Print account information \n"
            + " 6. Quit");
    int temp = scan.nextInt();
    return temp;
  }

  public boolean loginLanding(Scanner scan) {

    String loginString = "SELECT userid FROM users WHERE name = ? AND password = ?;";
    try (PreparedStatement loginStmt = this.getConnection().prepareStatement(loginString);) {

      System.out.println("Please enter username: ");
      String username = scan.next();
      System.out.println("Please enter password: ");
      String pass = scan.next();

      getConnection().setAutoCommit(false);

      loginStmt.setString(1, username);
      loginStmt.setString(2, pass);
      ResultSet loginResult = loginStmt.executeQuery();
      this.getConnection().commit();

      int userid = 0;
      while (loginResult.next()) {
        userid = loginResult.getInt("userid");
      }
      if (userid == 0) {
        System.err.println("No such users exists!\n");
        registrationLanding(scan);
        System.out.println("Welcome to Swosh! Please log in\n");
        return false;
      } else {

        this.setUser(new User(username, pass, userid));
        return true;
      }

    } catch (SQLException e) {
      System.err.println(
          "Something went wrong when logging in: "
              + e.getMessage()
              + "\n Possible cause: "
              + e.getCause());
    }
    return false;
  }

  public boolean registrationLanding(Scanner scan) {

    System.out.println("Welcome to registration!");
    System.out.println("Please enter your username: ");
    String registerName = scan.next();
    System.out.println("Please enter your password: ");
    String registerPass = scan.next();
    try {
      boolean regiResult = addUserAccount(registerName, registerPass, 0.0f);
      if (regiResult) {
        System.out.println("You are now registered!");
        return true;
      }
    } catch (SQLException e) {

      System.err.println(
          "Something went wrong with registration"
              + e.getMessage()
              + "\n Possible cause: "
              + e.getCause());
    }
    return false;
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

      conn.createStatement().executeUpdate(createDb);

      conn.createStatement().execute(useDb);
      conn.createStatement().executeUpdate(createUserTable);

      conn.createStatement().executeUpdate(createAccountTable);

      conn.createStatement().executeUpdate(createTransactionTable);

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

  public boolean addUserAccount(String name, String password, double amount) throws SQLException {

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
        return false;
      } else {
        return true;
      }

    } catch (SQLException e) {
      System.err.println(
          "Something went wrong when creating a user and account: "
              + e.getMessage()
              + "\n "
              + e.getCause());
    }
    return false;
  }

  public boolean removeUserAccount(Swosh this, User user, Scanner scanRegister)
      throws SQLException {
    System.out.println("Are you sure you want to delete your Swosh account?: \n 1. Yes \n 2. No");
    int deletionChoice = scanRegister.nextInt();
    if (deletionChoice == 1) {
      String deleteUserQuery = "DELETE FROM users WHERE name = ? AND password = ? AND userid = ?;";
      String deleteAccountQuery = "DELETE FROM account WHERE useraccid = ?;";
      String moneyBackQuery = "SELECT amount from account WHERE useraccid = ?;";
      try (PreparedStatement deleteUserStmt = getConnection().prepareStatement(deleteUserQuery);
          PreparedStatement moneyBackStmt = getConnection().prepareStatement(moneyBackQuery);
          PreparedStatement deleteAccountStmt = getConnection().prepareStatement(deleteAccountQuery);) {

        getConnection().setAutoCommit(false);
        // Get account balance
        moneyBackStmt.setInt(1, getUser().getUserId());
        ResultSet rs = moneyBackStmt.executeQuery();
        getConnection().commit();
        double amount = 0;
        while (rs.next()) {
          amount = rs.getDouble("amount");
        }
        System.out.println("You recieved " + amount + " from your account");

        // Delete account
        deleteAccountStmt.setInt(1, getUser().getUserId());
        int deleteAccRes = deleteAccountStmt.executeUpdate();
        getConnection().commit();
        if (deleteAccRes > 0) {
          System.out.println("Success....");
        }

        // Delete user
        deleteUserStmt.setString(1, user.getUsername());
        deleteUserStmt.setString(2, user.getPassword());
        deleteUserStmt.setInt(3, getUser().getUserId());
        int deleteResult = deleteUserStmt.executeUpdate();
        getConnection().commit();
        System.out.println("You have now deleted your account");

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
