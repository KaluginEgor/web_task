package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.RequestParameter;
import com.example.demo_web.service.LoginService;
import com.example.demo_web.manager.ConfigurationManager;
import com.example.demo_web.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        String login = request.getParameter(RequestParameter.LOGIN);
        String password = request.getParameter(RequestParameter.PASSWORD);
        LoginService loginService = new LoginService();

        if (loginService.isValidData(login, password)) {
            if (loginService.isRegistered(login, password)) {
                request.setAttribute("user", login);
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                request.setAttribute("errorLoginPassMessage",
                        MessageManager.getProperty("message.loginerror"));
                page = ConfigurationManager.getProperty("path.page.login");
            }
        } else {
            request.setAttribute("errorLoginDataMessage",
                    MessageManager.getProperty("message.logindataerror"));
            page = ConfigurationManager.getProperty("path.page.login");
        }
        return page;
    }
}