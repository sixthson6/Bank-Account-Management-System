package com.example;

// Source code is decompiled from a .class file using FernFlower decompiler.
class CurrentAccount extends Account {
    protected final double OVERDRAFT_LIMIT = 500.0;
 
    public CurrentAccount(String var1, String var2) {
       super(var1, var2, "Current");
    }
 
    @Override
    public void deposit(double var1) {
       this.balance += var1;
       System.out.println("Deposited: " + var1);
    }
 
    @Override
    public void withdraw(double var1) {
       if (var1 <= 0.0) {
          System.out.println("Invalid withdrawal amount.");
       } else if (var1 > this.balance + 500.0) {
          System.out.println("Withdrawal exceeds overdraft limit.");
       } else {
          this.balance -= var1;
          System.out.println("Withdrawn: GHS " + var1);
       }
 
    }
 
    @Override
    public void checkBalance() {
       System.out.println("Current balance: " + this.balance);
    }
 }
 