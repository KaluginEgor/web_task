package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements ActionCommand {
    @Override
    public String execute(SessionRequestContent sessionRequestContent) {
        String page = ConfigurationManager.getProperty("path.page.index");
        //request.getSession().invalidate();
        return page;
    }
}
