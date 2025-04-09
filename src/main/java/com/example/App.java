package com.example;

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
// import javafx.fxml.FXML;
// import javafx.scene.layout.AnchorPane;

public class App extends Application {

    private Account account;
    private final Transactions transactions = new Transactions();

    // @FXML
    // private AnchorPane tfTitle;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bank Account Management System");

        // Input fields
        TextField amountField = new TextField();
        TextField accNumberField = new TextField();
        TextField accNameField = new TextField();
        ComboBox<String> accTypeBox = new ComboBox<>();
        accTypeBox.getItems().addAll("Savings", "Current", "Fixed");

        TextField maturityField = new TextField();
        maturityField.setPromptText("Only for Fixed (3 or 6 months)");

        Button createBtn = new Button("Create Account");

        VBox creationBox = new VBox(10,
            new Label("Account Number:"), accNumberField,
            new Label("Account Name:"), accNameField,
            new Label("Account Type:"), accTypeBox,
            new Label("Amount Field"), amountField,
            maturityField,
            createBtn
        );
        creationBox.setPadding(new Insets(10));

        // Action buttons
    
        amountField.setPromptText("Amount");
        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button checkBalanceBtn = new Button("Check Balance");
        Button viewTransactionsBtn = new Button("View Transactions");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(true);

        VBox actionsBox = new VBox(10, amountField, depositBtn, withdrawBtn, checkBalanceBtn, viewTransactionsBtn);
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
                account.deposit(amt);
                transactions.addTransaction(account.accountNumber, account.accountName, "Deposit", amt);
                outputArea.setText("Deposited: " + amt);
                amountField.clear();
            } catch (Exception ex) {
                outputArea.setText("Error depositing amount.");
            }
        });

        withdrawBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText());
                account.withdraw(amt);
                transactions.addTransaction(account.accountNumber, account.accountName, "Withdraw", amt);
                outputArea.setText("Withdrawn: " + amt);
                amountField.clear();
            } catch (Exception ex) {
                outputArea.setText("Error withdrawing amount.");
            }
        });

        checkBalanceBtn.setOnAction(e -> {
            outputArea.setText("Balance: " + account.balance);
            amountField.clear();
        });

        viewTransactionsBtn.setOnAction(e -> {
            // Display transactions in the output area
            if (account == null) {
                outputArea.setText("No account created yet.");
                return;
            }
            
            transactions.viewTransactionsListString(outputArea);
            outputArea.setText("Transactions:\n" + transactions.viewTransactionsList());
            amountField.clear();
            // outputArea.setText(transactions.viewTransactionsList());
        });

        Scene scene = new Scene(mainBox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
