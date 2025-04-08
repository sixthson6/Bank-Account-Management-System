import java.time.LocalDate;
import java.time.Period;

public class FixedDepositAccount extends BankAccount {
    private final LocalDate creationDate;
    private final LocalDate maturityDate;
    private static final double INTEREST_RATE = 0.07; // 7% annual interest
    private static final int MIN_TERM_MONTHS = 6;

    public FixedDepositAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
        this.creationDate = LocalDate.now();
        this.maturityDate = creationDate.plusMonths(MIN_TERM_MONTHS);
    }

    @Override
    public void withdraw(double amount) throws InsufficientBalanceException, OverdraftExceededException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (LocalDate.now().isBefore(maturityDate)) {
            throw new InsufficientBalanceException("Cannot withdraw before maturity date: " + maturityDate);
        }

        if (amount > balance) {
            throw new OverdraftExceededException(0, amount - balance);
        }
        balance -= amount;
        addTransaction("WITHDRAWAL", amount);
    }

    @Override
    public void calculateInterest() {
        if (LocalDate.now().isAfter(maturityDate)) {
            double interest = balance * INTEREST_RATE;
            balance += interest;
            addTransaction("INTEREST", interest);
        }
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }
}