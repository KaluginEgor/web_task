package com.example.demo_web.command.impl;

import com.example.demo_web.command.*;

public class LogoutCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.FORWARD);
        //request.getSession().invalidate();
        return commandResult;
    }
}
