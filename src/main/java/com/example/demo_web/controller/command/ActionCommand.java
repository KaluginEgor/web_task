package com.example.demo_web.controller.command;

public interface ActionCommand {
    CommandResult execute(SessionRequestContent sessionRequestContent);
}
