package com.example.demo_web.controller.command;

import com.example.demo_web.exception.CommandException;
import com.example.demo_web.exception.ServiceException;

public interface ActionCommand {
    CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException;
}
