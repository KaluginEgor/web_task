package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.RequestParameter;
import com.example.demo_web.entity.User;
import com.example.demo_web.manager.ConfigurationManager;
import com.example.demo_web.manager.MessageManager;
import com.example.demo_web.service.RegisterService;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        String email = request.getParameter(RequestParameter.EMAIL);
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        RegisterService registerService = new RegisterService();

        if (registerService.isValidData(login, email, password)) {
           if (registerService.isNotRegistered(login)) {
               request.setAttribute("user", login);
               registerService.registerUser(login, email, password);
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
