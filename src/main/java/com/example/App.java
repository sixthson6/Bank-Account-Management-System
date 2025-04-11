package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class App extends Application {

    private Account account;
    private final Transactions transactions = new Transactions();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bank Account Management System");

        VBox creationBox = UIComponents.createAccountCreationPane(this::createAccount);
        VBox actionsBox = UIComponents.createActionsPane(this::handleDeposit, this::handleWithdraw, this::handleCheckBalance, this::handleViewTransactions);
        TextArea outputArea = UIComponents.outputArea;

        HBox mainBox = new HBox(20, creationBox, actionsBox, outputArea);
        mainBox.setPadding(new Insets(10));

        Scene scene = new Scene(mainBox, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createAccount(String num, String name, String type, String maturityText) {
        if (type == null || num.isEmpty() || name.isEmpty()) {
            UIComponents.outputArea.setText("Please enter all account details.");
            return;
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
                    int months = Integer.parseInt(maturityText);
                    account = new FixedDepositAccount(num, name, months);
                } catch (NumberFormatException ex) {
                    UIComponents.outputArea.setText("Invalid maturity period (must be 3 or 6).\n");
                    return;
                }
                break;
            default:
                UIComponents.outputArea.setText("Invalid account type selected.");
                return;
        }
        UIComponents.outputArea.setText("Account created successfully!");
    }

    private void handleDeposit(String amountText) {
        try {
            double amt = Double.parseDouble(amountText);
            account.deposit(amt);
            transactions.addTransaction(account.accountNumber, account.accountName, "Deposit", amt);
            UIComponents.outputArea.setText("Deposited: " + amt);
            UIComponents.amountField.clear();
        } catch (Exception ex) {
            UIComponents.outputArea.setText("Error depositing amount.");
        }
    }

    private void handleWithdraw(String amountText) {
        try {
            double amt = Double.parseDouble(amountText);
            if (account instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) account;
                if ((sa.balance - amt) < sa.MIN_BALANCE) {
                    UIComponents.outputArea.setText("Error: Cannot withdraw. Minimum balance requirement not met.");
                    return;
                }
            }
            account.withdraw(amt);
            transactions.addTransaction(account.accountNumber, account.accountName, "Withdraw", amt);
            UIComponents.outputArea.setText("Withdrawn: " + amt);
            UIComponents.amountField.clear();
        } catch (Exception ex) {
            UIComponents.outputArea.setText("Error withdrawing amount.");
        }
    }

    private void handleCheckBalance() {
        UIComponents.outputArea.setText("Balance: " + account.balance);
    }

    private void handleViewTransactions() {
        UIComponents.outputArea.setText(transactions.viewTransactionsListString());
    }
}

class UIComponents {
    static TextArea outputArea = new TextArea();
    static TextField amountField = new TextField();

    static VBox createAccountCreationPane(AccountCreationHandler handler) {
        TextField accNumberField = new TextField();
        TextField accNameField = new TextField();
        ComboBox<String> accTypeBox = new ComboBox<>();
        accTypeBox.getItems().addAll("Savings", "Current", "Fixed");
        TextField maturityField = new TextField();
        maturityField.setPromptText("Only for Fixed (3 or 6 months)");
        Button createBtn = new Button("Create Account");

        createBtn.setOnAction(e -> handler.create(
                accNumberField.getText(),
                accNameField.getText(),
                accTypeBox.getValue(),
                maturityField.getText()
        ));

        VBox vbox = new VBox(10, new Label("Account Number:"), accNumberField,
                new Label("Account Name:"), accNameField,
                new Label("Account Type:"), accTypeBox,
                maturityField, createBtn);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    static VBox createActionsPane(DepositHandler depositHandler, WithdrawHandler withdrawHandler,
                                  Runnable checkBalanceHandler, Runnable viewTransactionsHandler) {
        
        amountField.setPromptText("Amount");

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button checkBalanceBtn = new Button("Check Balance");
        Button viewTransactionsBtn = new Button("View Transactions");

        depositBtn.setOnAction(e -> depositHandler.deposit(amountField.getText()));
        withdrawBtn.setOnAction(e -> withdrawHandler.withdraw(amountField.getText()));
        checkBalanceBtn.setOnAction(e -> checkBalanceHandler.run());
        viewTransactionsBtn.setOnAction(e -> viewTransactionsHandler.run());

        VBox vbox = new VBox(10, amountField, depositBtn, withdrawBtn, checkBalanceBtn, viewTransactionsBtn);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    interface AccountCreationHandler {
        void create(String number, String name, String type, String maturity);
    }

    interface DepositHandler {
        void deposit(String amount);
    }

    interface WithdrawHandler {
        void withdraw(String amount);
    }
}
