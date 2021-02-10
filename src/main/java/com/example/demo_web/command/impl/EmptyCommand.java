package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.CommandResult;
import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.command.TransitionType;
import com.example.demo_web.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class EmptyCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String page = ConfigurationManager.getProperty("path.page.login");
        CommandResult commandResult = new CommandResult(page, TransitionType.FORWARD);
        return commandResult;
    }
}
