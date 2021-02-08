package com.example.demo_web.command;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.impl.EmptyCommand;
import com.example.demo_web.command.CommandEnum;
import com.example.demo_web.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;

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
            sessionRequestContent.setSessionAttribute("wrongAction", action
                    + MessageManager.getProperty("message.wrongaction"));
        }
        return current;
    }
}
