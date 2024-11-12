package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Main;
import service.UserService;
import util.AlertHelper;
import java.io.IOException;

public class MainController {

    @FXML
    private TextField accountField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button acountButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;

    private static String currentAccount;

    public static void setCurrentAccount(String account) {
        currentAccount = account;
    }

    public static String getCurrentAccount() {
        return currentAccount;
    }


    @FXML
    public void initialize() {

        System.out.println("Initializing MainController...");


        if (loginButton == null) {
            System.out.println("Login button is not initialized");
        }
        if (acountButton == null) {
            System.out.println("acount button is not initialized");
        }
        acountButton.setOnAction(this::handleCount);
        loginButton.setOnAction(this::handleLogin);
        exitButton.setOnAction(this::handleExit);
    }

    @FXML
    private void handleCount(ActionEvent actionEvent) {
        try {
            Stage stage = Main.getPrimaryStage();
            // 加载注册页面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            System.out.println("333");

            Parent mainPage = loader.load();
            Scene scene = new Scene(mainPage);
            stage.setScene(scene);
            stage.show();
            stage.setTitle("注册");
        } catch (IOException e) {
            System.out.println("444");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        UserDAO userDAO = new UserDAOImpl();
        UserService userService = new UserService(userDAO);
        String account = accountField.getText();
        String password = passwordField.getText();

        boolean isValid = userService.authenticateUser(account, password); // 验证用户登录信息
        if (isValid) {
            try {
                setCurrentAccount(account);
                Stage stage = Main.getPrimaryStage();
                // 加载登录后的主分区页面
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/transaction_details.fxml"));
                Parent mainPage = loader.load();

                // 使用 window 来切换场景
                stage.setScene(new Scene(mainPage));
                stage.setTitle("主界面");
                System.out.println("Login success, but no scene change for testing.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertHelper.showLoginFailedAlert(); // 提示用户登录失败
            System.out.println("Login failed.");

        }
    }

    @FXML
    private void handleExit(ActionEvent actionEvent) {
        Stage stage = Main.getPrimaryStage();
        // 关闭应用程序
        stage.close();
    }

}
