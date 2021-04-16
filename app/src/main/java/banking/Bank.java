package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class Bank {
    private int counterOfUsers = 0;
    private DataBaseBehavior dataBaseBehavior = new DataBaseBehavior();
    public Bank() throws SQLException {}
    public Bank(DataBaseBehavior dataBaseBehavior) throws SQLException {
        dataBaseBehavior = dataBaseBehavior;
    }
    public void createAccount() throws SQLException {
        RequisitesGenerator requisitesGenerator = new RequisitesGenerator("400000");
        if(!dataBaseBehavior.isUserExists(requisitesGenerator.getCardNumber())) {
            BankAccount newBankAccount = new BankAccount(counterOfUsers++,requisitesGenerator.getCardNumber(),
                    requisitesGenerator.getPIN());
            dataBaseBehavior.addCardToDataBase(newBankAccount);
            System.out.println("Your card has been created");
            System.out.println("Your card number:\n" + requisitesGenerator.getCardNumber());
            System.out.println("Your card PIN:\n" + requisitesGenerator.getPIN());
        }
        else createAccount();
    }
    public void logIn() throws InvalidUserDataException, SQLException {
        System.out.print("Enter your card number: \n>");
        Scanner scan = new Scanner(System.in);
        String cardNumber = scan.nextLine();
        System.out.print("Enter your pin: \n>");
        String pin = scan.nextLine();
        if(dataBaseBehavior.isUserExists(cardNumber) && dataBaseBehavior.isPinCorrect(cardNumber, pin)) {
            openMenu(cardNumber, pin);
        }
        else throw new InvalidUserDataException("Wrong card number or PIN!");
    }
    public void transferMoney(String giverCardNumber) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Transfer\nEnter card number: \n>");
        String receiverCardNumber = scan.nextLine();
        int money = 0;
        if(new RequisitesGenerator("400000").isNumberCorrectByLuhnAlgorithm(receiverCardNumber)) {
                if(!giverCardNumber.equals(receiverCardNumber)) {
                    if(dataBaseBehavior.isUserExists(receiverCardNumber)) {
                        System.out.print("Enter how much money you want to transfer: \n>");
                        money = scan.nextInt();
                        if(money <= dataBaseBehavior.getBalanceOfTheUser(giverCardNumber)) {
                            dataBaseBehavior.transferMoney(giverCardNumber, receiverCardNumber, money);
                            System.out.println("Success!");
                        }
                        else System.out.println("Not enough money!");
                    }
                        else System.out.println("Such a card does not exist.");
                }
                else System.out.println("You can't transfer money to the same account!");
        }
        else System.out.println("Probably you made a mistake in the card number. Please try again!");
    }
    public void openMenu(String cardNumber, String pin) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nYou have successfully logged in!\n");
        int choice = 0;
        while(choice != 5) {
            System.out.print("1. Balance\n2. Add income\n3. Do transfer" +
                    "\n4. Close account\n5. Log out \n0. Exit\n>");
            choice = scan.nextInt();
            switch (choice) {
                case 0:
                    if(dataBaseBehavior != null) { dataBaseBehavior.disestablishConnection(); }
                    System.exit(0);
                case 1:
                    System.out.println("\nBalance: " + dataBaseBehavior.getBalanceOfTheUser(cardNumber));
                    break;
                case 2:
                    dataBaseBehavior.depositMoneyToTheAccountById(cardNumber);
                    break;
                case 3:
                    transferMoney(cardNumber);
                    break;
                case 4:
                    dataBaseBehavior.deleteAccount(cardNumber);
                    System.out.println("\nThe account has been closed!\n");
                    return;
                case 5:
                    System.out.println("\nYou have successfully logged out!\n");
                    return;
                default:
                    System.out.println("Incorrect input!");
                    break;
            }
        }
    }
}
