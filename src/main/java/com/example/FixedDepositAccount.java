package com.example;

// Source code is decompiled from a .class file using FernFlower decompiler.
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class FixedDepositAccount extends Account {
   protected int maturityMonths;
   private final LocalDateTime openingDate;
   private LocalDateTime lastDepositTime;
   private LocalDateTime lastWithdrawTime;
   private double interest;

   public FixedDepositAccount(String var1, String var2, int var3) {
      super(var1, var2, "Fixed Deposit");
      this.maturityMonths = var3;
      this.openingDate = LocalDateTime.now();
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
   public void withdraw(double var1) {
      LocalDateTime var3 = this.openingDate.plusMonths((long)this.maturityMonths);
      if (LocalDateTime.now().isBefore(var3)) {
         System.out.println("Cannot withdraw before maturity date.");
      // } else if (var1 <= 0.0) {
      //    System.out.println("Invalid withdrawal amount.");
      // } else if (var1 > this.balance) {
      //    System.out.println("Insufficient funds.");
      } else {
         double var4 = this.maturityMonths == 3 ? 0.16 : 0.23;
         this.balance += this.balance * var4;
         this.balance -= var1;
         System.out.println("Withdrawn: " + var1);
      }

   }

   public void  calcInterest() {
      if (this.lastDepositTime != null && this.lastWithdrawTime != null) {
         long days = ChronoUnit.SECONDS.between(this.lastDepositTime, this.lastWithdrawTime); 
         double interest = (this.balance * 0.04 * days) / 365; // Assuming 5% annual interest rate
         this.balance += interest;
         System.out.println("Interest added: " + interest);
      } else {
         System.out.println("No interest added, no deposits or withdrawals made.");
      }
      System.out.printf("Interest of %.2f added. Total balance: %.2f\n", interest, balance);
      // return this.interest;
    }

   public void checkMaturity() {
      LocalDateTime var2 = this.openingDate.plusMonths((long)this.maturityMonths);
      if (LocalDateTime.now().isBefore(var2)) {
         System.out.println("Account is not yet matured. Maturity date: " + var2);
      } else {
         System.out.println("Account is matured.");
      }
      System.out.println("Maturity date: " + var2);
   }

   public LocalDateTime getMaturityDate() {
      // if (this.lastDepositTime != null) {
          // Assuming maturity is 30 days after the last deposit
      return this.lastDepositTime;
      // } else {
      //     System.out.println("No deposits made. Maturity date cannot be determined.");
      //     return null; // Return null if no deposits have been made
      // }
  }

   @Override
   public void checkBalance() {
      System.out.println("Current balance: " + this.balance);
   }
}

