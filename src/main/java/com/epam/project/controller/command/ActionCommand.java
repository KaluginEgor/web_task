package com.epam.project.controller.command;

import com.epam.project.exception.CommandException;

public interface ActionCommand {
    CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException;
}
