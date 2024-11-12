package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.UserService;

import java.io.IOException;

public class UserController {

    @FXML
    private TextField accountField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField balanceField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;
    UserDAO userDAO = new UserDAOImpl();
    private final UserService userService = new UserService(userDAO); //

    // 处理注册按钮点击事件
    @FXML
    private void handleRegister() {
        String account = accountField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String balance = balanceField.getText();

        // 验证输入是否为空
        if (account.isEmpty() || password.isEmpty() || name.isEmpty() || balance.isEmpty()) {
            showAlert("提示", "所有字段均为必填项！");
            return;
        }

        // 尝试将字符串余额转换为数字，验证合法性
        try {
            double balanceValue = Double.parseDouble(balance);
            boolean success = userService.register(account, password, name, balanceValue);

            if (success) {
                showAlert("成功", "注册成功！");
                // 注册成功后跳转到主菜单界面
                Thread.sleep(100); // 100毫秒的延迟

                loadMainMenu();
            } else {
                showAlert("失败", "注册失败，请稍后再试。");
            }
        } catch (NumberFormatException e) {
            showAlert("错误", "请输入有效的余额数字！");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // 处理返回按钮点击事件
    @FXML
    private void handleBack() {
        // 返回逻辑，例如返回上一页面
        System.out.println("返回到上一页面");
        loadMainMenu();
        //showAlert("提示", "返回到上一页面");
    }

    // 显示弹窗的帮助方法
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 加载主菜单界面
    private void loadMainMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow(); // 获取当前窗口
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("错误", "无法加载主菜单界面！");
            e.printStackTrace();
        }
    }
}
