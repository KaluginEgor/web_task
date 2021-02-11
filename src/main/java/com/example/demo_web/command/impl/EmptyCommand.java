package com.example.demo_web.command.impl;

import com.example.demo_web.command.*;

public class EmptyCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.FORWARD);
        return commandResult;
    }
}
