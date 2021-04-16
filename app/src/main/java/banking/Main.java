package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InvalidUserDataException, SQLException {
    int choice = 1;
    DataBaseBehavior usersDataBase = new DataBaseBehavior(args[1]);
    Bank bank = new Bank(usersDataBase);
    Scanner scan = new Scanner(System.in);
    while(choice != 0) {
        System.out.print("1. Create an account\n2. Log into account\n0. Exit\n>");
        choice = scan.nextInt();
        switch (choice) {
            case 0:
                usersDataBase.disestablishConnection();
                break;
            case 1:
            bank.createAccount();
                break;
            case 2:
                try {
                    bank.logIn();
                }
                catch (InvalidUserDataException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Incorrect input!");
                break;
        }
    }
    }
}