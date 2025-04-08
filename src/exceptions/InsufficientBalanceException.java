/**
 * Thrown when a withdrawal would cause the account balance to go below the minimum required amount
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("Insufficient funds for this transaction");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(double currentBalance, double requiredMinimum) {
        super(String.format("Current balance $%.2f is below required minimum $%.2f", 
              currentBalance, requiredMinimum));
    }
}