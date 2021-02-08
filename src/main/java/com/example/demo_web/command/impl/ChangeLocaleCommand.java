package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.RequestParameter;
import com.example.demo_web.command.SessionRequestContent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangeLocaleCommand implements ActionCommand {
    @Override
    public String execute(SessionRequestContent sessionRequestContent) {
        String page = sessionRequestContent.getRequestParameter(RequestParameter.CURRENT_PAGE);
        String lang = sessionRequestContent.getRequestParameter(RequestParameter.LANG);
        sessionRequestContent.setSessionAttribute(RequestParameter.LANG, lang);
        return page;
    }
}
