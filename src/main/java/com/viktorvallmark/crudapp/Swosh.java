package com.viktorvallmark.crudapp;

import com.viktorvallmark.crudapp.User.Role;
import java.sql.Connection;
import java.sql.DriverManager;
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

  public void addUser(String name, String pass, long id, int flag) {
    if (flag == 0) {
      User newUser = new User(name, pass, Role.Customer, generateId());

      userList.add(newUser);
    } else {
      User newAdmin = new User(name, pass, Role.Admin, generateId());
      userList.add(newAdmin);
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
