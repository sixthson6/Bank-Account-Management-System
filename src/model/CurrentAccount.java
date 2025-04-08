import java.time.LocalDate;

public class CurrentAccount extends BankAccount {
    private static final double OVERDRAFT_LIMIT = 500.0;

    public CurrentAccount(String accountNumber, String accountHolderName) {
        super(accountNumber, accountHolderName);
    }

    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void withdraw(double amount) throws OverdraftExceededException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (balance - amount < -OVERDRAFT_LIMIT) {
            throw new OverdraftExceededException(OVERDRAFT_LIMIT, amount - balance);
        }
        balance -= amount;
        addTransaction("WITHDRAWAL", amount);
    }

    @Override
    public void calculateInterest() {
        // Current accounts typically don't earn interest
    }
}