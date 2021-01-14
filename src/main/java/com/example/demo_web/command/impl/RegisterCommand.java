package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.dao.UserDao;
import com.example.demo_web.dao.impl.UserDaoImpl;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.pool.MySqlDataSourceFactory;
import com.example.demo_web.resource.ConfigurationManager;
import com.example.demo_web.resource.MessageManager;
import com.example.demo_web.service.LoginService;
import com.example.demo_web.service.RegisterService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

public class RegisterCommand implements ActionCommand {
    private static final String PARAM_NAME_EMAIL = "email";
    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";
    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        String email = request.getParameter(PARAM_NAME_EMAIL);
        String login = request.getParameter(PARAM_NAME_LOGIN);
        String password = request.getParameter(PARAM_NAME_PASSWORD);
        RegisterService registerService = new RegisterService();
        UserDao userDao = new UserDaoImpl();
        Connection connection = MySqlDataSourceFactory.getConnection();
        userDao.setConnection(connection);

        if (registerService.isValidData(login, email, password)) {
           if (registerService.isNotRegistered(login)) {
               request.setAttribute("user", login);
               // определение пути к main.jsp
               try {
                   userDao.create(new User(login, email, password));
               } catch (DaoException e) {
                   e.printStackTrace();
               }
               page = ConfigurationManager.getProperty("path.page.main");
           } else {
               request.setAttribute("errorUserRegistered",
                       MessageManager.getProperty("message.userregistered"));
               page = ConfigurationManager.getProperty("path.page.registration");
           }
        } else {
            request.setAttribute("errorRegisterMessage",
                    MessageManager.getProperty("message.registererror"));
            page = ConfigurationManager.getProperty("path.page.registration");
        }
        return page;
    }
}
