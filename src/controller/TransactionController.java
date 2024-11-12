package controller;

import dao.TransactionDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import model.User;
import service.UserService;

import java.io.IOException;
import java.util.List;

import static controller.MainController.getCurrentAccount;
import model.Transaction;
import dao.TransactionDAO;
import util.AlertHelper;

public class TransactionController {

    @FXML private TabPane tabPane;
    @FXML private VBox homeContent;
    @FXML private Label welcomeLabel;
    @FXML private VBox accountContent;
    @FXML private Button viewTransactionsButton;
    @FXML private Button addTransactionButton;
    @FXML private VBox settingsContent;
    @FXML private Button logoutButton;
    @FXML private Button backButton;


    //添加记账分区

    UserDAO userDAO = new UserDAOImpl();
    private final UserService userService = new UserService(userDAO); // 假设 userService 已被实例
    private final TransactionDAO transactionDAO = new TransactionDAOImpl();
    AlertHelper alertHelper = new AlertHelper();

    @FXML
    public void initialize() {
        System.out.println("Initializing TransactionController...");

        // 这里可以初始化视图
        String account = getCurrentAccount();
        User user = userService.getInfoUser(account);

        welcomeLabel.setText(user.getName() + " 用户登录成功！欢迎\n当前余额: " + user.getBalance());

        // 绑定按钮事件
        viewTransactionsButton.setOnAction(this::viewTransactions);
        addTransactionButton.setOnAction(this::addTransaction);
        logoutButton.setOnAction(this::logout);
        backButton.setOnAction(this::showMainMenu);

    }



    // 主页区内容
    private void viewTransactions(ActionEvent event) {
        System.out.println("5656");
        String account = getCurrentAccount();

        // 切换到交易明细界面
        // 你可以在这里创建新的页面或更新当前页面
        try {
            // 加载FXML文件
            Stage stage = Main.getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/view_transactions.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file location is null. Check if the file exists and the path is correct.");
            }
            System.out.println("6767");

            Parent viewTransactionRoot = loader.load();

            // 获取当前窗口并切换场景
            stage.setScene(new Scene(viewTransactionRoot));
            stage.setTitle("查看明细");

            // 传递参数（如果需要）
            ViewTransactionController controller = loader.getController();
            controller.setAccount(account); // 替换为实际的账户参数

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alertHelper.showInfoAlert("加载错误", "无法加载交易明细页面。");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            alertHelper.showInfoAlert("加载错误", "FXML路径问题: " + e.getMessage());
        }
    }



    // 记账分区内容
    private void addTransaction(ActionEvent event) {
        System.out.println("添加记账");
        // 切换到添加交易页面
        // 在此页面处理表单输入并提交
        String account = getCurrentAccount();

        try {
            // 加载FXML文件
            Stage stage = Main.getPrimaryStage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_transactions.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file location is null. Check if the file exists and the path is correct.");
            }

            Parent viewTransactionRoot = loader.load();

            // 获取当前窗口并切换场景
            stage.setScene(new Scene(viewTransactionRoot));
            stage.setTitle("添加记账");

            // 传递参数（如果需要）
            ViewTransactionController controller = loader.getController();
            controller.setAccount(account); // 替换为实际的账户参数

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alertHelper.showInfoAlert("加载错误", "无法加载添加记账页面。");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            alertHelper.showInfoAlert("加载错误", "FXML路径问题: " + e.getMessage());
        }
    }

    // 设置分区内容
    private void logout(ActionEvent event) {
        String account = getCurrentAccount();
        System.out.println("注销");
        // 处理注销逻辑，显示确认对话框等
        System.out.println("account" + account);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("注销");
        alert.setHeaderText(null);
        alert.setContentText("是否确认注销");

        // 创建“确定”和“取消”按钮
        ButtonType okButton = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);

        // 添加按钮到 Alert 中
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // 显示弹窗并等待用户响应
        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                userDAO.deletUser(account);
                //返回主菜单
                try {
                    Stage stage = Main.getPrimaryStage(); // 从 main.Main 类中获取 Stage

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                    Parent mainView = loader.load();


                    Scene scene = new Scene(mainView, 500, 400);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 执行注销操作
            } else {
                System.out.println("用户选择了取消");
                // 不执行任何操作
            }
        });
    }

    private void showMainMenu(ActionEvent event) {
        System.out.println("返回主菜单");
        // 返回主菜单逻辑
        try {
            Stage stage = Main.getPrimaryStage(); // 从 main.Main 类中获取 Stage

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
            Parent mainView = loader.load();


            Scene scene = new Scene(mainView, 500, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
