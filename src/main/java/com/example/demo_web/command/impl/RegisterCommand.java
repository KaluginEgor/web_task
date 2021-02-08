package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.RequestParameter;
import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.entity.User;
import com.example.demo_web.manager.ConfigurationManager;
import com.example.demo_web.manager.MessageManager;
import com.example.demo_web.service.RegisterService;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements ActionCommand {

    @Override
    public String execute(SessionRequestContent sessionRequestContent) {
        String page = null;
        String email = sessionRequestContent.getRequestParameter(RequestParameter.EMAIL);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        RegisterService registerService = new RegisterService();

        if (registerService.isValidData(login, email, password)) {
           if (registerService.isNotRegistered(login)) {
               sessionRequestContent.setRequestAttribute("user", login);
               registerService.registerUser(login, email, password);
               page = ConfigurationManager.getProperty("path.page.main");
           } else {
               sessionRequestContent.setRequestAttribute("errorUserRegistered",
                       MessageManager.getProperty("message.userregistered"));
               page = ConfigurationManager.getProperty("path.page.registration");
           }
        } else {
            sessionRequestContent.setRequestAttribute("errorRegisterMessage",
                    MessageManager.getProperty("message.registererror"));
            page = ConfigurationManager.getProperty("path.page.registration");
        }
        return page;
    }
}
