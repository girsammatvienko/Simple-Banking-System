package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBaseBehavior {
    private String url = "jdbc:sqlite:card.s3db";
    private Connection connection;
    public DataBaseBehavior() throws SQLException {
        establishConnection();
        createTableIsNotExists();
    }
    public DataBaseBehavior(String dataBaseName) throws SQLException {
        this.url = "jdbc:sqlite:" + dataBaseName;
        establishConnection();
        createTableIsNotExists();
    }
    private void establishConnection() throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Connection connection = dataSource.getConnection();
        this.connection = connection;
    }
    private void createTableIsNotExists() throws SQLException {
        if (connection.isValid(5)) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (\n" +
                        "id INTEGER,\n" +
                        "number TEXT,\n" +
                        "pin TEXT,\n" +
                        "balance INTEGER DEFAULT 0\n" +
                        ");");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void addCardToDataBase(BankAccount newBankAccount) throws SQLException {
            try (Statement statement = connection.createStatement()) {
                String query = "INSERT INTO card (id, number, pin) " +
                        "VALUES( " + newBankAccount.getId() + ","
                        + newBankAccount.getCardNumber() + ", "
                        + newBankAccount.getPin() + ");";
                statement.executeUpdate(query);
        }
            catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteAccount(String cardNumber) {
        String query = "DELETE FROM card WHERE number = " + cardNumber + ";";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            System.out.println(preparedStatement.executeUpdate());

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void depositMoneyToTheAccountById(String cardNumber) {
        System.out.print("Enter income: \n>");
        int money = new Scanner(System.in).nextInt();
        String query = "UPDATE card SET balance = balance + " + money + " WHERE number = " + cardNumber;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getBalanceOfTheUser(String cardNumber) {
        double balance = 0.0;
        String query = "SELECT balance FROM card WHERE number = " + cardNumber;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rowById = preparedStatement.executeQuery();
            balance = rowById.getDouble("balance");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
    public void loadData(ArrayList<BankAccount> accounts) {
            try (Statement statement = connection.createStatement()) {
                String query = "SELECT * FROM card;";
                try (ResultSet rows = statement.executeQuery(query)) {
                    while (rows.next()) {
                        int id = rows.getInt("id");
                        String cardNumber = rows.getString("number");
                        String pin = rows.getString("pin");
                        double balance = rows.getDouble("balance");
                        accounts.add(new BankAccount(id, cardNumber, pin, balance));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
    }
    }
    public void disestablishConnection() throws SQLException {
        try {
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isUserExists(String cardNumber) {
        boolean isExist = false;
        String query = "SELECT * FROM card where number = " + cardNumber + ";";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        ResultSet result = preparedStatement.executeQuery();
        isExist = result.next()? true: false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    return isExist;
    }
    public boolean isPinCorrect(String cardNumber, String PIN) {
        boolean isCorrect = false;
        String query = "SELECT * FROM card where number = " + cardNumber + " AND pin = " + PIN + ";";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet result = preparedStatement.executeQuery();
            isCorrect = result.next()? true:false;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return isCorrect;
    }
    public void transferMoney(String giverCardNumber,String recieverCardNumber, int sum) throws SQLException {
        String queryTransfer = "UPDATE card SET balance = balance - " + sum + " WHERE number = " + giverCardNumber + ";";
        String queryReceive = "UPDATE card SET balance = balance + " + sum + " WHERE number = " + recieverCardNumber + ";";
        connection.setAutoCommit(false);
        try (PreparedStatement transferStatement = connection.prepareStatement(queryTransfer);
             PreparedStatement receiveStatement = connection.prepareStatement(queryReceive)) {

            transferStatement.executeUpdate();
            receiveStatement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
        }
    }
}
