package com.example.demo_web.controller.command.impl.common.page;


import com.example.demo_web.controller.command.*;

public class OpenRegistrationPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.REGISTRATION, TransitionType.REDIRECT);
        return commandResult;
    }
}
