package com.viktorvallmark.crudapp;

import java.sql.*;
import java.util.Scanner;

public class App {

  public static void main(String[] args) {

    // mvn exec:java -Dexec.mainClass="com.viktorvallmark.crudapp.App" -s
    // "/usr/share/maven/conf/settings.xml"

    boolean isLoggedin = false;
    Swosh swosh = new Swosh();
    boolean runningLogin = true;
    boolean runningMenu = false;
    swosh.createDatabase();
    Scanner scanLogin = new Scanner(System.in);
    Scanner scanMain = new Scanner(System.in);
    Scanner scanRegister = new Scanner(System.in);
    int choiceLogin = swosh.login(scanMain);
    while (runningLogin) {
      switch (choiceLogin) {
        case 1:
          isLoggedin = swosh.loginLanding(scanLogin);
          if (isLoggedin) {
            runningLogin = false;
            runningMenu = true;
          }
          break;
        case 2:
          boolean temp = swosh.registrationLanding(scanRegister);
          if (!temp) {
            System.err.println("Something went wrong with the registration!");
            System.exit(1);
            break;
          }

          choiceLogin = swosh.login(scanMain);
          break;
        case 3:
          System.out.println("Thanks for using Swosh!");
          runningLogin = false;
          try {
            swosh.getConnection().close();
          } catch (SQLException e) {
            System.err.println(
                "Something went wrong when closing the connection to the database: "
                    + e.getMessage()
                    + " \n Possible cause: "
                    + e.getCause());
          }
          System.exit(0);
          break;
        default:
          break;
      }
    }
    if (isLoggedin) {
      while (runningMenu) {
        int choice = swosh.menu(scanLogin);
        try {

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
              boolean temp = swosh.removeUserAccount(swosh.getUser(), scanRegister);
              if (temp == true) {
                runningMenu = false;
              }
              System.out.println(
                  "Sad to see you leave, please register a new account whenever you want!");
              swosh.getConnection().close();
              System.exit(0);
              break;
            case 5:
              swosh.getUser().printAccount(scanRegister, swosh);
              break;
            case 6:
              System.out.println("Good bye and please come again!");
              swosh.getConnection().close();
              isLoggedin = false;
              runningMenu = false;
              System.exit(0);
              break;
            default:
              System.exit(1);
              break;
          }
        } catch (SQLException e) {
          System.err.println(
              "Something went wrong while in the options menu: "
                  + e.getMessage()
                  + " \n Possible cause: "
                  + e.getCause());
        }
      }
    }
  }
}
