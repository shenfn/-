package model;

public class User {
    private String account;
    private String password;
    private String name;
    private double balance;

    // 构造方法，用于初始化用户信息
    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public User(String account, String password, String name, double balance) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }
}
