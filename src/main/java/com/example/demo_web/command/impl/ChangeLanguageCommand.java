package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.RequestParameter;

import javax.servlet.http.HttpServletRequest;

public class ChangeLanguageCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        String lang = request.getParameter(RequestParameter.LANG);
        request.getSession().setAttribute(RequestParameter.LANG, lang);
        String page = request.getParameter("page");
        return page;
    }
}
