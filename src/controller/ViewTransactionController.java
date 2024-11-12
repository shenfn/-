package controller;

import dao.TransactionDAO;
import dao.TransactionDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import model.Transaction;
import model.User;
import service.UserService;
import util.AlertHelper;

import java.io.IOException;
import java.util.List;

import static controller.MainController.getCurrentAccount;

public class ViewTransactionController {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, String> purposeColumn;

    @FXML
    private TableColumn<Transaction, java.sql.Timestamp> dateColumn;

    @FXML
    private TableColumn<Transaction, Void> editColumn;

    @FXML
    private Button backButton;

    UserDAO userDAO = new UserDAOImpl();
    private final UserService userService = new UserService(userDAO); // 假设 userService 已被实例
    private final TransactionDAO transactionDAO = new TransactionDAOImpl();
    AlertHelper alertHelper = new AlertHelper();

    private String account;

    public void initialize() {
        System.out.println("Initializing ViewTransactionController...");

        // 设置表格列的单元格值工厂
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // 设置编辑按钮列
        editColumn.setCellFactory(col -> new TableCell<Transaction, Void>() {
            private final Button editButton = new Button("编辑");

            {
                editButton.setOnAction(e -> {
                    Transaction selectedTransaction = (Transaction) getTableRow().getItem();
                    System.out.println("9090");
                    if (selectedTransaction != null) {
                        System.out.println("1010");
                        openEditDialog(selectedTransaction);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    System.out.println("2323");
                    setGraphic(editButton);
                }
            }
        });

        // 获取并显示交易数据
        loadTransactionData();
    }

    public void setAccount(String account) {
        this.account = account;
        loadTransactionData(); // 载入账户相关数据
    }

    private void loadTransactionData() {
        if (account != null) {
            List<Transaction> transactions = transactionDAO.getTransactions(account);
            transactionTable.setItems(FXCollections.observableArrayList(transactions));
        }
    }

    private void openEditDialog(Transaction transaction) {
        // 获取交易记录的 transaction_id
        int transactionId = transaction.getId();
        System.out.println("打开编辑对话框，交易记录 ID: " + transactionId);  // 添加调试信息
        double oldAmount = transaction.getAmount();
        // 打开编辑对话框
        TextField amountField = new TextField(String.valueOf(transaction.getAmount()));
        TextField purposeField = new TextField(transaction.getPurpose());
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("收入", "支出");
        typeComboBox.setValue(transaction.getType());

        Button saveButton = new Button("保存");
        saveButton.setOnAction(e -> {
            transaction.setAmount(Double.parseDouble(amountField.getText()));
            transaction.setPurpose(purposeField.getText());
            transaction.setType(typeComboBox.getValue());

            // 更新数据库
            boolean success = transactionDAO.updateTransactionInDatabase(transaction,oldAmount);

            if (success) {
                AlertHelper.showInfoAlert("保存成功", "交易记录已成功更新！");
                transactionTable.refresh();  // 更新表格
            } else {
                AlertHelper.showInfoAlert("操作失败", "更新交易记录失败，请重试！");
            }
        });

        Button deleteButton = new Button("删除");
        deleteButton.setOnAction(e -> {
            System.out.println("删除按钮点击，交易记录 ID: " + transactionId);  // 打印 ID

            // 删除交易记录
            boolean success = transactionDAO.deleteTransaction(transactionId);
            if (success) {
                AlertHelper.showInfoAlert("删除成功", "交易记录已成功删除！");
                transactionTable.getItems().remove(transaction);  // 更新表格
            } else {
                AlertHelper.showInfoAlert("操作失败", "删除交易记录失败，请重试！");
            }
        });

        VBox dialogLayout = new VBox(10, new Label("Amount:"), amountField, new Label("Purpose:"), purposeField, new Label("Type:"), typeComboBox, saveButton, deleteButton);
        dialogLayout.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(dialogLayout, 400, 300);
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Transaction");
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    @FXML
    private void handleBackButton() {
        try {
            Stage stage = Main.getPrimaryStage();

            Parent root = FXMLLoader.load(getClass().getResource("/view/transaction_details.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertHelper.showErrorAlert("错误", "无法加载主菜单界面！");
            e.printStackTrace();
        }
        // 返回到transaction_details页面的逻辑
    }

}