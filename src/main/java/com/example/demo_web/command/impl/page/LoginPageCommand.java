package com.example.demo_web.command.impl.page;

import com.example.demo_web.command.*;

public class LoginPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.FORWARD);
        return commandResult;
    }
}
