package model;

import java.sql.Timestamp;

public class Transaction {
    private String account;
    private String type;
    private double amount;
    private String purpose;
    private Timestamp date;
    private int id; // 添加ID字段以便在修改时进行定位

    public Transaction(String account, String type, double amount, String purpose, Timestamp date, int id) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.purpose = purpose;
        this.date = date;
        this.id = id;
    }

    public Transaction(String account, String type, double amount, String purpose, Timestamp date) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.purpose = purpose;
        this.date = date;
    }
    public Transaction(String account, String type, double amount, String purpose) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.purpose = purpose;
    }

    public String getAccount() { return account; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getPurpose() { return purpose; }
    public int getId() { return id; }

    public Timestamp getDate() { return date; }
    // 添加setter方法
    public void setAccount(String account) { this.account = account; }
    public void setType(String type) { this.type = type; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setDate(Timestamp date) { this.date = date; }
    public void setId(int id) { this.id = id; }
}