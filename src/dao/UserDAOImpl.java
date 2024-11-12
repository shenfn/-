package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static util.DatabaseHelper.getConnection;

public class UserDAOImpl implements UserDAO{

    @Override
    public boolean loginUser(String account, String password) {
        System.out.println("99" + account + password);
        String sql = "SELECT * FROM users WHERE account = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("登录成功，账户余额: " + rs.getDouble("balance"));
                return true;
            } else {
                System.out.println("账号或密码错误");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registerUser(String account, String password, String name, double balance) {
        String sql = "INSERT INTO users (account, password, name, balance) VALUES (?, ?, ?, ?)";
        if(account != "" && password != "" && name != "" ) {

            try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, account);
                pstmt.setString(2, password);
                pstmt.setString(3, name);
                pstmt.setDouble(4, balance);
                int result = pstmt.executeUpdate();
                return result > 0;  // 成功插入则返回true
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public User getUserInfo(String account) {
        String sql = "SELECT name, balance FROM users WHERE account = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");
                return new User(name, balance);  // 返回包含用户名和余额的对象
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deletUser(String account) {
        String sql = "DELETE FROM users WHERE account = ?";
        System.out.println("accout = " + account);
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);  // 设置要删除的用户名
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("用户删除成功！");
            } else {
                System.out.println("未找到该用户！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new SQLException("删除用户时出错", e);
        }
    }

    @Override
    public User findUserByAccount(String account) {
        String sql = "SELECT * FROM users WHERE account = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 如果找到用户，创建并返回 User 对象
                return new User(
                        rs.getString("account"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 没有找到用户，返回null
    }

}
