package banking;

public class BankAccount {
    private int id;
    private String cardNumber;
    private String PIN;
    private double balance = 0;
    public BankAccount(int id, String cardNumber, String PIN) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
        this.id = id;
    }
    public BankAccount(int id, String cardNumber, String PIN, double balance) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
        this.id = id;
        this.balance = balance;
    }
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return PIN;
    }

    public void setPin(String pin) {
        this.PIN = pin;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
