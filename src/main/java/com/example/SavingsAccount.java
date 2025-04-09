package com.example;

// Source code is decompiled from a .class file using FernFlower decompiler.
class SavingsAccount extends Account {
    private final double MIN_BALANCE = 100.0;
 
    public SavingsAccount(String var1, String var2) {
       super(var1, var2, "Savings");
    }
 
    public void deposit(double var1) {
       this.balance += var1;
       System.out.println("Deposited: " + var1);
    }
 
    public void withdraw(double var1) {
       if (var1 <= 0.0) {
          System.out.println("Invalid withdrawal amount.");
       } else if (var1 > this.balance) {
          System.out.println("Insufficient funds.");
       } else {
          this.balance -= var1;
          System.out.println("Withdrawn: " + var1);
       }
 
    }
 
    public void checkBalance() {
       System.out.println("Current balance: " + this.balance);
    }
 }
 
