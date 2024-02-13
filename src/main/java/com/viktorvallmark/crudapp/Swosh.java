package com.viktorvallmark.crudapp;

import java.util.ArrayList;

public class Swosh {
  private ArrayList<User> userList = new ArrayList<>();
  private long userId = 1;

  public Swosh() {
    super();
  }

  private long generateId() {
    if (userId == 1) {
      return userId;
    } else {
      userId++;
      return userId;
    }
  }

  public void addUser(Role role, String name, String pass, long id) {

    User newUser = new User(name, pass, role, generateId());

    userList.add(newUser);
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
