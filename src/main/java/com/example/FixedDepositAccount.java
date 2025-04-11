package com.example;

// Source code is decompiled from a .class file using FernFlower decompiler.
import java.time.LocalDateTime;
// import java.time.temporal.ChronoUnit;

class FixedDepositAccount extends Account {
   protected int maturityMonths;
   private final LocalDateTime openingDate;

   public FixedDepositAccount(String accountNumber, String accountName, int maturityMonths) {
       super(accountNumber, accountName, "Fixed Deposit");
       this.maturityMonths = maturityMonths;
       this.openingDate = LocalDateTime.now();
   }

   public LocalDateTime getMaturityDate() {
       return openingDate.plusMonths(maturityMonths); // Calculate maturity date
   }

   public int getMaturityMonths() {
       return maturityMonths; // Return the maturity period
   }

   @Override
   public void deposit(double var1) {
      if (var1 % 100.0 != 0.0) {
         System.out.println("Deposit must be in multiples of 100.");
      } else {
         this.balance += var1;
         System.out.println("Deposited: " + var1);
      }

   }

   @Override
   public void withdraw(double amount) throws IllegalArgumentException {
       LocalDateTime maturityDate = getMaturityDate();

       if (LocalDateTime.now().isBefore(maturityDate)) {
           throw new IllegalArgumentException("Cannot withdraw before maturity date.");
       }
       if (amount <= 0.0) {
           throw new IllegalArgumentException("Invalid withdrawal amount.");
       }
       if (amount > this.balance) {
           throw new IllegalArgumentException("Insufficient funds.");
       }

       // Apply interest based on maturity period
       double interestRate = maturityMonths == 3 ? 0.16 : 0.23;
       this.balance += this.balance * interestRate; // Add interest
       this.balance -= amount; // Deduct withdrawal amount
   }

   @Override
   public void checkBalance() {
      System.out.println("Current balance: " + this.balance);
   }
}