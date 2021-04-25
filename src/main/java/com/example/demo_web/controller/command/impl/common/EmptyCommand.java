package com.example.demo_web.controller.command.impl.common;


import com.example.demo_web.controller.command.*;

public class EmptyCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.FORWARD);
        return commandResult;
    }
}
