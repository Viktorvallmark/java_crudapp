package com.viktorvallmark.crudapp;

import com.viktorvallmark.crudapp.User.Role;
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
      String useDb = "USE swosh;";
      String createUserTable = "CREATE TABLE IF NOT EXISTS users( userid INT(32) NOT NULL AUTO_INCREMENT, name"
          + " VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(255) NOT"
          + " NULL, PRIMARY KEY (userid));";
      String createAccountTable = "CREATE TABLE IF NOT EXISTS account( accid INT(32) NOT NULL, userid"
          + " INT(32) NOT NULL, amount DOUBLE NOT NULL);";
      String createTransactionTable = "CREATE TABLE IF NOT EXISTS transaction( transactionid INT(32) NOT NULL,"
          + " accid INT(32), toUser INT(32) NOT NULL, amount DOUBLE NOT NULL);";
      /*
       * System.out.println(createDb);
       * System.out.println(createUserTable);
       * System.out.println(createAccountTable);
       * System.out.println(createTransactionTable);
       * System.out.println("Creating database...");
       */
      int useDbRes = conn.createStatement().executeUpdate(useDb);
      int results = conn.createStatement().executeUpdate(createDb);
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

  public void addUser(String name, String pass, int flag) {
    try {
      if (flag == 0) {
        Statement statement = conn.createStatement();
        int resultSet = statement.executeUpdate(
            "INSERT INTO users(name, password, role) VALUES ('"
                + name
                + "','"
                + pass
                + "', 'Customer');");
        User newUser = new User(name, pass, Role.Customer);
        setUser(newUser);
        if (resultSet > 0) {
          System.out.println("User created successfully! ");
        }
      } else {

        User newAdmin = new User(name, pass, Role.Admin);
        Statement statement = conn.createStatement();
        int resultSet = statement.executeUpdate(
            "INSERT INTO user (name, password, role) VALUES ('"
                + name
                + "','"
                + pass
                + "', 'Admin');");
        setUser(newAdmin);
        if (resultSet > 0) {
          System.out.println("Admin created successfully! ");
        }
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    }
  }

  public boolean removeUser(Swosh this, User user, Scanner scanRegister) {
    try {
      System.out.println("Are you sure you want to delete your Swosh account?: \n 1. Yes \n 2. No");
      int deletionChoice = scanRegister.nextInt();
      if (deletionChoice == 1) {
        if (user.equals(null)) {
          throw new NullPointerException("User == null, something went horribly wrong!");
        }
        String deletionString = "DELETE FROM user WHERE name = " + user.getUsername() + ";";
        int resultDeletion = this.getConnection().createStatement().executeUpdate(deletionString);

        if (resultDeletion == 0) {
          throw new SQLException("Something went wrong with the database transaction!");
        } else {
          System.out.println(
              "Sad to see you leave Swosh, please create a new account whenever you want!");
          return true;
        }
      } else {
        return false;
      }
    } catch (SQLException e) {
      System.err.println("Something went wrong when deleting a user" + e.getMessage());
    }
    return false;
  }
}
