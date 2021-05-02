package com.epam.project.controller.command.impl.user;


import com.epam.project.controller.command.*;

/**
 * The type Logout command.
 */
public class LogoutCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.REDIRECT);
        sessionRequestContent.invalidate();
        return commandResult;
    }
}
