package com.example;

abstract class Account implements BankOperations {
    protected String accountNumber;
    protected String accountName;
    protected String accountType;
    protected double balance;
 
    public Account(String var1, String var2, String var3) {
       this.accountNumber = var1;
       this.accountName = var2;
       this.accountType = var3;
       this.balance = 0.0;
    }
}
