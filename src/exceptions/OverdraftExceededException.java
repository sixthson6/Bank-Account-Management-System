/**
 * Thrown when a withdrawal would exceed the allowed overdraft limit
 */
public class OverdraftExceededException extends Exception {
    private final double overdraftLimit;
    private final double attemptedWithdrawal;

    public OverdraftExceededException(double overdraftLimit, double attemptedWithdrawal) {
        super(String.format("Withdrawal of $%.2f exceeds overdraft limit of $%.2f", 
              attemptedWithdrawal, overdraftLimit));
        this.overdraftLimit = overdraftLimit;
        this.attemptedWithdrawal = attemptedWithdrawal;
    }

    // Optional getters for detailed error handling
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public double getAttemptedWithdrawal() {
        return attemptedWithdrawal;
    }
}