package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.RequestParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangeLocaleCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        String page = request.getParameter(RequestParameter.CURRENT_PAGE);
        String lang = request.getParameter(RequestParameter.LANG);
        HttpSession session = request.getSession();
        session.setAttribute(RequestParameter.LANG, lang);
        return page;
    }
}
