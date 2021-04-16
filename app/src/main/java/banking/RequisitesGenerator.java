package banking;

import java.util.Random;

public class RequisitesGenerator {
    private String bankIdentificationNumber;
    private String accountIdentifier;
    private String checksum;
    private String cardNumber;
    private String PIN;
    public RequisitesGenerator(String bankIdentificationNumber) {
        this.bankIdentificationNumber = bankIdentificationNumber;
        this.accountIdentifier =  generateAccountIdentifier();
        this.cardNumber = generateCardNumber();
        this.PIN = generatePin();
    }
    private String generatePin() { return String.format("%04d", new Random().nextInt(10000)); }
    public String generateCardNumber() { return bankIdentificationNumber + accountIdentifier + generateChecksum(); }
    private String generateAccountIdentifier() {
        return String.format("%09d", new Random().nextInt(1000000000));
    }
    private String generateChecksum() {
        String cardNumberWithoutChecksum = bankIdentificationNumber + accountIdentifier;
        String tempString = "";
        int sumOfDigits = 0;
        for(int i = 0;i < cardNumberWithoutChecksum.length();i++) {
            if(i % 2 == 0) {
                int currentDigit = Character.digit(cardNumberWithoutChecksum.charAt(i), 10) * 2;
                tempString += (currentDigit > 9)
                        ? currentDigit - 9 + "" : currentDigit + "";
            } else tempString += Character.digit(cardNumberWithoutChecksum.charAt(i),10) + "";
            sumOfDigits += Character.digit(tempString.charAt(i),10);
        }
        int checksum = (10 - (sumOfDigits%10));
        return checksum > 9? checksum - 9 + "": checksum + "";
    }
    public boolean isNumberCorrectByLuhnAlgorithm(String cardNumber) {
        int givenChecksum = Character.digit(cardNumber.charAt(cardNumber.length() - 1),10);
        String cardNumberWithoutChecksum = "";
        for (int i = 0; i < cardNumber.length() - 1; i++) {
            cardNumberWithoutChecksum += cardNumber.charAt(i);
        }
        String tempString = "";
        int sumOfDigits = 0;
        for (int i = 0; i < cardNumberWithoutChecksum.length(); i++) {
            if (i % 2 == 0) {
                int currentDigit = Character.digit(cardNumberWithoutChecksum.charAt(i), 10) * 2;
                tempString += (currentDigit > 9)
                        ? currentDigit - 9 + "" : currentDigit + "";
            } else tempString += Character.digit(cardNumberWithoutChecksum.charAt(i), 10) + "";
            sumOfDigits += Character.digit(tempString.charAt(i), 10);
        }
        int correctChecksum = (10 - (sumOfDigits%10));
        return givenChecksum == correctChecksum? true : false;
    }
    public String getBankIdentificationNumber() {
        return bankIdentificationNumber;
    }

    public void setBankIdentificationNumber(String bankIdentificationNumber) {
        this.bankIdentificationNumber = bankIdentificationNumber;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    private void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }


    public String getPIN() { return PIN;}
    public String getCardNumber() { return cardNumber;}
}
