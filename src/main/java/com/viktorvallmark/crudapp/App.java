package com.viktorvallmark.crudapp;

import java.sql.*;
import java.util.Scanner;

public class App {
  public static void main(String[] args) {
    boolean isLoggedin = false;
    User user = null;
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
            String stmt = "SELECT user FROM users WHERE name = '"
                + username
                + "' AND password = '"
                + pass
                + "'";
            ResultSet loginResults = swosh.getConnection().prepareStatement(stmt).executeQuery();
            if (loginResults == null) {
              System.out.println("Please register as an user!");
              running = false;
            } else {
              user = swosh.addUser(username, pass, 0);
              isLoggedin = true;
              scanLogin = null;
            }
            break;
          case 2:
            // TODO: Create a Customer user and then go to main interface event loop.
            System.out.println("Welcome to registration!");
            System.out.println("Please enter your username: ");
            String registerName = scanRegister.next();
            System.out.println("Please enter your password: ");
            String registerPass = scanRegister.next();
            String statement = "INSERT INTO user() VALUES (registerName, registerPass)";
            ResultSet regiResult = swosh.getConnection().prepareStatement(statement).executeQuery();
            while (regiResult.next()) {
              System.out.println("You are now registered!");
            }
            scanRegister = null;
            isLoggedin = true;
            user = swosh.addUser(registerName, registerPass, 0);
            break;
          case 3:
            running = false;
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
                  + " 5. Quit");

          int choice = scanLogin.nextInt();
          switch (choice) {
            case 1:
              System.out.println("How much money do you want to deposit?: \n");
              int deposit = scanRegister.nextInt();
              String stmt = "INSERT INTO account() VALUES deposit;";
              ResultSet results = swosh.getConnection().prepareStatement(stmt).executeQuery();
              if (results == null) {
                throw new SQLException("Something went wrong!");
              } else {
                System.out.println("You have deposited: " + deposit + " :-");
              }
              break;
            case 2:
              System.out.println("How much money do you want to send?: \n");
              int sendAmount = scanRegister.nextInt();
              System.out.println("To whom do you want to send?: \n");
              int sendToUser = scanRegister.nextInt();
              // TODO: Check if both users exists, select the user, deposit sendAmount,
              // withdraw
              // sendAmount from sender.
              String stmtSend = "INSERT INTO user() VALUES -sendAmount; INSERT INTO user() VALUES sendAmount;";
              ResultSet resultsSend = swosh.getConnection().prepareStatement(stmtSend).executeQuery();
              if (resultsSend == null) {
                throw new SQLException("Something went wrong!");
              } else {
                System.out.println(
                    "You have sent: "
                        + sendAmount
                        + " :- \n You have sent it to user: "
                        + sendToUser);
              }
              break;
            case 3:
              System.out.println("How much money do you want to withdraw?: \n");
              int withdraw = scanRegister.nextInt();
              String stmtWithdraw = "INSERT INTO account() VALUES -deposit;";
              ResultSet resultsWithdraw = swosh.getConnection().prepareStatement(stmtWithdraw).executeQuery();
              if (resultsWithdraw == null) {
                throw new SQLException("Something went wrong!");
              } else {
                System.out.println("You withdrew: " + withdraw + " :-");
              }
              break;
            case 4:
              System.out.println(
                  "Are you sure you want to delete your Swosh account?: \n 1. Yes 2. No");
              int deletionChoice = scanRegister.nextInt();
              if (deletionChoice == 1) {
                if (user.equals(null)) {
                  System.err.println("User == null, something went horribly wrong!");
                }
                String deletionString = "DELETE FROM user() VALUES "
                    + user.getUsername()
                    + user.getPassword()
                    + user.getRole()
                    + user.getId();
                ResultSet resultDeletion = swosh.getConnection().prepareStatement(deletionString).executeQuery();

                if (resultDeletion == null) {
                  throw new SQLException("Something went wrong with the database transaction!");
                } else {
                  System.out.println(
                      "Sad to see you leave Swosh, please create a new account whenever you want!");
                }
                user = null;
                isLoggedin = false;
                running = false;
              } else if (deletionChoice == 2) {
                break;
              }
              break;
            case 5:
              System.out.println("Good bye and please come again!");
              isLoggedin = false;
              running = false;
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
