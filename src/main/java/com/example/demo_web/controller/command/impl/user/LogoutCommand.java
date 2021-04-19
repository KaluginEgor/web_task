package com.example.demo_web.controller.command.impl.user;


import com.example.demo_web.controller.command.*;

public class LogoutCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.REDIRECT);
        sessionRequestContent.invalidate();
        return commandResult;
    }
}
