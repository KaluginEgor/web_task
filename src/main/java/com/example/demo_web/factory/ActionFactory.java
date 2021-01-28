package com.example.demo_web.factory;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.impl.EmptyCommand;
import com.example.demo_web.command.CommandEnum;
import com.example.demo_web.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {
    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new EmptyCommand();

        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }

        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
            current = currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            request.setAttribute("wrongAction", action
                    + MessageManager.getProperty("message.wrongaction"));
        }
        return current;
    }
}