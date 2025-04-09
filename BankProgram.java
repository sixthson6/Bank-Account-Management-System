import java.time.LocalDateTime;
import java.util.*;

interface BankOperations {
    void deposit(double amount);
    void withdraw(double amount);
    void checkBalance();
}

abstract class Account implements BankOperations {
    protected String accountNumber;
    protected String accountName;
    protected String accountType;
    protected double balance;

    public Account(String accountNumber, String accountName, String accountType) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = 0.0;
    }
}

class SavingsAccount extends Account {
    private final double MIN_BALANCE = 100.0;

    public SavingsAccount(String accountNumber, String accountName) {
        super(accountNumber, accountName, "Savings");
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
        } else if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        }
    }

    public void checkBalance() {
        System.out.println("Current balance: " + balance);
    }
}

class CurrentAccount extends Account {
    private final double OVERDRAFT_LIMIT = 500.0;

    public CurrentAccount(String accountNumber, String accountName) {
        super(accountNumber, accountName, "Current");
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
        } else if (amount > (balance + OVERDRAFT_LIMIT)) {
            System.out.println("Withdrawal exceeds overdraft limit.");
        } else {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        }
    }

    public void checkBalance() {
        System.out.println("Current balance: " + balance);
    }
}

class FixedDepositAccount extends Account {
    private int maturityMonths;
    private LocalDateTime openingDate;

    public FixedDepositAccount(String accountNumber, String accountName, int maturityMonths) {
        super(accountNumber, accountName, "Fixed Deposit");
        this.maturityMonths = maturityMonths;
        this.openingDate = LocalDateTime.now();
    }

    public void deposit(double amount) {
        if (amount % 100 != 0) {
            System.out.println("Deposit must be in multiples of 100.");
        } else {
            balance += amount;
            System.out.println("Deposited: " + amount);
        }
    }

    public void withdraw(double amount) {
        LocalDateTime maturityDate = openingDate.plusMonths(maturityMonths);
        if (LocalDateTime.now().isBefore(maturityDate)) {
            System.out.println("Cannot withdraw before maturity date.");
        } else if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
        } else if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else {
            double interestRate = (maturityMonths == 3) ? 0.16 : 0.23;
            balance += balance * interestRate;
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        }
    }

    public void checkBalance() {
        System.out.println("Current balance: " + balance);
    }
}

class Transaction {
    String accountNumber;
    String accountName;
    String type;
    double amount;
    LocalDateTime timestamp;

    public Transaction(String accountNumber, String accountName, String type, double amount) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String toString() {
        return String.format("[%s] %s | %s | %.2f", timestamp, accountNumber, type, amount);
    }
}

class Transactions {
    private LinkedList<Transaction> transactions = new LinkedList<>();

    public void addTransaction(String accountNumber, String accountName, String type, double amount) {
        transactions.add(new Transaction(accountNumber, accountName, type, amount));
    }

    public void viewTransactionsList() {
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
}

public class BankProgram {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Transactions transactionLog = new Transactions();

        System.out.print("Enter account number: ");
        String accNum = scanner.nextLine();

        System.out.print("Enter account name: ");
        String accName = scanner.nextLine();

        System.out.print("Enter account type (Savings/Current/Fixed): ");
        String accType = scanner.nextLine();

        Account account = null;
        if (accType.equalsIgnoreCase("Savings")) {
            account = new SavingsAccount(accNum, accName);
        } else if (accType.equalsIgnoreCase("Current")) {
            account = new CurrentAccount(accNum, accName);
        } else if (accType.equalsIgnoreCase("Fixed")) {
            System.out.print("Enter maturity period in months (3 or 6): ");
            int months = scanner.nextInt();
            // add error handling for invalid input. if not 3 or 6, ask again
            while (months != 3 && months != 6) {
                System.out.print("Invalid input. Enter maturity period in months (3 or 6): ");
                months = scanner.nextInt();
            }
            scanner.nextLine(); // consume newline
            account = new FixedDepositAccount(accNum, accName, months);
        } else {
            System.out.println("Invalid account type");
            return;
        }

        while (true) {
            System.out.println("\n1. Deposit\n2. Withdraw\n3. Check Balance\n4. View Transactions\n5. Exit");
            System.out.print("Choose an option: ");
            // add error handling for invalid input. if not 1-5, ask again
            // while (true) {
            //     if (scanner.hasNextInt()) {
            //         break;
            //     } else {
            //         System.out.print("Invalid input. Choose an option (1-5): ");
            //         scanner.next(); // consume invalid input
            //     }
            // }
            
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depAmt = scanner.nextDouble();
                    scanner.nextLine();
                    account.deposit(depAmt);
                    transactionLog.addTransaction(accNum, accName, "Deposit", depAmt);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withAmt = scanner.nextDouble();
                    scanner.nextLine();
                    account.withdraw(withAmt);
                    transactionLog.addTransaction(accNum, accName, "Withdraw", withAmt);
                    break;
                case 3:
                    account.checkBalance();
                    break;
                case 4:
                    transactionLog.viewTransactionsList();
                    break;
                case 5:
                    System.out.println("Thank you for using our bank service.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
