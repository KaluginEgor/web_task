package com.epam.project.controller.command.impl.common.page;


import com.epam.project.controller.command.*;

/**
 * The type Open registration page command.
 */
public class OpenRegistrationPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.REGISTRATION, TransitionType.REDIRECT);
        return commandResult;
    }
}
