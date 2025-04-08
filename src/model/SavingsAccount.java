public class SavingsAccount extends BankAccount {
    private static final double MINIMUM_BALANCE = 100.0;

    // Constructors
    public SavingsAccount(String accountNumber, String accountHolderName) {
        super(accountNumber, accountHolderName);
    }

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException, OverdraftExceededException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        if (balance - amount < MINIMUM_BALANCE) {
            if (balance - amount < 0) {
                throw new OverdraftExceededException(0, amount - balance); // Savings accounts forbid overdrafts
            }
            throw new InsufficientBalanceException(balance - amount, MINIMUM_BALANCE);
        }
        balance -= amount;
        addTransaction("WITHDRAWAL", amount);
    }

    @Override
    public void calculateInterest() {
        double interest = balance * 0.05; // 5% interest
        balance += interest;
        addTransaction("INTEREST", interest);
    }
}

