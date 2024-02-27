package com.viktorvallmark.crudapp;

import com.viktorvallmark.crudapp.User.Role;
import java.sql.*;
import java.util.Scanner;

public class App {
  public static void main(String[] args) {

    // mvn exec:java -Dexec.mainClass="com.viktorvallmark.crudapp.App" -s
    // "/usr/share/maven/conf/settings.xml"

    boolean isLoggedin = false;
    Swosh swosh = new Swosh();
    boolean running = true;
    swosh.createDatabase();
    Scanner scanLogin = new Scanner(System.in);
    Scanner scanMain = new Scanner(System.in);
    Scanner scanRegister = new Scanner(System.in);
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
            String stmt = "SELECT userid FROM users WHERE name = '"
                + username
                + "' AND password = '"
                + pass
                + "'";
            boolean loginResults = swosh.getConnection().createStatement().execute(stmt);
            if (loginResults == false) {
              System.out.println("Please register as an user!");
              running = false;
              System.exit(0);
            } else {
              swosh.addUser(username, pass, 0);
              isLoggedin = true;
            }
            break;
          case 2:
            System.out.println("Welcome to registration!");
            System.out.println("Please enter your username: ");
            String registerName = scanRegister.next();
            System.out.println("Please enter your password: ");
            String registerPass = scanRegister.next();
            String statement = "INSERT INTO user(name, pass, role) VALUES ("
                + registerName
                + ", "
                + registerPass
                + ","
                + Role.Customer
                + ")";
            int regiResult = swosh.getConnection().createStatement().executeUpdate(statement);
            if (regiResult != 0) {
              System.out.println("You are now registered!");
            }
            isLoggedin = true;
            swosh.addUser(registerName, registerPass, 0);
            break;
          case 3:
            System.out.println("Thanks for using Swosh!");
            running = false;
            swosh.getConnection().close();
            System.exit(0);
            break;
          default:
            break;
        }
        if (isLoggedin) {
          System.out.println(
              "Welcome to your Swosh! Please select an option: \n"
                  + " 1. Deposit money \n"
                  + " 2. Send money \n"
                  + " 3. Withdraw money \n"
                  + " 4. Close account \n"
                  + " 5. Print account information \n"
                  + " 6. Quit");

          int choice = scanLogin.nextInt();
          switch (choice) {
            case 1:
              swosh.getUser().addAmount(scanRegister, swosh);
              break;
            case 2:
              swosh.getUser().createTransaction(swosh, scanRegister);
              break;
            case 3:
              swosh.getUser().withdrawAmount(scanRegister, swosh);
              break;
            case 4:
              boolean temp = swosh.removeUser(swosh.getUser(), scanRegister);
              if (temp == true) {
                running = false;
              } else {
                running = true;
              }
              break;
            case 5:
              swosh.getUser().printAccount(scanRegister, swosh);
              break;
            case 6:
              System.out.println("Good bye and please come again!");
              isLoggedin = false;
              running = false;
              System.exit(0);
              break;
            default:
              break;
          }
        }
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      System.err.println("SQLError: " + e.getErrorCode());
    } finally {
      scanLogin.close();
      scanRegister.close();
      scanMain.close();
    }
  }
}
