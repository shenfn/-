package dao;

import model.Transaction;
import java.util.List;

public interface TransactionDAO {
    boolean updateTransactionInDatabase(Transaction transaction, double oldAmount);
    boolean deleteTransaction(int transactionId);
    List<Transaction> getTransactions(String account);
    boolean addTransaction(String account, String type, double amount, String purpose);
}