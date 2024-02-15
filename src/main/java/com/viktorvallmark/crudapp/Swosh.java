package com.viktorvallmark.crudapp;

import com.viktorvallmark.crudapp.User.Role;
import java.sql.*;
import java.util.ArrayList;

public class Swosh {
  private ArrayList<User> userList = new ArrayList<>();
  private long userId = 1;
  private Connection conn;

  public Swosh() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/swosh", "root", "123");
    } catch (Exception e) {
      System.err.println("SQLException: " + e.getMessage());
    }
  }

  private long generateId() {
    if (userId == 1) {
      return userId;
    } else {
      userId++;
      return userId;
    }
  }

  public void createDatabase() {
    try {
      String statement =
          "CREATE DATABASE IF NOT EXIST swosh; CREATE TABLE IF NOT EXIST users(user int NOT NULL,"
              + " name VARCHAR(128) NOT NULL, password VARCHAR(128) NOT NULL, role VARCHAR(32) NOT"
              + " NULL ); CREATE TABLE IF NOT EXIST account(acc_id int NOT NULL, user int NOT"
              + " NULL, amount double NOT NULL ); CREATE TABLE IF NOT EXIST"
              + " transaction(transaction int NOT NULL, fromUser int NOT NULL, toUser int NOT NULL,"
              + " amount double NOT NULL);";
      System.out.println("Creating database...");
      conn.prepareStatement(statement).execute();
      System.out.println("Database created!");
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    }
  }

  public Connection getConnection() {
    return conn;
  }

  public void addUser(String name, String pass, long id, int flag) {
    try {
      if (flag == 0) {
        User newUser = new User(name, pass, Role.Customer, generateId());
        Statement statement = conn.createStatement();
        ResultSet resultSet =
            statement.executeQuery(
                "INSERT INTO user(newUser.getId(), newUser.getUsername(), newUser.getPassword(),"
                    + " newUser.getRole() )");
        userList.add(newUser);
        while (resultSet.next()) {
          System.out.println("User created with id: " + newUser.getId());
        }
      } else {

        User newAdmin = new User(name, pass, Role.Admin, generateId());
        Statement statement = conn.createStatement();
        ResultSet resultSet =
            statement.executeQuery(
                "INSERT INTO user(newUser.getId(), newUser.getUsername(), newUser.getPassword(),"
                    + " newUser.getRole() )");
        userList.add(newAdmin);
        while (resultSet.next()) {
          System.out.println("Admin created with id: " + newAdmin.getId());
        }
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    }
  }

  public void removeUser(User user) {

    try {
      if (userList.contains(user)) {
        userList.remove(userList.indexOf(user));
        System.out.println(user.toString() + " has been deleted.");
      } else {
        System.out.println(user.toString() + " was not found.");
      }
    } catch (Exception e) {

      e.printStackTrace();
    }
  }
}
