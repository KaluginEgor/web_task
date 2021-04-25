package com.example.demo_web.controller.command;

import com.example.demo_web.controller.command.impl.EmptyCommand;

public class ActionFactory {
    private static final String COMMAND = "command";

    public ActionCommand defineCommand(SessionRequestContent sessionRequestContent) {
        ActionCommand current = new EmptyCommand();

        String action = sessionRequestContent.getRequestParameter(COMMAND);
        if (action == null || action.isEmpty()) {
            return current;
        }

        try {
            CommandName currentEnum = CommandName.valueOf(action.toUpperCase());
            current = currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            // wrong action
        }
        return current;
    }
}
