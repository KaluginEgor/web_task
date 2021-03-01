package com.example.demo_web.command.impl.page;

import com.example.demo_web.command.*;

public class RegistrationPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.REGISTRATION, TransitionType.FORWARD);
        return commandResult;
    }
}
