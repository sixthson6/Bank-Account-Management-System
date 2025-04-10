package com.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Source code is decompiled from a .class file using FernFlower decompiler.
class SavingsAccount extends Account {
    final double MIN_BALANCE = 100.0;
    private LocalDateTime lastDepositTime;
    private LocalDateTime lastWithdrawTime;
    private double interest;
 
    public SavingsAccount(String var1, String var2) {
       super(var1, var2, "Savings");
    }
 
    @Override
    public void deposit(double var1) {
      if (var1 <= 0) {
         System.out.println("Invalid deposit amount.");
         return;
     }
       this.balance += var1;
       this.lastDepositTime = LocalDateTime.now();
       System.out.println("Deposited: " + var1);
    }
 
    @Override
    public void withdraw(double var1) {
       if (var1 <= 0.0) {
         System.out.println("Invalid withdrawal amount.");
       } else {
         this.lastWithdrawTime = LocalDateTime.now();
         this.calcInterest();
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

  
 
    @Override
    public void checkBalance() {
       System.out.println("Current balance: " + this.balance);
    }
 }
 
