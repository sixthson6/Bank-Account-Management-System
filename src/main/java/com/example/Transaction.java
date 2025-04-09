package com.example;

import java.time.LocalDateTime;
import java.util.LinkedList;

class Transaction {
   String accountNumber;
   String accountName;
   String type;
   double amount;
   LocalDateTime timestamp;
   private final LinkedList<Transaction> list = new LinkedList<>();


   public Transaction(String var1, String var2, String var3, double var4) {
      this.accountNumber = var1;
      this.accountName = var2;
      this.type = var3;
      this.amount = var4;
      this.timestamp = LocalDateTime.now();
   }

   public String toString() {
      return String.format("[%s] (%s) | %s | %s | %.2f GHS", this.timestamp, this.accountName, this.accountNumber, this.type, this.amount);
   }
}
