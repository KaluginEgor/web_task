package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.CommandResult;
import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.command.TransitionType;
import com.example.demo_web.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class RegistrationPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String page = ConfigurationManager.getProperty("path.page.registration");
        CommandResult commandResult = new CommandResult(page, TransitionType.FORWARD);
        return commandResult;
    }
}
