import exceptions.*;
import model.BankAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Create accounts
            SavingsAccount savings = new SavingsAccount("SA001", "Alice", 500.00);
            CurrentAccount current = new CurrentAccount("CA001", "Bob", 200.00);
            FixedDepositAccount fixedDeposit = new FixedDepositAccount("FD001", "Charlie", 1000.00);

            // 2. Perform transactions
            System.out.println("\n=== Savings Account Transactions ===");
            testAccountOperations(savings);

            System.out.println("\n=== Current Account Transactions ===");
            testAccountOperations(current);

            System.out.println("\n=== Fixed Deposit Account Transactions ===");
            testFixedDepositOperations(fixedDeposit);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void testAccountOperations(BankAccount account) throws Exception {
        System.out.println("Initial balance: " + account.getBalance());

        // Deposit
        account.deposit(200.00);
        System.out.println("After deposit: " + account.getBalance());

        // Withdrawal
        try {
            account.withdraw(100.00);
            System.out.println("After withdrawal: " + account.getBalance());
        } catch (InsufficientBalanceException | OverdraftExceededException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }

        // Calculate interest (if applicable)
        account.calculateInterest();
        System.out.println("After interest: " + account.getBalance());

        // Show last 3 transactions
        System.out.println("\nTransaction History:");
        List<Transaction> transactions = account.getLastNTransactions(3);
        transactions.forEach(System.out::println);
    }

    private static void testFixedDepositOperations(FixedDepositAccount account) throws Exception {
        System.out.println("Initial balance: " + account.getBalance());
        System.out.println("Maturity date: " + account.getMaturityDate());

        // Try early withdrawal (should fail)
        try {
            account.withdraw(500.00);
        } catch (InsufficientBalanceException e) {
            System.out.println("Early withdrawal blocked: " + e.getMessage());
        }

        // Simulate reaching maturity date
        if (account instanceof FixedDepositAccount) {
            FixedDepositAccount fd = (FixedDepositAccount) account;
            System.out.println("\nSimulating maturity date reached...");
            fd.calculateInterest(); // Interest applied
            System.out.println("Balance after interest: " + fd.getBalance());

            // Successful withdrawal after maturity
            fd.withdraw(300.00);
            System.out.println("After mature withdrawal: " + fd.getBalance());
        }

        System.out.println("\nTransaction History:");
        List<Transaction> transactions = account.getLastNTransactions(3);
        transactions.forEach(System.out::println);
    }
}
