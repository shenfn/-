package service;


import dao.UserDAO;
import model.User;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }



    // 注册方法
    public boolean register(String account, String password, String name, double balance) {
        // 可以先校验用户是否已经存在
        if (userDAO.findUserByAccount(account) != null) {
            System.out.println("账号已存在");
            return false;
        }
        return userDAO.registerUser(account, password, name, balance);
    }

    public boolean authenticateUser(String account, String password) {
        return userDAO.loginUser(account, password);
    }

    public User getInfoUser(String account) {

        return userDAO.getUserInfo(account);
    }


}
