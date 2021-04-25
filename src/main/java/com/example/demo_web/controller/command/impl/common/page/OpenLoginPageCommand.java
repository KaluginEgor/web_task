package com.example.demo_web.controller.command.impl.common.page;


import com.example.demo_web.controller.command.*;

public class OpenLoginPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.REDIRECT);
        return commandResult;
    }
}
