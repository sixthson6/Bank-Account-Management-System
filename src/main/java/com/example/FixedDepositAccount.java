package com.example;

// Source code is decompiled from a .class file using FernFlower decompiler.
import java.time.LocalDateTime;

class FixedDepositAccount extends Account {
   private int maturityMonths;
   private LocalDateTime openingDate;

   public FixedDepositAccount(String var1, String var2, int var3) {
      super(var1, var2, "Fixed Deposit");
      this.maturityMonths = var3;
      this.openingDate = LocalDateTime.now();
   }

   public void deposit(double var1) {
      if (var1 % 100.0 != 0.0) {
         System.out.println("Deposit must be in multiples of 100.");
      } else {
         this.balance += var1;
         System.out.println("Deposited: " + var1);
      }

   }

   public void withdraw(double var1) {
      LocalDateTime var3 = this.openingDate.plusMonths((long)this.maturityMonths);
      if (LocalDateTime.now().isBefore(var3)) {
         System.out.println("Cannot withdraw before maturity date.");
      } else if (var1 <= 0.0) {
         System.out.println("Invalid withdrawal amount.");
      } else if (var1 > this.balance) {
         System.out.println("Insufficient funds.");
      } else {
         double var4 = this.maturityMonths == 3 ? 0.16 : 0.23;
         this.balance += this.balance * var4;
         this.balance -= var1;
         System.out.println("Withdrawn: " + var1);
      }

   }

   public void checkBalance() {
      System.out.println("Current balance: " + this.balance);
   }
}

