package com.viktorvallmark.crudapp;

import java.util.Scanner;

public class App {
  public static void main(String[] args) {

    Swosh swosh = new Swosh();
    boolean running = true;
    System.out.println(
        " Welcome to Swosh!\n"
            + " Please choose an option:\n"
            + " 1. Log in \n"
            + " 2. Register an account \n"
            + " 3. Quit");
    Scanner scanLogin = new Scanner(System.in);
    while (running) {
      switch (scanLogin.nextInt()) {
        case 1:
          // TODO: Check db for user, if not exist then boot to register account.
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
      scanLogin.close();
    }
  }
}
