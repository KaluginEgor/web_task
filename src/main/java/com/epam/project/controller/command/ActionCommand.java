package com.epam.project.controller.command;

import com.epam.project.exception.CommandException;

/**
 * The interface Action command.
 */
public interface ActionCommand {
    /**
     * Execute command result.
     *
     * @param sessionRequestContent the session request content
     * @return the command result
     * @throws CommandException the command exception
     */
    CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException;
}
