package dao;

import model.Transaction;
import dao.UserDAO;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import static util.DatabaseHelper.getConnection;

public class TransactionDAOImpl implements TransactionDAO {
    @Override
    public boolean updateTransactionInDatabase(Transaction transaction, double oldAmount) {
        String sql = "UPDATE transaction_details SET amount = ?, purpose = ?, type = ? WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 更新交易记录
            pstmt.setDouble(1, transaction.getAmount());
            pstmt.setString(2, transaction.getPurpose());
            pstmt.setString(3, transaction.getType());
            pstmt.setInt(4, transaction.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // 计算新的差值并更新用户余额

                double difference = transaction.getAmount() - oldAmount;
                boolean balanceUpdated = UserDAO.updateUserBalance(transaction.getAccount(), transaction.getType(), difference);

                if (balanceUpdated) {
                    System.out.println("交易记录及余额更新成功！");
                    return true;
                } else {
                    System.out.println("余额更新失败！");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean deleteTransaction(int transactionId) {
        String sql = "DELETE FROM transaction_details WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);  // 使用 transaction_id 删除指定记录

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // 如果有记录被删除，返回 true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> getTransactions(String account) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, account, type, amount, purpose, date FROM transaction_details WHERE account = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // 确保 id 被正确读取并赋值
                int id = rs.getInt("id");
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String purpose = rs.getString("purpose");
                Timestamp date = rs.getTimestamp("date");

                // 创建 Transaction 对象时传入 id
                Transaction transaction = new Transaction(account, type, amount, purpose, date, id);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public boolean addTransaction(String account, String type, double amount, String purpose) {
        String sql = "INSERT INTO transaction_details (account, type, amount, purpose) VALUES (?, ?, ?, ?)";
        boolean transactionSuccess = false;
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, purpose);
            pstmt.executeUpdate();
            transactionSuccess = true;  // 成功插入交易记录
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // 插入失败
        }

        // 更新用户余额
        if (transactionSuccess) {
            // 更新余额
            boolean balanceSuccess = UserDAO.updateUserBalance(account, type, amount);
            return balanceSuccess;  // 返回余额更新的结果
        }

        return false;  // 如果插入失败，直接返回失败
    }
}
