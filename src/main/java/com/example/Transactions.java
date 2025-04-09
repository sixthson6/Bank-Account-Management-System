package com.example;

// Source code is decompiled from a .class file using FernFlower decompiler.
import java.util.Iterator;
import java.util.LinkedList;

import javafx.scene.control.TextArea;

class Transactions {
   private LinkedList<Transaction> transactions = new LinkedList();

   Transactions() {
   }

   public void addTransaction(String var1, String var2, String var3, double var4) {
      this.transactions.add(new Transaction(var1, var2, var3, var4));
   }

   public String viewTransactionsList() {
      Iterator var1 = this.transactions.iterator();

      System.out.println("--------------------------------------------------");
      while(var1.hasNext()) {
         Transaction var2 = (Transaction)var1.next();
         System.out.println(var2);
        }
        
        System.out.println("--------------------------------------------------");
        return null;
   }

   public String viewTransactionsListString() {
    StringBuilder sb = new StringBuilder();
    for (Transaction t : transactions) sb.append(t).append("\n");
    return sb.toString();
   }


    boolean isAccountNumberExists(String num) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void viewTransactionsListString(TextArea outputArea) {
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) sb.append(t).append("\n");
        outputArea.setText(sb.toString());

    }
}

