package controller;

import dao.TransactionDAOImpl;
import dao.UserDAOImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import model.Transaction;
import model.User;
import service.UserService;
import util.AlertHelper;
import dao.TransactionDAO; // 替换为实际的 DAO 类路径
import dao.UserDAO; // 替换为实际的 DAO 类路径

import java.io.IOException;
import java.util.List;

import static controller.MainController.getCurrentAccount;

public class AddTransactionController {

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private TextField amountField;

    @FXML
    private TextField purposeField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button submitButton;

    @FXML
    private Button backButton;


    UserDAO userDAO = new UserDAOImpl();
    private final UserService userService = new UserService(userDAO); // 假设 userService 已被实例
    private final TransactionDAO transactionDAO = new TransactionDAOImpl();
    AlertHelper alertHelper = new AlertHelper();

    public void initialize() {
        System.out.println("Initializing AddController...");

        // 设置默认交易类型
        typeChoiceBox.setValue("收入");
    }

    // 处理提交按钮点击事件
    @FXML
    private void handleSubmit() {
        try {
            String account = getCurrentAccount();
            String type = typeChoiceBox.getValue();
            double amount = Double.parseDouble(amountField.getText());
            String purpose = purposeField.getText();

            // 创建新的交易记录并保存到数据库
            Transaction newTransaction = new Transaction(account, type, amount, purpose);
            boolean success = transactionDAO.addTransaction(
                    newTransaction.getAccount(),
                    newTransaction.getType(),
                    newTransaction.getAmount(),
                    newTransaction.getPurpose()
            );

            // 显示操作结果
            if (success) {
                AlertHelper.showInfoAlert("操作成功", "交易记录已成功添加！");
                // 这里你可以选择清空表单或者返回上一个页面
            } else {
                AlertHelper.showErrorAlert("操作失败", "添加交易记录失败，请重试！");
            }
        } catch (NumberFormatException e) {
            AlertHelper.showErrorAlert("错误", "请输入有效的金额！");
        } catch (Exception e) {
            AlertHelper.showErrorAlert("错误", "发生错误，请检查输入并重试！");
        }
    }

    // 处理返回按钮点击事件
    @FXML
    private void handleBack() {
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
