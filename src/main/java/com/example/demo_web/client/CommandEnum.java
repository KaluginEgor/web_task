package com.example.demo_web.client;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.impl.LoginCommand;
import com.example.demo_web.command.impl.LogoutCommand;
import com.example.demo_web.command.impl.RegisterCommand;
import com.example.demo_web.command.impl.RegistrationPageCommand;

public enum CommandEnum {
    LOGIN {
        {
            this.command = new LoginCommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutCommand();
        }
    },
    REGISTER {
        {
            this.command = new RegisterCommand();
        }
    },
    REGISTRATION_PAGE {
        {
            this.command = new RegistrationPageCommand();
        }
    };
    ActionCommand command;
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
