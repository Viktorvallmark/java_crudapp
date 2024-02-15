package com.viktorvallmark.crudapp;

import java.sql.*;
import java.util.Scanner;

public class App {
  public static void main(String[] args) {

    Swosh swosh = new Swosh();
    boolean running = true;
    swosh.createDatabase();
    Scanner scanLogin = new Scanner(System.in);
    Scanner scanMain = new Scanner(System.in);
    System.out.println(
        " Welcome to Swosh!\n"
            + " Please choose an option:\n"
            + " 1. Log in \n"
            + " 2. Register an account \n"
            + " 3. Quit");
    try {
      while (running) {
        switch (scanMain.nextInt()) {
          case 1:
            System.out.println("Please enter username: ");
            String username = scanLogin.next();
            System.out.println("Please enter password: ");
            String pass = scanLogin.next();
            String stmt =
                "SELECT user FROM users WHERE name = '"
                    + username
                    + "' AND password = '"
                    + pass
                    + "'";
            ResultSet results = swosh.getConnection().prepareStatement(stmt).executeQuery();
            if (results == null) {
              System.out.println("Please register as an user!");
            } else {
              // TODO: Implement the main user logged in interface.
            }
            break;
          case 2:
            // TODO: Create a Customer user and then go to main interface event loop.
            break;
          case 3:
            running = false;
            break;
          default:
            break;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      scanLogin.close();
      scanMain.close();
    }
  }
}
