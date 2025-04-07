# Bank-Account-Management-System

## `src`:

### `Main.java`:

### `Transactions`: Represents a single node as a transaction

### `model/`:

#### `BankAccount`: Base class for all bank account types (Savings, Current, Fixed Deposit).

##### Instance Variables

`accountNumber` (String): Unique account identifier.

`accountHolderName` (String): Name of the account owner.

`balance` (double): Current account balance.

`transactionHistory` (LinkedList<Transaction>): Stores transaction records.

##### Key Methods

###### Abstract (Must Implement in Child Classes)

`withdraw(double amount)` → Handles withdrawals with account-specific rules.

`calculateInterest()` → Applies interest (Savings/Fixed Deposit only).

###### Concrete (Shared Functionality)

`deposit(double amount)` → Adds funds to balance.

`getBalance()` → Returns current balance.

`addTransaction(String type, double amount)` → Logs transactions.

`getLastNTransactions(int n)` → Retrieves recent transactions.
