package com.epam.project.controller.command.impl.common;


import com.epam.project.controller.command.*;


/**
 * The type Change locale command.
 */
public class ChangeLocaleCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String page = sessionRequestContent.getRequestParameter(RequestParameter.CURRENT_PAGE);
        String lang = sessionRequestContent.getRequestParameter(RequestParameter.LANG);
        sessionRequestContent.setSessionAttribute(Attribute.LANG, lang);
        CommandResult commandResult = new CommandResult(page, TransitionType.FORWARD);
        return commandResult;
    }
}
