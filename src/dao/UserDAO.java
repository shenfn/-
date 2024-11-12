package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.DatabaseHelper.getConnection;

public interface UserDAO {

    boolean loginUser(String account, String password);
    boolean registerUser(String account, String password, String name, double balance);
    User getUserInfo(String account);
    void deletUser(String account);
    User findUserByAccount(String account);                   // 根据账号查找用户

    public static double getUserBalance(String account) {
        double balance = 0.0;
        String sql = "SELECT balance FROM users WHERE account = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
    public static boolean updateUserBalance(String account, String type, double amount) {
        // 获取当前用户余额
        double currentBalance = getUserBalance(account);

        // 根据类型更新余额
        double newBalance = ("收入".equals(type)) ? currentBalance + amount : currentBalance - amount;

        // 更新用户表中的余额
        String sql = "UPDATE users SET balance = ? WHERE account = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, account);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}



