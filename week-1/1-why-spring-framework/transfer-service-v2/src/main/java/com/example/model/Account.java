package com.example.model;

public class Account {

    private String number;
    private String holderName;
    private double balance;

    public Account(String number, String holderName, double balance) {
        this.number = number;
        this.holderName = holderName;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    @Override
    public String toString() {
        return "Account{" +
                "number='" + number + '\'' +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                '}';
    }

}
