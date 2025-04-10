package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class App extends Application {

    private Account account;
    // private SavingsAccount savingsAccount;
    private final Transactions transactions = new Transactions();

    // @FXML
    // private AnchorPane tfTitle;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bank Account Management System");

        // Input fields
        TextField transactionsListField = new TextField();
        TextField amountField = new TextField();
        TextField accNumberField = new TextField();
        TextField accNameField = new TextField();
        ComboBox<String> accTypeBox = new ComboBox<>();
        accTypeBox.getItems().addAll("Savings", "Current", "Fixed");

        TextField maturityField = new TextField();
        maturityField.setPromptText("Only for Fixed (3 or 6 months)");

        Button createBtn = new Button("Create Account");
        Button resetBtn = new Button("Reset");

        VBox creationBox = new VBox(10,
            new Label("Account Number:"), accNumberField,
            new Label("Account Name:"), accNameField,
            new Label("Account Type:"), accTypeBox,
            maturityField,
            createBtn,
            resetBtn
        );
        creationBox.setPadding(new Insets(10));

        // Action buttons
    
        amountField.setPromptText("Amount");
        transactionsListField.setPromptText("View last N transactions (N)");
        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button checkBalanceBtn = new Button("Check Balance");
        Button viewTransactionsBtn = new Button("View Transactions");
        Button accountDetailsBtn = new Button("Account Details");
        Button maturityDateBtn = new Button("Show Maturity Date");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(true);

        VBox actionsBox = new VBox(10, 
        new Label("Enter Amount"), amountField, new Label("Transactions:"), depositBtn, withdrawBtn, maturityDateBtn, checkBalanceBtn, viewTransactionsBtn, transactionsListField, accountDetailsBtn);
        actionsBox.setPadding(new Insets(10));

        HBox mainBox = new HBox(20, creationBox, actionsBox, outputArea);
        mainBox.setPadding(new Insets(10));

        // Event Handlers
        createBtn.setOnAction(e -> {
            String num = accNumberField.getText();
            String name = accNameField.getText();
            String type = accTypeBox.getValue();

            if (type == null || num.isEmpty() || name.isEmpty()) {
                outputArea.setText("Please enter all account details.");
                return;
            }

            //if account number is not integer, show error message
            try {
                Integer.parseInt(num);
            } catch (NumberFormatException ex) {
                outputArea.setText("Account number must be a number.");
                return;
            }
            //account number must be 6 digits, show error message
            if (num.length() != 6) {
                outputArea.setText("Account number must be 6 digits.");
                return;
            }

            //if account name is not string, show error message
            if (!name.matches("[a-zA-Z ]+")) {
                outputArea.setText("Account name must be a string.");
                return;
            }
            //if account type is not in the list, show error message
            if (!accTypeBox.getItems().contains(type)) {
                outputArea.setText("Account type must be Savings, Current or Fixed.");
                return;
            }
            //if maturity period is not 3 or 6, show error message
            if (type.equals("Fixed")) {
                try {
                    int months = Integer.parseInt(maturityField.getText());
                    if (months != 3 && months != 6) {
                        outputArea.setText("Maturity period must be 3 or 6 months.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    outputArea.setText("Maturity period must be an integer.");
                    return;
                }
            }
            

            switch (type) {
                case "Savings":
                    account = new SavingsAccount(num, name);
                    break;
                case "Current":
                    account = new CurrentAccount(num, name);
                    break;
                case "Fixed":
                    try {
                        int months = Integer.parseInt(maturityField.getText());
                        account = new FixedDepositAccount(num, name, months);
                    } catch (NumberFormatException ex) {
                        outputArea.setText("Invalid maturity period (must be 3 or 6).");
                        return;
                    }
                    break;
            }
            outputArea.setText("Account created successfully!");
        });

        // Amount the buttons deposit, withdraw, check balance and view transactions
        // after any action, clear the amount field
        
        depositBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText());
                if (account instanceof FixedDepositAccount) {
                    if (amt % 100.0 != 0.0) {
                        outputArea.setText("Error: Deposit must be in multiples of 100.");
                        return;
                    }
                }
                //reference the account object to the transactions object

                account.deposit(amt);
                transactions.addTransaction(account.accountNumber, account.accountName, "Deposit", amt);
                outputArea.setText("Deposited: GHS " + amt);
                amountField.clear();
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid amount. Please enter a valid number.");
            } catch (Exception ex) {
                outputArea.setText("Error depositing amount.");
            }
        });

        withdrawBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText());
                if (account instanceof SavingsAccount) {
                    SavingsAccount sa = (SavingsAccount) account;
                    if ((sa.balance - amt) < sa.MIN_BALANCE) {
                        outputArea.setText("Error: Cannot withdraw. Minimum balance requirement not met.");
                        return;
                    }
                }
                if (account instanceof CurrentAccount) {
                    CurrentAccount ca = (CurrentAccount) account;
                    if (( amt) > ca.balance + ca.OVERDRAFT_LIMIT) {
                        outputArea.setText("Error: Cannot withdraw. Overdraft limit exceeded.");
                        return;
                    }
                }
            
                if (account instanceof FixedDepositAccount) {
                    FixedDepositAccount fda = (FixedDepositAccount) account;
        
                    try {
                        fda.withdraw(amt); // Use the refactored withdraw method
                        transactions.addTransaction(fda.accountNumber, fda.accountName, "Withdraw", amt);
                        outputArea.setText("Withdrawn: GHS " + amt + "\nNew Balance: GHS " + fda.balance);
                    } catch (IllegalArgumentException ex) {
                        outputArea.setText("Error: " + ex.getMessage());
                    }
                } else if (account instanceof SavingsAccount) {
                    SavingsAccount sa = (SavingsAccount) account;
                    if ((sa.balance - amt) < sa.MIN_BALANCE) {
                        outputArea.setText("Error: Cannot withdraw. Minimum balance requirement not met.");
                        return;
                    }
                    sa.withdraw(amt);
                    transactions.addTransaction(sa.accountNumber, sa.accountName, "Withdraw", amt);
                    outputArea.setText("Withdrawn: GHS " + amt + "\nNew Balance: GHS " + sa.balance);
                } else if (account instanceof CurrentAccount) {
                    CurrentAccount ca = (CurrentAccount) account;
                    if (amt > ca.balance + ca.OVERDRAFT_LIMIT) {
                        outputArea.setText("Error: Cannot withdraw. Overdraft limit exceeded.");
                        return;
                    }
                    ca.withdraw(amt);
                    transactions.addTransaction(ca.accountNumber, ca.accountName, "Withdraw", amt);
                    outputArea.setText("Withdrawn: GHS " + amt + "\nNew Balance: GHS " + ca.balance);
                }
        
                amountField.clear();
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid amount. Please enter a valid number.");

            } catch (Exception ex) {
                outputArea.setText("Error withdrawing amount.");
            }
        });

        checkBalanceBtn.setOnAction(e -> {
            outputArea.setText("Balance: GHS " + account.balance);
            amountField.clear();
        });

        maturityDateBtn.setOnAction(e -> {
            if (account == null) {
                outputArea.setText("No account created yet.");
                return;
            }
        
            if (account instanceof FixedDepositAccount) {
                FixedDepositAccount fda = (FixedDepositAccount) account;
                LocalDateTime maturityDate = fda.getMaturityDate();
                outputArea.setText("Maturity Date: " + maturityDate.toString());
                        // Format the date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = maturityDate.format(formatter);

            outputArea.setText("Maturity Date: " + formattedDate);
            } else {
                outputArea.setText("Maturity date is only applicable for Fixed Deposit Accounts.");
            }
        });

        viewTransactionsBtn.setOnAction(e -> {
            // Display transactions in the output area
            if (account == null) {
                outputArea.setText("No account created yet.");
                return;
            }
        
            try {
                String input = transactionsListField.getText().trim();
                if (input.isEmpty()) {
                    // If no input is provided, display the full list of transactions
                    outputArea.setText(transactions.viewTransactionsListString());
                } else {
                    // If input is provided, parse it as N and display the last N transactions
                    int n = Integer.parseInt(input);
                    if (n <= 0) {
                        outputArea.setText("Please enter a positive number for N.");
                        return;
                    }
                    outputArea.setText(transactions.viewTransactionsListString(n));
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid input. Please enter a valid number for N.");
            }
        
            transactionsListField.clear();
            amountField.clear();
        });

        accountDetailsBtn.setOnAction(e -> {
            if (account == null) {
                outputArea.setText("No account created yet.");
                return;
            }
            String details = "--------------------------------\n" +
                             "Account Details:\n" +
                             "--------------------------------\n" +
                             "Account Number: " + account.accountNumber + "\n" +
                             "Account Name: " + account.accountName + "\n" +
                             "Account Type: " + account.accountType + "\n" +
                             "Balance: GHS " + account.balance + "\n" +
                             "--------------------------------\n" ;
            outputArea.setText(details);
            amountField.clear();
        });

        resetBtn.setOnAction(e -> {
            // Clear all input fields
            transactionsListField.clear();
            amountField.clear();
            accNumberField.clear();
            accNameField.clear();
            maturityField.clear();
            accTypeBox.setValue(null); // Reset ComboBox
            outputArea.clear(); // Clear the output area
        });

        // Set up the scene and stage
        Scene scene = new Scene(mainBox, 800, 350);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
