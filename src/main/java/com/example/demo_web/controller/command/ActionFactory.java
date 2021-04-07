package com.example.demo_web.controller.command;

import com.example.demo_web.controller.command.impl.EmptyCommand;

public class ActionFactory {
    public ActionCommand defineCommand(SessionRequestContent sessionRequestContent) {
        ActionCommand current = new EmptyCommand();

        String action = sessionRequestContent.getRequestParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }

        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
            current = currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            // wrong action
        }
        return current;
    }
}
