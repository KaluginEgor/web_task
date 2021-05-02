package com.epam.project.controller.command.impl.common.page;


import com.epam.project.controller.command.*;

/**
 * The type Open login page command.
 */
public class OpenLoginPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.REDIRECT);
        return commandResult;
    }
}
