// This is an Abstract base class for BankAccount
// methods are \:
//

// import java.util.LinkedList;

import java.util.LinkedList;
import java.util.List;

public abstract class BankAccount {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    // protected LinkedList<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
    this.accountNumber = accountNumber;
    this.accountHolderName = accountHolderName;
    this.balance = initialBalance;
    // this.transactionHistory = new LinkedList<>();
    }

    public abstract void withdraw(double amount); //throws InsufficientBalanceException;  
    // Forces child classes to define withdrawal rules

    public abstract void calculateInterest();  
    // For Savings/FixedDeposit accounts (no-op in CurrentAccount)

    // Deposit (shared by all accounts)
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
        // addTransaction("DEPOSIT", amount);
    }

    // // Balance check
    // public double getBalance() {
    //     return balance;
    // }

    // // Transaction history management
    // public void addTransaction(String type, double amount) {
    //     Transaction t = new Transaction(type, amount, balance);
    //     transactionHistory.addFirst(t); // Newest first
    // }

    // public List<Transaction> getLastNTransactions(int n) {
    //     return transactionHistory.stream().limit(n).toList();
    //}

    // Override toString() for debugging
    @Override
    public String toString() {
        return String.format("Account %s (%s): Balance $%.2f", 
            accountNumber, accountHolderName, balance);
    }

    // Validate withdrawal amount (reusable for child classes)
    // protected void validateWithdrawal(double amount) throws InsufficientBalanceException {
    //     if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
    //     if (amount > balance) throw new InsufficientBalanceException();
    // }
}
